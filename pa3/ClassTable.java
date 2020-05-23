import java.io.PrintStream;
import java.util.*;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    private Graph<class_c, String> inheritanceGraph = null;
	public class_c classEnv;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class = 
	    new class_c(0, 
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
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
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

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
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
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

		if (inheritanceGraph == null) {
			System.err.println("Inheritance graph has not been instantiated.");
			return;
		}

		/* Do somethind with Object_class, IO_class, Int_class,
		       Bool_class, and Str_class here */
		//inserting vertices (built-in objects) in the inheritance graph
		Vertex<class_c> object = inheritanceGraph.insertVertex(Object_class);
		Vertex<class_c> io = inheritanceGraph.insertVertex(IO_class);
		Vertex<class_c> int_class = inheritanceGraph.insertVertex(Int_class);
		Vertex<class_c> bool = inheritanceGraph.insertVertex(Bool_class);
		Vertex<class_c> str = inheritanceGraph.insertVertex(Str_class);
		//inserting edges (all the built-in objects inherit from Object)
		inheritanceGraph.insertEdge(io, object, null);
		inheritanceGraph.insertEdge(int_class, object, null);
		inheritanceGraph.insertEdge(bool, object, null);
		inheritanceGraph.insertEdge(str, object, null);
    }
	
    public ClassTable(Classes cls) {
		semantErrors = 0;
		errorStream = System.err;
		inheritanceGraph = new Graph<class_c,String>(true);
		installBasicClasses();
		class_c c = null;		
        for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
	    	c = (class_c) e.nextElement();
			inheritanceGraph.insertVertex(c);	
		}
        for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
	    	c = (class_c) e.nextElement();
			inherits(c);
		}
		//check that the graph is well-formed
		isDuplicatedClass();
		checkCircularInheritance();

	/* fill this in */
    }

    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(class_c c) {
	return semantError(c.getFilename(), c);
    }

    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
	errorStream.print(filename + ":" + t.getLineNumber() + ": ");
	return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError() {
	semantErrors++;
	return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
	return semantErrors != 0;
    }

	//insert a new edge in the inheritance graph
    private void inherits(class_c C) {
		Vertex<class_c> parent = null;
		Vertex<class_c> child = null;

		for (Vertex<class_c> v : inheritanceGraph.vertices()) { //find the parent class of the current class in the graph
			if (v.element().getName().equals(C.getParent())) { 
				parent = v; break;
			} ;
		}
		for (Vertex<class_c> v : inheritanceGraph.vertices()) { //find the current class in the graph
			if (v.element().getName().equals(C.getName())) { 
				child = v; break;
			} 
		}
		if (parent != null && child != null) inheritanceGraph.insertEdge(child, parent, null);
	}
	//check if there are duplicated classes
	private void isDuplicatedClass() {
		Set<AbstractSymbol> set = new HashSet<AbstractSymbol>();
		for (Vertex<class_c> v : inheritanceGraph.vertices()) {	
			if (!set.add(v.element().getName())) semantError(v.element()).println("Class " + v.element().getName() + " was previously defined."); 
		}
	}
	//check if the graph has cycles
    private void checkCircularInheritance() {
		LinkedList<Vertex<class_c>>[] cycles = Graph.Tarjan.circuit_enumeration(inheritanceGraph);	//using Tarjan algorithm to find cycles, return a linked list of cycles
		Set<AbstractSymbol> set = new HashSet<AbstractSymbol>();	//store vertex involved in a cycle
		for (LinkedList<Vertex<class_c>> l: cycles)
			for (Vertex<class_c> v : l) 
				set.add(v.element().getName());  			
		for (AbstractSymbol a : set) 
			semantError(findClass(a)).println("Class " + a + " , or an ancestor of " + a + ", is involved in an inheritance cycle."); 
    }
	public class_c findClass(AbstractSymbol c) {
		for (Vertex<class_c> v : inheritanceGraph.vertices()) //find the class
			if (v.element().getName().equals(c))  
				return v.element();
		return null;
	}
	public class_c[] getAncestors(AbstractSymbol c) { //find all ancestors of a class, given a name
		ArrayList<class_c> ancestors =  new ArrayList<class_c>();
		class_c C = findClass(c);
		while (!C.getName().equals(TreeConstants.Object_)) {
			C = findClass(C.getParent());
			ancestors.add(C);
		}
		class_c[] ancarr = new class_c[ancestors.size()];
		return (class_c[]) ancestors.toArray(ancarr);

	}
/*	private void depth(Vertex<class_c> u, ArrayList<class_c> cs) { //get parent of a depth of a class node
		cs.add(u.element());
		for (Edge<String> e : inheritanceGraph.outgoingEdges(u)) {  
			Vertex<class_c> v = inheritanceGraph.opposite(u, e);	
			if (!v.element().getParent().equals(TreeConstants.No_class)) //call depth until the root is found
				depth(v, cs);				
		}
	}	
	private int depth(Vertex<Class_> u) { //calculate depth of a class node
		for (Edge<E> e : inheritanceGraph.outgoingEdges(u)) {  
			Vertex<V> v = inheritanceGraph.opposite(u, e);	
			if (v.element().getParent().equals(TreeConstants.No_class)) //ascend until an element class has no parent
				return 0;
			return 1 + depth(v);				
	}*/
	private int depth(class_c c) { //calculate depth of a class node, precondition is that the graph is a tree and classes are unique
		if (c.getParent().equals(TreeConstants.No_class)) return 0;
		return 1 + depth(findClass(c.getParent()));	
	}
	public class_c lub(AbstractSymbol Cname, AbstractSymbol Tname) {//find least upper bound of two classes, precondition is that the graph is a tree and classes are unique
		class_c C = null;
		class_c T = null;
		if (Cname.equals(TreeConstants.SELF_TYPE)) C = classEnv;
		else C = findClass(Cname);
		if (Tname.equals(TreeConstants.SELF_TYPE)) T = classEnv;
		else T = findClass(Tname);

	  	while (C!=T){
			if (depth(C) > depth(T))
				C = findClass(C.getParent());
		  	else
		    	T= findClass(T.getParent());
	  	}
	  	return C;
	}
	public class_c lub(AbstractSymbol[] Cname) {//find least upper bound class of more than two classes, precondition is that the graph is a tree and classes are unique
		class_c LCA =  lub(Cname[0], Cname[1]);
		for (int i = 2; i < Cname.length; i++)
			LCA = lub(LCA.getName(),  Cname[i]);
		return LCA;
	}
	public boolean sub(AbstractSymbol Cname, AbstractSymbol Tname) {//check if a class named Cname is a descendant of a class named Tname,precondition is that the graph is acyclic
		class_c C = null;
		class_c T = null;
		if (Cname.equals(Tname)) return true; //it is the same class
		if (Tname.equals(TreeConstants.Object_)) return true; //all classes are descendendant of Object
		if (Cname.equals(TreeConstants.SELF_TYPE)) {
			C = classEnv;
			if (C.getName().equals(Tname)) return true; //SELF_TYPE refer to the same class
		}
		else C = findClass(Cname);
		if (Tname.equals(TreeConstants.SELF_TYPE)) {
			T = classEnv;
			if (T.getName().equals(Cname)) return true; //SELF_TYPE refer to the same class
		}
		else T = findClass(Tname);
	
		while (!C.getParent().equals(T.getName()) && !C.getParent().equals(TreeConstants.No_class))
			C = findClass(C.getParent());

		return !C.getName().equals(TreeConstants.Object_); 
	}
	public method methodEnv(AbstractSymbol Cname, AbstractSymbol f) { //method environment, return method named f of a class named Cname or all ancestors of Cname, null otherwise
		class_c C = findClass(Cname);
		if (C == null) return null;
		Feature feat = null;
		method m = null;
		boolean found = false;	
		while (!found && !C.getParent().equals(TreeConstants.No_class)) { //find method named f in the ancestor of C
   			for (Enumeration e = C.features.getElements(); e.hasMoreElements();) {
				feat = ((Feature)e.nextElement());
				if (feat instanceof method) {			    			
					m = (method) feat;
					if (m.name.equals(f)) {
						found = true; break; }
				}
    		}
			if (!found) C = findClass(C.getParent());
		}
		if (C.getName().equals(TreeConstants.Object_)) {
   			for (Enumeration e = C.features.getElements(); e.hasMoreElements();) {
				feat = ((Feature)e.nextElement());
				if (feat instanceof method) {			    			
					m = (method) feat;
					if (m.name.equals(f)) {
						found = true; break; }
				}
			}
		}	
		if (found)
			return m;
		else 
			return null;
	}
	public method methodEnv(AbstractSymbol f) { //method environment, return method named f of an ancestor class of the current class, null otherwise
		class_c C = findClass(classEnv.getParent());
		if (C == null) return null;
		Feature feat = null;
		method m = null;
		boolean found = false;	
		while (!found && !C.getParent().equals(TreeConstants.No_class)) { //find method named f in the ancestor of C
   			for (Enumeration e = C.features.getElements(); e.hasMoreElements();) {
				feat = ((Feature)e.nextElement());
				if (feat instanceof method) {			    			
					m = (method) feat;
					if (m.name.equals(f)) {
						found = true; break; }
				}
    		}
			if (!found) C = findClass(C.getParent());
		}
		if (C.getName().equals(TreeConstants.Object_)) {
   			for (Enumeration e = C.features.getElements(); e.hasMoreElements();) {
				feat = ((Feature)e.nextElement());
				if (feat instanceof method) {			    			
					m = (method) feat;
					if (m.name.equals(f)) {
						found = true; break; }
				}
			}
		}	
		if (found)
			return m;
		else 
			return null;
	}



	/*public void lub(AbstractSymbol Cname, AbstractSymbol Tname) {//find least upper bound of two classes, precondition is that the graph is a tree and classes are unique
		Vertex<Class_> vv = null;
		Vertex<Class_> ww = null;
		for (Vertex<Class_> v : inheritanceGraph.vertices())//find the class node
			if (v.element().getName().equals(Cname)) {  
				vv = v;
				if (ww != null) break;
			}
			if (v.element().getName().equals(Tname)) {  
				ww = v;
				if (vv != null) break;
			}
		}
	  	while (vv!=ww){
			if (depth(vv) > depth(ww))
				vv= inheritanceGraph.opposite(vv, inheritanceGraph.outgoingEdges(vv));
		  	else
		    	ww=inheritanceGraph.opposite(ww, inheritanceGraph.outgoingEdges(ww));
	  	}
	  	return vv;
	}*/

}



