package net.typho.beryllium.mixin.client;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;
import net.typho.beryllium.client.BerylliumWorldCreationTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;tabs([Lnet/minecraft/client/gui/tab/Tab;)Lnet/minecraft/client/gui/widget/TabNavigationWidget$Builder;"
            )
    )
    private TabNavigationWidget.Builder tabs(TabNavigationWidget.Builder instance, Tab[] tabs) {
        Tab[] nTabs = new Tab[tabs.length + 1];

        System.arraycopy(tabs, 0, nTabs, 0, tabs.length);
        nTabs[tabs.length] = new BerylliumWorldCreationTab((CreateWorldScreen) (Object) this, Text.translatable("createWorld.tab.beryllium.title"));

        return instance.tabs(nTabs);
    }
}
