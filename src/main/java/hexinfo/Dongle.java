//package com.ft.rockey;
package hexinfo;
//import java.io.*;
//package com.FTSafe;

import com.google.common.io.Resources;

public class Dongle
{
    public static final int CONST_PID = 0xFFFFFFFF;   //出厂时默认的PID

    public static final String CONST_USERPIN = "12345678";  //出厂时默认的USERPIN
    public static final String CONST_ADMINPIN = "FFFFFFFFFFFFFFFF"; //出厂时默认的ADMINPIN

    //加密锁协议类型
    public static final int PROTOCOL_HID = 0; //hid协议
    public static final int PROTOCOL_CCID = 1; //ccid协议

    //文件类型
    public static final int FILE_DATA = 1; //普通数据文件
    public static final int FILE_PRIKEY_RSA = 2; //RSA私钥文件
    public static final int FILE_PRIKEY_ECCSM2 = 3; //ECC或者SM2私钥文件(SM2私钥文件是ECC私钥文件的子集)
    public static final int FILE_KEY = 4; //SM4和3DES密钥文件
    public static final int FILE_EXE = 5; //可执行文件

    //LED灯状态定义
    public static final int LED_OFF = 0; //灯灭
    public static final int LED_ON = 1; //灯亮
    public static final int LED_BLINK = 2;//灯闪

    //PIN码类型
    public static final int FLAG_USERPIN = 0;//用户PIN
    public static final int FLAG_ADMINPIN = 1;//开发商PIN

    //加解密标志
    public static final int FLAG_ENCODE = 0;//加密
    public static final int FLAG_DECODE = 1; //解密

    //HASH算法类型
    public static final int FLAG_HASH_MD5 = 0; //MD5     运算结果16字节
    public static final int FLAG_HASH_SHA1 = 1;//SHA1    运算结果20字节
    public static final int FLAG_HASH_SM3 = 2; //SM3     运算结果32字节


    //远程升级的功能号
    public static final int UPDATE_FUNC_CreateFile = 1; //创建文件
    public static final int UPDATE_FUNC_WriteFile = 2; //写文件
    public static final int UPDATE_FUNC_DeleteFile = 3; //删除文件
    public static final int UPDATE_FUNC_FileLic = 4; //设置文件授权
    public static final int UPDATE_FUNC_SeedCount = 5; //设置种子码可运算次数
    public static final int UPDATE_FUNC_DownloadExe = 6; //升级可执行文件
    public static final int UPDATE_FUNC_UnlockUserPin = 7; //解锁用户PIN
    public static final int UPDATE_FUNC_Deadline = 8;//时钟锁升级使用期限


    //错误码
    public static final int DONGLE_SUCCESS = 0x00000000;            // 操作成功
    public static final int DONGLE_NOT_FOUND = 0xF0000001;          // 未找到指定的设备
    public static final int DONGLE_INVALID_HANDLE = 0xF0000002;     // 无效的句柄
    public static final int DONGLE_INVALID_PARAMETER = 0xF0000003;  // 参数错误
    public static final int DONGLE_COMM_ERROR = 0xF0000004;		   // 通讯错误
    public static final int DONGLE_INSUFFICIENT_BUFFER = 0xF0000005;// 缓冲区空间不足
    public static final int DONGLE_NOT_INITIALIZED = 0xF0000006;	   // 产品尚未初始化 (即没设置PID)
    public static final int DONGLE_ALREADY_INITIALIZED = 0xF0000007;// 产品已经初始化 (即已设置PID)
    public static final int DONGLE_ADMINPIN_NOT_CHECK = 0xF0000008; // 开发商密码没有验证
    public static final int DONGLE_USERPIN_NOT_CHECK = 0xF0000009;  // 用户密码没有验证
    public static final int DONGLE_INCORRECT_PIN = 0xF000FF00;	   // 密码不正确 (后2位指示剩余次数)
    public static final int DONGLE_PIN_BLOCKED = 0xF000000A;		   // PIN码已锁死
    public static final int DONGLE_ACCESS_DENIED = 0xF000000B;	   // 访问被拒绝
    public static final int DONGLE_FILE_EXIST = 0xF000000E;		   // 文件已存在
    public static final int DONGLE_FILE_NOT_FOUND = 0xF000000F;	   // 未找到指定的文件
    public static final int DONGLE_READ_ERROR = 0xF0000010;         // 读取数据错误
    public static final int DONGLE_WRITE_ERROR = 0xF0000011;        // 写入数据错误
    public static final int DONGLE_FILE_CREATE_ERROR = 0xF0000012;  // 创建文件或文件夹错误
    public static final int DONGLE_FILE_READ_ERROR = 0xF0000013;    // 读取文件错误
    public static final int DONGLE_FILE_WRITE_ERROR = 0xF0000014;   // 写入文件错误
    public static final int DONGLE_FILE_DEL_ERROR = 0xF0000015;     // 删除文件或文件夹错误
    public static final int DONGLE_FAILED = 0xF0000016;             // 操作失败
    public static final int DONGLE_CLOCK_EXPIRE = 0xF0000017;       // 加密锁时钟到期
    public static final int DONGLE_ERROR_UNKNOWN = 0xFFFFFFFF;      // 未知的错误

    public native int Dongle_Enum(byte[] dongleInfo, int [] count);
    public native int Dongle_Open(int [] handle, int index);
    public native int Dongle_ResetState(int handle);
    public native int Dongle_Close(int handle);
    public native int Dongle_GenRandom(int handle, int len, byte [] random);
    public native int Dongle_LEDControl(int handle, int flag);
    public native int Dongle_SwitchProtocol(int [] handle, int flag);
    public native int Dongle_RFS(int [] handle);
    public native int Dongle_CreateFile(int handle, int fileType, int fileID, byte [] fileAttr);
    public native int Dongle_WriteFile(int handle, int fileType, int fileID, int offset, byte [] data, int dataLen);
    public native int Dongle_ReadFile(int handle, int fileID, int offset, byte []outData, int dataLen);
    public native int Dongle_DownloadExeFile(int handle, byte[] exeFileInfo, int count);
    public native int Dongle_RunExeFile(int handle, int fileID, byte [] inOutData, int inOutDataLen, int [] mainRet);
    public native int Dongle_DeleteFile(int handle, int fileType, int fileID);
    public native int Dongle_ListFile(int handle, int fileType, byte []fileList, int [] dataLen);
    public native int Dongle_GenUniqueKey(int handle, int seedLen, byte [] seed, String [] pid, String [] adminPin);
    public native int Dongle_VerifyPIN(int handle, int flag, String pin, int [] remainCount);
    public native int Dongle_ChangePIN(int handle, int flag, String oldPin, String newPin, int tryCount);
    public native int Dongle_ResetUserPIN(int handle, String adminPin);
    public native int Dongle_SetUserID(int handle, int userID);
    public native int Dongle_GetDeadline(int handle, int [] time);
    public native int Dongle_SetDeadline(int handle, int time);
    public native int Dongle_GetUTCTime(int handle, int [] time);
    public native int Dongle_ReadData(int handle, int offset, byte [] data, int dateLen);
    public native int Dongle_WriteData(int handle, int offset, byte [] data, int dataLen);
    public native int Dongle_ReadShareMemory(int handle, byte [] data);
    public native int Dongle_WriteShareMemory(int handle, byte [] data, int datelen);
    public native int Dongle_RsaGenPubPriKey(int handle, int priFileID, byte [] pubBakup, int []pubLen, byte [] priBakup, int []priLen);
    public native int Dongle_RsaPri(int handle, int priFileID, int flag, byte [] inData, int inDataLen, byte [] outData, int [] outDataLen);
    public native int Dongle_RsaPub(int handle, int flag, byte [] pubKey, byte [] inData, int inDataLen, byte [] outData, int [] outDataLen);
    public native int Dongle_EccGenPubPriKey(int handle, int priFileID, byte [] pubBakup, int []pubLen, byte [] priBakup, int []priLen);
    public native int Dongle_EccSign(int handle, int priFileID, byte [] hashData, int hashDataLen, byte [] outData);
    public native int Dongle_EccVerify(int handle, byte [] pubKey, byte [] hashdata, int hashDataLen, byte [] sign);
    public native int Dongle_SM2GenPubPriKey(int handle, int priFileID, byte [] pubBakup, int []pubLen, byte [] priBakup, int []priLen);
    public native int Dongle_SM2Sign(int handle, int priFileID, byte [] hashData, int hashDataLen, byte [] outData);
    public native int Dongle_SM2Verify(int handle, byte [] pubKey, byte [] hashdata, int hashDataLen, byte [] sign);
    public native int Dongle_TDES(int handle, int keyFileID, int flag, byte [] inData, byte [] outData, int dataLen);
    public native int Dongle_SM4(int handle, int keyFileID, int flag, byte [] inData, byte [] outData, int dataLen);
    public native int Dongle_HASH(int handle, int flag, byte [] inData, int inDataLen, byte [] hash);
    public native int Dongle_Seed(int handle, byte [] seed, int seedLen, byte [] outData);
    public native int Dongle_LimitSeedCount(int handle, int count);
    public native int Dongle_GenMotherKey(int handle, byte [] moterData);
    public native int Dongle_RequestInit(int handle, byte [] request);
    public native int Dongle_GetInitDataFromMother(int handle, byte [] request, byte [] initData, int []dataLen);
    public native int Dongle_InitSon(int handle, byte [] initData, int dataLen);
    public native int Dongle_SetUpdatePriKey(int handle, byte [] priKey);
    public native int Dongle_MakeUpdatePacket(int handle, String shid, int func, int fileType, int fileID, int offset, byte []buffer, int bufferLen, byte [] uPubKey, byte [] outData, int [] outDataLen);
    public native int Dongle_MakeUpdatePacketFromMother(int handle, String shid, int func, int fileType, int fileID, int offset, byte []buffer, int bufferLen, byte [] outData, int [] outDataLen);
    public native int Dongle_Update(int handle, byte [] updateData, int dataLen);

    public native int GetDongleInfo(byte []dongleInfo, int index, short []ver, short []type, byte []birthday, int []agent, int []pid, int []uid, byte[]hid, int []isMother, int []devType);
    public native int Convert_DATA_LIC_To_Buffer(short readPriv, short writePriv, byte [] buffer, int [] bufferLen);
    public native int Convert_PRIKEY_LIC_To_Buffer(int count, byte callPriv, byte isDecOnRAM, byte isReset, byte []buffer, int[]bufferLen);
    public native int Convert_KEY_LIC_To_Buffer(int privEnc, byte []buffer, int []bufferLen);
    public native int Convert_EXE_LIC_To_Buffer(short privExe, byte []buffer, int []bufferLen);
    public native int Convert_DATA_FILE_ATTR_To_Buffer(int size, byte []dataLic, int dataLicLen, byte []buffer, int []bufferLen);
    public native int Convert_PRIKEY_FILE_ATTR_To_Buffer(short type, short size, byte []prikeyLic, int prikeyLicLen, byte []buffer, int []bufferLen);
    public native int Convert_KEY_FILE_ATTR_To_Buffer(int size, byte []keyLic, int keyLicLen, byte []buffer, int []bufferLen);
    public native int Convert_EXE_FILE_ATTR_To_Buffer(short size, byte []exeLic, int exeLicLen, byte []buffer, int []bufferLen);
    public native int Get_DATA_FILE_LIST_Info(byte[]fileList, int fileListLen,int index, short []fileID, int []fileSize, short []readPriv, short []writePriv);
    public native int Get_EXE_FILE_LIST_Info(byte[]fileList, int fileListLen, int index, short []fileID, short []fileSize, short []callPriv);
    public native int Get_KEY_FILE_LIST_Info(byte[]fileList, int fileListLen, int index, short []fileID, int []fileSize, int []encPriv);
    public native int Get_PRIKEY_FILE_LIST_Info(byte[]fileList, int fileListLen, int index, short []fileID, short []type, short []fileSize, int []count, byte []priv, byte []isDecOnRAM, byte []isReset);
    public native int Add_EXE_FILE_INFO_To_Buffer(byte []fileInfoBuffer, int[]fileInfoBufferLen, int index, short fileID, short fileSize, byte filePriv, byte [] exeBuffer);
    public native int Clear_EXE_FILE_INFO_Buffer(byte []fileInfoBuffer, int count);
    //Convert Struct to Buffer
    //Convert Buffer to Struct
    static
    {
        System.load(Resources.getResource("clib/Dongle_javax64.dll").getPath());
    }
}
