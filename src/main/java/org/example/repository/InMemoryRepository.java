package org.example.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository <T extends Identifiable<ID>, ID> implements Repository<T, ID>{
    private final Map<ID, T> storage = new ConcurrentHashMap<>();

    @Override
    public T save(T entity) {
        Objects.requireNonNull(entity, "entity cannot be null");
        Objects.requireNonNull(entity.getId(), "entity id cannot be null");
        storage.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity, "entity cannot be null");
        T update = storage.computeIfPresent(entity.getId(), (id, old) -> entity);
        if (update == null){
            throw new NoSuchElementException("Entity is not found by id: " + entity.getId() + ", update unreachable");
        }
        return update;
    }
    @Override
    public boolean deleteById(ID id) {
        return storage.remove(id) != null;
    }
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }
    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
    @Override
    public List<T> findAll(Comparator<T> comparator) {
        List<T> result = new ArrayList<>(storage.values());
        result.sort(comparator);
        return result;
    }
    public int size() {
        return storage.size();
    }
}




