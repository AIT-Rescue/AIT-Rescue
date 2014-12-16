package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.concurrent.ConcurrentHashMap;

public class Cache<V> {

    private ConcurrentHashMap<Long, V> map;

    public Cache() {
        this.map = new ConcurrentHashMap<>();
    }

    public boolean contains(EntityID rowKey, EntityID columnKey) {
        long key = (((long)rowKey.getValue()) << 32) + ((long)columnKey.getValue());
        return this.map.containsKey(key);
    }

    public void clear() {
        this.map.clear();
    }

    public V get(EntityID rowKey, EntityID columnKey) {
        long key = (((long)rowKey.getValue()) << 32) + ((long)columnKey.getValue());
        return this.map.get(key);
    }

    public V put(EntityID rowKey, EntityID columnKey, V value) {
        long key = (((long)rowKey.getValue()) << 32) + ((long)columnKey.getValue());
        return this.map.put(key, value);
    }

    public V remove(EntityID rowKey, EntityID columnKey) {
        long key = (((long)rowKey.getValue()) << 32) + ((long)columnKey.getValue());
        return this.map.remove(key);
    }
}
