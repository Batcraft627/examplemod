package tk.batcraft627.examplemod.examplemod.tileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tk.batcraft627.examplemod.examplemod.Containers.FirstBlockContainer;
import tk.batcraft627.examplemod.examplemod.tools.CustomEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.plaf.basic.BasicComboBoxUI;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;
import static tk.batcraft627.examplemod.examplemod.blocks.ModBlocks.FIRSTBLOCK_TILE;

public class FirstBlockTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider{

    private LazyOptional<IItemHandler> handler = LazyOptional.of (this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of (this::createEnergy);

    private int counter;
    public FirstBlockTile() {
        super(FIRSTBLOCK_TILE);
    }

    @Override
    //This adds an action for every tick
    public void tick() {
        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energy.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(1000));
            }
        } else{
            handler.ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(0);
                if (stack.getItem() == Items.DIAMOND){
                    h.extractItem(0,1,false);
                    counter = 20;
                }
            });
        }
    }

    @Override
    public void read(CompoundNBT tag){
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h ->((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
        CompoundNBT energyTag = tag.getCompound("energy");
        energy.ifPresent(h ->((INBTSerializable<CompoundNBT>)h).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag){
        handler.ifPresent(h ->{
        CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
        tag.put("inv", compound);
        });
        energy.ifPresent(h ->{
        CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
        tag.put("energy", compound);
        });
        return super.write(tag);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1){
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return stack.getItem() == Items.DIAMOND;
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot,@Nonnull ItemStack stack, boolean simulate){
                    if (stack.getItem() != Items.DIAMOND){
                        return stack;
                    }
                    return super.insertItem(slot,stack,simulate);
                }
            };
        }

    private IEnergyStorage createEnergy (){
        return new CustomEnergyStorage(1000000,0);
    }


    @Nonnull
    @Override
    public <T>LazyOptional<T> getCapability(@Nonnull Capability<T> cap , @Nullable Direction side){
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY){
            return energy.cast();
        }
            return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new FirstBlockContainer(i,world,pos,playerInventory,playerEntity);
    }

}

