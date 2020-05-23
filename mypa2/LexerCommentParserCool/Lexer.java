import java.io.*;
import java.util.*;

public class Lexer {
    private static final int N = 4096;
    char[] buffer = new char[N*2]; // empty buffer 
    int lexemeBegin = 0;    // start of lexeme    
    int forward = -1;   // pattern matching
    public static int curr_lineno = 1;    //line number
    static InputStream input;
    private StringBuilder lexeme = new StringBuilder();
    private Hashtable keywords = new Hashtable(); // reserved word table   
    private Hashtable words = new Hashtable(); // ID, TYPE table   
    public static final char EOF = (char)-1; // represent end of file char

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    public static String error_msg = "";
    public static final int MAX_LEN_STRING = 1024;
    
    void reserve(String lex) { AbstractTable.keywordtable.addString(lex); }

    public Lexer(InputStream input) {
        this.input = input;
        loadBuffer(0, N);
        // reserve keywords
        reserve("if"));
        reserve("class");
        reserve("inherits"); 
        reserve("not");
        reserve("isvoid");
        reserve("let");
        reserve("in");
        reserve("if");
        reserve("then");
        reserve("else");
        reserve("fi");
        reserve("while");
        reserve("loop");
        reserve("pool");
        reserve("case");
        reserve("of");
        reserve("esac");
        reserve("new");
        reserve("true");
        reserve("false");
    }

    void loadBuffer(int start, int end) {   //carica il buffer
        for (int i = start; i < end-1; i++) {// load chars from input
            int c = 0;
            try {
                c = input.read();
            }
            catch (IOException e) { e.printStackTrace(); }
            if (c == -1) { buffer[i] = EOF; }
            else buffer[i] = (char) c; 
        }
        buffer[end-1] = EOF;    // sentinel EOF 
    }

    void retract() {    // point to the previous char
        forward--;    
    }

    /*void printbuf() {
        System.out.print("currlex: "); int i; int j =0;
        for (i = lexemeBegin; i < forward+1; i++, j++)
            System.out.print(buffer[i]);
                    System.out.println(" --len="+j);
    }*/

    void skip() {   // skip lexeme
        lexemeBegin = forward+1;
        lexeme.delete(0, lexeme.length()); // svuota il buffer del lessema
    }

    void flushBuffer() {
        forward = -1;
        lexemeBegin = 0;
    }

    /*char[] getLexeme() {    // get the lexeme found
        int start = lexemeBegin;    // lexemeBegin può essere diverso da 0
        int end = forward;
        int length = 0; // lunghezza del lexema
        
        char[] tmplexeme = new char[N];
        if (start > end) {      // il lessema si trova tra le seconda e la prima metà del buffer
            while (buffer[start] != EOF)    // copia dalla seconda metà dal buffer
                tmplexeme[length++] = buffer[start++];
            start = 0;
        }
        while (start < end+1)   
            if (buffer[start] != EOF)  
                tmplexeme[length++] = buffer[start++];

        //printbuf(); 
        //System.out.println("lexemB: "+ lexemeBegin);
        //System.out.println("forward: "+ forward);

        lexemeBegin = forward+1;    // facciamo puntare al successivo lessema

        char[] lexeme = new char[length];
        for (int i = 0; i < length; i++)
            lexeme[i] = tmplexeme[i];

       System.out.print("currlex: "); 
        for (int i = 0; i < length; i++)
            System.out.print(lexeme[i]);
        System.out.println("");
        //System.out.println(lexeme); 
        return lexeme;
    }*/

    String getLexeme() {
        lexemeBegin = forward+1;    // facciamo puntare al successivo lessema
        String s = lexeme.toString();
        lexeme.delete(0, lexeme.length());
        return s;
    }

    void buildLexeme(char c) {  // salviamo i caratteri che formano il lessema corrente
        lexeme.append(c);        
    }

    char nextChar() {   // get the next char
        switch (buffer[++forward]) {    
            case EOF:
                if (forward == N-1) { // end of first buffer
                    loadBuffer(N, N*2);
                    forward = N;    // inizio del secondo buffer
                }
                else if (forward == (N*2)-1) { // end of second buffer
                    loadBuffer(0, N);
                    forward = 0;    // inizio del primo buffer
                }
                else
                    return EOF;
            default:
                return buffer[forward];
        }
    }

    public Token nextToken() throws SyntaxError, Exception {
        char c = 0;
        int state = 1;
        int valueNum;
        int strlen = 0; //string constant length
        boolean escape = false; // is escape found in string literal?
        boolean isNullchar = false; // is null char?
        boolean opened = false;
        boolean closed = false;
        int nested = 0; //StringBuilder comm = new StringBuilder();
        while ( true ) { 
            switch(state) {
                case 1: 
                    c = nextChar();
                    if (Character.isLetter(c)) state = 2;
                    else if (Character.isDigit(c)) state = 4;
                    else if (c == ';') state = 17;
                    else if (c == ':') state = 18;
                    else if (c == ',') state = 19;
                    else if (c == '<') state = 6;
                    else if (c == '=') state = 10;
                    else if (c == '(') state = 13;
                    else if (c == ')') state = 14;
                    else if (c == '{') state = 15;
                    else if (c == '}') state = 16;
                    else if (c == '+') state = 20;
                    else if (c == '-') state = 21;
                    else if (c == '*') state = 22;
                    else if (c == '/') state = 23;
                    else if (c == '~') state = 24;
                    else if (c == '@') state = 25;
                    else if (c == '.') state = 26;
                    //else if (c == '!') state = 27;
                    else if (c == '"') state = 28;
                    else if (c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == 11 || c == ' ') { if (c == '\n') curr_lineno++; state = 30; skip(); }
                    else if (c == EOF)	return new Symbol(TokenConstants.EOF);
                    else { 
						error_msg = "'"+c+"'"+ " it is not a valid start of lexeme ";						
						Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                    if (c != '\t' && c != '\n' && c != '\r' && c != '\f' && c != 11 && c != ' ')
                        buildLexeme(c); // memorizza il primo carattere del lessema
                    break;
                case 2: // TYPE or ID   
                    /* [A-Za-z_] | ([A-Za-z_] | [0-9])* */
                    c = nextChar(); 
                    if ( !Character.isLetterOrDigit(c) && c != '_') state = 3; // end of name of TYPE or ID
                    else { 
                        buildLexeme(c); // save name of type or id 
                        state = 2;
                    }
                    break;
                case 3: // return TYPE or ID or keyword
                    retract();
                    String lexeme = getLexeme(); 
                    Symbol w = (KeywordSymbol) AbstractTable.keywordtable.lookup(lexeme.toLowerCase()); // is it a keyword? Ignoring case
                    if (w != null) { //boolean constant first character could not be uppercase 
						if ((lexeme.toLowerCase.equals(TokenConstants.TRUE) || lexeme.toLowerCase.equals(TokenConstants.FALSE)) && Character.isUpperCase(lexeme.charAt(0))) { 
							error_msg = curr_lineno + " syntax error at or near TYPEID = " + w; 
							w =  new Symbol(TokenConstants.ERROR); w.value = error_msg; return w; }
						}
						return w; 
					}
                    // it is not a keyword    
                    //w = (IdSymbol) AbstractTable.idtable.lookup(lexeme); //is it a type or id ?
                    //if (w != null) { return w; }    
                    if (Character.isLowerCase(lexeme.charAt(0)))   // is ID
                        w = new Symbol(TokenConstants.OBJECTID);
                    else
						w = new Symbol(TokenConstants.TYPEID);
                    //else w = new Token(Tag.TYPE, s);   // is Type
					w.value = AbstractTable.idtable.addString(lexeme); 
					return w;
                case 4: // digit
                    /* [0-9]+ */
                    c = nextChar();
                    if (!Character.isDigit(c)) state = 5;
                    else {      // copy number value
                        //valueNum = 0;
                        //v = 10 *valueNum + Character.digit(c, 10);
                        //c = nextChar();
                        buildLexeme(c);                
                        state = 4;        
                    }
                    break;
                case 5: // return digit     
                    retract();
					Symbol s = new Symbol(TokenConstants.INT_CONST); s.value = AbstractTable.inttable.addString(getLexeme()); return s; 
                    //return new Symbol(TokenConstants.INT_CONST, getLexeme());
                case 6: // <, <-, <=
                    c = nextChar(); 
                    if (c == '-') { state = 7; buildLexeme(c); }
                    else if (c == '=') { state = 8; buildLexeme(c); }
                    else state = 9;
                    break;
                case 7:
                    return new Symbol(TokenConstants.ASSIGN);
                case 8:
                    return new Symbol(TokenConstants.LE);
                case 9: 
                    retract();
                    return new Symbol(TokenConstants.LT);
                case 10: // =, =>
                    c = nextChar();
                    if (c == '>') { state = 11; buildLexeme(c); }
                    else state = 12;
                    break; 
                case 11:
                    return new Symbol(TokenConstants.DARROW);
                case 12: // =  
                    retract(); 
                    return new Symbol(TokenConstants.EQ);
                    // (, ), {, }, ;, :, ',', +
                case 13: 
                    c = nextChar();
                    if (c == '*') { nested++; state = 34; }
                    else { retract(); return new Symbol(TokenConstants.LPAREN); }
                    break;
                case 14: return new Symbol(TokenConstants.RPAREN);
                case 15: return new Symbol(TokenConstants.LBRACE);
                case 16: return new Symbol(TokenConstants.RBRACE);
                case 17: return new Symbol(TokenConstants.SEMI);
                case 18: return new Symbol(TokenConstants.COLON);
                case 19: return new Symbol(TokenConstants.COMMA);
                case 20: return new Symbol(TokenConstants.PLUS);
                case 21: // -, --
                    c = nextChar();
                    if (c == '-') state = 31; // curr_lineno comment                 
                    else { state = 33; }//retract(); return new Token(Tag.MINUS, getLexeme()); }
                    break;
                case 22: return new Symbol(TokenConstants.MULT); 
                case 23: return new Symbol(TokenConstants.DIV);
                case 24: return new Symbol(TokenConstants.NEG);
                case 25: return new Symbol(TokenConstants.AT);
                case 26: return new Symbol(TokenConstants.DOT);
                //case 27: return new Token(Tag.NOT, getLexeme());
                case 28:    // string literal
                    /* '"' (ANY but '"', '^@', '\n', EOF) '"' */
                    c = nextChar(); 
                    if (c == '^') { isNullchar = true; state = 28; }
                    else if (c == '@') { 
                        if (isNullchar) { 
                            state = 32;  
                            isNullchar = false; 
                         }
                        else state = 28; }//throw new SyntaxError("String contains null character", curr_lineno); } }
                    else if (c == '\n') {
                        if (isNullchar) isNullchar = false; 
                        if (!escape) {
                            state = 29; 
                            curr_lineno++;
							error_msg = curr_lineno + "Unterminated string constant"; 
							Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                        else curr_lineno++; state = 28; }
                    else if (c == EOF) { 
						state = 29; isNullchar = false;  error_msg = curr_lineno + " EOF in string constant"); 
						Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
					}
                    else if (c == '\\') {   // is newline or double quote escaped?
                        if (isNullchar) isNullchar = false; 
                        escape = true; state = 28; } 
                    else if (c == '"') {
                        if (isNullchar) isNullchar = false; 
                        if (!escape) state = 29;  // end string
                        else state = 28; } //escaped double quote
                    else { escape = false; isNullchar = false; state = 28; }
                    if (strlen++ >= 2*(N-1)) { state = 28; 
							error_msg = curr_lineno + " String constant too long"; 
							Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
					}
                    else buildLexeme(c);
                    break;
                case 29:    // closing string literal
                    strlen = 0;
					Symbol sym = new Symbol(TokenConstants.STR_CONST); 
			       	sym.value = AbstractTable.stringtable.addString(Token.unescape(getLexeme())); return sym;
                    //return new Token(Tag.STRING, Token.unescape(getLexeme())); 
                case 30: // white space
                    /* WS+ */
                    c = nextChar();
                    if (c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == 11 || c == ' ') {  // ascii 11 is '\v'
                        if (c == '\n') curr_lineno++; state = 30; skip(); } //skip white space 
                    else { retract(); state = 1; }  // end white space
                    break;
                case 31:    // curr_lineno comment
                    c = nextChar(); 
                    if (c != '\n' && c != EOF) { state = 31; }   
                    else { if (c == '\n') curr_lineno++; 
                         skip(); state = 1; } // skip comment, end comment
                    break;
                case 32: //skip string constant content after null char found
                    c = nextChar();
                    if (strlen++ > MAX_LEN_STRING) { state = 1; error_msg = "String constant too long"; 
						Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                    if (c != '\n' && c != EOF && c != '"') { state = 32; }   
                    else { if (c == '\n') curr_lineno++; 
                         skip(); strlen = 0; 
							error_msg = curr_lineno + " String contains null character"; 
							Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                         //throw new SyntaxError("String contains null character", curr_lineno); }  // skip string constant, end string constant
                    break;
                case 33:
                    retract(); return new Symbol(TokenConstants.MINUS);
                case 34: //nested multiline comment
                    c = nextChar(); //comm.append(c);
                    if (c == ')') { 
                        if (closed) { 
                            if (--nested == 0) { skip(); state = 1; opened = closed = false; }} //System.out.println(comm); } }
                        else closed = false;
                        if (opened) opened = false;
                    }
                    else if (c == '(') {
                        if (!opened) opened = true;
                    } 
                    else if (c == '*') { 
                        if (opened) { nested++; opened = false; }
                        else closed = true;    
                    }
                    else if (c == '\n') { curr_lineno++; if (opened) opened = false; if (closed) closed = false;}
                    else if (c == EOF) { 
						error_msg = "EOF in comment";
						Symbol sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                    else { if (opened) opened = false; if (closed) closed = false; }
                    if (nested > 0) state = 34;   
                    break;
                default:
                    throw new Exception(state + ": invalid state");
            } 
        }
    }
}
