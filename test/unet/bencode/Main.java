package unet.bencode;

import unet.bencode.io.BencodeReader;
import unet.bencode.variables.BencodeArray;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args)throws IOException {
        BencodeObject o = new BencodeObject();
        o.put("b", "bar");
        o.put("c", "far");
        o.put("n", 100);
        o.put("y",  new byte[]{ 0, 0, 0, 0, 0, 0 });

        o.put("array", new BencodeArray());
        o.getBencodeArray("array").add("n");
        o.getBencodeArray("array").add(123.56);

        o.put("object", new BencodeObject());
        o.getBencodeObject("object").put("z", "another one");
        o.getBencodeObject("object").put("n", "mutate");

        byte[] buf = o.encode();
        System.out.println("EXPECTED: "+o.byteSize()+" ACTUAL: "+buf.length);
        System.out.println(new String(buf));
        System.out.println(o);

        o = new BencodeObject(buf);
        System.out.println(o);
    }
}
