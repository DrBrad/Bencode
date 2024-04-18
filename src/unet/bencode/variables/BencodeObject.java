package unet.bencode.variables;

import unet.bencode.Bencoder;
import unet.bencode.variables.inter.BencodeObserver;
import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static unet.bencode.utils.BencodeUtils.unpackBencode;

public class BencodeObject implements BencodeVariable, BencodeObserver {

    private Map<BencodeBytes, BencodeVariable> m;
    private BencodeObserver o;
    private int s = 2;
    private BencodeType type = BencodeType.OBJECT;

    public BencodeObject(){
        m = new HashMap<>();
    }

    public BencodeObject(Map<?, ?> m){
        this.m = new HashMap<>();

        for(Object o : m.keySet()){
            BencodeBytes k;

            if(o instanceof BencodeBytes){
                k = (BencodeBytes) o;
            }else if(o instanceof String){
                k = new BencodeBytes(((String) o).getBytes());
            }else{
                throw new IllegalArgumentException("Map keys must be in byte, string, or bencodebyte form.");
            }

            if(m.get(o) instanceof BencodeVariable){
                put(k, (BencodeVariable) m.get(o));
            }else if(m.get(o) instanceof Number){
                put(k, (Number) m.get(o));
            }else if(m.get(o) instanceof String){
                put(k, (String) m.get(o));
            }else if(m.get(o) instanceof byte[]){
                put(k, (byte[]) m.get(o));
            }else if(m.get(o) instanceof List<?>){
                put(k, (List<?>) m.get(o));
            }else if(m.get(o) instanceof Map<?, ?>){
                put(k, (Map<?, ?>) m.get(o));
            }
        }
    }

    public BencodeObject(byte[] buf){
        this(new Bencoder().decodeObject(buf));
    }

    private void put(BencodeBytes k, BencodeVariable v){
        m.put(k, v);
        setByteSize(k.byteSize()+v.byteSize());
    }

    private void put(BencodeBytes k, Number n){
        put(k, new BencodeNumber(n.toString()));
    }

    private void put(BencodeBytes k, byte[] b){
        put(k, new BencodeBytes(b));
    }

    private void put(BencodeBytes k, String s){
        put(k, new BencodeBytes(s.getBytes()));
    }

    private void put(BencodeBytes k, List<?> l){
        put(k, new BencodeArray(l));
    }

    private void put(BencodeBytes k, Map<?, ?> l){
        put(k, new BencodeObject(l));
    }

    public void put(String k, Number n){
        put(new BencodeBytes(k.getBytes()), new BencodeNumber(n.toString()));
    }

    public void put(String k, byte[] b){
        put(new BencodeBytes(k.getBytes()), new BencodeBytes(b));
    }

    public void put(String k, String s){
        put(new BencodeBytes(k.getBytes()), new BencodeBytes(s.getBytes()));
    }

    public void put(String k, List<?> l){
        put(new BencodeBytes(k.getBytes()), new BencodeArray(l));
    }

    public void put(String k, Map<?, ?> l){
        put(new BencodeBytes(k.getBytes()), new BencodeObject(l));
    }

    public void put(String k, BencodeArray a){
        put(new BencodeBytes(k.getBytes()), a);
        a.setObserver(this);
    }

    public void put(String k, BencodeObject o){
        put(new BencodeBytes(k.getBytes()), o);
        o.setObserver(this);
    }

    public BencodeVariable valueOf(BencodeBytes k){
        return m.get(k);
    }

    public Object get(String k){
        return m.get(new BencodeBytes(k.getBytes())).getObject();
    }

    public Integer getInteger(String k){
        return ((Number) m.get(new BencodeBytes(k.getBytes())).getObject()).intValue();
    }

    public Long getLong(String k){
        return ((Number) m.get(new BencodeBytes(k.getBytes())).getObject()).longValue();
    }

    public Short getShort(String k){
        return ((Number) m.get(new BencodeBytes(k.getBytes())).getObject()).shortValue();
    }

    public Double getDouble(String k){
        return ((Number) m.get(new BencodeBytes(k.getBytes())).getObject()).doubleValue();
    }

    public Float getFloat(String k){
        return ((Number) m.get(new BencodeBytes(k.getBytes())).getObject()).floatValue();
    }

    public String getString(String k){
        return new String((byte[]) m.get(new BencodeBytes(k.getBytes())).getObject());
    }

    public byte[] getBytes(String k){
        return (byte[]) m.get(new BencodeBytes(k.getBytes())).getObject();
    }

    public BencodeArray getBencodeArray(String k){
        return (BencodeArray) m.get(new BencodeBytes(k.getBytes()));
    }

    public BencodeObject getBencodeObject(String k){
        return (BencodeObject) m.get(new BencodeBytes(k.getBytes()));
    }

    public boolean containsKey(String s){
        return m.containsKey(new BencodeBytes(s.getBytes()));
    }

    public boolean containsValue(Number n){
        return m.containsValue(new BencodeNumber(n.toString()));
    }

    public boolean containsValue(String s){
        return m.containsValue(new BencodeBytes(s.getBytes()));
    }

    public boolean containsValue(byte[] b){
        return m.containsValue(new BencodeBytes(b));
    }

    public boolean containsValue(List<?> l){
        return m.containsValue(new BencodeArray(l));
    }

    public boolean containsValue(Map<?, ?> m){
        return this.m.containsValue(new BencodeObject(m));
    }

    public boolean containsValue(BencodeArray a){
        return m.containsValue(a);
    }

    public boolean containsValue(BencodeObject o){
        return m.containsValue(o);
    }

    public void remove(String k){
        BencodeBytes b = new BencodeBytes(k.getBytes());
        if(m.containsKey(b)){
            setByteSize(-b.byteSize()-m.get(b).byteSize());
            m.remove(b);
        }
    }

    public Set<BencodeBytes> keySet(){
        return m.keySet();
    }

    public List<BencodeVariable> values(){
        return new ArrayList<>(m.values());
    }

    public int size(){
        return m.size();
    }

    protected void setObserver(BencodeObserver observer){
        o = observer;
    }

    private void setByteSize(int s){
        if(o != null){
            o.update(s);
        }
        this.s += s;
    }

    @Override
    public void update(int s){
        if(o != null){
            o.update(s);
        }
        this.s += s;
    }

    @Override
    public Map<String, ?> getObject(){
        Map<String, Object> h = new HashMap<>();
        for(BencodeBytes k : m.keySet()){
            h.put(new String(k.getObject()), m.get(k).getObject());
        }
        return h;
    }

    @Override
    public int byteSize(){
        return s;
    }

    @Override
    public byte[] encode(){
        byte[] buf = new byte[s];

        buf[0] = (byte) type.getPrefix();
        int pos = 1;

        for(BencodeBytes k : m.keySet()){
            byte[] key = k.encode();
            System.arraycopy(key, 0, buf, pos, key.length);
            pos += key.length;

            byte[] value = m.get(k).encode();
            System.arraycopy(value, 0, buf, pos, value.length);
            pos += value.length;
        }

        buf[pos] = (byte) type.getSuffix();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        if(BencodeType.getTypeByPrefix((char) buf[off]).equals(type)){
            throw new IllegalArgumentException("Byte array is not a bencode object.");
        }
        off++;

        while(buf[off] != type.getSuffix()){
            BencodeBytes key = new BencodeBytes();
            key.decode(buf, off);
            off += key.byteSize();

            System.out.println(new String(key.getObject()));
            //break;

            BencodeVariable value = unpackBencode(buf, off);
            off += value.byteSize();

            break;

            //m.put(key, value);
        }
        s = off+1;
    }

    @Override
    public int hashCode(){
        return 3;
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder("{\r\n");

        for(BencodeBytes o : m.keySet()){
            String k = new String(o.getObject());

            if(m.get(o) instanceof BencodeNumber){
                b.append("\t\033[0;31m"+k+"\033[0m:\033[0;33m"+((BencodeNumber) m.get(o)).getObject()+"\033[0m\r\n");

            }else if(m.get(o) instanceof BencodeBytes){
                if(Charset.forName("US-ASCII").newEncoder().canEncode(new String(((BencodeBytes) m.get(o)).getObject()))){
                    b.append("\t\033[0;31m"+k+"\033[0m:\033[0;34m"+new String(((BencodeBytes) m.get(o)).getObject(), StandardCharsets.UTF_8)+"\033[0m\r\n");

                }else{
                    b.append("\t\033[0;31m"+k+"\033[0m:\033[0;34m BASE64 { "+Base64.getEncoder().encodeToString(((BencodeBytes) m.get(o)).getObject())+" }\033[0m\r\n");
                }

            }else if(m.get(o) instanceof BencodeArray){
                b.append("\t\033[0;32m"+k+"\033[0m:"+((BencodeArray) m.get(o)).toString().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");

            }else if(m.get(o) instanceof BencodeObject){
                b.append("\t\033[0;32m"+k+"\033[0m:"+((BencodeObject) m.get(o)).toString().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");
            }
        }

        return b+"}";
    }
}
