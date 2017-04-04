import com.google.common.io.Resources;
import com.halakasama.vpn.VPNClient;

/**
 * Created by pengfei.ren on 2017/3/12.
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        MessagePack msgPack = new MessagePack();
//        msgPack.register(DataPacket.class);
//        byte[] buf = msgPack.write(new DataPacket("192.1","192.2",new byte[]{1,2,3}));

//        String configPath = args[0];
        String mode = args[0];
        String configPath = Resources.getResource("server_config.json").getPath();
//        String configPath = Resources.getResource(Main.class,"server_config.json").getPath();
//        String configPath = Main.class.getResource("server_config.json").getPath();
        System.out.println(configPath);
        if (mode.equals("server")){
//            ServerDataChannel serverDataChannel = new ServerDataChannel(configPath);
//            serverDataChannel.serve();
        }else {
            int clientIndex = Integer.parseInt(args[1]);
            VPNClient vpnClient = new VPNClient(configPath,clientIndex);
            vpnClient.run();
        }
    }

}
