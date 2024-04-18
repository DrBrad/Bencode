package unet.bencode;

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
    }
}
