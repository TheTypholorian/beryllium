package net.typho.beryllium.tiers

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.typho.beryllium.Beryllium
import java.awt.Color

enum class Tier(
    val color: Color
) : StringRepresentable {
    WOOD(Color(117, 88, 33)),
    STONE(Color(149, 145, 141)),
    IRON(Color(216, 216, 216)),
    GOLD(Color(222, 177, 45)),
    DIAMOND(Color(51, 235, 203));

    fun getText(): Component = Component.translatable("tier.${name.lowercase()}")
        .withColor(color.rgb)

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