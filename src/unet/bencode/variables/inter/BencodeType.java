package unet.bencode.variables.inter;

public enum BencodeType {

    NUMBER {
        public boolean isPrefix(char c){
            return c == getPrefix();
        }

        public char getPrefix(){
            return 'i';
        }

        public char getSuffix(){
            return 'e';
        }
    },
    OBJECT {
        public boolean isPrefix(char c){
            return c == getPrefix();
        }

        public char getPrefix(){
            return 'd';
        }

        public char getSuffix(){
            return 'e';
        }
    },
    ARRAY {
        public boolean isPrefix(char c){
            return c == getPrefix();
        }

        public char getPrefix(){
            return 'l';
        }

        public char getSuffix(){
            return 'e';
        }
    },
    BYTES {
        public boolean isPrefix(char c){
            return (c >= '0' && c <= '9');
        }

        public char getDelimiter(){
            return ':';
        }
    }, INVALID;

    public boolean isPrefix(char c){
        return false;
    }

    public char getPrefix(){
        return 0x00;
    }

    public char getSuffix(){
        return 0x00;
    }

    public char getDelimiter(){
        return 0x00;
    }

    public static BencodeType getTypeByPrefix(char c){
        for(BencodeType type : values()){
            if(type.isPrefix(c)){
                return type;
            }
        }

        return INVALID;
    }
}
