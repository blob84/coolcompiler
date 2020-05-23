public class Test {
	public static void insert(IndexList<Integer> V, int len, Integer elem){
		int i = len - 1;
		while ( i>= 0 && V.get(i) > elem )
			i--;
		V.add(i+1, elem);
	}
	public static void insertionSort(IndexList<Integer> V){
		 int n = V.size();
	for (int i = 1; i < n; i++)
		 insert(V, i, V.remove(i));
	}
	public static void main(String[] args) {
		IndexList<Integer> V = new ArrayIndexList<Integer>();  
		V.add(0, 10);
		V.add(1, 20);
		V.add(2, 30);
		V.add(3, 24);
		V.add(4, 36);
		V.add(5, 21);
		insertionSort(V);
		for(int i = 0; i < V.size(); i++)
			System.out.println(V.get(i));
	
	}
}
