package org.example.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface Repository <T extends Identifiable<ID>, ID>{
    T save(T entity);
    T update(T entity);
    boolean deleteById(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAll(Comparator<T> comparator);

    default List<T> findAllSorted(Comparator<T> comparator, boolean ascending) {
        return findAll(ascending ? comparator : comparator.reversed());
    }
}
