package ch.heig.tasks.repositories;

import ch.heig.tasks.Entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, String> {
    @Override
    List<TagEntity> findAllById(Iterable<String> strings);
}