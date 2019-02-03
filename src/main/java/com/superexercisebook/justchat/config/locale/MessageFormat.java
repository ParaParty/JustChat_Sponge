package com.superexercisebook.justchat.config.locale;

import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;

public class MessageFormat {
    @Setting(comment = "The overview output format.")
    private TextTemplate overview = of(
            "[*]",TextColors.DARK_GREEN,
            arg("SENDER").color(TextColors.DARK_GREEN),
            TextColors.RESET,": ",
            arg("BODY")
    );

    @Setting(comment = "The normal text display format.")
    private TextTemplate text = of(
            arg("CONTENT")
    );

    @Setting(comment = "The URL text display format.")
    private TextTemplate URL = of(
            arg("CONTENT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    @Setting(comment = "The AT(@) display format.")
    private TextTemplate at = of(
            arg("TARGET").color(TextColors.BLUE)
    );

    @Setting(comment = "The face display format.")
    private String faceURL = "https://exercisebook.github.io/JustChat/resource/CQ/face/{ID}.{EXTENSION}";

    @Setting(comment = "The face display format.")
    private TextTemplate face = of(
            arg("CONTENT")
    );

    @Setting(comment = "The image display format.")
    private TextTemplate image = of(
            arg("CONTENT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    @Setting(comment = "The red envelope display format.")
    private TextTemplate redEnvelope = of(
            arg("TITLE").color(TextColors.RED).style(TextStyles.UNDERLINE)
    );

    @Setting(comment = "The share display format.")
    private TextTemplate share = of(
            arg("TITLE").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    @Setting(comment = "The rich message display format.")
    private TextTemplate rich = of(
            arg("TEXT").color(TextColors.BLUE).style(TextStyles.UNDERLINE)
    );

    public TextTemplate overview(){
        return overview;
    }

    public TextTemplate text(){
        return text;
    }

    public TextTemplate URL(){
        return URL;
    }

    public TextTemplate at(){
        return at;
    }

    public String faceURL(){
        return faceURL;
    }

    public TextTemplate facee(){
        return at;
    }

    public TextTemplate image() {
        return image;
    }

    public TextTemplate redEnvelope() {
        return redEnvelope;
    }

    public TextTemplate share() {
        return share;
    }

    public TextTemplate rich() {
        return rich;
    }

}
