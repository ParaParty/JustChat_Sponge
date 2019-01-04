package com.xuhao.didi.socket.client.sdk.client.action;

import com.xuhao.didi.core.iocore.interfaces.IOAction;

public interface IAction extends IOAction {
    //����key
    String ACTION_DATA = "action_data";
    //socket���߳�������Ӧ
    String ACTION_READ_THREAD_START = "action_read_thread_start";
    //socket���̹߳ر���Ӧ
    String ACTION_READ_THREAD_SHUTDOWN = "action_read_thread_shutdown";
    //socketд�߳�������Ӧ
    String ACTION_WRITE_THREAD_START = "action_write_thread_start";
    //socketд�̹߳ر���Ӧ
    String ACTION_WRITE_THREAD_SHUTDOWN = "action_write_thread_shutdown";
    //socket���ӷ������ɹ���Ӧ
    String ACTION_CONNECTION_SUCCESS = "action_connection_success";
    //socket���ӷ�����ʧ����Ӧ
    String ACTION_CONNECTION_FAILED = "action_connection_failed";
    //socket��������Ͽ�����
    String ACTION_DISCONNECTION = "action_disconnection";

}