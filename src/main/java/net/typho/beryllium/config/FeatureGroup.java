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

public class FeatureGroup implements FeatureGroupChild, Iterable<FeatureGroupChild> {
    public final Identifier id;
    public final String name;
    public final FeatureGroup parent;
    public final List<FeatureGroup> subGroups = new LinkedList<>();
    public final List<Feature<?>> features = new LinkedList<>();
    public final ItemStack icon;

    public FeatureGroup(ItemStack icon, Identifier id, FeatureGroupChild... features) {
        this.icon = icon;
        this.id = id;
        this.name = id.getPath();
        this.parent = null;

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
    }

    public FeatureGroup(ItemStack icon, Constructor constructor, String name, FeatureGroupChild... features) {
        this.icon = icon;
        this.id = constructor.id(name);
        this.name = name;
        this.parent = null;

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
    }

    public FeatureGroup(ItemStack icon, FeatureGroup parent, String folder, FeatureGroupChild... features) {
        this.icon = icon;
        this.id = parent.id.withSuffixedPath("/" + folder);
        this.name = folder;
        this.parent = parent;

        parent.subGroups.add(this);

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
    }

    @Override
    public @NotNull Iterator<FeatureGroupChild> iterator() {
        return Iterators.concat(subGroups.iterator(), features.iterator());
    }

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public Text name() {
        return Text.translatable(id.toTranslationKey());
    }

    @Override
    public void click(ServerConfigScreen.Node node, double mouseX, double mouseY) {
        node.parent().pushTab(this);
    }

    @SuppressWarnings("unchecked")
    public <O> Feature<O> getFeature(Class<O> cls, String name) {
        return (Feature<O>) features.stream().filter(feature -> feature.name.equals(name)).findAny().orElse(null);
    }

    public FeatureGroup getFolder(String name) {
        return subGroups.stream().filter(group -> group.name.equals(name)).findAny().orElse(null);
    }

    public <O> Feature<O> getFeature(Class<O> cls, String... path) {
        if (path.length == 1) {
            return getFeature(cls, path[0]);
        }

        FeatureGroup found = getFolder(path[0]);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFeature(cls, subPath);
    }

    public FeatureGroup getFolder(String... path) {
        if (path.length == 1) {
            return getFolder(path[0]);
        }

        FeatureGroup found = subGroups.stream().filter(group -> group.name.equals(path[0])).findAny().orElse(null);

        if (found == null) {
            return null;
        }

        String[] subPath = new String[path.length - 1];
        System.arraycopy(path, 1, subPath, 0, subPath.length);
        return found.getFolder(subPath);
    }

    @Override
    public void add(FeatureGroup group) {
        group.subGroups.add(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName() + " " + id.toString());

        for (FeatureGroup subGroup : subGroups) {
            builder.append('\n').append(subGroup);
        }

        for (Feature<?> feature : features) {
            builder.append('\n').append(feature);
        }

        return builder.toString();
    }
}
