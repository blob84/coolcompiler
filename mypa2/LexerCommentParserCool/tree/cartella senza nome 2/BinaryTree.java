package tree;
import position.*;
/**
 * An interface for a binary tree, where each node can have zero, one,
 * or two children.
 */
public interface BinaryTree<E> extends Tree<E> {
  /** Returns the left child of a node. */
  public Position<E> left(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException;
  /** Returns the right child of a node. */
  public Position<E> right(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException;
  /** Returns whether a node has a left child. */
  public boolean hasLeft(Position<E> v) throws InvalidPositionException;
  /** Returns whether a node has a right child. */
  public boolean hasRight(Position<E> v) throws InvalidPositionException;
	//public BinaryTree<E> mirror(BinaryTree<E> S, BinaryTree<E> T, Position<E> v, Position<E> n);
	//public void treeprint(BinaryTree<E> T, Position<E> v);
	public Position<E>  insertLeft(Position<E> v, E e);
	public Position<E>  insertRight(Position<E> v, E e);
	public Position<E>  addRoot(E e);
  	public E remove();
	public E remove(Position<E> v);
	public E replace(Position<E> v, E o) throws InvalidPositionException;
	public BinaryTree<E> copia(Position<E> p);
	public void attach(Position<E> v, BinaryTree<E> T1, BinaryTree<E> T2);
	public String treeprint(Position<E> v);
	public BinaryTree<E> mirror(Position<E> p);
	public void mirrorvoid(Position<E> p);

}
