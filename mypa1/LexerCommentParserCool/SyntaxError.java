public class SyntaxError extends Exception {

    public SyntaxError(String msg, int nline) {
        super("line: "+nline + " " + msg);
    }
}
