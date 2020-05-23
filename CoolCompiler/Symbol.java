public class Symbol {
	public int sym;
	public Object value;

    public Symbol(int symnum) {
        this.sym = symnum;
    }

    public Symbol(int symnum, Object value) {
        this.sym = symnum;
		this.value = value;
    }


    public String toString() {
		return "#"+sym;
    }
}    
