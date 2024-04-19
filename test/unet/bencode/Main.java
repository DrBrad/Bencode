package unet.bencode;

import unet.bencode.variables.BencodeBytes;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;

public class Main {

    public static void main(String[] args){
        BencodeObject o = new BencodeObject();
        o.put("animal", "bark");
        o.put("cat", "dog");

        byte[] buf = o.encode();
        System.out.println(new String(buf));

        o = new BencodeObject();
        o.decode(buf, 0);
        System.out.println(o);

        BencodeNumber n = new BencodeNumber(100);
        buf = n.encode();
        System.out.println(new String(buf));
        n = new BencodeNumber();
        n.decode(buf, 0);
        System.out.println(n.getObject());

        int x = 100;
        String s = ""+x;
        byte[] z = s.getBytes();
        System.out.println(new String(z));
    }
}
