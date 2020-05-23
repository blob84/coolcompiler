public class Tag {
    //token
    public static final int EOF_TYPE = 0;    // represent EOF token type
    public static final int TYPE        = 1;
    public static final int ID          = 2;      
    public static final int INTEGER     = 3;
    public static final int STRING      = 4;
    public static final int CLASS       = 5;
    public static final int INHERITS    = 6;
    public static final int NOT         = 7;
    public static final int ISVOID      = 8;
    public static final int LET         = 9;
    public static final int IN          = 10;
    public static final int IF          = 11;
    public static final int THEN        = 12;
    public static final int ELSE        = 13;
    public static final int FI          = 14;
    public static final int WHILE       = 15;
    public static final int LOOP        = 16;
    public static final int POOL        = 17;
    public static final int CASE        = 18;
    public static final int OF          = 19;
    public static final int ESAC        = 20;
    public static final int NEW         = 21;
    public static final int TRUE        = 22;
    public static final int FALSE       = 23;
    public static final int SEMI        = 24;
    public static final int COLON       = 25;
    public static final int EQUAL       = 26;
    public static final int ASSIGN      = 27;
    public static final int LPAR        = 28;
    public static final int RPAR        = 29;
    public static final int LBRACE      = 30;
    public static final int RBRACE      = 31;
    public static final int COMMA       = 32;
    public static final int PLUS        = 33;
    public static final int MINUS       = 34;
    public static final int MULT        = 35;
    public static final int DIV         = 36;
    public static final int LESS        = 37;
    public static final int LESSEQ      = 38;
    public static final int ARROW       = 39;
    public static final int NEG         = 40;
    public static final int AT          = 41;
    public static final int DOT         = 42;
    public static final int COMMENT     = 43;
    public static final int WHITESPACE  = 44;
  

   public static String[] tokenNames =
        {   "EOF", "TYPE", "ID", "INTEGER", "STRING", "CLASS", "INHERITS", "NOT", "ISVOID",
            "LET", "IN", "IF",
            "THEN", "ELSE", "FI", "WHILE", "LOOP", "POOL", "CASE", "OF", "ESAC", "NEW", 
            "TRUE", "FALSE", "SEMI", "COLON", "EQUAL", "ASSIGN", "LPAR", "RPAR", "LBRACE", 
            "RBRACE", "COMMA", "PLUS", "MINUS", "MULT", "DIV", "LESS", "LESSEQ", "ARROW", 
            "NEG", "AT", "DOT", "COMMENT", "WHITESPACE" 
        };

    public static String getTokenName(int x) { return tokenNames[x]; }


}
