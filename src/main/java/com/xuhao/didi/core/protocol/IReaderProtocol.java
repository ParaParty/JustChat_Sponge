package com.xuhao.didi.core.protocol;

import java.nio.ByteOrder;

/**
 * ��ͷ���ݸ�ʽ
 * Created by xuhao on 2017/5/22.
 */
public interface IReaderProtocol {
    /**
     * ���ذ�ͷ����,�ó��Ƚ���֪���,�������������ʱ,��һ�����������ڰ�ͷ.
     *
     * @return ��ͷ�ĳ���, �ó���Ӧ����һ���̶��İ�ͷ����
     */
    int getHeaderLength();

    /**
     * ��ܸ���{@link IReaderProtocol#getHeaderLength()}������ȡ����ͷ��,������ø÷���<br>
     * ���ɿ����߽��н���,������Ӧ�ôӲ��� {@link #getBodyLength(byte[], ByteOrder)}}header�н������ô�ͨѶ����˷��صİ��峤��<br>
     *
     * @param header    ����getHeaderLength()������õİ�ͷԭʼ����.������Ӧ�ôӴ�header�ֽ��������峤������.
     * @param byteOrder ��ǰ��ͷ�ֽ�������,��ͷ���ݵ��ֽ�������.
     * @return ������Ӧ�ôӴ�header�ֽ��������峤������.��ֵ��Ӧ����һ����������ֵ, Ӧ���ǽ���������һ����ֵ.
     */
    int getBodyLength(byte[] header, ByteOrder byteOrder);
}
