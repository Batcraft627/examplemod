package tk.batcraft627.examplemod.examplemod.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import tk.batcraft627.examplemod.examplemod.blocks.ModBlocks;

public class ModSetup {

    public ItemGroup itemGroup = new ItemGroup("examplemod") {
        @Override
        public ItemStack createIcon(){
            return new ItemStack(ModBlocks.FIRSTBLOCK);
        }
    };

    public void init(){

    }

}
