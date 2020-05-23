import java.io.*;

public class Compiler {
    public static void main(String[] args) {
        File file = new File(args[0]);
        FileInputStream fis = null;
/*        File file1 = new File(args[1]);
        FileOutputStream fis2 = null;
*/

		try { fis = new FileInputStream(file); } //fis2 = new FileOutputStream(file1); }
        catch (FileNotFoundException e) {  e.printStackTrace(); }

        /*try {
            int c = 0;
            while ((c = fis.read()) != -1)
                System.out.print((char)c);
        }
        catch (IOException e) {  e.printStackTrace(); }
        */
/*        Lexer lexer = new Lexer(fis);
        Token t = null;
        try {
            t = lexer.nextToken();
        }
        catch (SyntaxError e) { System.out.println(e.getMessage()); }
        catch (Exception e) { e.printStackTrace(); }

        if (t != null && t.tag != Tag.EOF_TYPE) {
            System.out.println(t);
            while (t.tag != Tag.EOF_TYPE) {
                try {
                    t = lexer.nextToken();
                    if (t.tag != Tag.EOF_TYPE)
                        System.out.println(t);
                }
                catch (SyntaxError e) { System.out.println(e.getMessage()); }
                catch (Exception e) { e.printStackTrace(); }
           }
        }
 
*/       Lexer lexer = new Lexer(fis);
       Parser parser = null;
       try {
            parser = new Parser(lexer, 2);
            parser.program();
        }
        catch (SyntaxError e) { System.out.println(e.getMessage()); }
        catch (Exception e) { e.printStackTrace(); }
        finally {
            //try { fis2.write(e.getMessage().getBytes()); } catch (Exception ef) {}
           parser.printParseTree();
        }

   }
}
