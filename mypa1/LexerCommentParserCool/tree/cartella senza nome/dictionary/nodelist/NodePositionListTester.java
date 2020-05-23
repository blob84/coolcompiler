import java.util.Iterator;
public class NodePositionListTester {
	public static void merge(PositionList<Integer> L1,
		PositionList<Integer> L2, PositionList<Integer> L){
		while (!L1.isEmpty() && !L2.isEmpty())
		if (L1.first().element() <= L2.first().element())
		     L.addLast(L1.remove(L1.first()));
		else
		     L.addLast(L2.remove(L2.first()));
		while(!L1.isEmpty()) // sposta i rimanenti elementi di L1
		     L.addLast(L1.remove(L1.first()));
		while(!L2.isEmpty()) // sposta i rimanenti elementi di L2
		     L.addLast(L2.remove(L2.first()));
	}
	public static void mergeSort(PositionList<Integer> L){
		int n = L.size();
		if (n < 2)
		    return;
		PositionList<Integer> L1 = new NodePositionList<Integer>();
		PositionList<Integer> L2 = new NodePositionList<Integer>();
		for (int i = 0; i < n/2; i++)
		    L1.addLast(L.remove(L.first()));
		for (int i = n/2; i < n; i++)
		    L2.addLast(L.remove(L.first()));
		mergeSort(L1);
		mergeSort(L2);
		merge(L1,L2,L);
	}
	public static void remove(PositionList<Integer> L, Integer i){
    Iterable<Position<Integer>> It = L.positions();
    Iterator<Position<Integer>> I = It.iterator();
    while (I.hasNext()){
        Position<Integer> p = I.next();
        if (p.element().equals(i))
             L.remove(p);
    	}
	}
	public static void main(String[] args) {
		PositionList<Integer> L1 = new NodePositionList<Integer>();
		L1.addLast(7);
		L1.addLast(5);
		L1.addLast(2);
		L1.addLast(4);
		L1.addLast(1);
		L1.addLast(8);
		mergeSort(L1);
		Iterator<Integer> I = L1.iterator();
		while (I.hasNext())
		    System.out.println(I.next()+" ");
		remove(L1,1);
		Iterator<Integer> I1 = L1.iterator();
		while (I1.hasNext())
		    System.out.println(I1.next()+" ciao ");
	}
}
