package de.escooter;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Der E-Scooter als reines Code-Modell (keine Registrierung eines Model-Layers nötig).
 * Koordinaten in Pixeln, Ursprung = Boden unter dem Spieler, -Y = oben, -Z = vorne.
 * UV-Layout passt zu textures/entity/escooter.png (64x64).
 */
public final class ScooterModel {
    private final ModelPart root;
    private final ModelPart front;      // Lenkeinheit (dreht sich beim Lenken)
    private final ModelPart frontWheel;
    private final ModelPart rearWheel;

    public ScooterModel() {
        ModelData data = new ModelData();
        ModelPartData r = data.getRoot();

        r.addChild("deck", ModelPartBuilder.create().uv(0, 0).cuboid(-3f, -3f, -8f, 6f, 1f, 16f), ModelTransform.NONE);
        r.addChild("battery", ModelPartBuilder.create().uv(26, 40).cuboid(-2.5f, -5f, -4f, 5f, 2f, 8f), ModelTransform.NONE);
        r.addChild("rear_wheel", ModelPartBuilder.create().uv(0, 40).cuboid(-1f, -3f, -3f, 2f, 6f, 6f),
                ModelTransform.pivot(0f, -3f, 7f));

        ModelPartData f = r.addChild("front", ModelPartBuilder.create()
                        .uv(0, 18).cuboid(-1f, -17f, -1f, 2f, 14f, 2f)        // Lenksäule
                        .uv(10, 18).cuboid(-5f, -19f, -1f, 10f, 2f, 2f)       // Lenker
                        .uv(0, 54).cuboid(-1f, -16f, -2f, 2f, 2f, 1f),        // Scheinwerfer
                ModelTransform.pivot(0f, 0f, -7f));
        f.addChild("front_wheel", ModelPartBuilder.create().uv(0, 40).cuboid(-1f, -3f, -3f, 2f, 6f, 6f),
                ModelTransform.pivot(0f, -3f, 0f));

        this.root = TexturedModelData.of(data, 64, 64).createModel();
        this.front = root.getChild("front");
        this.frontWheel = front.getChild("front_wheel");
        this.rearWheel = root.getChild("rear_wheel");
    }

    /**
     * @param wheelRot Raddrehung in Radiant
     * @param steer    Lenkeinschlag in Radiant
     */
    public void render(MatrixStack matrices, VertexConsumer vc, int light, int overlay, float wheelRot, float steer) {
        frontWheel.pitch = wheelRot;
        rearWheel.pitch = wheelRot;
        front.yaw = steer;
        root.render(matrices, vc, light, overlay);
    }
}
