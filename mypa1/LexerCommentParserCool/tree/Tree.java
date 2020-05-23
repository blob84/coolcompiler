package tree;

import java.util.*;
import position.*;
import nodelist.*;
public interface Tree<E> {
	public int size();
	public boolean isEmpty();
	/** Restituisce un iteratore degli elementi memorizzati nell'albero. */
	  public Iterator<E> iterator();
	/** Restituisce una collezione iterabile dei nodi. */
	  public Iterable<Position<E>> positions();
	/** Rimpiazza l'elemento memorizzato in un dato nodo. */
	  //public E replace(Position<E> v, E e);
		 //throws InvalidPositionException;
	/** Restituisce la radice dell'albero. */
	public Position<E> root() throws EmptyTreeException;
	/** Restituisce il padre di un dato nodo. */
	public Position<E> parent(Position<E> v)
		 throws InvalidPositionException, BoundaryViolationException;
	/** Restituisce una collezione iterabile dei figli di un dato nodo. */
	public Iterable<Position<E>> children(Position<E> v) 
		 throws InvalidPositionException;
	/** Ci dice se un dato nodo e` interno. */
	public boolean isInternal(Position<E> v) 
		 throws InvalidPositionException;
	/** Ci dice se un dato nodo e` esterno (una foglia). */
	public boolean isExternal(Position<E> v) 
		 throws InvalidPositionException;
	/** Ci dice se un dato nodo e` la radice di questo albero. */
	public boolean isRoot(Position<E> v)
		 throws InvalidPositionException;
}
