//import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
//import java_cup.runtime.Symbol;

public class CoolCompiler {
    public static void main(String[] args) {
		args = Flags.handleFlags(args);
		
		FileReader file = null;
		try {
			file = new FileReader(Flags.in_filename); 
			//System.out.println("#name \"" + args[i] + "\"");

			CoolLexer lexer = new CoolLexer(file);	//scanning
			lexer.set_filename(Flags.in_filename);

			CoolParser parser = new CoolParser(lexer);	//parsing
			Object result = parser.parse().value;

			((Program)result).semant();	//semantic analisys
			//((Program)result).dump_with_types(System.out, 0); // print annotated abstract syntax tree

			PrintStream output = System.out;
			String filename = null;
			if (Flags.out_filename == null) {
			if (Flags.in_filename != null) {
				filename = Flags.in_filename.substring(0, 
								   Flags.in_filename.lastIndexOf('.'))
				+ ".s";
			}
			} else {
			filename = Flags.out_filename;
			}

			if (filename != null) {
			try {
				output = new PrintStream(new FileOutputStream(filename));
			} catch (IOException ex) {
				Utilities.fatalError("Cannot open output file " + filename);
			}
			}

			((Program)result).cgen(output);	//code generation
		} 
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
	}
}
