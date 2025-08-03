package net.typho.beryllium.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.OptionalDynamic;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.typho.beryllium.util.Constructor;

public class BooleanProperty extends Property<Boolean> {
    public BooleanProperty(ServerConfig config, String category, String name, Boolean value) {
        super(config, category, name, BoolArgumentType.bool(), value);
    }

    public BooleanProperty(ServerConfig config, Constructor cons, String name, Boolean value) {
        super(config, cons, name, BoolArgumentType.bool(), value);
    }

    public BooleanProperty(ServerConfig config, String name, Boolean value) {
        super(config, name, BoolArgumentType.bool(), value);
    }

    @Override
    public void read(OptionalDynamic<?> dynamic) {
        value = dynamic.asBoolean(value);
    }

    @Override
    public void decode(ByteBuf buf) {
        value = buf.readBoolean();
    }

    @Override
    public NbtElement write(DynamicRegistryManager registries) {
        return NbtByte.of(value);
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBoolean(value);
    }

    @Override
    public void addWidget(GridWidget.Adder adder, int rows) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        Text text = display();
        adder.add(new TextWidget(renderer.getWidth(text), 22, text, renderer).alignLeft());
        adder.add(
                CyclingButtonWidget.onOffBuilder(value).omitKeyText().build(0, 0, 44, 20, Text.empty(), (button, value1) -> set(value1)),
                adder.copyPositioner().alignRight()
        );
    }
}
