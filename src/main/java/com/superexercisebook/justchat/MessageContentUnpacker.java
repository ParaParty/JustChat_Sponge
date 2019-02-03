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

                    if (function.equals("CQ:at")) {

                        Text t = textConfig.messageFormat().at().apply(ImmutableMap.of(
                                "TARGET", MessageTools.Base64Decode(obj.getString("target"))
                        )).build();
                        result.append(t);
                        //logger.info(t.toString());
                    } else if (function.equals("CQ:face")) {
                        try {
                            String strUrl = textConfig.messageFormat().faceURL().
                                    replace("{ID}",String.valueOf(obj.getInt("id"))).
                                    replace("{EXTENSION}",obj.getString("extension"));
                            URL url = new URL(strUrl);
                            ClickAction a = TextActions.openUrl(url);
                            Text t = textConfig.messageFormat().facee().apply(ImmutableMap.of(
                                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                            )).onClick(a).build();
                            result.append(t);
                        } catch (IOException e) {
                            logger.error("Received a message with a invalid rich content declaration", e);
                        }
                    } else if (function.equals("CQ:image")) {
                        try {
                            URL url = new URL(obj.getString("url"));
                            ClickAction a = TextActions.openUrl(url);

                            Text t = textConfig.messageFormat().image().apply(ImmutableMap.of(
                                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                            )).onClick(a).build();
                            result.append(t);
                        } catch (IOException e) {
                            logger.error("Received a message with a invalid image declaration", e);
                        }
                    } else if (function.equals("CQ:hb")) {
                        Text t = textConfig.messageFormat().redEnvelope().apply(ImmutableMap.of(
                                "TITLE", Text.of(MessageTools.Base64Decode(obj.getString("title")))
                        )).build();
                        result.append(t);
                    } else if (function.equals("CQ:share")) {
                        try {
                            URL url = new URL(MessageTools.Base64Decode(obj.getString("url")));
                            ClickAction a = TextActions.openUrl(url);
                            Text t = textConfig.messageFormat().share().apply(ImmutableMap.of(
                                    "TITLE", MessageTools.Base64Decode(obj.getString("title")),
                                    "CONTENT", MessageTools.Base64Decode(obj.getString("content"))
                            )).onClick(a).build();
                            result.append(t);
                        } catch (IOException e) {
                            logger.error("Received a message with a invalid share declaration", e);
                        }
                    } else if (function.equals("CQ:rich")) {
                        try {
                            URL url = new URL(MessageTools.Base64Decode(obj.getString("url")));
                            ClickAction a = TextActions.openUrl(url);
                            Text t = textConfig.messageFormat().rich().apply(ImmutableMap.of(
                                    "TEXT", MessageTools.Base64Decode(obj.getString("text"))
                            )).onClick(a).build();
                            result.append(t);
                        } catch (IOException e) {
                            logger.error("Received a message with a invalid rich content declaration", e);
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
