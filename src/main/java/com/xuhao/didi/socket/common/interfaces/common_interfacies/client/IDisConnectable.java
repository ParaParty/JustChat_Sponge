package com.xuhao.didi.socket.common.interfaces.common_interfacies.client;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IDisConnectable {
    /**
     * �Ͽ���ǰ���ӹ�����������,��������һ���쳣<br>
     *
     * @param e �Ͽ�ʱϣ��������쳣����
     */
    void disconnect(Exception e);

    /**
     * �Ͽ���ǰ���ӹ�����������,,�Ͽ��ص��еĶϿ��쳣����Null<br>
     */
    void disconnect();
}
