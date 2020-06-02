package tk.batcraft627.examplemod.examplemod.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import tk.batcraft627.examplemod.examplemod.tileEntity.FirstBlockTile;

public class ModBlocks {

    @ObjectHolder("examplemod:firstblock")
    public static FirstBlock FIRSTBLOCK;

    @ObjectHolder("examplemod:firstblock")
    public static TileEntityType<FirstBlockTile> FIRSTBLOCK_TILE;

}
