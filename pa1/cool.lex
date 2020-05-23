/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%
ZERO = [0]
NONZERODIGIT = [1-9]
DIGIT = [0-9]
UPLETTER = [A-Z]
LOWLETTER = [a-z]
LETTER = ({UPLETTER}|{LOWLETTER})
ESCAPEDCHARS = ("\b"|"\t"|"\n"|"\f")
WHITESPACE = [\ \t]
A_ = [aA]
B_ = [bB]
C_ = [cC]
D_ = [dD]
E_ = [eE]
F_ = [fF]
G_ = [gG]
H_ = [hH]
I_ = [iI]
J_ = [jJ]
K_ = [kK]
L_ = [lL]
M_ = [mM]
N_ = [nN]
O_ = [oO]
P_ = [pP]
Q_ = [qQ]
R_ = [rR]
S_ = [sS]
T_ = [tT]
U_ = [uU]
V_ = [vV]
W_ = [wW]
X_ = [xX]
Y_ = [yY]
Z_ = [zZ]
INHERITS = {I_}{N_}{H_}{E_}{R_}{I_}{T_}{S_}
POOL = {P_}{O_}{O_}{L_}  
CASE = {C_}{A_}{S_}{E_}
NOT = {N_}{O_}{T_}
IN = {I_}{N_}
CLASS = {C_}{L_}{A_}{S_}{S_}
FI = {F_}{I_}
DIV = {D_}{I_}{V_}
LOOP = {L_}{O_}{O_}{P_}
IF = {I_}{F_}
OF = {O_}{F_}
NEW = {N_}{E_}{W_}
ISVOID = {I_}{S_}{V_}{O_}{I_}{D_}
ELSE = {E_}{L_}{S_}{E_}
WHILE = {W_}{H_}{I_}{L_}{E_}
ESAC = {E_}{S_}{A_}{C_}
LET = {L_}{E_}{T_}
THEN = {T_}{H_}{E_}{N_}
BOOL_CONST = [t]{R_}{U_}{E_}|[f]{A_}{L_}{S_}{E_}
OBJECTID = {LOWLETTER}+({LETTER}|{DIGIT}|"_")*
TYPEID = {UPLETTER}+({LETTER}|{DIGIT}|"_")*
INT_CONST = {DIGIT}+
%{
/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

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

    StringBuffer string = new StringBuffer();
    private int nested = 0;
    public static String error_msg = "";
    public static final int MAX_LEN_STRING = 1024;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    Symbol s = null;
    switch(yy_lexical_state) {
    case YYINITIAL:
      /* nothing special to do in the initial state */
      return new Symbol(TokenConstants.EOF);
      //break;
      /* If necessary, add code for other states here, e.g:*/
    case MULTILINE_COMMENT:
      error_msg = "EOF in comment"; 
      yybegin(YYINITIAL);
      s =  new Symbol(TokenConstants.ERROR); s.value = error_msg; return s;
      //break;
    case LINE_COMMENT:
      /* end line comment */
      break;
    case STRING:
      error_msg = "EOF in string constant"; 
      yybegin(YYINITIAL);
      s =  new Symbol(TokenConstants.ERROR); s.value = error_msg; return s;
      //break;
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%line
%state STRING
%state LINE_COMMENT
%state MULTILINE_COMMENT
%state NULLCHAR
%%

<YYINITIAL>"=>"			           { /* Sample lexical rule for "=>" arrow.
                                                Further lexical rules should be defined
                                                 here, after the last %% separator */
                                             return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"("                             { return new Symbol(TokenConstants.LPAREN);  }
<YYINITIAL>")"                             { return new Symbol(TokenConstants.RPAREN);  }
<YYINITIAL>"{"                             { return new Symbol(TokenConstants.LBRACE);  } 
<YYINITIAL>"}"                             { return new Symbol(TokenConstants.RBRACE);  }
<YYINITIAL>"*"                             { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"+"                             { return new Symbol(TokenConstants.PLUS); } 
<YYINITIAL>"-"                             { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"/"                             { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"<-"                            { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"."                             { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>";"                             { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"                             { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"="                             { return new Symbol(TokenConstants.EQ); } 
<YYINITIAL>"<="                            { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"@"                             { return new Symbol(TokenConstants.AT); } 
<YYINITIAL>","                             { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"<"                             { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"~"                             { return new Symbol(TokenConstants.NEG); } 
<YYINITIAL>{INHERITS}                      { return new Symbol(TokenConstants.INHERITS); }                        
<YYINITIAL>{POOL}                          { return new Symbol(TokenConstants.POOL); }  
<YYINITIAL>{CASE}                          { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>\"                              { string.setLength(0); yybegin(STRING); }
<STRING>\"                                 { yybegin(YYINITIAL); 
                                             if (string.length() > MAX_LEN_STRING) {
					       error_msg = "String constant too long";
                                               Symbol s =  new Symbol(TokenConstants.ERROR); s.value = error_msg; return s;
                                              }  
					     else { Symbol s = new Symbol(TokenConstants.STR_CONST); 
					       s.value = AbstractTable.stringtable.addString(string.toString());
					       return s; } }
<STRING>\\\n                              { string.append('\n'); curr_lineno++; } 
<STRING>[^\^@\n\"\\]+                     { string.append( yytext()); }
<STRING>\\t                               { string.append('\t'); } 
<STRING>\\n                               { string.append('\n'); }
<STRING>\\\"                              { string.append('\"'); }
<STRING>\\\\                              { string.append('\\'); }
<STRING>\\b                               { string.append('\b'); }
<STRING>\\f                               { string.append('\f'); }
<STRING>\\[^tbfn\"\\\^@]                  { string.append( yytext().charAt(1) ); }
<STRING>\n                                { yybegin(YYINITIAL); curr_lineno++;  
                                            error_msg = "Unterminated string constant";
                                            //AbstractTable.stringtable.addString(string.deleteCharAt(string.length()-1).toString()); 
                                            Symbol s = new Symbol(TokenConstants.ERROR); s.value = error_msg; return s; }                                   
<STRING>\^@                               { yybegin(NULLCHAR); error_msg = "String contains null character."; Symbol s = new Symbol(TokenConstants.ERROR);  s.value = error_msg; return s; }
<STRING>\\\^@                             { yybegin(NULLCHAR); error_msg = "String contains escaped null character."; Symbol s = new Symbol(TokenConstants.ERROR);  s.value = error_msg; return s; }
<NULLCHAR>[^\"\n]                         { /* skip */ }
<NULLCHAR>\"|\n                           { yybegin(YYINITIAL); }
//<YYINITIAL>\0                           { error_msg = "This contains an escaped null character"; Symbol s = new Symbol(TokenConstants.ERROR);  s.value = error_msg; return s; } 
<YYINITIAL>{NOT}                          { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>{IN}                           { return new Symbol(TokenConstants.IN); }
<YYINITIAL>{CLASS}                        { return new Symbol(TokenConstants.CLASS); } 
<YYINITIAL>{FI}                           { return new Symbol(TokenConstants.FI); }
<YYINITIAL>{DIV}                          { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>{LOOP}                         { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>{IF}                           { return new Symbol(TokenConstants.IF); }
<YYINITIAL>{OF}                           { return new Symbol(TokenConstants.OF); }
<YYINITIAL>{INT_CONST}                    { Symbol s = new Symbol(TokenConstants.INT_CONST); s.value = AbstractTable.inttable.addString(yytext()); return s; }
<YYINITIAL>{NEW}                          { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>{ISVOID}                       { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>{ELSE}                         { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>{WHILE}                        { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>{ESAC}                         { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>{LET}                          { return new Symbol(TokenConstants.LET); }
<YYINITIAL>{THEN}                         { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>{BOOL_CONST}                   { BoolConst bool = null;
                                            if (yytext().equals("true")) bool = BoolConst.truebool;
                                            else if (yytext().equals("false")) bool = BoolConst.falsebool;
					    Symbol s = new Symbol(TokenConstants.BOOL_CONST, bool); s.value = Boolean.valueOf(yytext()); return s; }
<YYINITIAL>{OBJECTID}                     { Symbol s = new Symbol(TokenConstants.OBJECTID); s.value = AbstractTable.idtable.addString(yytext()); return s; }
<YYINITIAL>{TYPEID}                       { Symbol s = new Symbol(TokenConstants.TYPEID); s.value = AbstractTable.idtable.addString(yytext()); return s; }
<YYINITIAL>"*)"                           { error_msg = "Unmatched *)"; Symbol s = new Symbol(TokenConstants.ERROR); s.value = error_msg; return s; } 
<YYINITIAL>"--"                           { yybegin(LINE_COMMENT); }
<LINE_COMMENT>.*                          { /* ignore */ } 
<LINE_COMMENT>\n                          { yybegin(YYINITIAL); curr_lineno++; }
//<YYINITIAL>"(*"                        { yybegin(MULTILINE_COMMENT); string.append(yytext()); nested = 0; }
//<MULTILINE_COMMENT>"(*"                   { nested++; string.append(yytext());  }                             
//<MULTILINE_COMMENT>[^\n*()][()]*[^\n*()]  { /* ignore */  string.append(yytext());}
//<MULTILINE_COMMENT>[^\n*()]               { string.append(yytext()); }
//<MULTILINE_COMMENT>"*"+[^*()\n]?           { /* ignore */  string.append(yytext());}
//<MULTILINE_COMMENT>"*"+")"                { string.append(yytext()); if (nested-- == 0) {yybegin(YYINITIAL); System.out.println("nested: "+nested+" comment: \n"+string); string.delete(0, string.length()-1); }}
//<MULTILINE_COMMENT>\n                     { curr_lineno++; string.append("\n");}
//<YYINITIAL>"(*"                           { yybegin(MULTILINE_COMMENT); nested = 0; }
//<MULTILINE_COMMENT>"(*"                   { nested++;  }                             
//<MULTILINE_COMMENT>[^\n*()]               { /* ignore */ }
//<MULTILINE_COMMENT>[^*()\n][()]+[^*()\n]  { /* ignore */ }
//<MULTILINE_COMMENT>\n[()]+\n            { /* ignore */ }
//<MULTILINE_COMMENT>"*"+[^*()\n]?            { /* ignore */ }
//<MULTILINE_COMMENT>"*"+")"                {  if (nested-- == 0) yybegin(YYINITIAL); }
//<MULTILINE_COMMENT>\n                     { curr_lineno++; }
<YYINITIAL>"(*"                           { yybegin(MULTILINE_COMMENT); nested = 0; }
<MULTILINE_COMMENT>"(*"                   { nested++;  }                             
<MULTILINE_COMMENT>[^\n*()]               { /* ignore */ }
<MULTILINE_COMMENT>[()]                   { /* ignore */ }
<MULTILINE_COMMENT>"*"                    { /* ignore */ }
<MULTILINE_COMMENT>"*)"                   {  if (nested-- == 0) yybegin(YYINITIAL); }
<MULTILINE_COMMENT>\n                     { curr_lineno++; }

<YYINITIAL>{WHITESPACE}+                  { /* skip */ } 
<YYINITIAL>\n                             { curr_lineno++; }
<YYINITIAL>[\f\r\^K]                      { /* skip */ }
.                                         { /* This rule should be the very last
					       in your lexical specification and
                                               will match match everything not
                                               matched by other lexical rules. */
                                               // System.err.println("line: "+get_curr_lineno() + " " + "LEXER BUG - UNMATCHED: " + yytext());
                                            error_msg = yytext(); Symbol s = new Symbol(TokenConstants.ERROR); s.value = error_msg; return s; }

