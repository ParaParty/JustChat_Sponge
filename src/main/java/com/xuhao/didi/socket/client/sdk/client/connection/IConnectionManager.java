package com.xuhao.didi.socket.client.sdk.client.connection;

import com.xuhao.didi.socket.client.impl.client.PulseManager;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.ISocketActionListener;
import com.xuhao.didi.socket.client.sdk.client.connection.abilities.IConfiguration;
import com.xuhao.didi.socket.client.sdk.client.connection.abilities.IConnectable;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.client.IDisConnectable;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.client.ISender;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IConnectionManager extends
        IConfiguration,
        IConnectable,
        IDisConnectable,
        ISender<IConnectionManager>,
        IRegister<ISocketActionListener, IConnectionManager> {
    /**
     * �Ƿ�����
     *
     * @return true ������,false δ����
     */
    boolean isConnect();

    /**
     * �Ƿ��ڶϿ����ӵĽ׶�.
     *
     * @return true ���ڶϿ�����,false�����л����ѶϿ�.
     */
    boolean isDisconnecting();

    /**
     * ��ȡ������������,������������������������Ϊ.
     *
     * @return ����������
     */
    PulseManager getPulseManager();

    /**
     * �Ƿ�OkSocket����˴�����
     *
     * @param isHold true ���б����������.false �����б��滺�����.
     */
    void setIsConnectionHolder(boolean isHold);

    /**
     * ���������Ϣ
     *
     * @return ������Ϣ
     */
    ConnectionInfo getRemoteConnectionInfo();

    /**
     * ��ñ���������Ϣ
     *
     * @return ���ذ���Ϣ
     */
    ConnectionInfo getLocalConnectionInfo();

    /**
     * ���ñ��ض˿���Ϣ
     *
     * @param localConnectionInfo ���ذ󶨶˿���Ϣ
     */
    void setLocalConnectionInfo(ConnectionInfo localConnectionInfo);

    /**
     * ����ǰ�����ӹ������е�������Ϣ�����л�.
     *
     * @param info �µ�������Ϣ
     */
    void switchConnectionInfo(ConnectionInfo info);

    /**
     * �������������,������������������
     *
     * @return ����������
     */
    AbsReconnectionManager getReconnectionManager();

}

