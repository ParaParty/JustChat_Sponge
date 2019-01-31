package com.superexercisebook.justchat.config.locale;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;

@ConfigSerializable
public class Locale {
    @Setting(comment = "The overview output format")
    private TextTemplate MSGFormat_overview = of(
            TextColors.DARK_GREEN,"[*]",
            arg("SENDER").color(TextColors.DARK_GREEN),
            TextColors.RESET,": ",
            arg("BODY")
    );

    @Setting(comment = "The normal text display format")
    private TextTemplate MSGFormat_text = of(
            arg("CONTENT")
    );

    @Setting(comment = "The URL text display format")
    private TextTemplate MSGFormat_URL = of(
            arg("CONTENT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    @Setting(comment = "The AT(@) display format")
    private TextTemplate MSGFormat_at = of(
            arg("TARGET").color(TextColors.BLUE)
    );

    @Setting(comment = "The image display format")
    private TextTemplate MSGFormat_image = of(
            arg("CONTENT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    public TextTemplate MSGFormat_overview(){
        return MSGFormat_overview;
    }

    public TextTemplate MSGFormat_text(){
        return MSGFormat_text;
    }

    public TextTemplate MSGFormat_URL(){
        return MSGFormat_URL;
    }

    public TextTemplate MSGFormat_at(){
        return MSGFormat_at;
    }

    public TextTemplate MSGFormat_image(){
        return MSGFormat_image;
    }
}
