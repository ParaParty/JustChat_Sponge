package com.superexercisebook.justchat;

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
import java.util.Base64;

import static org.spongepowered.api.text.format.TextColors.*;

public class JustchatClient extends Thread{

    final static byte[] MessageHeader = {0x11,0x45,0x14};

    public ConnectionInfo info;
    public IConnectionManager clientManager;
    public Logger logger;
    //�����Զ������ͷ
    private OkSocketOptions.Builder okOptionsBuilder;


    @Override
    public void run(){

        info = new ConnectionInfo("127.0.0.1", 5000);

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
                /*if (PulsePacker.isPulse(header)){
                    return 0;
                }
                else*/ if (MessagePacker.isMessage(header)){
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
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                //��ѭ���Ϲ���,����ص��ſ��������յ����������ص�����,������OriginalData��,Ϊbyte[]����,�����������Ѿ�������ֽ�������,ֱ�ӷ���ByteBuffer�м���ʹ��

                /*if (PulsePacker.isPulse(data.getHeadBytes())){
                    logger.info("1");
                    clientManager.getPulseManager().pulse();
                }
                else */if (MessagePacker.isMessage(data.getHeadBytes())){
                    try {
                        String str= new String (data.getBodyBytes(), Charset.forName("UTF-8"));

                        logger.info(str);

                        JSONObject jsonObject = new JSONObject(str);
                        int version = jsonObject.getInt("version");
                        if (version==MessagePacker.PackVersion) {
                            int messageType = jsonObject.getInt("type");

                            if (messageType==MessagePackType.MESSAGE) {

                                String sender = new String(Base64.getDecoder().decode(jsonObject.getString("sender")), Charset.forName("UTF-8"));
                                String content = new String(Base64.getDecoder().decode(jsonObject.getString("content")), Charset.forName("UTF-8"));

                                Text TTag = Text.builder("[*] ").color(DARK_GREEN).build();
                                Text TSender = Text.builder(sender).color(DARK_GREEN).build();
                                Text TSplit = Text.builder(": ").build();
                                Text TContent = Text.builder(content).build();

                                Text Content = Text.builder().append(TTag, TSender, TSplit, TContent).build();
                                MessageChannel.TO_ALL.send(Content);

                            } else {
                                logger.info("Received a message with an unrecognized type.");
                            }
                        } else
                        {
                            if (version>MessagePacker.PackVersion) {
                                logger.info("Received a message made by a lower-version server.");
                            } else {
                                logger.info("Received a message made by a higher-version server.");
                            }
                        }
                    } catch (JSONException e) {
                        logger.error("Received an unrecognized message.");
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
