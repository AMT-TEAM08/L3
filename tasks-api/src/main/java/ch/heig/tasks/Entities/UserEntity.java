package ch.heig.tasks.Entities;

import ch.heig.tasks.api.model.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user")
    private List<TaskEntity> tasks = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public User toUser() {
        User u = new User();
        u.setId(this.id);
        u.setName(this.name);
        return u;
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

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public void addTask(TaskEntity task) {
        this.tasks.add(task);
    }
}