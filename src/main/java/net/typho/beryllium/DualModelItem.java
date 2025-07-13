package net.typho.beryllium;

import net.minecraft.util.Identifier;

public interface DualModelItem {
    Identifier worldModel();

    Identifier guiModel();
}
