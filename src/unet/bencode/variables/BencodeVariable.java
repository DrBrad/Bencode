package unet.bencode.variables;

import java.text.ParseException;

public interface BencodeVariable {

    Object getObject()throws ParseException;

    int byteSize();
}
