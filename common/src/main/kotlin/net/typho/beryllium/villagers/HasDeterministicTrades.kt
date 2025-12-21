package net.typho.beryllium.villagers

import net.minecraft.util.RandomSource

interface HasDeterministicTrades {
    fun `beryllium$getTradeRandom`(): RandomSource
}