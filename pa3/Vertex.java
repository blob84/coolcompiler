public interface Vertex<V> {
	public V element(); 
	public Object getColor();	/* colore del vertice */
	public Vertex<V> getPredVert();	/* vertice precedente */
	public int getDiscoverTime();	/* tempo di scoperta del vertice */
	public int getFinishTime();		/* tempo di fine analisi della lista di adiacenza */
	public void setColor(Object color);	
	public void setPredVert(Vertex<V> v);	
	public void setDiscoverTime(int time);	
	public void setFinishTime(int time);		
}
