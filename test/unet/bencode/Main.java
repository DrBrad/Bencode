package unet.bencode;

import unet.bencode.variables.BencodeArray;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;

public class Main {

    public static void main(String[] args){
        BencodeObject o = new BencodeObject();
        o.put("animal", "bark");
        o.put("cat", "dog");
        o.put("test", new BencodeObject());
        o.getBencodeObject("test").put("NEW", "ANOTHER");
        o.put("test2", 100);
        o.put("asd", new BencodeArray());
        o.getBencodeArray("asd").add(1034);
        o.getBencodeArray("asd").add("BOOP");
        o.put("test3", 130);

        byte[] buf = o.encode();
        System.out.println(new String(buf));

        o = new BencodeObject(buf);
        System.out.println(o);

        BencodeNumber n = new BencodeNumber(100.2);
        buf = n.encode();

        n = new BencodeNumber();
        n.decode(buf, 0);

        System.out.println(n);
    }
}
