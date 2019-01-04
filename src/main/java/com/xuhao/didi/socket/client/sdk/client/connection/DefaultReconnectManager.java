package com.xuhao.didi.socket.client.sdk.client.connection;


import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.impl.exceptions.ManuallyDisconnectException;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread;
import com.xuhao.didi.socket.common.interfaces.utils.ThreadUtils;

import java.util.Iterator;

/**
 * Created by xuhao on 2017/6/5.
 */

public class DefaultReconnectManager extends AbsReconnectionManager {

    /**
     * �������ʧ�ܴ���,�������Ͽ��쳣
     */
    private static final int MAX_CONNECTION_FAILED_TIMES = 12;
    /**
     * ����ʧ�ܴ���,�������Ͽ��쳣
     */
    private int mConnectionFailedTimes = 0;

    private volatile ReconnectTestingThread mReconnectTestingThread;

    public DefaultReconnectManager() {
        mReconnectTestingThread = new ReconnectTestingThread();
    }

    @Override
    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
        if (isNeedReconnect(e)) {//break with exception
            reconnectDelay();
        } else {
            resetThread();
        }
    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
        resetThread();
    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
        if (e != null) {
            mConnectionFailedTimes++;
            if (mConnectionFailedTimes > MAX_CONNECTION_FAILED_TIMES) {
                resetThread();
                //����ʧ�ܴﵽ��ֵ,��Ҫ�л�������·.
                ConnectionInfo originInfo = mConnectionManager.getRemoteConnectionInfo();
                ConnectionInfo backupInfo = originInfo.getBackupInfo();
                if (backupInfo != null) {
                    ConnectionInfo bbInfo = new ConnectionInfo(originInfo.getIp(), originInfo.getPort());
                    backupInfo.setBackupInfo(bbInfo);
                    if (!mConnectionManager.isConnect()) {
                        SLog.i("Prepare switch to the backup line " + backupInfo.getIp() + ":" + backupInfo.getPort() + " ...");
                        synchronized (mConnectionManager) {
                            mConnectionManager.switchConnectionInfo(backupInfo);
                        }
                        reconnectDelay();
                    }
                } else {
                    reconnectDelay();
                }
            } else {
                reconnectDelay();
            }
        }
    }

    /**
     * �Ƿ���Ҫ����
     *
     * @param e
     * @return
     */
    private boolean isNeedReconnect(Exception e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            if (e != null && !(e instanceof ManuallyDisconnectException)) {//break with exception
                Iterator<Class<? extends Exception>> it = mIgnoreDisconnectExceptionList.iterator();
                while (it.hasNext()) {
                    Class<? extends Exception> classException = it.next();
                    if (classException.isAssignableFrom(e.getClass())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }


    /**
     * ���������߳�,�ر��߳�
     */
    private synchronized void resetThread() {
        if (mReconnectTestingThread != null) {
            mReconnectTestingThread.shutdown();
        }
    }

    /**
     * ��ʼ�ӳ�����
     */
    private void reconnectDelay() {
        synchronized (mReconnectTestingThread) {
            if (mReconnectTestingThread.isShutdown()) {
                mReconnectTestingThread.start();
            }
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private class ReconnectTestingThread extends AbsLoopThread {
        /**
         * ��ʱ����ʱ��
         */
        private long mReconnectTimeDelay = 10 * 1000;

        @Override

        protected void beforeLoop() throws Exception {
            super.beforeLoop();
            if (mReconnectTimeDelay < mConnectionManager.getOption().getConnectTimeoutSecond() * 1000) {
                mReconnectTimeDelay = mConnectionManager.getOption().getConnectTimeoutSecond() * 1000;
            }
        }

        @Override
        protected void runInLoopThread() throws Exception {
            if (mDetach) {
                SLog.i("ReconnectionManager already detached by framework.We decide gave up this reconnection mission!");
                shutdown();
                return;
            }

            //�ӳ�ִ��
            SLog.i("Reconnect after " + mReconnectTimeDelay + " mills ...");
            ThreadUtils.sleep(mReconnectTimeDelay);

            if (mDetach) {
                SLog.i("ReconnectionManager already detached by framework.We decide gave up this reconnection mission!");
                shutdown();
                return;
            }

            if (mConnectionManager.isConnect()) {
                shutdown();
                return;
            }
            boolean isHolden = mConnectionManager.getOption().isConnectionHolden();

            if (!isHolden) {
                detach();
                shutdown();
                return;
            }
            ConnectionInfo info = mConnectionManager.getRemoteConnectionInfo();
            SLog.i("Reconnect the server " + info.getIp() + ":" + info.getPort() + " ...");
            synchronized (mConnectionManager) {
                if (!mConnectionManager.isConnect()) {
                    mConnectionManager.connect();
                } else {
                    shutdown();
                }
            }
        }


        @Override
        protected void loopFinish(Exception e) {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

}
