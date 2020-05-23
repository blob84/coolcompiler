package tree;

package dictionary;

import java.util.Comparator;
import nodelist.*;
import binarytree.BTPosition;
import binarytree.LinkedBinaryTree;
import lasdException.InvalidEntryException;
import position.Position;
import priorityQueue.DefaultComparator;
import priorityQueue.InvalidKeyException;
import entry.Entry;

public class BinSearchTree<K,V> 
extends LinkedBinaryTree<Entry<K,V>> implements Dictionary<K,V> {

protected Comparator<K> C;	// comparator
protected Position<Entry<K,V>> 
            actionPos; // insert node or removed node's parent
protected int numEntries = 0;	// number of entries
/** Creates a BinSearchTree with a default comparator. */
public BinSearchTree()  { 
  C = new DefaultComparator<K>(); 
  addRoot(null);
}
/** Creates a BinSearchTree with the given comparator. */

public BinSearchTree(Comparator<K> c)  { 
  C = c; 
  addRoot(null);
}
/** Nested class for location-aware binary search tree entries */
protected static class BSTEntry<K,V> implements Entry<K,V> {
  protected K key;
  protected V value;
  protected Position<Entry<K,V>> pos;
  BSTEntry() { /* default constructor */ }
  BSTEntry(K k, V v, Position<Entry<K,V>> p) { 
    key = k; value = v; pos = p;
  }
  public K getKey() { return key; }
  public V getValue() { return value; }
  public Position<Entry<K,V>> position() { return pos; }
}

/** Extracts the key of the entry at a given node of the tree. */
protected K key(Position<Entry<K,V>> position)  {
  return position.element().getKey();
}
/** Extracts the value of the entry at a given node of the tree. */  
protected V value(Position<Entry<K,V>> position)  { 
  return position.element().getValue(); 
}
/** Extracts the entry at a given node of the tree. */  
protected Entry<K,V> entry(Position<Entry<K,V>> position)  { 
  return position.element();
}
/** Replaces an entry with a new entry (and reset the entry's location) */
protected void replaceEntry(Position <Entry<K,V>> pos, Entry<K,V> ent) {
  ((BSTEntry<K,V>) ent).pos = pos;
  replace(pos, ent);
}
/** Checks whether a given key is valid. */  
protected void checkKey(K key) throws InvalidKeyException {
if(key == null)  // just a simple test for now
  throw new InvalidKeyException("null key");
}
/** Checks whether a given entry is valid. */
protected void checkEntry(Entry<K,V> ent) throws InvalidEntryException {
if(ent == null || !(ent instanceof BSTEntry))
  throw new InvalidEntryException("invalid entry");
}
/** Auxiliary method for inserting an entry at an external node */
protected Entry<K,V> insertAtExternal(Position<Entry<K,V>> v, Entry<K,V> e) {
	expandExternal(v,null,null);
	replace(v, e);
	numEntries++;
	return e;
}
/** Auxiliary method for removing an external node and its parent */
protected void removeExternal(Position<Entry<K,V>> v) {
removeAboveExternal(v);
numEntries--;
}
/** Auxiliary method used by find, insert, and remove. */
protected Position<Entry<K,V>> treeSearch(K key, Position<Entry<K,V>> pos) {
if (isExternal(pos)) return pos; // key not found; return external node
else {
  K curKey = key(pos);
  int comp = C.compare(key, curKey);
  if (comp < 0) 
    return treeSearch(key, left(pos));	// search left subtree
  else if (comp > 0)
    return treeSearch(key, right(pos));	// search right subtree
  return pos;		// return internal node where key is found
}
}
/** Adds to L all entries in the subtree rooted at v having keys
* equal to k. */
protected void addAll(PositionList<Entry<K,V>> L, 
		      Position<Entry<K,V>> v, K k) {
if (isExternal(v)) return;
Position<Entry<K,V>> pos = treeSearch(k, v);
if (!isExternal(pos))  {  // we found an entry with key equal to k 
  addAll(L, left(pos), k);
  L.addLast(pos.element()); 	// add entries in inorder
  addAll(L, right(pos), k);
} // this recursive algorithm is simple, but it's not the fastest
}
/** Returns the number of entries in the tree. */
public int size() { return numEntries; }
/** Returns whether the tree is empty. */
public boolean isEmpty() { return size() == 0; }
/** Returns an entry containing the given key.  Returns null if no
  * such entry exists. */
public Entry<K,V> find(K key) throws InvalidKeyException {
  checkKey(key);		// may throw an InvalidKeyException
  Position<Entry<K,V>> curPos = treeSearch(key, root());
  actionPos = curPos;		// node where the search ended
  if (isInternal(curPos)) return entry(curPos);
  return null;
}
/** Returns an iterable collection of all the entries containing the
  * given key. */
public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
  checkKey(key);		// may throw an InvalidKeyException
  PositionList<Entry<K,V>> L = new NodePositionList<Entry<K,V>>();
  addAll(L, root(), key);
  return L;
}
/** Inserts an entry into the tree and returns the newly created entry. */
public Entry<K,V> insert(K k, V x) throws InvalidKeyException {
  checkKey(k);	// may throw an InvalidKeyException
  Position<Entry<K,V>> insPos = treeSearch(k, root());
  while (!isExternal(insPos))  // iterative search for insertion position
    insPos = treeSearch(k, left(insPos));
  actionPos = insPos;	// node where the new entry is being inserted
  return insertAtExternal(insPos, new BSTEntry<K,V>(k, x, insPos));
}
/** Removes and returns a given entry. */
public Entry<K,V> remove(Entry<K,V> ent) throws InvalidEntryException  {
  checkEntry(ent);            // may throw an InvalidEntryException
  Position<Entry<K,V>> remPos = ((BSTEntry<K,V>) ent).position(); 
  Entry<K,V> toReturn = entry(remPos);	// entry to be returned
  if (isExternal(left(remPos))) remPos = left(remPos);  // left easy case
  else if (isExternal(right(remPos))) remPos = right(remPos); // right easy case
  else {			//  entry is at a node with internal children
    Position<Entry<K,V>> swapPos = remPos;	// find node for moving entry
    remPos = right(swapPos);
    do
	remPos = left(remPos);
    while (isInternal(remPos));
    replaceEntry(swapPos, (Entry<K,V>) parent(remPos).element());
  }
  actionPos = sibling(remPos);	// sibling of the leaf to be removed
  removeExternal(remPos);
  return toReturn;
}
/** Returns an iterator containing all entries in the tree. */
public Iterable<Entry<K,V>> entries() {
  PositionList<Entry<K,V>> entries = new NodePositionList<Entry<K,V>>();
  Iterable<Position<Entry<K,V>>> positer = positions();
  for (Position<Entry<K,V>> cur: positer)
    if (isInternal(cur))
	entries.addLast(cur.element());
  return entries;
}

}
//begin#fragment BinSearchTree3
