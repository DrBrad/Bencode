package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeVariable;

import java.util.Arrays;

public class BencodeBytes implements BencodeVariable {

    private byte[] b;
    private int s;

    public BencodeBytes(byte[] b){
        this.b = b;
        s = (b.length+":").getBytes().length+b.length;
    }

    @Override
    public byte[] getObject(){
        return b;
    }

    @Override
    public int byteSize(){
        return s;
    }

    @Override
    public byte[] encode(){
        byte[] r = new byte[s];
        byte[] l = (b.length+":").getBytes();
        System.arraycopy(l, 0, r, 0, l.length);
        System.arraycopy(b, 0, r, l.length, b.length);
        return r;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof BencodeBytes){
            return Arrays.equals(encode(), ((BencodeBytes) o).encode());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return 0;
    }
}
