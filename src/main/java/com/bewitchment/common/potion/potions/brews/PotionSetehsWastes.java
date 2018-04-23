package com.bewitchment.common.potion.potions.brews;

import com.bewitchment.api.cauldron.DefaultModifiers;
import com.bewitchment.api.cauldron.IBrewModifierList;
import com.bewitchment.common.potion.BrewMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.HashMap;
import java.util.Map;

public class PotionSetehsWastes extends BrewMod {
	public PotionSetehsWastes() {
		super("setehs_wastes", false, 0xD2691E, true, 0);
		stateMap.put(Blocks.SAND, Blocks.SAND.getStateFromMeta(1));
	}

	private final Map<Block, IBlockState> stateMap = new HashMap<>();

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		int box = 2 + modifiers.getLevel(DefaultModifiers.RADIUS).orElse(0);

		BlockPos posI = pos.add(box, box / 2, box);
		BlockPos posF = pos.add(-box, -box / 2, -box);

		Iterable<MutableBlockPos> spots = BlockPos.getAllInBoxMutable(posI, posF);
		for (BlockPos spot : spots) {
			if (spot.distanceSq(pos) < 2 + box * box / 2) {
				IBlockState state = world.getBlockState(spot);
				Block block = state.getBlock();
				if (block == Blocks.SANDSTONE_STAIRS) {
					IBlockState newState = Blocks.RED_SANDSTONE_STAIRS.getDefaultState()
							.withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING))
							.withProperty(BlockStairs.HALF, state.getValue(BlockStairs.HALF));
					world.setBlockState(spot, newState);
				} else if (stateMap.containsKey(block)) {
					world.setBlockState(spot, stateMap.get(block), 3);
				} else if (block == Blocks.SANDSTONE) {
					BlockSandStone.EnumType type = state.getValue(BlockSandStone.TYPE);
					IBlockState other = getSandStone(type);
					world.setBlockState(spot, other, 3);
				}
			}
		}
	}

	private IBlockState getSandStone(BlockSandStone.EnumType type) {
		IBlockState state = Blocks.RED_SANDSTONE.getDefaultState();
		return state.withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.byMetadata(type.getMetadata()));
	}
}