package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;

public class CraftVillagerZombie extends CraftZombie implements ZombieVillager {

    public CraftVillagerZombie(CraftServer server, net.minecraft.world.entity.monster.ZombieVillager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.ZombieVillager getHandle() {
        return (net.minecraft.world.entity.monster.ZombieVillager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVillagerZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return Villager.Profession.valueOf(BuiltInRegistries.VILLAGER_PROFESSION.getKey(getHandle().getVillagerData().getProfession()).getPath().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        Validate.notNull(profession);
        getHandle().setVillagerData(getHandle().getVillagerData().setProfession(BuiltInRegistries.VILLAGER_PROFESSION.get(new ResourceLocation(profession.name().toLowerCase(Locale.ROOT)))));
    }

    @Override
    public Villager.Type getVillagerType() {
        return Villager.Type.valueOf(BuiltInRegistries.VILLAGER_TYPE.getKey(getHandle().getVillagerData().getType()).getPath().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerType(Villager.Type type) {
        Validate.notNull(type);
        getHandle().setVillagerData(getHandle().getVillagerData().setType(BuiltInRegistries.VILLAGER_TYPE.get(CraftNamespacedKey.toMinecraft(type.getKey()))));
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");

        return getHandle().villagerConversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().villagerConversionTime = -1;
            getHandle().getEntityData().set(net.minecraft.world.entity.monster.ZombieVillager.DATA_CONVERTING_ID, false);
            getHandle().conversionStarter = null;
            getHandle().removeEffect(MobEffects.DAMAGE_BOOST, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.CONVERSION);
        } else {
            getHandle().startConverting((UUID) null, time);
        }
    }

    @Override
    public OfflinePlayer getConversionPlayer() {
        return (getHandle().conversionStarter == null) ? null : Bukkit.getOfflinePlayer(getHandle().conversionStarter);
    }

    @Override
    public void setConversionPlayer(OfflinePlayer conversionPlayer) {
        if (!this.isConverting()) return;
        getHandle().conversionStarter = (conversionPlayer == null) ? null : conversionPlayer.getUniqueId();
    }
}
