package net.typho.beryllium.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.family.BlockFamily;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(BlockStateModelGenerator.class)
public interface VariantPoolFunctionsAccessor {
    @Accessor("VARIANT_POOL_FUNCTIONS")
    static Map<BlockFamily.Variant, BiConsumer<BlockStateModelGenerator.BlockTexturePool, Block>> getVariantPools() {
        throw new UnsupportedOperationException();
    }
}
