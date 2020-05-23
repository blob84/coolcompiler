/** A hash table with linear probing and the MAD hash function */
import java.util.Iterator;
import position.*;
import nodelist.*;

public class HashTableMap<K,V> implements Map<K,V> {
  public static class HashEntry<K,V> implements Entry<K,V> {
    protected K key;
    protected V value;
    public HashEntry(K k, V v) { key = k; value = v; }
    public V getValue() { return value; }
    public K getKey() { return key; }
    public V setValue(V val) {
      V oldValue = value;
      value = val;
      return oldValue;
      }
    public boolean equals(Object o) {
      HashEntry<K,V> ent;
      try { ent = (HashEntry<K,V>) o; }
      catch (ClassCastException ex) { return false; }
      return (ent.getKey() == key) && (ent.getValue() == value);
    }
  }
  protected Entry<K,V> AVAILABLE = new HashEntry<K,V>(null, null); // marker 
  protected int n = 0; 		// number of entries in the dictionary
  protected int capacity;	// capacity of the bucket array
  protected Entry<K,V>[] bucket;// bucket array
  protected int scale, shift;   // the shift and scaling factors
  /** Creates a hash table with initial capacity 1023. */
  public HashTableMap() { this(1023); }
  /** Creates a hash table with the given capacity. */
  public HashTableMap(int cap) {
    capacity = cap;
    bucket = (Entry<K,V>[]) new Entry[capacity]; // safe cast
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
  /** Returns an iterable object containing all of the keys. */
  public Iterable<K> keys() {
    PositionList<K> keys = new NodePositionList<K>();
    for (int i=0; i<capacity; i++) 
      if ((bucket[i] != null) && (bucket[i] != AVAILABLE)) 
	keys.addLast(bucket[i].getKey());
    return keys;
  }
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K, V>> entries = new NodePositionList<Entry<K, V>>();
		for (int i=0; i<capacity; i++) 
		  if ((bucket[i] != null) && (bucket[i] != AVAILABLE)) 
			entries.addLast(bucket[i]);
		return entries;
  	}
	public Iterable<Entry<K, V>> sameHashEntries(K key) {
		/*PositionList<Entry<K, V>> entries = new NodePositionList<Entry<K, V>>();
		checkKey(key);
		for (int i=0; i<capacity; i++) 
		  if ((bucket[i] != null) && (bucket[i] != AVAILABLE) && bucket[i].getKey().equals((key)) 
			entries.addLast(bucket[i]);
		return entries; */
		 PositionList<Entry<K,V>> listToReturn = new NodePositionList<Entry<K,V>>();
		 checkKey(key);
		 int i = hashValue(key);
		 int j = i;
		 do {
		 	Entry<K,V> e = bucket[i];
		  	if ( e == null )
				break;
		  	if (hashValue(e.getKey()) == j)
				listToReturn.addLast(e);
		  	i = (i + 1) % capacity;
		 } while (i != j);
		return listToReturn;
	}
  /** Helper search method - returns index of found key or -(a + 1),
   * where a is the index of the first empty or available slot found. */
  protected int findEntry(K key) throws InvalidKeyException {
    int avail = -1;
    checkKey(key);
    int i = hashValue(key);
    int j = i;
    do {
      Entry<K,V> e = bucket[i];
      if ( e == null) {
	if (avail < 0)
	  avail = i;	// key is not in table
	break;
      }
      if (key.equals(e.getKey())) // we have found our key
	return i;	// key found
      if (e == AVAILABLE) {	// bucket is deactivated
	if (avail < 0)
	  avail = i;	// remember that this slot is available
      }
      i = (i + 1) % capacity;	// keep looking
    } while (i != j);
    return -(avail + 1);  // first empty or available slot
  }
  /** Returns the value associated with a key. */
  public V get (K key) throws InvalidKeyException {
    int i = findEntry(key);  // helper method for finding a key
    if (i < 0) return null;  // there is no value for this key
    return bucket[i].getValue();     // return the found value in this case
  }
  /** Put a key-value pair in the map, replacing previous one if it exists. */
  public V put (K key, V value) throws InvalidKeyException {
    int i = findEntry(key); //find the appropriate spot for this entry
    if (i >= 0)	//  this key has a previous value
      return ((HashEntry<K,V>) bucket[i]).setValue(value); // set new value 
    if (n >= capacity/2) {
      rehash(); // rehash to keep the load factor <= 0.5
      i = findEntry(key); //find again the appropriate spot for this entry
    }
    bucket[-i-1] = new HashEntry<K,V>(key, value); // convert to proper index
    n++;
    return null; 	// there was no previous value
  }
  /** Doubles the size of the hash table and rehashes all the entries. */
  protected void rehash() {
    capacity = 2*capacity;
    Entry<K,V>[] old = bucket;
    bucket = (Entry<K,V>[]) new Entry[capacity]; // new bucket is twice as big 
    java.util.Random rand = new java.util.Random();
    scale = rand.nextInt(capacity-1) + 1;    	// new hash scaling factor
    shift = rand.nextInt(capacity); 		// new hash shifting factor
    for (int i=0; i<old.length; i++) {
      Entry<K,V> e = old[i];
      if ((e != null) && (e != AVAILABLE)) { // a valid entry
	int j = - 1 - findEntry(e.getKey());
	bucket[j] = e;
      }
    }
  }
  /** Removes the key-value pair with a specified key. */
  public V remove (K key) throws InvalidKeyException {
    int i = findEntry(key);  	// find this key first
    if (i < 0) return null;  	// nothing to remove
    V toReturn = bucket[i].getValue();
    bucket[i] = AVAILABLE; 		// mark this slot as deactivated
    n--;
    return toReturn;
  }
} 
