package tree;

/**
* Interfaccia per uno stack: un insieme di oggetti che sono inseriti
  * e prelevati secondo il criterio last-in-first-out.
  *
  * @autore Rober

  * 
  * @autore Roberto Tamassia
  * @autore Michael Goodrich
  * @vedere EmptyStackException
  */
public interface Stack <E> {
  /* Restituisce il numero di elementi dello stack.
  * @return numero di elementi dello stack.
  */
  public int size( );
  /* Verifica se lo stack è vuoto.
  * @return vero se lo stack è vuoto, falso altrimenti.
  */
  public boolean isEmpty();
  /* Esamina l'elemento in cima allo stack.
  * ©return l'elemento al top dello stack.
  * @exception EmptyStackException se lo stack è vuoto.
  */
  public E top()
     throws EmptyStackException;
  /* Inserisce un elemento in cima allo stack.
  * @param è l'elemento che deve essere inserito.
  */
  public void push (E element);
/**
  * Estrae l'elemento in cima allo stack.
  * @return l'elemento estratto.
  * @exception EmptyStackException se lo stack è vuoto.
  */
  public E pop() 
    throws EmptyStackException;
}

