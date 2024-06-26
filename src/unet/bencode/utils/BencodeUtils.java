package unet.bencode.utils;

import unet.bencode.variables.BencodeArray;
import unet.bencode.variables.BencodeBytes;
import unet.bencode.variables.BencodeNumber;
import unet.bencode.variables.BencodeObject;
import unet.bencode.variables.inter.BencodeType;
import unet.bencode.variables.inter.BencodeVariable;

public class BencodeUtils {

    public static BencodeVariable unpackBencode(byte[] buf, int off){
        BencodeType type = BencodeType.getTypeByPrefix((char) buf[off]);

        BencodeVariable variable;
        switch(type){
            case NUMBER:
                variable = new BencodeNumber();
                break;

            case ARRAY:
                variable = new BencodeArray();
                break;

            case OBJECT:
                variable = new BencodeObject();
                break;

            case BYTES:
                variable = new BencodeBytes();
                break;

            default:
                throw new IllegalArgumentException("Invalid key type.");
        }

        variable.decode(buf, off);
        return variable;
    }
}
