package ch.heig.tasks.Entities;

import ch.heig.tasks.api.model.Tag;
import jakarta.persistence.*;

import java.util.List;


@Entity(name = "Tag")
@Table(name = "Tags")
public class TagEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    private List<TaskEntity> tasks;


    public TagEntity() {
    }

    public TagEntity(String name, String description, List<TaskEntity> tasks) {
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }

    public TagEntity(Tag tag) {
        this.name = tag.getName();
        this.description = tag.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
