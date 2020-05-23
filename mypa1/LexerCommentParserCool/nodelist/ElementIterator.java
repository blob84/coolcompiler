package nodelist;
import java.util.Iterator;
import java.util.*;
import position.*;
public class ElementIterator<E> implements Iterator<E>{
  protected PositionList<E> list; // la lista su cui implem. l'iteratore
  protected Position<E> cursor; // indica la successiva posizione
  public ElementIterator(PositionList<E> L) {
    list = L;
    cursor = (list.isEmpty())? null : list.first();
  }
  public boolean hasNext() { return (cursor != null);  }
  public E next() throws NoSuchElementException {
    if (cursor == null)
      throw new NoSuchElementException("No next element");
    E toReturn = cursor.element();
    cursor = (cursor == list.last())? null : list.next(cursor);
    return toReturn;
  }
	public void remove() {
				

  }
}

