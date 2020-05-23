import arraylist.*;
import position.*;
import nodelist.*;
import java.util.Iterator;
import java.util.Comparator;

public class ArrayListDictionary<K, V> implements Dictionary<K, V> {
  public static class ListEntry<K,V> implements Entry<K,V> {
    protected K key;
    protected V value;
    public ListEntry(K k, V v) { key = k; value = v; }
    public V getValue() { return value; }
    public K getKey() { return key; }
    public V setValue(V val) {
      V oldValue = value;
      value = val;
      return oldValue;
      }
    public boolean equals(Object o) {
      ListEntry<K,V> ent;
      try { ent = (ListEntry<K,V>) o; }
      catch (ClassCastException ex) { return false; }
      return (ent.getKey() == key) && (ent.getValue() == value);
    }
  }
	protected ArrayIndexList<Entry<K,V>> S;
	protected Comparator<K> c;
	public ArrayListDictionary() {
		S = new ArrayIndexList<Entry<K,V>>();
		c = new DefaultComparator<K>();	
	}
	public int size() {
		return S.size();
	}
	public boolean isEmpty() {
		return S.isEmpty();
	}
	public Entry<K,V> insert(K k, V v) {
		/*for (int i = 0; c.compare(S[i].getKey(), k) <= 0; i++)
			;
		int n = size();
		while (n-- > i) 
			S[n+1] = S[n];
		S[i] = new ListEntry<K,V>(k, v);
		return S[i];*/
		int i;		
		for (i = 0; c.compare(S.get(i).getKey(), k) <= 0; i++)
			;
		Entry<K,V> e = new ListEntry<K,V>(k, v);
		S.add(i, e);
		return e;		
	}
	public Entry<K,V> remove(Entry<K,V> e) {
		Entry<K,V> temp = e;
		int i;
		for (i = 0; i < size() && S.get(i) == e; i++)
			;
		S.remove(i);
		return temp;
	}	
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> P = new NodePositionList<Entry<K,V>>();
		return P;
	}
	public Iterator<Entry<K,V>> findAll(K k) {
		PositionList<Entry<K,V>> L = new NodePositionList<Entry<K,V>>();
		int i = BinarySearchIndex(S, k, 0, size()-1);
		if (i != -1)
			L.addLast((Entry<K,V>) S.get(i));
		int j = i-1;
		while (j >= 0 && c.compare(((Entry<K,V>) S.get(j)).getKey(), k) == 0)
			L.addLast((Entry<K,V>) S.get(j--));
		j= i+1;
		while (j < size() && c.compare(((Entry<K,V>) S.get(j)).getKey(), k) == 0)
			L.addLast((Entry<K,V>) S.get(j++));
		return L.iterator();
	}
	public Entry<K,V> find(K k) {
		Entry<K,V> e = 	BinarySearch(S, k, 0, size()-1);
		return e;
	}
	public Entry<K,V> BinarySearch(ArrayIndexList S, K k, int low, int high) {
		int mid;
		if (low > high)
      		return null;
		else
      		mid = (low+high)/2;
      	Entry<K,V> e = (Entry<K,V>) S.get(mid);
      	if (c.compare(k, e.getKey()) == 0) 
        	return e;
      	else if (c.compare(k, e.getKey()) < 0)
        	return BinarySearch(S,k,low,mid-1);
      	else
        	return BinarySearch(S,k,mid+1,high);
	}
	public int BinarySearchIndex(ArrayIndexList S, K k, int low, int high) {
		int mid;
		if (low > high)
      		return -1;
		else
      		mid = (low+high)/2;
      	Entry<K,V> e = (Entry<K,V>) S.get(mid);
      	if (c.compare(k, e.getKey()) == 0) 
        	return mid;
      	else if (c.compare(k, e.getKey()) < 0)
        	return BinarySearchIndex(S,k,low,mid-1);
      	else
        	return BinarySearchIndex(S,k,mid+1,high);
	}
	/*public void BinarySearchAllIt(PositionList<Entry<K,V>> P, ArrayIndexList S, K k, int low, int high) {
		int mid;
		while (low <= high) {
      		mid = (low+high)/2;
    	  	Entry<K,V> e = (Entry<K,V>) S.get(mid);
		  	if (k == e.getKey()) {
				P.addLast(e);
				int i = mid-1;
				while (c.compare(((Entry<K,V>) S.get(i)).getKey(), k) == 0)
					P.addLast((Entry<K,V>) S.get(i--));
				i = mid +1;
				while (c.compare(((Entry<K,V>) S.get(i)).getKey(), k) == 0)
					P.addLast((Entry<K,V>) S.get(i++));
			}
		  	else if (c.compare(k, e.getKey()) < 0)
		    	high = mid-1;
		  	else
		    	low = mid+1;
		}
	}*/
}
