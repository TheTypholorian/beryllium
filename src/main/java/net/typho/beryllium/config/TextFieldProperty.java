package net.typho.beryllium.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.typho.beryllium.util.Constructor;

public abstract class TextFieldProperty<O> extends Property<O> {
    public TextFieldProperty(ServerConfig config, String category, String name, ArgumentType<O> argumentType, O value) {
        super(config, category, name, argumentType, value);
    }

    public TextFieldProperty(ServerConfig config, Constructor cons, String name, ArgumentType<O> argumentType, O value) {
        super(config, cons, name, argumentType, value);
    }

    public TextFieldProperty(ServerConfig config, String name, ArgumentType<O> argumentType, O value) {
        super(config, name, argumentType, value);
    }

    @Override
    public void addWidget(GridWidget.Adder adder, int rows) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        Text text = display();
        adder.add(new TextWidget(renderer.getWidth(text), 22, text, renderer).alignLeft());
        TextFieldWidget field = new TextFieldWidget(renderer, 0, 0, 44, 20, display());
        field.setText(value.toString());
        field.setTextPredicate(this::textPredicate);
        field.setChangedListener(this::set);
        adder.add(
                field,
                adder.copyPositioner().alignRight()
        );
    }

    public boolean textPredicate(String s) {
        try {
            argumentType.parse(new StringReader(s));
            return true;
        } catch (CommandSyntaxException e) {
            return false;
        }
    }
}
