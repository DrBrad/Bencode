package unet.bencode.variables;

import unet.bencode.variables.inter.BencodeObserver;
import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static unet.bencode.utils.BencodeUtils.unpackBencode;

public class BencodeArray extends BencodeVariable implements BencodeObserver {

    private List<BencodeVariable> l;
    private BencodeObserver o;
    private int s = 2;

    public BencodeArray(){
        type = BencodeType.ARRAY;
        l = new ArrayList<>();
    }

    public BencodeArray(List<?> l){
        this();

        for(Object v : l){
            if(v instanceof BencodeVariable){
                add((BencodeVariable) v);
            }else if(v instanceof Number){
                add((Number) v);
            }else if(v instanceof String){
                add((String) v);
            }else if(v instanceof byte[]){
                add((byte[]) v);
            }else if(v instanceof List<?>){
                add((List<?>) v);
            }else if(v instanceof Map<?, ?>){
                add((Map<?, ?>) v);
            }
        }
    }

    public BencodeArray(byte[] buf){
        this();
        decode(buf);
    }

    public BencodeArray(byte[] buf, int off){
        this();
        decode(buf, off);
    }

    private void add(BencodeVariable v){
        l.add(v);
        setByteSize(v.byteSize());
    }

    public void add(Number n){
        add(new BencodeNumber(n));
    }

    public void add(byte[] b){
        add(new BencodeBytes(b));
    }

    public void add(String s){
        add(new BencodeBytes(s.getBytes()));
    }

    public void add(List<?> l){
        add(new BencodeArray(l));
    }

    public void add(Map<?, ?> l){
        add(new BencodeObject(l));
    }

    public void add(BencodeArray a){
        l.add(a);
        setByteSize(a.byteSize());
        a.setObserver(this);
    }

    public void add(BencodeObject o){
        l.add(o);
        setByteSize(o.byteSize());
        o.setObserver(this);
    }

    private void set(int i, BencodeVariable v){
        l.set(i, v);
        setByteSize(-l.get(i).byteSize()+v.byteSize());
    }

    public void set(int i, Number n){
        set(i, new BencodeNumber(n));
    }

    public void set(int i, byte[] b){
        set(i, new BencodeBytes(b));
    }

    public void set(int i, String s){
        set(i, new BencodeBytes(s.getBytes()));
    }

    public void set(int i, List<?> l){
        set(i, new BencodeArray(l));
    }

    public void set(int i, Map<?, ?> m){
        set(i, new BencodeObject(m));
    }

    public void set(int i, BencodeArray a){
        l.set(i, a);
        setByteSize(-l.get(i).byteSize()+a.byteSize());
        a.setObserver(this);
    }

    public void set(int i, BencodeObject o){
        l.set(i, o);
        setByteSize(-l.get(i).byteSize()+o.byteSize());
        o.setObserver(this);
    }

    public BencodeVariable valueOf(int i){
        return l.get(i);
    }

    public Object get(int i){
        return l.get(i).getObject();
    }

    public Integer getInteger(int i){
        return ((Number) l.get(i).getObject()).intValue();
    }

    public Long getLong(int i){
        return ((Number) l.get(i).getObject()).longValue();
    }

    public Short getShort(int i){
        return ((Number) l.get(i).getObject()).shortValue();
    }

    public Double getDouble(int i){
        return ((Number) l.get(i).getObject()).doubleValue();
    }

    public Float getFloat(int i){
        return ((Number) l.get(i).getObject()).floatValue();
    }

    public String getString(int i){
        return new String((byte[]) l.get(i).getObject());
    }

    public byte[] getBytes(int i){
        return (byte[]) l.get(i).getObject();
    }

    public BencodeArray getBencodeArray(int i){
        return (BencodeArray) l.get(i);
    }

    public BencodeObject getBencodeObject(int i){
        return (BencodeObject) l.get(i);
    }

    public boolean contains(Number n){
        return l.contains(new BencodeNumber(n));
    }

    public boolean contains(String s){
        return l.contains(new BencodeBytes(s.getBytes()));
    }

    public boolean contains(byte[] b){
        return l.contains(new BencodeBytes(b));
    }

    public boolean contains(List<?> l){
        return this.l.contains(new BencodeArray(l));
    }

    public boolean contains(Map<?, ?> m){
        return l.contains(new BencodeObject(m));
    }

    public boolean contains(BencodeArray a){
        return l.contains(a);
    }

    public boolean contains(BencodeObject o){
        return l.contains(o);
    }

    private void remove(BencodeVariable v){
        if(l.contains(v)){
            l.remove(v);
            setByteSize(-v.byteSize());
        }
    }

    public void remove(Number n){
        remove(new BencodeNumber(n));
    }

    public void remove(byte[] b){
        remove(new BencodeBytes(b));
    }

    public void remove(String s){
        remove(new BencodeBytes(s.getBytes()));
    }

    private int indexOf(BencodeVariable v){
        if(l.contains(v)){
            return l.indexOf(v);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int indexOf(Number n){
        return indexOf(new BencodeNumber(n));
    }

    public int indexOf(byte[] b){
        return indexOf(new BencodeBytes(b));
    }

    public int indexOf(String s){
        return indexOf(new BencodeBytes(s.getBytes()));
    }

    public int size(){
        return l.size();
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
    public Object getObject(){
        List<Object> a = new ArrayList<>();
        for(BencodeVariable v : l){
            a.add(v.getObject());
        }
        return a;
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

        for(BencodeVariable v : l){
            byte[] key = v.encode();
            System.arraycopy(key, 0, buf, pos, key.length);
            pos += key.length;
        }

        buf[pos] = (byte) type.getSuffix();

        return buf;
    }

    @Override
    public void decode(byte[] buf, int off){
        if(!BencodeType.getTypeByPrefix((char) buf[off]).equals(type)){
            throw new IllegalArgumentException("Byte array is not a bencode array.");
        }

        int s = off;
        off++;

        while(buf[off] != type.getSuffix()){
            BencodeVariable var = unpackBencode(buf, off);
            off += var.byteSize();
            l.add(var);
        }
        this.s = (off-s)+1;
    }

    @Override
    public int hashCode(){
        return 2;
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder("[\r\n");

        for(BencodeVariable v : l){
            if(v instanceof BencodeNumber){
                b.append("\t\033[0;33m"+v+"\033[0m\r\n");

            }else if(v instanceof BencodeBytes){
                b.append("\t\033[0;34m"+v+"\033[0m\r\n");

            }else if(v instanceof BencodeArray){
                b.append("\t\033[0m"+v.toString().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");

            }else if(v instanceof BencodeObject){
                b.append("\t\033[0m"+v.toString().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");
            }
        }

        return b+"]";
    }
}
