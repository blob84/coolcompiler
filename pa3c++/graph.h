#include <vector>
#include "edge.h"
#include <stack>

//using namespace std;

class Graph {
	private:
		bool is_direct;	//direct graph?
		int n_edge; //number of edges
		int n_vertex; //number of vertex
		std::vector< std::vector<Edge *> > adj; //adjacency matrix
		void check_index(Edge *); //check vertex index of edges
		void check_index(int); //check vertex index
	public:
		Graph(int);
		Graph(int, bool);
		void insert_edge(Edge *);
		void remove_edge(Edge *); //remove edge without free memory
		void removeAll(); //remove all edges from the matrix and free edges memory
		void print(); //print vertex data of edges
		std::vector<Edge *> out_edges(int); //outging edges of vertex
		std::vector<Edge *> in_edges(int); //ingoing edges of vertex
		int get_num_vertex(); 
		int get_num_edges();
		void *get_element(int); //return element of vertex v
		class Tarjan {	//Tarjan algorithm: find all simple cycles in a graph
			public:
				static std::vector< std::vector<int> > *circuit_enumeration(Graph);
				static bool backtrack(Graph *, int, int, std::vector<bool> *, std::stack<int> *, std::stack<int> *, std::vector< std::vector<int> > *);
		};
};
