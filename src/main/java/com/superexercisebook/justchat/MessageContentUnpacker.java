package com.superexercisebook.justchat;

import com.google.common.collect.ImmutableMap;
import com.superexercisebook.justchat.config.locale.Locale;
import com.superexercisebook.justchat.pack.MessageTools;
import org.json.JSONArray;
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

        Text.Builder result = Text.builder();

        for (int i=0;i<data.length();i++) {
            JSONObject obj=data.getJSONObject(i);

            //logger.info(obj.toString());

            if (obj.getString("type").equals("text")) {

                Text t = textConfig.MSGFormat_text().apply(ImmutableMap.of(
                            "CONTENT",Text.of(MessageTools.Base64Decode(obj.getString("content")))

                )).build();
                result.append(t);
                //logger.info(t.toString());


            }
            else
            if (obj.getString("type").equals("cqcode")){
                if (obj.getString("function").equals("CQ:at")){

                    Text t = textConfig.MSGFormat_at().apply(ImmutableMap.of(
                            "TARGET",MessageTools.Base64Decode(obj.getString("target"))
                    )).build();
                    result.append(t);
                    //logger.info(t.toString());
                }
                else
                if (obj.getString("function").equals("CQ:image")){
                    try {
                        URL url = new URL(obj.getString("url"));
                        ClickAction a = TextActions.openUrl(url);

                        Text t = textConfig.MSGFormat_image().apply(ImmutableMap.of(
                                "CONTENT",MessageTools.Base64Decode(obj.getString("content"))
                        )).onClick(a).build();
                        result.append(t);
                        //logger.info(t.toString());
                    }
                    catch(IOException e){
                        logger.error("Received a message with a invalid image declaration",e);
                    }
                }
            }
        }

        //logger.info(result.build().toString());
        return result.build();
    }
}
