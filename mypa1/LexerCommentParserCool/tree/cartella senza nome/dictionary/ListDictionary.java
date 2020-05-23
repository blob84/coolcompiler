import position.*;
import nodelist.*;
import java.util.Iterator;

public class ListDictionary<K, V> implements Dictionary<K, V> {
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
	protected PositionList<Entry<K,V>> S;
	
	public ListDictionary() {
		 S = new NodePositionList<Entry<K,V>>();
	}
	public int size() {
		return S.size();
	}
	public boolean isEmpty() {
		return S.isEmpty();
	}
	public Entry<K,V> insert(K k, V v) {
		Entry<K,V> e = new ListEntry<K,V>(k, v);
		S.addLast(e);
		return e;		
	}
	public Entry<K,V> remove(Entry<K,V> e) {
		for (Position<Entry<K,V>> p : S.positions())
			if (p.element() == e) {
				S.remove(p);
				return e;
			}
		return null;
	}	
	public Iterable<Entry<K,V>> entries() {
		return S;
	}
	/* restituisce un oggetto iterabile di entry con chiave k */
	public Iterator<Entry<K,V>> findAll(K k) {
		PositionList<Entry<K,V>> L = new NodePositionList<Entry<K,V>>();
		for (Entry<K,V> e : entries())
			if (e.getKey() == k)
				L.addLast(e);
		return L.iterator();
	}
	/*public Iterable<Entry<K,V>> findAll(K k) {
		PositionList<Entry<K,V>> L = new NodePositionList<Entry<K,V>>();
		for (Entry<K,V> e : entries())
			if (e.getKey() == k)
				L.addLast(e);
		return L;
	}*/
	/*public Entry<K,V> find(K k) {
		for (Position<Entry<K,V>> e : S.positions())
			if (e.element().getKey() == k) {
				return e.element();
			}
		return null;
	}*/
}
