package net.typho.beryllium.config;

import com.google.common.collect.Iterators;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.typho.beryllium.util.Constructor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConfigOptionGroup implements ConfigOptionGroupChild, Iterable<ConfigOptionGroupChild> {
    public final Identifier id;
    public final String name;
    public final ConfigOptionGroup parent;
    public final List<ConfigOptionGroup> subGroups = new LinkedList<>();
    public final List<ConfigOption<?>> options = new LinkedList<>();
    public final ItemStack icon;

    public ConfigOptionGroup(ItemStack icon, Identifier id, ConfigOptionGroupChild... features) {
        this.icon = icon;
        this.id = id;
        this.name = id.getPath();
        this.parent = null;

        for (ConfigOptionGroupChild feature : features) {
            feature.add(this);
        }
    }

    public ConfigOptionGroup(ItemStack icon, Constructor constructor, String name, ConfigOptionGroupChild... features) {
        this.icon = icon;
        this.id = constructor.id(name);
        this.name = name;
        this.parent = null;

        for (ConfigOptionGroupChild feature : features) {
            feature.add(this);
        }
    }

    public ConfigOptionGroup(ItemStack icon, ConfigOptionGroup parent, String folder, ConfigOptionGroupChild... features) {
        this.icon = icon;
        this.id = parent.id.withSuffixedPath("/" + folder);
        this.name = folder;
        this.parent = parent;

        parent.subGroups.add(this);

        for (ConfigOptionGroupChild feature : features) {
            feature.add(this);
        }
    }

    @Override
    public @NotNull Iterator<ConfigOptionGroupChild> iterator() {
        return Iterators.concat(subGroups.iterator(), options.iterator());
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public Text name() {
        return Text.translatable(id.toTranslationKey("config").replace('/', '.'));
    }

    @Override
    public void click(ServerConfigScreen.Node node, double mouseX, double mouseY) {
        node.parent().pushTab(this);
    }

    @SuppressWarnings("unchecked")
    public <O> ConfigOption<O> getFeature(Class<O> cls, String name) {
        return (ConfigOption<O>) options.stream().filter(feature -> feature.name.equals(name)).findAny().orElse(null);
    }

    public ConfigOptionGroup getFolder(String name) {
        return subGroups.stream().filter(group -> group.name.equals(name)).findAny().orElse(null);
    }

    public <O> ConfigOption<O> getFeature(Class<O> cls, String... path) {
        if (path.length == 1) {
            return getFeature(cls, path[0]);
        }

        ConfigOptionGroup found = getFolder(path[0]);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFeature(cls, subPath);
    }

    public ConfigOptionGroup getFolder(String... path) {
        if (path.length == 1) {
            return getFolder(path[0]);
        }

        ConfigOptionGroup found = subGroups.stream().filter(group -> group.name.equals(path[0])).findAny().orElse(null);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFolder(subPath);
    }

    @Override
    public void add(ConfigOptionGroup group) {
        group.subGroups.add(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName() + " " + id.toString());

        for (ConfigOptionGroup subGroup : subGroups) {
            builder.append('\n').append(subGroup);
        }

        for (ConfigOption<?> option : options) {
            builder.append('\n').append(option);
        }

        return builder.toString();
    }
}
