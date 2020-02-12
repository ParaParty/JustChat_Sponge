package com.superexercisebook.justchat.client;

import com.google.common.collect.ImmutableMap;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
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
import java.util.List;


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
        String raw = MessageTools.Base64Decode(obj.getString("content"));
        UrlDetector parser = new UrlDetector(raw, UrlDetectorOptions.HTML);
        List<Url> detectUrl = parser.detect();

        Text.Builder retText = Text.builder();

        int plainTextStart = 0;
        int i = 0;  // Original Content Pointer
        for (Url nowUrl : detectUrl) {
            String originalUrl = nowUrl.getOriginalUrl();

            // next array
            int[] next = new int[originalUrl.length()];
            getNext(originalUrl, next);

            // match
            int j = 0;
            for (j = 0; (i < raw.length()) && (j < originalUrl.length()); ) {
                if ((j == -1) || (raw.charAt(i) == originalUrl.charAt(j))) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            }

            /// current plain Text
            ///     Start  plainTextStart
            ///     End    i - originalUrl.length()
            ///     Length i - originalUrl.length() - plainTextStart
            if (i - originalUrl.length() - plainTextStart > 0) {
                String part = raw.substring(plainTextStart, i - originalUrl.length());
                Text partText = textConfig.messageFormat().text().apply(ImmutableMap.of(
                        "CONTENT", Text.of(part)
                )).build();
                retText.append(partText);
            }

            /// current URL
            ///     Start  i - originalUrl.length()
            ///     End    i
            ///     Length originalUrl.length()
            String part = raw.substring(i - originalUrl.length(), i);

            URL url = null;
            try {
                url = new URL(nowUrl.toString());
            } catch (Exception ignored) {

            }

            Text partText;

            if (url == null) {
                partText = textConfig.messageFormat().URL().apply(ImmutableMap.of(
                        "CONTENT", Text.of(part)
                )).build();
            } else {
                ClickAction a = TextActions.openUrl(url);
                partText = textConfig.messageFormat().URL().apply(ImmutableMap.of(
                        "CONTENT", Text.of(part)
                )).onClick(a).build();
            }

            retText.append(partText);


            plainTextStart = i;

        }


        if (i < raw.length()) {
            String part = raw.substring(i, raw.length());
            Text partText = textConfig.messageFormat().text().apply(ImmutableMap.of(
                    "CONTENT", Text.of(part)
            )).build();
            retText.append(partText);
        }

        return retText.build();
    }

    private void getNext(String pattern, int[] next) {
        int i = 0;
        int k = -1;
        next[0] = -1;

        for (i = 0; i < pattern.length()-1; ) {
            if ((k == -1) || (pattern.charAt(i) == pattern.charAt(k))) {
                i++;
                k++;
                if (pattern.charAt(k) != pattern.charAt(i)) next[i] = k;
                else next[i] = next[k];
            } else k = next[k];
        }

    }
}
