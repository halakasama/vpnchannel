import com.google.common.collect.Lists;

import java.nio.ByteBuffer;

/**
 * Created by admin on 2017/3/27.
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        byteBuffer.clear();
        byteBuffer.put(new byte[]{0,1,2,3,4,5,6,7,8,9});
        byteBuffer.flip();
        byteBuffer.getInt();
        byte[] array = new byte[10];
        byteBuffer.get(array,0,byteBuffer.remaining());
        System.out.println(Lists.newArrayList(array));
        System.out.println(byteBuffer.remaining());
    }
}
