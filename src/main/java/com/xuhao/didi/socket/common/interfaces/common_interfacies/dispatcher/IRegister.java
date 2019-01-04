package com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher;

public interface IRegister<T, E> {
    /**
     * ע��һ���ص�������
     *
     * @param socketActionListener �ص�������
     */
    E registerReceiver(T socketActionListener);

    /**
     * ����ص�������
     *
     * @param socketActionListener ע��ʱ�Ľ�����,��Ҫ����Ľ�����
     */
    E unRegisterReceiver(T socketActionListener);
}
