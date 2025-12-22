package net.typho.beryllium.tiers

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.typho.beryllium.Beryllium

enum class Tier(
    val color: ChatFormatting
) : StringRepresentable {
    COMMON(ChatFormatting.GRAY),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.AQUA),
    EPIC(ChatFormatting.LIGHT_PURPLE),
    LEGENDARY(ChatFormatting.GOLD);

    fun getText(): Component = Component.translatable("tier.${name.lowercase()}")
        .withStyle(color)

    fun getTooltipBackground(): ResourceLocation = Beryllium.id("${name.lowercase()}_tier")

    override fun getSerializedName(): String {
        return name.lowercase()
    }

    companion object {
        val codec: Codec<Tier> = StringRepresentable.fromValues { Tier.entries.toTypedArray() }
        val packetCodec: StreamCodec<ByteBuf, Tier> = ByteBufCodecs.idMapper(
            { i -> Tier.entries[i] },
            { tier -> tier.ordinal }
        )

        fun pickTier(max: Tier, chance: Float, random: RandomSource): Tier {
            return max // TODO implement
        }
    }
}