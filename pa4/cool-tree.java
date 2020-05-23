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
import java.util.Comparator;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();
    public abstract void cgen(PrintStream s);

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract AbstractSymbol getName();
    public abstract AbstractSymbol getParent();
    public abstract AbstractSymbol getFilename();
    public abstract Features getFeatures();

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
    public abstract void code(PrintStream s, CgenClassTable cgenctab);
	public abstract int NT();

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


/** Defines AST constructor 'program'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class program extends Program {
    public Classes classes;
    /** Creates "program" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public program(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new program(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "program\n");
        classes.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    ((Class_)e.nextElement()).dump_with_types(out, n + 1);
        }
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
	ClassTable classTable = new ClassTable(classes);
	
	/* some semantic analysis code may go here */

	if (classTable.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	}
    }
    /** This method is the entry point to the code generator.  All of the work
      * of the code generator takes place within CgenClassTable constructor.
      * @param s the output stream 
      * @see CgenClassTable
      * */
    public void cgen(PrintStream s) 
    {
        // spim wants comments to start with '#'
        s.print("# start of generated code\n");

	CgenClassTable codegen_classtable = new CgenClassTable(classes, s);

	s.print("\n# end of generated code\n");
    }

}


/** Defines AST constructor 'class_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_ extends Class_ {
    public AbstractSymbol name;
    public AbstractSymbol parent;
    public Features features;
    public AbstractSymbol filename;
    /** Creates "class_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
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
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }
    public AbstractSymbol getFilename() { return filename; }
    public Features getFeatures()       { return features; }

}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    public AbstractSymbol name;
    public Formals formals;
    public AbstractSymbol return_type;
    public Expression expr;
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

}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression init;
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

}


/** Defines AST constructor 'formal'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formal extends Formal {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    /** Creates "formal" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formal(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formal(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formal\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression expr;
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

}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    public AbstractSymbol name;
    public Expression expr;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream
		o, S1 , E |- e1 : v1 , S2
		E(Id) = l1
		S3 = S2 [v1 / l1]
		so, S1 , E |- Id ← e1 : v1 , S3
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		//CgenSupport.emitStore(CgenSupport.SELF, CgenSupport.ACC, s); //	sw	$s0 $a0, $s0 contain the pointer of the current object

		expr.code(s, cgenctab);	//the value of this expression is stored in $a0
		
		Object[] obj = (Object[]) cgenctab.env.lookup(name);	//get the variable (global or local) and the offset
		String dest;
		if (obj[0] instanceof attr)	//it is an attribute
			dest = CgenSupport.SELF;
		else 
			dest = CgenSupport.FP;

		CgenSupport.emitStore(CgenSupport.ACC, (Integer) obj[1], dest, s); //sw	$a0 offset($s0) or sw $a0 offset($fp), store the new value in the location of the attribute or the local variable
		if (Flags.cgen_Memmgr == Flags.GC_GENGC && obj[0] instanceof attr) { //notify the collector of the assignment to the attribute
			CgenSupport.emitAddiu(CgenSupport.A1, CgenSupport.SELF, 4*((Integer) obj[1]), s);
			CgenSupport.emitJal("_GenGC_Assign", s);
		}
    }

	public int NT() {
		return expr.NT();
	}
}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol type_name;
    public AbstractSymbol name;
    public Expressions actual;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : v1 , S2
		so, S2 , E |- e2 : v2 , S3
		.
		.
		so, Sn , E |- en : vn , Sn+1
		so, Sn+1 , E |- e0 : v0 , Sn+2
		v0 = X(a1 = la1 , . . . , am = lam )
		implementation(T, f) = (x1 , . . . , xn , en+1 )
		lxi = newloc(Sn+2 ), for i = 1 . . . n and each lxi is distinct
		Sn+3 = Sn+2 [v1 / lx1 , . . . , vn / lxn ]
		v0 , Sn+3 , [a1 : la1 , . . . , am : lam , x1 : lx1 , . . . , xn : lxn ] |- en+1 : vn+1 , Sn+4
		so, S1 , E |- e0 @T.f (e1 , . . . , en ) : vn+1 , Sn+4

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		for (Enumeration e = actual.getElements(); e.hasMoreElements();) { 
	    	((Expression)e.nextElement()).code(s, cgenctab); //generate code for each parameter
			CgenSupport.emitPush(CgenSupport.ACC, s);	//generate code to add parameters onto the stack
		}

		expr.code(s, cgenctab); //generate code for the caller expression, $a0 contains the caller object

		int callinglabel = cgenctab.label++;
		CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, callinglabel, s); //if the caller is not void goto label
		CgenSupport.emitLoadAddress(CgenSupport.ACC, CgenSupport.STRCONST_PREFIX+AbstractTable.stringtable.lookup(cgenctab.currClass.getFilename().toString()).index, s); //load file name into a register
		CgenSupport.emitLoadImm(CgenSupport.T1, this.getLineNumber(), s); //load line number into a register		
		CgenSupport.emitJal("_dispatch_abort", s); //it is void
		
		CgenSupport.emitLabelDef(callinglabel, s); //contains the calling to the method
		//la Class_dispTab, get the dispatch table pointer, all methods of acenstor classes have the same offset in the dispatch table				
		CgenSupport.emitLoadAddress(CgenSupport.T1, type_name.toString()+CgenSupport.DISPTAB_SUFFIX, s);

		AbstractSymbol type = type_name;
		if (!cgenctab.impl.containsKey(type.toString()+"."+name.toString())) {
			for (Object a : cgenctab.getAncestors(type)) {
				CgenNode a1 = (CgenNode) a; 
				if (cgenctab.impl.containsKey(a1.getName().toString()+"."+name.toString()))
					type = a1.getName();
			}
		}
		//System.out.println("type: "+type+ " method: "+name+ " 	"+ cgenctab.impl.get(type.toString()+"."+name.toString()));
		int offset = (Integer) cgenctab.impl.get(type.toString()+"."+name.toString()); //get offset of the method 
		CgenSupport.emitLoad(CgenSupport.T1, offset, CgenSupport.T1, s); //put in a register the pointer of the required method
		CgenSupport.emitJalr(CgenSupport.T1, s);	//jump to method
    }

	public int NT() {
		int nt1 = expr.NT();
		int nt = 0;
		
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) 
			nt =  java.lang.Math.max(((Expression)e.nextElement()).NT(), nt);

		return 	java.lang.Math.max(nt1, nt);
	}
}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol name;
    public Expressions actual;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : v1 , S2
		so, S2 , E |- e2 : v2 , S3
		.
		.
		so, Sn , E |- en : vn , Sn+1
		so, Sn+1 , E |- e0 : v0 , Sn+2
		v0 = X(a1 = la1 , . . . , am = lam )
		implementation(X, f) = (x1 , . . . , xn , en+1 )
		lxi = newloc(Sn+2 ), for i = 1 . . . n and each lxi is distinct
		Sn+3 = Sn+2 [v1 / lx1 , . . . , vn / lxn ]
		v0 , Sn+3 , [a1 : la1 , . . . , am : lam , x1 : lx1 , . . . , xn : lxn ] |- en+1 : vn+1 , Sn+4
		so, S1 , E |- e0.f(e1 , . . . , en) : vn+1 , Sn+4
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		for (Enumeration e = actual.getElements(); e.hasMoreElements();) { 
	    	((Expression)e.nextElement()).code(s, cgenctab); //generate code for eveluate parameter expression
			CgenSupport.emitPush(CgenSupport.ACC, s);	//generate code to add parameters onto the stack
			//cgenctab.nextp += 1;
		}

		AbstractSymbol type = expr.get_type(); //get the class where is defined the method
		if (type == null || (type != null && (type.equals(TreeConstants.SELF_TYPE) || type.equals(TreeConstants.No_type) ))) { //the caller has no expression or is self
			type = cgenctab.currClass.getName();
			if (type == null || (type != null && type.equals(TreeConstants.No_type) )) //the caller has no expression
				CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s); //put self into accumulator
			else	//it is self
				expr.code(s, cgenctab); //generate code for the self expression, $a0 contains the caller object
		}
		else	
			expr.code(s, cgenctab); //generate code for the caller expression, $a0 contains the caller object
		//if this method is not in the current class, look if there is in one of all ancestor classes
		//System.out.println("type: "+type+ " method: "+name+ " 	"+ cgenctab.impl.get(type.toString()+"."+name.toString()));
		if (!cgenctab.impl.containsKey(type.toString()+"."+name.toString())) {
			for (Object a : cgenctab.getAncestors(type)) {
				CgenNode a1 = (CgenNode) a; 
				if (cgenctab.impl.containsKey(a1.getName().toString()+"."+name.toString()))
					type = a1.getName();
			}
		}
		//System.out.println("type: "+type+ " method: "+name+ " 	"+ cgenctab.impl.get(type.toString()+"."+name.toString()));
/*		if (type == null || (type != null && (type.equals(TreeConstants.SELF_TYPE) || type.equals(TreeConstants.No_type) )) { //the caller has no expression
			//if this method is not in the current class, look if there is in one of all ancestor classes
			type = cgenctab.currClass.getName();	
			if (!cgenctab.impl.containsKey(type.toString()+"."+name.toString())) {
				for (Object a : cgenctab.getAncestors(cgenctab.currClass.getName())) {
					CgenNode a1 = (CgenNode) a;
					if (cgenctab.impl.containsKey(a1.getName().toString()+"."+name.toString()))
						type = a1.getName();
				}
			}
			CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s); //put self into accumulator
		}
		else {	//if this method is not defined into the class of the caller expression, look at their ancestors
			//if (type != null) {
				if (type.equals(TreeConstants.SELF_TYPE))	//the caller is self 
					type = cgenctab.currClass.getName(); //find the method from the current class if there is not here
				if (!cgenctab.impl.containsKey(type.toString()+"."+name.toString()))	
					for (Object a : cgenctab.getAncestors(type)) {
						CgenNode a1 = (CgenNode) a;
						if (cgenctab.impl.containsKey(a1.getName().toString()+"."+name.toString())) 
							type = a1.getName(); 
					}
			//}
			expr.code(s, cgenctab); //generate code for the caller expression, $a0 contains the caller object
		}
*/		//System.out.println("type: "+type+ " method: "+name+ " 	"+ cgenctab.impl.get(type.toString()+"."+name.toString()));
		int callinglabel = cgenctab.label++;
		CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, callinglabel, s); //if the caller is not void goto label
		CgenSupport.emitLoadAddress(CgenSupport.ACC, CgenSupport.STRCONST_PREFIX+AbstractTable.stringtable.lookup(cgenctab.currClass.getFilename().toString()).index, s); //load file name into a register
		CgenSupport.emitLoadImm(CgenSupport.T1, this.getLineNumber(), s); //load line number into a register		
		CgenSupport.emitJal("_dispatch_abort", s); //it is void

		CgenSupport.emitLabelDef(callinglabel, s); //contains the calling to the method		
		CgenSupport.emitLoad(CgenSupport.T1, 2, CgenSupport.ACC, s); //lw $t1 8($a0), get dispatch pointer of the object in the accumulator
		int offset = (Integer) cgenctab.impl.get(type.toString()+"."+name.toString()); //get offset of the method 
		CgenSupport.emitLoad(CgenSupport.T1, offset, CgenSupport.T1, s); //put in a register the pointer of the required method
		CgenSupport.emitJalr(CgenSupport.T1, s);	//jump to method
		//System.out.println("curr: "+type+ "  offset: "+offset);
    }

	public int NT() {
		int nt1 = expr.NT();
		int nt = 0;
		
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) 
			nt =  java.lang.Math.max(((Expression)e.nextElement()).NT(), nt);

		return 	java.lang.Math.max(nt1, nt);
	}
}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    public Expression pred;
    public Expression then_exp;
    public Expression else_exp;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Bool(true), S2
		so, S2 , E |- e2 : v2 , S3
		so, S1 , E |- if e1 then e2 else e3 fi : v2 , S3

		so, S1 , E |- e1 : Bool(false), S2
		so, S2 , E |- e3 : v3 , S3
		so, S1 , E |- if e1 then e2 else e3 fi : v3 , S3
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int falselabel = cgenctab.label++;
		int endiflabel;
		pred.code(s, cgenctab); //evaluate the boolean expression

		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s); //get the boolean value

		CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.ZERO, falselabel, s); //if false go to label

		then_exp.code(s, cgenctab); //it is true
		endiflabel = cgenctab.label++;
		CgenSupport.emitBranch(endiflabel, s); //go to end if label

		CgenSupport.emitLabelDef(falselabel, s); //false label
		else_exp.code(s, cgenctab);	//it is false
		CgenSupport.emitLabelDef(endiflabel, s); //definition of end if label
    }

	public int NT() {
		int nt = java.lang.Math.max(pred.NT(), then_exp.NT());
		return java.lang.Math.max(else_exp.NT(), nt);
	}
}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    public Expression pred;
    public Expression body;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E 	|- e1 : Bool(true), S2
		so, S2 , E	|- e2 : v2 , S3
		so, S3 , E	|- while e1 loop e2 pool : void, S4
		so, S1 , E	|- while e1 loop e2 pool : void, S4

		so, S1 , E |- e 1 : Bool(false), S2
		so, S1 , E |- while e 1 loop e 2 pool : void, S2

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int looplabel = cgenctab.label++;
		int endlooplabel;

		CgenSupport.emitLabelDef(looplabel, s); //loop label, Ln		
		pred.code(s, cgenctab); //evaluate the boolean expression

		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s); //get the boolean value
		endlooplabel = cgenctab.label++;
		CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.ZERO, endlooplabel, s); //if false go to exit loop label, Ln+1

		body.code(s, cgenctab); //it is true
		CgenSupport.emitBranch(looplabel, s); //go to loop label, Ln
		CgenSupport.emitLabelDef(endlooplabel, s); //exit loop label, Ln+1
		CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s); //while loop always returns void		
    }

	public int NT() {
		return 	java.lang.Math.max(pred.NT(), body.NT());
	}
}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    public Expression expr;
    public Cases cases;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e0 : v0 , S2
		v0 = X(. . .)
		Ti = closest ancestor of X in {T1 , . . . , Tn }
		l0 = newloc(S2)
		S3 = S2 [v0 /l0]
		E0 = E[l0 /Idi]
		so, S3 , E0 |- ei : v1 , S4
		so, S1 , E |- case e0 of Id1 : T1 ⇒ e1 ; . . . ; Idn : Tn ⇒ en ; esac : v1 , S4
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		expr.code(s, cgenctab); //generate code for the first expression, $a0 contains the value
	
		int endcaselabel = cgenctab.label++;
		int nextbranchlabel = cgenctab.label++;

		CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nextbranchlabel, s); //check that the expression is not void

		CgenSupport.emitLoadAddress(CgenSupport.ACC, CgenSupport.STRCONST_PREFIX+AbstractTable.stringtable.lookup(cgenctab.currClass.getFilename().toString()).index, s); //load file name into a register
		CgenSupport.emitLoadImm(CgenSupport.T1, this.getLineNumber(), s); //load line number into a register		
		CgenSupport.emitJal("_case_abort2", s); //if it is void error

		branch[] bs = new branch[cases.getLength()];
		int i = 0; 
		for (Enumeration e = cases.getElements(); e.hasMoreElements();) 
			bs[i++] = (branch) e.nextElement();
		java.util.Arrays.sort(bs, new Comparator<branch>() {//sort the array of branches in descending order, from the type with max class tag to the minimum
				public int compare(branch b1, branch b2) {
				    return (Integer) cgenctab.tagOfClass.get(b2.type_decl) - (Integer) cgenctab.tagOfClass.get(b1.type_decl); 
				}
			});

		i = 0;
		//Enumeration e = cases.getElements();	//get the first branch
    	branch b = bs[i++]; //(branch) e.nextElement();
		cgenctab.env.enterScope();
		Object[] obj = {b, new Integer(cgenctab.nextp)};	//contains the branch and the offset
		cgenctab.env.addId(((branch) b).name, obj);

		CgenSupport.emitLabelDef(nextbranchlabel, s); //create label for the first branch
		CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.ACC, s); //get the class tag of the type (class) of the first expression

		//AbstractSymbol type = expr.get_type(); //get the type of the first expression
		//System.out.println("type: "+type+ " 	tag:"+cgenctab.tagOfClass.get(type));		
		CgenNode node = (CgenNode) cgenctab.probe(b.type_decl); //get the node type (class) of the branch expression in the graph
		
		int firstclassTag = (Integer) cgenctab.tagOfClass.get(b.type_decl); //the class tag of the type (class) of the expression of the frist branch
		int lastclassTag = (Integer) firstclassTag + cgenctab.count(node)-1; //the class tag of the type (class) of the last descendant of the first expression type
	
		nextbranchlabel = cgenctab.label++;	//label for next branch expression
		CgenSupport.emitBlti(CgenSupport.T2, firstclassTag, nextbranchlabel, s); //$t2 < first class tag,  go to next branch
		CgenSupport.emitBgti(CgenSupport.T2, lastclassTag, nextbranchlabel, s);  //$t2 > last descendant class tag, go to next branch
		CgenSupport.emitStore(CgenSupport.ACC, cgenctab.nextp, CgenSupport.FP, s); //assign the value of the first expression to the variable of this branch
		cgenctab.nextp += 1;	//next free slot

		b.expr.code(s, cgenctab); //generate code for the branch expression
		CgenSupport.emitBranch(endcaselabel, s); //go to the next expression, exit case expression
		cgenctab.nextp -= 1;
		cgenctab.env.exitScope();

		for (; i < bs.length; i++) { //e.hasMoreElements();) {
	    	b = (branch) bs[i]; //e.nextElement(); 				System.out.println(b.type_decl);		
			cgenctab.env.enterScope();
			obj[0]= b; obj[1] = new Integer(cgenctab.nextp);	//contains the branch and the offset
			cgenctab.env.addId(((branch) b).name, obj);

			CgenSupport.emitLabelDef(nextbranchlabel, s); //create label for the next branch

			//type = b.expr.get_type();
			node = (CgenNode) cgenctab.probe(b.type_decl); 		
		
			firstclassTag = (Integer) cgenctab.tagOfClass.get(b.type_decl); //get the class tag of the type (class) of the expression of the frist branch
			lastclassTag = (Integer) firstclassTag + cgenctab.count(node)-1; //the class tag of the type (class) of the last descendant of the first expression type
			//System.out.println("Type: "+ type + "  lasttag: " + lastclassTag);
			nextbranchlabel = cgenctab.label++;
			CgenSupport.emitBlti(CgenSupport.T2, firstclassTag, nextbranchlabel, s); //$t2 < first class tag, go to next branch  
			CgenSupport.emitBgti(CgenSupport.T2, lastclassTag, nextbranchlabel, s);  //$t2 > last descendant class tag, go to next branch
			CgenSupport.emitStore(CgenSupport.ACC, cgenctab.nextp, CgenSupport.FP, s); //assign the value of the first expression to the variable of this branch
			cgenctab.nextp += 1;	//next free slot

			b.expr.code(s, cgenctab); //generate code for the branch expression
			CgenSupport.emitBranch(endcaselabel, s); //go to the next expression, exit case
			cgenctab.nextp -= 1;
			cgenctab.env.exitScope();
		}
		int abortlabel = nextbranchlabel;
		CgenSupport.emitLabelDef(abortlabel, s); //case abort label
		CgenSupport.emitJal("_case_abort", s);	//no branch is chosen

		CgenSupport.emitLabelDef(endcaselabel, s); //label for next expression, exit case
		//cgenctab.nextp -= cgenctab.nextp+cases.getLength();	//free memory

    }
	public int NT() {
		int nt = 0;

		for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    	branch b = (branch) e.nextElement();
			nt = java.lang.Math.max(b.expr.NT(), nt);			
		}
		return 	java.lang.Math.max(expr.NT(), 1+nt);
	}

}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    public Expressions body;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : v1 , S2
		so, S2 , E |- e2 : v2 , S3
		.
		.
		so, Sn , E |- en : vn , Sn+1
		so, S1 , E |- { e1 ; e2 ; . . . ; en ; } : vn , Sn+1

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
        for (Enumeration e = body.getElements(); e.hasMoreElements();) 
	    	((Expression)e.nextElement()).code(s, cgenctab);
    }

	public int NT() {
		int nt = 0;
        for (Enumeration e = body.getElements(); e.hasMoreElements();) 
	  	  nt = java.lang.Math.max(((Expression)e.nextElement()).NT(), nt);
		return nt;
	}

}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    public AbstractSymbol identifier;
    public AbstractSymbol type_decl;
    public Expression init;
    public Expression body;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : v1 , S2
		l1 = newloc(S2)
		S3 = S2 [v1 /l1]
		E' = E[l1 /Id]
		so, S3 , E' |- e2 : v2 , S4
		so, S1 , E |- let Id : T1 ← e1 in e2 : v2 , S4

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		//create default initializtation for any type of variable
		if (type_decl.equals(TreeConstants.Int)) //int_const(index), index of integer 0 
			CgenSupport.emitLoadInt(CgenSupport.ACC, (IntSymbol) AbstractTable.inttable.lookup("0"), s);  
		else if (type_decl.equals(TreeConstants.Str))	//str_const(index), index of empty string
			CgenSupport.emitLoadString(CgenSupport.ACC, (StringSymbol) AbstractTable.stringtable.lookup(""), s);
		else if (type_decl.equals(TreeConstants.Bool)) //bool_const0	
			CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
		else //it is not a built-in object
			CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s);	//default initialization is void (0)

		CgenSupport.emitStore(CgenSupport.ACC, cgenctab.nextp, CgenSupport.FP, s); //store the new value in a new location with default initialization		
		
		if (init.get_type() != null && !init.get_type().equals(TreeConstants.No_type)) { //initialization
			init.code(s, cgenctab);	//generate code for init expression
			CgenSupport.emitStore(CgenSupport.ACC, cgenctab.nextp, CgenSupport.FP, s); //store the new value in a new location with custom initialization		
		}

		cgenctab.env.enterScope();	//increment the environment with a new variable
		Object[] obj = {this, new Integer(cgenctab.nextp)};	//contains the let expression and the offset of the new local variable
		cgenctab.env.addId(identifier, obj);

		cgenctab.nextp += 1; //next free slot memory

		body.code(s, cgenctab); //generate code for the body expression, $a0 contains the value

		cgenctab.nextp -= 1; 
		cgenctab.env.exitScope();

    }

	public int NT() {
	  	  return java.lang.Math.max(init.NT(), 1+body.NT());
	}

}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {∗, +, −, /}
		v1 = Int(i1 op i2)
		so, S1 , E |- e1 op e2 : v1 , S3

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;	//System.out.println(cgenctab.nextp);
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push integer object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab);
		CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $a1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value in $a1 and put it in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value in $a0  and put it in $t2, the last integer object
		CgenSupport.emitAdd(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);	
		CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s); //store integer value into integer object in $a0
		cgenctab.nextp -= 1; 
		//System.out.println(cgenctab.nextp);
    }
	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {∗, +, −, /}
		v1 = Int(i1 op i2)
		so, S1 , E |- e1 op e2 : v1 , S3

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push integer object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab); 		CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $t1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value in $a0, the last integer object
		CgenSupport.emitSub(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);	
 		CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s); //store integer value into integer object in $a0
		cgenctab.nextp -= 1; 
    }
	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {∗, +, −, /}
		v1 = Int(i1 op i2)
		so, S1 , E |- e1 op e2 : v1 , S3
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push integer object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab);		CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $t1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value in $a0, the last integer object
		CgenSupport.emitMul(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);	
		CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s); //store integer value into integer object in $a0
		cgenctab.nextp -= 1; 
    }

	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {∗, +, −, /}
		v1 = Int(i1 op i2)
		so, S1 , E |- e1 op e2 : v1 , S3

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//store integer object into the frame pointer
		cgenctab.nextp += 1;
		e2.code(s, cgenctab); 		CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $t1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value in $a0, the last integer object
		CgenSupport.emitDiv(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);	
		CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s); //store integer value into integer object in $a0
		cgenctab.nextp -= 1; 
    }

	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    public Expression e1;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		v1 = Int(-i1)
		so, S1 , E |- ̃e1 : v1 , S2

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		e1.code(s, cgenctab);
		CgenSupport.emitJal("Object.copy", s); //create a new copy to not modify the original, the accumulator will contains the copy
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);	//load the integer value into $t1
		CgenSupport.emitNeg(CgenSupport.T1, CgenSupport.T1, s);	//negate the integer
		CgenSupport.emitStore(CgenSupport.T1, 3, CgenSupport.ACC, s);	//store the integer value into the accumulator $a0
    }
	public int NT() {
	  	  return e1.NT();
	}

}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {≤, <}
		v1 = Bool(true), if i1 op i2
		v1 = Bool(false), otherwise
		so, S1 , E |- e1 op e2 : v1, S3
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;
		int label = cgenctab.label++;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push integer object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab); 		//CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $t1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value of the first expression in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value of the second expression in $a0, the last integer object
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);	//load true bool value into the accumulator
		CgenSupport.emitBlt(CgenSupport.T1, CgenSupport.T2, label, s);	//check that $t1 < $t2 and if true goto label
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s); //load false bool value into the accumulator

		CgenSupport.emitLabelDef(label, s);	//label to next expression
		cgenctab.nextp -= 1; 
    }
	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		In e1 = e2 , first e1 is evaluated and then e2 is evaluated. The two objects are compared for equality
		by first comparing their pointers (addresses). If they are the same, the objects are equal. The value
		void is not equal to any object except itself. If the two objects are of type String, Bool, or Int, their
		respective contents are compared.
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;	//System.out.println(cgenctab.nextp);
		int label = cgenctab.label++;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab); 		//CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.T1, thisnt, CgenSupport.FP, s); //get the object in $t1 from $fp
		//CgenSupport.emitMove(CgenSupport.T1, CgenSupport.T3, s); //put the object of the first expression in $t1
		CgenSupport.emitMove(CgenSupport.T2, CgenSupport.ACC, s); //put the object of the second expression in $t2
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);	//load true bool value into the accumulator
		CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.T2, label, s);	//first test their address, check that $t1 = $t2 and if true goto label
		CgenSupport.emitLoadBool(CgenSupport.A1, BoolConst.falsebool, s); //load false bool value into $a1
		CgenSupport.emitJal("equality_test", s);	//if false then call global predefined label to check for equality
		CgenSupport.emitLabelDef(label, s);	//label to next expression
		cgenctab.nextp -= 1; //System.out.println(cgenctab.nextp);
    }
	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    public Expression e1;
    public Expression e2;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Int(i1), S2
		so, S2 , E |- e2 : Int(i2), S3
		op ∈ {≤, <}
		v1 = Bool(true), if i1 op i2
		v1 = Bool(false), otherwise
		so, S1 , E |- e1 op e2 : v1, S3
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int thisnt = cgenctab.nextp;
		int label = cgenctab.label++;
		e1.code(s, cgenctab);
		CgenSupport.emitStore(CgenSupport.ACC, thisnt, CgenSupport.FP, s);	//push integer object into the stack
		cgenctab.nextp += 1;
		e2.code(s, cgenctab); 		//CgenSupport.emitJal("Object.copy", s); 
		CgenSupport.emitLoad(CgenSupport.A1, thisnt, CgenSupport.FP, s); //get integer object in $t1 from $fp
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.A1, s); //get integer value of the first expression in $t1
		CgenSupport.emitLoad(CgenSupport.T2, 3, CgenSupport.ACC, s); //get integer value of the second expression in $a0, the last integer object
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);	//load true bool value into the accumulator
		CgenSupport.emitBleq(CgenSupport.T1, CgenSupport.T2, label, s);	//check that $t1 < $t2 and if true goto label
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s); //load false bool value into the accumulator

		CgenSupport.emitLabelDef(label, s);	//label to next expression
		cgenctab.nextp -= 1; 
    }
	public int NT() {
	  	  return java.lang.Math.max(e1.NT(), 1+e2.NT());
	}

}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    public Expression e1;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : Bool(b), S2
		v1 = Bool(¬b)
		so, S1 , E |- not e1 : v1, S2
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		int label = cgenctab.label++;
		e1.code(s, cgenctab);
		CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);	//get the boolean value from the accumulator
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s); //load true booleean object into the acumulator
		CgenSupport.emitBeqz(CgenSupport.T1, label, s);	//check that $t1 = 0 and if true goto label
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s); //if $t1 = 0 load false boolean object into the accumulator

		CgenSupport.emitLabelDef(label, s);	//label to next expression
    }
	
	public int NT() {
	  	  return e1.NT();
	}
}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    public AbstractSymbol token;
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
	CgenSupport.emitLoadInt(CgenSupport.ACC,
                                (IntSymbol)AbstractTable.inttable.lookup(token.getString()), s);
    }
	public int NT() {
	  	  return 0;
	}
}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    public Boolean val;
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
    }
	public int NT() {
	  	  return 0;
	}
}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    public AbstractSymbol token;
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
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
	CgenSupport.emitLoadString(CgenSupport.ACC,
                                   (StringSymbol)AbstractTable.stringtable.lookup(token.getString()), s);
    }
	public int NT() {
	  	  return 0;
	}
}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    public AbstractSymbol type_name;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
	SELF_TYPE
	la	$t1 class_objTab
	lw	$t2 0($s0)
	sll	$t2 $t2 3
	addu	$t1 $t1 $t2
	move	$s1 $t1
	lw	$a0 0($t1)
	jal	Object.copy
	lw	$t1 4($s1)
	jalr		$t1
    * */	

    public void code(PrintStream s, CgenClassTable cgenctab) {
		if (type_name.equals(TreeConstants.SELF_TYPE)) {
			CgenSupport.emitLoadAddress(CgenSupport.T1, "class_objTab", s);	//load the table of all objects into $t1
			CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.SELF, s);	//load class tag into $t2
			CgenSupport.emitSll(CgenSupport.T2, CgenSupport.T2, 3, s);	//calculate the offset of the self object into the objects table
			CgenSupport.emitAddu(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);	//get the class_protObj address
			CgenSupport.emitMove(CgenSupport.T3, CgenSupport.T1, s); //copy the address into $a1
			CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.T1, s);	//load the address of class_protObj in $t3
			CgenSupport.emitJal("Object.copy", s); //create copy of the new object
			CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.T3, s); //load the class_init address
			CgenSupport.emitJalr(CgenSupport.T1, s); //go to class_init label
		}
		else {
			CgenSupport.emitLoadAddress(CgenSupport.ACC, type_name+CgenSupport.PROTOBJ_SUFFIX, s); //la $a0 Type_protObj
			CgenSupport.emitJal("Object.copy", s);	//jal Object.copy
			CgenSupport.emitJal(type_name.toString()+CgenSupport.CLASSINIT_SUFFIX, s);	//jal Type_init
		}
    }
	public int NT() {
		if (type_name.equals(TreeConstants.SELF_TYPE)) 
	  		return 1;
		return 0;
	}

}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    public Expression e1;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		so, S1 , E |- e1 : void, S2
		so, S1 , E |- isvoid e1 : Bool(true), S2

		so, S1 , E |- e1 : X(. . .), S2
		so, S1 , E |- isvoid e1 : Bool(false), S2

      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		e1.code(s, cgenctab);
		CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);	//get the boolean value from the accumulator
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s); //load true booleean object into the acumulator
		CgenSupport.emitBeqz(CgenSupport.T1, cgenctab.label, s);	//check that $t1 = 0 and if true goto label
		CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s); //if $t1 != 0 load false boolean object into the accumulator

		CgenSupport.emitLabelDef(cgenctab.label++, s);	//label to next expression
    }
	public int NT() {
	  	  return e1.NT();
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
    }

	public int NT() {
	  	  return 0;
	}
}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    public AbstractSymbol name;
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
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 

		E(Id) = l
		S(l) = v
		so, S, E |- Id : v, S
      * */
    public void code(PrintStream s, CgenClassTable cgenctab) {
		Object[] obj = (Object[]) cgenctab.env.lookup(name);
		//obj[0] is the expression and obj[1] is the offset
		if (obj != null && obj[0] instanceof attr)	//it is an attribute
			CgenSupport.emitLoad(CgenSupport.ACC, (Integer) obj[1], CgenSupport.SELF, s);
		else if (obj == null && name.equals(TreeConstants.self))	//it is self object
			CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
		else //it is a temporary variable 
			CgenSupport.emitLoad(CgenSupport.ACC, (Integer) obj[1], CgenSupport.FP, s);
    }

	public int NT() {
	  	  return 0;
	}
}


