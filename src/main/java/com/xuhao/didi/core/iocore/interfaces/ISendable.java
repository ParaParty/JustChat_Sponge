package com.xuhao.didi.core.iocore.interfaces;

import java.io.Serializable;

/**
 * �ɷ�����,�̳и���,��ʵ��parse�������ɻ�÷�������
 * Created by xuhao on 2017/5/16.
 */
public interface ISendable extends Serializable {
    /**
     * ����ת��
     *
     * @return ��Ҫ���͵����ݵ��ֽ�����
     */
    byte[] parse();
}
