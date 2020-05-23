import java_cup.runtime.Symbol;

public class Parser {
    Lexer lexer;     
    Token[] lookahead;
    LinkedTree parseTree;
    Position<Symbol> root;
    Position<Symbol> currentNode;
    private static int bufp = 0;
    private static int k;

    public Parser(Lexer lexer, int k) throws SyntaxError, Exception {
        parseTree = new LinkedTree<Symbol>();
        this.lexer = lexer;
        this.k = k;   // numero di token da guardare in avanti (1 + k-1), se LL(2) allora bisogna guardare, oltre al token corrente, 2-1=1 token in avanti.
        lookahead = new Token[k];
        for (int i = 0; i < k; i++) 
           advance(); 
    }
    
    public void match(int x) throws SyntaxError, Exception { System.out.println("token: "+LL(1));
        if ( LL(1).tag == x ) { 
            parseTree.insertChild(currentNode, new Symbol(LL(1)));
            advance();
        }
        else throw new SyntaxError("expecting "+Tag.getTokenName(x)+ "; found "+ Tag.getTokenName(LL(1).tag), lexer.linenum);
    }

    public void advance() throws SyntaxError, Exception {
         lookahead[bufp] = lexer.nextToken();  
         bufp = (bufp + 1) % k; 
    }

    public Token LL(int i) {
        return lookahead[(bufp+i-1) % k];
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

/* program ::= classP SEMI { classP SEMI } */
    public void program() throws SyntaxError, Exception {
        root = parseTree.addRoot(new Symbol("program")); // radice dell'albero
        //Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = root;     // attraversiamo questo sottoalbero

        classP(); match(Tag.SEMI); 
        while (LL(1).tag == Tag.CLASS) {
            classP(); match(Tag.SEMI); //match(Tag.EOF_TYPE);
        }

        //currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente
    } 

/* classP ::= CLASS TYPE [ INHERITS TYPE ] LBRACE { feature SEMI } RBRACE 
          */
    void classP() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("classP")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.CLASS); match(Tag.TYPE);
        if (LL(1).tag == Tag.INHERITS) { 
            match(Tag.INHERITS); match(Tag.TYPE);
        }
        match(Tag.LBRACE);
        while (LL(1).tag == Tag.ID) { 
            feature(); match(Tag.SEMI); 
        }
        match(Tag.RBRACE); 

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente
    }

/*feature ::= ID featureprime 
            */
    void feature() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("feature")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.ID);
        featureprime();

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente
    } 
    
/* featureprime ::= LPAR [ formal { COMMA formal } ] RPAR COLON TYPE LBRACE expr RBRACE
            | COLON TYPE [ ASSIGN expr ]
            */
    void featureprime() throws SyntaxError, Exception { 
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("featureprime")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        if (LL(1).tag == Tag.LPAR) {
            match(Tag.LPAR);
            if (LL(1).tag == Tag.ID) { 
                formal();
                while (LL(1).tag == Tag.COMMA) { 
                    match(Tag.COMMA); formal();
                }
            }
            match(Tag.RPAR); match(Tag.COLON); match(Tag.TYPE); match(Tag.LBRACE); expr(); match(Tag.RBRACE);
        }
        else if (LL(1).tag == Tag.COLON) {     
            match(Tag.COLON); match(Tag.TYPE);
            if (LL(1).tag == Tag.ASSIGN) {
                match(Tag.ASSIGN); expr();
            }
        }
        else throw new SyntaxError("expecting "+Tag.getTokenName(Tag.COLON)+", "+Tag.getTokenName(Tag.COLON)+"; found "+ Tag.getTokenName(LL(1).tag), lexer.linenum);

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente  
    }    

/* formal ::= ID COLON TYPE
            */
    void formal() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("formal")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.ID); match(Tag.COLON); match(Tag.TYPE);

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente  
    }

/* expr ::= ID ASSIGN expr  
            | notexpr */
    void expr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("expr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        
        // LL(2)
        //retractToken();// System.out.println("exprlooks="+lookahead); 
        if (LL(1).tag == Tag.ID && LL(2).tag == Tag.ASSIGN) {   
            match(Tag.ID);
            match(Tag.ASSIGN);
            expr();
        }    
        else {
            switch (LL(1).tag) {
                case Tag.ID: case Tag.NOT: case Tag.INTEGER: case Tag.STRING: case Tag.ISVOID: case Tag.LET: 
                case Tag.IF: case Tag.WHILE: case Tag.CASE: case Tag.NEW: case Tag.TRUE: 
                case Tag.FALSE: case Tag.LPAR: case Tag.LBRACE: case Tag.NEG: 
                    notexpr();
                    break;
                default:
                String tokensexp = Tag.getTokenName(Tag.ID)+", "+Tag.getTokenName(Tag.NOT)+", "+Tag.getTokenName(Tag.INTEGER)+", "+Tag.getTokenName(Tag.STRING)+", "+Tag.getTokenName(Tag.ISVOID)+", "+Tag.getTokenName(Tag.LET)
                                   +", "+Tag.getTokenName(Tag.IF)+", "+Tag.getTokenName(Tag.WHILE)+", "+Tag.getTokenName(Tag.CASE)+", "+Tag.getTokenName(Tag.NEW)+", "+Tag.getTokenName(Tag.TRUE)+", "+Tag.getTokenName(Tag.FALSE)
                                   +", "+Tag.getTokenName(Tag.LPAR)+", "+Tag.getTokenName(Tag.LBRACE)+", "+Tag.getTokenName(Tag.NEG); 
                throw new SyntaxError("expecting "+tokensexp+"; found "+ Tag.getTokenName(LL(1).tag), lexer.linenum); 
            }
        }

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente           
     }                      

/* notexpr ::= { NOT } relexpr
            */
    void notexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("notexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        while (LL(1).tag == Tag.NOT)
            match(Tag.NOT);
        relexpr();

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente           
    }

/* relexpr ::= addexpr { ( LESS | LESSEQ | EQUAL ) addexpr }
            */
    void relexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("relexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        addexpr();
        while (LL(1).tag == Tag.LESS || LL(1).tag == Tag.LESSEQ || LL(1).tag == Tag.EQUAL ) {
            if (LL(1).tag == Tag.LESS)
                match(Tag.LESS);
            else if (LL(1).tag == Tag.LESSEQ)
                match(Tag.LESSEQ);
            else
                match(Tag.EQUAL);
            addexpr();
        }
    
        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente               
    }

/* addexpr ::= mulexpr { ( PLUS | MINUS ) mulexpr }          
            */
    void addexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("addexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        mulexpr(); 
        while (LL(1).tag == Tag.PLUS || LL(1).tag == Tag.MINUS) {
            if (LL(1).tag == Tag.PLUS)
                match(Tag.PLUS);
            else if (LL(1).tag == Tag.MINUS)
                match(Tag.MINUS);
            mulexpr();
        }  
    
       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }

/* mulexpr ::= isvoidexpr { ( MULT | DIV ) isvoidexpr } 
            */
    void mulexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("mulexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        isvoidexpr();
        while (LL(1).tag == Tag.MULT || LL(1).tag == Tag.DIV) {
            if (LL(1).tag == Tag.MULT)
                match(Tag.MULT);
            else if (LL(1).tag == Tag.DIV)
                match(Tag.DIV);
            isvoidexpr();
        }    

       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }

/*isvoidexpr ::= { ISVOID } negexpr
            */
    void isvoidexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("isvoidexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        while (LL(1).tag == Tag.ISVOID) 
            match(Tag.ISVOID); 
        negexpr();

       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }

/* negexpr ::= { NEG } dispatchexpr
           */
    void negexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("negexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        while (LL(1).tag == Tag.NEG) 
            match(Tag.NEG); 
        dispatchexpr();

       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }

/* dispatchexpr ::= otherexpr { (atexpr | dotexpr) }
            */
    void dispatchexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("dispatchexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        otherexpr();
        while (LL(1).tag == Tag.AT || LL(1).tag == Tag.DOT) {
            if (LL(1).tag == Tag.AT) 
                atexpr();
            else if (LL(1).tag == Tag.DOT)
                dotexpr();
        }

       currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }
/* atexpr ::= AT TYPE dotexpr
            */    
    void atexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("atexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.AT); match(Tag.TYPE); dotexpr();

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }
       
/* dotexpr ::= DOT ID callexpr
            */     
    void dotexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("dotexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.DOT); match(Tag.ID); callexpr();

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                 
    }

/* otherexpr ::= LET ID COLON TYPE [ ASSIGN expr ] { COMMA ID COLON TYPE [ ASSIGN expr ] } IN expr  
            | IF expr THEN expr ELSE expr FI
            | WHILE expr LOOP expr POOL
            | CASE expr OF ID COLON TYPE ARROW expr SEMI { ID COLON TYPE ARROW expr SEMI } ESAC 
            | ID [ callexpr ]            
            | NEW TYPE
            | LBRACE expr SEMI { expr SEMI } RBRACE
            | LPAR expr RPAR
            | INTEGER | STRING | TRUE | FALSE
            */
    void otherexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("otherexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        if (LL(1).tag == Tag.LET) {
            match(Tag.LET); match(Tag.ID); match(Tag.COLON); match(Tag.TYPE);
            if (LL(1).tag == Tag.ASSIGN) {
                match(Tag.ASSIGN); expr();
            }
            while (LL(1).tag == Tag.COMMA) {
                match(Tag.COMMA); match(Tag.ID);
                match(Tag.COLON); match(Tag.TYPE);
                if (LL(1).tag == Tag.ASSIGN) {
                    match(Tag.ASSIGN); expr();
                }
            }
            match(Tag.IN); expr();
        }
        else if (LL(1).tag == Tag.IF) {
            match(Tag.IF); expr(); match(Tag.THEN); expr(); match(Tag.ELSE);
            expr(); match(Tag.FI);
        }
        else if (LL(1).tag == Tag.WHILE) {
            match(Tag.WHILE); expr(); match(Tag.LOOP);
            expr(); match(Tag.POOL);
        }
        else if (LL(1).tag == Tag.CASE) {
            match(Tag.CASE); expr(); match(Tag.OF); match(Tag.ID); match(Tag.COLON);
            match(Tag.TYPE); match(Tag.ARROW);
            expr(); match(Tag.SEMI);
            while (LL(1).tag == Tag.ID) {
                match(Tag.ID); match(Tag.COLON);
                match(Tag.TYPE); match(Tag.ARROW);
                expr(); match(Tag.SEMI);  
            }
            match(Tag.ESAC);
        } 
        else if (LL(1).tag == Tag.ID) {
            match(Tag.ID);
            if (LL(1).tag == Tag.LPAR)
                callexpr();
        }
        else if (LL(1).tag == Tag.NEW) {
            match(Tag.NEW); match(Tag.TYPE);
        }
        else if (LL(1).tag == Tag.LBRACE) {
            match(Tag.LBRACE); expr(); match(Tag.SEMI);
            while (LL(1).tag == Tag.ID || LL(1).tag == Tag.INTEGER || LL(1).tag == Tag.STRING || LL(1).tag == Tag.NOT ||
                 LL(1).tag == Tag.ISVOID || LL(1).tag == Tag.LET || LL(1).tag == Tag.IF || LL(1).tag == Tag.WHILE ||
                 LL(1).tag == Tag.CASE || LL(1).tag == Tag.NEW || LL(1).tag == Tag.TRUE || LL(1).tag == Tag.FALSE || 
                 LL(1).tag == Tag.LPAR || LL(1).tag == Tag.LBRACE || LL(1).tag == Tag.NEG ) {
                 expr(); match(Tag.SEMI);
            }
            match(Tag.RBRACE);
        }
        else if (LL(1).tag == Tag.LPAR) {
            match(Tag.LPAR);  expr(); match(Tag.RPAR);
        }
        else if (LL(1).tag == Tag.INTEGER)
            match(Tag.INTEGER);
        else if (LL(1).tag == Tag.STRING) 
            match(Tag.STRING); 
        else if (LL(1).tag == Tag.TRUE)
            match(Tag.TRUE);
        else if (LL(1).tag == Tag.FALSE)
            match(Tag.FALSE);
        else throw new SyntaxError("expecting "+Tag.getTokenName(Tag.LET)+", "+Tag.getTokenName(Tag.IF)+", "+Tag.getTokenName(Tag.WHILE)+", "+Tag.getTokenName(Tag.CASE)+", "
                                    +Tag.getTokenName(Tag.ID)+", "+Tag.getTokenName(Tag.LPAR)+", "+Tag.getTokenName(Tag.NEW)+", "+Tag.getTokenName(Tag.LBRACE)+", "+Tag.getTokenName(Tag.LPAR)+", "
                                    +Tag.getTokenName(Tag.INTEGER)+", "+Tag.getTokenName(Tag.STRING)+", "+Tag.getTokenName(Tag.TRUE)+", "+Tag.getTokenName(Tag.FALSE)+"; found "+ Tag.getTokenName(LL(1).tag), lexer.linenum); 

        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                  
    }

/* callexpr ::= LPAR [ expr { COMMA expr } ] RPAR
            */
    void callexpr() throws SyntaxError, Exception {
        Position<Symbol> r = parseTree.insertChild(currentNode, new Symbol("callexpr")); // creiamo un nuovo nodo per un non-terminale dell'albero (radice di un sottoalbero)
        Position<Symbol> save = currentNode;      //salviamo il nodo corrente
        currentNode = r;     // attraversiamo questo sottoalbero

        match(Tag.LPAR);
        switch (LL(1).tag) {
            case Tag.ID: case Tag.INTEGER: case  Tag.STRING: case Tag.NOT:
            case Tag.ISVOID: case Tag.LET: case Tag.IF: case Tag.WHILE: 
            case Tag.CASE: case Tag.NEW: case Tag.TRUE: case Tag.FALSE: 
            case Tag.LPAR: case Tag.LBRACE: case Tag.NEG:
            expr();
            while (LL(1).tag == Tag.COMMA) {
                match(Tag.COMMA); expr();
            }
            break;
/*            default:
            String tokensexp = Tag.getTokenName(Tag.ID)+", "+Tag.getTokenName(Tag.INTEGER)+", "+Tag.getTokenName(Tag.STRING)+", "+Tag.getTokenName(Tag.NOT)+", "+Tag.getTokenName(Tag.ISVOID)+", "+Tag.getTokenName(Tag.LET)+", "
                               +Tag.getTokenName(Tag.IF)+", "+Tag.getTokenName(Tag.WHILE)+", "+Tag.getTokenName(Tag.CASE)+", "+Tag.getTokenName(Tag.NEW)+", "+Tag.getTokenName(Tag.TRUE)+", "+Tag.getTokenName(Tag.FALSE)+", "
                               +Tag.getTokenName(Tag.LPAR)+", "+Tag.getTokenName(Tag.LBRACE)+", "+Tag.getTokenName(Tag.NEG);
            throw new SyntaxError("expecting "+tokensexp+"; found "+ Tag.getTokenName(LL(1).tag), lexer.linenum);  
        */}
        match(Tag.RPAR);
   
        currentNode = save; // usciti dal sottoalbero ripristiniamo il nodo precedente                  
    }

    public void printParseTree() {
        System.out.println(LinkedTree.print(parseTree, root, ""));         
    }
}
