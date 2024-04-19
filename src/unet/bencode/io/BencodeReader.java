package unet.bencode.io;

import unet.bencode.variables.*;
import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BencodeReader {

    private InputStream in;

    public BencodeReader(InputStream in){
        this.in = in;
    }

    public BencodeVariable read()throws IOException {
        return get((byte) in.read());
    }

    public void close()throws IOException {
        in.close();
    }

    private BencodeVariable get(byte b)throws IOException {
        switch(BencodeType.getTypeByPrefix((char) b)){
            case NUMBER:
                return getNumber();

            case ARRAY:
                return getList();

            case OBJECT:
                return getMap();

            case BYTES:
                return getBytes(b);
        }

        throw new IOException("Prefix is not of bencode type.");
    }

    private BencodeNumber getNumber()throws IOException {
        char[] c = new char[32];
        int i = 0;

        byte b;
        while((b = (byte) in.read()) != BencodeType.NUMBER.getSuffix()){
            c[i] = (char) b;
            i++;
        }

        try{
            return new BencodeNumber(NumberFormat.getInstance().parse(new String(c, 0, i)));
        }catch(ParseException e){
            throw new IOException("Failed to parse bencode number");
        }
    }

    private BencodeBytes getBytes(byte b)throws IOException {
        byte[] c = new byte[8];
        c[0] = b;
        int i = 1;

        while((b = (byte) in.read()) != BencodeType.BYTES.getDelimiter()){
            c[i] = b;
            i++;
        }

        int length = 0;
        for(int j = 0; j < i; j++){
            length = length*10+(c[j]-'0');
        }

        byte[] buf = new byte[length];
        in.read(buf, 0, buf.length);

        return new BencodeBytes(buf);
    }

    private BencodeArray getList()throws IOException {
        List<BencodeVariable> a = new ArrayList<>();

        byte b;
        while((b = (byte) in.read()) != BencodeType.ARRAY.getSuffix()){
            a.add(get(b));
        }

        return new BencodeArray(a);
    }

    private BencodeObject getMap()throws IOException {
        Map<BencodeBytes, BencodeVariable> m = new HashMap<>();

        byte b;
        while((b = (byte) in.read()) != BencodeType.OBJECT.getSuffix()){
            BencodeBytes key = getBytes(b);
            m.put(key, get((byte) in.read()));
        }

        return new BencodeObject(m);
    }
}
