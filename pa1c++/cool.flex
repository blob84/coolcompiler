/*
 *  The scanner definition for COOL.
 */

/*
 *  Stuff enclosed in %{ %} in the first section is copied verbatim to the
 *  output, so headers and global definitions are placed here to be visible
 * to the code in the file.  Don't remove anything that was here initially
 */
%{
#include <cool-parse.h>
#include <stringtab.h>
#include <utilities.h>

/* The compiler assumes these identifiers. */
#define yylval cool_yylval
#define yylex  cool_yylex

/* Max size of string constants */
#define MAX_STR_CONST 1025
#define YY_NO_UNPUT   /* keep g++ happy */

extern FILE *fin; /* we read from this file */

/* define YY_INPUT so we read from the FILE fin:
 * This change makes it possible to use this scanner in
 * the Cool compiler.
 */
#undef YY_INPUT
#define YY_INPUT(buf,result,max_size) \
	if ( (result = fread( (char*)buf, sizeof(char), max_size, fin)) < 0) \
		YY_FATAL_ERROR( "read() in flex scanner failed");

char string_buf[MAX_STR_CONST]; /* to assemble string constants */
char *string_buf_ptr;
int nested; 

extern int curr_lineno;
extern int verbose_flag;

extern YYSTYPE cool_yylval;

/*
 *  Add Your own definitions here
 */

%}

/*
 * Define names for regular expressions here.
 */

ZERO [0]
NONZERODIGIT [1-9]
DIGIT [0-9]
UPLETTER  [A-Z]
LOWLETTER  [a-z]
LETTER  ({UPLETTER}|{LOWLETTER})
ESCAPEDCHARS  ("\b"|"\t"|"\n"|"\f")
WHITESPACE  [\ \t]
A_  [aA]
B_  [bB]
C_  [cC]
D_  [dD]
E_  [eE]
F_  [fF]
G_  [gG]
H_  [hH]
I_  [iI]
J_  [jJ]
K_  [kK]
L_  [lL]
M_  [mM]
N_  [nN]
O_  [oO]
P_  [pP]
Q_  [qQ]
R_  [rR]
S_  [sS]
T_  [tT]
U_  [uU]
V_  [vV]
W_  [wW]
X_  [xX]
Y_  [yY]
Z_  [zZ]

DARROW          =>
ASSIGN	<-
LE	<= 	
 /*
  * Keywords are case-insensitive except for the values true and false,
  * which must begin with a lower-case letter.
  */
INHERITS  {I_}{N_}{H_}{E_}{R_}{I_}{T_}{S_}
POOL  {P_}{O_}{O_}{L_}  
CASE  {C_}{A_}{S_}{E_}
NOT  {N_}{O_}{T_}
IN  {I_}{N_}
CLASS  {C_}{L_}{A_}{S_}{S_}
FI  {F_}{I_}
DIV  {D_}{I_}{V_}
LOOP  {L_}{O_}{O_}{P_}
IF  {I_}{F_}
OF  {O_}{F_}
NEW  {N_}{E_}{W_}
ISVOID  {I_}{S_}{V_}{O_}{I_}{D_}
ELSE  {E_}{L_}{S_}{E_}
WHILE  {W_}{H_}{I_}{L_}{E_}
ESAC  {E_}{S_}{A_}{C_}
LET  {L_}{E_}{T_}
THEN  {T_}{H_}{E_}{N_}
BOOL_CONST  t{R_}{U_}{E_}|f{A_}{L_}{S_}{E_}
OBJECTID  {LOWLETTER}+({LETTER}|{DIGIT}|"_")*
TYPEID  {UPLETTER}+({LETTER}|{DIGIT}|"_")*
INT_CONST  {DIGIT}+

%x STRING
%x NULLCHAR
%x LINE_COMMENT
%x MULTILINE_COMMENT

%%
 /*
  *  The multiple-character operators.
  */
{DARROW}		{ return (DARROW); }
"("             { return ('(');  }
")"            	{ return (')');  }
"{"             { return ('{');  } 
"}"             { return ('}');  }
"*"             { return ('*');  }
"+"             { return ('+');  } 
"-"             { return ('-');  }
"/"             { return ('/');  }
{ASSIGN}        { return (ASSIGN); }
"."             { return ('.');  }
";"             { return (';');  }
":"             { return (':');  }
"="             { return ('=');  } 
{LE}            { return (LE); }
"@"             { return ('@');  } 
","             { return (',');  }
"<"             { return ('<');  }
"~"             { return ('~');  } 
{INHERITS}     	{ return (INHERITS); }                        
{POOL}          { return (POOL); }  
{CASE}          { return (CASE); }
\"              { memset(string_buf, 0, sizeof(string_buf)); //clean string buffer
				  string_buf_ptr = string_buf; BEGIN(STRING); }
<STRING>\"      { BEGIN(0); 
                  if (string_buf_ptr-string_buf >= MAX_STR_CONST) {
				  	cool_yylval.error_msg = "String constant too long";
                  	return ERROR;
                  }  
				  else {  
					cool_yylval.symbol = stringtable.add_string(string_buf);
					return STR_CONST; } 
				}
<STRING>\\\n                              { *string_buf_ptr++ = '\n'; curr_lineno++;  } 
<STRING>[^\0\n\"\\]+                     { strcpy(string_buf_ptr, yytext); string_buf_ptr += strlen(yytext); }
<STRING>\\t                               { *string_buf_ptr++ = '\t'; } 
<STRING>\\n                               { *string_buf_ptr++ = '\n'; }
<STRING>\\\"                              { *string_buf_ptr++ = '\"'; }
<STRING>\\\\                              { *string_buf_ptr++ = '\\'; }
<STRING>\\b                               { *string_buf_ptr++ = '\b'; }
<STRING>\\f                               { *string_buf_ptr++ = '\f'; }
 /*
  *  String constants (C syntax)
  *  Escape sequence \c is accepted for all characters c. Except for 
  *  \n \t \b \f, the result is c.
  *
  */
<STRING>\\[^tbfn\"\\\0]                  { *string_buf_ptr++ = yytext[1]; }
<STRING>\n                                { BEGIN(0); curr_lineno++;  
                                            cool_yylval.error_msg = "Unterminated string constant";
                                            return ERROR; }
<STRING>\0                               { BEGIN(NULLCHAR); cool_yylval.error_msg = "String contains null character."; return ERROR; }
<STRING>\\\0                             { BEGIN(NULLCHAR); cool_yylval.error_msg = "String contains escaped null character."; return ERROR; }
<NULLCHAR>[^\"\n]                         { /* skip */ }
<NULLCHAR>\"|\n                           { BEGIN(0); }
{NOT}           { return (NOT); }
{IN}            { return (IN); }
{CLASS}         { return (CLASS); } 
{FI}            { return (FI); }
{LOOP}          { return (LOOP); }
{IF}            { return (IF); }
{OF}            { return (OF); }
{INT_CONST}     { cool_yylval.symbol = inttable.add_string(yytext); return INT_CONST; }
{NEW}           { return (NEW); }
{ISVOID}        { return (ISVOID); }
{ELSE}          { return (ELSE); }
{WHILE}         { return (WHILE); }
{ESAC}          { return (ESAC); }
{LET}           { return (LET); }
{THEN}          { return (THEN); }
{BOOL_CONST}    { char boo[5]; char *s = boo;			
				  char *t = yytext;
				  while (*s++ = tolower(*t++)) 
					; s = boo; 	
				  if (strcmp(s, "true") == 0) cool_yylval.boolean = true;
                  else if (strcmp(s, "false") == 0) cool_yylval.boolean = false;
				  return BOOL_CONST; }
{OBJECTID}                     { cool_yylval.symbol = idtable.add_string(yytext); return OBJECTID; }
{TYPEID}                       { cool_yylval.symbol = idtable.add_string(yytext); return TYPEID; }
"*)"                           { cool_yylval.error_msg = "Unmatched *)"; return ERROR; } 
"--"                           { BEGIN(LINE_COMMENT); }
<LINE_COMMENT>.*	           { /* ignore */ } 
<LINE_COMMENT>\n               { BEGIN(0); curr_lineno++; }
"(*"                           { BEGIN(MULTILINE_COMMENT); nested = 0; }
 /*
  *  Nested comments
  */
<MULTILINE_COMMENT>"(*"                   { nested++;  }                             
<MULTILINE_COMMENT>[^\n*()]               { /* ignore */ }
<MULTILINE_COMMENT>[()]                   { /* ignore */ }
<MULTILINE_COMMENT>"*"                    { /* ignore */ }
<MULTILINE_COMMENT>"*)"                   { if (nested-- == 0) BEGIN(0); }
<MULTILINE_COMMENT>\n                     { curr_lineno++; }
{WHITESPACE}+                  { /* skip */ } 
\n                             { curr_lineno++; }
[\f\r\v]                      { /* skip */ }
<STRING><<EOF>> {
	BEGIN(0);
	cool_yylval.error_msg = "EOF in string constant"; 
	return ERROR;
}
<MULTILINE_COMMENT><<EOF>> {	
	BEGIN(0); nested = 0;	
	cool_yylval.error_msg = "EOF in comment"; 
	return ERROR;
}
<LINE_COMMENT><<EOF>> {
    /* end line comment */
	BEGIN(0);
	return 0;	//return end of file
}
<<EOF>> {	
	return 0; //return end of file
}
.                              { /* This rule should be the very last
					       			in your lexical specification and
                                    will match match everything not
                                    matched by other lexical rules. */
                                    cool_yylval.error_msg = yytext; return ERROR; }
%%
