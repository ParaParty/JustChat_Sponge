package com.xuhao.didi.socket.client.sdk.client.bean;

/**
 * Created by xuhao on 2017/5/18.
 */

public interface IPulse {
    /**
     * ��ʼ����
     */
    void pulse();

    /**
     * ����һ������
     */
    void trigger();

    /**
     * ֹͣ����
     */
    void dead();

    /**
     * �������غ�ι��,ACK
     */
    void feed();
}

