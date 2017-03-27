package com.halakasama.control;

import java.nio.channels.SelectionKey;

/**
 * Created by admin on 2017/3/27.
 */
public interface ControlChannelHandler {
    void handleTcpEvent(SelectionKey selectionKey);
}
