package tree;

/**
  * Implementazione dell'interfaccia stack mediante un array
  * di lunghezza assegnata. Viene lanciata un'eccezione se si
  * tenta un'operazione di push quando la lunghezza dello
  * stack è uguale a quella dell'array. Questa classe
  * comprende i principali metodi della classe incorporata
  * java.util.Stack.
  **/
public class ArrayStack <E> implements Stack <E>{
    protected int capacity; // effettiva capacità dell'array stack
    public static final int CAPACITY = 1000; // capacità di default dell'array
    protected E S[]; // generico array per implementare lo stack
    protected int top = -1 ; // indice dell'elemento in cima allo stack
    public ArrayStack() {
      this(CAPACITY); // capacità di default
    }
    public ArrayStack(int cap) {
      capacity = cap;
      S = (E[]) new Object[capacity]; // ignorare il warning del compilatore
    }
    public int size() {
      return (top + 1);
    }
    public boolean isEmpty() {
      return (top < 0);
    }
    public void push(E element) throws FullStackException {
      if (size() == capacity)
         throw new FullStackException( " Stack pieno. " );
      S[++top] = element;
    }
    public E top( ) throws EmptyStackException {
      if (isEmpty())
         throw new EmptyStackException(" Stack vuoto. ");
      return S[top];
    }
    public E pop() throws EmptyStackException {
      E element;
      if (isEmpty())
         throw new EmptyStackException( " Stack vuoto. " );
      element = S[top];
      S[top--] = null; // dereferenzia S[top] per consentire garbage collection,
    	return element;
	}
   public String toString() {
     String s;
     s = "[";
     if (size() > 0) s+= S[0];
     if (size() > 1)
        for (int i = 1; i <= size()-1; i++) 
           s += "," + S[i];
     return s + " ] " ;
   }
// stampa informazioni sullo stato di un'operazione recente e dello stack,
   public void status(String op, Object element) {
     System.out.print("          >" + op); // stampa questa operazione
     System.out.println(", returns" + element); // valore restituito
     System.out.print(" result: size =" + size() + ", isEmpty = " + isEmpty());
     System.out.println(", stack: " + this); // contenuto dello stack
   }
   /**public static void main(String[] args) {
     Object o;
     ArrayStack<Integer> A = new ArrayStack<Integer>();
     A.status("new ArrayStack<Integer> A", null);
     A.push(7);
     A.status("A.pushC7)", null);
     o = A.pop();
     A.status("A.pop()", o);
     A.push(9);
     A.status(" A.push(9) ", null);
     o = A.pop();
     A.status("A.pop())", o);
     ArrayStack<String> B = new ArrayStack<String>();
     B.status("new ArrayStack<String> B", null);
     B.push("Bob");
     B.status("B.push(\"Bob\" ) ", null);
     B.push(" Alice");
     B.status("B.push(\"Alice\")", null);
     o = B.pop();
     B.status("B.pop()", o);
     B.push("Eve");
     B.status("B.push(\"Eve\" ) ", null);
	}*/
}
