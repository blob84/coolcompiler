package tree;

import java.util.*;
import nodelist.*;
import position.*;

public class LinkedTree<E> implements Tree<E>{
  	protected TreePosition<E> root; // riferimento al nodo radice
	protected int size;  // numero di nodi
/**  Crea un albero vuoto. */
	public LinkedTree() {     
		root = null;  
		size = 0;
	}  
/** Se v e` un nodo valido, fa il cast a TreePosition, altrimenti lancia
   ** una eccezione. */
  	protected TreePosition<E> checkPosition(Position<E> v) 
    throws InvalidPositionException {
	if (v == null || !(v instanceof TreePosition))
		throw new InvalidPositionException("La posizione e` invalida");
		return (TreePosition<E>) v;
	}
/** Ci dice se un nodo e` interno. */
  	public boolean isInternal(Position<E> v) throws InvalidPositionException {
  	  return !isExternal(v);
	}
/** Ci dice se un nodo e` esterno (una foglia). */
	public boolean isExternal(Position<E> v) throws InvalidPositionException {
    	TreePosition<E> vv = checkPosition(v);
		return (vv.getChildren() == null) || vv.getChildren().isEmpty();
	}  
/** Ci dice se un nodo e` il nodo radice*/
	public boolean isRoot(Position<E> v) throws InvalidPositionException { 
    	checkPosition(v);
		return (v == root()); 
	}/** Restituisce la radice di questo albero. */
	public Position<E> root() throws EmptyTreeException {
		if (root == null)
		  throw new EmptyTreeException("L'albero e` vuoto");
		return root;
	}  
/** Restituisce una collezione iterable dei figli di un nodo. */
  	public Iterable<Position<E>> children(Position<E> v) 
		 throws InvalidPositionException { 
		TreePosition<E> vv = checkPosition(v);
		if (isExternal(v))
      		throw new InvalidPositionException("Una foglia non ha figli."); 
		return vv.getChildren();
	}
	public boolean isEmpty() {
		return size == 0;
	}
	public int size() {
		return size;
	}
 	protected void preorderPositions(Position<E> v,PositionList<Position<E>> pos)
		  throws InvalidPositionException {
		checkPosition(v);
		pos.addLast(v);
		if (isInternal(v))
			for (Position<E> w : children(v))
		   		preorderPositions(w, pos); // ricorsione su ciascun figlio
	}
/** Returns the parent of a node. */
	public Position<E> parent(Position<E> v) 
    	throws InvalidPositionException, BoundaryViolationException { 
    	TreePosition<E> vv = checkPosition(v);
    	Position<E> parentPos = vv.getParent();
    	if (parentPos == null)
      		throw new BoundaryViolationException("No parent");
    	return parentPos; 
  	}
/** Restituisce una collezione iterable dei nodi dell'albero. */
  	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> positions = new NodePositionList<Position<E>>();
		if(size != 0)
		  preorderPositions(root(), positions); //assegna le posizioni in preorder
		return positions;
	}  
	/** Restituisce un iteratore degli elementi memorizzati nei nodi */
	public Iterator<E> iterator() {
		Iterable<Position<E>> positions = positions();
		PositionList<E> elements = new NodePositionList<E>();
		for (Position<E> pos: positions) 
		  	elements.addLast(pos.element());
		return elements.iterator();  // Un iteratore degli elementi
	}
	/** Adds a root node to an empty tree */
	public Position<E> addRoot(E e) throws NonEmptyTreeException {
		if(!isEmpty())
		  throw new NonEmptyTreeException("Tree already has a root");
		size = 1;
		root = new TreeNode<E>(e,null,null);
		return root;
	}
	public Position<E> insertChild(Position<E> v, E e) throws 		InvalidPositionException {
		TreePosition<E> vv = checkPosition(v);
		TreePosition<E> ww = new TreeNode<E>(e,vv,null);
		PositionList<Position<E>> children = vv.getChildren(); 
		if (children == null){
			children = new NodePositionList<Position<E>>();
			vv.setChildren(children);
		}
		children.addLast(ww);
		size++;
		return ww;
	}
	public String postorder(Position<E> v)
	{
		checkPosition(v);
		String s = "";
		for (Position<E> w : children(v))
			s += postorder(w);
		s += v.element();
		return s;
	}
	/* Profondità del sottoalbero radicato in v */
	public int depth(Position<E> v) {
		checkPosition(v);
		if (isRoot(v))
			return 0;
		return 1 + depth(parent(v));
	}/* Numero di nodi foglia nell'albero */
	public int numberLeaves(LinkedTree<E> T, Position<E> v) {
		checkPosition(v);
		if (T.isExternal(v))
			return 1;
		int n = 0;
		for (Position<E> w : T.children(v))
			n += numberLeaves(T, w);
		return n;
	}/* Verifica se v è un antenato di w */
	public boolean ancestor(Position<E> v, Position<E> w) {
		TreePosition<E> vv = checkPosition(v);
	  	TreePosition<E> ww = checkPosition(w);
	  	if (isRoot(ww))
			return false;
	  	TreePosition<E> p = ww.getParent();
	  	if (p == vv)
			return true;
	  	return ancestor (v,p);
	} /* verifica se p è un discendente di q */
	public boolean isDescendent(Position<E> p, Position<E> q) {
		checkPosition(p);
	  	checkPosition(q);
		if (isExternal(q))	
			return false;
		for (Position<E> w: children(q)) {
			if (w == p)
				return true;
			isDescendent(p, w);
		}	
		return false;
	}
	/* trova l'antenato comune più basso */
	public Position<E> lca(Position<E> v, Position<E> w) {
		TreePosition<E> vv = checkPosition(v);
	  	TreePosition<E> ww = checkPosition(w);
	  	while (vv!=ww){
			if (depth(vv) > depth(ww))
				vv=vv.getParent();
		  	else
		    	ww=ww.getParent();
	  	}
	  	return vv;
	}
	public Iterable<Position<E>> path(Position<E> u, Position<E> v) {
		PositionList<Position<E>> positions = new NodePositionList<Position<E>>();
		Position<E> ancestor = lca(u, v);	/* trova l'antenato comune più basso */
		TreePosition<E> uu = checkPosition(u);
	  	TreePosition<E> vv = checkPosition(v);
		Position<Position<E>> last;
		while (uu != ancestor) {
			positions.addLast(uu);
			uu = uu.getParent();
		}
		last = positions.last(); /* l'ultimo inserito prima di v */
		positions.addLast(vv);
	  	while (vv != ancestor){
			positions.addAfter(last, vv.getParent());
			vv = vv.getParent();
	  	}
	  	return positions;
	}
	public static<E> Iterable<Position<E>> atDepth (Tree<E> T, int i) throws InvalidPositionException { //InvalidDepthException{
		PositionList<Position<E>> toReturn = new NodePositionList<Position<E>>();
		Iterable<Position<E>> list;
		if (i<0) throw new InvalidPositionException("profondita` invalida"); //InvalidDepthException("profondita` invalida");
	    else if (i==0) toReturn.addLast(T.root());
	    else {
		    list = atDepth(T, i-1);
	        for (Position<E> p: list){
		        if (T.isInternal(p))
		            for (Position<E> q : T.children(p))
		                toReturn.addLast(q);
	             }
	         }
		return toReturn;
	}
	public int height2 (Position<E> v) {
	  if (isExternal(v)) return 0;
	  int h = 0;
	  for (Position<E> w : children(v))
		 h = Math.max(h, height2(w));
	  return 1 + h;
	}

     public static <E> String parentheticRepresentation(Tree<E> T, Position<E> v) {
        String s = v.element().toString();
        if (T.isInternal(v)) {
            Boolean firstTime = true;
            for (Position<E> w : T.children(v)) {
                if (firstTime) {
                    s += " (" + parentheticRepresentation(T, w);
                    firstTime = false;
                }
                else s += " " + parentheticRepresentation(T, w);
            }
            s += " )";
        }
        return s;
    }

 	public static <E> String print(Tree<E> T, Position<E> v, String indent) {
		String s = v.element().toString();
		if (T.isInternal(v)) {
            s += " (";
        	for (Position<E> w : T.children(v)) {
                String newIndent;
                if (indent.equals("")) 
                    newIndent = ".. ";
                else 
                    newIndent = "..." + indent;
   		        s += "\n"+newIndent+print(T, w, newIndent); // ricorsione su ciascun figlio
            }
            s += " )";
        }
        return s;
	}


}

