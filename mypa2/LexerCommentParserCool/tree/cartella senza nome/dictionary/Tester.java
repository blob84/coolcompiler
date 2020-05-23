import java.util.Iterator;

public class Tester {
	public static void main(String[] args) {
		ListDictionary<Integer,String> L = new ListDictionary<Integer,String>();
		L.insert(1, "a");
		L.insert(2, "b");
		L.insert(1, "c");
		L.insert(3, "s");
		L.insert(2, "h");
		L.insert(2, "d");
		L.insert(3, "z");
		L.insert(2, "q");
		L.insert(2, "f");
		Iterator<Entry<Integer,String>> I = L.findAll(2);
		while (I.hasNext())
			System.out.println(I.next().getValue());
	}
	


}
