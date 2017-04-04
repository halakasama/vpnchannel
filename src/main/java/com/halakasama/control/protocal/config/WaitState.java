package com.halakasama.control.protocal.config;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class WaitState implements ConfigWaitState {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitState.class);
    private static WaitState ourInstance = new WaitState();

    public static WaitState getInstance() {
        return ourInstance;
    }

    private WaitState() {
    }

    @Override
    public void proceed(Message message, ConfigWait configWait) {
        ConnectContext connectContext = configWait.getConnectContext();
        LocalContextHelper localContextHelper = configWait.getLocalContextHelper();

        //检查消息是否合法
        if ( !ConfigMessageType.isVirtualAddress(message.msgType) ){
            LOGGER.error("Wrong message received. {}",message);
            return;
        }

        //注册本机虚拟地址,并启动数据通道处理线程
        String virtualAddress = StringUtils.newStringUtf8(message.content);
        LOGGER.info("Local virtual address is {}.", virtualAddress);
        localContextHelper.registerConnection(connectContext, virtualAddress);
        //todo 启动数据通道处理线程
    }
}
