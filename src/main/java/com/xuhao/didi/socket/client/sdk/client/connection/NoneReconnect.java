package com.xuhao.didi.socket.client.sdk.client.connection;


import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;

/**
 * �������������ӵ�����������
 * Created by Tony on 2017/10/24.
 */
public class NoneReconnect extends AbsReconnectionManager {
    @Override
    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {

    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {

    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {

    }
}
