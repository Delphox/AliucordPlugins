package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.discord.api.channel.Channel;
import com.discord.utilities.permissions.PermissionUtils;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3;
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowHiddenChannels extends Plugin {

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3", Collections.singletonList("invoke"));
        return map;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Show hidden channels in servers";
        manifest.version = "0.1.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch("com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3", "invoke", (_this, args, ret) -> {
            WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3 lambda3 = (WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3) _this;
            WidgetChannelListModel.Companion.TextLikeChannelData textLikeChannelData = WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1.invoke$default(lambda3.$getTextLikeChannelData$1, lambda3.$channel, lambda3.$muted, null, 4, null);

            Channel channel = lambda3.$channel;
            String channelName = channel.l();

            if (!channelName.contains(" (Hidden)") && !PermissionUtils.INSTANCE.hasAccess(lambda3.$channel, lambda3.$permissions)) {
                try {
                    Field nameField = channel.getClass().getDeclaredField("name");
                    nameField.setAccessible(true);
                    nameField.set(channel, channelName + " (Hidden)");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return new ChannelListItemTextChannel(channel, textLikeChannelData.getSelected(), textLikeChannelData.getMentionCount(), textLikeChannelData.getUnread(), lambda3.$muted, textLikeChannelData.getLocked(), lambda3.$channelsWithActiveThreads$inlined.contains(Long.valueOf(lambda3.$channel.g())));
        });
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}