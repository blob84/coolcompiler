import java_cup.runtime.Symbol;
import java.io.IOException;

public class CoolParser {
    CoolTokenLexer lexer;     
    Symbol[] lookahead;
    private static int bufp = 0;
    private static int k;
	private String curr_non_terminal = ""; //the current non_terminal the parser is analyzing to recover from error
	private boolean error = false;

    public CoolParser(CoolTokenLexer lexer) { //throw Exception {
		this.k = 2;
        this.lexer = lexer;
        //this.k = k;   // numero di token da guardare in avanti (1 + k-1), se LL(2) allora bisogna guardare, oltre al token corrente, 2-1=1 token in avanti.
        lookahead = new Symbol[k];
        for (int i = 0; i < k; i++) 
           advance(); 
    }
    
    public Object match(int x) { //System.out.println("In match lineno: "+curr_lineno()+" token name: "+ Utilities.tokenToString(LL(1)) + "	tokennum: "+LL(1)+ "	x: #"+x);
        if ( LL(1).sym == x ) { 
            Symbol token = LL(1);
            advance();
			if (token.value != null)
				return token.value;
			return Utilities.tokenToString(token);        
		}
		else {	
			syntax_error(LL(1));
			error = true;
			advance();
	        //System.out.println("expecting "+x+ "; found "+ Utilities.tokenToString(LL(1)));
			return null;
		}
    }

    public void advance() {
		try {
        	lookahead[bufp] = lexer.next_token();  
         	bufp = (bufp + 1) % k; 
    	}
		catch (IOException e) {}
	}

    public Symbol LL(int i) {
        return lookahead[(bufp+i-1) % k];
    }

	int curr_lineno() {
		return lexer.curr_lineno();
    }

    AbstractSymbol curr_filename() {
		return lexer.curr_filename();
    }

	public int error_recovery() {
		//advance();
		if (curr_non_terminal.equals("class")) {//go to next class if there is an error in the class definition
			while (LL(1).sym != TokenConstants.CLASS && LL(1).sym != TokenConstants.EOF) { 
			//System.out.println("In match lineno: "+curr_lineno()+" token name: "+ Utilities.tokenToString(LL(1)) + "	tokennum: "+LL(1));
				advance();	}
		}
		else if (curr_non_terminal.equals("feature")) {  //if the token of next variable or IN is not found go to next expression inside the block or go to the next class
			while (LL(1).sym != TokenConstants.OBJECTID && LL(1).sym != TokenConstants.CLASS && LL(1).sym != TokenConstants.EOF) {
			//System.out.println("In match lineno: "+curr_lineno()+" token name: "+ Utilities.tokenToString(LL(1)) + "	tokennum: "+LL(1));
				advance(); }
		}
		else if (curr_non_terminal.equals("letexpr")) //if the token of next variable or IN is not found go to next expression inside the block or go to the next class
			while (LL(1).sym != TokenConstants.COMMA && LL(1).sym != TokenConstants.IN && LL(1).sym != TokenConstants.INT_CONST && LL(1).sym != TokenConstants.STR_CONST && 
				LL(1).sym != TokenConstants.NOT && LL(1).sym != TokenConstants.ISVOID && LL(1).sym != TokenConstants.LET && LL(1).sym != TokenConstants.IF && LL(1).sym != TokenConstants.WHILE &&
                 LL(1).sym != TokenConstants.CASE && LL(1).sym != TokenConstants.NEW && LL(1).sym != TokenConstants.BOOL_CONST && LL(1).sym != TokenConstants.NEG && 
				LL(1).sym != TokenConstants.CLASS && LL(1).sym != TokenConstants.EOF) { 
			//System.out.println("In match lineno: "+curr_lineno()+" token name: "+ Utilities.tokenToString(LL(1)) + "	tokennum: "+LL(1));
				advance(); }
		else if (curr_non_terminal.equals("blockexpr")) { 
			while (LL(1).sym != TokenConstants.OBJECTID && LL(1).sym != TokenConstants.INT_CONST && LL(1).sym != TokenConstants.STR_CONST && LL(1).sym != TokenConstants.NOT &&
                 LL(1).sym != TokenConstants.ISVOID && LL(1).sym != TokenConstants.LET && LL(1).sym != TokenConstants.IF && LL(1).sym != TokenConstants.WHILE &&
                 LL(1).sym != TokenConstants.CASE && LL(1).sym != TokenConstants.NEW && LL(1).sym != TokenConstants.BOOL_CONST && LL(1).sym != TokenConstants.NEG && 
				LL(1).sym != TokenConstants.CLASS && LL(1).sym != TokenConstants.EOF) {
				//System.out.println("In match lineno: "+curr_lineno()+" token name: "+ Utilities.tokenToString(LL(1)) + "	tokennum: "+LL(1));
				//if (LL(1).sym == TokenConstants.RBRACE && enter) curr_non_terminal = ""
				advance(); }
		}
		error = false;
		return LL(1).sym; //return the last token found
	}

    int omerrs = 0;

    public void syntax_error(Symbol cur_token) {
        int lineno = curr_lineno(); 
		String filename = curr_filename().getString();
		    System.err.print("\"" + filename + "\", line " + lineno + 
				     ": parse error at or near ");
		    Utilities.printToken(cur_token);
		omerrs++;
		if (omerrs>50) {
		   System.err.println("More than 50 errors");
		   System.exit(1);
		}
    }

/*    public void retractToken() throws SyntaxError, Exception {  // inserisci k token nel buffer
        //nextLookaheads[0] = lookahead; 
        for (int i = 0; i < k; i++) 
            nextLookaheads[i] = lexer.nextToken(); 
        //System.out.println("nexlook[0]"+nextLookaheads[0]);
    }

    boolean isEmptyLookaheads() {    //verifica se il buffer dei lookahead è vuoto
        int i = k-1; 
        while (i >= 0 && nextLookaheads[i] == null) i--; 
        if (i+1 == 0) return true;
        return false;        
    }
*/

public Symbol debug_parse() { return null; }

public Symbol parse() { return program(); }	

/* program ::= classP SEMI { classP SEMI } */
    public Symbol program() {
		curr_non_terminal = "program";
		Classes classes = new Classes(curr_lineno());
		Class_ c; 
		
		Symbol s = classP();
		if (s != null) {
			c = (Class_) s.value; 
			match(TokenConstants.SEMI);
			if (error) error_recovery(); //class error, go to next class
			else classes.appendElement(c); 
		}
		else 
			error_recovery(); //class error, go to next class
		curr_non_terminal = "program";
        while (LL(1).sym == TokenConstants.CLASS) {
			s = classP();
			if (s != null)           
				c = (Class_) s.value; 
			else { int token = error_recovery(); if (token == TokenConstants.EOF) return null;
					continue; } //class error, go to next class
			match(TokenConstants.SEMI); //match(TokenConstants.EOF_TYPEID);
			if (error) { 
				error_recovery(); continue; } //class error, go to next class
			else classes.appendElement(c); 
        }
		curr_non_terminal = "program";
		return new Symbol(0, new programc(curr_lineno(), classes));
    } 

/* classP ::= CLASS TYPEID [ INHERITS TYPEID ] LBRACE { feature SEMI } RBRACE 
          */
    Symbol classP() {
		curr_non_terminal = "class";
	   	AbstractSymbol name;
	    AbstractSymbol parent = null;
	    Features features = new Features(curr_lineno());
		Feature feature; 

        match(TokenConstants.CLASS);
		if (error) return null;  

		name = (AbstractSymbol) match(TokenConstants.TYPEID);
		if (error) return null;  

        if (LL(1).sym == TokenConstants.INHERITS) { 
            match(TokenConstants.INHERITS); 
			if (error) return null; 

			parent = (AbstractSymbol) match(TokenConstants.TYPEID);
			if (error) return null;  
        }
        match(TokenConstants.LBRACE);
		if (error) return null;  

        while (LL(1).sym == TokenConstants.OBJECTID) { 
			Symbol s = feature();
			if (s != null)			
				feature = (Feature) s.value; 
			else { 
				int token = error_recovery(); 	
				if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //there aren't other features, go to next class
			continue; }	//feature error, go to next feature

            features.appendElement(feature); 
			match(TokenConstants.SEMI); 
			if (error) { //feature error, go to next feature
				int token = error_recovery(); 	//System.out.println("LL(1): "+LL(1).value.toString());
				if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //there aren't other features, go to next class
			}
        }
        match(TokenConstants.RBRACE); 
		if (error) return null;  

		curr_non_terminal = "class";
		if (parent == null)
			return new Symbol(0, new class_c(curr_lineno(), name, AbstractTable.idtable.addString("Object"), features, curr_filename()));
		return new Symbol(0, new class_c(curr_lineno(), name, parent, features, curr_filename()));
    }

/*feature ::= OBJECTID featureprime 
            */
    Symbol feature() {
		curr_non_terminal = "feature";
    	AbstractSymbol name;
		Feature feature;

		name = (AbstractSymbol) match(TokenConstants.OBJECTID);	//System.out.println("feat name: "+name);
		if (error) return null;   

		feature = featureprime(name);
		if (feature == null) return null;   

        return new Symbol(0, feature);
    } 
    
/* featureprime ::= LPAREN [ formal { COMMA formal } ] RPAREN COLON TYPEID LBRACE expr RBRACE
            | COLON TYPEID [ ASSIGN expr ]
            */
    Feature featureprime(AbstractSymbol name) { 
		curr_non_terminal = "feature";
    	Formals formals = new Formals(curr_lineno());
    	AbstractSymbol return_type;
    	Expression expr;
    	AbstractSymbol type_decl;
    	Expression init = new no_expr(curr_lineno());

        if (LL(1).sym == TokenConstants.LPAREN) {	//it is a method
            match(TokenConstants.LPAREN);
			if (error) return null;

            if (LL(1).sym == TokenConstants.OBJECTID) { 
				Symbol s = formal();
				curr_non_terminal = "feature";
				if (s != null)
                	formals.appendElement((Formal) s.value);
				else return null; 
                while (LL(1).sym == TokenConstants.COMMA) { 
                    match(TokenConstants.COMMA); 
					if (error) return null;
					formals.appendElement((Formal) formal().value);
                }
            }
            match(TokenConstants.RPAREN); 
			if (error) return null;

			match(TokenConstants.COLON);  
			if (error) return null;

			return_type = (AbstractSymbol) match(TokenConstants.TYPEID); 
			if (error) return null;

			match(TokenConstants.LBRACE);
			if (error) return null; 

			Symbol e = expr();
			curr_non_terminal = "feature"; 
			if (e !=  null)
				expr = (Expression) e.value; 
			else return null;

			match(TokenConstants.RBRACE);
			if (error) return null; 

			return new method(curr_lineno(), name, formals, return_type, expr);
        }
        else if (LL(1).sym == TokenConstants.COLON) {	//it is an attribute     
            match(TokenConstants.COLON); 
			if (error) return null;

			type_decl = (AbstractSymbol) match(TokenConstants.TYPEID);
			if (error) return null; 

            if (LL(1).sym == TokenConstants.ASSIGN) {
                match(TokenConstants.ASSIGN);
				if (error) return null;
				Symbol e = expr();
				curr_non_terminal = "feature";
				if (e != null)
					init = (Expression) e.value;
				else return null;
            }
			return new attr(curr_lineno(), name, type_decl, init);
        }
        //else syntax_error("expecting "+TokenConstants.getTokenName(TokenConstants.COLON)+", "+TokenConstants.getTokenName(TokenConstants.COLON)+"; found "+ TokenConstants.getTokenName(LL(1).sym), lexer.linenum);
		else {
			syntax_error(LL(1));
			error = true;
			return null;
		}
    }    

/* formal ::= OBJECTID COLON TYPEID
            */
    Symbol formal() {
		curr_non_terminal = "formal";
	    AbstractSymbol name;
	    AbstractSymbol type_decl;
 
        name = (AbstractSymbol) match(TokenConstants.OBJECTID); 
		if (error) { curr_non_terminal = "formal"; return null; }

		match(TokenConstants.COLON); 
		if (error) { curr_non_terminal = "formal"; return null; }
		
		type_decl = (AbstractSymbol) match(TokenConstants.TYPEID);
		if (error) { curr_non_terminal = "formal"; return null; }

		curr_non_terminal = "formal"; 
		return new Symbol(0, new formalc(curr_lineno(), name, type_decl));
    }

/* expr ::= OBJECTID ASSIGN expr  
            | notexpr */
    Symbol expr() {
		curr_non_terminal = "expr";
		AbstractSymbol name;
		Expression e1;

        if (LL(1).sym == TokenConstants.OBJECTID && LL(2).sym == TokenConstants.ASSIGN) {   
            name = (AbstractSymbol) match(TokenConstants.OBJECTID);	
			if (error) return null;	

            match(TokenConstants.ASSIGN);
			if (error) return null;	

			Symbol e = expr();
			if (e != null)
            	e1 = (Expression) e.value;
			else return null; 
			return new Symbol(0, new assign(curr_lineno(), name, e1));
        }    
        else {	
            switch (LL(1).sym) {
                case TokenConstants.OBJECTID: case TokenConstants.NOT: case TokenConstants.INT_CONST: case TokenConstants.STR_CONST: case TokenConstants.ISVOID: case TokenConstants.LET: 
                case TokenConstants.IF: case TokenConstants.WHILE: case TokenConstants.CASE: case TokenConstants.NEW: case TokenConstants.BOOL_CONST: 
                case TokenConstants.LPAREN: case TokenConstants.LBRACE: case TokenConstants.NEG: 
					e1 = notexpr();
					curr_non_terminal = "expr";
					if (e1 == null) return null;
                    return new Symbol(0, e1);
                default: syntax_error(LL(1)); 
						error = true; return null;
                /*String tokensexp = TokenConstants.getTokenName(TokenConstants.OBJECTID)+", "+TokenConstants.getTokenName(TokenConstants.NOT)+", "+TokenConstants.getTokenName(TokenConstants.INT_CONST)+", "+TokenConstants.getTokenName(TokenConstants.STR_CONST)+", "+TokenConstants.getTokenName(TokenConstants.ISVOID)+", "+TokenConstants.getTokenName(TokenConstants.LET)
                                   +", "+TokenConstants.getTokenName(TokenConstants.IF)+", "+TokenConstants.getTokenName(TokenConstants.WHILE)+", "+TokenConstants.getTokenName(TokenConstants.CASE)+", "+TokenConstants.getTokenName(TokenConstants.NEW)+", "+TokenConstants.getTokenName(TokenConstants.TRUE)+", "+TokenConstants.getTokenName(TokenConstants.FALSE)
                                   +", "+TokenConstants.getTokenName(TokenConstants.LPAREN)+", "+TokenConstants.getTokenName(TokenConstants.LBRACE)+", "+TokenConstants.getTokenName(TokenConstants.NEG); 
                throw new SyntaxError("expecting "+tokensexp+"; found "+ TokenConstants.getTokenName(LL(1).sym), lexer.linenum); */
            }
        }
		//return null;
     }                      

/* notexpr ::= { NOT } relexpr
            */
    Expression notexpr() {
		curr_non_terminal = "notexpr";
    	Expression e1;

		if (LL(1).sym == TokenConstants.NOT) {
        	while (LL(1).sym == TokenConstants.NOT) {
            	match(TokenConstants.NOT);
				if (error) return null;	
			}
        	e1 = relexpr();
			curr_non_terminal = "notexpr";
			if (e1 != null)
				return new comp(curr_lineno(), e1);
			return null;
		}
		e1 = relexpr();
		curr_non_terminal = "notexpr";
		return e1;
    }

/* relexpr ::= addexpr [ ( LT | LE | EQ ) addexpr ]
            */
    Expression relexpr() {
		curr_non_terminal = "relexpr";
	    Expression e1;
	    Expression e2;
		int op = 0;

        e1 = addexpr();
		curr_non_terminal = "relexpr";
        if (LL(1).sym == TokenConstants.LT || LL(1).sym == TokenConstants.LE || LL(1).sym == TokenConstants.EQ ) {
            if (LL(1).sym == TokenConstants.LT) {
				op = LL(1).sym;			
                match(TokenConstants.LT);
				if (error) return null;	
			}
            else if (LL(1).sym == TokenConstants.LE) {
				op = LL(1).sym;			
                match(TokenConstants.LE);
				if (error) return null;	
			}
            else {
				op = LL(1).sym;			
                match(TokenConstants.EQ);
				if (error) return null;	
			}
            e2 = addexpr();
			curr_non_terminal = "relexpr";
			if (e1 == null || e2 == null) return null;
			if (op == TokenConstants.LT)
				return new lt(curr_lineno(), e1, e2);
			else if (op == TokenConstants.LE)
				return new leq(curr_lineno(), e1, e2);
			else  
				return new eq(curr_lineno(), e1, e2);
		}
		return e1;
    }

/* addexpr ::= mulexpr { ( PLUS | MINUS ) mulexpr }          
            */
    Expression addexpr() {
		curr_non_terminal = "addexpr";
	    Expression e1;
	    Expression e2;
		int op = 0;

        e1 = mulexpr();  
		curr_non_terminal = "addexpr";
        while (LL(1).sym == TokenConstants.PLUS || LL(1).sym == TokenConstants.MINUS) { 
            if ((op = LL(1).sym) == TokenConstants.PLUS) {
                match(TokenConstants.PLUS);
				if (error) return null;	
			}
            else if ((op = LL(1).sym) == TokenConstants.MINUS) { 
                match(TokenConstants.MINUS);
				if (error) return null;	
			}
            e2 = mulexpr();
			curr_non_terminal = "addexpr";
			if (e1 == null || e2 == null) return null;
			if (op == TokenConstants.PLUS)
				e1 = new plus(curr_lineno(), e1, e2);
			else
				e1 = new sub(curr_lineno(), e1, e2);
        }  
		return e1;
    }

/* mulexpr ::= isvoidexpr { ( MULT | DIV ) isvoidexpr } 
            */
    Expression mulexpr() {
		curr_non_terminal = "mulexpr";
	    Expression e1;
	    Expression e2;
		int op = 0;

        e1 = isvoidexpr();
		curr_non_terminal = "mulexpr";
        while (LL(1).sym == TokenConstants.MULT || LL(1).sym == TokenConstants.DIV) {
            if ((op = LL(1).sym) == TokenConstants.MULT) {
                match(TokenConstants.MULT);
				if (error) return null;	
			}
            else if ((op = LL(1).sym) == TokenConstants.DIV) { 
                match(TokenConstants.DIV);
				if (error) return null;	
			}
            e2 = isvoidexpr();
			curr_non_terminal = "mulexpr";
			if (e1 == null || e2 == null) return null;
			if (op == TokenConstants.DIV)
				e1 = new divide(curr_lineno(), e1, e2);
			else
				e1 = new mul(curr_lineno(), e1, e2);
        }    
		return e1;
    }

/*isvoidexpr ::= { ISVOID } negexpr
            */
    Expression isvoidexpr() {
		curr_non_terminal = "isvoidexpr";
		Expression e1;

        if (LL(1).sym == TokenConstants.ISVOID) { 
        	while (LL(1).sym == TokenConstants.ISVOID) {
            	match(TokenConstants.ISVOID); 
				if (error) return null;	
			}
        	e1 = negexpr();
			curr_non_terminal = "isvoidexpr";
			if (e1 == null) return null;
			return new isvoid(curr_lineno(), e1);
		}
		e1 = negexpr();
		curr_non_terminal = "isvoidexpr";
		return e1;    
	}

/* negexpr ::= { NEG } dispatchexpr
           */
    Expression negexpr() {
		curr_non_terminal = "negexpr";
		Expression e1;

        if (LL(1).sym == TokenConstants.NEG) { 
        	while (LL(1).sym == TokenConstants.NEG) {
            	match(TokenConstants.NEG); 
				if (error) return null;	
			}
        	e1 = dispatchexpr();
			curr_non_terminal = "negexpr";
			if (e1 == null) return null;
			return new neg(curr_lineno(), e1);
		}
		e1 = dispatchexpr();
		curr_non_terminal = "negexpr";
		return e1;    
	}

/* dispatchexpr= otherexpr { [AT TYPEID] DOT OBJECTID callexpr }
            */
    Expression dispatchexpr() { 
		curr_non_terminal = "dispatchexpr";
	    Expression expr;
    	AbstractSymbol name;
    	AbstractSymbol type_name = null;
    	Expressions actual;

        expr = otherexpr();
		curr_non_terminal = "dispatchexpr";
		if (expr == null) return null;
        while (LL(1).sym == TokenConstants.AT || LL(1).sym == TokenConstants.DOT) {
            if (LL(1).sym == TokenConstants.AT) {
				match(TokenConstants.AT); 
				if (error) return null;	

			   	type_name = (AbstractSymbol) match(TokenConstants.TYPEID);
				if (error) return null;	
			} 
			match(TokenConstants.DOT);
			if (error) return null;	

			name = (AbstractSymbol) match(TokenConstants.OBJECTID);
			if (error) return null;	

			actual = callexpr(); 
			curr_non_terminal = "dispatchexpr";	
			if (error) return null; 
			if (type_name == null)
				expr = new dispatch(curr_lineno(), expr, name, actual);
			else
				expr = new static_dispatch(curr_lineno(), expr, type_name, name, actual);
        }
		return expr;    
    }

/*
    Expression dispatchexpr() throws SyntaxError, Exception {
	    Expression expr;
    	AbstractSymbol name;
    	AbstractSymbol type_name = null;
    	Expressions actual;

        expr = otherexpr();
        while (LL(1).sym == TokenConstants.AT || LL(1).sym == TokenConstants.DOT) {
            if (LL(1).sym == TokenConstants.AT) 
                actual = atexpr(expr, name, type_name, actual);
            else if (LL(1).sym == TokenConstants.DOT)
                actual = dotexpr(expr, name, type_name, actual);
        }

       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }
*//* atexpr ::= AT TYPEID dotexpr
            */    
/*    Expression atexpr(Expression expr, AbstractSymbol name, AbstractSymbol type_name, Expressions actual) throws SyntaxError, Exception {
        match(TokenConstants.AT); 
		type_name = match(TokenConstants.TYPEID); 
		dotexpr(Expression expr, AbstractSymbol name, AbstractSymbol type_name, Expressions actual);

    }
       
*//* dotexpr ::= DOT OBJECTID callexpr
            */     
/*    Expression dotexpr(Expression expr, AbstractSymbol name, Expressions actual) throws SyntaxError, Exception {
	    Expression expr;
    	AbstractSymbol name;
    	AbstractSymbol type_name = null;
    	Expressions actual;

        match(TokenConstants.DOT); 
		name = match(TokenConstants.OBJECTID); 
		actual = callexpr();

		return new 
		
    }
*/
/* 
	otherexpr ::= LET OBJECTID COLON TYPEID [ ASSIGN expr ] letexpr_nested  
            | IF expr THEN expr ELSE expr FI
            | WHILE expr LOOP expr POOL
            | CASE expr OF branchexpr ESAC 
            | OBJECTID [ callexpr ]            
            | NEW TYPEID
            | LBRACE expr SEMI { expr SEMI } RBRACE
            | LPAREN expr RPAREN
            | INT_CONST | STR_CONST | TRUE | FALSE

	letexpr_nested ::= { COMMA OBJECTID COLON TYPEID [ ASSIGN expr ] } IN expr
	branchexpr ::= OBJECTID COLON TYPEID ARROW expr SEMI { OBJECTID COLON TYPEID ARROW expr SEMI }
*/

	Expression letexpr_nested() {
    	AbstractSymbol identifier;
    	AbstractSymbol type_decl;
    	Expression init = new no_expr(curr_lineno());
		Expression body;

        if (LL(1).sym == TokenConstants.COMMA) {
          	match(TokenConstants.COMMA); 
			if (error) return null; 

			identifier = (AbstractSymbol) match(TokenConstants.OBJECTID); 
			if (error) {
				error_recovery(); //let expression error, go to next variable
				body = letexpr_nested();
				return null;
			}		
			else {
				match(TokenConstants.COLON); 
				if (error) {
					error_recovery(); //let expression error, go to next variable
					body = letexpr_nested();
					return null;
				}		
				else { 
					type_decl = (AbstractSymbol) match(TokenConstants.TYPEID);
					if (error) {
						error_recovery(); //let expression error, go to next variable
						body = letexpr_nested();
						return null;
					}		
					else {
						if (LL(1).sym == TokenConstants.ASSIGN) {
						    match(TokenConstants.ASSIGN); 
							if (error) error_recovery(); //let expression error, go to next variable
							else {
								Symbol e = expr();
								curr_non_terminal = "letexpr";
								if (e != null)
									init = (Expression) e.value;
								else error_recovery(); //let expression error, go to next variable
							}
						}
						body = letexpr_nested();
						if (body == null) return null;
						return new let(curr_lineno(), identifier, type_decl, init, body);
					}
            	}
        	}
	   	}
		else {
        	match(TokenConstants.IN); 
			if (error) return null; 
	
			Symbol e = expr(); //System.out.println("error: "+ error+"	curr_non_terminal: "+ curr_non_terminal+ "	typeid: "+ (String) LL(1).value.toString());
			curr_non_terminal = "letexpr";	
			if (e != null)
				body = (Expression) e.value;
			else return null; 
			return body;
		}
	}

	Cases branchexpr() {
		curr_non_terminal = "otherexpr";
   		AbstractSymbol name;
    	AbstractSymbol type_decl;
    	Expression expr;
		Cases cases = new Cases(curr_lineno());

		name = (AbstractSymbol) match(TokenConstants.OBJECTID); 
		if (error) return cases; 
		match(TokenConstants.COLON);
		if (error) return cases; 
        type_decl = (AbstractSymbol) match(TokenConstants.TYPEID); 
		if (error) return cases; 		
		match(TokenConstants.DARROW);
		if (error) return cases; 
		Symbol e = expr();
		curr_non_terminal = "otherexpr";
		if (e != null)
        	expr = (Expression) e.value;
		else return cases; 
		match(TokenConstants.SEMI);
		if (error) return cases; 
		cases.appendElement(new branch(curr_lineno(), name, type_decl, expr));
        while (LL(1).sym == TokenConstants.OBJECTID) {
        	name = (AbstractSymbol) match(TokenConstants.OBJECTID);
			if (error) return cases; 
		 	match(TokenConstants.COLON);
			if (error) return cases;             
			type_decl = (AbstractSymbol) match(TokenConstants.TYPEID); 
			if (error) return cases; 			
			match(TokenConstants.DARROW);
			if (error) return cases;             
			e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)			
				expr = (Expression) e.value; 
			else return cases;			
			match(TokenConstants.SEMI);
			if (error) return cases;   
			cases.appendElement(new branch(curr_lineno(), name, type_decl, expr));
        }
		return cases;
	}

    Expression otherexpr() {
		curr_non_terminal = "otherexpr";
		//let expression with more than one identifiers are converted to nested let expression,
		//for example: let e1, e2, e3 in e4 <=> let e1 in ( let e2 in ( let e3 in e4 ) )
        if (LL(1).sym == TokenConstants.LET) {
			curr_non_terminal = "letexpr";
	    	AbstractSymbol identifier;
	    	AbstractSymbol type_decl;
	    	Expression init = new no_expr(curr_lineno());
	    	Expression body = null;

            match(TokenConstants.LET); 
			if (error) return null; 

			identifier = (AbstractSymbol) match(TokenConstants.OBJECTID); 
			if (error) { 
				int token = error_recovery(); //let expression error, go to next variable or IN or next block expression or next feature or class
				if (LL(1).sym == TokenConstants.IN)
					letexpr_nested();
			}
			else {
				match(TokenConstants.COLON); 
				if (error) { 
					error_recovery(); //let expression error, go to next variable or IN
					if (LL(1).sym == TokenConstants.IN)
						letexpr_nested();
				}
				else { 
					type_decl = (AbstractSymbol) match(TokenConstants.TYPEID);
					if (error) {  //System.out.println("sono qui");
						error_recovery(); //let expression error, go to next variable or IN
						if (LL(1).sym == TokenConstants.IN) { //System.out.println("sono qui");
							letexpr_nested(); }
					}
					else {
						if (LL(1).sym == TokenConstants.ASSIGN) {
						    match(TokenConstants.ASSIGN); 
							if (error) { 
								error_recovery(); //let expression error, go to next variable or IN
								if (LL(1).sym == TokenConstants.IN) //if next token is IN parse from IN
									letexpr_nested();
							}
							else {
								Symbol e = expr();
								curr_non_terminal = "letexpr";
								if (e != null)
									init = (Expression) e.value;
								else { 
									error_recovery(); //let expression error, go to next variable or IN
									if (LL(1).sym == TokenConstants.IN)
										letexpr_nested();
								}
							}
						}
						body = letexpr_nested();
						if (body == null) return null;
						return new let(curr_lineno(), identifier, type_decl, init, body);
					}
            	}
        	}
			return null;
		}
        else if (LL(1).sym == TokenConstants.IF) {	
	    	Expression pred;
	    	Expression then_exp;
	    	Expression else_exp;
	
            match(TokenConstants.IF); 
			if (error) return null; 

			Symbol e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)
				pred = (Expression) e.value;
			else return null; 

			match(TokenConstants.THEN); 
			if (error) return null; 

			e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)			
				then_exp = (Expression) e.value; 
			else return null;

			match(TokenConstants.ELSE);
			if (error) return null; 

			e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)
            	else_exp = (Expression) e.value; 
			else return null;

			match(TokenConstants.FI);
			if (error) return null; 

			return new cond(curr_lineno(), pred, then_exp, else_exp);
        }
        else if (LL(1).sym == TokenConstants.WHILE) {
	    	Expression pred;
	    	Expression body = null;
            
			match(TokenConstants.WHILE); 
			if (error) return null; 

			Symbol e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)
				pred = (Expression) e.value; 
			else return null;

			match(TokenConstants.LOOP);
			if (error) return null; 

			e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)
            	body = (Expression) e.value; 
			match(TokenConstants.POOL);
			if (error) return null; 

			return new loop(curr_lineno(), pred, body);
        }
        else if (LL(1).sym == TokenConstants.CASE) {
    		Expression expr;
    		Cases cases;
 
            match(TokenConstants.CASE); 
			if (error) return null; 

			Symbol e = expr();
			curr_non_terminal = "otherexpr";
			if (e != null)
				expr = (Expression) e.value;
			else return null; 

			match(TokenConstants.OF);
			if (error) return null; 

			cases = branchexpr();

            match(TokenConstants.ESAC);
			if (error) return null; 

			return new typcase(curr_lineno(), expr, cases);
        } 
        else if (LL(1).sym == TokenConstants.OBJECTID) {
    		Expression expr;
    		AbstractSymbol name;
    		Expressions actual = null;
            
			name = (AbstractSymbol) match(TokenConstants.OBJECTID); 
			if (error) return null; 

            if (LL(1).sym == TokenConstants.LPAREN) {
				expr = (Expression) new object(curr_lineno(), AbstractTable.idtable.addString("self"));
                actual = callexpr();
				curr_non_terminal = "otherexpr"; 
				if (error) return null;
				return new dispatch(curr_lineno(), expr, name, actual);
			}
			return new object(curr_lineno(), name);
        }
        else if (LL(1).sym == TokenConstants.NEW) {
			AbstractSymbol type_name;

            match(TokenConstants.NEW);
			if (error) return null; 

			type_name = (AbstractSymbol) match(TokenConstants.TYPEID);
			if (error) return null; 

			return new new_(curr_lineno(), type_name);
        }
        else if (LL(1).sym == TokenConstants.LBRACE) {
			curr_non_terminal = "blockexpr";
    		Expressions body = new Expressions(curr_lineno());
			boolean blockerror = false;

            match(TokenConstants.LBRACE); 
			if (error) return null; 
			else  {
				Symbol e = expr();
				curr_non_terminal = "blockexpr";
				if (e != null) {
					body.appendElement((Expression) e.value); 
					match(TokenConstants.SEMI);
					if (error) { 
						blockerror = true;
						int token = error_recovery(); //go to next block expression
						if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //return if there isn't a next block expression
					}
				} 
				else {	
					blockerror = true;				
					int token = error_recovery(); //go to next block expression
					if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //return if there isn't a next block expression
				}
			}
			while (true) {
		        if (LL(1).sym == TokenConstants.OBJECTID || LL(1).sym == TokenConstants.INT_CONST || LL(1).sym == TokenConstants.STR_CONST || LL(1).sym == TokenConstants.NOT ||
		             LL(1).sym == TokenConstants.ISVOID || LL(1).sym == TokenConstants.LET || LL(1).sym == TokenConstants.IF || LL(1).sym == TokenConstants.WHILE ||
		             LL(1).sym == TokenConstants.CASE || LL(1).sym == TokenConstants.NEW || LL(1).sym == TokenConstants.BOOL_CONST || 
		             LL(1).sym == TokenConstants.LPAREN || LL(1).sym == TokenConstants.LBRACE || LL(1).sym == TokenConstants.NEG ) {

					Symbol e = expr();
					curr_non_terminal = "blockexpr";
					if (e != null) {
		            	body.appendElement((Expression) e.value); 
						match(TokenConstants.SEMI);
						if (error) { 
							blockerror = true;
							int token = error_recovery(); //go to next block expression
							if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //return if there isn't a next block expression
						}
					}
					else { blockerror = true; error_recovery(); 
							int token = error_recovery(); //go to next block expression
							if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //return if there isn't a next block expression
					} 
		        }
				else { blockerror = true; syntax_error(LL(1)); error = true; 
							int token = error_recovery(); //go to next block expression
							if (token == TokenConstants.CLASS || token == TokenConstants.EOF) return null; //return if there isn't a next block expression
				}			
				if (LL(1).sym == TokenConstants.RBRACE) break;
				else if (LL(1).sym == TokenConstants.CLASS || LL(1).sym == TokenConstants.EOF) return null; 
			}
            match(TokenConstants.RBRACE);
			if (error) return null; 
			if (blockerror) return null;
			return new block(curr_lineno(), body);
        }
        else if (LL(1).sym == TokenConstants.LPAREN) {
			Expression e = null;
            match(TokenConstants.LPAREN); 
			if (error) return null; 
			Symbol s = expr();
			curr_non_terminal = "otherexpr";
			if (s != null) 
				e = (Expression) s.value; 
			match(TokenConstants.RPAREN);
			if (error) return null; 
			return e;
        }
        else if (LL(1).sym == TokenConstants.INT_CONST) {
            AbstractSymbol token = (AbstractSymbol) match(TokenConstants.INT_CONST);
			if (error) return null; 
			return new int_const(curr_lineno(), token);
		}
        else if (LL(1).sym == TokenConstants.STR_CONST) {
         	AbstractSymbol token = (AbstractSymbol) match(TokenConstants.STR_CONST); 
			if (error) return null;  
			return new string_const(curr_lineno(), token);
		}
        else if (LL(1).sym == TokenConstants.BOOL_CONST) {
            Boolean token = (Boolean) match(TokenConstants.BOOL_CONST);
			if (error) return null; 
			return new bool_const(curr_lineno(), token);
		}
		else {
			syntax_error(LL(1)); 
			error = true;
			return null;
		}
        /*else throw new SyntaxError("expecting "+TokenConstants.getTokenName(TokenConstants.LET)+", "+TokenConstants.getTokenName(TokenConstants.IF)+", "+TokenConstants.getTokenName(TokenConstants.WHILE)+", "+TokenConstants.getTokenName(TokenConstants.CASE)+", "
                                    +TokenConstants.getTokenName(TokenConstants.OBJECTID)+", "+TokenConstants.getTokenName(TokenConstants.LPAREN)+", "+TokenConstants.getTokenName(TokenConstants.NEW)+", "+TokenConstants.getTokenName(TokenConstants.LBRACE)+", "+TokenConstants.getTokenName(TokenConstants.LPAREN)+", "
                                    +TokenConstants.getTokenName(TokenConstants.INT_CONST)+", "+TokenConstants.getTokenName(TokenConstants.STR_CONST)+", "+TokenConstants.getTokenName(TokenConstants.TRUE)+", "+TokenConstants.getTokenName(TokenConstants.FALSE)+"; found "+ TokenConstants.getTokenName(LL(1).sym), lexer.linenum); */
    }

/* callexpr ::= LPAREN [ expr { COMMA expr } ] RPAREN
            */
    Expressions callexpr() {
		curr_non_terminal = "callexpr";
		Expressions actual = new Expressions(curr_lineno());

        match(TokenConstants.LPAREN); 
        switch (LL(1).sym) {
            case TokenConstants.OBJECTID: case TokenConstants.INT_CONST: case  TokenConstants.STR_CONST: case TokenConstants.NOT:
            case TokenConstants.ISVOID: case TokenConstants.LET: case TokenConstants.IF: case TokenConstants.WHILE: 
            case TokenConstants.CASE: case TokenConstants.NEW: case TokenConstants.BOOL_CONST:
            case TokenConstants.LPAREN: case TokenConstants.LBRACE: case TokenConstants.NEG:
			Symbol e = expr();
			curr_non_terminal = "callexpr";
			if (e != null)
            	actual.appendElement((Expression) e.value);  
            while (LL(1).sym == TokenConstants.COMMA) { 
                match(TokenConstants.COMMA); 
				if (error) return actual; 
				e = expr();
				if (e != null)
	            	actual.appendElement((Expression) e.value);  
            }
            break;
			//default: 
				//syntax_error(LL(1)); error = true; return actual;
        }
        match(TokenConstants.RPAREN); 
  		return actual;
    }
}
