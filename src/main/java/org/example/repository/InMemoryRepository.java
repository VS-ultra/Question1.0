package org.example.repository;

import java.util.*;

public class InMemoryRepository <T extends Identifiable<ID>, ID> implements Repository<T, ID>{
    private final Map<ID, T> storage = new HashMap<>();

    @Override
    public T save(T entity) {
        return storage.put(entity.getId(), entity);
    }
    @Override
    public T update(T entity) {
        if (!storage.containsValue(entity.getId())){
            throw new NoSuchElementException("Entity is not found by id: " + entity.getId() + " update unreachable");
        }
        return storage.put(entity.getId(), entity);
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
