class Edge {
	public:
		void *el_origin;
		void *el_destination;
		int i_origin;
		int i_destination;
		Edge(int i, int j, void *u, void *v) {
			i_origin = i;
			i_destination = j;
			el_origin = u;
			el_destination = v;
		}
};
