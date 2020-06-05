package tk.batcraft627.examplemod.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.batcraft627.examplemod.examplemod.Containers.FirstBlockContainer;
import tk.batcraft627.examplemod.examplemod.blocks.FirstBlock;
import tk.batcraft627.examplemod.examplemod.blocks.ModBlocks;
import tk.batcraft627.examplemod.examplemod.items.FirstItem;
import tk.batcraft627.examplemod.examplemod.setup.ClientProxy;
import tk.batcraft627.examplemod.examplemod.setup.IProxy;
import tk.batcraft627.examplemod.examplemod.setup.ModSetup;
import tk.batcraft627.examplemod.examplemod.setup.ServerProxy;
import tk.batcraft627.examplemod.examplemod.tileEntity.FirstBlockTile;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")

public class Examplemod {

    //These are variables
    public static final String MODID = "examplemod";
    public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static ModSetup setup = new ModSetup();
    private static final Logger LOGGER = LogManager.getLogger();

    public Examplemod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
    setup.init();
    proxy.init();
    }
    // You can use EventBusSubscriber to automatically subscribe events on the contained class
    // (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new FirstBlock());
        }
        @SubscribeEvent
        //Registers the items that I have made
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties()
                    .group(setup.itemGroup);
            event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK,properties).setRegistryName("firstblock"));
            event.getRegistry().register(new FirstItem());
        }

        @SubscribeEvent
        //Registers the block as a tile entity
        public static void onTilEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event){
            event.getRegistry().register(TileEntityType.Builder.create(FirstBlockTile:: new,ModBlocks.FIRSTBLOCK).build(null)
            .setRegistryName("firstblock"));
        }

        @SubscribeEvent
        //Registers the container for the block
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event){
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
               return new FirstBlockContainer(windowId,Examplemod.proxy.getClientWorld(),pos,inv,Examplemod.proxy.getClientPlayer());
            }).setRegistryName("firstblock"));
        }
    }
}
