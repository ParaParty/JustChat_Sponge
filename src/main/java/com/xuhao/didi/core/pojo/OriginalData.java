package com.xuhao.didi.core.pojo;

import java.io.Serializable;

/**
 * ԭʼ���ݽṹ��
 * Created by xuhao on 2017/5/16.
 */
public final class OriginalData implements Serializable {
    /**
     * ԭʼ���ݰ�ͷ�ֽ�����
     */
    private byte[] mHeadBytes;
    /**
     * ԭʼ���ݰ����ֽ�����
     */
    private byte[] mBodyBytes;

    public byte[] getHeadBytes() {
        return mHeadBytes;
    }

    public void setHeadBytes(byte[] headBytes) {
        mHeadBytes = headBytes;
    }

    public byte[] getBodyBytes() {
        return mBodyBytes;
    }

    public void setBodyBytes(byte[] bodyBytes) {
        mBodyBytes = bodyBytes;
    }
}
