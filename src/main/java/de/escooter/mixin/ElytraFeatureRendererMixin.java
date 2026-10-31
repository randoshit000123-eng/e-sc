package de.escooter.mixin;

import de.escooter.EScooterClient;
import de.escooter.ScooterRiding;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Ersetzt die Flügel-Darstellung der Elytra durch den E-Scooter (solange nicht gegleitet wird). */
@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void escooter$renderScooter(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                        BipedEntityRenderState state, float limbAngle, float limbDistance,
                                        CallbackInfo ci) {
        if (!ScooterRiding.active(state)) {
            return; // beim Gleiten: normale Elytra
        }
        ci.cancel();

        float wheelRot = state.limbFrequency * ScooterRiding.WHEEL_FACTOR;
        float steer = MathHelper.clamp(state.yawDegrees, -45.0f, 45.0f) * 0.4f * MathHelper.RADIANS_PER_DEGREE;

        matrices.push();
        matrices.translate(0.0f, 1.5f, 0.0f); // Boden (Modellursprung liegt auf Schulterhöhe, Boden = 24px tiefer)
        EScooterClient.model().render(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(EScooterClient.TEXTURE)),
                light, OverlayTexture.DEFAULT_UV, wheelRot, steer);
        matrices.pop();
    }
}
