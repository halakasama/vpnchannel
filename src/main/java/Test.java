import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

/**
 * Created by pengfei.ren on 2017/3/14.
 */
public class Test {
    @Message // Annotation
    public static class MyMessage {
        // public fields are serialized.
        public String name;
        public double version;
        public byte[] data;
    }

    public static void main(String[] args) throws Exception {
        MyMessage src = new MyMessage();
        src.name = "msgpack";
        src.version = 0.6;
        src.data = new byte[]{1,2,3};

        MessagePack msgpack = new MessagePack();
        // Serialize
        byte[] bytes = msgpack.write(src);
        // Deserialize
        MyMessage dst = msgpack.read(bytes, MyMessage.class);
    }
}
