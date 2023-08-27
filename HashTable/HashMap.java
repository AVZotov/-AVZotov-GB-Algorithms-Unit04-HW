package Seminar004.CW.Seminar.HashTable;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class HashMap<K, V> implements Iterable<HashMap.Entity> {
    private static final int INIT_COUNT_BUCKET = 16;
    private static final double LOAD_FACTOR = 0.5;
    private int recordsAmount = 0;
    private Bucket[] buckets;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (Bucket bucket : buckets) {
            if (bucket != null) {
                for (Entity entity : bucket) {
                    stringBuilder.append(String.format(" %s", entity));
                }
            }
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    public HashMap() {
        buckets = new HashMap.Bucket[INIT_COUNT_BUCKET];
    }

    public HashMap(int initCount) {
        buckets = new HashMap.Bucket[initCount];
    }

    public V put(K key, V value) {
        if (buckets.length * LOAD_FACTOR <= recordsAmount) {
            resize();
        }
        int index = getBucketIndex(key);
        Bucket bucket = buckets[index];

        if (bucket == null) {
            bucket = new Bucket();
            buckets[index] = bucket;
        }

        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;
        V buf = bucket.add(entity);

        if (buf == null) {
            recordsAmount++;
        }
        return buf;
    }

    public V get(K key) {
        Bucket bucket = buckets[getBucketIndex(key)];
        if (bucket == null) {
            return null;
        }
        return bucket.get(key);
    }

    public V remove(K key) {
        int index = getBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null) {
            return null;
        }
        V buf = bucket.delete(key);

        if (buf != null) {
            recordsAmount--;
        }
        return buf;
    }

    private void resize() {
        Bucket[] old = buckets;
        buckets = new HashMap.Bucket[old.length * 2];
        recordsAmount = 0;

        for (int i = 0; i < old.length; i++) {
            Bucket bucket = old[i];
            if (bucket != null) {
                Bucket.Node node = bucket.head;

                while (node != null) {
                    put(node.value.key, node.value.value);
                    node = node.next;
                }
            }
        }
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @NotNull
    @Override
    public Iterator<HashMap.Entity> iterator() {
        return new HashMapIterator();
    }

    class HashMapIterator implements Iterator<HashMap.Entity> {

        int currentIndex = 0;
        Bucket.Node currentNode;
        Bucket currentBucket;

        public HashMapIterator() {
            currentBucket = buckets[currentIndex];
            currentNode = null;
        }


        @Override
        public boolean hasNext() {
            if (currentIndex >= buckets.length)
                return false;

            if (currentBucket != null && currentBucket.head != null)
                return true;

            while (currentIndex < buckets.length) {
                if (buckets[currentIndex] != null) {
                    currentBucket = buckets[currentIndex];
                    return true;
                }
                currentIndex++;
            }
            return false;
        }

        @Override
        public Entity next() {
            if (currentNode == null) {
                currentNode = currentBucket.head;
            }
            Entity entity = currentNode.value;
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                currentIndex++;
                currentNode = null;
                if (currentIndex < buckets.length) {
                    currentBucket = buckets[currentIndex];
                }
            }
            return entity;
        }
    }

    class Entity {
        K key;
        V value;

        @Override
        public String toString() {
            return String.format("{key:%s\tvalue:%s}", key, value);
        }
    }

    class Bucket implements Iterable<Entity>{
        Node head;

        public V add(Entity entity) {
            Node node = new Node();
            node.value = entity;

            if (head == null) {
                head = node;
                return null;
            } else {
                Node currentNode = head;

                while (true) {
                    if (currentNode.value.key.equals(entity.key)) {
                        V oldValue = currentNode.value.value;
                        currentNode.value.value = entity.value;
                        return oldValue;
                    }
                    if (currentNode.next != null) {
                        currentNode = currentNode.next;
                    } else {
                        currentNode.next = node;
                        return null;
                    }
                }
            }
        }

        public V get(K key) {
            Node node = head;

            while (node != null) {
                if (node.value.key.equals(key)) {
                    return node.value.value;
                }
                node = node.next;
            }
            return null;
        }

        public V delete(K key) {
            if (head == null) {
                return null;
            }
            if (head.value.key.equals(key)) {
                V buf = head.value.value;
                head = head.next;
                return buf;
            } else {
                Node node = head;

                while (node.next != null) {
                    if (node.next.value.key.equals(key)) {
                        V buf = node.next.value.value;
                        node.next = node.next.next;
                        return buf;
                    }
                    node = node.next;
                }
                return null;
            }
        }

        @NotNull
        @Override
        public Iterator iterator() {
            return new BucketIterator();
        }

        private class BucketIterator implements Iterator<HashMap.Entity> {

            Node currentNode;

            public BucketIterator() {
                currentNode = head;
            }

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public Entity next() {
                Entity entity = currentNode.value;
                currentNode = currentNode.next;
                return entity;
            }
        }

        class Node {
            Node next;
            Entity value;
        }

    }
}
