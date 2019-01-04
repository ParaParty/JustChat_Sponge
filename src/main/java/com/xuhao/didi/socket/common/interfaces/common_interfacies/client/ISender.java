package com.xuhao.didi.socket.common.interfaces.common_interfacies.client;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface ISender<T> {
    /**
     * �ڵ�ǰ�������Ϸ�������
     *
     * @param sendable ���з���������Bean {@link ISendable}
     * @return T
     */
    T send(ISendable sendable);
}
