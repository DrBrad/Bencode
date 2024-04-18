package unet.bencode.variables.inter;

public interface BencodeVariable {

    Object getObject();

    int byteSize();

    byte[] encode();
}
