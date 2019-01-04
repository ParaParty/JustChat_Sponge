package com.xuhao.didi.socket.client.sdk.client;

import java.io.Serializable;

/**
 * ������Ϣ������
 * Created by xuhao on 2017/5/16.
 */
public final class ConnectionInfo implements Serializable, Cloneable {
    /**
     * IPV4��ַ
     */
    private String mIp;
    /**
     * ���ӷ������˿ں�
     */
    private int mPort;
    /**
     * ����IP��ַPing��ͨʱ�ı���IP
     */
    private ConnectionInfo mBackupInfo;

    public ConnectionInfo(String ip, int port) {
        this.mIp = ip;
        this.mPort = port;
    }

    /**
     * ��ȡ�����IP��ַ
     *
     * @return ip��ַ
     */
    public String getIp() {
        return mIp;
    }

    /**
     * ��ȡ����Ķ˿ں�
     *
     * @return �˿ں�
     */
    public int getPort() {
        return mPort;
    }

    /**
     * ��ȡ���õ�Ip�Ͷ˿ں�
     *
     * @return ���õĶ˿ںź�IP��ַ
     */
    public ConnectionInfo getBackupInfo() {
        return mBackupInfo;
    }

    /**
     * ���ñ��õ�IP�Ͷ˿ں�,���Բ�����
     *
     * @param backupInfo ���õ�IP�Ͷ˿ں���Ϣ
     */
    public void setBackupInfo(ConnectionInfo backupInfo) {
        mBackupInfo = backupInfo;
    }

    @Override
    public ConnectionInfo clone() {
        ConnectionInfo connectionInfo = new ConnectionInfo(mIp, mPort);
        if (mBackupInfo != null) {
            connectionInfo.setBackupInfo(mBackupInfo.clone());
        }
        return connectionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ConnectionInfo)) { return false; }

        ConnectionInfo connectInfo = (ConnectionInfo) o;

        if (mPort != connectInfo.mPort) { return false; }
        return mIp.equals(connectInfo.mIp);
    }

    @Override
    public int hashCode() {
        int result = mIp.hashCode();
        result = 31 * result + mPort;
        return result;
    }
}
