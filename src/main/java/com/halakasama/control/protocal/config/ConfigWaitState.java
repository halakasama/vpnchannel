package com.halakasama.control.protocal.config;

import com.halakasama.control.Message;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public interface ConfigWaitState {
    void proceed(Message message, ConfigWait configWait);

}
