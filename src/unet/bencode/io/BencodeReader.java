package unet.bencode.io;

import unet.bencode.variables.*;
import unet.bencode.variables.inter.BencodeVariable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BencodeReader {

    private InputStream in;

    public BencodeReader(InputStream in){
        this.in = in;
    }

    public BencodeVariable read()throws IOException {
        return get((byte) in.read());
    }

    public void close()throws IOException {
        in.close();
    }

    private BencodeVariable get(byte b)throws IOException {
        switch(b){
            case 'i':
                return getNumber();

            case 'l':
                return getList();

            case 'd':
                return getMap();

            default:
                if(b >= '0' && b <= '9'){
                    return getBytes(b);
                }
                break;
        }

        return null;
    }

    private BencodeNumber getNumber()throws IOException {
        char[] c = new char[32];
        int i = 0;

        byte b;
        while((b = (byte) in.read()) != 'e'){
            c[i] = (char) b;
            i++;
        }

        return new BencodeNumber(new String(c, 0, i));
    }

    private BencodeBytes getBytes(byte b)throws IOException {
        char[] c = new char[8];
        c[0] = (char) b;
        int i = 1;

        while((b = (byte) in.read()) != ':'){
            c[i] = (char) b;
            i++;
        }

        byte[] buf = new byte[Integer.parseInt(new String(c, 0, i))];
        in.read(buf, 0, buf.length);

        return new BencodeBytes(buf);
    }

    private BencodeArray getList()throws IOException {
        List<BencodeVariable> a = new ArrayList<>();

        byte b;
        while((b = (byte) in.read()) != 'e'){
            a.add(get(b));
        }

        return new BencodeArray(a);
    }

    private BencodeObject getMap()throws IOException {
        Map<BencodeBytes, BencodeVariable> m = new HashMap<>();

        byte b;
        while((b = (byte) in.read()) != 'e'){
            BencodeBytes key = getBytes(b);
            m.put(key, get((byte) in.read()));
        }

        return new BencodeObject(m);
    }
}
