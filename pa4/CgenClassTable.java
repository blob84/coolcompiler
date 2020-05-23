/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;


/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;

	public static SymbolTable env = new SymbolTable(); //contains variable associated to an offset	
	public static Map impl = new HashMap();	//contains class name and method name associated to an offset
	public static Map tagOfClass = new HashMap();	//contains class name mapped to a class tag
	public static class_ currClass; //current class
	public static int label = 0;	//next label 
	public static int nextp = 0; //next free slot in memory
	public static ArrayList<CgenNode> classes; //a list of all nodes taken from the inheritance graph


    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class = 
	    new class_(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class = 
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class = 
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class = 
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	stringclasstag = 5 /* Change to your String class tag here */;
	intclasstag =    3 /* Change to your Int class tag here */;
	boolclasstag =   4 /* Change to your Bool class tag here */;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

	classes = new ArrayList<CgenNode>();
	traverse(root(), classes); //build a list of all nodes
	ClassMapping();	//map any class name to a tag (integer)

	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	if (Flags.cgen_debug) System.out.println("coding class name table");
	codeClassNameTab();

	if (Flags.cgen_debug) System.out.println("coding class objects table");
	codeClassObjTab();

	//                 Add your code to emit
	//                   - prototype objects
	//                   - class_nameTab
	//                   - dispatch tables

	if (Flags.cgen_debug) System.out.println("coding dispatch table");
	codeDispatchTable();

	if (Flags.cgen_debug) System.out.println("coding prototype objects");
	codePrototypeObjects();

	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	//                   - the class methods
	//                   - etc...

	if (Flags.cgen_debug) System.out.println("coding objects initialization");
	codeObjectInit();

	if (Flags.cgen_debug) System.out.println("coding methods definition");
	codeMethodDef();
	}
	//fill the map between class and tag
	private void ClassMapping() {
		ArrayList<CgenNode> classesWithoutBuiltinObj = new ArrayList<CgenNode>(); int tmp = 0;
        for (CgenNode C : classes) {	 
			if (C.getName().equals(TreeConstants.Object_)) {
				tagOfClass.put(C.getName(), 0);  tmp = 0; }
			else if (C.getName().equals(TreeConstants.IO)) { 
				tagOfClass.put(C.getName(), 1); tmp = 1; }
			else if (C.getName().equals(TreeConstants.Str)) {
				tagOfClass.put(C.getName(), stringclasstag); tmp = 5; }
			else if (C.getName().equals(TreeConstants.Int)) {
				tagOfClass.put(C.getName(), intclasstag); tmp = 3; }
			else if (C.getName().equals(TreeConstants.Bool)) {
				tagOfClass.put(C.getName(), boolclasstag); tmp = 4; }
			else if (C.getName().equals(TreeConstants.Main)) {
				tagOfClass.put(C.getName(), 2); tmp = 2; }
			else { 
				classesWithoutBuiltinObj.add(C);
				continue;
			}
			//System.out.println("C: "+C.getName()+ " ctag: "+tmp);
		}
		int classTag = 6;
       for (class_ C : classesWithoutBuiltinObj) {	//System.out.println("C: "+C.getName()+ " ctag: "+classTag);
			tagOfClass.put(C.getName(), classTag++); }	
	}

	public void codePrototypeObjects() {
		//create prototype objects for built-in objects
		AbstractSymbol[] classesNameTab = new AbstractSymbol[tagOfClass.size()]; //class names ordered as the respective tag in ascending order
		for (Object o : tagOfClass.keySet()) {
			AbstractSymbol cname = (AbstractSymbol) o;
			int i = (Integer) tagOfClass.get(cname);
			classesNameTab[i] = cname;
		}
		int classTag = 0;
		for (AbstractSymbol cname : classesNameTab) { //System.out.println("C: "+cname+ " ctag: "+classTag);
			str.print(CgenSupport.WORD); //Garbage Collector Tag
			str.print(-1);
			str.print("\n");

			CgenSupport.emitProtObjRef(cname, str); //prototype object
			str.print(CgenSupport.LABEL);

			str.print(CgenSupport.WORD);	//Class tag
			str.print(classTag++);
			str.print("\n");

			CgenNode node = (CgenNode) probe(cname);
			//need two stack for iteration over the ancestor classes
			Stack stack = new Stack(); //one for calculate the number of attributes
			Stack stack1 = new Stack();	//one for generate code for default initialization
			stack.add(node);
			stack1.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
				node = node.getParentNd(); 
				stack.add(node);
				stack1.add(node);
			}
			
			int numAttr = 0;	
			while (!stack.isEmpty()) {	//count the number of attributes of the current class and ancestor classes to use it for compute the size of the object
				node  = (CgenNode) stack.pop();
				for (Enumeration e2 = node.getFeatures().getElements(); e2.hasMoreElements(); ) {	
					Feature f =  (Feature) e2.nextElement();
					if (f instanceof attr) 
						numAttr++;
				}
			}
			
			str.print(CgenSupport.WORD); //Object size (int 32-bit words)
			str.print(numAttr+3);
			str.print("\n");
			
			str.print(CgenSupport.WORD); //Dispatch pointer
			CgenSupport.emitDispTableRef(cname, str);
			str.print("\n");
						
			while (!stack1.isEmpty()) {	//insert the attributes in the following order AN < AN-1 < ... < A2 < A1
				node  = (CgenNode) stack1.pop();
				for (Enumeration e3 = node.getFeatures().getElements(); e3.hasMoreElements(); ) {	
					Feature f =  (Feature) e3.nextElement();
					if (f instanceof attr) { 
						attr a = (attr) f;	
						str.print(CgenSupport.WORD); //attributes list, default initialization
						if (a.type_decl.equals(TreeConstants.Int)) //int_const(index), index of integer 0 
							str.print(CgenSupport.INTCONST_PREFIX + AbstractTable.inttable.lookup("0").index);  
						else if (a.type_decl.equals(TreeConstants.Str))	//str_const(index), index of empty string
							str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup("").index);
						else if (a.type_decl.equals(TreeConstants.Bool)) //bool_const0	
							str.print(CgenSupport.BOOLCONST_PREFIX + "0");
						else //it is not a built-in object
							str.print("0");
						str.print("\n");
					}
				}
			}
		}


		/*ArrayList<CgenNode> classesWithoutBuiltinObj = new ArrayList<CgenNode>(); int tmp = 0;
        for (CgenNode C : classes) {	 
			if (C.getName().equals(TreeConstants.Object_)) {
				tagOfClass.put(C.getName(), 0);  tmp = 0; }
			else if (C.getName().equals(TreeConstants.IO)) { 
				tagOfClass.put(C.getName(), 1); tmp = 1; }
			else if (C.getName().equals(TreeConstants.Str)) {
				tagOfClass.put(C.getName(), stringclasstag); tmp = 5; }
			else if (C.getName().equals(TreeConstants.Int)) {
				tagOfClass.put(C.getName(), intclasstag); tmp = 3; }
			else if (C.getName().equals(TreeConstants.Bool)) {
				tagOfClass.put(C.getName(), boolclasstag); tmp = 4; }
			else if (C.getName().equals(TreeConstants.Main)) {
				tagOfClass.put(C.getName(), 2); tmp = 2; }
			else { 
				classesWithoutBuiltinObj.add(C);
				continue;
			}
			//System.out.println("C: "+C.getName()+ "- tag: "+ tmp);
			str.print(CgenSupport.WORD); //Garbage Collector Tag
			str.print(-1);
			str.print("\n");

			CgenSupport.emitProtObjRef(C.getName(), str); //prototype object
			str.print(CgenSupport.LABEL);

			str.print(CgenSupport.WORD);	//Class tag
			if (C.getName().equals(TreeConstants.Object_)) 
				str.print(0);
			else if (C.getName().equals(TreeConstants.Str))
				str.print(stringclasstag);
			else if (C.getName().equals(TreeConstants.Int))
				str.print(intclasstag);
			else if (C.getName().equals(TreeConstants.Bool))
				str.print(boolclasstag);
			else if (C.getName().equals(TreeConstants.Main))
				str.print(2);
			else if (C.getName().equals(TreeConstants.IO))
				str.print(1);
			str.print("\n");

			CgenNode node = (CgenNode) probe(C.getName());
			//need two stack for iteration over the ancestor classes
			Stack stack = new Stack(); //one for calculate the number of attributes
			Stack stack1 = new Stack();	//one for generate code for default initialization
			stack.add(node);
			stack1.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
				node = node.getParentNd(); 
				stack.add(node);
				stack1.add(node);
			}
			
			int numAttr = 0;	
			while (!stack.isEmpty()) {	//count the number of attributes of the current class and ancestor classes to use it for compute the size of the object
				node  = (CgenNode) stack.pop();
				for (Enumeration e2 = node.getFeatures().getElements(); e2.hasMoreElements(); ) {	
					Feature f =  (Feature) e2.nextElement();
					if (f instanceof attr) 
						numAttr++;
				}
			}
			
			str.print(CgenSupport.WORD); //Object size (int 32-bit words)
			str.print(numAttr+3);
			str.print("\n");
			
			str.print(CgenSupport.WORD); //Dispatch pointer
			CgenSupport.emitDispTableRef(C.getName(), str);
			str.print("\n");
						
			while (!stack1.isEmpty()) {	//insert the attributes in the following order AN < AN-1 < ... < A2 < A1
				node  = (CgenNode) stack1.pop();
				for (Enumeration e3 = node.getFeatures().getElements(); e3.hasMoreElements(); ) {	
					Feature f =  (Feature) e3.nextElement();
					if (f instanceof attr) { 
						attr a = (attr) f;	
						str.print(CgenSupport.WORD); //attributes list, default initialization
						if (a.type_decl.equals(TreeConstants.Int)) //int_const(index), index of integer 0 
							str.print(CgenSupport.INTCONST_PREFIX + AbstractTable.inttable.lookup("0").index);  
						else if (a.type_decl.equals(TreeConstants.Str))	//str_const(index), index of empty string
							str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup("").index);
						else if (a.type_decl.equals(TreeConstants.Bool)) //bool_const0	
							str.print(CgenSupport.BOOLCONST_PREFIX + "0");
						else //it is not a built-in object
							str.print("0");
						str.print("\n");
					}
				}
			}
        }
		int classTag = 6;	//create prototyper objects
        for (class_ C : classesWithoutBuiltinObj) {	//System.out.println("C: "+C.getName()+ " ctag: "+classTag);
			tagOfClass.put(C.getName(), classTag);	

			str.print(CgenSupport.WORD); //Garbage Collector Tag
			str.print(-1);
			str.print("\n");

			CgenSupport.emitProtObjRef(C.getName(), str); //prototype object
			str.print(CgenSupport.LABEL);

			str.print(CgenSupport.WORD);	//Class tag
			str.print(classTag++);
			str.print("\n");

			CgenNode node = (CgenNode) probe(C.getName());
			//need two stack for iteration over the ancestor classes
			Stack stack = new Stack(); //one for calculate the number of attributes
			Stack stack1 = new Stack();	//one for generate code for default initialization
			stack.add(node);
			stack1.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
				node = node.getParentNd(); 
				stack.add(node);
				stack1.add(node);
			}
			
			int numAttr = 0;	
			while (!stack.isEmpty()) {	//count the number of attributes of the current class and ancestor classes to use it for compute the size of the object
				node  = (CgenNode) stack.pop();
				for (Enumeration e2 = node.getFeatures().getElements(); e2.hasMoreElements(); ) {	
					Feature f =  (Feature) e2.nextElement();
					if (f instanceof attr) 
						numAttr++;
				}
			}
			
			str.print(CgenSupport.WORD); //Object size (int 32-bit words)
			str.print(numAttr+3);
			str.print("\n");
			
			str.print(CgenSupport.WORD); //Dispatch pointer
			CgenSupport.emitDispTableRef(C.getName(), str);
			str.print("\n");
						
			while (!stack1.isEmpty()) {	//insert the attributes in the following order AN < AN-1 < ... < A2 < A1
				node  = (CgenNode) stack1.pop();
				for (Enumeration e3 = node.getFeatures().getElements(); e3.hasMoreElements(); ) {	
					Feature f =  (Feature) e3.nextElement();
					if (f instanceof attr) { 
						attr a = (attr) f;	
						str.print(CgenSupport.WORD); //attributes list, default initialization
						if (a.type_decl.equals(TreeConstants.Int)) //int_const(index), index of integer 0 
							str.print(CgenSupport.INTCONST_PREFIX + AbstractTable.inttable.lookup("0").index);  
						else if (a.type_decl.equals(TreeConstants.Str))	//str_const(index), index of empty string
							str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup("").index);
						else if (a.type_decl.equals(TreeConstants.Bool)) //bool_const0	
							str.print(CgenSupport.BOOLCONST_PREFIX + "0");
						else //it is not a built-in object
							str.print("0");
						str.print("\n");
					}
				}
			}
        }*/
	}
	public void codeClassObjTab() {	
		//class_objTab		
		str.print(CgenSupport.CLASSOBJTAB);
		str.print(CgenSupport.LABEL);

		AbstractSymbol[] classesNameTab = new AbstractSymbol[tagOfClass.size()];
		for (Object o : tagOfClass.keySet()) {
			AbstractSymbol cname = (AbstractSymbol) o;
			int i = (Integer) tagOfClass.get(cname);
			classesNameTab[i] = cname;
		}	
		for (AbstractSymbol cname : classesNameTab) {	//insert class names for other objects 
			str.print(CgenSupport.WORD);
			CgenSupport.emitProtObjRef(cname, str);
			str.print("\n");
			str.print(CgenSupport.WORD);
			CgenSupport.emitInitRef(cname, str);
			str.print("\n");
		}
	}
	public void codeClassNameTab() {
		//class_nameTab
		str.print(CgenSupport.CLASSNAMETAB);
		str.print(CgenSupport.LABEL);
/*        for (class_ C : classes) {	
			AbstractSymbol cname = C.getName();	//get the symbol in the string table
			str.print(CgenSupport.WORD);	//emit reference to the string constant, using a prefix plus an index 
			str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(cname.toString()).index);
			str.print("\n");
		} */
		AbstractSymbol[] classesNameTab = new AbstractSymbol[tagOfClass.size()];
		for (Object o : tagOfClass.keySet()) {
			AbstractSymbol cname = (AbstractSymbol) o;
			int i = (Integer) tagOfClass.get(cname);
			classesNameTab[i] = cname;
		}	
		for (AbstractSymbol cname : classesNameTab) {	//insert class names for other objects 
			//System.out.println("C: "+cname);
			str.print(CgenSupport.WORD);	//emit reference to the string constant, using a prefix plus an index 
			str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(cname.toString()).index);
			str.print("\n");
		}

/*        for (CgenNode C : classes) {	//insert class names for built-in object and Main
			if (C.getName().equals(TreeConstants.Object_) ||
				C.getName().equals(TreeConstants.IO) 	|| 
				C.getName().equals(TreeConstants.Str)	||
				C.getName().equals(TreeConstants.Int)	||
				C.getName().equals(TreeConstants.Bool) || 
				C.getName().equals(TreeConstants.Main) ) {
			System.out.println("C: "+C.getName()+ " - "+ CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(C.getName().toString()).index);
				AbstractSymbol cname = C.getName();	//get the symbol in the string table
				str.print(CgenSupport.WORD);	//emit reference to the string constant, using a prefix plus an index 
				str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(cname.toString()).index);
				str.print("\n");
			}
			else {
				classesWithoutBuiltinObj.add(C);
				continue;
			}
		}
        for (class_ C : classesWithoutBuiltinObj) {	//insert class names for other objects 
			AbstractSymbol cname = C.getName();	//get the symbol in the string table
			str.print(CgenSupport.WORD);	//emit reference to the string constant, using a prefix plus an index 
			str.print(CgenSupport.STRCONST_PREFIX + AbstractTable.stringtable.lookup(cname.toString()).index);
			str.print("\n");
		}*/
	}
	public void codeDispatchTable() {
		//dispatch table
        for (class_ C : classes) {	//C is the current class
			CgenSupport.emitDispTableRef(C.getName(), str);
			str.print(CgenSupport.LABEL);

			Stack stack = new Stack();
			CgenNode node = (CgenNode) probe(C.getName()); 
			stack.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack ordering method from the first ancestor class to the last child, like attributes
				node = node.getParentNd(); 
				stack.add(node);
			}
			Set mnames = new HashSet(); //contains all the methods of the current class
			Map dispTab = new LinkedHashMap(); //LinkedHashMap preserves the insertion order of method names
			//int offset = 0;
			while (!stack.isEmpty()) {	//generating code
				node  = (CgenNode) stack.pop();
				AbstractSymbol cname;	//class name of the defined method
				for (Enumeration e1 = node.getFeatures().getElements(); e1.hasMoreElements(); ) {	
					Feature f =  (Feature) e1.nextElement();
					if (f instanceof method) {
						cname = node.getName();	//method is defined into an ancestor class
						//check for overriding method
						for (Enumeration e2 = C.getFeatures().getElements(); e2.hasMoreElements(); ) {	//find the method with the same name in the descendant class (current class)	
							Feature f1 =  (Feature) e2.nextElement();
							if (f1 instanceof method) {
								method m = (method) f1;	
								if (((method) f).name.equals(m.name)) { //overriding method, method is defined in the current class
									cname = C.getName(); break; }
							}
						}	
						//if (!mnames.add(((method) f).name.toString())) {  //if this method is not in the current dispatch table
						dispTab.put(((method) f).name, cname); //existing method names are replaced with the last encountered (the last class)
						//System.out.println(cname.toString()+"."+((method) f).name.toString()+ " "+ mnames.contains(cname.toString()+"."+((method) f).name.toString()));
						/*if (mnames.add(cname.toString()+"."+((method) f).name.toString())) {  //if this method is not in the current dispatch table
							AbstractSymbol[] cm = new AbstractSymbol[2];
							cm[0] = node.getName(); //class name
							cm[1] = ((method) f).name; //method name			
							mnamesstack.push(cm);

							dispTab.add()	
							//mnames.add(cname.toString()+"."+((method) f).name.toString());	//add this method name to the temporary dispatch list
							//System.out.println("Class: "+C.getName()+ " " +cname.toString()+"."+((method) f).name.toString());
							str.print(CgenSupport.WORD);					 
							CgenSupport.emitMethodRef(cname, ((method) f).name, str);
							impl.put(cname.toString()+"."+((method) f).name.toString(), offset++);//build a map with (class name, method name) as key that return an offset into the dispatch table	
							str.print("\n"); 
							//if (C.getName().toString().equals("B"))	
							//System.out.println(cname.toString()+"."+((method) f).name.toString()+ " "+ mnames.contains(cname.toString()+"."+((method) f).name.toString()));
						}*/
					}
				}
			}
			mnames.clear();
			int offset = 0;
			for (Object o : dispTab.keySet()) {
				AbstractSymbol mname = (AbstractSymbol) o;
				AbstractSymbol cname = (AbstractSymbol) dispTab.get(mname);
				str.print(CgenSupport.WORD);					 
				CgenSupport.emitMethodRef(cname, mname, str);
				impl.put(cname.toString()+"."+mname.toString(), offset++);//build a map with (class name, method name) as key that return an offset into the dispatch table	
				str.print("\n"); 
			}




		}
		/*for (class_ C : classes) {	//C is the current class
			CgenSupport.emitDispTableRef(C.getName(), str);
			str.print(CgenSupport.LABEL);

			Stack stack = new Stack();	//contains all ancestors of the current class, including the current class
			CgenNode node = (CgenNode) probe(C.getName()); //get the node of the current class
			stack.add(node);
			for (CgenNode an : getAncestors(C.getName())) 
				stack.add(an);
			Stack mnamesstack = new Stack(); //contains the method names from the first ancestor to the last children  
			Set mnames = new HashSet(); //contains all the methods name of the current class
			while (!stack.isEmpty()) {	//insert the method names of the current class from the first ancestor to the current class
				node  = (CgenNode) stack.pop(); 
				Feature[] feats = new Feature[node.getFeatures().getLength()]; 
				int i = node.getFeatures().getLength()-1; //puts features of the current node (class) in reverse order
				for (Enumeration e1 = node.getFeatures().getElements(); e1.hasMoreElements(); ) {	
					Feature f =  (Feature) e1.nextElement();
					feats[i] = f;
				}
				for (Feature f : feats)
					if (f instanceof method) {
						if (mnames.add(((method) f).name))			
							String[] cm = new String[2];
							cm[0] = node.getName().toString(); //class name
							cm[1] = ((method) f).name.toString(); //method name			
							mnamesstack.push(cm);
					}
				}
			}	
			mnames.clear();	
			int offset = 0;
			while (!mnamesstack.isEmpty()) {	//calculate the offset of any method of the current class and print the dispatch table
				String[] cm  = (String[]) mnamesstack.pop(); 
				str.print(CgenSupport.WORD);					 
				CgenSupport.emitMethodRef(cm[0], cm[1], str);
				impl.put(cm[0]+"."+cm[1], offset++);//build a map with (class name, method name) as key that return an offset into the dispatch table	
				str.print("\n"); 
			}
		}	*/		
	}
	public void codeObjectInit() {	
		//objects initialization, Class_init		
        for (CgenNode C : classes) {
			int nt = 0; //number of temporaries
			currClass = (class_) C;
			
			if (C.basic() == false) {
			for (Enumeration e = C.getFeatures().getElements(); e.hasMoreElements(); ) {	
				Feature f =  (Feature) e.nextElement();
				if (f instanceof attr)  
					nt += ((attr) f).init.NT();	//calculate the number of temporaries needed for each expression of the attribute
			}	}
			CgenSupport.emitInitRef(C.getName(), str); 
			str.print(CgenSupport.LABEL);
			//init stack, PROLOGUE
			CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -(4*(nt+3)), str); //	addiu	$sp $sp -12 
			CgenSupport.emitStore(CgenSupport.FP, nt+3, CgenSupport.SP, str); //	sw	$fp 12($sp)
			CgenSupport.emitStore(CgenSupport.SELF, nt-1+3, CgenSupport.SP, str);//	sw	$s0 8($sp)
			CgenSupport.emitStore(CgenSupport.RA, nt-2+3, CgenSupport.SP, str); //	sw	$ra 4($sp)
			//set up frame pointer
			CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str); //	addiu	$fp $sp 4
			CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str); //	move	$s0 $a0		

			nextp = 0; //set up the next slot free in memory for local variables

			CgenNode node = (CgenNode) probe(C.getName()); 
			if ( !node.getParentNd().getName().equals(TreeConstants.No_class) ) { 	//init parent object
				CgenSupport.emitJal(C.getParent() + CgenSupport.CLASSINIT_SUFFIX, str); //	jal Parent_init		
			}
			env.enterScope();	//variable environment
			//insert all attributes (including ancestors attributes) and store the corresponding offset in the environment  
			node = (CgenNode) probe(C.getName()); 
			Stack stack = new Stack();
			stack.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
				node = node.getParentNd(); 
				stack.add(node);
			}
			int offset = 0; //offset of the attribute in the prot_Obj
			while (!stack.isEmpty()) {
				node  = (CgenNode) stack.pop();
				for (Enumeration e1 = node.features.getElements(); e1.hasMoreElements(); ) {	
					Feature f =  (Feature) e1.nextElement();
					if (f instanceof attr) { 
						Object obj[] = {f, new Integer(3+offset++)};	//contains the attribute and the offset
						env.addId(((attr) f).name, obj);
					}
				}
			}
			if (C.basic() == false) {	//skip basic classes, no custom attribute definition
				for (Enumeration e1 = node.getFeatures().getElements(); e1.hasMoreElements(); ) {	
					Feature f =  (Feature) e1.nextElement();
					if (f instanceof attr) {
						attr a = (attr) f;
						if (a.init.get_type() != null && !a.init.get_type().equals(TreeConstants.No_type)) { //if init expression is defined
							a.init.code(str, this);	//generate code for expression of the attributes
							Object[] obj = (Object[]) env.lookup(a.name);	//get the variable (global or local) and the offset
							CgenSupport.emitStore(CgenSupport.ACC, (Integer) obj[1], CgenSupport.SELF, str); //store the value of the attribute
						} 
					}
				}
			}
			//EPILOGUE
			CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str); //	sw	$a0 $s0
			CgenSupport.emitLoad(CgenSupport.FP, nt+3, CgenSupport.SP, str); //	lw	$fp 12($sp)
			CgenSupport.emitLoad(CgenSupport.SELF, nt-1+3, CgenSupport.SP, str);//	lw	$s0 8($sp)
			CgenSupport.emitLoad(CgenSupport.RA, nt-2+3, CgenSupport.SP, str); //	lw	$ra 4($sp)
			CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4*(nt+3), str); //	addiu	$sp $sp 12 
			CgenSupport.emitReturn(str); //jr $ra
			env.exitScope();
		}
	}

	public void codeMethodDef() {
		//generating code for method definition, Class.method
        for (CgenNode C : classes) {
			if (C.basic() == true) continue; //skip basic classes, their method definition are in the trap.handler file	
			currClass = (class_) C;

			env.enterScope();	//variable environment
			//insert all attributes (including ancestors attributes) and store the corresponding offset in the environment  
			CgenNode node = (CgenNode) probe(C.getName()); 
			Stack stack = new Stack();
			stack.add(node);
			while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
				node = node.getParentNd(); 
				stack.add(node);
			}
			int offset = 0; //offset of the attribute in the prot_Obj
			while (!stack.isEmpty()) {
				node  = (CgenNode) stack.pop();
				for (Enumeration e1 = node.features.getElements(); e1.hasMoreElements(); ) {	
					Feature f =  (Feature) e1.nextElement();
					if (f instanceof attr) { 
						Object obj[] = {f, new Integer(3+offset++)};	//contains the attribute and the offset
						env.addId(((attr) f).name, obj);
					}
				}
			}
			//generate code for any method of the current class
			for (Enumeration e2 = C.getFeatures().getElements(); e2.hasMoreElements();) {
				Feature f = (Feature) e2.nextElement();
				if (f instanceof method) {
					env.enterScope();

					method m = (method) f;
					int nt = m.expr.NT();	//get the number of temporaries for the expression body of the current method
					//nt += 0; //space for old $fp, $s0 and $ra

					formal[] fs = new formal[m.formals.getLength()];
					int i = 0;
					for (Enumeration e3 = m.formals.getElements(); e3.hasMoreElements();) {
						formal p = (formal) e3.nextElement(); fs[i++] = p; }
					offset = 0;	
					for (i = fs.length-1; i >= 0; i--) {	//reverse parameters order 					
						formal p = fs[i];
						Object[] obj = {p, new Integer(offset+nt+3)}; offset++; //contains parameter of method and the offset
						env.addId(((formal) p).name, obj); //insert parameter and its offset in the environment
					}

					CgenSupport.emitMethodRef(C.getName(), m.name, str);
					str.print(CgenSupport.LABEL);
					//PROLOGUE
					CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -(4*(nt+3)), str); //	addiu	$sp $sp -12 
					CgenSupport.emitStore(CgenSupport.FP, nt+3, CgenSupport.SP, str); //	sw	$fp 12($sp)
					CgenSupport.emitStore(CgenSupport.SELF, nt-1+3, CgenSupport.SP, str);//	sw	$s0 8($sp)
					CgenSupport.emitStore(CgenSupport.RA, nt-2+3, CgenSupport.SP, str); //	sw	$ra 4($sp)
					//set up frame pointer
					CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str); //	addiu	$fp $sp 4
					CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str); //	sw	$s0 $a0	
	
					nextp = 0; //set up the next slot free in memory for local variables
					//generating code for the expression body
					m.expr.code(str, this);

					//EPILOGUE
					CgenSupport.emitLoad(CgenSupport.FP, nt+3, CgenSupport.SP, str); //	lw	$fp 12($sp)
					CgenSupport.emitLoad(CgenSupport.SELF, nt-1+3, CgenSupport.SP, str);//	lw	$s0 8($sp)
					CgenSupport.emitLoad(CgenSupport.RA, nt-2+3, CgenSupport.SP, str); //	lw	$ra 4($sp)
					CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, (4*(m.formals.getLength()+nt+3)), str); //	addiu	$sp $sp (4*n+12) 
					CgenSupport.emitReturn(str); //jr $ra

					env.exitScope();
				}
			}
			env.exitScope();
		}
    }
/*	//compute the offset if it is a name of an attribute of the current class
	public int getOffset(AbstractSymbol cname, AbstractSymbol mname) {
		CgenNode node = (CgenNode) probe(cname); 
		stack.add(node);
		while (!node.getName().equals(TreeConstants.Object_)) {	//insert node into the stack, AN < AN-1 < ... < A2 < A1
			node = node.getParentNd(); 
			stack.add(node);
		}
		int offset = 0;
		while (!stack.isEmpty()) {
			node  = (CgenNode) stack.pop();
			for (Enumeration e = node.getFeatures().getElements(); e.hasMoreElements(); ) {	
				Feature f =  (Feature) e.nextElement();
				if (f instanceof method) { 
					if ((((method) f).name).equals(mname))
						return offset;
					offset++;		
				}
			}
		}
		return -1;
	}*/
	//check if a class named c1 is ancestor of a class named c2
	public boolean isAncestor(AbstractSymbol c1, AbstractSymbol c2) {
		CgenNode descendant = (CgenNode) probe(c2);
 
		if (c1.equals(TreeConstants.Object_) || c1.equals(c2)) return true;

		while (!descendant.getName().equals(TreeConstants.Object_)) {	
			if (descendant.getName().equals(c1)) return true;
			descendant = descendant.getParentNd(); 
		}
		return false;	
	}
	//get all acenstors of a specified class named c
	public ArrayList getAncestors(AbstractSymbol c) {
		CgenNode ancestor = (CgenNode) probe(c);
		ArrayList ancestors = new ArrayList(); 

		if (c.equals(TreeConstants.Object_)) return null; //Object class has no parent

		while (!ancestor.getName().equals(TreeConstants.Object_)) {	
			ancestor = ancestor.getParentNd(); 
			ancestors.add(ancestor);
		}
		return ancestors;	
	}
	//counts the number of discendants of a node
	public int count(CgenNode r) {
		int n = 1;
		for (Enumeration e = r.getChildren(); e.hasMoreElements(); ) {	
			CgenNode  gn = (CgenNode) e.nextElement();
			n += count(gn);
		}
		return n;
	}
	//traverse the graph and insert the node in a list
	public void traverse(CgenNode node, ArrayList cls) {	//traverse the graph and insert the node in a list
		cls.add((class_) node);
		for (Enumeration e = node.getChildren(); e.hasMoreElements(); ) {	
			CgenNode  gn = (CgenNode) e.nextElement();
			traverse(gn, cls);
		}
	}
	//keys for the implementation method
	public class Keys {
		public AbstractSymbol cname; //class name
		public AbstractSymbol mname; //method name

		public Keys(AbstractSymbol c, AbstractSymbol m) {
			cname = c;
			mname = m;
		}

		public boolean equals(Object o) {
			if (o != null && o instanceof Keys) {
				Keys k = (Keys)	o;
				return cname.equals(k.cname) && mname.equals(k.mname); 
			}
			return false;
		}
		
		public int hashcode() {
			return (cname.toString()+"."+mname.toString()).hashCode();
		}

	}

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
}
			  
    
