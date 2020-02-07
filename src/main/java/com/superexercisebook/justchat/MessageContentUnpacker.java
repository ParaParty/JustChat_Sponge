package com.superexercisebook.justchat;

import com.google.common.collect.ImmutableMap;
import com.superexercisebook.justchat.config.locale.Locale;
import com.superexercisebook.justchat.pack.MessageTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MessageContentUnpacker {
    JSONArray data;
    public Logger logger;
    public Locale textConfig;

    public MessageContentUnpacker(JSONArray s){
        data = s;
    }

    public Text toText(){

        try {

            Text.Builder result = Text.builder();

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                String type = obj.getString("type");
                //logger.info(obj.toString());

                if (type.equals("text")) {

                    Text t = textConfig.messageFormat().text().apply(ImmutableMap.of(
                            "CONTENT", Text.of(MessageTools.Base64Decode(obj.getString("content")))
                    )).build();
                    result.append(t);
                    //logger.info(t.toString());


                } else if (type.equals("cqcode")) {
                    String function = obj.getString("function");

                    switch (function) {
                        case "CQ:at": {
                            Text t = textConfig.messageFormat().at().apply(ImmutableMap.of(
                                    "TARGET", MessageTools.Base64Decode(obj.getString("target"))
                            )).build();
                            result.append(t);
                            //logger.info(t.toString());
                            break;
                        }
                        case "CQ:face": {
                            URL url = null;
                            try {
                                String strUrl = textConfig.messageFormat().faceURL().
                                        replace("{ID}", String.valueOf(obj.getInt("id")));
                                url = new URL(strUrl);
                            } catch (Exception ignored) {

                            }

                            Text t = null;
                            if (url == null) {
                                t = textConfig.messageFormat().face().apply(ImmutableMap.of(
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).build();

                            } else {
                                ClickAction a = TextActions.openUrl(url);
                                t = textConfig.messageFormat().face().apply(ImmutableMap.of(
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).onClick(a).build();
                            }
                            result.append(t);

                            break;
                        }
                        case "CQ:image": {
                            URL url = null;
                            try {
                                url = new URL(obj.getString("url"));
                            } catch (Exception ignored) {

                            }

                            Text t;
                            if (url == null) {
                                t = textConfig.messageFormat().image().apply(ImmutableMap.of(
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).build();

                            } else {
                                ClickAction a = TextActions.openUrl(url);
                                t = textConfig.messageFormat().image().apply(ImmutableMap.of(
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).onClick(a).build();
                            }

                            result.append(t);
                            break;
                        }
                        case "CQ:hb": {
                            Text t = textConfig.messageFormat().redEnvelope().apply(ImmutableMap.of(
                                    "TITLE", Text.of(MessageTools.Base64Decode(obj.getString("title")))
                            )).build();
                            result.append(t);
                            break;
                        }
                        case "CQ:share": {
                            URL url = null;
                            try {
                                url = new URL(MessageTools.Base64Decode(obj.getString("url")));
                            } catch (Exception ignored) {

                            }

                            Text t;
                            if (url == null) {
                                t = textConfig.messageFormat().share().apply(ImmutableMap.of(
                                        "TITLE", MessageTools.Base64Decode(obj.getString("title")),
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).build();
                            } else {
                                ClickAction a = TextActions.openUrl(url);
                                t = textConfig.messageFormat().share().apply(ImmutableMap.of(
                                        "TITLE", MessageTools.Base64Decode(obj.getString("title")),
                                        "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                                )).onClick(a).build();
                            }

                            result.append(t);
                            break;
                        }
                        case "CQ:rich": {
                            URL url = null;
                            try {
                                url = new URL(MessageTools.Base64Decode(obj.getString("url")));
                            } catch (Exception ignored) {

                            }

                            Text t;
                            if (url == null) {
                                t = textConfig.messageFormat().rich().apply(ImmutableMap.of(
                                        "TEXT", MessageTools.Base64Decode(obj.getString("text"))
                                )).build();
                            } else {
                                ClickAction a = TextActions.openUrl(url);
                                t = textConfig.messageFormat().rich().apply(ImmutableMap.of(
                                        "TEXT", MessageTools.Base64Decode(obj.getString("text"))
                                )).onClick(a).build();
                            }

                            result.append(t);
                            break;
                        }
                    }
                }
            }

            //logger.info(result.build().toString());
            return result.build();

        } catch (JSONException e){
            logger.error("Can not resolve this message", e);
            return Text.builder().build();
        }

    }
}
