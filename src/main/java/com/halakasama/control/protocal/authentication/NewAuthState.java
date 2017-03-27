package com.halakasama.control.protocal.authentication;

import com.halakasama.control.ConnectContext;
import com.halakasama.control.protocal.ProtoStateAdapter;

/**
 * Created by pengfei.ren on 2017/3/26.
 */
public class NewAuthState extends ProtoStateAdapter{
    public NewAuthState(ConnectContext connectContext) {
        super(connectContext);
    }

    @Override
    public void proceed() {

    }
}
