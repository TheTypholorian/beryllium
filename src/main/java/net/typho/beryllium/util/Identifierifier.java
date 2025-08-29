package net.typho.beryllium.util;

import net.minecraft.util.Identifier;

public interface Identifierifier {
    Identifier id(String name);

    default Identifier fileId(String name) {
        Identifier id = id(name);
        return Identifier.of(id.getNamespace(), id.getPath().replace('/', '_'));
    }
}
