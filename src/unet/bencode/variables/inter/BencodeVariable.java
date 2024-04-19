package unet.bencode.variables.inter;

public abstract class BencodeVariable {

    protected BencodeType type;

    public abstract Object getObject();

    public abstract int byteSize();

    public abstract byte[] encode();

    public abstract void decode(byte[] buf, int off);
}
