package unet.bencode;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BencodeObject {

    private HashMap<String, Object> m;

    public BencodeObject(){
        m = new HashMap<>();
    }

    public BencodeObject(Map<String, ?> m){
        this.m = (HashMap<String, Object>) m;
    }

    public BencodeObject(byte[] buf)throws ParseException {
        this(buf, 0, buf.length);
    }

    public BencodeObject(byte[] buf, int off, int len)throws ParseException {
        m = (HashMap<String, Object>) new Bencoder().decodeObject(ByteBuffer.wrap(buf, off, len));
    }

    public void put(String key, Object val){
        m.put(key, val);
    }

    public Object get(String key){
        return m.get(key);
    }

    public byte[] getBytes(String key){
        return (byte[]) m.get(key);
    }

    public String getString(String key){
        return new String((byte[]) m.get(key));
    }

    public int getInteger(String key){
        return ((Number) m.get(key)).intValue();
    }

    public short getShort(String key){
        return ((Number) m.get(key)).shortValue();
    }

    public long getLong(String key){
        return ((Number) m.get(key)).longValue();
    }

    public double getDouble(String key){
        return ((Number) m.get(key)).doubleValue();
    }

    public float getFloat(String key){
        return ((Number) m.get(key)).floatValue();
    }

    public List<?> getList(String key){
        return (List<?>) m.get(key);
    }

    public Map<?, ?> getMap(String key){
        return (Map<?, ?>) m.get(key);
    }

    public int size(){
        return m.size();
    }

    public byte[] encode(){
        return new Bencoder().encode(m);
    }
}
