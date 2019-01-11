package com.superexercisebook.justchat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;

import java.io.IOException;
import java.net.URL;

import static org.spongepowered.api.text.format.TextColors.*;

public class MessageContentUnpacker {
    JSONArray data;
    public Logger logger;

    public MessageContentUnpacker(String s){
        data = new JSONArray(s);
    }

    public Text toText(){

        Text.Builder result = Text.builder();

        for (int i=0;i<data.length();i++) {
            JSONObject obj=data.getJSONObject(i);

            //logger.info(obj.toString());

            if (obj.getString("type").equals("text")) {
                Text t = Text.builder(MessageTools.Base64Decode(obj.getString("content"))).build();
                result.append(t);
                //logger.info(t.toString());
            }
            else
            if (obj.getString("type").equals("cqcode")){
                if (obj.getString("function").equals("CQ:at")){
                    Text t = Text.builder(MessageTools.Base64Decode(obj.getString("target"))).color(BLUE).build();
                    result.append(t);
                    //logger.info(t.toString());
                }
                else
                if (obj.getString("function").equals("CQ:image")){
                    try {
                        URL url = new URL(obj.getString("url"));
                        ClickAction a = TextActions.openUrl(url);
                        Text t = Text.builder(MessageTools.Base64Decode(obj.getString("content"))).color(BLUE).onClick(a).build();
                        result.append(t);
                        //logger.info(t.toString());
                    }
                    catch(IOException e){
                        logger.error("Received a message with a invalid image declaration");
                    }
                }
            }
        }

        //logger.info(result.build().toString());
        return result.build();
    }
}
