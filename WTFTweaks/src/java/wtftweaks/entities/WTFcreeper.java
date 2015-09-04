package wtftweaks.entities;

import wtftweaks.configs.WTFTweaksConfig;
import wtftweaks.util.CustomExplosion;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WTFcreeper extends EntityCreeper
{
	 /**
     * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
     * weird)
     */
    private int lastActiveTime;
    /** The amount of time since the creeper was close enough to the player to ignite */
    private int timeSinceIgnited;
    private int fuseTime = 30;
    /** Explosion radius for this creeper. */
    private int explosionRadius = 3;
    private static final String __OBFID = "CL_00001684";
	public WTFcreeper(World p_i1733_1_) {
		super(p_i1733_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;

            if (this.func_146078_ca())
            {
                this.setCreeperState(1);
            }

            int i = this.getCreeperState();

            if (i > 0 && this.timeSinceIgnited == 0)
            {
                this.playSound("creeper.primed", 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;

            if (this.timeSinceIgnited < 0)
            {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime)
            {
                this.timeSinceIgnited = this.fuseTime;
                this.func_146077_cc();
            }
        }

        super.onUpdate();
    }


	private void func_146077_cc()
	    {

		 if (!this.worldObj.isRemote)
	        {
	            //boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

	            if (this.getPowered())
	            {
	            	CustomExplosion explosion = new CustomExplosion(this, this.worldObj, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ), WTFTweaksConfig.creeperPower);
	            }
	            else
	            {
	            	CustomExplosion explosion = new CustomExplosion(this, this.worldObj, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ), WTFTweaksConfig.creeperPower);
	            }

	            this.setDead();
	        }
	    }


}