#include "graph.h"
#include <stdio.h>
#include <iostream>
#include <stdexcept>   

Graph::Graph(int nv) : n_vertex(nv), is_direct(true), adj(n_vertex, std::vector<Edge *>(n_vertex)) {
}

Graph::Graph(int nv, bool v): n_vertex(nv), is_direct(v), adj(n_vertex, std::vector<Edge *>(n_vertex)) {
}

void Graph::check_index(Edge *e) {
	int i = e->i_origin;
	int j = e->i_destination; 
	if (i >= n_vertex || i < 0 || j >= n_vertex || j < 0)
		throw out_of_range("vertex index out of range!");
}

void Graph::check_index(int i) {
	if (i >= n_vertex || i < 0)
		throw out_of_range("vertex index out of range!");
}

void Graph::insert_edge(Edge *e) {
	check_index(e);
	int i = e->i_origin;
	int j = e->i_destination; 
	if (adj[i][j] == nullptr) {
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
	if (adj[i][j] != nullptr) { 
		adj[i][j] = nullptr; 
		n_edge--;
		if (!is_direct) {
			adj[j][i] = nullptr;
			n_edge--;
		}
	}
}

void Graph::removeAll() {
	for (int i = 0; i < n_vertex; i++)
		for (int j = 0; j < n_vertex; j++) 
			if(adj[i][j] != nullptr) { 
				adj[i][j] = nullptr;
				delete adj[i][j];
			}
}

void Graph::print() {	
	for (int i = 0; i < n_vertex; i++)
		for (int j = 0; j < n_vertex; j++) {
			if(adj[i][j] != nullptr) 
				cout << i << ": " << *(static_cast<string *>(adj[i][j]->el_origin)) << " - " << j << ": " << *(static_cast<string *>(adj[i][j]->el_destination)) << endl; }
}

vector<Edge *> Graph::out_edges(int v) {
	check_index(v);
	vector<Edge*> oe;
	for (int j = 0; j < n_vertex; j++)
		if (adj[v][j] != nullptr)
			 oe.push_back(adj[v][j]);
	return oe;
}

vector<Edge *> Graph::in_edges(int v) {
	check_index(v);
	std::vector<Edge*> oe;
	for (int i = 0; i < n_vertex; i++)
		if (adj[i][v] != nullptr)
			 oe.push_back(adj[i][v]);
	return oe;
}

int Graph::get_num_vertex() {
	return n_vertex;
}

int Graph::get_num_edges() {
	return n_edge;
}

vector< vector<int> > *Graph::Tarjan::circuit_enumeration(Graph g) {
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
bool Graph::Tarjan::backtrack(Graph *graph, int s, int v, vector<bool> *marked, stack<int> *markedS, stack<int> *pointS, vector< vector<int> > *cycles) {
	pointS->push(v);
	(*marked)[v] = true;
	markedS->push(v);
	bool f = false;

	for (Edge *e : graph->out_edges(v)) { 
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

/*
int main(void) {
	Graph g = Graph(5);
	void *s0 = static_cast<void *>(new string("1"));
	void *s1 = static_cast<void *>(new string("2"));
	void *s2 = static_cast<void *>(new string("3"));
	void *s3 = static_cast<void *>(new string("4"));
	void *s4 = static_cast<void *>(new string("5"));
	void *s5 = static_cast<void *>(new string("6"));
	void *s6 = static_cast<void *>(new string("7"));
	
	Edge *e1 = new Edge(0, 1, s0, s1);
	Edge *e2 = new Edge(0, 3, s0, s3);
	Edge *e3 = new Edge(1, 2, s1, s2);
	Edge *e4 = new Edge(1, 3, s1, s3);
	Edge *e5 = new Edge(2, 3, s2, s3);
	Edge *e6 = new Edge(3, 4, s3, s4);
	Edge *e7 = new Edge(4, 0, s4, s0);

	try {
		g.insert_edge(e1);
		g.insert_edge(e2);
		g.insert_edge(e3);
		g.insert_edge(e4);
		g.insert_edge(e5);
		g.insert_edge(e6);
		g.insert_edge(e7);
	}
	catch (const out_of_range& e) {
		cerr << e.what() << endl;
	}

	g.print();

	//for (Edge *e : g.in_edges(3)) {
	//	cout << e->i_origin << ": " << *(static_cast<string *>(e->el_origin)) << " - " << e->i_destination << ": " << *(static_cast<string *>(e->el_destination)) << endl; 
	//}
	vector< vector<int> > *loops = Graph::Tarjan::circuit_enumeration(g);
	for (vector<int> v : *loops) {
		for (int i : v)
			cout << i+1 << ", ";
		cout << endl; 
	}
	g.print();


	delete loops;
	g.removeAll();
	delete static_cast<string *>(s0);
	delete static_cast<string *>(s1);
	delete static_cast<string *>(s2);
	delete static_cast<string *>(s3);
	delete static_cast<string *>(s4);
	delete static_cast<string *>(s5);
	delete static_cast<string *>(s6);
}*/
