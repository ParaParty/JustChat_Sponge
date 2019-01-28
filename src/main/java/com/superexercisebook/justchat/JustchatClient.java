package com.superexercisebook.justchat;

import com.google.common.collect.ImmutableMap;
import com.superexercisebook.justchat.pack.MessagePackType;
import com.superexercisebook.justchat.pack.Packer;
import com.superexercisebook.justchat.pack.MessageTools;
import com.superexercisebook.justchat.pack.Packer_Pulse;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.Text;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import com.superexercisebook.justchat.Settings;

public class JustchatClient extends Thread{

    final static byte[] MessageHeader = {0x11,0x45,0x14};
    //�����Զ������ͷ

    public ConnectionInfo info;
    public IConnectionManager clientManager;
    private OkSocketOptions.Builder okOptionsBuilder;

    public Logger logger;
    public Settings config;


    private Packer_Pulse mPulseData = new Packer_Pulse();

    @Override
    public void run(){

        info = new ConnectionInfo(config.getGeneral().server().ip(), config.getGeneral().server().port());
        logger.info("Target server: "+config.getGeneral().server().ip()+":"+config.getGeneral().server().port());

        //����OkSocket,����������ӵ�ͨ��,�õ�ͨ��Manager
        clientManager = OkSocket.open(info);

        okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength(){
                /*
                 * ���ز���Ϊ������ı���ͷ����(�ֽ���)��
                 * �����ص�ֵӦ���Ϸ������ĵ��еı���ͷ�Ĺ̶�����ֵ(�ֽ���),������Ҫ���̨ͬѧ�̶�
                 */
                return MessageHeader.length+4 /*�̶�����ͷ�ĳ���(�ֽ���)*/;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                /*
                 * �峤Ҳ��Ϊ��Ч�غɳ��ȣ�
                 * ��ֵӦ����Ϊ�������������header�ж�ȡ��
                 * �ӱ���ͷ����header�н�����Ч���س���ʱ�����ע������е�byteOrder��
                 * ����ǿ�ҽ�����ʹ��java.nio.ByteBuffer��������һ�㡣
                 * ����Ҫ������Ч�غɵĳ���,���ҷ��صĳ����в�Ӧ�ð�������ͷ�Ĺ̶�����
                 */
                /*
                String a= new String("");
                for (int i=0;i<7;i++){
                    a=a+(header[i]&0xff)+" ";
                }
                logger.info(a);

                logger.info("||" +(header[3]&0xff)*(2<<23)+""+
                        (header[4]&0xff)*(2<<15)+""+
                        (header[5]&0xff)*(2<<7)+""+
                        (header[6]&0xff));
                */
                //0 1 2 3 4 5 6
                /*if (Packer_Pulse.isPulse(header)){
                    return 0;
                }
                else*/ if (Packer.isMessage(header)){
                    int len = (header[3]&0xff)*(2<<23)+
                            (header[4]&0xff)*(2<<15)+
                            (header[5]&0xff)*(2<<7)+
                            (header[6]&0xff);
                    //logger.info(""+len);
                    return len /*��Ч���س���(�ֽ���)���̶�����ͷ����(�ֽ���)����*/;
                };
                return 0;
            }
        });
        //���µ��޸ĺ�Ĳ������ø����ӹ�����
        clientManager.option(okOptionsBuilder.build());

        //ע��Socket��Ϊ������,SocketActionAdapter�ǻص���Simple��,�����ص�������������ĵ�
        clientManager.registerReceiver(new SocketActionAdapter(){
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                logger.info("connected.");

                if (config.getGeneral().server().pulseInterval()>0) {
                    okOptionsBuilder.setPulseFrequency(config.getGeneral().server().pulseInterval()*1000);
                    clientManager.getPulseManager().setPulseSendable(mPulseData).pulse();
                }



            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                //��ѭ���Ϲ���,����ص��ſ��������յ����������ص�����,������OriginalData��,Ϊbyte[]����,�����������Ѿ�������ֽ�������,ֱ�ӷ���ByteBuffer�м���ʹ��

                /*if (Packer_Pulse.isPulse(data.getHeadBytes())){
                    logger.info("1");
                    clientManager.getPulseManager().pulse();
                }
                else */if (Packer.isMessage(data.getHeadBytes())){
                    try {
                        String str= new String (data.getBodyBytes(), Charset.forName("UTF-8"));

                        //logger.info(str);

                        JSONObject jsonObject = new JSONObject(str);
                        int version = jsonObject.getInt("version");
                        if (version== MessagePackType.PackVersion) {
                            int messageType = jsonObject.getInt("type");

                            if (messageType== MessagePackType.PULSE){
                                clientManager.getPulseManager().feed();
                            } else if (messageType== MessagePackType.INFO){

                            } else if (messageType== MessagePackType.MESSAGE) {

                                String sender = MessageTools.Base64Decode(jsonObject.getString("sender"));
                                MessageContentUnpacker content = new MessageContentUnpacker(jsonObject.getJSONArray("content"));
                                content.textConfig = config.getText();
                                content.logger = logger;

                                Text Content = config.getText().MSGFormat_overview().apply(ImmutableMap.of(
                                        "SENDER",sender,
                                        "BODY",content.toText()

                                )).build();

                                MessageChannel.TO_ALL.send(Content);

                            } else {
                                logger.info("Received a message with an unrecognized type.");
                            }
                        } else
                        {
                            if (version> MessagePackType.PackVersion) {
                                logger.info("Received a message made by a higher-version server.");
                            } else {
                                logger.info("Received a message made by a lower-version server.");
                            }
                        }
                    } catch (JSONException e) {
                        logger.error("Received an unrecognized message.",e);
                    }


                    /*
                    Text ot = Text.builder("[From Socket Server] "+str).build();
                    Text text = Text.builder().color(GREEN).append(ot).build();
                    */
                };


            }


        });

        //����ͨ����������
        clientManager.connect();
    }
}
