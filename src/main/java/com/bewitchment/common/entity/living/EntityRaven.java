package com.bewitchment.common.entity.living;

import com.bewitchment.Bewitchment;
import com.bewitchment.common.entity.util.ModEntityTameable;
import com.bewitchment.common.item.tool.ItemBoline;
import com.bewitchment.registry.ModObjects;
import com.bewitchment.registry.ModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class EntityRaven extends ModEntityTameable {
	public EntityRaven(World world) {
		this(world, new ResourceLocation(Bewitchment.MODID, "entities/raven"), Items.GOLD_NUGGET, ModObjects.silver_nugget);
	}
	
	protected int shearTimer;
	
	protected EntityRaven(World world, ResourceLocation lootTableLocation, Item... tameItems) {
		super(world, lootTableLocation, tameItems);
		setSize(0.4f, 0.4f);
		moveHelper = new EntityFlyHelper(this);
	}
	
	@Override
	protected PathNavigate createNavigator(World world) {
		PathNavigateFlying path = new PathNavigateFlying(this, world);
		path.setCanEnterDoors(true);
		path.setCanFloat(true);
		path.setCanOpenDoors(false);
		return path;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return ModSounds.RAVEN_CRY;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() instanceof ItemSeeds;
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && player.getHeldItem(hand).getItem() instanceof ItemBoline && shearTimer == 0 && !(this instanceof EntityOwl)) {
			InventoryHelper.spawnItemStack(world, posX, posY, posZ, new ItemStack(ModObjects.ravens_feather, 1 + world.rand.nextInt(3)));
			shearTimer = 12000;
			return true;
		}
		else return super.processInteract(player, hand);
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!onGround && motionY <= 0) motionY *= 0.6;
		if (shearTimer > 0) shearTimer--;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {
	}
	
	@Override
	protected void updateFallState(double y, boolean grounded, IBlockState state, BlockPos pos) {
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("shearTimer", shearTimer);
		super.writeEntityToNBT(tag);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		shearTimer = tag.getInteger("shearTimer");
		super.readEntityFromNBT(tag);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1);
		getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(1);
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, aiSit);
		tasks.addTask(2, new EntityAIMate(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / 2));
		tasks.addTask(2, new EntityAIAttackMelee(this, 0.5, false));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 5, 1));
		tasks.addTask(3, new EntityAIFollowParent(this, getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue()));
		tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue()));
		tasks.addTask(4, new EntityAIWander(this, getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue()));
		tasks.addTask(5, new EntityAIFollowOwnerFlying(this, getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue(), 2, 5));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
	}
}