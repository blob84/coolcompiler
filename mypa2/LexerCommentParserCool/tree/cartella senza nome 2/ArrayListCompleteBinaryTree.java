package tree;

import arraylist.*;
import position.*;
import nodelist.*;
import java.util.Iterator;

public class ArrayListCompleteBinaryTree<E> implements CompleteBinaryTree<E> {
 protected IndexList<BTPos<E>> T; // indexed list of tree positions
 protected static class BTPos<E> implements Position<E> {
		E element;
		int index;      // indice di questa posizione nell'array list
		public BTPos(E elt, int i) {
		    element = elt;
		    index = i;
		}
		public E element() { return element; }
		public int index() { return index; }
		public E setElement(E elt) {
		    E temp = element;
		    element = elt;
		    return temp;
		}
		public String toString() {
		    return("[" + element + "," + index + "]");
		}
	  	/*protected BTPos<E> checkPosition(Position<E> p) throws InvalidPositionException {
			if (p == null)
			  throw new InvalidPositionException("Null position passed to ArrayList");
			try {
				checkIndex(p.index(), p.index());
			  BTPos<E> temp = (BTPos<E>) p;
			  return temp;
			} catch (IndexOutOfBoundsException e) {
			  throw new InvalidPositionException("Position is of wrong type for this list");
			}
  		}*/			
	}
	public ArrayListCompleteBinaryTree() {
		T = new ArrayIndexList<BTPos<E>>();
		T.add(0, null); // the location at rank 0 is deliberately empty
	}
	/** Returns the number of (internal and external) nodes. */
	public int size() { return T.size() - 1; }
	/** Returns whether the tree is empty. */
	public boolean isEmpty() { return (size() == 0); }
	/** Returns whether v is an internal node. */
	public boolean isInternal(Position<E> v) throws InvalidPositionException {
		return hasLeft(v); // if v has a right child it will have a left child
	}
	/** Returns whether v is an external node. */
	public boolean isExternal(Position<E> v) throws InvalidPositionException {
		return !isInternal(v);
	}
	/** Returns whether v is the root node. */
	public boolean isRoot(Position<E> v) throws InvalidPositionException {
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return vv.index() == 1;
	}
	public boolean hasLeft(Position<E> v) throws InvalidPositionException {
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return 2*vv.index() <= size();
	}
	/** Returns whether v has a right child. */
	public boolean hasRight(Position<E> v) throws InvalidPositionException {
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return 2*vv.index() + 1 <= size();
	}
	/** Returns the root of the tree. */
	public Position<E> root() throws EmptyTreeException {
		if (isEmpty()) throw new EmptyTreeException("Tree is empty");
		return T.get(1);
	}
	/** Returns the left child of v. */
	public Position<E> left(Position<E> v)
	throws InvalidPositionException, BoundaryViolationException {
		if (!hasLeft(v)) throw new BoundaryViolationException("No left child");
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return T.get(2*vv.index());
	}
	public Position<E> right(Position<E> v)
	throws InvalidPositionException {
		if (!hasRight(v)) throw new BoundaryViolationException("No right child");
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return T.get(2*vv.index() + 1);
	}
	/** Returns the parent of v. */
	public Position<E> parent(Position<E> v)
	throws InvalidPositionException, BoundaryViolationException {
		if (isRoot(v)) throw new BoundaryViolationException("No parent");
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return T.get(vv.index()/2);
	}
	/** Returns an iterable collection of the children of v. */
	public Iterable<Position<E>> children(Position<E> v) throws
	InvalidPositionException {
		PositionList<Position<E>> children = new NodePositionList<Position<E>>();
		if (hasLeft(v))
		    children.addLast(left(v));
		if (hasRight(v))
		    children.addLast(right(v));
		return children;
	}
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> positions = new NodePositionList<Position<E>>();
		for (int i =1; i < T.size(); i++)
		    positions.addLast(T.get(i));
		return positions;
	}
	/** Replaces the element at v. */
	public E replace(Position<E> v, E o) throws InvalidPositionException {
		//BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
		return vv.setElement(o);
	}
	/** Adds an element just after the last node (in a level numbering). */
	public Position<E> add(E e) {
		int i = size() + 1;
		BTPos<E> p = new BTPos<E>(e,i);
		T.add(i, p);
		return p;
	}
	/** Removes and returns the element at the last node. */
	public E remove() throws EmptyTreeException {
		if(isEmpty()) throw new EmptyTreeException("Tree is empty");
		return T.remove(size()).element();
	}
	  // Additional accessor method
  /** Return the sibling of a node */
  public Position<E> sibling(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException {
      //BTPos<E> vv = checkPosition(v);
		BTPos<E> vv = (BTPos<E>) v;
      BTPos<E> parentPos = T.get(vv.index());
      if (parentPos != null) {
		BTPos<E> sibPos;
		BTPos<E> leftPos = T.get(2*parentPos.index());
	if (leftPos == vv)
	  sibPos = T.get(2*parentPos.index()+1);
	else
	  sibPos = T.get(2*parentPos.index());
	if (sibPos != null)
	  return sibPos;
      }
      throw new BoundaryViolationException("No sibling");
  }
	  /** Determines whether the given position is a valid node. */
  protected BTPos<E> checkPosition(Position<E> v) 
    throws InvalidPositionException 
  {
    if (v == null || !(v instanceof BTPos))
      throw new InvalidPositionException("Position is invalid");
    return (BTPos<E>) v;
  }
		public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
		public E remove(Position<E> v) {
			return null;
		}
	public Position<E>  addRoot(E e) {
		return null;
	}
	public Position<E>  insertLeft(Position<E> v, E e) {
		return null;
	}
	public Position<E>  insertRight(Position<E> v, E e) {
		return null;
	}
	public LinkedBinaryTree<E> copia(Position<E> p) {
		return null;
	}
	public String treeprint(Position<E> v) { return null; }
	public void attach(Position<E> v, BinaryTree<E> T1, BinaryTree<E> T2) {}
	public BinaryTree<E> mirror(Position<E> p) { return null; }
	public void mirrorvoid(Position<E> p) {  }

}
