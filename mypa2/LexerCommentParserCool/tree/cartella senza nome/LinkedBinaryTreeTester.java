package tree;

import java.util.Iterator;

public class LinkedBinaryTreeTester {
	public static void main(String[] args) {
		LinkedBinaryTree<String> T = new LinkedBinaryTree<String>();
		Position<String> r = T.addRoot("r");
		Position<String> a = T.insertLeft(r, "a");
		Position<String> b = T.insertRight(r, "b");
		Position<String> c = T.insertLeft(a, "c");
		Position<String> e = T.insertRight(b,"e");
		Position<String> f = T.insertLeft(b,"f");
		Position<String> g = T.insertRight(a,"g");
		Position<String> h = T.insertLeft(g,"h");
		Position<String> i = T.insertRight(g,"i");
		
/*		
		//BinaryTree<String> B = (LinkedBinaryTree<String>) A;
		//A.treeprint(A, r);
		Position<String> n = null;
		BinaryTree<String> S = new LinkedBinaryTree<String>();
		BinaryTree<String> C = T.mirror(S, T, r, n);
		System.out.println(S.root().element());
		S.treeprint(S, S.root()); 		System.out.println("...................");
		System.out.println(S.size()+" "+S.isEmpty());
		T.treeprint(T, T.root());
		Position<String> E = T.addRoot("E");
		Position<String> D = T.insertLeft(E, "D");
		Position<String> H = T.insertRight(E, "H");
		Position<String> B = T.insertLeft(D, "B");
		Position<String> C = T.insertRight(B,"C");
		Position<String> A = T.insertLeft(B,"A");
		Position<String> F = T.insertLeft(H,"F");
		Position<String> G = T.insertRight(F,"G");
*/
		T.treeprint(T, T.root());	System.out.println("........................");
		/*PositionList<Position<String>> P0 =  T.preorder(T);
		Iterator<Position<String>> I0 = P0.iterator();
		while (I0.hasNext())
			System.out.println(I0.next().element()); System.out.println("--------------------");
		*/PositionList<Position<String>> P1 =  T.postorder(T);
		Iterator<Position<String>> I1 = P1.iterator();
		while (I1.hasNext())
			System.out.println(I1.next().element());
		

	}
}
