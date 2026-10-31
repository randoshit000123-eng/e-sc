package de.escooter;

import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.Items;

/** Gemeinsame Logik: Wann wird ein Spieler als Scooter-Fahrer dargestellt? */
public final class ScooterRiding {
    /** Pixel, um die der Spieler über dem Boden steht (Deck-Oberseite). */
    public static final float RAISE = 3.0f;
    /** Raddrehung pro limbFrequency-Einheit (Radradius 3px, 1 Block = 16px). */
    public static final float WHEEL_FACTOR = 16.0f / 3.0f / 4.0f;

    private ScooterRiding() {}

    public static boolean active(BipedEntityRenderState s) {
        return s.equippedChestStack.isOf(Items.ELYTRA)
                && !s.isGliding
                && !s.hasVehicle
                && s.leaningPitch <= 0.0f;
    }
}
