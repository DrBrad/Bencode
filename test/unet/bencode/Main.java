package unet.bencode;

import unet.bencode.utils.BencodeUtils;
import unet.bencode.variables.BencodeBytes;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        BencodeObject o = new BencodeObject();
        o.put("animal", "bark");
        o.put("cat", "dog");
        o.put("test", new BencodeObject());
        //o.getBencodeObject("test").put("NEW", "ANOTHER");
        //o.put("test", 100);

        byte[] buf = o.encode();
        System.out.println(new String(buf));

        o = new BencodeObject();
        o.decode(buf, 0);
        System.out.println(o);

        BencodeNumber n = new BencodeNumber(100.2);
        buf = n.encode();

        n = new BencodeNumber();
        n.decode(buf, 0);

        System.out.println(n);
        /*
        Number n = 100;
        System.out.println(new String(n.toString().getBytes()));

        int intValue = 12345;
        double doubleValue = 12345.67890500;

        // Convert int to char array
        char[] intCharArray = BencodeUtils.numberToByteArray(intValue);
        System.out.println("Integer as char array: " + new String(intCharArray));

        // Convert double to char array
        char[] doubleCharArray = BencodeUtils.numberToByteArray(doubleValue);
        System.out.println("Double as char array: " + new String(doubleCharArray));
        */
    }

}
