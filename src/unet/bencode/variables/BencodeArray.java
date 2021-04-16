package unet.bencode.variables;

import unet.bencode.Bencoder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BencodeArray implements BencodeVariable {

    private ArrayList<BencodeVariable> l = new ArrayList<>();
    private int s = 2;

    public BencodeArray(){
    }

    public BencodeArray(List<?> l){
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
        this(new Bencoder().decodeArray(buf));
    }

    private void add(BencodeVariable v){
        l.add(v);
        s += v.byteSize();
    }

    public void add(Number n){
        add(new BencodeNumber(n.toString()));
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
        s += a.byteSize();
    }

    public void add(BencodeObject o){
        l.add(o);
        s += o.byteSize();
    }

    public BencodeVariable valueOf(int i){
        return l.get(i);
    }

    public Object get(int i)throws ParseException {
        return l.get(i).getObject();
    }

    public Integer getInteger(int i)throws ParseException {
        return ((Number) l.get(i).getObject()).intValue();
    }

    public Long getLong(int i)throws ParseException {
        return ((Number) l.get(i).getObject()).longValue();
    }

    public Short getShort(int i)throws ParseException {
        return ((Number) l.get(i).getObject()).shortValue();
    }

    public Double getDouble(int i)throws ParseException {
        return ((Number) l.get(i).getObject()).doubleValue();
    }

    public Float getFloat(int i)throws ParseException {
        return ((Number) l.get(i).getObject()).floatValue();
    }

    public String getString(int i)throws ParseException {
        return new String((byte[]) l.get(i).getObject());
    }

    public byte[] getBytes(int i)throws ParseException {
        return (byte[]) l.get(i).getObject();
    }

    public BencodeArray getBencodeList(int i){
        return (BencodeArray) l.get(i);
    }

    public BencodeObject getBencodeMap(int i){
        return (BencodeObject) l.get(i);
    }

    public boolean contains(Number n){
        return l.contains(new BencodeNumber(n.toString()));
    }

    public boolean contains(String s){
        return l.contains(new BencodeBytes(s.getBytes()));
    }

    public boolean contains(byte[] b){
        return l.contains(new BencodeBytes(b));
    }

    public boolean contains(List<?> l){
        return l.contains(new BencodeArray(l));
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

    public int size(){
        return l.size();
    }

    @Override
    public Object getObject()throws ParseException {
        ArrayList<Object> a = new ArrayList<>();
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
    public int hashCode(){
        return 2;
    }

    public byte[] encode(){
        return new Bencoder().encode(this);
    }
}
