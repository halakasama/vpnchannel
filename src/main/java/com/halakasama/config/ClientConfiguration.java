package com.halakasama.config;

import com.halakasama.vpn.ClientSocket;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class ClientConfiguration extends Configuration{
    private Logger LOGGER = LoggerFactory.getLogger(ClientConfiguration.class);
    public ClientSocket clientSocket;

    public ClientConfiguration(String configPath, int clientIndex){
        super(configPath);
        Map object = null;
        try {
            object = (Map) JSONValue.parse(new FileInputStream(configPath));
        } catch (FileNotFoundException e) {
            LOGGER.error("Config file path is not valid.",e);
        }
        List<Map> list = (List<Map>)object.get("clients");
        Map i = list.get(clientIndex);
        String virtualAddress = (String)i.get("virtualAddress");
        String physicalAddress = (String)i.get("physicalAddress");
        clientSocket = new ClientSocket(physicalAddress,virtualAddress,super.clientPort);
    }
}