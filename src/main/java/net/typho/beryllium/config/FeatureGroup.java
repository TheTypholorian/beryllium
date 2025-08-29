package net.typho.beryllium.config;

import net.minecraft.util.Identifier;
import net.typho.beryllium.util.Constructor;

import java.util.LinkedList;
import java.util.List;

public class FeatureGroup implements FeatureGroupChild {
    public final Identifier id;
    public final String name;
    public final FeatureGroup parent;
    public final List<FeatureGroup> subGroups = new LinkedList<>();
    public final List<Feature<?>> features = new LinkedList<>();

    public FeatureGroup(Identifier id, FeatureGroupChild... features) {
        this.id = id;
        this.name = id.getPath();
        this.parent = null;

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
    }

    public FeatureGroup(Constructor constructor, String name, FeatureGroupChild... features) {
        this.id = constructor.id(name);
        this.name = name;
        this.parent = null;

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
    }

    public FeatureGroup(FeatureGroup parent, String folder, FeatureGroupChild... features) {
        this.id = parent.id.withSuffixedPath("/" + folder);
        this.name = folder;
        this.parent = parent;

        parent.subGroups.add(this);

        for (FeatureGroupChild feature : features) {
            feature.add(this);
        }
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
