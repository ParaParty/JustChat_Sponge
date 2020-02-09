package com.superexercisebook.justchat.client;

import com.google.common.collect.ImmutableMap;
import com.superexercisebook.justchat.GlobalState;
import com.superexercisebook.justchat.config.locale.Locale;
import com.superexercisebook.justchat.client.packet.MessageTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;

import java.net.URL;


class MessageContentUnpacker {
    private JSONArray data;
    private Locale textConfig;

    MessageContentUnpacker(JSONArray s) {
        data = s;
        textConfig = GlobalState.config.getText();
    }

    Text toText() {

        try {

            Text.Builder result = Text.builder();

            for (int i = 0; i < data.length(); i++) {

                JSONObject obj = data.getJSONObject(i);
                String type = obj.getString("type");

                Text part = null;

                if (type.equals("text")) {
                    part = unpackText(obj);
                } else if (type.equals("cqcode")) {

                    String function = obj.getString("function");
                    switch (function) {
                        case "CQ:at": {
                            part = unpackCQCodeAt(obj);
                            break;
                        }
                        case "CQ:face": {
                            part = unpackCQCodeFace(obj);
                            break;
                        }
                        case "CQ:image": {
                            part = unpackCQCodeImage(obj);
                            break;
                        }
                        case "CQ:hb": {
                            part = unpackCQCodeHB(obj);
                            break;
                        }
                        case "CQ:share": {
                            part = unpackCQCodeShare(obj);
                            break;
                        }
                        case "CQ:rich": {
                            part = unpackCQCodeRich(obj);
                            break;
                        }
                    }

                }

                if (part != null) result.append(part);

            }

            //logger.info(result.build().toString());
            return result.build();

        } catch (JSONException e) {
            GlobalState.logger.error("Can not resolve this message", e);
            return Text.builder().build();
        }

    }

    private Text unpackCQCodeRich(JSONObject obj) {
        URL url = null;
        try {
            url = new URL(MessageTools.Base64Decode(obj.getString("url")));
        } catch (Exception ignored) {

        }

        Text ret;
        if (url == null) {
            ret = textConfig.messageFormat().rich().apply(ImmutableMap.of(
                    "TEXT", MessageTools.Base64Decode(obj.getString("text"))
            )).build();
        } else {
            ClickAction a = TextActions.openUrl(url);
            ret = textConfig.messageFormat().rich().apply(ImmutableMap.of(
                    "TEXT", MessageTools.Base64Decode(obj.getString("text"))
            )).onClick(a).build();
        }

        return ret;
    }

    private Text unpackCQCodeShare(JSONObject obj) {
        URL url = null;
        try {
            url = new URL(MessageTools.Base64Decode(obj.getString("url")));
        } catch (Exception ignored) {

        }

        Text ret;
        if (url == null) {
            ret = textConfig.messageFormat().share().apply(ImmutableMap.of(
                    "TITLE", MessageTools.Base64Decode(obj.getString("title")),
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).build();
        } else {
            ClickAction a = TextActions.openUrl(url);
            ret = textConfig.messageFormat().share().apply(ImmutableMap.of(
                    "TITLE", MessageTools.Base64Decode(obj.getString("title")),
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).onClick(a).build();
        }

        return ret;
    }

    private Text unpackCQCodeHB(JSONObject obj) {
        return textConfig.messageFormat().redEnvelope().apply(ImmutableMap.of(
                "TITLE", Text.of(MessageTools.Base64Decode(obj.getString("title")))
        )).build();
    }

    private Text unpackCQCodeImage(JSONObject obj) {
        URL url = null;
        try {
            url = new URL(obj.getString("url"));
        } catch (Exception ignored) {

        }

        Text ret;
        if (url == null) {
            ret = textConfig.messageFormat().image().apply(ImmutableMap.of(
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).build();

        } else {
            ClickAction a = TextActions.openUrl(url);
            ret = textConfig.messageFormat().image().apply(ImmutableMap.of(
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).onClick(a).build();
        }

        return ret;
    }

    private Text unpackCQCodeFace(JSONObject obj) {
        URL url = null;
        try {
            String strUrl = textConfig.messageFormat().faceURL().
                    replace("{ID}", String.valueOf(obj.getInt("id")));
            url = new URL(strUrl);
        } catch (Exception ignored) {

        }

        Text ret = null;
        if (url == null) {
            ret = textConfig.messageFormat().face().apply(ImmutableMap.of(
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).build();

        } else {
            ClickAction a = TextActions.openUrl(url);
            ret = textConfig.messageFormat().face().apply(ImmutableMap.of(
                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
            )).onClick(a).build();
        }

        return ret;
    }

    private Text unpackCQCodeAt(JSONObject obj) {
        return textConfig.messageFormat().at().apply(ImmutableMap.of(
                "TARGET", MessageTools.Base64Decode(obj.getString("target"))
        )).build();
    }

    private Text unpackText(JSONObject obj) {
        return textConfig.messageFormat().text().apply(ImmutableMap.of(
                "CONTENT", Text.of(MessageTools.Base64Decode(obj.getString("content")))
        )).build();
    }
}
