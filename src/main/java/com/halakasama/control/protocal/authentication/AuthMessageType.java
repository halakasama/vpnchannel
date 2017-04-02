package com.halakasama.control.protocal.authentication;

/**
 * Created by admin on 2017/3/28.
 */
public class AuthMessageType {
    public static final short AuthRequest = 0; //发送本机身份代码，请求认证
    public static final short ChallengeCode = 1;//对方发送挑战随机码，进行挑战
    public static final short ChallengeResponse = 2;//本机发送应答消息
    public static final short AuthResult = 3;//对方发送认证结果
    public static final short AuthMessageTypeNum = 5;

    public static boolean isValidAuthMessage(short messageType){
        return messageType < AuthMessageTypeNum && messageType >= 0;
    }

    public static boolean isAuthRequest(short msgType){
        return msgType == AuthRequest;
    }
    public static boolean isChallengeCode(short msgType){
        return msgType == ChallengeCode;
    }

    public static boolean isChallengeResponse(short msgType){
        return msgType == ChallengeResponse;
    }

    public static boolean isAuthResult(short msgType){
        return msgType == AuthResult;
    }
}
