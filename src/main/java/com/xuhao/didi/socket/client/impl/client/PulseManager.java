package com.xuhao.didi.socket.client.impl.client;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.socket.client.impl.exceptions.DogDeadException;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.bean.IPulse;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuhao on 2017/5/18.
 */

public class PulseManager implements IPulse {
    /**
     * ���ݰ�������
     */
    private volatile IConnectionManager mManager;
    /**
     * �������ݰ�
     */
    private IPulseSendable mSendable;
    /**
     * ���Ӳ���
     */
    private volatile OkSocketOptions mOkOptions;
    /**
     * ��ǰƵ��
     */
    private volatile long mCurrentFrequency;
    /**
     * ��ǰ���߳�ģʽ
     */
    private volatile OkSocketOptions.IOThreadMode mCurrentThreadMode;
    /**
     * �Ƿ�����
     */
    private volatile boolean isDead = false;
    /**
     * ������©�Ĵ���
     */
    private volatile AtomicInteger mLoseTimes = new AtomicInteger(-1);

    private PulseThread mPulseThread = new PulseThread();

    PulseManager(IConnectionManager manager, OkSocketOptions okOptions) {
        mManager = manager;
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
    }

    public synchronized IPulse setPulseSendable(IPulseSendable sendable) {
        if (sendable != null) {
            mSendable = sendable;
        }
        return this;
    }

    public IPulseSendable getPulseSendable() {
        return mSendable;
    }

    @Override
    public synchronized void pulse() {
        privateDead();
        updateFrequency();
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX) {
            if (mPulseThread.isShutdown()) {
                mPulseThread.start();
            }
        }
    }

    @Override
    public synchronized void trigger() {
        if (isDead) {
            return;
        }
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX && mManager != null && mSendable != null) {
            mManager.send(mSendable);
        }
    }

    public synchronized void dead() {
        mLoseTimes.set(0);
        isDead = true;
        privateDead();
    }

    private synchronized void updateFrequency() {
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX) {
            mCurrentFrequency = mOkOptions.getPulseFrequency();
            mCurrentFrequency = mCurrentFrequency < 1000 ? 1000 : mCurrentFrequency;//�����СΪһ��
        } else {
            privateDead();
        }
    }

    @Override
    public synchronized void feed() {
        mLoseTimes.set(-1);
    }

    private void privateDead() {
        if (mPulseThread != null) {
            mPulseThread.shutdown();
        }
    }

    public int getLoseTimes() {
        return mLoseTimes.get();
    }

    protected synchronized void setOkOptions(OkSocketOptions okOptions) {
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
        updateFrequency();
    }

    private class PulseThread extends AbsLoopThread {

        @Override
        protected void runInLoopThread() throws Exception {
            if (isDead) {
                shutdown();
                return;
            }
            if (mManager != null && mSendable != null) {
                if (mOkOptions.getPulseFeedLoseTimes() != -1 && mLoseTimes.incrementAndGet() >= mOkOptions.getPulseFeedLoseTimes()) {
                    mManager.disconnect(new DogDeadException("you need feed dog on time,otherwise he will die"));
                } else {
                    mManager.send(mSendable);
                }
            }

            //not safety sleep.
            Thread.sleep(mCurrentFrequency);
        }

        @Override
        protected void loopFinish(Exception e) {
        }
    }


}
