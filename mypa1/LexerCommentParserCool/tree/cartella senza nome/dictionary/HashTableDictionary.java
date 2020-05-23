/** A hash table with linear probing and the MAD hash function */
import java.util.Iterator;
import position.*;
import nodelist.*;

public class HashTableDictionary<K,V> implements Dictionary<K,V> {
  protected int n = 0; 		// number of entries in the dictionary
  protected int capacity;	// capacity of the bucket array
  protected ListDictionary<K,V>[] bucket;// bucket array
  protected int scale, shift;   // the shift and scaling factors
  /** Creates a hash table with initial capacity 1023. */
  public HashTableDictionary() { this(1023); }
  /** Creates a hash table with the given capacity. */
  public HashTableDictionary(int cap) {
    capacity = cap;
    bucket = (ListDictionary<K,V>[]) new ListDictionary[capacity]; // safe cast
    java.util.Random rand = new java.util.Random();
    scale = rand.nextInt(capacity-1) + 1;
    shift = rand.nextInt(capacity);
  }
  /** Determines whether a key is valid. */
  protected void checkKey(K k) {
    if (k == null) throw new InvalidKeyException("Invalid key: null.");
  }
  /** Hash function applying MAD method to default hash code. */
  public int hashValue(K key) {
    return Math.abs(key.hashCode()*scale + shift) % capacity;
  }
  /** Returns the number of entries in the hash table. */
  public int size() { return n; }
  /** Returns whether or not the table is empty. */
  public boolean isEmpty() { return (n == 0); }
  /** Doubles the size of the hash table and rehashes all the entries. */
  	protected void rehash() {
		capacity = 2*capacity;
		ListDictionary<K,V>[] old = bucket;
		bucket = (ListDictionary<K,V>[]) new ListDictionary[capacity]; // new bucket is twice as big 
		java.util.Random rand = new java.util.Random();
		scale = rand.nextInt(capacity-1) + 1;    	// new hash scaling factor
		shift = rand.nextInt(capacity); 		// new hash shifting factor
		for (int i=0; i<old.length; i++) {
			ListDictionary<K,V> l = old[i];
		  	if (l != null) { // a valid entry
				bucket[i] = l;
		  	}
		}
  	}
  	public Entry<K,V> insert(K key, V value) {
		if ((n+1)/capacity > 1)
			rehash();
		ListDictionary<K,V> l = bucket[hashValue(key)];
		Entry<K,V> e = l.insert(key, value);
		n++;
		return e; 
	}
	public Iterator<Entry<K,V>> findAll(K k) {
		return bucket[hashValue(k)].findAll(k);
	}
  	public Entry<K,V> remove(Entry<K,V> e) {
		ListDictionary<K,V> l = bucket[hashValue(e.getKey())];
		Entry<K,V> t = l.remove(e);
		if (t != null)
			n--;
		return t;
	}
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> P = new NodePositionList<Entry<K,V>>();
		for (int i = 0; i < capacity; i++) {
			if (bucket[i] != null) {
				for (Entry<K,V> e : bucket[i].entries())
					P.addLast(e);
			}
		}
		return P;
	}
} 
