package unet.bencode;

import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bencoder {

    private ByteBuffer buffer;

    public byte[] encode(List<?> l){
        buffer = ByteBuffer.allocate(65507);
        put(l);
        byte[] buf = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(buf);
        return buf;
    }

    public byte[] encode(Map<?, ?> m){
        buffer = ByteBuffer.allocate(65507);
        put(m);
        byte[] buf = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(buf);
        return buf;
    }

    public List<?> decodeArray(ByteBuffer buffer)throws ParseException {
        this.buffer = buffer;
        return getList(buffer.get());
    }

    public Map<?, ?> decodeObject(ByteBuffer buffer)throws ParseException {
        this.buffer = buffer;
        return getMap(buffer.get());
    }

    private void put(Object o){
        if(o instanceof String){
            put((String) o);
        }else if(o instanceof byte[]){
            put((byte[]) o);
        }else if(o instanceof Number){
            put((Number) o);
        }else if(o instanceof List<?>){
            put((List<?>) o);
        }else if(o instanceof Map<?, ?>){
            put((Map<?, ?>) o);
        }
    }

    private void put(String s){
        put(s.getBytes());
    }

    private void put(byte[] b){
        buffer.put(Integer.toString(b.length).getBytes());
        buffer.put(":".getBytes());
        buffer.put(b);
    }

    private void put(Number n){
        buffer.put("i".getBytes());
        buffer.put(n.toString().getBytes());
        buffer.put("e".getBytes());
    }

    private void put(List<?> l){
        buffer.put("l".getBytes());
        for(Object o : l){
            put(o);
        }
        buffer.put("e".getBytes());
    }

    private void put(Map<?, ?> m){
        buffer.put("d".getBytes());
        for(Object o : m.keySet()){
            put(o);
            put(m.get(o));
        }
        buffer.put("e".getBytes());
    }

    private Object get(byte l)throws ParseException {
        switch(l){
            case 'i':
                return getNumber(l);
            case 'l':
                return getList(l);
            case 'd':
                return getMap(l);
            default:
                if(l >= '0' && l <= '9'){
                    return getBytes(l);
                }
        }
        throw new ParseException("Failed to determine type", 0);
    }

    private Number getNumber(byte l)throws ParseException {
        if(l == 'i'){
            StringBuilder n = new StringBuilder();

            byte b;
            while((b = buffer.get()) != 'e'){
                n.append((char) b);
            }

            return NumberFormat.getInstance().parse(n.toString());
        }
        throw new ParseException("Failed to parse number", 0);
    }

    private List<?> getList(byte l)throws ParseException {
        if(l == 'l'){
            ArrayList<Object> a = new ArrayList<>();

            byte b;
            while((b = buffer.get()) != 'e'){
                a.add(get(b));
            }

            return a;
        }
        throw new ParseException("Failed to parse list", 0);
    }

    private Map<String, ?> getMap(byte l)throws ParseException {
        if(l == 'd'){
            HashMap<String, Object> m = new HashMap<>();

            byte b;
            while((b = buffer.get()) != 'e'){
                m.put(new String(getBytes(b)), get(buffer.get()));
            }

            return m;
        }
        buffer.position(buffer.position()-1);
        throw new ParseException("Failed to parse map", 0);
    }

    private byte[] getBytes(byte l)throws ParseException {
        if(l >= '0' && l <= '9'){
            StringBuilder n = new StringBuilder();
            n.append(l);

            byte b;
            while((b = buffer.get()) != ':'){
                n.append((char) b);
            }

            byte[] buf = new byte[Integer.parseInt(n.toString())];
            buffer.get(buf);

            return buf;
        }
        buffer.position(buffer.position()-1);
        throw new ParseException("Failed to parse bytes", 0);
    }
}
