package com.halakasama;

import hexinfo.Dongle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Arrays;

/**
 * Created by root on 17-4-5.
 */
public class UKeyInfo {
    private static int keyLen = 16;

    private static final Logger LOGGER = LoggerFactory.getLogger(UKeyInfo.class);

    public static byte[] int2byte(int res) //将整型数转换成byte数组
    {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }
    public static int byte2int(byte[] res) //将byte数组转换成整型
    {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }
    public static void KeyCfg2UKey(int[] keyConfig, int ServerID) //将keyFile的属性keyCurt、keyLast写入加密狗中
    {
        //操作加密狗的准备工作
        byte [] dongleInfo = new byte [100];
        int [] count = new int[1];
        int [] handle = new int [1];
        int nRet = 0;
        int i = 0;

        Dongle dongle = new Dongle();
        //枚举锁
        nRet = dongle.Dongle_Enum(dongleInfo, count);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            return ;
        }

        //打开第一把锁
        nRet = dongle.Dongle_Open(handle, 0);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            return ;
        }
        //验证开发商密码
        int []nRemain = new int[1];
        String strPin = "FFFFFFFFFFFFFFFF"; //默认开发商密码
        nRet = dongle.Dongle_VerifyPIN(handle[0], dongle.FLAG_ADMINPIN, strPin, nRemain);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            dongle.Dongle_Close(handle[0]);
            return ;
        }

        //将keyFile的属性存储至加密狗的数据区
        /*System.out.println(keyConfig[0]);
        System.out.println(keyConfig[1]);
        */
        byte[] keyCurt = new byte[4];
        keyCurt = int2byte(keyConfig[0]);
        byte[] keyLast = new byte[4];
        keyLast = int2byte(keyConfig[1]);

        int configLen = keyConfig.length * (Integer.SIZE/8); //存储文件属性在数据区的长度
        int offSet = (ServerID-1)*configLen; //文件属性在数据存储区的偏移量

        //写数据区
        nRet = dongle.Dongle_WriteData(handle[0],  offSet, keyCurt, 4);
        nRet = dongle.Dongle_WriteData(handle[0], offSet+4, keyLast, 4);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            return ;
        }

        //关闭加密锁
        nRet = dongle.Dongle_Close(handle[0]);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            return;
        }
    }

    public static int[] GetKeyConfig(int fileID) { //获取密钥指针，fileID是密钥文件在ukey中的编号，与serverID一致
        //操作加密狗的准备工作
        int[] keyConfig = new int[2];

        byte[] dongleInfo = new byte[100];
        int[] count = new int[1];
        int[] handle = new int[1];
        int nRet = 0;
        int i = 0;

        Dongle dongle = new Dongle();
        //枚举锁
        nRet = dongle.Dongle_Enum(dongleInfo, count);

        //打开第一把锁
        nRet = dongle.Dongle_Open(handle, 0);

        //验证开发商密码
        int[] nRemain = new int[1];
        String strPin = "FFFFFFFFFFFFFFFF"; //默认开发商密码
        nRet = dongle.Dongle_VerifyPIN(handle[0], dongle.FLAG_ADMINPIN, strPin, nRemain);

        byte[] keyCurt = new byte[4];
        byte[] keyLast = new byte[4];
        int configLen = keyConfig.length * (Integer.SIZE / 8); //存储文件属性在数据区的长度
        int offSet = (fileID - 1) * configLen; //文件属性在数据存储区的偏移量
        nRet = dongle.Dongle_ReadData(handle[0], offSet, keyCurt, 4);
        nRet = dongle.Dongle_ReadData(handle[0], offSet + 4, keyLast, 4);

        keyConfig[0] = byte2int(keyCurt);
        keyConfig[1] = byte2int(keyLast);

        return keyConfig;
    }


    public static int getUKeyID() //获取加密狗的UID
    {
        byte [] dongleInfo = new byte [1024];
        int [] count = new int[1];
        int [] handle = new int [1];
        int nRet = 0;
        Dongle dongle = new Dongle();
        //枚举锁
        nRet = dongle.Dongle_Enum(dongleInfo, count);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            LOGGER.error("getUKeyID: Dongle_Enum error. error code: 0x " + String.format("%08X",nRet));
            System.exit(1);
        }

        int index = 0;//找到的第1把锁
        short []ver = new short [1];
        short []type = new short [1];
        byte []birthday = new byte [8];
        int []agent = new int[1];
        int []pid = new int[1];
        int []uid = new int[1];
        byte []hid = new byte[8];
        int []isMother = new int[1];
        int []devType = new int[1];

        nRet = dongle.GetDongleInfo(dongleInfo, 0, ver, type, birthday, agent, pid, uid, hid, isMother, devType);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            LOGGER.error("getUKeyID: GetDongleInfo error. error code: 0x " + String.format("%08X",nRet));
            System.exit(1);
        }

        return uid[0];
    }

    private static String GetUKeyPath(int UKeyID) { //获取加密狗的路径
        String strUKeyID = String.format("%08x",UKeyID);
        String strUKeyPath = "";

        FileSystemView sys = FileSystemView.getFileSystemView();
        File[] files = File.listRoots();
        for(int i = 0; i < files.length; i++) {
            String volumName = sys.getSystemDisplayName(files[i]);
            if (volumName.length() < 9)
                continue;
            if (strUKeyID.equals(volumName.substring(0,8))){
                strUKeyPath = String.valueOf(files[i]);
            }
        }
        return strUKeyPath;
    }

    public static byte[] GetKey(int UKeyID, int serverID) throws IOException { //获取密钥
        String ukeyPath = GetUKeyPath(UKeyID);
        int[] keyConfig = new int[2];
        keyConfig = GetKeyConfig(serverID);
        int keyCurt = keyConfig[0];
        int keyLast = keyConfig[1];
        long usedSize =  (keyCurt-1) * 16L;
        String strServerID = Integer.toString(serverID);
        FileInputStream fis = new FileInputStream( ukeyPath+'/'+strServerID);
        fis.skip(usedSize);

        byte[] buffer = new byte[keyLen];
        fis.read(buffer);
        return buffer;
    }

    public static void main(final String args[]) throws IOException {
        int[] keyConfig = new int[]{1,1};
        int serverID = 1;
        int[] temp = GetKeyConfig(serverID);
        System.out.println(Arrays.toString(temp));
        KeyCfg2UKey(keyConfig, serverID);

        int ukeyID = getUKeyID();
        System.out.println(String.valueOf(ukeyID));

        String ukeyPath = GetUKeyPath(ukeyID);
        System.out.println(ukeyPath);
        byte[] buffer = GetKey(ukeyID, serverID);
        System.out.println(Arrays.toString(buffer));
    }
}
