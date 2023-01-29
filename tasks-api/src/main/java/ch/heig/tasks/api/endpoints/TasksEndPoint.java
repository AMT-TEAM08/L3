package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.api.TasksApi;
import ch.heig.tasks.api.exceptions.TaskNotFoundException;
import ch.heig.tasks.api.model.TaskRequest;
import ch.heig.tasks.api.model.TaskResponse;
import ch.heig.tasks.repositories.TaskRepository;
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

    @Autowired
    private UsersEndPoint usersEndPoint;

    @Override
    public ResponseEntity<List<TaskResponse>> getTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        List<TaskResponse> tasks  = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            TaskResponse task = new TaskResponse();
            task.setId(taskEntity.getId());
            task.setName(taskEntity.getName());
            task.setDescription(taskEntity.getDescription());
            task.setDueDate(taskEntity.getDueDate());
            task.setUserId(taskEntity.getUser().getId());
            tasks.add(task);
        }
        return new ResponseEntity<List<TaskResponse>>(tasks,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setName(task.getName());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setDueDate(task.getDueDate());
        taskEntity.setUser(usersEndPoint.getUser(task.getUserId()).getBody());
        TaskEntity taskAdded = taskRepository.save(taskEntity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskAdded.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<TaskResponse> getTask(Integer id) {
        Optional<TaskEntity> opt = taskRepository.findById(id);
        if (opt.isPresent()) {
            TaskEntity taskEntity = opt.get();
            TaskResponse task = new TaskResponse();
            task.setId(taskEntity.getId());
            task.setName(taskEntity.getName());
            task.setDescription(taskEntity.getDescription());
            task.setDueDate(taskEntity.getDueDate());
            task.setUserId(taskEntity.getUser().getId());
            return new ResponseEntity<TaskResponse>(task, HttpStatus.OK);
        } else {
//            return ResponseEntity.notFound().build();
            throw new TaskNotFoundException(id);
        }
    }

}
