package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

public class BencodeNumber extends BencodeVariable {

    private Number n;
    private int s;

    public BencodeNumber(){
        type = BencodeType.NUMBER;
    }

    public BencodeNumber(Number n){
        this();
        this.n = n;

        s = 2+n.toString().getBytes().length;
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
        byte[] c = n.toString().getBytes();
        System.arraycopy(c, 0, b, 1, c.length);

        return b;
    }

    @Override
    public void decode(byte[] buf, int off){
        if(!BencodeType.getTypeByPrefix((char) buf[off]).equals(type)){
            throw new IllegalArgumentException("Byte array is not a bencode number.");
        }

        char[] c = new char[32];
        off++;
        int s = off;

        while(buf[off] != type.getSuffix()){
            c[off-s] = (char) buf[off];
            off++;
        }

        try{
            n = NumberFormat.getInstance().parse(new String(c, 0, off-s));
        }catch(ParseException e){
            throw new IllegalArgumentException("Number is invalid.");
        }

        this.s = off-s+2;
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

    @Override
    public String toString(){
        return n.toString();
    }
}
