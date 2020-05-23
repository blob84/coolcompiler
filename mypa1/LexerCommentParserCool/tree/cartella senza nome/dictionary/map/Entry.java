package map;
public interface Entry<K, V> {
    /** Restituisce la chiave memorizzata in questa entry. */
    public K getKey();
    /** Restituisce il valore memorizzato in questa entry. */
    public V getValue();
}

