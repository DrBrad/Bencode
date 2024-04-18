package unet.bencode.variables.inter;

public enum BencodeType {

    NUMBER {
        private boolean isPrefix(char c){
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
        private boolean isPrefix(char c){
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
        private boolean isPrefix(char c){
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
        private boolean isPrefix(char c){
            System.out.println("BYTE PREFIX CHECK");
            return (c >= '0' && c <= '9');
        }

        public char getDelimiter(){
            return ':';
        }
    }, INVALID;

    private boolean isPrefix(char c){
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
