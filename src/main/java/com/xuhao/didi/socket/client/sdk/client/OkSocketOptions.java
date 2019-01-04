package com.xuhao.didi.socket.client.sdk.client;

import com.xuhao.didi.core.iocore.interfaces.IIOCoreOptions;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.client.connection.AbsReconnectionManager;
import com.xuhao.didi.socket.client.sdk.client.connection.DefaultReconnectManager;
import com.xuhao.didi.socket.client.sdk.client.connection.abilities.IConfiguration;
import com.xuhao.didi.socket.common.interfaces.default_protocol.DefaultNormalReaderProtocol;

import java.nio.ByteOrder;

/**
 * OkSocket����������<br>
 * Created by xuhao on 2017/5/16.
 */
public class OkSocketOptions implements IIOCoreOptions {
    /**
     * ����Ƿ��ǵ���ģʽ
     */
    private static boolean isDebug;
    /**
     * SocketͨѶģʽ
     * <p>
     * ��ע��:<br>
     * ����ʽ��֧�����л�(�Ͽ����л�)<br>
     * ������ʽ�������л�<br>
     * </p>
     */
    private IOThreadMode mIOThreadMode;
    /**
     * �����Ƿ������<br>
     * <p>
     * true:���ӽ��ᱣ���ڹ�������,���������Ż��Ͷ�������<br>
     * false:���ᱣ���ڹ�������,�����Ѿ�����Ļ����ɾ��,�������������Ż��Ͷ�������.
     * </p>
     */
    private boolean isConnectionHolden;
    /**
     * д��Socket�ܵ��и����������ֽ���
     */
    private ByteOrder mWriteOrder;
    /**
     * ��Socket�ܵ��ж�ȡ�ֽ���ʱ���ֽ���
     */
    private ByteOrder mReadByteOrder;
    /**
     * SocketͨѶ��,ҵ��㶨������ݰ���ͷ��ʽ
     */
    private IReaderProtocol mReaderProtocol;
    /**
     * ���͸�������ʱ�������ݰ����ܳ���
     */
    private int mWritePackageBytes;
    /**
     * �ӷ�������ȡʱ���ζ�ȡ�Ļ����ֽڳ���,��ֵԽ��,��ȡЧ��Խ��.������Ӧ��ϵͳ���Ľ�Խ��
     */
    private int mReadPackageBytes;
    /**
     * ����Ƶ�ʵ�λ�Ǻ���
     */
    private long mPulseFrequency;
    /**
     * ������ʧ����<br>
     * ���ڻ���ڶ�ʧ����ʱ���Ͽ���ͨ��������<br>
     * �׳�{@link com.xuhao.didi.socket.client.impl.exceptions.DogDeadException}
     */
    private int mPulseFeedLoseTimes;
    /**
     * ���ӳ�ʱʱ��(��)
     */
    private int mConnectTimeoutSecond;
    /**
     * ����ȡ���ݵ�����(MB)<br>
     * ��ֹ�����������������������ݵ���ǰ���ڴ����.
     */
    private int mMaxReadDataMB;
    /**
     * �������ӹ�����
     */
    private AbsReconnectionManager mReconnectionManager;
    /**
     * ��ȫ�׽��ֲ�����
     */
    private OkSocketSSLConfig mSSLConfig;
    /**
     * �׽��ֹ���
     */
    private OkSocketFactory mOkSocketFactory;
    /**
     * �Ӷ����߳̽��лص�.
     */
    private boolean isCallbackInIndependentThread;
    /**
     * ���ַ��ŵ�handler��,�ⲿ��Ҫ����HandlerToken���ҵ���Handler.post(runnable);
     */
    private ThreadModeToken mCallbackThreadModeToken;

    private OkSocketOptions() {
    }

    public static void setIsDebug(boolean isDebug) {
        OkSocketOptions.isDebug = isDebug;
    }

    public static abstract class ThreadModeToken {
        public abstract void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable);
    }

    public static class Builder {
        private OkSocketOptions mOptions;

        public Builder() {
            this(OkSocketOptions.getDefault());
        }

        public Builder(IConfiguration configuration) {
            this(configuration.getOption());
        }

        public Builder(OkSocketOptions okOptions) {
            mOptions = okOptions;
        }

        /**
         * SocketͨѶģʽ
         * <p>
         * ��ע��:<br>
         * ����ʽ��֧�����л�(�Ͽ����л�)<br>
         * ������ʽ�������л�<br>
         * </p>
         *
         * @param IOThreadMode {@link IOThreadMode}
         */
        public Builder setIOThreadMode(IOThreadMode IOThreadMode) {
            mOptions.mIOThreadMode = IOThreadMode;
            return this;
        }

        /**
         * ����ȡ���ݵ�����(MB)<br>
         * ��ֹ�����������������������ݵ���ǰ���ڴ����<br>
         *
         * @param maxReadDataMB ���ֽ�Ϊ��λ
         */
        public Builder setMaxReadDataMB(int maxReadDataMB) {
            mOptions.mMaxReadDataMB = maxReadDataMB;
            return this;
        }

        /**
         * ��ȫ�׽��ֲ�����<br>
         *
         * @param SSLConfig {@link OkSocketSSLConfig}
         */
        public Builder setSSLConfig(OkSocketSSLConfig SSLConfig) {
            mOptions.mSSLConfig = SSLConfig;
            return this;
        }

        /**
         * SocketͨѶ��,ҵ��㶨������ݰ���ͷ��ʽ<br>
         * Ĭ�ϵ�Ϊ{@link DefaultNormalReaderProtocol}<br>
         *
         * @param readerProtocol {@link IReaderProtocol} ͨѶͷЭ��
         */
        public Builder setReaderProtocol(IReaderProtocol readerProtocol) {
            mOptions.mReaderProtocol = readerProtocol;
            return this;
        }

        /**
         * �����������Ƶ��<br>
         * ��λ�Ǻ���<br>
         *
         * @param pulseFrequency ���������
         */

        public Builder setPulseFrequency(long pulseFrequency) {
            mOptions.mPulseFrequency = pulseFrequency;
            return this;
        }

        /**
         * �����Ƿ������<br>
         * <p>
         * true:���ӽ��ᱣ���ڹ�������,���������Ż��Ͷ�������<br>
         * false:���ᱣ���ڹ�������,�����Ѿ�����Ļ����ɾ��,�������������Ż��Ͷ�������.
         * </p>
         * Ĭ���� true
         *
         * @param connectionHolden true ���˴����ӽ���OkSocket���л������,false �򲻽��л������.
         */
        public Builder setConnectionHolden(boolean connectionHolden) {
            mOptions.isConnectionHolden = connectionHolden;
            return this;
        }

        /**
         * ������ʧ����<br>
         * ���ڻ���ڶ�ʧ����ʱ���Ͽ���ͨ��������<br>
         * �׳�{@link com.xuhao.didi.socket.client.impl.exceptions.DogDeadException}<br>
         * Ĭ����5��
         *
         * @param pulseFeedLoseTimes ��ʧ����ACK�Ĵ���,����5,����ʧ3��ʱ,�Զ��Ͽ�.
         */
        public Builder setPulseFeedLoseTimes(int pulseFeedLoseTimes) {
            mOptions.mPulseFeedLoseTimes = pulseFeedLoseTimes;
            return this;
        }

        /**
         * �������Socket�ܵ��и����������ֽ���<br>
         * Ĭ����:����ֽ���<br>
         *
         * @param writeOrder {@link ByteOrder} �ֽ���
         * @deprecated ��ʹ�� {@link Builder#setWriteByteOrder(ByteOrder)}
         */
        public Builder setWriteOrder(ByteOrder writeOrder) {
            setWriteByteOrder(writeOrder);
            return this;
        }


        /**
         * �������Socket�ܵ��и����������ֽ���<br>
         * Ĭ����:����ֽ���<br>
         *
         * @param writeOrder {@link ByteOrder} �ֽ���
         */
        public Builder setWriteByteOrder(ByteOrder writeOrder) {
            mOptions.mWriteOrder = writeOrder;
            return this;
        }

        /**
         * ��������Socket�ܵ��ж�ȡʱ���ֽ���<br>
         * Ĭ����:����ֽ���<br>
         *
         * @param readByteOrder {@link ByteOrder} �ֽ���
         */
        public Builder setReadByteOrder(ByteOrder readByteOrder) {
            mOptions.mReadByteOrder = readByteOrder;
            return this;
        }

        /**
         * ���͸�������ʱ�������ݰ����ܳ���
         *
         * @param writePackageBytes �������ݰ����ܴ�С
         */
        public Builder setWritePackageBytes(int writePackageBytes) {
            mOptions.mWritePackageBytes = writePackageBytes;
            return this;
        }

        /**
         * �ӷ�������ȡʱ�������ݰ����ܳ���
         *
         * @param readPackageBytes �������ݰ����ܴ�С
         */
        public Builder setReadPackageBytes(int readPackageBytes) {
            mOptions.mReadPackageBytes = readPackageBytes;
            return this;
        }

        /**
         * �������ӳ�ʱʱ��,�ó�ʱʱ������·�ϴӿ�ʼ���ӵ������ϵ�ʱ��
         *
         * @param connectTimeoutSecond ��ʱ����,ע�ⵥλ����
         * @return
         */
        public Builder setConnectTimeoutSecond(int connectTimeoutSecond) {
            mOptions.mConnectTimeoutSecond = connectTimeoutSecond;
            return this;
        }

        /**
         * ���ö������������ӹ�����<br>
         * Ĭ�ϵ����ӹ�����Ϊ{@link DefaultReconnectManager}<br>
         * �������Ҫ�������������øò���Ϊ{@link com.xuhao.didi.socket.client.sdk.client.connection.NoneReconnect}
         *
         * @param reconnectionManager ��������������{@link AbsReconnectionManager}
         * @return
         */
        public Builder setReconnectionManager(
                AbsReconnectionManager reconnectionManager) {
            mOptions.mReconnectionManager = reconnectionManager;
            return this;
        }

        /**
         * ����Socket������,�����ṩһ���������ӵ�Socket.
         * �����Ǽ���Socket,Ҳ������δ���ܵ�socket.
         *
         * @param factory socket��������
         * @return
         */
        public Builder setSocketFactory(OkSocketFactory factory) {
            mOptions.mOkSocketFactory = factory;
            return this;
        }

        /**
         * ���ûص����߳���,������UI�߳���.
         *
         * @param threadModeToken ���android���,����ʹ�ص���android�����߳���,
         *                        ��Ҫ�Լ�ʵ��handleCallbackEvent����.�ڷ�����ʹ��Handler.post(runnable)���лص�
         * @return
         */
        public Builder setCallbackThreadModeToken(ThreadModeToken threadModeToken) {
            mOptions.mCallbackThreadModeToken = threadModeToken;
            return this;
        }

        public OkSocketOptions build() {
            return mOptions;
        }
    }

    public IOThreadMode getIOThreadMode() {
        return mIOThreadMode;
    }

    public long getPulseFrequency() {
        return mPulseFrequency;
    }

    public OkSocketSSLConfig getSSLConfig() {
        return mSSLConfig;
    }

    public OkSocketFactory getOkSocketFactory() {
        return mOkSocketFactory;
    }

    public int getConnectTimeoutSecond() {
        return mConnectTimeoutSecond;
    }

    public boolean isConnectionHolden() {
        return isConnectionHolden;
    }

    public int getPulseFeedLoseTimes() {
        return mPulseFeedLoseTimes;
    }

    public AbsReconnectionManager getReconnectionManager() {
        return mReconnectionManager;
    }

    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public int getWritePackageBytes() {
        return mWritePackageBytes;
    }

    @Override
    public int getReadPackageBytes() {
        return mReadPackageBytes;
    }

    @Override
    public ByteOrder getWriteByteOrder() {
        return mWriteOrder;
    }

    @Override
    public IReaderProtocol getReaderProtocol() {
        return mReaderProtocol;
    }

    @Override
    public int getMaxReadDataMB() {
        return mMaxReadDataMB;
    }

    @Override
    public ByteOrder getReadByteOrder() {
        return mReadByteOrder;
    }

    public ThreadModeToken getCallbackThreadModeToken() {
        return mCallbackThreadModeToken;
    }

    public boolean isCallbackInIndependentThread() {
        return isCallbackInIndependentThread;
    }

    public static OkSocketOptions getDefault() {
        OkSocketOptions okOptions = new OkSocketOptions();
        okOptions.mPulseFrequency = 5 * 1000;
        okOptions.mIOThreadMode = IOThreadMode.DUPLEX;
        okOptions.mReaderProtocol = new DefaultNormalReaderProtocol();
        okOptions.mMaxReadDataMB = 5;
        okOptions.mConnectTimeoutSecond = 3;
        okOptions.mWritePackageBytes = 100;
        okOptions.mReadPackageBytes = 50;
        okOptions.mReadByteOrder = ByteOrder.BIG_ENDIAN;
        okOptions.mWriteOrder = ByteOrder.BIG_ENDIAN;
        okOptions.isConnectionHolden = true;
        okOptions.mPulseFeedLoseTimes = 5;
        okOptions.mReconnectionManager = new DefaultReconnectManager();
        okOptions.mSSLConfig = null;
        okOptions.mOkSocketFactory = null;
        okOptions.isCallbackInIndependentThread = true;
        okOptions.mCallbackThreadModeToken = null;
        return okOptions;
    }

    /**
     * �߳�ģʽ
     */
    public enum IOThreadMode {
        /**
         * ����ͨѶ
         */
        SIMPLEX,
        /**
         * ˫��ͨѶ
         */
        DUPLEX;
    }
}