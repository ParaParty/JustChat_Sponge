package com.superexercisebook.justchat.client;

import com.google.common.collect.ImmutableMap;
import com.superexercisebook.justchat.GlobalState;
import com.superexercisebook.justchat.client.packet.*;
import com.superexercisebook.justchat.client.packet.packer.Packer;
import com.superexercisebook.justchat.client.packet.packer.PlayerList;
import com.superexercisebook.justchat.client.packet.packer.Pulse;
import com.superexercisebook.justchat.client.packet.packer.Registration;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import java.nio.charset.StandardCharsets;

import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;

public class MessageHandler implements ISocketActionListener {

    private final IConnectionManager clientManager;
    private final OkSocketOptions.Builder okOptionsBuilder;

    private Pulse mPulseData = new Pulse();

    MessageHandler(IConnectionManager clientManager, OkSocketOptions.Builder okOptionsBuilder) {
        this.clientManager = clientManager;
        this.okOptionsBuilder = okOptionsBuilder;
    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String action) {
        GlobalState.logger.info("Connected to the server. Waiting for authorizing.");
        GlobalState.logger.info("Server information : " + connectionInfo.getIp() + ":" + connectionInfo.getPort());

        okOptionsBuilder.setPulseFrequency(20 * 1000);
        clientManager.getPulseManager().setPulseSendable(mPulseData).pulse();

        Registration Pack = new Registration();
        clientManager.send(Pack);
    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e) {
        GlobalState.logger.info("Fail to connect to the server.");
        GlobalState.logger.info("Server information : " + connectionInfo.getIp() + ":" + connectionInfo.getPort());
        if (e != null) {
            GlobalState.logger.error(e.getMessage());
        }
    }

    @Override
    public void onSocketIOThreadStart(String s) {

    }

    @Override
    public void onSocketIOThreadShutdown(String s, Exception e) {

    }

    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {

        if (Packer.isMessage(data.getHeadBytes())) {
            try {
                String str = new String(data.getBodyBytes(), StandardCharsets.UTF_8);

                //logger.info(str);

                JSONObject jsonObject = new JSONObject(str);
                int version = jsonObject.getInt("version");
                if (version == PacketType.PackVersion) {

                    int messageType = jsonObject.getInt("type");

                    if (messageType == PacketType.PULSE) {
                        clientManager.getPulseManager().feed();
                    } else if (messageType == PacketType.MESSAGE) {

                        String sender = MessageTools.Base64Decode(jsonObject.getString("sender"));
                        String world = jsonObject.getString("world");
                        String world_display = MessageTools.Base64Decode(jsonObject.getString("world_display"));
                        MessageContentUnpacker content = new MessageContentUnpacker(jsonObject.getJSONArray("content"));

                        Text Content = GlobalState.config.getText().messageFormat().overview().apply(ImmutableMap.of(
                                "SENDER", sender,
                                "WORLD", world,
                                "WORLD_DISPLAY", world_display,
                                "BODY", content.toText()
                        )).build();

                        MessageChannel.TO_ALL.send(Content);

                    } else if ((messageType == PacketType.PlayerList) && (jsonObject.getInt("subtype") == PacketType.PlayerListRequest)) {

                        PlayerList pack = new PlayerList(
                                jsonObject.getString("sender"),
                                jsonObject.getString("world"),
                                jsonObject.getString("world_display")
                        );
                        clientManager.send(pack);

                    } else if (messageType == PacketType.INFO) {

                    } else if (messageType == PacketType.REGISTRATION) {

                    } else {
                        GlobalState.logger.info("Received a message with an unrecognized type.");
                    }

                } else {
                    if (version > PacketType.PackVersion) {
                        GlobalState.logger.info("Received a message made by a higher-version server.");
                    } else {
                        GlobalState.logger.info("Received a message made by a lower-version server.");
                    }
                }
            } catch (JSONException e) {
                GlobalState.logger.error("Received an unrecognized message.", e);
            }


        }


    }

    @Override
    public void onSocketWriteResponse(ConnectionInfo connectionInfo, String s, ISendable iSendable) {

    }

    @Override
    public void onPulseSend(ConnectionInfo connectionInfo, IPulseSendable iPulseSendable) {

    }

    @Override
    public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e) {
        GlobalState.logger.info("Disconnected from the server.");
        GlobalState.logger.info("Server information : " + connectionInfo.getIp() + ":" + connectionInfo.getPort());
        if (e instanceof ReloadException) {
            clientManager.switchConnectionInfo(((ReloadException) e).getNewConnection());
            clientManager.connect();
        } else if (e != null) {
            GlobalState.logger.info(e.getMessage());
        }
    }


}
