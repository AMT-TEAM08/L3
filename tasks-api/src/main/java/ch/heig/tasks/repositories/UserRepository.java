package ch.heig.tasks.repositories;

import ch.heig.tasks.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findById(int id);
    List<UserEntity> findAll();
}