package ch.heig.quotes.repositories;

import ch.heig.quotes.Entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    TaskEntity findById(int id);
    List<TaskEntity> findAll();
}