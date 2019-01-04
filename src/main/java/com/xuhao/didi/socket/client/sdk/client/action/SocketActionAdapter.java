package com.xuhao.didi.socket.client.sdk.client.action;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;

/**
 * Socket��Ϊ������,����Ϊ��������һ��Simple�汾,�������{@link ISocketActionListener}
 * Created by xuhao on 2017/5/17.
 */

public abstract class SocketActionAdapter implements ISocketActionListener {
    /**
     * SocketͨѶIO�̵߳�����<br>
     * �÷������ú�IO�߳̽�����������<br>
     * ����InputStream�߳�������,���ص��˷���,���OutPutStream�߳�����,Ҳ��ص��˷���.<br>
     * һ�γɹ���˫��ͨѶ����,����ô˷�������.<br>
     *
     * @param action {@link IAction#ACTION_READ_THREAD_START}
     *               {@link  IAction#ACTION_WRITE_THREAD_START}
     */
    @Override
    public void onSocketIOThreadStart(String action) {

    }

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
    @Override
    public void onSocketIOThreadShutdown(String action, Exception e) {

    }

    /**
     * Socket�Ͽ�����еĻص�<br>
     * ��Socket���׶Ͽ���,ϵͳ��ص��÷���<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_DISCONNECTION}
     * @param e      Socket�Ͽ�ʱ���쳣��Ϣ,����������Ͽ�(����disconnect()),�쳣��Ϣ��Ϊnull.ʹ��e����ʱӦ�ý����пղ���
     */
    @Override
    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {

    }

    /**
     * ��Socket���ӽ����ɹ���<br>
     * ϵͳ��ص��÷���,��ʱ�п��ܶ�д�̻߳�δ�������,��������Ӱ���<br>
     * ���ص��˷�����,���ǿ�����ΪSocket�����Ѿ��������,���Ҷ�д�߳�Ҳ��ʼ����<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_CONNECTION_SUCCESS}
     */
    @Override
    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {

    }

    /**
     * ��Socket����ʧ��ʱ����лص�<br>
     * ����Socket����,������������ֹ���,��������쳣�������¸÷������ص�<br>
     * ϵͳ�ص��˷���ʱ,IO�߳̾�δ����.���IO�߳���������ص�{@link #onSocketDisconnection(ConnectionInfo, String, Exception)}<br>
     *
     * @param info   ������ӵ�������Ϣ
     * @param action {@link IAction#ACTION_CONNECTION_FAILED}
     * @param e      ����δ�ɹ������Ĵ���ԭ��
     */
    @Override
    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {

    }

    /**
     * SocketͨѶ�ӷ�������ȡ����Ϣ�����Ӧ<br>
     *
     * @param action {@link IAction#ACTION_READ_COMPLETE}
     * @param data   ԭʼ�Ķ�ȡ��������{@link OriginalData}
     */
    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {

    }

    /**
     * SocketͨѶд�������Ӧ�ص�<br>
     *
     * @param action {@link IAction#ACTION_WRITE_COMPLETE}
     * @param data   д��������{@link ISendable}
     */
    @Override
    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {

    }

    /**
     * Socket�������ͺ�Ļص�<br>
     * ����������һ���������д����<br>
     * ���������ͺ󽫲���ص�{@link #onSocketWriteResponse(ConnectionInfo, String, ISendable)}����<br>
     *
     * @param info ������ӵ�������Ϣ
     * @param data ������������{@link IPulseSendable}
     */
    @Override
    public void onPulseSend(ConnectionInfo info, IPulseSendable data) {

    }
}
