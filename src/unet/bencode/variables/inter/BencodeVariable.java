package unet.bencode.variables.inter;

public abstract class BencodeVariable {

    protected BencodeType type;

    public BencodeType getType(){
        return type;
    }

    public abstract Object getObject();

    public abstract int byteSize();

    public abstract byte[] encode();

    public void decode(byte[] buf){
        decode(buf, 0);
    }

    public abstract void decode(byte[] buf, int off);
}
