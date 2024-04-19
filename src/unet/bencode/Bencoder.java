package unet.bencode;

import unet.bencode.variables.*;
import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bencoder {

    private byte[] buf;
    private int pos = 0;

    public List<BencodeVariable> decodeArray(byte[] buf){
        this.buf = buf;
        if(buf[0] != 'l'){
            throw new IllegalArgumentException("Can't decode, byte array is not bencode array.");
        }
        return getList();
    }

    public Map<BencodeBytes, BencodeVariable> decodeObject(byte[] buf){
        this.buf = buf;
        if(buf[0] != 'd'){
            throw new IllegalArgumentException("Can't decode, byte array is not bencode object.");
        }
        return getMap();
    }

    private void test(byte[] buf){
        char c = 'c';
        switch(BencodeType.getTypeByPrefix(c)){
            case NUMBER:
                break;
        }
    }

    private BencodeVariable get(){
        switch(buf[pos]){
            case 'i':
                return getNumber();

            case 'l':
                return new BencodeArray(getList());

            case 'd':
                return new BencodeObject(getMap());

            default:
                if(buf[pos] >= '0' && buf[pos] <= '9'){
                    return getBytes();
                }
                break;
        }

        return null;
    }

    private BencodeNumber getNumber(){
        char[] c = new char[32];
        pos++;
        int s = pos;

        while(buf[pos] != 'e'){
            c[pos-s] = (char) buf[pos];
            pos++;
        }
        pos++;

        return null;//new BencodeNumber(new String(c, 0, pos-s-1));
    }

    private BencodeBytes getBytes(){
        char[] c = new char[8];
        int s = pos;

        while(buf[pos] != ':'){
            c[pos-s] = (char) buf[pos];
            pos++;
        }

        byte[] b = new byte[Integer.parseInt(new String(c, 0, pos-s))];
        System.arraycopy(buf, pos+1, b, 0, b.length);
        pos += b.length+1;

        return new BencodeBytes(b);
    }

    private List<BencodeVariable> getList(){
        List<BencodeVariable> a = new ArrayList<>();
        pos++;

        while(buf[pos] != 'e'){
            a.add(get());
        }
        pos++;

        return a;
    }

    private Map<BencodeBytes, BencodeVariable> getMap(){
        Map<BencodeBytes, BencodeVariable> m = new HashMap<>();
        pos++;

        while(buf[pos] != 'e'){
            m.put(getBytes(), get());
        }
        pos++;

        return m;
    }
}
