package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeVariable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

public class BencodeNumber implements BencodeVariable {

    private String n;
    private int s;

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
