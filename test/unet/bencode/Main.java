package unet.bencode;

import unet.bencode.variables.BencodeBytes;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;

public class Main {

    public static void main(String[] args){
        BencodeObject o = new BencodeObject();
        o.put("animal", "bark");
        o.put("cat", "dog");
        //o.put("test", 100);

        byte[] buf = o.encode();
        System.out.println(new String(buf));

        o = new BencodeObject();
        o.decode(buf, 0);
        System.out.println(o);
    }
}
