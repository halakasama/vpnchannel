package com.halakasama.control.protocal.config;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.LocalContextHelper;
import com.halakasama.control.Message;
import com.halakasama.control.protocal.ProtocolType;
import com.halakasama.server.VirtualAddressManager;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class PushState implements ConfigPushState {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushState.class);
    private static PushState ourInstance = new PushState();

    public static PushState getInstance() {
        return ourInstance;
    }

    private PushState() {
    }

    /**
     * 服务器向客户端推送配置信息
     * @param message
     * @param configPush
     */
    @Override
    public void proceed(Message message, ConfigPush configPush) {
        ConnectContext connectContext = configPush.getConnectContext();
        SocketChannel socketChannel = connectContext.getSocketChannel();
        LocalContextHelper localContextHelper = configPush.getLocalContextHelper();

        String virtualAddress = VirtualAddressManager.getInstance().getVirtualAddress();
        byte[] content = StringUtils.getBytesUtf8(virtualAddress);

        //推送虚拟地址并注册在ServerContext
        Message.sendMessage(socketChannel, new Message(ProtocolType.ConfigProtocolPush,ConfigMessageType.VirtualAddress,content,content.length));
        localContextHelper.registerConnection(connectContext, virtualAddress);

        configPush.getSuccessor().trigger();
    }
}
