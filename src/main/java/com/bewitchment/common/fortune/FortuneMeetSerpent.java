package com.bewitchment.common.fortune;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.registry.Fortune;
import com.bewitchment.common.entity.spirit.demon.EntitySerpent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class FortuneMeetSerpent extends Fortune {
	public FortuneMeetSerpent() {
		super(new ResourceLocation(Bewitchment.MODID, "meet_serpent"), true);
	}
	
	@Override
	public boolean apply(EntityPlayer player) {
		BlockPos pos = new BlockPos(player.posX + player.getRNG().nextGaussian() * 4, player.posY, player.posZ + player.getRNG().nextGaussian() * 4);
		EntitySerpent serpent = new EntitySerpent(player.world);
		if (player.world.isAirBlock(pos) && player.world.isAirBlock(pos.up()) && player.world.getBlockState(pos.down()).canEntitySpawn(serpent)) {
			serpent.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			serpent.onInitialSpawn(player.world.getDifficultyForLocation(pos), null);
			player.world.spawnEntity(serpent);
			if (player.getRNG().nextInt(10) < player.world.getDifficulty().ordinal()) serpent.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 900, 1));
			if (player.getRNG().nextInt(10) < player.world.getDifficulty().ordinal()) serpent.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 900, 1));
			if (player.getRNG().nextInt(10) < player.world.getDifficulty().ordinal()) serpent.addPotionEffect(new PotionEffect(MobEffects.SPEED, 900, 1));
			if (player.getRNG().nextInt(10) < player.world.getDifficulty().ordinal()) serpent.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 900, 1));
			return true;
		}
		return false;
	}
}