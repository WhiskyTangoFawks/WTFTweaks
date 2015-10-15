package wtftweaks.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import wtfcore.api.BlockSets;
import wtfcore.utilities.LoadBlockSets;
import wtftweaks.configs.WTFTweaksConfig;
import wtftweaks.entities.WTFcreeper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CustomExplosion extends Explosion{

	World world;
	int originX;
	int originY;
	int originZ;
	float baseStr;
	Random random = new Random();
	Entity sourceEntity;
	boolean isSmoking = false;

	int top;
	int bottom;
	int north;
	int south;
	int east;
	int west;

	int counterMod;


	float motionFactor = 2F;
	protected Map<Entity, Vec3> affectedPlayers = new HashMap<Entity, Vec3>();
	public HashSet<ChunkPosition> allBlocks = new HashSet<ChunkPosition>();

	public CustomExplosion(Entity entity, World world, int x, int y, int z, float str){
		super(world, entity, x, y, z, str);
		this.world=world;
		this.originX=x;
		this.originY=y;
		this.originZ=z;
		this.baseStr = str;
		this.sourceEntity = entity;
		this.counterMod = 0;

		populateAffectedBlocksList();
		affectEntitiesWithin();
		doExplosionB();
	}

	HashSet<ChunkPosition> toAtomize = new HashSet<ChunkPosition>();
	HashSet<ChunkPosition> toDrop = new HashSet<ChunkPosition>();
	HashSet<ChunkPosition> toFracture = new HashSet<ChunkPosition>();


	/**
	 * Populates the affectedBlocksList with any blocks that should be affected by this explosion
	 */
	protected void populateAffectedBlocksList() {

		//These define the boundaries of the cube that the explosion happens within- and are set based on the actual explosion
		top = 0;
		bottom = 0;
		north = 0;
		south = 0;
		east = 0;
		west = 0;

		//I need to set these based on the maximum dimensions of hte explosion- which is the distance it would travel through air



		float xpos = getModifier(world.getBlock(originX+1, originY, originZ));
		float xneg = getModifier(world.getBlock(originX-1, originY, originZ));
		float ypos = getModifier(world.getBlock(originX, originY+1, originZ));
		float yneg = getModifier(world.getBlock(originX, originY-1, originZ));
		float zpos = getModifier(world.getBlock(originX, originY, originZ+1));
		float zneg = getModifier(world.getBlock(originX, originY, originZ-1));


		float ftotal = xpos+xneg+ypos+yneg+zpos+zneg;

		xpos = setModifier(xpos, ftotal) * baseStr;
		xneg = setModifier(xneg, ftotal) * baseStr;
		ypos = setModifier(ypos, ftotal) * baseStr;
		yneg = setModifier(yneg, ftotal) * baseStr;
		zpos = setModifier(zpos, ftotal) * baseStr;
		zneg = setModifier(zneg, ftotal) * baseStr;

		if (sourceEntity instanceof WTFcreeper){
			yneg = yneg/2;
		}

		int xMin = -4-MathHelper.floor_float(xneg);
		int xMax = 4+MathHelper.floor_float(xpos);
		int yMin =-4-MathHelper.floor_float(yneg);
		int yMax = 4+MathHelper.floor_float(ypos);
		int zMin = -4-MathHelper.floor_float(zneg);
		int zMax = 4+MathHelper.floor_float(zpos);;

		//check adjacent blocks, and modify directional strength
		//add here

		for (int xloop = xMin; xloop < xMax+1; xloop++) {
			for (int yloop = yMin; yloop < yMax+1; yloop++) {
				for (int zloop = zMin; zloop < zMax+1; zloop++) {

					//This checks if it's an edge of the cube
					if (xloop == xMin || xloop == xMax || yloop == yMin || yloop == yMax || zloop == zMin || zloop == zMax)
					{
						//the values to increment along the ray each loop
						double incX = (0 + xloop);
						double incY = (0 + yloop);
						double incZ = (0 + zloop);

						//length of the vector
						double vectorLength = Math.sqrt(incX * incX + incY * incY + incZ * incZ);

						//setting the values
						incX /= (vectorLength);
						incY /= (vectorLength);
						incZ /= (vectorLength);

						//explosion origin- 0.5 makes it so that it starts in the center of a block
						double currentX = originX + 0.5;
						double currentY = originY + 0.5;
						double currentZ = originZ + 0.5;

						float xcomp;
						float ycomp;
						float zcomp;

						//selecting between pos and neg vector strength components
						if (xloop > 0){xcomp = xpos;}
						else if (xloop < 0){xcomp = xneg;}
						else {xcomp = 0F;}

						if (zloop > 0){zcomp = zpos;}
						else if (zloop < 0){zcomp = zneg;}
						else {zcomp = 0F;}

						if (yloop > 0){ycomp = ypos;}
						else if (yloop < 0){ycomp = yneg;}
						else {ycomp = 0F;}

						float absIncX = MathHelper.abs((float)incX);
						float absIncY = MathHelper.abs((float)incY);
						float absIncZ = MathHelper.abs((float)incZ);

						double total = absIncX + absIncY + absIncZ;

						//setting the vector strength, as a sum of each component times the corresponding increment,

						double vector = (xcomp*(absIncX/total) + ycomp*(absIncY/total) + zcomp*(absIncZ/total)) * (0.7 + random.nextFloat()*0.6);


						//
						float attenuation = 0.75F;

						for (double loop = 0.3; vector > 0; vector -= loop * attenuation ){
							int x = MathHelper.floor_double(currentX);
							int y = MathHelper.floor_double(currentY);
							int z = MathHelper.floor_double(currentZ);

							Block block = world.getBlock(x, y, z);
							ChunkPosition chunkposition = new ChunkPosition(x,y,z);
							//affectedBlockPositions.add(chunkposition);
							float resistance = block.getExplosionResistance(sourceEntity, world, originX, originY, originZ, x, y, z);

							if (vector> resistance*7){
								toAtomize.add(chunkposition);
								vector = vector - 2*resistance; //again
							}
							else if (vector > resistance){
								toDrop.add(chunkposition);
								vector = vector - resistance;
							}
							else if (WTFTweaksConfig.explosionFractures){
								toFracture.add(chunkposition);
								vector = vector - resistance/3;
							}
							currentX += incX;
							currentY += incY;
							currentZ += incZ;

						}
						//this set checks where this ray ends, against the current max value in that direction
						//this is used later to set the bounding box in which to check for entities
						north = (currentX > north) ? MathHelper.ceiling_double_int(currentX) : north;
						south = (currentX < south) ? MathHelper.floor_double(currentX) : south;

						top = (currentY > top) ? MathHelper.ceiling_double_int(currentY) : top;
						bottom = (currentY < top) ? MathHelper.floor_double(currentY) : bottom;

						east = (currentZ > east) ? MathHelper.ceiling_double_int(currentZ) : east;
						west = (currentZ < west) ? MathHelper.floor_double(currentZ) : west;


					}
				}
			}
		}
		this.affectedBlockPositions.addAll(allBlocks);
	}

	/**
	 * Affects all entities within the explosion, causing damage if flagged to do so
	 */
	protected void affectEntitiesWithin() {

		List<?> list = world.getEntitiesWithinAABBExcludingEntity(sourceEntity, AxisAlignedBB.getBoundingBox(north, top, east, south, bottom, west));
		Vec3 vec3 = Vec3.createVectorHelper(originX, originY, originZ);

		int diameter = (MathHelper.abs_int(top-bottom) + MathHelper.abs_int(north-south) + MathHelper.abs_int(east-west))/3;

		for (int k2 = 0; k2 < list.size(); ++k2)
		{
			Entity entity = (Entity)list.get(k2);
			double d7 = entity.getDistance(originX, originY, originZ) / diameter;

			if (d7 <= 1.0D) {
				double d0 = entity.posX - originX;
				double d1 = entity.posY + entity.getEyeHeight() - originY;
				double d2 = entity.posZ - originZ;
				double d8 = MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

				if (d8 != 0.0D) {
					d0 /= d8;
					d1 /= d8;
					d2 /= d8;
					double d9 = world.getBlockDensity(vec3, entity.boundingBox);
					double d10 = (1.0D - d7) * d9;
					float amount =  ((int)((d10 * d10 + d10) / 2.0D * 8.0D * diameter + 1.0D));

					//if (entity.attackEntityFrom(getDamageSource(), amount) && isFlaming && !entity.isImmuneToFire()) {
					//	if (!scalesWithDistance || rand.nextFloat() < d10) {
					//		entity.setFire(burnTime);
					//	}
					//}

					double d11 = EnchantmentProtection.func_92092_a(entity, d10);
					entity.motionX += d0 * d11 * motionFactor;
					entity.motionY += d1 * d11 * motionFactor;
					entity.motionZ += d2 * d11 * motionFactor;

					if (entity instanceof EntityPlayer) {
						affectedPlayers.put(entity, Vec3.createVectorHelper(d0 * d10, d1 * d10, d2 * d10));
					}
				}
			}
		}
	}


	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */

	public void doExplosionB()
	{
		this.world.playSoundEffect(originX, originY, originZ, "random.explode", 4.0F, (1.0F + (random.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

		if (baseStr >= 2.0F && isSmoking) {
			world.spawnParticle("hugeexplosion", originX, originY, originZ, 1.0D, 0.0D, 0.0D);
		} else {
			world.spawnParticle("largeexplode", originX, originY, originZ, 1.0D, 0.0D, 0.0D);
		}

		Iterator<ChunkPosition> iterator;
		ChunkPosition chunkposition;
		int i, j, k;
		Block block;

			iterator = toAtomize.iterator();
			while (iterator.hasNext()) {
				chunkposition = iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = world.getBlock(i, j, k);
				world.setBlockToAir(i, j, k);
				spawnExtraParticles(i, j, k);
				block.onBlockExploded(world, i, j, k, this);
				if (LoadBlockSets.isExplosive(block))
				{
					//create new custom explosion here
					//WTFexplosion.createExplosion(sourceEntity, this.world, i, j, k, (float)BlockData.getExplosionSize(block), (float)BlockData.getExplosionSize(block), true);
				}
			}

			iterator = toDrop.iterator();
			while (iterator.hasNext()) {
				chunkposition = iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = world.getBlock(i, j, k);
				if (block.canDropFromExplosion(this)) {
					block.dropBlockAsItemWithChance(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F /  baseStr, 0);
				}

				block.onBlockExploded(world, i, j, k, this);
				if (LoadBlockSets.isExplosive(block))
				{
					//WTFexplosion.createExplosion(sourceEntity, this.world, i, j, k, (float)BlockData.getExplosionSize(block), (float)BlockData.getExplosionSize(block), true);
				}
			}
			iterator = toFracture.iterator();
			while (iterator.hasNext()) {
				chunkposition = iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = world.getBlock(i, j, k);
				FracMethods.fracStone(i, j, k, world);
			}


		/*
		if (isFlaming) {
			iterator = affectedBlockPositions.iterator();
			while (iterator.hasNext()) {
				chunkposition = (ChunkPosition)iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = worldObj.getBlock(i, j, k);
				Block block1 = worldObj.getBlock(i, j - 1, k);
				// func_149730_j() returns block.opaque
				if (block == Blocks.air && block1.func_149730_j() && rand.nextInt(3) == 0) {
					worldObj.setBlock(i, j, k, Blocks.fire);
				}
			}
		}
	*/
		notifyClients();
	}

	/**
	 * Actually explodes the block and spawns particles if allowed
	 */
	private void spawnExtraParticles(int i, int j, int k) {
			double d0 = i + world.rand.nextFloat();
			double d1 = j + world.rand.nextFloat();
			double d2 = k + world.rand.nextFloat();
			double d3 = d0 - originX;
			double d4 = d1 - originY;
			double d5 = d2 - originZ;
			double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
			d3 /= d6;
			d4 /= d6;
			d5 /= d6;
			double d7 = 0.5D / (d6 / baseStr + 0.1D);
			d7 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
			d3 *= d7;
			d4 *= d7;
			d5 *= d7;
			world.spawnParticle("explode", (d0 + originX * 1.0D) / 2.0D, (d1 + originY * 1.0D) / 2.0D, (d2 + originZ * 1.0D) / 2.0D, d3, d4, d5);
			world.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);

	}




	/** Returns map of affected players */

	@Override
	public Map<Entity, Vec3> func_77277_b() { return affectedPlayers; }

	/**
	 * Returns either the entity that placed the explosive block, the entity that caused the explosion,
	 * the entity that threw the entity that caused the explosion, or null.
	 */

	@Override
	public EntityLivingBase getExplosivePlacedBy() {
		return sourceEntity == null ? null : (sourceEntity instanceof EntityTNTPrimed ? ((EntityTNTPrimed) sourceEntity).getTntPlacedBy() :
			(sourceEntity instanceof EntityLivingBase ? (EntityLivingBase) sourceEntity : (sourceEntity instanceof EntityThrowable ? ((EntityThrowable) sourceEntity).getThrower() : null)));
	}

	protected void notifyClients() {
		if (!world.isRemote) {
			Iterator<?> iterator = world.playerEntities.iterator();
			while (iterator.hasNext()) {
				EntityPlayer player = (EntityPlayer) iterator.next();
				if (player.getDistanceSq(originX, originY, originZ) < 4096.0D) {
					((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S27PacketExplosion(originX, originY, originZ, baseStr, affectedBlockPositions, this.func_77277_b().get(player)));
				}
			}
		}
	}

	private float getModifier(Block block){
		if (BlockSets.explosiveModBlock.containsKey(block)){
			counterMod++;
			return BlockSets.explosiveModBlock.get(block);
		}
		else{
			//System.out.println(block.getUnlocalizedName());
			return 0;
		}
	}
	private float setModifier(float preset, float totalAssigned){
		if (preset != 0){
			return preset;
		}
		else {
			return (6F-totalAssigned)/(6F - counterMod);
		}

	}


}
