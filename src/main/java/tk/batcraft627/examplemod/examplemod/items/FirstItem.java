package tk.batcraft627.examplemod.examplemod.items;

import net.minecraft.item.Item;
import tk.batcraft627.examplemod.examplemod.Examplemod;

public class FirstItem extends Item {
    public FirstItem(){
        super(new Item.Properties()
                .maxStackSize(1)
                .group(Examplemod.setup.itemGroup)
        );
        setRegistryName("firstitem");
    }
}
