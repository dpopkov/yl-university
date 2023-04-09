package io.ylab.intensive.lesson05.eventsourcing.shared;

import java.util.List;
import java.util.Optional;

public interface ReadingDao<T> {

    Optional<T> findById(Long id);

    List<T> findAll();
}
