package com.halakasama.control.protocal.config;


import com.halakasama.control.Message;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public interface ConfigPushState {
    void proceed(Message message, ConfigPush configPush);
}
