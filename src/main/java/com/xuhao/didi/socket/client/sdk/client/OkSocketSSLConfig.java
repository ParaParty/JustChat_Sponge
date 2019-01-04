package com.xuhao.didi.socket.client.sdk.client;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by Tony on 2017/12/27.
 */

public class OkSocketSSLConfig {
    /**
     * ��ȫЭ������(ȱʡΪ SSL)
     */
    private String mProtocol;
    /**
     * ����֤�������(ȱʡΪ X509)
     */
    private TrustManager[] mTrustManagers;
    /**
     * ֤����Կ������(ȱʡΪ null)
     */
    private KeyManager[] mKeyManagers;
    /**
     * �Զ��� SSLFactory(ȱʡΪ null)
     */
    private SSLSocketFactory mCustomSSLFactory;

    private OkSocketSSLConfig() {

    }

    public static class Builder {
        private OkSocketSSLConfig mConfig;

        public Builder() {
            mConfig = new OkSocketSSLConfig();
        }

        public Builder setProtocol(String protocol) {
            mConfig.mProtocol = protocol;
            return this;
        }

        public Builder setTrustManagers(TrustManager[] trustManagers) {
            mConfig.mTrustManagers = trustManagers;
            return this;
        }

        public Builder setKeyManagers(KeyManager[] keyManagers) {
            mConfig.mKeyManagers = keyManagers;
            return this;
        }

        public Builder setCustomSSLFactory(SSLSocketFactory customSSLFactory) {
            mConfig.mCustomSSLFactory = customSSLFactory;
            return this;
        }

        public OkSocketSSLConfig build() {
            return mConfig;
        }
    }

    public KeyManager[] getKeyManagers() {
        return mKeyManagers;
    }

    public String getProtocol() {
        return mProtocol;
    }

    public TrustManager[] getTrustManagers() {
        return mTrustManagers;
    }

    public SSLSocketFactory getCustomSSLFactory() {
        return mCustomSSLFactory;
    }
}
