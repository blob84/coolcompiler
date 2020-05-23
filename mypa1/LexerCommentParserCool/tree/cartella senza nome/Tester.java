package tree;

import java.util.Iterator;

public class Tester {
	public static void main(String[] args) {
		LinkedTree<String> T = new LinkedTree<String>();
		Position<String> r = T.addRoot("r");
		Position<String> a = T.insertChild(r, "a");
		Position<String> b = T.insertChild(r, "b");
		T.insertChild(a, "c");
		T.insertChild(b,"e");
		T.insertChild(b,"f");
		Position<String> g = T.insertChild(b,"g");
		T.insertChild(g,"h");
		T.insertChild(g,"i");
		Iterator<String> It = T.iterator();
		while (It.hasNext())
			System.out.println(It.next());
	}
}
