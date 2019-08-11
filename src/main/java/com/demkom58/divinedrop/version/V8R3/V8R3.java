package com.demkom58.divinedrop.version.V8R3;

import com.demkom58.divinedrop.config.ConfigData;
import com.demkom58.divinedrop.DivineDrop;
import com.demkom58.divinedrop.ItemsHandler;
import com.demkom58.divinedrop.lang.Language;
import com.demkom58.divinedrop.version.Version;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class V8R3 implements Version {
    public static final String VERSION = "1.8.9";
    public static final String PATH = "minecraft/lang/%s.lang";

    private final DivineDrop plugin;
    private final ConfigData data;
    private final ItemsHandler logic;

    private V8R3() {
        this.plugin = null;
        this.data = null;
        this.logic = null;
    }

    public V8R3(@NotNull final DivineDrop plugin,
                @NotNull final ConfigData data,
                @NotNull final ItemsHandler logic) {
        this.plugin = plugin;
        this.data = data;
        this.logic = logic;
    }

    public static String langFormat(@NotNull final String locale) {
        final String[] lang = locale.split("_");

        if (lang.length == 1)
            return lang[0];

        return lang[0] + "_" + lang[1].toUpperCase();
    }

    @Override
    public String getI18NDisplayName(@Nullable ItemStack item) {
        if (item == null)
            return null;

        return getName(item);
    }

    @NotNull
    @Override
    public String getLangPath(@NotNull final String locale) {
        return String.format(PATH, langFormat(locale));
    }

    @NotNull
    @Override
    public Map<String, String> parseLang(@NotNull InputStream inputStream) throws IOException {
        return V8LangParser.parseLang(inputStream);
    }

    @NotNull
    @Override
    public String id() {
        return VERSION;
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new V8Listener(plugin, data, logic);
    }

    @Override
    public @NotNull String reformatLangCode(@NotNull final String localeCode) {
        return V8R3.langCode(localeCode);
    }

    private String getName(ItemStack bItemStack) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(bItemStack);
        String s = getLangNameNMS(itemStack);

        if (itemStack.getTag() != null && itemStack.getTag().hasKeyOfType("display", 10)) {
            net.minecraft.server.v1_8_R3.NBTTagCompound nbtTagCompound = itemStack.getTag().getCompound("display");

            if (nbtTagCompound.hasKeyOfType("Name", 8))
                s = nbtTagCompound.getString("Name");
        }
        return s;
    }

    private String getLangNameNMS(net.minecraft.server.v1_8_R3.ItemStack itemStack) {
        return Language.getInstance().getLocName(itemStack.getItem().e_(itemStack) + ".name").trim();
    }

    public static @NotNull String langCode(@NotNull final String localeCode) {
        String[] parts = localeCode.split("_", 2);
        return parts[0].toLowerCase() + "_" + parts[1].toUpperCase();
    }

}