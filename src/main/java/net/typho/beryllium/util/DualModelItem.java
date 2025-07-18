package net.typho.beryllium.util;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public interface DualModelItem {
    Identifier worldModel();

    Identifier guiModel();

    void transform(ModelTransformationMode renderMode, BakedModel model, MatrixStack matrices);
}
