#ifndef SEMANT_H_
#define SEMANT_H_

#include <assert.h>
#include <iostream>  
#include "cool-tree.h"
#include "stringtab.h"
#include <symtab.h>
#include "list.h"
#include "graph.h"

#define TRUE 1
#define FALSE 0

class ClassTable;
typedef ClassTable *ClassTableP;

// This is a structure that may be used to contain the semantic
// information such as the inheritance graph.  You may use it or not as
// you like: it is only here to provide a container for the supplied
// methods.

class ClassTable {
private:
  int semant_errors;
  void install_basic_classes();
  ostream& error_stream;

public:
  ClassTable(Classes);
  int errors() { return semant_errors; }
  ostream& semant_error();
  ostream& semant_error(Class_ c);
  ostream& semant_error(Symbol filename, tree_node *t);
	Graph inheritance_graph;
	class__class *find_class(Symbol);
	class__class *class_env;
	std::vector<class__class *> *get_ancestors(Symbol);
	int depth(class__class *);
	class__class *lub(Symbol, Symbol);
	class__class *lub(Symbol *);
	bool sub(Symbol, Symbol);
	method_class *method_env(Symbol Cname, Symbol f);
};


#endif

