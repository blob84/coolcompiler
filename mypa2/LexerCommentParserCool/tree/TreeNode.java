package tree;
import position.*;
//import nodelist.*;

public class TreeNode<E> implements TreePosition<E>{
  	private E element;  // l'elemento memorizzato in questo nodo
	private TreePosition<E> parent;  // nodo padre
	private PositionList<Position<E>> children;  // nodi figli
	/** Costruttore */
	public TreeNode(E element, TreePosition<E> parent, 
	PositionList<Position<E>> children) { 
		setElement(element);
		setParent(parent);
		setChildren(children);
	}
	/** Restituisce l'elemento in questa posizione */
	public E element() { return element; }
	public void setElement(E elem) { element=elem; }
	public PositionList<Position<E>> getChildren() { return children; }
	public void setChildren(PositionList<Position<E>> c) { children=c; }
	public TreePosition<E> getParent() { return parent; }
	public void setParent(TreePosition<E> v) { parent=v; }
}
