package de.escooter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class EScooterClient implements ClientModInitializer {
    public static final String MOD_ID = "escooter";
    public static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/escooter.png");

    private static ScooterModel model;

    /** Lazy: ModelParts erst beim ersten Rendern bauen (Client sicher gestartet). */
    public static ScooterModel model() {
        if (model == null) {
            model = new ScooterModel();
        }
        return model;
    }

    @Override
    public void onInitializeClient() {
        // Eingebautes Resource Pack (Hotbar-Design, Item-Icon & Name). Aktiv, sobald die Mod geladen ist,
        // und in den Resource-Pack-Einstellungen als "E-Scooter Pack" sichtbar.
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(MOD_ID, "escooter_pack"),
                        container,
                        Text.literal("E-Scooter Pack"),
                        ResourcePackActivationType.ALWAYS_ENABLED));
    }
}
