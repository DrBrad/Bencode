package unet.bencode;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BencodeArray {

    private ArrayList<Object> a;

    public BencodeArray(){
        a = new ArrayList<>();
    }

    public BencodeArray(List<?> a){
        this.a = (ArrayList<Object>) a;
    }

    public BencodeArray(byte[] buf)throws ParseException {
        this(buf, 0, buf.length);
    }

    public BencodeArray(byte[] buf, int off, int len)throws ParseException {
        a = (ArrayList<Object>) new Bencoder().decodeArray(ByteBuffer.wrap(buf, off, len));
    }

    public void put(Object o){
        a.add(o);
    }

    public Object get(int i){
        return a.get(i);
    }

    public byte[] getBytes(int i){
        return (byte[]) a.get(i);
    }

    public String getString(int i){
        return new String((byte[]) a.get(i));
    }

    public int getInteger(int i){
        return ((Number) a.get(i)).intValue();
    }

    public short getShort(int i){
        return ((Number) a.get(i)).shortValue();
    }

    public long getLong(int i){
        return ((Number) a.get(i)).longValue();
    }

    public double getDouble(int i){
        return ((Number) a.get(i)).doubleValue();
    }

    public float getFloat(int i){
        return ((Number) a.get(i)).floatValue();
    }

    public List<?> getList(int i){
        return (List<?>) a.get(i);
    }

    public Map<?, ?> getMap(int i){
        return (Map<?, ?>) a.get(i);
    }

    public int size(){
        return a.size();
    }

    public byte[] encode(){
        return new Bencoder().encode(a);
    }
}
