import java.util.Iterator;

public class LinkedTreeTester {
	public static void main(String[] args) {
		LinkedTree<String> T = new LinkedTree<String>();
		Position<String> r = T.addRoot("r");
		Position<String> a = T.insertChild(r, "a");
		Position<String> b = T.insertChild(r, "b");
		Position<String> c = T.insertChild(a, "c");
		Position<String> e = T.insertChild(b,"e");
		Position<String> f = T.insertChild(b,"f");
		Position<String> g = T.insertChild(b,"g");
		Position<String> h = T.insertChild(g,"h");
		Position<String> i = T.insertChild(g,"i");
		
		/*Iterable<Position<String>> IT = T.positions(); 
		Iterator<Position<String>> I = IT.iterator();
		//Iterator<String> I = T.iterator();		
		while (I.hasNext())
			System.out.println(I.next().element());	
		*/Iterable<Position<String>> PTH = T.path(e, h);
		Iterator<Position<String>> IPTH = PTH.iterator();
		while (IPTH.hasNext())
			System.out.println("path: " + IPTH.next().element());

		Iterable<Position<String>> DP = LinkedTree.atDepth(T, 3);
		Iterator<Position<String>> IDP = DP.iterator();
		while (IDP.hasNext()) 
			System.out.println("dth: " + IDP.next().element());	
		

	}
}
