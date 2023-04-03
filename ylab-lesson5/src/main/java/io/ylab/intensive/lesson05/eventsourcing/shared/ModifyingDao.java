package io.ylab.intensive.lesson05.eventsourcing.shared;

public interface ModifyingDao<T> {

    void delete(Long id);

    void save(T object);
}
