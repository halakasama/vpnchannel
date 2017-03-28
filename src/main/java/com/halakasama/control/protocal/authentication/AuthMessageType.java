package com.halakasama.control.protocal.authentication;

/**
 * Created by admin on 2017/3/28.
 */
public class AuthMessageType {
    public static final int AuthRequest = 0;
    public static final int ChallengeCode = 1;
    public static final int ChallengeResponse = 2;
    public static final int AuthResult = 3;
    public static final int AuthMessageTypeNum = 4;

    public boolean isValidAuthMessage(int messageType){
        return messageType < AuthMessageTypeNum && messageType >= 0;
    }
}
