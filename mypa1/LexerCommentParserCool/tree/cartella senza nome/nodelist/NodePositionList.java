package nodelist;
import java.util.*;
import position.*;

public class NodePositionList<E> implements PositionList<E> {
protected int numElts;            	// Number of elements in the list
  protected DNode<E> header, trailer;	// Special sentinels

  /** Constructor that creates an empty list; O(1) time */
  public NodePositionList() {
    numElts = 0;
    header = new DNode<E>(null, null, null);	// create header
    trailer = new DNode<E>(header, null, null);	// create trailer
    header.setNext(trailer);	// make header and trailer point to each other
  }
  /** Checks if position is valid for this list and converts it to
    *  DNode if it is valid; O(1) time */
  protected DNode<E> checkPosition(Position<E> p)
    throws InvalidPositionException {
    if (p == null)
      throw new InvalidPositionException
	("Null position passed to NodeList");
    if (p == header)
	throw new InvalidPositionException
	  ("The header node is not a valid position");
    if (p == trailer)
	throw new InvalidPositionException
	  ("The trailer node is not a valid position");
    try {
      DNode<E> temp = (DNode<E>) p;
      if ((temp.getPrev() == null) || (temp.getNext() == null))
	throw new InvalidPositionException
	  ("Position does not belong to a valid NodeList");
      return temp;
    } catch (ClassCastException e) {
      throw new InvalidPositionException
	("Position is of wrong type for this list");
    }
  }
	  /** Insert the given element at the beginning of the list, returning
    * the new position; O(1) time  */
  public void addFirst(E element) {
    numElts++;
    DNode<E> newNode = new DNode<E>(header, header.getNext(), element);
    header.getNext().setPrev(newNode);
    header.setNext(newNode);
  }
  public void addLast(E element) {
    numElts++;
    DNode<E> newNode = new DNode<E>(trailer.getPrev(), trailer, element);
    trailer.getPrev().setNext(newNode);
    trailer.setPrev(newNode);
  }
  public Position<E> first()
      throws EmptyListException {
    if (isEmpty())
      throw new EmptyListException("List is empty");
    return header.getNext();
  }
  public Position<E> last()
      throws EmptyListException {
    if (isEmpty())
      throw new EmptyListException("List is empty");
    return trailer.getPrev();
  }
  /**Remove the given position from the list; O(1) time */
  public E remove(Position<E> p)
      throws InvalidPositionException {
    DNode<E> v = checkPosition(p);
    numElts--;
    DNode<E> vPrev = v.getPrev();
    DNode<E> vNext = v.getNext();
    vPrev.setNext(vNext);
    vNext.setPrev(vPrev);
    E vElem = v.element();
    // unlink the position from the list and make it invalid
    v.setNext(null);
    v.setPrev(null);
    return vElem;
  }
  /** Replace the element at the given position with the new element
    * and return the old element; O(1) time  */
  public E set(Position<E> p, E element)
      throws InvalidPositionException {
    DNode<E> v = checkPosition(p);
    E oldElt = v.element();
    v.setElement(element);
    return oldElt;
  }
	public Iterator<E> iterator() {
		return new ElementIterator<E>(this);
	}
	public boolean isEmpty() {
		return numElts == 0;
	}
	  public int size() { return numElts; }
  public Position<E> next(Position<E> p) 
    throws InvalidPositionException, BoundaryViolationException {
		    DNode<E> v = checkPosition(p);
			return v.getNext();		
	}
  /** Returns the node before a given node in the list. */
  public Position<E> prev(Position<E> p) 
    throws InvalidPositionException, BoundaryViolationException {
	    DNode<E> v = checkPosition(p);
		return v.getPrev();		
	}

  /** Inserts an element at the front of the list, returning new position. */
  public void addAfter(Position<E> p, E e) 
	    throws InvalidPositionException {
	}
  /** Inserts an element before the given node in the list. */
  /** Insert the given element before the given position, returning
    * the new position; O(1) time  */
  public void addBefore(Position<E> p, E element) 
      throws InvalidPositionException {			 
    DNode<E> v = checkPosition(p);
    numElts++;
    DNode<E> newNode = new DNode<E>(v.getPrev(), v, element);
    v.getPrev().setNext(newNode);
    v.setPrev(newNode);
  }
  /** Removes a node from the list, returning the element stored there. */
  public Iterable<Position<E>> positions() {// crea una lista di posizioni
    PositionList<Position<E>> P = new NodePositionList<Position<E>>();
    if (!isEmpty()) {
      Position<E> p = first();
      while (true) {
        P.addLast(p); // aggiunge la posizione p come ultimo elemento di P
        if (p == last())
          break;
        p = next(p);
      }
    }
    return P; // restituisce P come oggetto Iterable
  }
}
