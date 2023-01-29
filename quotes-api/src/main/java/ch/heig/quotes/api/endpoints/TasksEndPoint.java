package ch.heig.quotes.api.endpoints;

import ch.heig.quotes.Entities.TaskEntity;
import ch.heig.quotes.api.TasksApi;
import ch.heig.quotes.api.exceptions.TaskNotFoundException;
import ch.heig.quotes.api.model.Task;
import ch.heig.quotes.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TasksEndPoint implements TasksApi {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseEntity<List<Task>> getTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        List<Task> tasks  = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            Task task = new Task();
            task.setId(taskEntity.getId());
            task.setName(taskEntity.getName());
            task.setDescription(taskEntity.getDescription());
            task.setDueDate(taskEntity.getDueDate());
            task.setUserId(taskEntity.getUser().getId());
            tasks.add(task);
        }
        return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
    }

    public ResponseEntity<Void> addTask(@RequestBody Task task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setName(task.getName());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setDueDate(task.getDueDate());
        TaskEntity taskAdded = taskRepository.save(taskEntity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskAdded.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<Task> getTask(Integer id) {
        Optional<TaskEntity> opt = taskRepository.findById(id);
        if (opt.isPresent()) {
            TaskEntity taskEntity = opt.get();
            Task task = new Task();
            task.setId(taskEntity.getId());
            task.setName(taskEntity.getName());
            task.setDescription(taskEntity.getDescription());
            task.setDueDate(taskEntity.getDueDate());
            task.setUserId(taskEntity.getUser().getId());
            return new ResponseEntity<Task>(task, HttpStatus.OK);
        } else {
//            return ResponseEntity.notFound().build();
            throw new TaskNotFoundException(id);
        }
    }

}
