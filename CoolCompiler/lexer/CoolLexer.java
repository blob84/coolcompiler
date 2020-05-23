package lexer;

import java.io.*;
import java.util.*;
//import java_cup.runtime.Symbol;
import utilities.*;

public class CoolLexer {
    private static final int N = 4096;
    char[] buffer = new char[N*2]; // empty buffer 
    int lexemeBegin = 0;    // start of lexeme    
    int forward = -1;   // pattern matching
    public static int curr_lineno = 1;    //line number
    //static InputStream input;
    static FileReader input;
    private StringBuilder lexeme = new StringBuilder();
    private Hashtable keywords = new Hashtable(); // reserved word table   
    private Hashtable words = new Hashtable(); // ID, TYPE table   

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

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

    public CoolLexer(FileReader input) {
        this.input = input;
        loadBuffer(0, N);
        // reserve keywords
        reserve("if");
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
                c = input.read(); if (c == 0) c = 65534; //store the null character that is 65534
            }
            catch (IOException e) { e.printStackTrace(); }
            if (c == -1) { buffer[i] = TokenConstants.EOF; }
            else buffer[i] = (char) c;   //System.out.println(new String(buffer));
        }
        buffer[end-1] = TokenConstants.EOF;    // sentinel EOF 
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
            case TokenConstants.EOF:
                if (forward == N-1) { // end of first buffer
                    loadBuffer(N, N*2);
                    forward = N;    // inizio del secondo buffer
                }
                else if (forward == (N*2)-1) { // end of second buffer
                    loadBuffer(0, N);
                    forward = 0;    // inizio del primo buffer
                }
                else
                    return TokenConstants.EOF;
            default:
                return buffer[forward];
        }
    }

    public Symbol next_token() throws IOException, FileNotFoundException {
        char c = 0;
        int state = 1;
        int valueNum;
        int strlen = 0; //string constant length
        boolean escape = false; // is escape found in string literal?
        boolean isNullchar = false; // is null char?
        boolean opened = false;
        boolean closed = false;
        int nested = 0; //StringBuilder comm = new StringBuilder();
		Symbol sym = null; //current returned symbol
		getLexeme(); //flush lexeme buffer
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
                    else if (c == TokenConstants.EOF)	return new Symbol(TokenConstants.EOF);
                    else {  
						if ((int)c == 65534) error_msg = ""+'\0'; //print null character
						else error_msg = ""+c;						
						sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
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
                    }	//System.out.println("nextstate: "+state);
                    break;
                case 3: // return TYPE or ID or keyword
                    retract(); 
                    String s = getLexeme();  
                    AbstractSymbol w = (KeywordSymbol) AbstractTable.keywordtable.lookup(s.toLowerCase()); // is it a keyword? Ignoring case
                    if (w != null) { //boolean constant first character could not be uppercase 
						if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false")) { 
							if (Character.isLowerCase(s.charAt(0))) {	//it is a boolean constant if its first letter is lowercase, otherwise is a type id
								BoolConst bool = null;
								if (s.toLowerCase().equals("true")) bool = BoolConst.truebool;
								else if (s.toLowerCase().equals("false")) bool = BoolConst.falsebool;
								sym = new Symbol(TokenConstants.BOOL_CONST, bool); sym.value = Boolean.valueOf(s); return sym; 
							}
						}
						else return new Symbol(((KeywordSymbol) w).token);  
 					}
                    // it is not a keyword    
                    //w = (IdSymbol) AbstractTable.idtable.lookup(lexeme); //is it a type or id ?
                    //if (w != null) { return w; }    
                    if (Character.isLowerCase(s.charAt(0)))   // is ID
                        sym = new Symbol(TokenConstants.OBJECTID);
                    else
						sym = new Symbol(TokenConstants.TYPEID);
                    //else w = new Token(Tag.TYPE, s);   // is Type
					sym.value = AbstractTable.idtable.addString(s); 
					return sym;
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
					sym = new Symbol(TokenConstants.INT_CONST); sym.value = AbstractTable.inttable.addString(getLexeme()); return sym; 
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
                    c = nextChar();	//System.out.println("c: "+c);
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
                case 22: { 
					c = nextChar();					
					if (c == ')') {
						error_msg = "Unmatched *)"; 
						sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
					}
					retract();					
				  	return new Symbol(TokenConstants.MULT); 
                case 23: return new Symbol(TokenConstants.DIV);
                case 24: return new Symbol(TokenConstants.NEG);
                case 25: return new Symbol(TokenConstants.AT);
                case 26: return new Symbol(TokenConstants.DOT);
                //case 27: return new Token(Tag.NOT, getLexeme());
                case 28:    // string literal
                    /* '"' (ANY but '"', '\0', '\n', EOF) '"' */
                    c = nextChar(); //System.out.println("c st: "+getLexeme().toString());
                    if ((int)c == 65534)  
                        state = 32;  
                    else if (c == '\n') { strlen++;
                        if (!escape) {
                            state = 29; 
                            curr_lineno++;
							error_msg = "Unterminated string constant"; 
							sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                        else { curr_lineno++; state = 28; escape = false; } //newline escaped 
					}
                    else if (c == TokenConstants.EOF) { strlen++;
						state = 29; error_msg = "EOF in string constant"; 
						sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                    else if (c == '\\') {   // is newline or double quote escaped?
                        if (!escape) { escape = true; state = 28; } //start escape sequence
						else { escape = false; state = 28; strlen++; } //the escape sequence is escaped, regex: \\\\
					}	 
                    else if (c == '"') {
                        if (!escape) state = 29;  // end string
                        else { state = 28; strlen++; }  //escaped double quote
					}
                    else { if (escape == true && c != 't' && c != 'n' && c != 'f' && c != 'b') escape = false; //end of escape sequence if it is not \n, \t, \f or \b
							state = 28; strlen++; }
                    if (strlen > MAX_LEN_STRING) { } //state = 29; escape = false; } 
                    else { //end of escape sequence if it is \n, \t, \f, \b or \"
						if (c=='t' && escape == true) { c = '\t'; escape = false;} 
						else if (c=='n' && escape == true) { c = '\n'; escape = false;}
						else if (c=='f' && escape == true) { c = '\f'; escape = false;}
						else if (c=='b' && escape == true) { c = '\b'; escape = false;}
						else if (c == '"' && escape == true) { c = '\"'; escape = false; }
						if (c != '\0') buildLexeme(c); } //store string constants except nullchar 
                    break;
                case 29:    // closing string literal
                    if (strlen > MAX_LEN_STRING) { 
		                strlen = 0;
						error_msg = "String constant too long."; //System.out.println("c: "+ c);
						sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; 
					}
					else {
						strlen = 0;		
						sym = new Symbol(TokenConstants.STR_CONST); //System.out.println("here fuiw");
				       	sym.value = AbstractTable.stringtable.addString(this.unescape(getLexeme().toString()).toString()); return sym;
					}
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
                    if (c != '\n' && c != TokenConstants.EOF) { state = 31; }   
                    else { if (c == '\n') curr_lineno++; 
                         skip(); state = 1; } // skip comment, end comment
                    break;
                case 32: //skip string constant content after null char found
                    c = nextChar();
                    if (c != '\n' && c != TokenConstants.EOF && c != '"') { state = 32; }   
                    else { if (c == '\n') curr_lineno++; 
                         skip(); strlen = 0; 
							if (!escape) error_msg = "String contains null character.";
							else { error_msg = "String contains escaped null character."; escape = false; } 
							sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
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
                    else if (c == TokenConstants.EOF) { 
						error_msg = "EOF in comment";
						sym =  new Symbol(TokenConstants.ERROR); sym.value = error_msg; return sym; }
                    else { if (opened) opened = false; if (closed) closed = false; }
                    if (nested > 0) state = 34;   
                    break;
                default:
                    //throw new Exception(state + ": invalid state");
            } 
        }
    }

    public static String unescape(String text) {   // una sequenza di caratterti \c significa c
        boolean escaped = true;
        if (text.length() > 0) text = text.substring(1, text.length()-1); //System.out.println("text: "+text);//toggle double quote from the constant string
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\\' && text.charAt(i+1) != 'n' && text.charAt(i+1) != 'b' && text.charAt(i+1) != 't' && text.charAt(i+1) != 'f')
                escaped = false;
            if (!escaped) { 
                text = text.substring(0, i)+text.substring(i+1, text.length()); 
                escaped = true;
            }
        }
        return text;
    }
}
