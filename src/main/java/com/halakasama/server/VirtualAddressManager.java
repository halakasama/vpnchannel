package com.halakasama.server;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pengfei.ren on 2017/4/2.
 */
public class VirtualAddressManager {
    private static VirtualAddressManager ourInstance = new VirtualAddressManager();

    private static final String SUBNET = "10.9.8.";
    private Set<String> addressInUse;

    public static VirtualAddressManager getInstance() {
        return ourInstance;
    }

    private VirtualAddressManager() {
        addressInUse = new HashSet<>();
    }


    public synchronized String getVirtualAddress(){
        for (int i = 1; i < 255; ++i){
            if ( !addressInUse.contains(SUBNET + i) ){
                addressInUse.add(SUBNET + i);
                return SUBNET + i;
            }
        }
        return "";
    }
    public synchronized void removeVirtualAddress(String address){
        addressInUse.remove(address);
    }
}
