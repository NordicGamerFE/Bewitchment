package com.bewitchment.client.core;

import java.awt.Color;
import java.util.ArrayList;

import com.bewitchment.api.hotbar.IHotbarAction;
import com.bewitchment.api.ritual.EnumGlyphType;
import com.bewitchment.api.spell.ISpell;
import com.bewitchment.api.state.StateProperties;
import com.bewitchment.client.ResourceLocations;
import com.bewitchment.client.core.event.GirdleOfTheWoodedHUD;
import com.bewitchment.client.core.event.MimicEventHandler;
import com.bewitchment.client.core.event.MiscEventHandler;
import com.bewitchment.client.core.event.RenderingHacks;
import com.bewitchment.client.core.event.WerewolfEventHandler;
import com.bewitchment.client.core.hud.BloodViewerHUD;
import com.bewitchment.client.core.hud.EnergyHUD;
import com.bewitchment.client.core.hud.ExtraBarButtonsHUD;
import com.bewitchment.client.core.hud.HudController;
import com.bewitchment.client.core.hud.MoonHUD;
import com.bewitchment.client.core.hud.SelectedActionHUD;
import com.bewitchment.client.core.hud.VampireBloodBarHUD;
import com.bewitchment.client.fx.ParticleF;
import com.bewitchment.client.gui.GuiTarots;
import com.bewitchment.client.handler.ColorPropertyHandler;
import com.bewitchment.client.handler.ItemCandleColorHandler;
import com.bewitchment.client.handler.Keybinds;
import com.bewitchment.client.handler.ModelHandler;
import com.bewitchment.client.render.entity.layer.MantleLayer;
import com.bewitchment.client.render.entity.renderer.EmptyRenderer;
import com.bewitchment.client.render.entity.renderer.RenderAlphaHellhound;
import com.bewitchment.client.render.entity.renderer.RenderBatSwarm;
import com.bewitchment.client.render.entity.renderer.RenderBlackDog;
import com.bewitchment.client.render.entity.renderer.RenderBlindworm;
import com.bewitchment.client.render.entity.renderer.RenderBrewArrow;
import com.bewitchment.client.render.entity.renderer.RenderBrewBottle;
import com.bewitchment.client.render.entity.renderer.RenderBroom;
import com.bewitchment.client.render.entity.renderer.RenderDemon;
import com.bewitchment.client.render.entity.renderer.RenderDemoness;
import com.bewitchment.client.render.entity.renderer.RenderHellhound;
import com.bewitchment.client.render.entity.renderer.RenderImp;
import com.bewitchment.client.render.entity.renderer.RenderLizard;
import com.bewitchment.client.render.entity.renderer.RenderNewt;
import com.bewitchment.client.render.entity.renderer.RenderOwl;
import com.bewitchment.client.render.entity.renderer.RenderRaven;
import com.bewitchment.client.render.entity.renderer.RenderSnake;
import com.bewitchment.client.render.entity.renderer.RenderToad;
import com.bewitchment.client.render.entity.renderer.RenderUran;
import com.bewitchment.client.render.entity.renderer.SpellRenderer;
import com.bewitchment.client.render.tile.TileRenderCauldron;
import com.bewitchment.client.render.tile.TileRenderGemBowl;
import com.bewitchment.client.render.tile.TileRenderPlacedItem;
import com.bewitchment.common.Bewitchment;
import com.bewitchment.common.block.ModBlocks;
import com.bewitchment.common.block.misc.BlockWitchFire;
import com.bewitchment.common.content.cauldron.BrewData;
import com.bewitchment.common.content.tarot.TarotHandler.TarotInfo;
import com.bewitchment.common.core.proxy.ISidedProxy;
import com.bewitchment.common.core.statemappers.AllDefaultModelStateMapper;
import com.bewitchment.common.entity.EntityAoE;
import com.bewitchment.common.entity.EntityBatSwarm;
import com.bewitchment.common.entity.EntityBrew;
import com.bewitchment.common.entity.EntityBrewArrow;
import com.bewitchment.common.entity.EntityFlyingBroom;
import com.bewitchment.common.entity.EntityLingeringBrew;
import com.bewitchment.common.entity.EntitySpellCarrier;
import com.bewitchment.common.entity.living.animals.EntityBlindworm;
import com.bewitchment.common.entity.living.animals.EntityLizard;
import com.bewitchment.common.entity.living.animals.EntityNewt;
import com.bewitchment.common.entity.living.animals.EntityOwl;
import com.bewitchment.common.entity.living.animals.EntityRaven;
import com.bewitchment.common.entity.living.animals.EntitySnake;
import com.bewitchment.common.entity.living.animals.EntityToad;
import com.bewitchment.common.entity.spirits.demons.EntityDemon;
import com.bewitchment.common.entity.spirits.demons.EntityDemoness;
import com.bewitchment.common.entity.spirits.demons.EntityHellhound;
import com.bewitchment.common.entity.spirits.demons.EntityHellhoundAlpha;
import com.bewitchment.common.entity.spirits.demons.EntityImp;
import com.bewitchment.common.entity.spirits.demons.EntityUran;
import com.bewitchment.common.entity.spirits.ghosts.EntityBlackDog;
import com.bewitchment.common.item.ModItems;
import com.bewitchment.common.item.magic.ItemSpellPage;
import com.bewitchment.common.lib.LibGui;
import com.bewitchment.common.lib.LibMod;
import com.bewitchment.common.tile.tiles.TileEntityCauldron;
import com.bewitchment.common.tile.tiles.TileEntityGemBowl;
import com.bewitchment.common.tile.tiles.TileEntityPlacedItem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This class was created by <Arekkuusu> on 26/02/2017.
 * It's distributed as part of Bewitchment under
 * the MIT license.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements ISidedProxy {

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) {
		unifyVariants(ModBlocks.ember_grass);
		unifyVariants(ModBlocks.leaves_cypress);
		unifyVariants(ModBlocks.leaves_elder);
		unifyVariants(ModBlocks.leaves_juniper);
		unifyVariants(ModBlocks.leaves_yew);
		unifyVariants(ModBlocks.gem_bowl);
		unifyVariants(ModBlocks.moonbell);
		ModelHandler.registerModels();
	}

	@SubscribeEvent
	public static void stitchEventPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(ResourceLocations.STEAM);
		event.getMap().registerSprite(ResourceLocations.BEE);
		event.getMap().registerSprite(ResourceLocations.FLAME);
		event.getMap().registerSprite(ResourceLocations.GRAY_WATER);
		event.getMap().registerSprite(ResourceLocations.BAT);
	}

	private static void unifyVariants(Block block) {
		ModelLoader.setCustomStateMapper(block, new AllDefaultModelStateMapper(block));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		registerRenders();

		HudController.registerNewComponent(new BloodViewerHUD());
		HudController.registerNewComponent(new EnergyHUD());
		HudController.registerNewComponent(new MoonHUD());
		HudController.registerNewComponent(new SelectedActionHUD());
		HudController.registerNewComponent(ExtraBarButtonsHUD.INSTANCE);
		HudController.registerNewComponent(new VampireBloodBarHUD());

		MinecraftForge.EVENT_BUS.register(new GirdleOfTheWoodedHUD());
		MinecraftForge.EVENT_BUS.register(new WerewolfEventHandler());
		MinecraftForge.EVENT_BUS.register(new RenderingHacks());
		MinecraftForge.EVENT_BUS.register(new MiscEventHandler(Minecraft.getMinecraft()));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Keybinds.registerKeys();
		RenderPlayer defaultRP = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default");
		defaultRP.addLayer(new MantleLayer(defaultRP.getMainModel()));
		RenderPlayer slimRP = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim");
		slimRP.addLayer(new MantleLayer(slimRP.getMainModel()));
		BlockColors blocks = Minecraft.getMinecraft().getBlockColors();
		// Block Colors
		blocks.registerBlockColorHandler(ColorPropertyHandler.INSTANCE, ModBlocks.candle_medium, ModBlocks.candle_small, ModBlocks.candle_medium_lit, ModBlocks.candle_small_lit, ModBlocks.lantern, ModBlocks.revealing_lantern);

		blocks.registerBlockColorHandler(new IBlockColor() {

			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				return state.getValue(BlockWitchFire.TYPE).getColor();
			}
		}, ModBlocks.witchfire);

		blocks.registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				EnumGlyphType type = state.getValue(StateProperties.GLYPH_TYPE);
				switch (type) {
					case ENDER:
						return 0x770077;
					case GOLDEN:
						return 0xe3dc3c;
					case NETHER:
						return 0xbb0000;
					default:
					case NORMAL:
						return 0xFFFFFF;
					case ANY:
						return 0x00FF00; // A green one should never happen!
				}
			}
		}, ModBlocks.ritual_glyphs);

		blocks.registerBlockColorHandler(new IBlockColor() {

			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				return (worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : -1);
			}
		}, ModBlocks.spanish_moss, ModBlocks.spanish_moss_end);

		blocks.registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				if (tintIndex == 1) {
					return Color.HSBtoRGB((pos.getX() + pos.getY() + pos.getZ()) % 50 / 50f, 0.4f, 1f);
				}
				return -1;
			}
		}, ModBlocks.crystal_ball);

		ItemColors items = Minecraft.getMinecraft().getItemColors();
		// Item Colors
		items.registerItemColorHandler(new ItemCandleColorHandler(), Item.getItemFromBlock(ModBlocks.candle_medium), Item.getItemFromBlock(ModBlocks.candle_small));

		items.registerItemColorHandler(new IItemColor() {

			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (tintIndex == 0) {
					ISpell s = ItemSpellPage.getSpellFromItemStack(stack);
					if (s != null)
						return s.getColor();
				}
				return -1;
			}
		}, ModItems.spell_page);

		items.registerItemColorHandler(new IItemColor() {

			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (tintIndex == 0) {
					return BrewData.fromStack(stack).getColor();
				}
				return -1;
			}
		}, ModItems.brew_phial_drink, ModItems.brew_phial_linger, ModItems.brew_phial_splash, ModItems.brew_arrow);

		items.registerItemColorHandler(ColorPropertyHandler.INSTANCE, Item.getItemFromBlock(ModBlocks.lantern), Item.getItemFromBlock(ModBlocks.revealing_lantern));

		ClientCommandHandler.instance.registerCommand(new ClientCommandGuiConfig());
	}

	@Override
	public void spawnParticle(ParticleF particleF, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... args) {
		if (doParticle()) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particleF.newInstance(x, y, z, xSpeed, ySpeed, zSpeed, args));
		}
	}

	private boolean doParticle() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			return false;

		float chance = 1F;
		if (Minecraft.getMinecraft().gameSettings.particleSetting == 1)
			chance = 0.6F;
		else if (Minecraft.getMinecraft().gameSettings.particleSetting == 2)
			chance = 0.2F;

		return chance == 1F || Math.random() < chance;
	}

	private void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellCarrier.class, SpellRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBroom.class, RenderBroom::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBatSwarm.class, RenderBatSwarm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBrewArrow.class, RenderBrewArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBrew.class, RenderBrewBottle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLingeringBrew.class, EmptyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAoE.class, EmptyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityOwl.class, RenderOwl::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySnake.class, RenderSnake::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlindworm.class, RenderBlindworm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLizard.class, RenderLizard::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNewt.class, RenderNewt::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityToad.class, RenderToad::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRaven.class, RenderRaven::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityUran.class, RenderUran::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHellhound.class, RenderHellhound::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHellhoundAlpha.class, RenderAlphaHellhound::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlackDog.class, RenderBlackDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDemon.class, RenderDemon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDemoness.class, RenderDemoness::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, RenderImp::new);
		MinecraftForge.EVENT_BUS.register(new RenderBatSwarm.PlayerHider());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new TileRenderCauldron());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGemBowl.class, new TileRenderGemBowl());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlacedItem.class, new TileRenderPlacedItem());
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics;
	}

	@Override
	public void handleTarot(ArrayList<TarotInfo> tarots) {
		EntityPlayer p = Minecraft.getMinecraft().player;
		p.openGui(Bewitchment.instance, LibGui.TAROT.ordinal(), p.world, (int) p.posX, (int) p.posY, (int) p.posZ);
		if (Minecraft.getMinecraft().currentScreen instanceof GuiTarots) {
			Minecraft.getMinecraft().addScheduledTask(() -> ((GuiTarots) Minecraft.getMinecraft().currentScreen).loadData(tarots));
		} else {
			GuiTarots gt = new GuiTarots();
			Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(gt));
			Minecraft.getMinecraft().addScheduledTask(() -> gt.loadData(tarots));
		}
	}

	@Override
	public void loadActionsClient(ArrayList<IHotbarAction> actions, EntityPlayer player) {
		if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getUniqueID() == player.getUniqueID()) {
			ExtraBarButtonsHUD.INSTANCE.setList(actions);
		}
	}

	@Override
	public boolean isPlayerInEndFire() {
		Minecraft mc = Minecraft.getMinecraft();
		IBlockState ibs = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
		boolean res = (ibs.getBlock() == ModBlocks.witchfire && ibs.getValue(BlockWitchFire.TYPE) == BlockWitchFire.EnumFireType.ENDFIRE);
		return res;
	}

	@Override
	public void stopMimicking(EntityPlayer p) {
		MimicEventHandler.stopMimicking((AbstractClientPlayer) p);
	}
	
	@Override
	public void registerTexture(Fluid fluid)
	{
		StateMapper mapper = new StateMapper(fluid);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(fluid.getBlock()));
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(fluid.getBlock()), mapper);
		ModelLoader.setCustomStateMapper(fluid.getBlock(), mapper);
	}
	
	private static class StateMapper extends StateMapperBase implements ItemMeshDefinition
	{
		private final ModelResourceLocation location;
		
		public StateMapper(Fluid fluid)
		{
			this.location = new ModelResourceLocation(new ResourceLocation(LibMod.MOD_ID, "fluid"), fluid.getName());
		}
		
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state)
		{
			return location;
		}
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			return location;
		}
	}
}
