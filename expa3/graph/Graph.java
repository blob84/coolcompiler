import java.lang.IllegalArgumentException;
import java.util.*;

public class Graph<V, E> {
	private LinkedList<Vertex<V>> vList; /* lista di vertici */
	private LinkedList<Edge<E>> eList;	/* lista di archi */
	private boolean isDirect; //il grafo è diretto o no

	/* inizializza un grafo vuoto */
	public Graph(boolean direct) {
		isDirect = direct;
		vList = new LinkedList<Vertex<V>>();
		eList = new LinkedList<Edge<E>>();
	}

	public Graph() {
		isDirect = false;
		vList = new LinkedList<Vertex<V>>();
		eList = new LinkedList<Edge<E>>();
	}

	public boolean isDirect() { return isDirect; }
	// verifica la correttezza dell'indice
	protected void checkIndex(int r, int n) {
		if (r < 0 || r >= n)
      		throw new IndexOutOfBoundsException("Illegal index: " + r);
	}	
	// restituisce l'indice del vertice nella lista
	public int getIndexVertex(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);	
		return vv.index;
	}
	// restituisce un vertice in base alla posizione nella lista
	public Vertex<V> getVertex(int i) throws IndexOutOfBoundsException {	
		checkIndex(i, numVertices());
		return vList.get(i);
	}
	// restituisce una lista iterabile di vertici
  	public Iterable<Vertex<V>> vertices() { return vList; }

	//restituisce una lista iterabile di archi
  	public Iterable<Edge<E>> edges() { return eList; }

	/* restituisce il numero dei vertici */
  	public int numVertices() {
		return vList.size();
	}
	/* restituisce il numero degli archi */
  	public int numEdges() {
		return eList.size();
	}
	/* verifica che l'oggetto Vertex passato come parametro sia conforme */
	private MyVertex<V> checkVertex(Vertex<V> v) {
    	if (v == null || !(v instanceof Vertex))
        throw new IllegalArgumentException("Il vertice non è valido");
    	return (MyVertex<V>) v;
	}
	/* verifica che l'oggetto Edge passato come parametro sia conforme */
	private MyEdge<E> checkEdge(Edge<E> e) {
    	if (e== null || !(e instanceof Edge))
         throw new IllegalArgumentException("L'arco non è valido");
    	return (MyEdge<E>) e;
	}
	/* inserisce un nuovo vertice con elemento o */
	public Vertex<V> insertVertex(V o) throws IllegalArgumentException {
		MyVertex<V> v = new MyVertex<V>(o);		
		vList.addLast(v);
		v.index = numVertices()-1;	//memorizza la posizione del vertice nella lista
		return v;
	}
	/* inserisce un nuovo lato con i vertici u, v ed elemento o */
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E o) throws IllegalArgumentException {
		MyVertex<V> uu = checkVertex(u);
		MyVertex<V> vv = checkVertex(v);
		MyEdge<E> e = new MyEdge(uu, vv, o);
		eList.add(e);
		vv.inEdges.add(e);	/* e è un arco entrante da v */
		uu.outEdges.add(e);	/* e è un arco uscente da u */
		return e;
	}
	/* rimuove un vertice */
  	public V removeVertex(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		for (Edge<E> e : ingoingEdges(vv))	/* rimuove gli archi uscenti ed entranti da v */
			removeEdge(e);
		for (Edge<E> e : outgoingEdges(vv))
			removeEdge(e);
		vList.remove(vv);	
		return vv.element();
	}
  	/* rimuove un arco */
	public E removeEdge(Edge<E> e) throws IllegalArgumentException {
		MyEdge<E> ee = checkEdge(e);
		MyVertex<V> origin = (MyVertex<V>) ee.origin;
		MyVertex<V> dest = (MyVertex<V>) ee.destination;
		dest.inEdges.remove(ee);
		origin.outEdges.remove(ee);
		eList.remove(ee);
		return ee.element();
	}
	/* restituisce un vettore dei vertici finali dell'arco e*/
  	public Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException {
		MyEdge<E> ee = checkEdge(e);
		return ee.endVertices();
	}
	/* restituisce il numero degli archi entranti da v */
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		return vv.inEdges.size();
	}
	/* restituisce il numero degli archi uscenti da v */	
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		return vv.outEdges.size();
	}
	/* restituisce il numero degli archi entranti ed uscenti da v */
	public int degree(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		return vv.degree();
	}
	/* restituisce un arco avente come vertice origine u e come vertice destinazione v */
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> uu = checkVertex(u);
		MyVertex<V> vv = checkVertex(v);
		MyEdge<E> found = null;
		for (Edge<E> e : uu.outEdges) {	/* cerca l'arco tra gli archi uscenti dal vertice u */
			MyVertex<V> origin = (MyVertex<V>) ((MyEdge<E>)e).endVertices()[0];
			MyVertex<V> destination = (MyVertex<V>) ((MyEdge<E>)e).endVertices()[1];
			if (origin.element().equals(uu.element()) && destination.element().equals(vv.element())) {
				found = (MyEdge<E>) e; break; 
			}
		}
		return found;
	}
	/* definisce una classe per i vertici MyVertex<V> */
	private class MyVertex<V> implements Vertex<V> {
		public ArrayList<Edge<E>> outEdges;	/* archi uscenti */
		public ArrayList<Edge<E>> inEdges;	/* archi entranti */
		private V element;
		public int index;
		/* variabili per l'attraversamento depth-first */
		private Object color;	/* colore del vertice */
		private Vertex<V> pi;	/* vertice precedente */
		private int discover;	/* tempo di scoperta del vertice */
		private int finish;		/* tempo di fine analisi della lista di adiacenza */
		
		public MyVertex(V o) {
			element = o;
			outEdges = new ArrayList<Edge<E>>();
			if (isDirect)
				inEdges = new ArrayList<Edge<E>>();
			else inEdges = outEdges;
		}
		public int inDegree() {
			return inEdges.size();
		}
		public int outDegree() {
			return outEdges.size();
		}
		public int degree() {
			if (isDirect)
				return inDegree()+outDegree();
			return outDegree();
		}
		public Object getColor() { return color; }
		public Vertex<V> getPredVert() { return pi; }	/* restituisce vertice predecessore */
		public int getDiscoverTime() {	return discover; }
		public int getFinishTime() { return finish; }	
		public void setColor(Object color) { this.color = color; }
		public void setPredVert(Vertex<V> v) { pi = v; }	
		public void setDiscoverTime(int time) { discover = time; }	
		public void setFinishTime(int time) { time = finish; }		

		public V element() { return element; }
	}
	/* definisce una classe per il lati Edge<E> */
	private class MyEdge<E> implements Edge<E> {
		private E element;
		private Vertex<V> origin;	/* vertice origine */
		private Vertex<V> destination;	/* vertice destinazione */

		public MyEdge(Vertex<V> u, Vertex<V> v, E o) {
			element = o;
			origin = u;
			destination = v;
		}

		public Vertex<V>[] endVertices() {
			return new Vertex[]{origin, destination};
		}

		public E element() { return element; }
	}
	/* contenitore per gli attributi e metodi DFS */
	private static class DFS {
		public static int time;	/* tempo globale */
		public static final Object WHITE = new Object();
		public static final Object BLACK = new Object();	/* marca il vertice come fine dell'' analisi della lista di adiacenza */
		public static final Object GRAY = new Object();	/* marca il vertice come scoperto */
		public static boolean g;
		//inizializza la visita dfs per ogni vertice partendo dal primo
		public static <V,E> void dfs(Graph<V,E> g, Set<Vertex<V>> visited, Map<Vertex<V>,Edge<E>> forest) {
			for (Vertex<V> u: g.vertices())	{	
				u.setColor(WHITE); u.setPredVert(null);
			}
			time = 0;
			for (Vertex<V> u: g.vertices())	{	
				if (u.getColor() == WHITE)
					dfs(g, u, visited, forest);
			}
		}
		/* ricerca depth first search, l'insieme visited mantiene una lista di nodi visitati e forest associa al vertice l'arco usato per scoprire v */
		public static <V,E> void dfs(Graph<V,E> g, Vertex<V> u, Set<Vertex<V>> visited, Map<Vertex<V>,Edge<E>> forest) {
			visited.add(u);
			time++;
			u.setDiscoverTime(time);
			u.setColor(GRAY); /* vertice è stato scoperto */ 
			for (Edge<E> e : g.outgoingEdges(u)) {  /* esamina ogni arco uscente dal vertice corrente u */
				Vertex<V> v = g.opposite(u, e);	/* memorizza il vertice opposto a quello dell'arco corrente */
				if (v.getColor() == WHITE) {	/* il vertice non è stato ancora scoperto */
					v.setPredVert(u); 
					forest.put(v, e);
					dfs(g, v, visited, forest);
				}
			}
			u.setColor(BLACK);	/* fini visita */
			time++;
			u.setFinishTime(time);
		}
		/* prepara e chiama i vertici di un grafo per la copia tramite attraversamento depth first */
		public static <V,E> void dfsCopy(Graph<V,E> g, Graph<V,E> gcopy) {
			for (Vertex<V> u: g.vertices())	{	
				u.setColor(WHITE); u.setPredVert(null);
				gcopy.insertVertex(u.element());	//inserisce i vertici del grafo copiato nelle stessa posizione dei vertici nel grafo originale in modo che abbiano gli stessi indici
			}
			time = 0;
			for (Vertex<V> u: g.vertices())	{	
				if (u.getColor() == WHITE)
					dfsCopy(g, gcopy, u);
			}
		}
		/* copia un grafo tramite l'attraversamento depth first, complssità di tempo O(E+V)*/
		public static <V,E> void dfsCopy(Graph<V,E> g, Graph<V,E> gcopy, Vertex<V> u) {
			time++;
			u.setDiscoverTime(time);
			u.setColor(GRAY); /* il vertice è stato scoperto */ 
			for (Edge<E> e : g.outgoingEdges(u)) {  /* esamina ogni arco uscente dal vertice corrente u */
				Vertex<V> v = g.opposite(u, e);	/* memorizza il vertice opposto a quello dell'arco corrente */
				int vorindex = g.getIndexVertex(u);	//ottiene l'indice nella lista dei vertici del vertice u nel grafo originale
				int destindex = g.getIndexVertex(v); //ottiene l'indice nella lista dei vertici del vertice v nel grafo originale
				Vertex<V> or = gcopy.getVertex(vorindex);	//l'indice dei vertici della copia e quelli del grafo originale sono uguali	
				Vertex<V> dest = gcopy.getVertex(destindex);
				/*for (Vertex<V> copy : gcopy.vertices())	// trova il vertice origine nella copia  
					if (copy.element().equals(u.element())) { or = copy; break; }
				for (Vertex<V> copy : gcopy.vertices())	// trova il vertice destinazione nella copi 
					if (copy.element().equals(v.element())) { dest = copy; break; }
				*/gcopy.insertEdge(or, dest, e.element());
				if (v.getColor() == WHITE) {	/* il vertice non è stato ancora scoperto */
					v.setPredVert(u); 
					dfsCopy(g, gcopy, v);
				}
			}
			u.setColor(BLACK);	/* fine visita */
			time++;
			u.setFinishTime(time);
		}
	}

	private static class Tarjan {
		/* algoritmo di Tarjan, enumerazione dei cicli in un grafo diretto, restituisce un vettore di liste di percorsi di cicli dei vertici del grafo */
		public static <V,E> LinkedList<Vertex<V>>[] circuit_enumeration(Graph<V,E> g) {
			int n = g.numVertices();
			boolean[] marked = new boolean[n];
			Stack markedS = new Stack();
			Stack pointS = new Stack();
			ArrayList<LinkedList<Vertex<V>>> cycles = new ArrayList<LinkedList<Vertex<V>>>();
			int s;	


			for (Vertex<V> u: g.vertices())		
				u.setColor(DFS.WHITE);

			for (int i = 0; i < n; i++)
				marked[i] = false;
		
			for (int i = 0; i < n; i++) { 
				s = i;
				backtrack(g, s, i, marked, markedS, pointS, cycles);
				while (!markedS.empty()) {
					int u = (Integer) markedS.pop();
					marked[u] = false;
				}
			}
			LinkedList[] arrCycles = new LinkedList[cycles.size()];
			return (LinkedList<Vertex<V>>[]) cycles.toArray(arrCycles);
		}
		/* verifica i percorsi da un nodo s a t */
		public static <V,E> boolean backtrack(Graph<V,E> graph, int s, int v, boolean[] marked, Stack markedS, Stack pointS, ArrayList<LinkedList<Vertex<V>>> cycles) {
			pointS.push(v);
			marked[v] = true;
			markedS.push(v);
			boolean f = false;

			for (Edge<E> e : graph.outgoingEdges(graph.getVertex(v))) {
				int w = graph.getIndexVertex(graph.opposite(graph.getVertex(v), e));	 //System.out.println("w: "+(w+1));
				if (w < s) { graph.removeEdge(e); /*graph.removeVertex(graph.getVertex(w));*/  /*System.out.println("s is: "+(s+1)+" v is: "+(v+1) +" removed: "+(w+1));*/ }
				else if (w == s) {	//memorizza i vertici del ciclo
					f = true;
					int size = pointS.size();
					int[] tmp = new int[size];
					int i = 0;
					LinkedList<Vertex<V>> paths = new LinkedList<Vertex<V>>();
					while (!pointS.empty())	tmp[i++] = (Integer) pointS.pop();	//System.out.print("cycle: ");
					for (i = size-1; i >= 0; i--) { 
						//System.out.print((tmp[i]+1)+" "); 
						paths.add(graph.getVertex(tmp[i]));
						pointS.push(tmp[i]); 
					}
					paths.add(graph.getVertex(tmp[size-1]));
					//System.out.println(tmp[size-1]+1);
					//System.out.print("vertex: "+graph.getVertex(size-1).element());
					//paths.addLast(graph.getVertex(i));
					cycles.add(paths);
				}
				else if (!marked[w]) {
					boolean g = backtrack(graph, s, w, marked, markedS, pointS, cycles); //System.out.println("f: "+f+ " g: "+g);
					f = f || g;
					//System.out.println("g: "+g);
				} 
			}
			if (f == true) {	//System.out.println("peek: "+ (((Integer) markedS.peek())+1));
				while ((Integer) markedS.peek() != v) { //System.out.println("peek: "+ (((Integer) markedS.peek())+1));
					int u = (Integer) markedS.pop();
					marked[u] = false;
				}
				if (!markedS.empty()) {
					markedS.pop();
					marked[v] = false;
				}
			}	//cancella il vertice corrente dallo stack
			pointS.pop();  //System.out.println("f: "+f); 
			return f;
		}
	}
	/* calcola un ordine topologico del grafo inserendo nella lista i vertici nell'ordine in cui sono stati creati, 
		se non esiste un ordine topologico perché il grafo ha cicli, la lista dei vertici vordlist non è completa
	*/
	public static <V,E> void topologicalOrder(Graph<V,E> g, LinkedList<Vertex<V>> vordlist) {
		Vertex<V> found = null;	
		for (Vertex<V> v : g.vertices()) { 
			if (g.inDegree(v) == 0) { 
				found = v; break;
			}
		}
		if (found != null) {	/* rimuove il vertice senza archi entranti dal grafo e calcola l'ordine topologico del grafo privato da questo vertice */
			g.removeVertex(found);
			vordlist.addLast(found);
			topologicalOrder(g, vordlist);
		}
	}
	/* verifica se un grafo è senza cicli constatando se ha un ordine topologico */  
	public static <V,E> boolean isDAG(Graph<V,E> g) {
		if (g.isDirect() == false) return false;
		LinkedList<Vertex<V>> l = new LinkedList<Vertex<V>>();
		Graph<V,E> gcopy = g.deepcopy();	
		int numVertices = gcopy.numVertices();
		Graph.topologicalOrder(gcopy, l);	//calcola l'ordine topologico sulla copia 
		//restituisce vero se il numero dei vertici ordinati è uguale al numero dei vertici del grafo,
		if (l.size() == numVertices) return true;			// pertanto non sono stati trovati cicli
		return false;
	}
	/* riempie un arraylist di cicli, funziona solo per i grafi diretti */
/*	public static <V,E> void getCycles(Graph<V,E> g, ArrayList<Set<V,E>> paths, Set<V,E> vertices) {
		if (g.isDirect() == true)  {
			Graph<V,E> gcopy = g.deepcopy();	
			LinkedList<Vertex<V>> l = new LinkedList<Vertex<V>>();
			Graph<V,E> gcopy = g.deepcopy();	
			Graph.topologicalOrder(gcopy, l);	//calcola l'ordine topologico sulla copia 
			

		}
	}
*/
	// restituisce un vettore di archi in uscita di v
	public Edge<E>[] outgoingEdges(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		MyEdge[] es = new MyEdge[vv.outEdges.size()];
		return (MyEdge<E>[]) vv.outEdges.toArray(es);
	}
	// restituisce un vettore di archi in entrata di v
	public Edge<E>[] ingoingEdges(Vertex<V> v) throws IllegalArgumentException {
		MyVertex<V> vv = checkVertex(v);
		MyEdge[] es = new MyEdge[vv.inEdges.size()];
		return (MyEdge<E>[]) vv.inEdges.toArray(es);
	}
	/* per un arco e incidente al vertice v, restituisce l'altro vertice dell'arco */ 
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
        checkVertex(v);
        MyEdge<E> ee = checkEdge(e);
        Vertex<V>[] endv = ee.endVertices();
        if (v == endv[0])
             return endv[1];
        else if (v == endv[1])
             return endv[0];
        else
       		throw new IllegalArgumentException("Il vertice non è sull'arco specificato");
	}

	/* crea una copia del grafo, metodo iterativo lento */
	public Graph<V,E> deepcopySlow() {
		Graph<V,E> gcopy = new Graph<V,E>(isDirect);

		/* copia ed inserisce tutti i vertici nello stesso ordine dell'originale '*/
		for (Vertex<V> u : vertices()) 
			gcopy.insertVertex(u.element());
		
		Vertex<V> vor = null;
		Vertex<V> vdest = null;
		for (Vertex<V> u : vertices()) { System.out.println("vert: "+ u.element());
			V velor = u.element();

			for (Vertex<V> v : gcopy.vertices())	/* trova il vertice origine nella copia */
				if (v.element().equals(velor)) { vor = v; break; }

			for (Edge<E> e : outgoingEdges(u)) {	
				E eel = e.element();
				V veldest = opposite(u, e).element();	System.out.println("edg: ("+velor+","+veldest+")");

				for (Vertex<V> v : gcopy.vertices())	/* trova il vertice destinazione nella copia */
					if (v.element().equals(veldest)) { vdest=v; break; }  

				gcopy.insertEdge(vor, vdest, eel);				
			}
			vor = vdest = null;
		}
		/*for (Vertex<V> u : gcopy.vertices()) { System.out.println("verticex gcopy: "+ u.element());
			V velor = u.element();
			for (Edge<E> e : outgoingEdges(u)) {	
				E eel = e.element();
				V veldest = opposite(u, e).element();	System.out.println("edg copy: ("+velor+","+veldest+")");
			}
		}*/
		return gcopy;
	} 
	//crea una copia con attraversamento deph-first del grafo
	public Graph<V,E> deepcopy() {
		Graph<V,E> gcopy = new Graph<V,E>(isDirect);
		DFS.dfsCopy(this, gcopy);

		/*for (Vertex<V> u : gcopy.vertices()) { System.out.println("verticex gcopy: "+ u.element());
			V velor = u.element();
			for (Edge<E> e : outgoingEdges(u)) {	
				E eel = e.element();
				V veldest = opposite(u, e).element();	System.out.println("edg copy: ("+velor+","+veldest+")");
			}
		}*/

		return gcopy;
	}
}
