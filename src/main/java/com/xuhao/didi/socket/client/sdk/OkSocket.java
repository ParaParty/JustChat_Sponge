package com.xuhao.didi.socket.client.sdk;


import com.xuhao.didi.socket.client.impl.client.ManagerHolder;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerActionListener;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;

/**
 * OkSocket��һ����������SocketͨѶ���,�����ṩ����,˫����TCPͨѶ.
 * �����ṩOkSocket�����ж���ӿ�,ʹ��OkSocket���Ӧ�ӱ����open����һ������ͨ��.
 * Created by xuhao on 2017/5/16.
 */
public class OkSocket {

    private static ManagerHolder holder = ManagerHolder.getInstance();

    /**
     * ���һ��SocketServer������.
     *
     * @param serverPort
     * @return
     */
    public static IRegister<IServerActionListener, IServerManager> server(int serverPort) {
        return (IRegister<IServerActionListener, IServerManager>) holder.getServer(serverPort);
    }

    /**
     * ����һ��socketͨѶͨ��,����ΪĬ�ϲ���
     *
     * @param connectInfo ������Ϣ{@link ConnectionInfo}
     * @return �ò��������ӹ����� {@link IConnectionManager} ���Ӳ�������Ϊ���ø�ͨ���Ĳ���,��Ӱ��ȫ�ֲ���
     */
    public static IConnectionManager open(ConnectionInfo connectInfo) {
        return holder.getConnection(connectInfo);
    }

    /**
     * ����һ��socketͨѶͨ��,����ΪĬ�ϲ���
     *
     * @param ip   ��Ҫ���ӵ�����IPV4��ַ
     * @param port ��Ҫ���ӵ��������ŵ�Socket�˿ں�
     * @return �ò��������ӹ����� {@link IConnectionManager} ���Ӳ�������Ϊ���ø�ͨ���Ĳ���,��Ӱ��ȫ�ֲ���
     */
    public static IConnectionManager open(String ip, int port) {
        ConnectionInfo info = new ConnectionInfo(ip, port);
        return holder.getConnection(info);
    }

    /**
     * ����һ��socketͨѶͨ��
     * Deprecated please use {@link OkSocket#open(ConnectionInfo)}@{@link IConnectionManager#option(OkSocketOptions)}
     *
     * @param connectInfo ������Ϣ{@link ConnectionInfo}
     * @param okOptions   ���Ӳ���{@link OkSocketOptions}
     * @return �ò��������ӹ����� {@link IConnectionManager} ���Ӳ�������Ϊ���ø�ͨ���Ĳ���,��Ӱ��ȫ�ֲ���
     * @deprecated
     */
    public static IConnectionManager open(ConnectionInfo connectInfo, OkSocketOptions okOptions) {
        return holder.getConnection(connectInfo, okOptions);
    }

    /**
     * ����һ��socketͨѶͨ��
     * Deprecated please use {@link OkSocket#open(String, int)}@{@link IConnectionManager#option(OkSocketOptions)}
     *
     * @param ip        ��Ҫ���ӵ�����IPV4��ַ
     * @param port      ��Ҫ���ӵ��������ŵ�Socket�˿ں�
     * @param okOptions ���Ӳ���{@link OkSocketOptions}
     * @return �ò��������ӹ����� {@link IConnectionManager}
     * @deprecated
     */
    public static IConnectionManager open(String ip, int port, OkSocketOptions okOptions) {
        ConnectionInfo info = new ConnectionInfo(ip, port);
        return holder.getConnection(info, okOptions);
    }
}
