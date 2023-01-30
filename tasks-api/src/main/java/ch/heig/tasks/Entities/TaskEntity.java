package ch.heig.tasks.Entities;


import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity(name = "TaskResponse")
@Table(name = "Tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public TaskEntity() {}

    public TaskEntity(String name, String description, OffsetDateTime dueDate, UserEntity user) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.user = user;
    }

    public TaskEntity update(TaskEntity task) {
        this.name = task.name;
        this.description = task.description;
        this.dueDate = task.dueDate;
        this.user = task.user;
        return task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
