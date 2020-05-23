package tree;

import java.util.Iterator;
import nodelist.*;
import position.*;
import stack.*;
/**
  * An implementation of the BinaryTree interface by means of a linked structure.
  */
public class LinkedBinaryTree<E> implements BinaryTree<E> {
  protected BTPosition<E> root;	// reference to the root
  protected int size;		// number of nodes
  /**  Creates an empty binary tree. */
  public LinkedBinaryTree() { 		    
    root = null;  // start with an empty tree
    size = 0;
  }
  /** Returns the number of nodes in the tree. */
  public int size() {
    return size; 
  } 
	public boolean isEmpty() {
		return size == 0;
	}
  /** Returns whether a node is internal. */
  public boolean isInternal(Position<E> v) throws InvalidPositionException {
    checkPosition(v);		// auxiliary method
    return (hasLeft(v) || hasRight(v));
  }
/** Returns whether a node is internal. */
  public boolean isExternal(Position<E> v) throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);		// auxiliary method
    return !isInternal(vv);
  }
  /** Returns whether a node is the root. */
  public boolean isRoot(Position<E> v) throws InvalidPositionException { 
    checkPosition(v);
    return (v == root()); 
  }
  /** Returns whether a node has a left child. */
  public boolean hasLeft(Position<E> v) throws InvalidPositionException { 
    BTPosition<E> vv = checkPosition(v);
    return (vv.getLeft() != null);
  }
  /** Returns whether a node has a left child. */
  public boolean hasRight(Position<E> v) throws InvalidPositionException { 
    BTPosition<E> vv = checkPosition(v);
    return (vv.getRight() != null);
  }	
  /** Returns the root of the tree. */
  public Position<E> root() throws EmptyTreeException {
    if (root == null)
      throw new EmptyTreeException("The tree is empty");
    return root;
  } 
  /** Returns the left child of a node. */
  public Position<E> left(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException { 
    BTPosition<E> vv = checkPosition(v);
    Position<E> leftPos = vv.getLeft();
    if (leftPos == null)
      throw new BoundaryViolationException("No left child");
    return leftPos;
  }
	/** Returns the right child of a node. */
  public Position<E> right(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException { 
    BTPosition<E> vv = checkPosition(v);
    Position<E> rightPos = vv.getRight();
    if (rightPos == null)
      throw new BoundaryViolationException("No left child");
    return rightPos;
  }
  /** Returns the parent of a node. */
  public Position<E> parent(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException { 
    BTPosition<E> vv = checkPosition(v);
    Position<E> parentPos = vv.getParent();
    if (parentPos == null)
      throw new BoundaryViolationException("No parent");
    return parentPos; 
  }
  /** Returns an iterable collection of the children of a node. */
  public Iterable<Position<E>> children(Position<E> v) 
    throws InvalidPositionException { 
   PositionList<Position<E>> children = new NodePositionList<Position<E>>();
    if (hasLeft(v))
      children.addLast(left(v));
    if (hasRight(v))
      children.addLast(right(v));
    return children;
  }
  /** Returns an iterable collection of the tree nodes. */
  public Iterable<Position<E>> positions() {
   PositionList<Position<E>> positions = new NodePositionList<Position<E>>();
    if(size != 0)
      preorderPositions(root(), positions);  // assign positions in preorder
    return positions;
  } 
 /** Returns an iterator of the elements stored at the nodes */
  public Iterator<E> iterator() {
    Iterable<Position<E>> positions = positions();
   PositionList<E> elements = new NodePositionList<E>();
   for (Position<E> pos: positions) 
     elements.addLast(pos.element());
    return elements.iterator();  // An iterator of elements
  }
  /** Replaces the element at a node. */
  public E replace(Position<E> v, E o) 
    throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);
    E temp = v.element();
    vv.setElement(o);
    return temp;
  }
  // Additional accessor method
  /** Return the sibling of a node */
  public Position<E> sibling(Position<E> v) 
    throws InvalidPositionException, BoundaryViolationException {
      BTPosition<E> vv = checkPosition(v);
      BTPosition<E> parentPos = vv.getParent();
      if (parentPos != null) {
		BTPosition<E> sibPos;
		BTPosition<E> leftPos = parentPos.getLeft();
	if (leftPos == vv)
	  sibPos = parentPos.getRight();
	else
	  sibPos = parentPos.getLeft();
	if (sibPos != null)
	  return sibPos;
      }
      throw new BoundaryViolationException("No sibling");
  }
  // Additional update methods
  /** Adds a root node to an empty tree */
  public Position<E> addRoot(E e) throws NonEmptyTreeException {
    if(!isEmpty())
      throw new NonEmptyTreeException("Tree already has a root");
    size = 1;
    root = createNode(e,null,null,null);
    return root;
  }
  /** Inserts a left child at a given node. */
  public Position<E>  insertLeft(Position<E> v, E e)
    throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);
    Position<E> leftPos = vv.getLeft();
    if (leftPos != null)
      throw new InvalidPositionException("Node already has a left child");
    BTPosition<E> ww = createNode(e, vv, null, null);
    vv.setLeft(ww);
    size++;
    return ww;
  }
  /** Inserts a right child at a given node. */
  public Position<E>  insertRight(Position<E> v, E e)
    throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);
    Position<E> rightPos = vv.getRight();
    if (rightPos != null)
      throw new InvalidPositionException("Node already has a left child");
    BTPosition<E> ww = createNode(e, vv, null, null);
    vv.setRight(ww);
    size++;
    return ww;
  }
  /** Removes a node with zero or one child. */
  public E remove(Position<E> v)
    throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);
    BTPosition<E> leftPos = vv.getLeft();
    BTPosition<E> rightPos = vv.getRight();
    if (leftPos != null && rightPos != null)
      throw new InvalidPositionException("Cannot remove node with two children");
    BTPosition<E> ww; 	// the only child of v, if any
    if (leftPos != null)
      ww = leftPos;
    else if (rightPos != null)
      ww = rightPos;
    else 		// v is a leaf
      ww = null;
    if (vv == root) { 	// v is the root
      if (ww != null)
	ww.setParent(null);
      root = ww;
    }
    else { 		// v is not the root
      BTPosition<E> uu = vv.getParent();
      if (vv == uu.getLeft())
	uu.setLeft(ww);
      else
	uu.setRight(ww);
      if(ww != null)
	ww.setParent(uu);
    }
    size--;
    return v.element();
  }
/** Attaches two trees to be subtrees of an external node. */
  public void attach(Position<E> v, BinaryTree<E> T1, BinaryTree<E> T2) 
    throws InvalidPositionException {
    BTPosition<E> vv = checkPosition(v);
    if (isInternal(v))
      throw new InvalidPositionException("Cannot attach from internal node");
	int newSize = size + T1.size() + T2.size(); 
	if (!T1.isEmpty()) {
	  BTPosition<E> r1 = checkPosition(T1.root());
	  vv.setLeft(r1);
	  r1.setParent(vv);		// T1 should be invalidated
   	}	
	if (!T2.isEmpty()) {
	  BTPosition<E> r2 = checkPosition(T2.root());
	  vv.setRight(r2);
	  r2.setParent(vv);		// T2 should be invalidated
	}
	size = newSize;
  }
  /** If v is a good binary tree node, cast to BTPosition, else throw exception */
  protected BTPosition<E> checkPosition(Position<E> v) 
    throws InvalidPositionException {
    if (v == null || !(v instanceof BTPosition))
      throw new InvalidPositionException("The position is invalid");
	return (BTPosition<E>) v;
  }
  /** Creates a new binary tree node */
  protected BTPosition<E> createNode(E element, BTPosition<E> parent, 
				  BTPosition<E> left, BTPosition<E> right) {
    return new BTNode<E>(element,parent,left,right); }
  /** Creates a list storing the the nodes in the subtree of a node,
    * ordered according to the preorder traversal of the subtree. */
  protected void preorderPositions(Position<E> v, PositionList<Position<E>> pos) 
    throws InvalidPositionException {
    pos.addLast(v);
    if (hasLeft(v))
      preorderPositions(left(v), pos);	// recurse on left child
    if (hasRight(v))
      preorderPositions(right(v), pos);	// recurse on right child
  }
/*	public BinaryTree<E> mirror(BinaryTree<E> S, BinaryTree<E> T, Position<E> v, Position<E> n)
	{	
		try {
			S.root(); 
		}
		catch (EmptyTreeException e) {
			S.addRoot(v.element());
			n = S.root();
		}	//System.out.println("n="+n.element());
		//BinaryTree<E> S = new LinkedBinaryTree<E>();  
		Position<E> oldn = n; //System.out.println("oldn="+oldn.element());
		if (hasRight(v)) {		//System.out.println(T.right(v).element());
			n = S.insertLeft(n, T.right(v).element());	 //System.out.println("inserito"+" el: "+v.element()+" cel: "+right(v).element());
			if (isExternal(T.right(v)))
				n = oldn;
	    	mirror(S, T, T.right(v), n); //System.out.println("dopo rec: "+v.element()+" "+S.left(v).element());	// recurse on right child
			n = oldn;
		}
		if (hasLeft(v)) { 	 
			n = oldn;
			n = S.insertRight(n, T.left(v).element()); //System.out.println("inserito"+" el: "+v.element()+" cel: "+left(v).element());
			if (isExternal(T.left(v)))
				n = oldn;
			mirror(S, T, T.left(v), n); 		// recurse on left child
		}
				//System.out.println(n.element()); 

		return S;
	}*/
	public String treeprint(Position<E> v)
	{
		BTPosition<E> vv = checkPosition(v);
		String s = "";
	    if (hasLeft(vv))
	      s += treeprint(left(vv)) + " ";	// recurse on right child
	    if (hasRight(vv)) 
	   	  s += treeprint(right(vv)) + " "; 		// recurse on left child
		s += vv.element();	// azione di visita principale
		return s;
	}
	/*public BinaryTree<E> buildExpression(String expr) {
		ArrayStack<BinaryTree<E>> S = new ArrayStack<BinaryTree<E>>();
		for (int i = 0; i < expr.length(); i++) {
			char c = expr.charAt(i);
			if (c.isLetter() || c.isDigit() || c == '+' || c == '-' || c == '*' || c == '/') {
				LinkedBinaryTree<Character> T = new LinkedBinaryTree<Character>();
				T.addRoot(c);
				S.push(T);
			}
			else if (c == '(')
				continue;
			else {
				LinkedBinaryTree<Character> T2 = S.pop();
				T = S.pop();
				LinkedBinaryTree<Character> T1 = S.pop();
				T.attach(T.root(), T1, T2);
				S.push(T);
			}
		}
		return S.pop();
	}*/
	/* preorder iterativo */
	public static <E> PositionList<Position<E>> preorder(LinkedBinaryTree<E> T) {
		PositionList<Position<E>> P = new NodePositionList<Position<E>>();
		ArrayStack<Position<E>> S = new ArrayStack<Position<E>>();
		Position<E> v = T.root(); 
		S.push(v);
		while (!S.isEmpty()) {
			P.addLast(v = S.pop());
			if (T.hasRight(v))
				S.push(T.right(v));
			if (T.hasLeft(v))
				S.push(T.left(v));
		}
		return P;
	}
	/* postorder iterativo */
/*	public static <E> PositionList<Position<E>> postorder(LinkedBinaryTree<E> T) {
		PositionList<Position<E>> P = new NodePositionList<Position<E>>();
		ArrayStack<Position<E>> S0 = new ArrayStack<Position<E>>();
		ArrayStack<Position<E>> S1 = new ArrayStack<Position<E>>();
		Position<E> v = T.root(); 
		S0.push(v); S1.push(v); 
		while (!S0.isEmpty()) {	
			if (!T.isExternal(v)) {	
				if (T.hasRight(v)) {
					S0.push(T.right(v));
					S1.push(T.right(v));
				}
				if (T.hasLeft(v)) {
					S0.push(T.left(v));
					S1.push(T.left(v));
				}
				if (!T.hasLeft(v))
					v = T.right(v);
				else
					v = T.left(v); 
			}
			else {
				if (!T.isRoot(S0.top()) && T.parent(S0.top()) == T.root() && T.hasLeft(T.root()) && T.hasRight(T.root()) && T.left(T.root()) != S0.top()) {
					if (!T.ancestor(S0.top(), v))
						v = S0.top();
					else {
						P.addLast(S0.pop()); S1.pop();
					}
				}
				else if (!T.isRoot(S0.top()) && T.right(T.parent(S0.top())) == S0.top() && T.isInternal(S0.top()) && S0.top() != S1.top()) {
					v = S0.top(); System.out.println("qui"); }
				else { 
					P.addLast(S0.pop()); S1.pop();
				}
	 		}
		}
		return P;
	}*/
	public static <E> PositionList<Position<E>> postorder0(LinkedBinaryTree<E> T) {
		PositionList<Position<E>> L = new NodePositionList<Position<E>>();
		Stack<Position<E>> S = new ArrayStack<Position<E>>();
		S.push(T.root());
		while(!S.isEmpty()){
		    Position<E> v = S.top();
		    if (T.isExternal(v)){
		         L.addLast(v);
		         T.remove(v);
		         S.pop();
		    }
		    else {
		         if (T.hasRight(v))
		             S.push(T.right(v));
		         if (T.hasLeft(v))
		             S.push(T.left(v));
		         }
		    }
		return L;
	}

	public boolean ancestor(Position<E> v, Position<E> w) {
		BTPosition<E> vv = checkPosition(v);
	  	BTPosition<E> ww = checkPosition(w);
	  	if (isRoot(ww))
			return false;
	  	BTPosition<E> p = ww.getParent();
	  	if (p == vv)
			return true;
	  	return ancestor (v,p);
	}
	public E remove() {
		return null;
	}
	public BinaryTree<E> copia(Position<E> p) {
		if (isExternal(p)) {
			BinaryTree<E> B = new LinkedBinaryTree<E>();
			B.addRoot(p.element()); 
			return B;
		}
		BinaryTree<E> B = new LinkedBinaryTree<E>();
		BinaryTree<E> B1 = new LinkedBinaryTree<E>();
		BinaryTree<E> B2 = new LinkedBinaryTree<E>();
		if (hasLeft(p)) 
			B1 = copia(left(p));
		if (hasRight(p)) 
			B2 = copia(right(p));
		Position<E> newnode = B.addRoot(p.element()); // azione di visita principale
		B.attach(newnode, B1, B2);
		return B;
	}
	public BinaryTree<E> mirror(Position<E> p) {
		if (isExternal(p)) {
			BinaryTree<E> B = new LinkedBinaryTree<E>();
			B.addRoot(p.element()); 
			return B;
		}
		BinaryTree<E> B = new LinkedBinaryTree<E>();
		BinaryTree<E> B1 = new LinkedBinaryTree<E>();
		BinaryTree<E> B2 = new LinkedBinaryTree<E>();
		if (hasLeft(p)) 
			B1 = mirror(left(p));
		if (hasRight(p)) 
			B2 = mirror(right(p));
		Position<E> newnode = B.addRoot(p.element()); // azione di visita principale
		B.attach(newnode, B2, B1);	
		return B;
	}
	public void mirrorvoid(Position<E> p) {
		if (isExternal(p)) 
			return;
		else {
			if (hasLeft(p)) 
				mirrorvoid(left(p));
			if (hasRight(p)) 
				mirrorvoid(right(p));
			if (!hasLeft(p))
				swap(null, right(p));
			else if (!hasRight(p))				
				swap(left(p), null);
			else
				swap(left(p), right(p));
		}
	}
	protected void swap(Position<E> p1, Position<E> p2) {
		BTPosition<E> pp1, pp2, father;
		try { 
			pp1 = checkPosition(p1);
		}
		catch (InvalidPositionException i) {
			pp1 = null;
		}
		try { 
			pp2 = checkPosition(p2);
		}
		catch (InvalidPositionException i) {
			pp2 = null;
		}
		if (pp1 != null)
			father = pp1.getParent();
		else if (pp2 != null)
			father = pp2.getParent();
		else 
			throw new InvalidPositionException("ivnalid position");
		father.setLeft(pp2);
		father.setRight(pp1);
	}
	public int depth(Position<E> p) {
		BTPosition<E> pp = checkPosition(p);
		if (isRoot(pp))
			return 0;
		else
			return 1 + depth(parent(pp));
	}
	public boolean equalException(Position<E> p, BinaryTree<E> W, Position<E> q) {
		try {
			isequal(p, W, q);
			return true;
		}
		catch (RuntimeException e) {
			return false;
		}
	}
	public void isequal(Position<E> p, BinaryTree<E> W, Position<E> q) {
		if (hasLeft(p) && W.hasLeft(q))
			equal(left(p), W, W.left(q));
		if (hasRight(p) && W.hasRight(q))
			equal(right(p), W, W.right(q));
		if (p.element().equals(q.element()))
			return;
		throw new RuntimeException("false");
	}
	public boolean equal(Position<E> p, BinaryTree<E> W, Position<E> q) {
		Stack<Position<E>> S1 = new ArrayStack<Position<E>>();
		Stack<Position<E>> S2 = new ArrayStack<Position<E>>();
		Position<E> v = p;
		Position<E> w = q; 
		S1.push(v);
		S2.push(w);
		while (!S1.isEmpty() && !S2.isEmpty()) {
			//P.addLast(v = S.pop());
			v = S1.pop();
			w = S2.pop();	//System.out.println(v.element()+"+"+w.element());
			if (!v.element().equals(w.element()))
				return false;	
			if (hasRight(v) && W.hasRight(w)) { 
				S1.push(right(v));
				S2.push(W.right(w));
			}
			else if (hasRight(v) && !W.hasRight(w) || !hasRight(v) && W.hasRight(w))
				return false;
			if (hasLeft(v) && W.hasLeft(w)) {
				S1.push(left(v));
				S2.push(W.left(w));
			}
			else if (hasLeft(v) && !W.hasLeft(w) || !hasLeft(v) && W.hasLeft(w))
				return false;
		}
		return true;
	}
	/*public static<E> int isRightLarger(BinaryTree<E> T, Position<E> p) {
		final Position<E> q = p;
		int n = 0;
		int nleft = 0;
		int nright = 0;
		if (T.isExternal(p))
			return 1;
		if (T.hasLeft(p))
			nleft += isRightLarger(T, T.left(p));
		if (T.hasRight(p))
			nright += isRightLarger(T, T.right(p));
		if (p == q) {
			if (nleft >= nright) 
				n = -2;
			else
				n = nright+nleft;
		}
		else
			n = nright+nleft;
		return n+1;
	}*/
}
