package unet.bencode.variables;

public interface BencodeVariable {

    Object getObject();

    int byteSize();

    BencodeType getType();
}
