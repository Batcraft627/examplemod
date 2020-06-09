package tk.batcraft627.examplemod.examplemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import tk.batcraft627.examplemod.examplemod.tileEntity.FirstBlockTile;

import javax.annotation.Nullable;

public class FirstBlock extends Block {

    public FirstBlock(){
    super(Properties.create(Material.IRON)
            .sound(SoundType.METAL)
            .hardnessAndResistance(2.0f)
            .lightValue(14));
    setRegistryName("firstblock");
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }

    @Nullable
    @Override
    //This makes the block a Tile Entity
    public TileEntity createTileEntity (BlockState state, IBlockReader world){
        return new FirstBlockTile();
    }

    @Override
    //This will change the way the block is facing depending on where the player
    //is stood
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack){

        if(entity !=null){
            world.setBlockState(pos, state.with(BlockStateProperties.FACING,getFacingFromEntity(pos,entity)),2);
        }
    }


    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
        Vec3d vec = entity.getPositionVec();
        return Direction.getFacingFromVector((float) (vec.x - clickedBlock.getX()), (float) (vec.y - clickedBlock.getY()), (float) (vec.z - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }

    @Override
    //This brings up the GUI when a player right clicks the block
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
       if(!world.isRemote){
           TileEntity tileEntity = world.getTileEntity(pos);
                   if(tileEntity instanceof INamedContainerProvider){
                       NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                   }
       }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }
}
