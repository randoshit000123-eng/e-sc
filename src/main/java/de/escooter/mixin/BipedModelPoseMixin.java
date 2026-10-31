package de.escooter.mixin;

import de.escooter.ScooterRiding;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fahrpose. Hängt an BipedEntityModel, damit Spielermodell UND Rüstungs-Layer dieselbe Pose bekommen.
 * Läuft am Ende von setAngles (nach den Vanilla-Animationen), also überschreiben wir nur das Nötige.
 */
@Mixin(BipedEntityModel.class)
public abstract class BipedModelPoseMixin {
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;

    @Inject(
            method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
            at = @At("TAIL")
    )
    private void escooter$pose(BipedEntityRenderState state, CallbackInfo ci) {
        if (!ScooterRiding.active(state)) {
            return;
        }
        // Weicher Übergang: limbAmplitudeMultiplier ist bereits interpoliert (0 = Stand, 1 = volle Fahrt)
        float speed = MathHelper.clamp(state.limbAmplitudeMultiplier, 0.0f, 1.0f);
        float lean = 0.05f + 0.17f * speed;

        // Spieler steht auf dem Deck, nicht im Boden
        float up = ScooterRiding.RAISE;
        head.pivotY -= up;
        body.pivotY -= up;
        rightArm.pivotY -= up;
        leftArm.pivotY -= up;
        rightLeg.pivotY -= up;
        leftLeg.pivotY -= up;

        // Oberkörper leicht nach vorn, Kopf gleicht aus
        body.pitch = lean;
        head.pitch -= lean * 0.8f;

        // Beide Hände am Lenker
        float reach = -0.85f - lean * 0.5f;
        rightArm.pitch = reach;
        leftArm.pitch = reach;
        rightArm.yaw = 0.0f;
        leftArm.yaw = 0.0f;
        rightArm.roll = 0.08f;
        leftArm.roll = -0.08f;

        // Fahrstellung: rechter Fuß vorne, linker hinten
        rightLeg.pitch = -0.25f;
        leftLeg.pitch = 0.30f;
        rightLeg.yaw = -0.08f;
        leftLeg.yaw = 0.08f;
        rightLeg.roll = 0.0f;
        leftLeg.roll = 0.0f;
        rightLeg.pivotZ -= 1.5f;
        leftLeg.pivotZ += 2.0f;
    }
}
