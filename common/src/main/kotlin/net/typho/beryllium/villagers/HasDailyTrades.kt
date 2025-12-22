package net.typho.beryllium.villagers

import net.minecraft.util.RandomSource

interface HasDailyTrades {
    fun `beryllium$getTradeRandom`(): RandomSource
}