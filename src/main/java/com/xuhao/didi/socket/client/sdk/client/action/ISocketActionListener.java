package com.xuhao.didi.socket.client.sdk.client.action;


import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;

public interface ISocketActionListener {
    /**
     * SocketͨѶIO�̵߳�����<br>
     * �÷������ú�IO�߳̽�����������<br>
     * ����InputStream�߳�������,���ص��˷���,���OutPutStream�߳�����,Ҳ��ص��˷���.<br>
     * һ�γɹ���˫��ͨѶ����,����ô˷�������.<br>
     *
     * @param action {@link IAction#ACTION_READ_THREAD_START}
     *               {@link  IAction#ACTION_WRITE_THREAD_START}
     */
    void onSocketIOThreadStart(String action);

    /**
     * SocketͨѶIO�̵߳Ĺر�<br>
     * �÷������ú�IO�߳̽���������<br>
     * ����InputStream�߳����ٺ�,���ص��˷���,���OutPutStream�߳�����,Ҳ��ص��˷���.<br>
     * һ�γɹ���˫��ͨѶ����,����ô˷�������.<br>
     *
     * @param action {@link IAction#ACTION_READ_THREAD_SHUTDOWN}
     *               {@link  IAction#ACTION_WRITE_THREAD_SHUTDOWN}
     * @param e      �̹߳ر����������쳣��Ϣ,�����Ͽ�Ҳ���ܻ����쳣��Ϣ.
     */
    void onSocketIOThreadShutdown(String action, Exception e);

    /**
     * SocketͨѶ�ӷ�������ȡ����Ϣ�����Ӧ<br>
     *
     * @param action {@link IAction#ACTION_READ_COMPLETE}
     * @param data   ԭʼ�Ķ�ȡ��������{@link OriginalData}
     */
    void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data);

    /**
     * SocketͨѶд�������Ӧ�ص�<br>
     *
     * @param action {@link IAction#ACTION_WRITE_COMPLETE}
     * @param data   д��������{@link ISendable}
     */
    void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data);

    /**
     * Socket�������ͺ�Ļص�<br>
     * ����������һ���������д����<br>
     * ���������ͺ󽫲���ص�{@link #onSocketWriteResponse(ConnectionInfo, String, ISendable)}����<br>
     *
     * @param info ������ӵ�������Ϣ
     * @param data ������������{@link IPulseSendable}
     */
    void onPulseSend(ConnectionInfo info, IPulseSendable data);

    /**
     * Socket�Ͽ�����еĻص�<br>
     * ��Socket���׶Ͽ���,ϵͳ��ص��÷���<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_DISCONNECTION}
     * @param e      Socket�Ͽ�ʱ���쳣��Ϣ,����������Ͽ�(����disconnect()),�쳣��Ϣ��Ϊnull.ʹ��e����ʱӦ�ý����пղ���
     */
    void onSocketDisconnection(ConnectionInfo info, String action, Exception e);

    /**
     * ��Socket���ӽ����ɹ���<br>
     * ϵͳ��ص��÷���,��ʱ�п��ܶ�д�̻߳�δ�������,��������Ӱ���<br>
     * ���ص��˷�����,���ǿ�����ΪSocket�����Ѿ��������,���Ҷ�д�߳�Ҳ��ʼ����<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_CONNECTION_SUCCESS}
     */
    void onSocketConnectionSuccess(ConnectionInfo info, String action);

    /**
     * ��Socket����ʧ��ʱ����лص�<br>
     * ����Socket����,������������ֹ���,��������쳣�������¸÷������ص�<br>
     * ϵͳ�ص��˷���ʱ,IO�߳̾�δ����.���IO�߳���������ص�{@link #onSocketDisconnection(ConnectionInfo, String, Exception)}<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_CONNECTION_FAILED}
     * @param e      ����δ�ɹ������Ĵ���ԭ��
     */
    void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e);
}
