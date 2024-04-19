package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

public class BencodeNumber extends BencodeVariable {

    private int n;
    private int s;

    public BencodeNumber(){
        type = BencodeType.NUMBER;
    }

    public BencodeNumber(int n){
        this();
        this.n = n;

        s = 2;
        int t = n;
        while(t != 0){
            t /= 10;
            s++;
        }
        //this.n = n;
        //s = n+2;
    }

    @Override
    public Number getObject(){
        return n;
    }

    @Override
    public int byteSize(){
        return s;
    }

    @Override
    public byte[] encode(){
        byte[] b = new byte[s];
        b[0] = (byte) type.getPrefix();
        b[s-1] = (byte) type.getSuffix();
        int t = n;

        for(int i = s-2; i >= 1; i--){
            b[i] = (byte) (t%10);
            System.out.println(i+"  "+b[i]);
            t /= 10;
        }

        b[1] = (char) 3;

        return b;
    }

    @Override
    public void decode(byte[] buf, int off){
        if(!BencodeType.getTypeByPrefix((char) buf[off]).equals(type)){
            throw new IllegalArgumentException("Byte array is not a bencode bytes / string.");
        }

        byte[] c = new byte[32];
        off++;
        int s = off;

        while(buf[off] != type.getSuffix()){
            c[off-s] = buf[off];
            off++;
        }
        off++;

        n = 0;
        for(int i = 0; i < off-s; i++){
            n = n*10+(c[i]-'0');
        }
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
