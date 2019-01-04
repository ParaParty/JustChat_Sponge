package com.xuhao.didi.socket.client.sdk.client.connection;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.socket.client.impl.client.PulseManager;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ��������������
 * Created by xuhao on 2017/6/5.
 */
public abstract class AbsReconnectionManager implements ISocketActionListener {
    /**
     * ���ӹ�����
     */
    protected volatile IConnectionManager mConnectionManager;
    /**
     * ����������
     */
    protected PulseManager mPulseManager;
    /**
     * �Ƿ�����
     */
    protected volatile boolean mDetach;
    /**
     * ��Ҫ���ԵĶϿ����Ӽ���,��Exception�ڴ˼�����,���Ը����͵ĶϿ��쳣,�����Զ�����
     */
    protected volatile Set<Class<? extends Exception>> mIgnoreDisconnectExceptionList = new LinkedHashSet<>();

    public AbsReconnectionManager() {

    }

    /**
     * ������ĳһ�����ӹ�����
     *
     * @param manager ��ǰ���ӹ�����
     */
    public synchronized void attach(IConnectionManager manager) {
        if (mDetach) {
            detach();
        }
        mDetach = false;
        mConnectionManager = manager;
        mPulseManager = manager.getPulseManager();
        mConnectionManager.registerReceiver(this);
    }

    /**
     * ������ӵ�ǰ�����ӹ�����
     */
    public synchronized void detach() {
        mDetach = true;
        if (mConnectionManager != null) {
            mConnectionManager.unRegisterReceiver(this);
        }
    }

    /**
     * �����Ҫ���Ե��쳣,���Ͽ��쳣Ϊ���쳣ʱ,�������������.
     *
     * @param e ��Ҫ���Ե��쳣
     */
    public final void addIgnoreException(Class<? extends Exception> e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.add(e);
        }
    }

    /**
     * �����Ҫ���Ե��쳣,���Ͽ��쳣Ϊ���쳣ʱ,�������������.
     *
     * @param e ��Ҫɾ�����쳣
     */
    public final void removeIgnoreException(Exception e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e.getClass());
        }
    }

    /**
     * ɾ����Ҫ���Ե��쳣
     *
     * @param e ��Ҫ���Ե��쳣
     */
    public final void removeIgnoreException(Class<? extends Exception> e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e);
        }
    }

    /**
     * ɾ�����еĺ����쳣
     */
    public final void removeAll() {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.clear();
        }
    }

    @Override
    public void onSocketIOThreadStart(String action) {
        //do nothing;
    }

    @Override
    public void onSocketIOThreadShutdown(String action, Exception e) {
        //do nothing;
    }

    @Override
    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        //do nothing;
    }

    @Override
    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
        //do nothing;
    }

    @Override
    public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
        //do nothing;
    }

}
