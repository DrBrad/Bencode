package unet.bencode.io;

import unet.bencode.variables.*;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeWriter {

    private OutputStream out;

    public BencodeWriter(OutputStream out){
        this.out = out;
    }

    public void write(BencodeVariable ben)throws IOException {
        put(ben);
    }

    public void flush()throws IOException {
        out.flush();
    }

    public void close()throws IOException {
        out.close();
    }

    private void put(BencodeVariable v)throws IOException {
        switch(v.getType()){
            case BYTES:
                put((BencodeBytes) v);
                break;

            case NUMBER:
                put((BencodeNumber) v);
                break;

            case ARRAY:
                put((BencodeArray) v);
                break;

            case OBJECT:
                put((BencodeObject) v);
                break;
        }
    }

    private void put(BencodeBytes v)throws IOException {
        out.write(v.getBytes());
    }

    private void put(BencodeNumber n)throws IOException {
        out.write(n.getBytes());
    }

    private void put(BencodeArray l)throws IOException {
        out.write('l');

        for(int i = 0; i < l.size(); i++){
            put(l.valueOf(i));
        }
        out.write('e');
    }

    private void put(BencodeObject m)throws IOException {
        out.write('d');

        for(BencodeBytes k : m.keySet()){
            put(k);
            put(m.valueOf(k));
        }
        out.write('e');
    }
}
