public class Token {
    public int tag;
    public String text;
    public Token(int t, String text) { tag=t; this.text=text; }
    public String toString() {
        String tname = Tag.tokenNames[tag];
        return "<'"+text+"',"+tname+">";
    }

    public static String unescape(String text) {   // una sequenza di caratterti \c significa c
        boolean escaped = true;
        int i;
        for (i = 0; i < text.length(); i++) {
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
