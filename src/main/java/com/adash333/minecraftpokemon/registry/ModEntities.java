package com.adash333.minecraftpokemon.registry;

import com.adash333.minecraftpokemon.MinecraftPokemonMod;
import com.adash333.minecraftpokemon.entity.DragonPokemonEntity;
import com.adash333.minecraftpokemon.entity.MewBlockEntity;
import com.adash333.minecraftpokemon.entity.MonsterBallEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinecraftPokemonMod.MOD_ID);

    public static final RegistryObject<EntityType<MewBlockEntity>> MEW_BLOCK = ENTITY_TYPES.register("mew_block",
            () -> EntityType.Builder.of(MewBlockEntity::new, MobCategory.CREATURE)
                    .sized(0.7F, 1.05F)
                    .clientTrackingRange(8)
                    .build("mew_block"));

    public static final RegistryObject<EntityType<DragonPokemonEntity>> DRAGON_POKEMON = ENTITY_TYPES.register("dragon_pokemon",
            () -> EntityType.Builder.of(DragonPokemonEntity::new, MobCategory.CREATURE)
                    .sized(1.1F, 1.8F)
                    .clientTrackingRange(8)
                    .build("dragon_pokemon"));

    public static final RegistryObject<EntityType<MonsterBallEntity>> MONSTER_BALL = ENTITY_TYPES.register("monster_ball",
            () -> EntityType.Builder.<MonsterBallEntity>of(MonsterBallEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("monster_ball"));

    private ModEntities() {
    }
}
