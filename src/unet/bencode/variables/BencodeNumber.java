package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeVariable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

public class BencodeNumber implements BencodeVariable {

    private String n;
    private int s;

    public BencodeNumber(){
    }

    public BencodeNumber(String n){
        this.n = n;
        s = n.getBytes().length+2;
    }

    @Override
    public Number getObject(){
        try{
            return NumberFormat.getInstance().parse(n);
        }catch(ParseException e){
            return 0;
        }
    }

    @Override
    public int byteSize(){
        return s;
    }

    @Override
    public byte[] encode(){
        return ('i'+n+'e').getBytes();
    }

    @Override
    public void decode(byte[] buf, int off){
        byte[] c = new byte[32];
        off++;
        int s = off;

        while(buf[off] != 'e'){
            c[off-s] = buf[off];
            off++;
        }
        off++;

        int value = 0;
        for (int i = 0; i < c.length; i++) {
            value = (value << 8) | (c[i] & 0xFF);
        }

        //return new BencodeNumber(new String(c, 0, pos-s-1));
    }

    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) | (bytes[i] & 0xFF);
        }
        return value;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof BencodeNumber){
            return Arrays.equals(encode(), ((BencodeNumber) o).encode());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return 1;
    }
}
