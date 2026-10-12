package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinecraftPokemonMod.MOD_ID);
    public static final RegistryObject<MobEffect> ALLAY_FORM = EFFECTS.register("allay_form",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x7DDFFF) { });

    private ModEffects() { }
}
