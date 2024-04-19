package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.util.Arrays;

public class BencodeBytes extends BencodeVariable {

    private byte[] b;
    private int s;

    public BencodeBytes(){
        type = BencodeType.BYTES;
    }

    public BencodeBytes(byte[] b){
        this();
        this.b = b;
        s = (b.length+type.getDelimiter()+"").getBytes().length+b.length;
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
    public void decode(byte[] buf, int off){
        if(!BencodeType.getTypeByPrefix((char) buf[off]).equals(type)){
            throw new IllegalArgumentException("Byte array is not a bencode bytes / string.");
        }

        byte[] c = new byte[8];
        int s = off;

        while(buf[off] != type.getDelimiter()){
            c[off-s] = buf[off];
            off++;
        }

        int length = 0;
        for(int i = 0; i < off-s; i++){
            length = length*10+(c[i]-'0');
        }

        b = new byte[length];
        System.arraycopy(buf, off+1, b, 0, b.length);
        this.s = (off-s)+b.length+1;
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
