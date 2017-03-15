package com.halakasama.config;

import com.google.common.collect.Maps;
import com.halakasama.vpn.ClientSocket;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class Configuration {
    private Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    public InetAddress serverAddress;
    public int serverPort;
    public int clientPort;

    public String virtualNetwork;
    public String virtualMask;
    public String virtualBroadcast;

    public Configuration(String configPath) {
        Map object = null;
        try {
            object = (Map) JSONValue.parse(new FileInputStream(configPath));
        } catch (FileNotFoundException e) {
            LOGGER.error("Config file path is not valid.",e);
        }

        try {
            serverAddress = InetAddress.getByName((String) object.get("serverAddress"));
        } catch (UnknownHostException e) {
            LOGGER.error("Server address is not valid.",e);
        }
        serverPort = (Integer) object.get("serverPort");
        clientPort = (Integer) object.get("clientPort");
        virtualNetwork = (String) object.get("virtualNetwork");
        virtualMask = (String) object.get("virtualMask");
        virtualBroadcast = (String)object.get("virtualBroadcast");
    }




    public static Configuration getConfiguration(String path, boolean isClient, int clientIndex){
        if (isClient){
            return new ClientConfiguration(path,clientIndex);
        }else {
            return new ServerConfiguration(path);
        }
    }
}
