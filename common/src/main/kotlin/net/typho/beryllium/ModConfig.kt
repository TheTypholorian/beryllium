package net.typho.beryllium

import me.fzzyhmstrs.fzzy_config.annotations.Action
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction
import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.fzzyhmstrs.fzzy_config.api.FileType
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt

class ModConfig : Config(Beryllium.id("config")) {
    companion object {
        @JvmField
        val instance = ConfigApi.registerAndLoadConfig(::ModConfig)

        fun init() = Unit
    }

    override fun fileType() = FileType.JSON5

    @JvmField
    var crossbows = CrossbowsSection()

    class CrossbowsSection : ConfigSection() {
        @JvmField
        var burstCrossbow = BurstCrossbowSection()

        class BurstCrossbowSection() : ConfigSection() {
            @RequiresAction(Action.RESTART)
            @JvmField
            var enabled = true
            @RequiresAction(Action.RESTART)
            @JvmField
            var canLoadWindCharges = true
            @JvmField
            var velocityMultiplier = ValidatedFloat(0.5f, 10f, 0.1f)
            @JvmField
            var chargeDurationMultiplier = ValidatedFloat(2f, 10f, 0.5f)
            @JvmField
            var numProjectiles = ValidatedInt(3, 10, 1)
            @JvmField
            var spread = ValidatedInt(30, 100, 0)
        }
    }

    @JvmField
    var elytra = ElytraSection()

    class ElytraSection : ConfigSection() {
        @JvmField
        var rocketsEnabled = false
    }

    @JvmField
    var endCity = EndCitySection()

    class EndCitySection : ConfigSection() {
        @JvmField
        var shulkerLevitationTicks = ValidatedInt(50, 500, 20)
    }

    @RequiresAction(Action.RESTART)
    @JvmField
    var durabilityEnabled = false
    @RequiresAction(Action.RESTART)
    @JvmField
    var enchantmentsEnabled = false
    @JvmField
    var removedItems = RemovedItemsSection()

    class RemovedItemsSection : ConfigSection() {
        @RequiresAction(Action.RESTART)
        @JvmField
        var vanillaTieredGearEnabled = false
        @RequiresAction(Action.RESTART)
        @JvmField
        var maceEnabled = false
    }

    @JvmField
    var tooltips = TooltipsSection()

    class TooltipsSection : ConfigSection() {
        @JvmField
        var isMaterial = true
        @JvmField
        var usage = true
        @JvmField
        var nonConsumable = true
    }

    @JvmField
    var trident = TridentSection()

    class TridentSection : ConfigSection() {
        @JvmField
        var builtinRiptide = true
        @JvmField
        var riptideInRain = false
    }

    @JvmField
    var smithing = SmithingSection()

    class SmithingSection : ConfigSection() {
        @JvmField
        var templatesConsumable = false
        @RequiresAction(Action.RELOAD_DATA)
        @JvmField
        var templatesDuplicatable = true
    }

    @JvmField
    var villagers = VillagersSection()

    class VillagersSection : ConfigSection() {
        @JvmField
        var discountsEnabled = false
    }
}