import java.io.PrintStream;
import graph.Graph;
import graph.Vertex;
import graph.Edge;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    private Graph<Class_, String> inheritanceGraph;
	public Class_ classEnv;

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

		/* Do somethind with Object_class, IO_class, Int_class,
		       Bool_class, and Str_class here */
		//inserting vertices (built-in objects) in the inheritance graph
		Vertex<Class_> object = inheritanceGraph.insertVertex(Object_class);
		Vertex<Class_> io = inheritanceGraph.insertVertex(Io_class);
		Vertex<Class_> int_class = inheritanceGraph.insertVertex(Int_class);
		Vertex<Class_> bool = inheritanceGraph.insertVertex(Bool_class);
		Vertex<Class_> str = inheritanceGraph.insertVertex(Str_class);
		//inserting edges (all the built-in objects inherit from Object)
		inheritanceGraph.insertEdge(io, object, null);
		inheritanceGraph.insertEdge(int_class, object, null);
		inheritanceGraph.insertEdge(bool, object, null);
		inheritanceGraph.insertEdge(str, object, null);
    }
	
    public ClassTable(Classes cls) {
		semantErrors = 0;
		errorStream = System.err;
		inheritanceGraph = new Graph<Class_,String>(true);
		installBasicClasses();
		for (Class_ c : cls)	//inserts classes in the graph
			inheritanceGraph.insertVertex(c);	
		for (Class_ c : cls) //build inheritance in the graph
			inherits(c);
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
    private void inherits(Class_ C) {
		Vertex<Class_> parent = null;

		for (Vertex<Class_> v : inheritanceGraph.vertices()) { //find the parent class of the current class if any
			if (v.element().getName().equals(C.getParent().getName())) { 
				parent = v; break;
			}
		}
		if (parent == null) semantError(C).println(C.getParent().getName() + " is not defined");  //the parent class is not defined
	}
	//check if there are duplicated classes
	private void isDuplicatedClass() {
		Set<Vertex<Class_>> set = new HashSet<Vertex<Class_>>();
		for (Vertex<Class_> v : inheritanceGraph.vertices()) {
			if (!set.add(v)) semantError(v.element()).println(v.element().getName() + " can't be redefined."); 
		}
	}
	//check if the graph has cycles
    private void checkCircularInheritance() {
		LinkedList<Vertex<Class_>>[] cycles = Graph.Tarjan.circuit_enumeration(inheritanceGraph);	//using Tarjan algorithm to find cycles, return a linked list of cycles
		Set<Vertex<Class_>> set = new HashSet<Vertex<Class_>>();	//store vertex involved in a cycle
		for (Linked<Vertex<Class_>> l: cycles)
			for (Vertex<Class_> v : l)
				set.add(v);			
		for (Vertex<Class_> v : set)		
			semantError(v.element()).println(v.element().getName() + " is involved in a cycle");
    }
	public Class_ findClass(AbstractSymbol c) {
		for (Vertex<Class_> v : inheritanceGraph.vertices()) //find the class
			if (v.element().getName().equals(c))  
				return v.element();
		return null;
	}
	public Class_[] ancestorClass(AbstractSymbol c) { //find all ancestor of a class, given a name
		Vertex<Class_> u = null;
		for (Vertex<Class_> v : inheritanceGraph.vertices()) //find the class node
			if (v.element().getName().equals(c)) {  
				u = v;
				break;
			}
		ArrayList<Class_> cs = new ArrayList<Class_>();
		depth(u, cs);
		Class_[] vcs = new Class_[cs.size()];
		return (Class_[]) cs.toArray(vcs);
	}
	private void depth(Vertex<Class_> u, ArrayList<Class_> cs) { //get parent of a depth of a class node
		cs.add(v.element());
		for (Edge<E> e : inheritanceGraph.outgoingEdges(u)) {  
			Vertex<V> v = inheritanceGraph.opposite(u, e);	
			if (!v.element().getParent().equals(TreeConstants.No_class)) //call depth until the root is found
				depth(v, cs);				
		}
	}	
	/*private int depth(Vertex<Class_> u) { //calculate depth of a class node
		for (Edge<E> e : inheritanceGraph.outgoingEdges(u)) {  
			Vertex<V> v = inheritanceGraph.opposite(u, e);	
			if (v.element().getParent().equals(TreeConstants.No_class)) //ascend until an element class has no parent
				return 0;
			return 1 + depth(v);				
	}*/
	private int depth(Class_ c) { //calculate depth of a class node, precondition is that the graph is a tree and classes are unique
		if (c.getParent().equals(TreeConstants.No_class)) return 0;
		return 1 + depth(c.getParent());	
	}
	public Class_ lub(AbstractSymbol Cname, AbstractSymbol Tname) {//find least upper bound of two classes, precondition is that the graph is a tree and classes are unique
		if (Cname.equals(TreeConstants.SELF_TYPE)) C = classEnv;
		else C = findClass(Cname);
		if (Tname.equals(TreeConstants.SELF_TYPE)) T = classEnv;
		else T = findClass(Tname);

	  	while (C!=T){
			if (depth(C) > depth(T))
				C= C.getParent();
		  	else
		    	T=T.getParent();
	  	}
	  	return C;
	}
	public Class_ lub(AbstractSymbol[] Cname) {//find least upper bound class of more than two classes, precondition is that the graph is a tree and classes are unique
		Class_ LCA =  lub(Cname[0], Cname[1]);
		for (int i = 2; i < C.length; i++)
			LCA = lub(LCA.getName(),  Cname[i]);
		return LCA;
	}
	public boolean sub(AbstractSymbol Cname, AbstractSymbol Tname) {//check if a class named Cname is a descendant of a class named Tname,precondition is that the graph is acyclic
		if (Cname.equals(Tname)) return true; //it is the same class
		if (Cname.equals(TreeConstants.SELF_TYPE)) C = classEnv;
		else C = findClass(Cname);
		if (Tname.equals(TreeConstants.SELF_TYPE)) T = classEnv;
		else T = findClass(Tname);
	
		while (!C.getParent().getName().equals(T.getParent().getName()) || !C.getParent().getName().equals(TreeConstants.No_class))
			C = C.getParent();

		return !C.equals(TreeConstants.No_class); 
	}
	public method methodEnv(AbstractSymbol Cname, AbstractSymbol f) { //method environment, return method named f of a class named Cname, null otherwise
		Class_ C = findClass(Cname);
		method m = null;
		boolean found = false;
		for (Enumeration e = ((class_c) C).features.getElements(); e.hasMoreElements();) { //find method named f in the class C
    		m = ((method)e.nextElement());
			if (m.name.equals(f)) {
				found = true; break; }
       	}
		while (!found && !C.getParent().equals(TreeConstants.No_class)) { //find method named f in the ancestor of C
   			for (Enumeration e = ((class_c) C).features.getElements(); e.hasMoreElements();) {
				m = ((method)e.nextElement());
				if (m.name.equals(name)) {
					found = true; break; }
    		}
			C = C.getParent();
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



