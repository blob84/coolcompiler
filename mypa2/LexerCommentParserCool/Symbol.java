public class Symbol {
    private Token token = null;
    private String name = null;

    public Symbol(String variable) {
        name = variable;
    }

    public Symbol(Token token) {
        this.token = token;
        name = token.text;
    }

    public String toString() {
        String s;

        if (token == null)
            s = name;
        else
            s = token.toString();            
        return s;
    }
}    
