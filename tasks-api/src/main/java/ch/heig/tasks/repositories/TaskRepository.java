package ch.heig.tasks.repositories;

import ch.heig.tasks.Entities.TaskEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    TaskEntity findById(int id);

    List<TaskEntity> findAll();

    List<TaskEntity> findAllByTags_Name(String name);
}