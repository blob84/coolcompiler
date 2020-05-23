// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines list phylum Classes
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines list phylum Features
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines list phylum Formals
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;                                 
    public AbstractSymbol get_type() { return type; }           
    public Expression set_type(AbstractSymbol s) { type = s; return this; } 
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines list phylum Expressions
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void traverse(InformationGainer info);
    public abstract AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab);
}


/** Defines list phylum Cases
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'programc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class programc extends Program {
    protected Classes classes;
    /** Creates "programc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }
    public void traverse(InformationGainer info) {
		info.enterProgram(this);
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    	((Class_)e.nextElement()).traverse(info);
        }
		info.exitProgram(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    	((Class_)e.nextElement()).typeCheck(s, ctab);
        }
		return TreeConstants.No_type;
    }


    /** This method is the entry point to the semantic checker.  You will
        need to complete it in programming assignment 4.
	<p>
        Your checker should do the following two things:
	<ol>
	<li>Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
		/* ClassTable constructor may do some semantic analysis */
		ClassTable classTable = new ClassTable(classes);	//this class builds and checks that the inheritance graph is well formed

		if (classTable.errors()) {
			System.err.println("Compilation halted due to static semantic errors.");
			System.exit(1);
		}
		
		/* some semantic analysis code may go here */
		//InformationGainer mainFinder = new MainFinder(classTable);	//check for main class and main method
		//traverse(mainFinder);
		InformationGainer builderSymTab = new BuilderSymbolTable(classTable);
		traverse(builderSymTab);
		typeCheck(new SymbolTable(), classTable);		
		
		if (classTable.errors()) {
			System.err.println("Compilation halted due to static semantic errors.");
			System.exit(1);
		}
    }
}
	abstract class InformationGainer {
		public abstract void enterProgram(TreeNode currnode); 
		public abstract void enterClass(TreeNode currnode); 
		public abstract void enterMethod(TreeNode currnode);
		public abstract void enterAttr(TreeNode currnode);
		public abstract void enterFormalc(TreeNode currnode);
		public abstract void enterBranch(TreeNode currnode);
		public abstract void enterAssign(TreeNode currnode);
		public abstract void enterStatic_dispatch(TreeNode currnode);
		public abstract void enterDispatch(TreeNode currnode);
		public abstract void enterCond(TreeNode currnode);
		public abstract void enterLoop(TreeNode currnode);
		public abstract void enterTypcase(TreeNode currnode);
		public abstract void enterBlock(TreeNode currnode);
		public abstract void enterLet(TreeNode currnode);
		public abstract void enterPlus(TreeNode currnode);
		public abstract void enterSub(TreeNode currnode);
		public abstract void enterMul(TreeNode currnode);
		public abstract void enterDivide(TreeNode currnode);
		public abstract void enterNeg(TreeNode currnode);
		public abstract void enterLt(TreeNode currnode);
		public abstract void enterEq(TreeNode currnode);
		public abstract void enterLeq(TreeNode currnode);
		public abstract void enterComp(TreeNode currnode);
		public abstract void enterInt_const(TreeNode currnode);
		public abstract void enterBool_const(TreeNode currnode);
		public abstract void enterSring_const(TreeNode currnode);
		public abstract void enterNew_(TreeNode currnode);
		public abstract void enterIsvoid(TreeNode currnode);
		public abstract void enterNo_expr(TreeNode currnode);
		public abstract void enterObject(TreeNode currnode);

		public abstract void exitProgram(TreeNode currnode); 
		public abstract void exitClass(TreeNode currnode); 
		public abstract void exitMethod(TreeNode currnode);
		public abstract void exitAttr(TreeNode currnode);
		public abstract void exitFormalc(TreeNode currnode);
		public abstract void exitBranch(TreeNode currnode);
		public abstract void exitAssign(TreeNode currnode);
		public abstract void exitStatic_dispatch(TreeNode currnode);
		public abstract void exitDispatch(TreeNode currnode);
		public abstract void exitCond(TreeNode currnode);
		public abstract void exitLoop(TreeNode currnode);
		public abstract void exitTypcase(TreeNode currnode);
		public abstract void exitBlock(TreeNode currnode);
		public abstract void exitLet(TreeNode currnode);
		public abstract void exitPlus(TreeNode currnode);
		public abstract void exitSub(TreeNode currnode);
		public abstract void exitMul(TreeNode currnode);
		public abstract void exitDivide(TreeNode currnode);
		public abstract void exitNeg(TreeNode currnode);
		public abstract void exitLt(TreeNode currnode);
		public abstract void exitEq(TreeNode currnode);
		public abstract void exitLeq(TreeNode currnode);
		public abstract void exitComp(TreeNode currnode);
		public abstract void exitInt_const(TreeNode currnode);
		public abstract void exitBool_const(TreeNode currnode);
		public abstract void exitSring_const(TreeNode currnode);
		public abstract void exitNew_(TreeNode currnode);
		public abstract void exitIsvoid(TreeNode currnode);
		public abstract void exitNo_expr(TreeNode currnode);
		public abstract void exitObject(TreeNode currnode);
	}

	class BuilderSymbolTable extends InformationGainer {
		SymbolTable checkersymtab = new SymbolTable(); //symbol table used to check for semantic errors
		SymbolTable completesymtab = new SymbolTable(); //symbol table containing all scopes 
		ClassTable ctab;
		class_c classEnv;
		class_c cmain = null;	//Main class
		method mmain = null;	//method main in class Main 

		public BuilderSymbolTable(ClassTable ctab) {
			this.ctab = ctab;
		} 	
		public void enterProgram(TreeNode currnode) {
		}
		public void exitProgram(TreeNode currnode) {
			if (cmain == null)
				ctab.semantError().println("Class Main is not defined.");
			else if (mmain == null)
				ctab.semantError(ctab.classEnv).println("No 'main' method in class " + cmain.name);
		}
		public void enterClass(TreeNode currnode) {
			ctab.classEnv = (class_c) currnode;

			if (ctab.classEnv.name.equals(TreeConstants.Main))
				cmain = ctab.classEnv;	//class Main is found
			if (ctab.classEnv.parent.equals(TreeConstants.Int))
				ctab.semantError(ctab.classEnv).println("Class " + ctab.classEnv.name + " cannot inherit class Int.");
			else if (ctab.classEnv.parent.equals(TreeConstants.Str))
				ctab.semantError(ctab.classEnv).println("Class " + ctab.classEnv.name + " cannot inherit class String.");
			else if (ctab.classEnv.parent.equals(TreeConstants.Bool))
				ctab.semantError(ctab.classEnv).println("Class " + ctab.classEnv.name + " cannot inherit class Bool.");
			else if (ctab.classEnv.parent.equals(TreeConstants.SELF_TYPE))
				ctab.semantError(ctab.classEnv).println("Class " + ctab.classEnv.name + " cannot inherit SELF_TYPE.");
			else if (ctab.classEnv.name.equals(TreeConstants.SELF_TYPE))
				ctab.semantError(ctab.classEnv).println("Redefinition of basic class SELF_TYPE.");
			else if (ctab.findClass(ctab.classEnv.parent) == null)
				ctab.semantError(ctab.classEnv).println("Class " + ctab.classEnv.name + " inherits from an undefined class " + ctab.classEnv.parent + ".");

			checkersymtab.enterScope();
			completesymtab.enterScope();
		}
		public void exitClass(TreeNode currnode) {
			checkersymtab.exitScope();
		}
		public void enterMethod(TreeNode currnode) {
			method m = (method) currnode;
			Object o = checkersymtab.probe(m.name);

			if (o == null || !(o instanceof method)) {	//check that the method is not defined in this class and that is only a method name
				checkersymtab.addId(m.name, m);
				completesymtab.addId(m.name, m);
			}
			else ctab.semantError(ctab.classEnv).println("Method "+ m.name + " is multiply defined.");

			if (m.name.equals(TreeConstants.main_meth) && ctab.classEnv.name.equals(TreeConstants.Main))//if the current method is named main and it is in the current class named Main
				mmain = m; //method main in class Main is found

			checkersymtab.enterScope();
			completesymtab.enterScope();
		}	
		public void exitMethod(TreeNode currnode) {
			checkersymtab.exitScope();
		}	
		public void enterAttr(TreeNode currnode) {
			attr a = (attr) currnode;
			attr tmp;
 
			if (a.name.equals(TreeConstants.self))
				ctab.semantError(ctab.classEnv).println("'self' cannot be the name of an attribute.");
			else if (checkersymtab.probe(a.name) == null) {	//check that the attribute is not defined in this class
				class_c[] ancestors = ctab.getAncestors(ctab.classEnv.getName());
				for (class_c c : ancestors) {	//check if the attribute is already defined in the ancestor classes
					for (Enumeration e = c.features.getElements(); e.hasMoreElements(); ) {
	    				Feature f = (Feature)e.nextElement();
						if (f instanceof attr && ((attr)f).name.equals(a.name)) {
							ctab.semantError(ctab.classEnv).println("Attribute " + a.name + " is an attribute of an inherited class.");
							return;  

						}
        			}
				}
				checkersymtab.addId(a.name, a);
				completesymtab.addId(a.name, a);
			}
			else ctab.semantError(ctab.classEnv).println("Attribute " + a.name + " is multiply defined in class.");
			
		}	
		public void enterFormalc(TreeNode currnode) {
			formalc f = (formalc) currnode; 
			if (checkersymtab.probe(f.name) == null) {	//check that the formal parameter of a method is not defined in this method
				if (f.name.equals(TreeConstants.self)) {	
					ctab.semantError(ctab.classEnv).println("'self' cannot be the name of a formal parameter."); 
				}
				if (f.type_decl.equals(TreeConstants.SELF_TYPE)) {	
					ctab.semantError(ctab.classEnv).println("Formal parameter " + f.name + " cannot have type SELF_TYPE."); 
				}
				checkersymtab.addId(f.name, f);
				completesymtab.addId(f.name, f);
			}
			else ctab.semantError(ctab.classEnv).println("Formal parameter " + f.name + " is multiply defined.");
		}
		public void enterTypcase(TreeNode currnode) {
			typcase t = (typcase) currnode; 

			Set<AbstractSymbol> set = new HashSet<AbstractSymbol>();
			//check for duplicate branch type in case expression
	        for (Enumeration e = t.cases.getElements(); e.hasMoreElements();) { 
				branch b = (branch)e.nextElement();
				if (!set.add(b.type_decl)) 
					ctab.semantError(ctab.classEnv).println("Duplicate branch " + b.type_decl + " in case statement.");
			}
		}
		public void exitTypcase(TreeNode currnode) {
		}
		public void enterBranch(TreeNode currnode) {
			branch b = (branch) currnode;
			checkersymtab.enterScope();
			checkersymtab.addId(b.name, b.type_decl);
			completesymtab.addId(b.name, b.type_decl);
		}
		public void exitBranch(TreeNode currnode) {
			checkersymtab.exitScope();
		}
		public void enterAssign(TreeNode currnode) {
		/*	assign a = (assign) currnode;
			if (checkersymtab.lookup(a.name) == null) {	//check if the variable is defined in any scope
				boolean found = false;
		        for (Enumeration e = ((class_c) classEnv).features.getElements(); e.hasMoreElements();) { 
				//if there isn't a variable in all scopes, it is possible that it is defined after the current feature (a method), check all attributes in the current class
					Feature f = (Feature)e.nextElement();
				    if (f instanceof attr && ((attr)f).name.equals(a.name)) {
						found = true; break;				
					}
		        }
				if (!found)
					ctab.semantError(classEnv.getFilename(), currnode).println("Assignment to undeclared variable " + a.name + ".");
			}
		*/}
		public void enterStatic_dispatch(TreeNode currnode) {
		}
		public void enterDispatch(TreeNode currnode) {
		}
		public void enterCond(TreeNode currnode) {
		}
		public void enterLoop(TreeNode currnode) {
		}
		public void enterBlock(TreeNode currnode) {
		}
		public void enterNew_(TreeNode currnode) {
			new_ n = (new_) currnode;
			class_c  c = (class_c) ctab.findClass(n.type_name);
			if (c == null && !n.type_name.equals(TreeConstants.SELF_TYPE)) 		//undefined class
				ctab.semantError(ctab.classEnv).println("'new' used with undefined class " + n.type_name + ".");
		}
		public void enterObject(TreeNode currnode) {
/*			object o = (object) currnode;
			if (checkersymtab.lookup(o.name) == null && !o.name.equals(TreeConstants.self)) {	//check that the variable is defined in the current scope of the current class
				boolean found = false;
		        for (Enumeration e = ((class_c) classEnv).features.getElements(); e.hasMoreElements();) { 
				//if there isn't a variable in the outer most scope (current class), it is possible that it is defined after the current scope (a method), check all attributes in the current class
					Feature f = (Feature)e.nextElement();
				    if (f instanceof attr && ((attr) f).name.equals(o.name)) {
						found = true; break;				
					}
		        }
				if (!found)
					ctab.semantError(classEnv.getFilename(), currnode).println("Undeclared identifier " + o.name + ".");
			}
*/		}
		public void enterLet(TreeNode currnode) {
			checkersymtab.enterScope();
			completesymtab.enterScope();
			let l = (let) currnode;
			checkersymtab.addId(l.identifier, l);
			completesymtab.addId(l.identifier, l);
		}
		public void exitLet(TreeNode currnode) {
			checkersymtab.exitScope();
		}
		public  void enterPlus(TreeNode currnode) {}
		public  void enterSub(TreeNode currnode) {}
		public  void enterMul(TreeNode currnode) {}
		public  void enterDivide(TreeNode currnode) {}
		public  void enterNeg(TreeNode currnode) {}
		public  void enterLt(TreeNode currnode) {}
		public  void enterEq(TreeNode currnode) {}
		public  void enterLeq(TreeNode currnode) {}
		public  void enterComp(TreeNode currnode) {}
		public  void enterInt_const(TreeNode currnode) {}
		public  void enterBool_const(TreeNode currnode) {}
		public  void enterSring_const(TreeNode currnode) {}
		public  void enterIsvoid(TreeNode currnode) {}
		public  void enterNo_expr(TreeNode currnode) {}

		public  void exitAssign(TreeNode currnode) {}
		public  void exitStatic_dispatch(TreeNode currnode) {}
		public  void exitDispatch(TreeNode currnode) {}
		public  void exitCond(TreeNode currnode) {}
		public  void exitLoop(TreeNode currnode) {}
		public  void exitBlock(TreeNode currnode) {}
		public  void exitPlus(TreeNode currnode) {}
		public  void exitSub(TreeNode currnode) {}
		public  void exitMul(TreeNode currnode) {}
		public  void exitDivide(TreeNode currnode) {}
		public  void exitNeg(TreeNode currnode) {}
		public  void exitLt(TreeNode currnode) {}
		public  void exitEq(TreeNode currnode) {}
		public  void exitLeq(TreeNode currnode) {} 
		public  void exitComp(TreeNode currnode) {}
		public  void exitInt_const(TreeNode currnode) {}
		public  void exitBool_const(TreeNode currnode) {}
		public  void exitSring_const(TreeNode currnode) {}
		public  void exitNew_(TreeNode currnode) {}
		public  void exitIsvoid(TreeNode currnode) {}
		public  void exitNo_expr(TreeNode currnode) {}
		public  void exitObject(TreeNode currnode) {}	
		public  void exitAttr(TreeNode currnode) {}	
		public  void exitFormalc(TreeNode currnode) {}		
	}

/*____________________________________________________________________________________________________________________*/
/** Defines AST constructor 'class_c'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    /** Creates "class_c" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
    public AbstractSymbol getFilename() { return filename; }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }
    public void traverse(InformationGainer info) {
		info.enterClass(this);
        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
	    	((Feature)e.nextElement()).traverse(info);
        }
		info.exitClass(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		ctab.classEnv = this;
		s.enterScope();
        for (Enumeration e = features.getElements(); e.hasMoreElements(); ) {
	    	((Feature)e.nextElement()).typeCheck(s, ctab);
        }
		s.exitScope();
		return TreeConstants.No_type;
    }
}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
    /** Creates "method" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
	    ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
	expr.dump_with_types(out, n + 2);
    }
    public void traverse(InformationGainer info) {
		info.enterMethod(this);
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
	    	((Formal)e.nextElement()).traverse(info);
        }
		expr.traverse(info);
		info.exitMethod(this);
    }
/*
	M(C, f) = (T1 , . . . , Tn , T0)
	0C[SELF_TYPEC / self][T1 / x1 ] . . . [Tn / xn ], M, C |- e : T0'
	T0' ≤ SELF_TYPEC if T0 = SELF TYPE
	T0' ≤ T0 otherwise
	-----------------------------------------------------------------
	OC , M, C |- f(x1 : T1 , . . . , xn : Tn ) : T0 { e };
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T0 = return_type;

		method def = ctab.methodEnv(name); //find the method in parent classes

		if (def != null) { 		
			if (def.formals.getLength() != formals.getLength()) {
				ctab.semantError(ctab.classEnv).println("Incompatible number of formal parameters in redefined method " + name +".");
				return TreeConstants.No_type;
			}
		}
		s.addId(name, return_type);
		s.enterScope();
		AbstractSymbol[] T = new AbstractSymbol[formals.getLength()];
		int i = 0;
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) { //insert method parameters into the symbol table and store their type
	    	T[i++] = ((formalc)e.nextElement()).typeCheck(s, ctab);
        }
		if (def != null) {	//method definition found in an ancestor class, now check that the redefinition method has the same type (formal parameters type and return type)
			if (!return_type.equals(def.return_type)) {
				ctab.semantError(ctab.classEnv).println("In redefined method " + name + " , return type " + return_type + " is different from original return type " + def.return_type + ".");
				//return TreeConstants.No_type;
			}
			i = 0;
			AbstractSymbol Tdef;
		    for (Enumeration e = def.formals.getElements(); e.hasMoreElements(); ) { //check type of the parameters of the redefinited method 
				Tdef = ((formalc)e.nextElement()).type_decl;
				if (!T[i].equals(Tdef)) {
					ctab.semantError(ctab.classEnv).println("In redefined method " + name + " , parameter type " + T[i]  + " is different from original type " + Tdef + ".");
					break; //return TreeConstants.No_type;
				}
	 			i++;
		    }
		}				
		AbstractSymbol T0prime = expr.typeCheck(s, ctab); 
		if (T0.equals(TreeConstants.SELF_TYPE) && !T0prime.equals(TreeConstants.SELF_TYPE))//declared type return is SELF_TYPE and the type of the returned expression is not SELF_TYPE (objectid is not self)
			ctab.semantError(ctab.classEnv).println("Inferred return type " + T0prime + " of method " + name + " does not conform to declared return type "+ T0 + ".");
		else if (!ctab.sub(T0prime, T0))
			ctab.semantError((class_c) ctab.classEnv).println("Inferred return type " + T0prime + " of method " + name + " does not conform to declared return type "+ T0 + ".");
		s.exitScope();
		return TreeConstants.No_type;
    }
}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
    /** Creates "attr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
    }
    public void traverse(InformationGainer info) {
		info.enterAttr(this);
		init.traverse(info);
		info.exitAttr(this);
    }
/*
	OC(x) = T0
	OC[SELF_TYPEC / self], M, C |- e1 : T1
	T1 ≤ T0
	---------------------------------------
	OC , M, C |- x : T0 ← e1 ;

	OC(x) = T
	-------------------------------
	OC , M, C |- x : T ;
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T0 = type_decl;
		AbstractSymbol T1 = init.typeCheck(s, ctab);
		s.addId(name, type_decl);
		if (!T1.equals(TreeConstants.No_type)) { // if initialization
			if (!ctab.sub(T1, T0)) {	
				if (T1.equals(TreeConstants.self))
					ctab.semantError((class_c) ctab.classEnv).println("Inferred type SELF_TYPE of initialization of attribute " + name + " does not conform to declared type "+ T0 + ".");
				else ctab.semantError((class_c) ctab.classEnv).println("Inferred type " + T1 + " of initialization of attribute " + name + " does not conform to declared type "+ T0 + ".");
			}
		}
		return TreeConstants.No_type;
    }
}


/** Defines AST constructor 'formalc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }
    public void traverse(InformationGainer info) {
		info.enterFormalc(this);
		info.exitFormalc(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		s.addId(name, type_decl);
		return type_decl;
	}
}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
    /** Creates "branch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }
   public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	expr.dump_with_types(out, n + 2);
    }
    public void traverse(InformationGainer info) {
		info.enterBranch(this);
		expr.traverse(info);
		info.exitBranch(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		s.enterScope();
		s.addId(name, type_decl);
		AbstractSymbol T = expr.typeCheck(s, ctab);
		s.exitScope();
		return T;
    }
}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;
    /** Creates "assign" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
	expr.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterAssign(this);
		expr.traverse(info);
		info.exitAssign(this);
    }
/*
	O(Id) = T
	O, M, C |- e1 : T'
	T' ≤ T
	--------------------------
	O, M, C |- Id ← e1 : T'
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T = (AbstractSymbol) s.lookup(name); 
		if (T == null) {	//check if the variable is defined in any scope
	        for (Enumeration e = ctab.classEnv.features.getElements(); e.hasMoreElements();) { 
			//if there isn't a variable in all scopes, it is possible that it is defined after the current feature (a method), check all attributes in the current class
				Feature f = (Feature)e.nextElement();
			    if (f instanceof attr && ((attr)f).name.equals(name)) {
					T = ((attr)f).type_decl; break;				
				}
			}
			if (T == null) {
				class_c[] ancestors = ctab.getAncestors(ctab.classEnv.getName());
				for (class_c c : ancestors) {	//find object id in the ancestor classes
					for (Enumeration e = c.features.getElements(); e.hasMoreElements(); ) {
	    				Feature f = (Feature)e.nextElement();
						if (f instanceof attr && ((attr)f).name.equals(name)) {
							T = ((attr)f).type_decl; break;
						}
        			}
				}
			}
			if (T == null) {
				ctab.semantError(ctab.classEnv).println("Assignment to undeclared variable " + name + ".");
				T = TreeConstants.Object_;
			}
		}
		AbstractSymbol Tprime = expr.typeCheck(s, ctab);
		if (!ctab.sub(Tprime, T)) {
			ctab.semantError((class_c) ctab.classEnv).println("Type " + Tprime + " of assigned expression does not conform to declared type " + T + " of identifier " + name + ".");
			//this.set_type(TreeConstants.Object_);
			//return TreeConstants.Object_;
		}
		this.set_type(Tprime);
		return Tprime;
    }
}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "static_dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterStatic_dispatch(this);
		expr.traverse(info);
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
	    	((Expression)e.nextElement()).traverse(info);
        }
		info.exitStatic_dispatch(this);
    }
/*
	O, M, C |- e0 : T0
	O, M, C |- e1 : T1
	.
	.
	O, M, C |- en : Tn
	T0 ≤ T
	M (T, f ) = (T1', . . . , Tn', Tn+1')
	Ti ≤ Ti' 1 ≤ i ≤ n
	Tn+1 = T0 if Tn+1' = SELF_TYPE
	Tn+1 = Tn+1' otherwise
----------------------------------------------
	O, M, C |- e0.f (e1 , . . . , en ) : Tn+1
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol Ta[] = new AbstractSymbol[actual.getLength()];
		AbstractSymbol T0 = expr.typeCheck(s, ctab);
		AbstractSymbol T = type_name;
		if (T.equals(TreeConstants.SELF_TYPE))
			ctab.semantError(ctab.classEnv).println("Static dispatch to SELF_TYPE.");
		if (!ctab.sub(T0, T))
			ctab.semantError(ctab.classEnv).println("Expression type" + T0 + " does not conform to declared static dispatch type " + T + ".");
		//find the calling method in the respective class T
		method m = ctab.methodEnv(T, name);
		if (m == null)	//method not found
			ctab.semantError((class_c) ctab.classEnv).println("Static dispatch to undefined method " + name + ".");
		else { //method found
			if (m.formals.getLength() != actual.getLength())
				ctab.semantError(ctab.classEnv).println("Static dispatch method " + name + " called with wrong number of arguments");
			else { 
				int i = 0;	//check type of dynamic parameters
			    for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
					Ta[i++] = ((Expression)e.nextElement()).typeCheck(s, ctab);
				}
				i = 0;	//store static parameters
				formalc Tprime[] = new formalc[m.formals.getLength()];
			    for (Enumeration e = m.formals.getElements(); e.hasMoreElements(); ) 
					Tprime[i++] = ((formalc)e.nextElement());
				AbstractSymbol Tn1prime = m.return_type; 

				for (i = 0; i < Ta.length; i++)	//check that each dynamic type parameter is a subtype of the static type parameter
					if (!ctab.sub(Ta[i], Tprime[i].type_decl))
						ctab.semantError(ctab.classEnv).println("In call of method" + m.name +", type " + Tprime[i].type_decl + " of parameter " + Tprime[i].name + " does not conform to declared type " + Ta[i] + ".");
				AbstractSymbol Tn1 = null;
				if (Tn1prime.equals(TreeConstants.SELF_TYPE))
					Tn1 = T0;
				else Tn1 = Tn1prime;
				this.set_type(Tn1);
				return Tn1;
			}
		}
		this.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
    }
}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterDispatch(this);
		expr.traverse(info);
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
	    	((Expression)e.nextElement()).traverse(info);
        }
		info.exitDispatch(this);
    }
/*
	O, M, C |- e0 : T0
	O, M, C |- e1 : T1
	.
	.
	O, M, C |- en : Tn
	T0' = C if T0 = SELF_TYPEC
	T0' = T0 otherwise
	M (T0', f ) = (T1', . . . , Tn', Tn+1')
	Ti ≤ Ti' 1 ≤ i ≤ n
	Tn+1 = T0 if Tn+1' = SELF_TYPE
	Tn+1 = Tn+1' otherwise
----------------------------------------------
	O, M, C |- e0.f (e1 , . . . , en ) : Tn+1
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[actual.getLength()];
		AbstractSymbol T0 = expr.typeCheck(s, ctab);
		AbstractSymbol T0prime = null;

		if (T0.equals(TreeConstants.SELF_TYPE))
			T0prime = ((class_c) ctab.classEnv).getName();
		else T0prime = T0;
		//find the calling method in the respective class
		method m = ctab.methodEnv(T0prime, name);
		if (m == null)
			ctab.semantError(ctab.classEnv).println("Dispatch to undefined method " + name + ".");
		else {	//method found
			if (m.formals.getLength() != actual.getLength())
				ctab.semantError(ctab.classEnv).println("Method " + name + " called with wrong number of arguments");
			else {
				int i = 0;	//check type of dynamic parameters
			    for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
					T[i++] = ((Expression)e.nextElement()).typeCheck(s, ctab);
				}
				i = 0;	//store static parameters
				formalc Tprime[] = new formalc[m.formals.getLength()];
			    for (Enumeration e = m.formals.getElements(); e.hasMoreElements(); ) 
					Tprime[i++] = ((formalc)e.nextElement());
				AbstractSymbol Tn1prime = m.return_type; 

				for (i = 0; i < T.length; i++)	//check that each dynamic type parameter is a subtype of the static type parameter
					if (!ctab.sub(T[i], Tprime[i].type_decl))
						ctab.semantError((class_c) ctab.classEnv).println("In call of method " + m.name +", type " + T[i] + " of parameter " + Tprime[i].name + " does not conform to declared type " + Tprime[i].type_decl + ".");
				AbstractSymbol Tn1 = null;
				if (Tn1prime.equals(TreeConstants.SELF_TYPE))
					Tn1 = T0;
				else Tn1 = Tn1prime;
				this.set_type(Tn1);
				return Tn1;
			}
		}
		this.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
    }
}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
    /** Creates "cond" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
	pred.dump_with_types(out, n + 2);
	then_exp.dump_with_types(out, n + 2);
	else_exp.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterCond(this);
		pred.traverse(info);
 		then_exp.traverse(info);
		else_exp.traverse(info);
		info.exitCond(this);
    }
/*	assign type rule
	O, M, C |- e1 : Bool
	O, M, C |- e2 : T2
	O, M, C |- e3 : T3
	------------------------------------------------
	O, M, C |- if e1 then e2 else e3 fi : lub(T2, T3)
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		AbstractSymbol T0 = pred.typeCheck(s, ctab);
		if (!T0.equals(TreeConstants.Bool))
			ctab.semantError((class_c) ctab.classEnv).println("Predicate of 'if' does not have type Bool.");
		T[0] = then_exp.typeCheck(s, ctab);
		T[1] = else_exp.typeCheck(s, ctab);
		AbstractSymbol lub = ctab.lub(T[0], T[1]).getName();
		this.set_type(lub);
		return lub;
    }
}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
    /** Creates "loop" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
	pred.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterLoop(this);
		pred.traverse(info);
 		body.traverse(info);
		info.exitLoop(this);
    }
/*
	O, M, C |- e1 : Bool
	O, M, C |- e2 : T2
	------------------------------------------
	O, M, C |- while e1 loop e2 pool : Object
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T0 = pred.typeCheck(s, ctab);
		if (!T0.equals(TreeConstants.Bool))
			ctab.semantError((class_c) ctab.classEnv).println("Predicate of 'while' does not have type Bool.");
		AbstractSymbol T1 = body.typeCheck(s, ctab);
		this.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
    }
}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
    /** Creates "typcase" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
	expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterTypcase(this);
		expr.traverse(info);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    	((Case)e.nextElement()).traverse(info);
        }
		info.exitTypcase(this);
    }
/*
	O, M, C |- e0 : T0
	O[T1 /x1 ], M, C |- e1 : T1'
	.
	.
	O[Tn /xn ], M, C |- en : Tn'
---------------------------------------------------------------------------
	O, M, C |- case e0 of x1 : T1 ⇒ e1 ; . . . xn : Tn ⇒ en ; esac : lub(T1'... Tn')
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T0 = expr.typeCheck(s, ctab);
		AbstractSymbol T[] = new AbstractSymbol[cases.getLength()];
		int i = 0;
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    	T[i++] = ((branch)e.nextElement()).typeCheck(s, ctab);
        }
		AbstractSymbol lub = ((class_c) ctab.lub(T)).getName();// System.out.println("curr class: " + ctab.classEnv.name + "- lub: "+lub + " - T0: "+T0);
		this.set_type(lub);
		return lub;
    }

}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    protected Expressions body;
    /** Creates "block" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterBlock(this);
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    	((Expression)e.nextElement()).traverse(info);
        }
		info.exitBlock(this);
    }
/*
	O, M, C |- e1 : T1
	O, M, C |- e2 : T2
	.
	.
	O, M, C |- en : Tn
	----------------------------------------
	O, M, C |- { e1 ; e2 ; . . . en ; } : Tn
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[body.getLength()];
		int i = 0;
		for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    	T[i++] = ((Expression)e.nextElement()).typeCheck(s, ctab);
        }
		this.set_type(T[body.getLength()-1]);
		return T[body.getLength()-1];
    }
}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
    /** Creates "let" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
	dump_AbstractSymbol(out, n + 2, identifier);
	dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterLet(this);
		init.traverse(info);
		body.traverse(info);
		info.exitLet(this);
    }
/*
	T0' = SELF_TYPEC if T0 = SELF_TYPE
	T0' = T0 otherwise
	O, M, C |- e1 : T1
	T1 ≤ T0'
	O[T0'/x], M, C |- e2 : T2
	--------------------------------------
	O, M, C |- let x : T0 ← e1 in e2 : T2
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T0 = type_decl;
		AbstractSymbol T1 = null;
		AbstractSymbol T2 = null;
		AbstractSymbol T0prime;

		if (identifier.equals(TreeConstants.self)) {
			ctab.semantError(ctab.classEnv).println("'self' cannot be bound in a 'let' expression.");
			T0 = TreeConstants.Object_;
		}
		T0prime = T0;
		T1 = init.typeCheck(s, ctab);
		s.enterScope();
		s.addId(identifier, T0);
		if (!T1.equals(TreeConstants.No_type)) { //if initialization
			if (!ctab.sub(T1, T0prime))
				ctab.semantError(ctab.classEnv).println("Inferred type" + T1 + "  of initialization of " + identifier + " does not conform to identifier's declared type " + T0prime + ".");
		}
		T2 = body.typeCheck(s, ctab);
		s.exitScope();
		this.set_type(T2);
		return T2;
    }
}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "plus" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterPlus(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitPlus(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {∗, +, −, /}
	--------------------------
	O, M, C |- e1 op e2 : Int
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " + " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;
    }
}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "sub" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterSub(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitSub(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {∗, +, −, /}
	--------------------------
	O, M, C |- e1 op e2 : Int
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " - " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;
    }
}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "mul" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterMul(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitMul(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {∗, +, −, /}
	--------------------------
	O, M, C |- e1 op e2 : Int
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " * " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;
    }
}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "divide" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterDivide(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitDivide(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {∗, +, −, /}
	--------------------------
	O, M, C |- e1 op e2 : Int
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " / " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;
    }
}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    protected Expression e1;
    /** Creates "neg" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterNeg(this);
		e1.traverse(info);
		info.exitNeg(this);
    }
/*
	O, M, C |- e1 : Int
	--------------------
	O, M, C |- ~e1 : Int
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T = e1.typeCheck(s, ctab);
		if (!T.equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("Argument of '~' has type" + T + " instead of Int.");
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;    
	}
}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "lt" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterLt(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitLt(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {<, ≤}
	---------------------------
	O, M, C |- e1 op e2 : Bool
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " < " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "eq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterEq(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitEq(this);
    }
/*
	O, M, C |- e1 : T1
	O, M, C |- e2 : T2
	T1 ∈ {Int, String, Bool} V T2 ∈ {Int, String, Bool} ⇒ T1 = T2
	--------------------------------------------------------------	
	O, M, C |- e1 = e2 : Bool
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T1 = e1.typeCheck(s, ctab);
		AbstractSymbol T2 = e2.typeCheck(s, ctab);

		if ((T1.equals(TreeConstants.Int) && !T2.equals(TreeConstants.Int)) || (!T1.equals(TreeConstants.Int) && T2.equals(TreeConstants.Int)) || 
			(T1.equals(TreeConstants.Str) && !T2.equals(TreeConstants.Str)) || (!T1.equals(TreeConstants.Str) && T2.equals(TreeConstants.Str)) ||
			(T1.equals(TreeConstants.Bool) && !T2.equals(TreeConstants.Bool)) || (!T1.equals(TreeConstants.Bool) && T2.equals(TreeConstants.Bool))) {
			ctab.semantError((class_c) ctab.classEnv).println("Illegal comparison with a basic type.");
			//this.set_type(TreeConstants.Object_);
			//return TreeConstants.Object_;
		}	
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "leq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterLeq(this);
		e1.traverse(info);
		e2.traverse(info);
		info.exitLeq(this);
    }
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {<, ≤}
	---------------------------
	O, M, C |- e1 op e2 : Bool
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T[] = new AbstractSymbol[2];
		T[0] = e1.typeCheck(s, ctab);
		T[1] = e2.typeCheck(s, ctab);
		if (!T[0].equals(TreeConstants.Int) || !T[1].equals(TreeConstants.Int)) {
			ctab.semantError((class_c) ctab.classEnv).println("non-Int arguments: "+ T[0] + " <= " + T[1]);
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    protected Expression e1;
    /** Creates "comp" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterComp(this);
		e1.traverse(info);
		info.exitComp(this);
    }
/*
	O, M, C |- e1 : Bool
	----------------------
	O, M, C |- ¬e1 : Bool
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol T = e1.typeCheck(s, ctab);
		if (!T.equals(TreeConstants.Bool)) {
			ctab.semantError((class_c) ctab.classEnv).println("Argument of 'not' has type " + T + " instead of Bool.");
			this.set_type(TreeConstants.Object_);
			return TreeConstants.Object_;
		}
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "int_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
	dump_AbstractSymbol(out, n + 2, token);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterInt_const(this);
		info.exitInt_const(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		this.set_type(TreeConstants.Int);
		return TreeConstants.Int;
    }
}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    protected Boolean val;
    /** Creates "bool_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
	dump_Boolean(out, n + 2, val);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterBool_const(this);
		info.exitBool_const(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "string_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
	out.print(Utilities.pad(n + 2) + "\"");
	Utilities.printEscapedString(out, token.getString());
	out.println("\"");
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterSring_const(this);
		info.exitSring_const(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		this.set_type(TreeConstants.Str);
		return TreeConstants.Str;
    }
}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    protected AbstractSymbol type_name;
    /** Creates "new_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
	dump_AbstractSymbol(out, n + 2, type_name);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterNew_(this);
		info.exitNew_(this);
    }
/*
	T' = SELF_TYPEC if T = SELF TYPE
	T' = T otherwise
	------------------------------------
	O, M, C |- new T : Tprime
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		AbstractSymbol Tprime = null;
		AbstractSymbol T = type_name;
		if (T.equals(TreeConstants.SELF_TYPE))
			Tprime = TreeConstants.SELF_TYPE;
		else Tprime = T;
		this.set_type(Tprime);
		return Tprime;
    }
}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    protected Expression e1;
    /** Creates "isvoid" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterIsvoid(this);
		e1.traverse(info);
		info.exitIsvoid(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		e1.typeCheck(s, ctab);
		this.set_type(TreeConstants.Bool);
		return TreeConstants.Bool;
    }
}


/** Defines AST constructor 'no_expr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterNo_expr(this);
		info.exitNo_expr(this);
    }
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		this.set_type(TreeConstants.No_type);
		return TreeConstants.No_type;
    }
}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    protected AbstractSymbol name;
    /** Creates "object" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
	dump_AbstractSymbol(out, n + 2, name);
	dump_type(out, n);
    }
    public void traverse(InformationGainer info) {
		info.enterObject(this);
		info.exitObject(this);
    }
/*
	O(Id) = T
	-----------------
	O, M, C |- Id : T
*/
    public AbstractSymbol typeCheck(SymbolTable s, ClassTable ctab) {
		if (name.equals(TreeConstants.self)) {
			this.set_type(TreeConstants.SELF_TYPE);
			return TreeConstants.SELF_TYPE;
		}
		AbstractSymbol T = (AbstractSymbol) s.lookup(name);
		if (T == null) { 		
	        for (Enumeration e = ctab.classEnv.features.getElements(); e.hasMoreElements();) { 
			//if there isn't a variable in the symbol table, it is possible that is defined after the current feature (method), check all attributes in the current class
				Feature f = (Feature)e.nextElement();
			    if (f instanceof attr && ((attr) f).name.equals(name)) {
					T = ((attr) f).type_decl; break;				
				}
	        }
			if (T == null) {
				class_c[] ancestors = ctab.getAncestors(ctab.classEnv.getName());
				for (class_c c : ancestors) {	//find object id in the ancestor classes
					for (Enumeration e = c.features.getElements(); e.hasMoreElements(); ) {
	    				Feature f = (Feature)e.nextElement();
						if (f instanceof attr && ((attr)f).name.equals(name)) {
							T = ((attr)f).type_decl; break;
						}
        			}
				}
				if (T == null) {	//no object id found
					ctab.semantError(ctab.classEnv).println("Undeclared identifier " + name + ".");
					this.set_type(TreeConstants.Object_);
					return TreeConstants.Object_;
				}
			}
		}		
		this.set_type(T);
		return T;
    }
}


