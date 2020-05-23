#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include "semant.h"
#include "utilities.h"

extern int semant_debug;
extern char *curr_filename;

///////////////GRAPH DEFINITION///////////////////
Graph::Graph(int nv) : is_direct(true), n_vertex(nv), adj(n_vertex, std::vector<Edge *>(n_vertex, NULL)) {
}

Graph::Graph(int nv, bool v): is_direct(v), n_vertex(nv), adj(n_vertex, std::vector<Edge *>(n_vertex, NULL)) {
}

void Graph::check_index(Edge *e) {
	int i = e->i_origin;
	int j = e->i_destination; 
	if (i >= n_vertex || i < 0 || j >= n_vertex || j < 0) {
		cerr << "vertex index out of range!" << endl;
		exit(1);
	}		
}	

void Graph::check_index(int i) {
	if (i >= n_vertex || i < 0) {
		cerr << "vertex index out of range!" << endl;
		exit(1);
	}
}

void Graph::insert_edge(Edge *e) {
	check_index(e);
	int i = e->i_origin;
	int j = e->i_destination; 
	if (adj[i][j] == NULL) {
		adj[i][j] = e; 
		n_edge++;
		if (!is_direct) {
			n_edge++;
			adj[j][i] = e;
		}
	}
}

void Graph::remove_edge(Edge *e) {
	check_index(e);
	int i = e->i_origin;
	int j = e->i_destination;	
	if (adj[i][j] != NULL) { 
		adj[i][j] = NULL; 
		n_edge--;
		if (!is_direct) {
			adj[j][i] = NULL;
			n_edge--;
		}
	}
}

void Graph::removeAll() {
	for (int i = 0; i < n_vertex; i++)
		for (int j = 0; j < n_vertex; j++) 
			if(adj[i][j] != NULL) { 
				adj[i][j] = NULL;
				delete adj[i][j];
			}
}

void Graph::print() {	
	for (int i = 0; i < n_vertex; i++)
		for (int j = 0; j < n_vertex; j++) 
			if(adj[i][j] != NULL) 
				cout << i << ": " << j << endl; //<< *((string *) (adj[i][j]->el_origin)) << " - " << j << ": " << *((string *) (adj[i][j]->el_destination)) << endl; }
}

std::vector<Edge *> Graph::out_edges(int v) {
	check_index(v);
	std::vector<Edge*> oe;
	for (int j = 0; j < n_vertex; j++)
		if (adj[v][j] != NULL)
			 oe.push_back(adj[v][j]);
	return oe;
}

std::vector<Edge *> Graph::in_edges(int v) {
	check_index(v);
	std::vector<Edge*> oe;
	for (int i = 0; i < n_vertex; i++)
		if (adj[i][v] != NULL)
			 oe.push_back(adj[i][v]);
	return oe;
}

void *Graph::get_element(int v) {
	const std::vector<Edge *> &eeout = out_edges(v);
	if (eeout.size() == 0) {
		const std::vector<Edge *> &eein = in_edges(v); 
		if (eein.size() == 0)
			return NULL;
		else return eein[0]->el_destination;
	}
	else return eeout[0]->el_origin; 
}

int Graph::get_num_vertex() {
	return n_vertex;
}

int Graph::get_num_edges() {
	return n_edge;
}

std::vector< std::vector<int> > *Graph::Tarjan::circuit_enumeration(Graph g) {
	int n = g.get_num_vertex();
	std::vector<bool> marked(n, false);
	std::stack<int> markedS;
	std::stack<int> pointS;
	std::vector< std::vector<int> > *cycles = new std::vector< std::vector<int> >();

	for (int i = 0; i < n; i++) { 
		Tarjan::backtrack(&g, i, i, &marked, &markedS, &pointS, cycles);
		while (!markedS.empty()) {
			int u = markedS.top();
			markedS.pop();
			marked[u] = false;
		}
	}
	return cycles;
}

/* verifica i percorsi da un nodo s a t */
bool Graph::Tarjan::backtrack(Graph *graph, int s, int v, std::vector<bool> *marked, std::stack<int> *markedS, std::stack<int> *pointS, std::vector< std::vector<int> > *cycles) {
	pointS->push(v);
	(*marked)[v] = true;
	markedS->push(v);
	bool f = false;

	//std::vector<Edge *> &ve = graph->out_edges(v);
	for (unsigned int i = 0; i < graph->out_edges(v).size(); i++) {
		Edge *e = graph->out_edges(v)[i];
		int w = e->i_destination;	 //System.out.println("w: "+(w+1));
		if (w < s) { graph->remove_edge(e); /*graph.removeVertex(graph.getVertex(w));*/  /*System.out.println("s is: "+(s+1)+" v is: "+(v+1) +" removed: "+(w+1));*/ }
		else if (w == s) {	//memorizza i vertici del ciclo
			f = true;
			int size = pointS->size();
			int *tmp = new int[size];
			int i = 0;
			std::vector<int> paths;
			while (!pointS->empty()) { tmp[i++] = pointS->top(); pointS->pop(); }	//System.out.print("cycle: ");
			for (i = size-1; i >= 0; i--) { 
				//System.out.print((tmp[i]+1)+" "); 
				paths.push_back(tmp[i]);
				pointS->push(tmp[i]); 
			}
			paths.push_back(tmp[size-1]);
			//System.out.println(tmp[size-1]+1);
			//System.out.print("vertex: "+graph.getVertex(size-1).element());
			//paths.addLast(graph.getVertex(i));
			cycles->push_back(paths);
			delete [] tmp;
		}
		else if (!(*marked)[w]) {
			bool g = Tarjan::backtrack(graph, s, w, marked, markedS, pointS, cycles); //System.out.println("f: "+f+ " g: "+g);
			f = f || g;
		//System.out.println("g: "+g);
		}
	}
	if (f == true) {	//System.out.println("peek: "+ (((Integer) markedS.peek())+1));
		while ( markedS->top() != v) { //System.out.println("peek: "+ (((Integer) markedS.peek())+1));
			int u = markedS->top();
			markedS->pop();
			(*marked)[u] = false;
		}
		if (!markedS->empty()) {
			markedS->pop();
			(*marked)[v] = false;
		}
	}	//cancella il vertice corrente dallo stack
	pointS->pop();  //System.out.println("f: "+f); 
	return f;
}
///////////END GRAPH DEFINITION//////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
// Symbols
//
// For convenience, a large number of symbols are predefined here.
// These symbols include the primitive type and method names, as well
// as fixed names used by the runtime system.
//
//////////////////////////////////////////////////////////////////////
static Symbol 
    arg,
    arg2,
    Bool,
    concat,
    cool_abort,
    copy,
    Int,
    in_int,
    in_string,
    IO,
    length,
    Main,
    main_meth,
    No_class,
    No_type,
    Object,
    out_int,
    out_string,
    prim_slot,
    self,
    SELF_TYPE,
    Str,
    str_field,
    substr,
    type_name,
    val;
//
// Initializing the predefined symbols.
//
static void initialize_constants(void)
{
    arg         = idtable.add_string("arg");
    arg2        = idtable.add_string("arg2");
    Bool        = idtable.add_string("Bool");
    concat      = idtable.add_string("concat");
    cool_abort  = idtable.add_string("abort");
    copy        = idtable.add_string("copy");
    Int         = idtable.add_string("Int");
    in_int      = idtable.add_string("in_int");
    in_string   = idtable.add_string("in_string");
    IO          = idtable.add_string("IO");
    length      = idtable.add_string("length");
    Main        = idtable.add_string("Main");
    main_meth   = idtable.add_string("main");
    //   _no_class is a symbol that can't be the name of any 
    //   user-defined class.
    No_class    = idtable.add_string("_no_class");
    No_type     = idtable.add_string("_no_type");
    Object      = idtable.add_string("Object");
    out_int     = idtable.add_string("out_int");
    out_string  = idtable.add_string("out_string");
    prim_slot   = idtable.add_string("_prim_slot");
    self        = idtable.add_string("self");
    SELF_TYPE   = idtable.add_string("SELF_TYPE");
    Str         = idtable.add_string("String");
    str_field   = idtable.add_string("_str_field");
    substr      = idtable.add_string("substr");
    type_name   = idtable.add_string("type_name");
    val         = idtable.add_string("_val");
}

class__class *ClassTable::find_class(Symbol s) {	//find class by name
	for (int i = 0; i < inheritance_graph.get_num_vertex(); i++) {
		void *v = inheritance_graph.get_element(i); //return element of the vertex i	
		class__class *c;
		if (v != NULL) c = (class__class *) v;
		else return NULL;
		//printf("i: %d child: %s\n", i, c->name->get_string());
		if (c->get_name()->equal_string(s->get_string(), s->get_len())) { 
			return c; }
	}
	return NULL;
}

std::vector<class__class *> *ClassTable::get_ancestors(Symbol c) { //find and return all ancestors of a class, given a name
	std::vector<class__class *> *ancestors = new std::vector<class__class *>;
	class__class *C = find_class(c);	
	while (!C->get_name()->equal_string(Object->get_string(), Object->get_len())) {
		C = find_class(C->get_parent()); 
		ancestors->push_back(C);
	}
	return ancestors;
}

int ClassTable::depth(class__class *c) { //calculate depth of a class node, precondition is that the graph is a tree and classes are unique
	if (c->get_parent()->equal_string(No_class->get_string(), No_class->get_len())) return 0;
	return 1 + depth(find_class(c->get_parent()));	
}

class__class *ClassTable::lub(Symbol Cname, Symbol Tname) {//find least upper bound of two classes, precondition is that the graph is a tree and classes are unique
	class__class *C = NULL;
	class__class *T = NULL;
	if (Cname->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) C = class_env;
	else C = find_class(Cname);
	if (Tname->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) T = class_env;
	else T = find_class(Tname);  
  	while ( !C->name->equal_string(T->name->get_string(), T->name->get_len()) ) {
		int a = depth(C); 
		int b = depth(T);	
		if (a > b)
			C = find_class(C->get_parent());
	  	else
	    	T= find_class(T->get_parent());
  	}
  	return C;
}

class__class *ClassTable::lub(Symbol *Cname) {//find least upper bound class of more than two classes, precondition is that the graph is a tree and classes are unique
	class__class *LCA =  lub(Cname[0], Cname[1]);
	Cname += 2;	
	while (*Cname)
		LCA = lub(LCA->get_name(),  *Cname++);
	return LCA;
}

bool ClassTable::sub(Symbol Cname, Symbol Tname) {//check if a class named Cname is a descendant of a class named Tname,precondition is that the graph is acyclic
	class__class *C = NULL;
	class__class *T = NULL;
	if (Cname->equal_string(Tname->get_string(), Tname->get_len())) return true; //it is the same class
	if (Tname->equal_string(Object->get_string(), Object->get_len())) return true; //all classes are descendendant of Object
	if (Cname->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) {	
		C = class_env;	//printf("Cname: C: %s\n", C->name->get_string());
		if (C->get_name()->equal_string(Tname->get_string(), Tname->get_len())) return true; //SELF_TYPE refer to the same class
	}
	else C = find_class(Cname);
	if (Tname->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) {
		T = class_env;
		if (T->get_name()->equal_string(Cname->get_string(), Cname->get_len())) return true; //SELF_TYPE refer to the same class
	}
	else T = find_class(Tname);

	while (!C->get_parent()->equal_string(T->get_name()->get_string(), T->get_name()->get_len()) && !C->get_parent()->equal_string(No_class->get_string(), No_class->get_len()))
		C = find_class(C->get_parent());

	if (C->get_name()->equal_string(Object->get_string(), Object->get_len())) return false;
	else return true;  
}

method_class *ClassTable::method_env(Symbol Cname, Symbol f) { //method environment, return method named f of a class named Cname or all ancestors of Cname, null otherwise
	class__class *C = find_class(Cname);
	if (C == NULL) return NULL;
	Feature_class *feat = NULL;
	Features features = NULL;
	method_class *m = NULL;
	bool found = false;	
	while (!found && !C->get_parent()->equal_string(No_class->get_string(), No_class->get_len())) { //find method named f in the ancestors of C
		features = C->get_features(); 
		for(int i = features->first(); features->more(i); i = features->next(i)) {
			feat = features->nth(i);
			if (feat->type == 0) { //it is a method			    			
				m = (method_class *) feat;
				if (m->name->equal_string(f->get_string(), f->get_len())) {
					found = true; break; }
			}
    	}
		if (!found) C = find_class(C->get_parent());
	}
	if (C->get_name()->equal_string(Object->get_string(), Object->get_len())) {	//the last class is Object
		features = C->get_features(); 
		for(int i = features->first(); features->more(i); i = features->next(i)) {
			feat = features->nth(i);
			if (feat->type == 0) { //it is a method			    			
				m = (method_class *) feat;
				if (m->name->equal_string(f->get_string(), f->get_len())) {
					found = true; break; }
			}
		}
	}	
	if (found)
		return m;
	else 
		return NULL;
}

ClassTable::ClassTable(Classes classes) : semant_errors(0) , error_stream(cerr), inheritance_graph(Graph(classes->len()+6)) {
    /* Fill this in */  
	install_basic_classes();	

	//build the inheritance graph
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) { 
		class__class *child = (class__class *) classes->nth(i);			
		class__class *parent = NULL;
		int idx, jdx;	  			
		//if the parent class is a built-in class, find it in the inheritance graph
		if (child->get_parent()->equal_string(Object->get_string(), Object->get_len())) { 
			//store the vertex index of the built-in object	
			jdx = 0; parent = find_class(child->get_parent()); }
		else if	(child->get_parent()->equal_string(IO->get_string(), IO->get_len())) {
			jdx = 1; parent = find_class(child->get_parent()); }
		else if	(child->get_parent()->equal_string(Int->get_string(), Int->get_len())) {
			jdx = 2; parent = find_class(child->get_parent()); }
		else if	(child->get_parent()->equal_string(Bool->get_string(), Bool->get_len())) {
			jdx = 3; parent = find_class(child->get_parent()); }
		else if	(child->get_parent()->equal_string(Str->get_string(), Str->get_len())) {
			jdx = 4; parent = find_class(child->get_parent()); }
		else {	
			int j;
			for(j = classes->first(); classes->more(j); j = classes->next(j)) {	//get the vertex-index of the parent class and the class itself
				parent = (class__class *) classes->nth(j);	 
				if (child->get_parent()->equal_string(parent->get_name()->get_string(), parent->get_name()->get_len())) {
					jdx = j+5;
					/* skip built-in index object */ break; }
			}	
			if (parent->get_parent()->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) {
				jdx = j+5; 
 				parent = (class__class *) class_(SELF_TYPE, Object, nil_Features(), parent->get_filename()); }	//store a dummy SELF_TYPE class
		} 
		idx = i+5; 
		//printf("i: %d-idx: %d child: %s\tjdx: %d parent: %s\n", i, idx, child->name->get_string(), jdx, parent->name->get_string());
		Edge *e = new Edge(idx, jdx, (void *) child, (void *) parent);
		inheritance_graph.insert_edge(e); 
	} 
	//check for duplicate built-in classes
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		class__class *c = (class__class *) classes->nth(i);	
		if (c->get_name()->equal_string(Object->get_string(), Object->get_len()) ||
			c->get_name()->equal_string(IO->get_string(), IO->get_len()) ||
			c->get_name()->equal_string(Int->get_string(), Int->get_len()) ||
			c->get_name()->equal_string(Bool->get_string(), Bool->get_len()) ||
			c->get_name()->equal_string(Str->get_string(), Str->get_len()) )
			semant_error(c) << "Redefinition of basic class " <<  c->get_name() << "." << endl; 
	}
	//check for duplicate custom classes
	std::vector<Symbol> clss;
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		class__class *c1 = (class__class *) classes->nth(i);	
		if ( std::find(clss.begin(), clss.end(), c1->get_name() ) == clss.end() ) //if it is not present
			clss.push_back(c1->get_name());	
		else semant_error(c1) << "Class " <<  c1->get_name() << " was previously defined." << endl; 
	}
	//check for circular inheritance
	std::vector< std::vector <int> > *cycles = Graph::Tarjan::circuit_enumeration(inheritance_graph);	//using Tarjan algorithm to find cycles, return a vector of cycles
	std::vector<int> set; 	//store vertices index involved in a cycle
	for (unsigned int i = 0; i < cycles->size(); i++)
		for (unsigned int j = 0; j < cycles[i].size(); j++)
			if ( std::find(set.begin(), set.end(), (*cycles)[i][j] ) == set.end() ) //if the vertex is not present
				set.push_back((*cycles)[i][j]);  			
	for (unsigned int i = 0; i < set.size(); i++) {
		class__class *c = (class__class *) inheritance_graph.get_element(set[i]); //return element of the vertex index i	;
		semant_error(c) << "Class " << c->get_name() << ", or an ancestor of " << c->get_name() << ", is involved in an inheritance cycle." << endl; 	
	}
	delete cycles;	
}

void ClassTable::install_basic_classes() {

    // The tree package uses these globals to annotate the classes built below.
   // curr_lineno  = 0;
    Symbol filename = stringtable.add_string("<basic class>");
    
    // The following demonstrates how to create dummy parse trees to
    // refer to basic Cool classes.  There's no need for method
    // bodies -- these are already built into the runtime system.
    
    // IMPORTANT: The results of the following expressions are
    // stored in local variables.  You will want to do something
    // with those variables at the end of this method to make this
    // code meaningful.

    // 
    // The Object class has no parent class. Its methods are
    //        abort() : Object    aborts the program
    //        type_name() : Str   returns a string representation of class name
    //        copy() : SELF_TYPE  returns a copy of the object
    //
    // There is no need for method bodies in the basic classes---these
    // are already built in to the runtime system.

    Class_ Object_class =
	class_(Object, 
	       No_class,
	       append_Features(
			       append_Features(
					       single_Features(method(cool_abort, nil_Formals(), Object, no_expr())),
					       single_Features(method(type_name, nil_Formals(), Str, no_expr()))),
			       single_Features(method(copy, nil_Formals(), SELF_TYPE, no_expr()))),
	       filename);

    // 
    // The IO class inherits from Object. Its methods are
    //        out_string(Str) : SELF_TYPE       writes a string to the output
    //        out_int(Int) : SELF_TYPE            "    an int    "  "     "
    //        in_string() : Str                 reads a string from the input
    //        in_int() : Int                      "   an int     "  "     "
    //
    Class_ IO_class = 
	class_(IO, 
	       Object,
	       append_Features(
			       append_Features(
					       append_Features(
							       single_Features(method(out_string, single_Formals(formal(arg, Str)),
										      SELF_TYPE, no_expr())),
							       single_Features(method(out_int, single_Formals(formal(arg, Int)),
										      SELF_TYPE, no_expr()))),
					       single_Features(method(in_string, nil_Formals(), Str, no_expr()))),
			       single_Features(method(in_int, nil_Formals(), Int, no_expr()))),
	       filename);  

    //
    // The Int class has no methods and only a single attribute, the
    // "val" for the integer. 
    //
    Class_ Int_class =
	class_(Int, 
	       Object,
	       single_Features(attr(val, prim_slot, no_expr())),
	       filename);

    //
    // Bool also has only the "val" slot.
    //
    Class_ Bool_class =
	class_(Bool, Object, single_Features(attr(val, prim_slot, no_expr())),filename);

    //
    // The class Str has a number of slots and operations:
    //       val                                  the length of the string
    //       str_field                            the string itself
    //       length() : Int                       returns length of the string
    //       concat(arg: Str) : Str               performs string concatenation
    //       substr(arg: Int, arg2: Int): Str     substring selection
    //       
    Class_ Str_class =
	class_(Str, 
	       Object,
	       append_Features(
			       append_Features(
					       append_Features(
							       append_Features(
									       single_Features(attr(val, Int, no_expr())),
									       single_Features(attr(str_field, prim_slot, no_expr()))),
							       single_Features(method(length, nil_Formals(), Int, no_expr()))),
					       single_Features(method(concat, 
								      single_Formals(formal(arg, Str)),
								      Str, 
								      no_expr()))),
			       single_Features(method(substr, 
						      append_Formals(single_Formals(formal(arg, Int)), 
								     single_Formals(formal(arg2, Int))),
						      Str, 
						      no_expr()))),
	       filename);

	//find and store the positions of the built-in classes in the list
	int obj_classi = 0, io_classi = 1, int_classi = 2, bool_classi = 3, str_classi = 4;
	//cast to 'class__class' once when calling its methods
	class__class *IO_cl = (class__class *) IO_class;
	class__class *Int_cl = (class__class *) Int_class;
	class__class *Bool_cl = (class__class *) Bool_class;
	class__class *Str_cl = (class__class *) Str_class;
	class__class *Obj_cl = (class__class *) Object_class;

	//insert built-in objects in the inheritance graph
	Edge *e1 = new Edge(io_classi, obj_classi, (void *) IO_cl, (void *) Obj_cl);
	Edge *e2 = new Edge(int_classi, obj_classi, (void *) Int_cl, (void *) Obj_cl);
	Edge *e3 = new Edge(bool_classi, obj_classi, (void *) Bool_cl, (void *) Obj_cl);
	Edge *e4 = new Edge(str_classi, obj_classi, (void *) Str_cl, (void *) Obj_cl);

	inheritance_graph.insert_edge(e1);
	inheritance_graph.insert_edge(e2);
	inheritance_graph.insert_edge(e3);
	inheritance_graph.insert_edge(e4); 
}

////////////////////////////////////////////////////////////////////
//
// semant_error is an overloaded function for reporting errors
// during semantic analysis.  There are three versions:
//
//    ostream& ClassTable::semant_error()                
//
//    ostream& ClassTable::semant_error(Class_ c)
//       print line number and filename for `c'
//
//    ostream& ClassTable::semant_error(Symbol filename, tree_node *t)  
//       print a line number and filename
//
///////////////////////////////////////////////////////////////////

ostream& ClassTable::semant_error(Class_ c)
{                                                             
    return semant_error(c->get_filename(),c);
}    

ostream& ClassTable::semant_error(Symbol filename, tree_node *t)
{
    error_stream << filename << ":" << t->get_line_number() << ": ";
    return semant_error();
}

ostream& ClassTable::semant_error()                  
{                                                 
    semant_errors++;                            
    return error_stream;
} 



/*   This is the entry point to the semantic checker.

     Your checker should do the following two things:

     1) Check that the program is semantically correct
     2) Decorate the abstract syntax tree with type information
        by setting the `type' field in each Expression node.
        (see `tree.h')

     You are free to first do 1), make sure you catch all semantic
     errors. Part 2) can be done in a second stage, when you want
     to build mycoolc.
 */
void program_class::semant()
{	
    initialize_constants();	
    /* ClassTable constructor may do some semantic analysis */ 
    ClassTable *classtable = new ClassTable(classes); 
/*	for (int i = 0; i < classtable->inheritance_graph.get_num_vertex(); i++) {
		class__class *c =  (class__class *) classtable->inheritance_graph.get_element(i);
		printf("vx: %s\n", c->name->get_string()); }
*/
    /* some semantic analysis code may go here */
	//check for illegal class inheritance 
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {	
		classtable->class_env = (class__class *) classes->nth(i);	
		if (classtable->class_env->get_parent()->equal_string(Int->get_string(), Int->get_len()))
//		if (!strcmp(classtable->class_env->get_parent()->get_string(), Int->get_string()))
			classtable->semant_error(classtable->class_env) << "Class " << classtable->class_env->get_name() << " cannot inherit class Int." << endl;

		else if (classtable->class_env->get_parent()->equal_string(Str->get_string(), Str->get_len()))
//		else if (!strcmp(classtable->class_env->get_parent()->get_string(), Str->get_string()))
			classtable->semant_error(classtable->class_env) << "Class " << classtable->class_env->get_name() << " cannot inherit class String." << endl;

		else if (classtable->class_env->get_parent()->equal_string(Bool->get_string(), Bool->get_len()))
//		else if (!strcmp(classtable->class_env->get_parent()->get_string(), Bool->get_string()))
			classtable->semant_error(classtable->class_env) << "Class " << classtable->class_env->get_name() << " cannot inherit class Bool." << endl;

		else if (classtable->class_env->get_parent()->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
//		else if (!strcmp(classtable->class_env->get_parent()->get_string(), SELF_TYPE->get_string()))
			classtable->semant_error(classtable->class_env) << "Class " << classtable->class_env->get_name() << " cannot inherit class SELF_TYPE." << endl;

		else if (classtable->class_env->get_name()->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
//		else if (!strcmp(classtable->class_env->get_name()->get_string(), SELF_TYPE->get_string()))
			classtable->semant_error(classtable->class_env) << "Redefinition of basic class SELF_TYPE." << endl;

		else if (classtable->find_class(classtable->class_env->get_parent()) == NULL)
			classtable->semant_error(classtable->class_env) << "Class " << classtable->class_env->get_name() << " inherits from an undefined class " << classtable->class_env->get_parent() << "." << endl;
	}

	//check for main class and main method
	bool main = false;
	bool mmain = false;
	method_class *m = NULL;
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		classtable->class_env = (class__class *) classes->nth(i);	
		if (classtable->class_env->get_name()->equal_string(Main->get_string(), Main->get_len())) {
			main = true;	//found main method
			Features features = classtable->class_env->get_features(); 
			for(int j = features->first(); features->more(j); j = features->next(j)) {
				Feature_class *feat = features->nth(j);
				if (feat->type == 0) { //it is a method			    			
					m = (method_class *) feat;
					if (m->name->equal_string(main_meth->get_string(), main_meth->get_len())) {
						if (m->formals->len() > 0) classtable->semant_error(classtable->class_env) << "'main' method in class Main should have no arguments." << endl;
						mmain = true; break; 
					}	
				}
			}
			break; //found main class, exit loop
		}
	}
	if (main == false) 	classtable->semant_error() << "Class Main is not defined." << endl;
	else if (mmain == false) classtable->semant_error(classtable->class_env) << "No 'main' method in class " << classtable->class_env->get_name() << endl;
    if (classtable->errors()) {
		delete classtable;
		cerr << "Compilation halted due to static semantic errors." << endl;
		exit(1);
    }
	//check that the method and attribute names of a class are uniques
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		class__class *c = (class__class *) classes->nth(i);	
		Features features = c->get_features(); 
		std::vector<Symbol> methods;
		std::vector<Symbol> attrs; //collection of attributes of the same class
		for(int j = features->first(); features->more(j); j = features->next(j)) {
			Feature_class *feat = features->nth(j);
			if (feat->type == 0) { //it is a method
				method_class *m = (method_class *) feat;
				if ( std::find(methods.begin(), methods.end(), m->name ) == methods.end() ) //if it is not present
					methods.push_back(m->name);	
				else classtable->semant_error(c) << "Method "<< m->name << " is multiply defined." << endl;
				std::vector<Symbol> forms; //collection of formal parameters of the current method
				//check formal parameters of the current method are unique
				for(int l = m->formals->first(); m->formals->more(l); l = m->formals->next(l)) {
					formal_class *f = (formal_class *) m->formals->nth(l);
					if ( std::find(forms.begin(), forms.end(), f->name ) == forms.end() ) {//if it is not present
						forms.push_back(f->name);
						if (f->name->equal_string(self->get_string(), self->get_len()) ) 	
							classtable->semant_error(c) << "'self' cannot be the name of a formal parameter." << endl; 
						if (f->type_decl->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()) ) 	
							classtable->semant_error(c) << "Formal parameter " << f->name << " cannot have type SELF_TYPE." << endl; 
					}
					else classtable->semant_error(c) << "Formal parameter " << f->name << " is multiply defined." << endl; 
				}	
			}
			else if (feat->type == 1) { //it is an attribute
				attr_class *a = (attr_class *) feat;
				if (a->name->equal_string(self->get_string(), self->get_len()))
					classtable->semant_error(c) << "'self' cannot be the name of an attribute." << endl;
				else if ( std::find(attrs.begin(), attrs.end(), a->name ) == attrs.end() ) { //if it is not present in the same class
					//check if the attribute is already defined in the ancestor classes
					std::vector<class__class *> *ancestors = classtable->get_ancestors(c->name); //printf("cname: %s\n", c->name->get_string());
					bool found = false;
					for (unsigned  int k = 0; k < ancestors->size(); k++) {	//printf("ancestors k:%s\n", (*ancestors)[k]->name->get_string());
						class__class *canc = (*ancestors)[k];
						Features featuresanc = canc->get_features(); 
						for(int h = featuresanc->first(); featuresanc->more(h); h = featuresanc->next(h)) {
							Feature_class *featanc = featuresanc->nth(h);
							if (featanc->type == 1) { //it is an attribute
								attr_class *ainh = (attr_class *) featanc;
								if ( ainh->name->equal_string(a->name->get_string(), a->name->get_len()) ) {  //if it is present		
									classtable->semant_error(c) << "Attribute " << a->name << " is an attribute of an inherited class." << endl;
									found = true;								
								}
							}
			   			}
					}
					delete ancestors;
					if (!found) attrs.push_back(a->name);	
				}
				else classtable->semant_error(c) << "Attribute " << a->name << " is multiply defined in class." << endl;
			}
		}
	}
	SymbolTable<Symbol, void *> *symtab = new SymbolTable<Symbol, void *>(); 
	type_check(symtab, classtable);
	classtable->inheritance_graph.removeAll(); 
	delete symtab;
    if (classtable->errors()) {
		delete classtable;
		cerr << "Compilation halted due to static semantic errors." << endl;
		exit(1);
    } 
	delete classtable;	
}

Symbol class__class::get_parent() { return parent; }
Symbol class__class::get_name() { return name; }
Features class__class::get_features() { return features; }
/*
void program_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		classes->nth(i)->traverse(symtab, ctab);	
}

void class__class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {
	symtab->enterscope();
	for(int i = features->first(); features->more(i); i = features->next(i)) {
		features->nth(i)->traverse(symtab, ctab);
	symtab.exitscope();
}

void method_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {
	for(int i = formals->first(); formals->more(i); i = formals->next(i)) {
		formals->nth(i)->traverse(symtab, ctab);
	expr->traverse(symtab, ctab);
}

void attr_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {
	init->traverse(symtab, ctab);
}

void formal_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void branch_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void assign_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void static_dispatch_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void dispatch_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void cond_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void loop_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void typcase_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void block_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void let_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void plus_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void sub_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void mul_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void divide_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void neg_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void lt_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void eq_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void leq_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void comp_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void int_const_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void bool_const_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void string_const_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void new__class_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void isvoid_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void no_expr_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
void object_class::traverse(SymbolTable<Symbol, void *> *, ClassTable *) {}
*/
Symbol program_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
		class__class *c = (class__class *) classes->nth(i);	
		c->type_check(symtab, ctab);
	}
	return No_type;
}         

Symbol class__class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	ctab->class_env = this;	//printf("curr class: %s\n", ctab->class_env->name->get_string());
	symtab->enterscope();
	Features features = ctab->class_env->get_features(); 
	for(int i = features->first(); features->more(i); i = features->next(i)) {
		Feature_class *feat = features->nth(i);
		feat->type_check(symtab, ctab);
	}
	symtab->exitscope();
	return No_type;
} 

/*
	M(C, f) = (T1 , . . . , Tn , T0)
	0C[SELF_TYPEC / self][T1 / x1 ] . . . [Tn / xn ], M, C |- e : T0'
	T0' ≤ SELF_TYPEC if T0 = SELF TYPE
	T0' ≤ T0 otherwise
	-----------------------------------------------------------------
	OC , M, C |- f(x1 : T1 , . . . , xn : Tn ) : T0 { e };
*/
Symbol method_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T0 = return_type;
	if (ctab->find_class(T0) == NULL && !T0->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
		ctab->semant_error(ctab->class_env) << "Undefined return type " << T0 << " in method " << name << "." << endl;
	
	method_class *def = ctab->method_env(ctab->class_env->parent, name); //find the method in parent classes

	if (def != NULL) { 		
		if (def->formals->len() != formals->len()) {
			ctab->semant_error(ctab->class_env) << "Incompatible number of formal parameters in redefined method " << name << "." << endl;
			return No_type;
		}
	}
	//symtab->addid(name, (void **) return_type);
	symtab->enterscope();
	Symbol *T = new Symbol[formals->len()];
	int j = 0;
	for(int i = formals->first(); formals->more(i); i = formals->next(i))  //insert method parameters into the symbol table and store their type
		T[j++] = formals->nth(i)->type_check(symtab, ctab);

	if (def != NULL) {	//method definition found in an ancestor class, now check that the redefinition method has the same type (formal parameters type and return type)
		if (!return_type->equal_string(def->return_type->get_string(), def->return_type->get_len()))  
			ctab->semant_error(ctab->class_env) << "In redefined method " << name << " , return type " << return_type << " is different from original return type " << def->return_type << "." << endl;
		j = 0;
		Symbol Tdef;
		for(int i = def->formals->first(); def->formals->more(i); i = def->formals->next(i)) { //insert method parameters into the symbol table and store their type
			Tdef = ((formal_class *) def->formals->nth(i))->type_decl;
			if (!T[j]->equal_string(Tdef->get_string(), Tdef->get_len())) {
				ctab->semant_error(ctab->class_env) << "In redefined method " << name << " , parameter type " << T[i]  << " is different from original type " << Tdef << "." << endl;
				break; //return TreeConstants.No_type;
			}
 			j++;
	    }
	}
	Symbol T0prime = expr->type_check(symtab, ctab);  //declared type return is SELF_TYPE and the type of the returned expression is not SELF_TYPE (objectid is not self)
	if (T0->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()) && !T0prime->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()) ) 
		ctab->semant_error(ctab->class_env) << "Inferred return type " << T0prime << " of method " << name << " does not conform to declared return type "<< T0 << "." << endl;
	else if (!ctab->sub(T0prime, T0))
		ctab->semant_error(ctab->class_env) << "Inferred return type " << T0prime << " of method " << name << " does not conform to declared return type "<< T0 << "." << endl;
	symtab->exitscope();
	delete [] T;
	return No_type;
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
Symbol attr_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T0 = type_decl;
	Symbol T1 = init->type_check(symtab, ctab);
	symtab->addid(name, (void **) type_decl);
	if (!T1->equal_string(No_type->get_string(), No_type->get_len()) ) { // if initialization
		if (!ctab->sub(T1, T0)) {	
			if (!T1->equal_string(self->get_string(), self->get_len()))
				ctab->semant_error(ctab->class_env) << "Inferred type SELF_TYPE of initialization of attribute " << name << " does not conform to declared type "<< T0 << "." << endl;
			else ctab->semant_error(ctab->class_env) << "Inferred type " << T1 << " of initialization of attribute " << name << " does not conform to declared type "<< T0 << "." << endl;
		}
	}
	return No_type;
} 

Symbol formal_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	symtab->addid(name, (void **) type_decl);
	return type_decl;
} 

Symbol branch_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	symtab->enterscope();
	symtab->addid(name, (void **) type_decl);
	Symbol T = expr->type_check(symtab, ctab);
	symtab->exitscope();
	return T;
} 
/*
	O(Id) = T
	O, M, C |- e1 : T'
	T' ≤ T
	--------------------------
	O, M, C |- Id ← e1 : T'
*/
Symbol assign_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T = (Symbol) symtab->lookup(name); 
	if (name->equal_string(self->get_string(), self->get_len())) {
		ctab->semant_error(ctab->class_env) << "Cannot assign to 'self'." << endl;
		T = Object;
	}
	else if (T == NULL) {	//check if the variable is defined in any scope
		Features features = ctab->class_env->get_features(); 
		for(int i = features->first(); features->more(i); i = features->next(i)) {
			Feature_class *feat = features->nth(i);
			//if there isn't a variable in all scopes, it is possible that it is defined after the current feature (a method), check all attributes in the current class
		    if (feat->type == 1 && ((attr_class *) feat)->name->equal_string(name->get_string(), name->get_len()) ) {
				T = ((attr_class *) feat)->type_decl; break;				
			}
		}
		if (T == NULL) {
			std::vector<class__class *> *ancestors = ctab->get_ancestors(ctab->class_env->name);
			for (unsigned int j = 0; j < ancestors->size(); j++) {	//find object id in the ancestor classes
				class__class *c = (*ancestors)[j];
				Features features = c->get_features(); 
				for(int i = features->first(); features->more(i); i = features->next(i)) {
					Feature_class *feat = features->nth(i);
					//if there isn't a variable in all scopes, it is possible that it is defined after the current feature (a method), check all attributes in the current class
					if (feat->type == 1 && ((attr_class *) feat)->name->equal_string(name->get_string(), name->get_len()) ) {
						T = ((attr_class *) feat)->type_decl; break;				
					}
				}
			}
			delete ancestors;	
			if (T == NULL) {
				ctab->semant_error(ctab->class_env) << "Assignment to undeclared variable " << name << "." << endl;
				T = Object;
			}
		}
	}
	Symbol Tprime = expr->type_check(symtab, ctab);	//printf("T: %s\tTprime: %s\n", T->get_string(), Tprime->get_string());
	if (!ctab->sub(Tprime, T)) {
		ctab->semant_error(ctab->class_env) << "Type " << Tprime << " of assigned expression does not conform to declared type " << T << " of identifier " << name << "." << endl;
		//this.set_type(TreeConstants.Object_);
		//return TreeConstants.Object_;
	}
	this->set_type(Tprime);
	return Tprime;
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
Symbol static_dispatch_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *Ta = new Symbol[actual->len()];
	Symbol T0 = expr->type_check(symtab, ctab);
	Symbol T = type_name;
	if (T->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
		ctab->semant_error(ctab->class_env) << "Static dispatch to SELF_TYPE." << endl;
	if (!ctab->sub(T0, T))
		ctab->semant_error(ctab->class_env) << "Expression type" << T0 << " does not conform to declared static dispatch type " << T << "." << endl;
	//find the calling method in the respective class T
	method_class *m = ctab->method_env(T, name);
	if (m == NULL)	//method not found
		ctab->semant_error(ctab->class_env) << "Static dispatch to undefined method " << name << "." << endl;
	else { //method found
		if (m->formals->len() != actual->len())
			ctab->semant_error(ctab->class_env) << "Static dispatch method " << name << " called with wrong number of arguments" << endl;
		else { 
			int i = 0;	//check type of dynamic parameters
			for(int j = actual->first(); actual->more(j); j = actual->next(j)) {
				Expression_class *e = actual->nth(j);
				Ta[i++] = e->type_check(symtab, ctab);
			}
			i = 0;	//store static parameters
			Formal *Tprime = new Formal[m->formals->len()];
			for(int j = m->formals->first(); m->formals->more(j); j = m->formals->next(j)) 
				Tprime[i++] = m->formals->nth(j);
			Symbol Tn1prime = m->return_type;

			for (i = 0; i < actual->len(); i++)	//check that each dynamic type parameter is a subtype of the static type parameter
				if (!ctab->sub(Ta[i], ((formal_class *) Tprime[i])->type_decl))
					ctab->semant_error(ctab->class_env) << "In call of method" << m->name <<", type " << ((formal_class *) Tprime[i])->type_decl << " of parameter " << ((formal_class *) Tprime[i])->name << " does not conform to declared type " << Ta[i]  << "." << endl;				
			delete [] Tprime;
			delete [] Ta;
			Symbol Tn1 = NULL;
			if (Tn1prime->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
				Tn1 = T0;
			else Tn1 = Tn1prime;
			this->set_type(Tn1);
			return Tn1;
		}
	}
	delete [] Ta;
	this->set_type(Object);
	return Object;
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
Symbol dispatch_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[actual->len()];
	Symbol T0 = expr->type_check(symtab, ctab);	
	Symbol T0prime = NULL;

	if (T0->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
		T0prime = ctab->class_env->get_name();
	else T0prime = T0;
	//find the calling method in the respective class T
	method_class *m = ctab->method_env(T0prime, name);
	if (m == NULL)	//method not found
		ctab->semant_error(ctab->class_env) << "Dispatch to undefined method " << name << "." << endl;
	else { //method found
		if (m->formals->len() != actual->len())
			ctab->semant_error(ctab->class_env) << "Method " << name << " called with wrong number of arguments" << endl;
		else { 
			int i = 0;	//check type of dynamic parameters
			for(int j = actual->first(); actual->more(j); j = actual->next(j)) {
				Expression_class *e = actual->nth(j);
				T[i++] = e->type_check(symtab, ctab);
			}
			i = 0;	//store static parameters
			Formal *Tprime = new Formal[m->formals->len()];
			for(int j = m->formals->first(); m->formals->more(j); j = m->formals->next(j)) 
				Tprime[i++] = m->formals->nth(j);
			Symbol Tn1prime = m->return_type;

			for (i = 0; i < actual->len(); i++)	{//check that each dynamic type parameter is a subtype of the static type parameter
				//printf("%s\n", ctab->sub(T[i], ((formal_class *) Tprime[i])->type_decl));
				if (!ctab->sub(T[i], ((formal_class *) Tprime[i])->type_decl))
					ctab->semant_error(ctab->class_env) << "In call of method " << m->name <<", type " << T[i] << " of parameter " << ((formal_class *) Tprime[i])->name << " does not conform to declared type " << ((formal_class *) Tprime[i])->type_decl << "." << endl;	 }			
			delete [] T;
			delete [] Tprime;
			Symbol Tn1 = NULL;
			if (Tn1prime->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
				Tn1 = T0;
			else Tn1 = Tn1prime;
			this->set_type(Tn1);
			return Tn1;	
		}
	}
	delete [] T;
	this->set_type(Object);
	return Object;
} 
/*	assign type rule
	O, M, C |- e1 : Bool
	O, M, C |- e2 : T2
	O, M, C |- e3 : T3
	------------------------------------------------
	O, M, C |- if e1 then e2 else e3 fi : lub(T2, T3)
*/
Symbol cond_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	Symbol T0 = pred->type_check(symtab, ctab);
	if (!T0->equal_string(Bool->get_string(), Bool->get_len()))
		ctab->semant_error(ctab->class_env) << "Predicate of 'if' does not have type Bool." << endl;
	T[0] = then_exp->type_check(symtab, ctab);
	T[1] = else_exp->type_check(symtab, ctab);	//printf("T[0]: %s\tT[1]: %s\n", T[0]->get_string(), T[1]->get_string());
	Symbol lub = ctab->lub(T[0], T[1])->get_name();	
	this->set_type(lub);
	delete [] T;
	return lub;
} 
/*
	O, M, C |- e1 : Bool
	O, M, C |- e2 : T2
	------------------------------------------
	O, M, C |- while e1 loop e2 pool : Object
*/
Symbol loop_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T0 = pred->type_check(symtab, ctab);
	if (!T0->equal_string(Bool->get_string(), Bool->get_len()))
		ctab->semant_error(ctab->class_env) << "Predicate of 'while' does not have type Bool." << endl;
	Symbol T1 = body->type_check(symtab, ctab);
	this->set_type(Object);
	return Object;
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
Symbol typcase_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	std::vector<Symbol> set; 
	//check for duplicate branch type in case expression
	for(int i = cases->first(); cases->more(i); i = cases->next(i)) {
		branch_class *b = (branch_class *) cases->nth(i);
		if ( std::find(set.begin(), set.end(), b->type_decl ) == set.end() ) //if it is not present			
			set.push_back(b->type_decl);			
		else ctab->semant_error(ctab->class_env) << "Duplicate branch " << b->type_decl << " in case statement." << endl;
	}

	Symbol T0 = expr->type_check(symtab, ctab);
	Symbol *T = new Symbol[cases->len()+1];
	int i = 0;
	for(int j = cases->first(); cases->more(j); j = cases->next(j)) 
		T[i++] = cases->nth(j)->type_check(symtab, ctab);
	T[i] = NULL;
	Symbol lub = ctab->lub(T)->get_name();
	this->set_type(lub);
	delete [] T;
	return lub;
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
Symbol block_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[body->len()];
	int i = 0;
	for(int j = body->first(); body->more(j); j = body->next(j)) 
    	T[i++] = body->nth(j)->type_check(symtab, ctab);
	this->set_type(T[body->len()-1]);
	return T[body->len()-1];
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
Symbol let_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T0 = type_decl;
	Symbol T1 = NULL;
	Symbol T2 = NULL;
	Symbol T0prime;

	if (identifier->equal_string(self->get_string(), self->get_len())) {
		ctab->semant_error(ctab->class_env) << "'self' cannot be bound in a 'let' expression." << endl;
		T0 = Object;
	}
	T0prime = T0;
	T1 = init->type_check(symtab, ctab);
	symtab->enterscope();
	symtab->addid(identifier, (void **) T0);
	if (!T1->equal_string(No_type->get_string(), No_type->get_len())) { //if initialization
		if (!ctab->sub(T1, T0prime))
			ctab->semant_error(ctab->class_env) << "Inferred type" << T1 << "  of initialization of " << identifier << " does not conform to identifier's declared type " << T0prime << "." << endl;
	}
	T2 = body->type_check(symtab, ctab);
	symtab->exitscope();
	this->set_type(T2);
	return T2;
} 
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {∗, +, −, /}
	--------------------------
	O, M, C |- e1 op e2 : Int
*/
Symbol plus_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " + " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Int);
	return Int;
} 
Symbol sub_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " - " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Int);
	return Int;
} 
Symbol mul_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " * " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Int);
	return Int;
} 
Symbol divide_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " / " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Int);
	return Int;
} 
/*
	O, M, C |- e1 : Int
	--------------------
	O, M, C |- ~e1 : Int
*/
Symbol neg_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T = e1->type_check(symtab, ctab);
	if (!T->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "Argument of '~' has type" << T << " instead of Int." << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Int);
	return Int;
} 
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {<, ≤}
	---------------------------
	O, M, C |- e1 op e2 : Bool
*/
Symbol lt_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " < " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Bool);
	return Bool;
} 
/*
	O, M, C |- e1 : T1
	O, M, C |- e2 : T2
	T1 ∈ {Int, String, Bool} V T2 ∈ {Int, String, Bool} ⇒ T1 = T2
	--------------------------------------------------------------	
	O, M, C |- e1 = e2 : Bool
*/
Symbol eq_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T1 = e1->type_check(symtab, ctab);
	Symbol T2 = e2->type_check(symtab, ctab);
	if ((T1->equal_string(Int->get_string(), Int->get_len()) && !T2->equal_string(Int->get_string(), Int->get_len())) || (!T1->equal_string(Int->get_string(), Int->get_len()) && T2->equal_string(Int->get_string(), Int->get_len())) || 
		(T1->equal_string(Str->get_string(), Str->get_len()) && !T2->equal_string(Str->get_string(), Str->get_len())) || (!T1->equal_string(Str->get_string(), Str->get_len()) && T2->equal_string(Str->get_string(), Str->get_len())) ||
		(T1->equal_string(Bool->get_string(), Bool->get_len()) && !T2->equal_string(Bool->get_string(), Bool->get_len())) || (!T1->equal_string(Bool->get_string(), Bool->get_len()) && T2->equal_string(Bool->get_string(), Bool->get_len()))) {
		ctab->semant_error(ctab->class_env) << "Illegal comparison with a basic type." << endl;
		//this.set_type(TreeConstants.Object_);
		//return TreeConstants.Object_;
	}	
	this->set_type(Bool);
	return Bool;
} 
/*
	O, M, C |- e1 : Int
	O, M, C |- e2 : Int
	op ∈ {<, ≤}
	---------------------------
	O, M, C |- e1 op e2 : Bool
*/
Symbol leq_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol *T = new Symbol[2];
	T[0] = e1->type_check(symtab, ctab);
	T[1] = e2->type_check(symtab, ctab);
	if (!T[0]->equal_string(Int->get_string(), Int->get_len()) || !T[1]->equal_string(Int->get_string(), Int->get_len())) {
		ctab->semant_error(ctab->class_env) << "non-Int arguments: "<< T[0] << " <= " << T[1] << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Bool);
	return Bool;
} 
/*
	O, M, C |- e1 : Bool
	----------------------
	O, M, C |- ¬e1 : Bool
*/
Symbol comp_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	Symbol T = e1->type_check(symtab, ctab);
	if (!T->equal_string(Bool->get_string(), Bool->get_len())) {
		ctab->semant_error(ctab->class_env) << "Argument of 'not' has type " << T << " instead of Bool." << endl;
		this->set_type(Object);
		return Object;
	}
	this->set_type(Bool);
	return Bool;
} 

Symbol int_const_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	this->set_type(Int);
	return Int;
} 

Symbol bool_const_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	this->set_type(Bool);
	return Bool;
} 

Symbol string_const_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	this->set_type(Str);
	return Str;
} 
/*
	T' = SELF_TYPEC if T = SELF TYPE
	T' = T otherwise
	------------------------------------
	O, M, C |- new T : Tprime
*/
Symbol new__class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	class__class  *c = ctab->find_class(type_name);
	if (c == NULL && !type_name->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len())) 		//undefined class
		ctab->semant_error(ctab->class_env) << "'new' used with undefined class " << type_name << "." << endl;

	Symbol Tprime = NULL;
	Symbol T = type_name;
	if (T->equal_string(SELF_TYPE->get_string(), SELF_TYPE->get_len()))
		Tprime = SELF_TYPE;
	else Tprime = T;
	this->set_type(Tprime);
	return Tprime;
} 

Symbol isvoid_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	e1->type_check(symtab, ctab);
	this->set_type(Bool);
	return Bool;
} 

Symbol no_expr_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	this->set_type(No_type);
	return No_type;
} 
/*
	O(Id) = T
	-----------------
	O, M, C |- Id : T
*/
Symbol object_class::type_check(SymbolTable<Symbol, void *> *symtab, ClassTable *ctab) { 
	if (name->equal_string(self->get_string(), self->get_len())) {
		this->set_type(SELF_TYPE);
		return SELF_TYPE;
	}
	Symbol T = (Symbol) symtab->lookup(name);
	if (T == NULL) { 		
	//forward reference
		Features features = ctab->class_env->get_features(); 
		for(int i = features->first(); features->more(i); i = features->next(i)) {
			Feature_class *feat = features->nth(i);
			if (feat->type == 1 && ((attr_class *) feat)->name->equal_string(name->get_string(), name->get_len())) { //it is an attribute			    			
				T = ((attr_class *) feat)->type_decl; break;
			}
	   	}
		if (T == NULL) {
			std::vector<class__class *> *ancestors = ctab->get_ancestors(ctab->class_env->name);
			for (unsigned int j = 0; j < ancestors->size(); j++) {	//find object id in the ancestor classes
				class__class *c = (*ancestors)[j];
				Features features = c->get_features(); 
				for(int i = features->first(); features->more(i); i = features->next(i)) {
					Feature_class *feat = features->nth(i);
					if (feat->type == 1 && ((attr_class *) feat)->name->equal_string(name->get_string(), name->get_len())) { //it is an attribute			    			
						T = ((attr_class *) feat)->type_decl; break; 
					}					 
				}
			}
			delete ancestors;
			if (T == NULL) {	//no object id found
				ctab->semant_error(ctab->class_env) << "Undeclared identifier " << name << "." << endl;
				this->set_type(Object);
				return Object;
			}
		}
	}
	this->set_type(T);
	return T;
} 
