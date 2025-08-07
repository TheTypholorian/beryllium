package net.typho.beryllium.util;

import net.minecraft.util.Identifier;

public interface DualModelItem {
    Identifier worldModel();

    Identifier guiModel();
}
