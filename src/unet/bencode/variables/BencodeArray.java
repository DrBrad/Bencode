package unet.bencode.variables;

import unet.bencode.Bencoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
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
        this(new Bencoder().decodeArray(buf, 0));
    }

    public BencodeArray(byte[] buf, int off){
        this(new Bencoder().decodeArray(buf, off));
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

    private void set(int i, BencodeVariable v){
        s = (s-l.get(i).byteSize())+v.byteSize();
        l.set(i, v);
    }

    public void set(int i, Number n){
        set(i, new BencodeNumber(n.toString()));
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
        s = (s-l.get(i).byteSize())+a.byteSize();
        l.set(i, a);
    }

    public void set(int i, BencodeObject o){
        s = (s-l.get(i).byteSize())+o.byteSize();
        l.set(i, o);
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

    public BencodeArray getBencodeArray(int i){
        return (BencodeArray) l.get(i);
    }

    public BencodeObject getBencodeObject(int i){
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
            s -= v.byteSize();
            l.remove(v);
        }
    }

    public void remove(Number n){
        remove(new BencodeNumber(n.toString()));
    }

    public void remove(byte[] b){
        remove(new BencodeBytes(b));
    }

    public void remove(String s){
        remove(new BencodeBytes(s.getBytes()));
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

    public String prettify(){
        StringBuilder b = new StringBuilder("[\r\n");

        for(BencodeVariable v : l){
            try{
                if(v instanceof BencodeNumber){
                    b.append("\t\033[0;31m"+v.getObject()+"\033[0m\r\n");

                }else if(v instanceof BencodeBytes){
                    if(Charset.forName("US-ASCII").newEncoder().canEncode(new String((byte[]) v.getObject()))){
                        b.append("\t\033[0;34m"+new String((byte[]) v.getObject(), StandardCharsets.UTF_8)+"\033[0m\r\n");

                    }else{
                        b.append("\t\033[0;34mBASE64 { "+ Base64.getEncoder().encodeToString((byte[]) v.getObject())+" }\033[0m\r\n");
                    }

                }else if(v instanceof unet.bencode.variables.BencodeArray){
                    b.append("\t\033[0m"+((unet.bencode.variables.BencodeArray) v).prettify().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");

                }else if(v instanceof BencodeObject){
                    b.append("\t\033[0m"+((BencodeObject) v).prettify().replaceAll("\\r?\\n", "\r\n\t")+"\r\n");
                }
            }catch(ParseException e){
            }
        }

        return b+"]";
    }

    public byte[] encode(){
        return new Bencoder().encode(this);
    }
}
