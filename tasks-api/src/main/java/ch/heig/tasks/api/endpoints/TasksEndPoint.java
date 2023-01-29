package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.TasksApi;
import ch.heig.tasks.api.exceptions.TaskNotFoundException;
import ch.heig.tasks.api.model.TaskRequest;
import ch.heig.tasks.api.model.TaskResponse;
import ch.heig.tasks.mappers.TaskMapper;
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
import java.util.Objects;
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
            tasks.add(TaskMapper.mapTaskEntityToTaskResponse(taskEntity));
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
            return new ResponseEntity<>(TaskMapper.mapTaskEntityToTaskResponse(taskEntity), HttpStatus.OK);
        } else {
//            return ResponseEntity.notFound().build();
            throw new TaskNotFoundException(id);
        }
    }

    /**
     * PATCH /tasks/{task_id} : Update a task
     *
     * @param taskId      The ID of the task (required)
     * @param taskRequest (required)
     * @return Task updated successfully (status code 200)
     * or Task not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<TaskResponse> tasksTaskIdPatch(Integer taskId, TaskRequest taskRequest) {
        Optional<TaskEntity> opt = taskRepository.findById(taskId);

        if (opt.isPresent()) {

            UserEntity user = usersEndPoint.getUser(taskRequest.getUserId()).getBody();

            if (user == null) {
                return ResponseEntity.badRequest().build();
            }

            TaskEntity taskEntity = opt.get();
            taskEntity.setName(taskRequest.getName());
            taskEntity.setDescription(taskRequest.getDescription());
            taskEntity.setDueDate(taskRequest.getDueDate());
            taskEntity.setUser(user);
            taskRepository.save(taskEntity);
            return new ResponseEntity<TaskResponse>(TaskMapper.mapTaskEntityToTaskResponse(taskEntity), HttpStatus.OK);
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    /**
     * PUT /tasks/{task_id} : Replace a task
     *
     * @param taskId      The ID of the task (required)
     * @param taskRequest (required)
     * @return Task replaced successfully (status code 200)
     * or Task not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<Void> tasksTaskIdPut(Integer taskId, TaskRequest taskRequest) {
        Optional<TaskEntity> opt = taskRepository.findById(taskId);
        if (opt.isPresent()) {
            TaskEntity taskEntity = opt.get();
            taskEntity.setName(taskRequest.getName());
            taskEntity.setDescription(taskRequest.getDescription());
            taskEntity.setDueDate(taskRequest.getDueDate());
            taskEntity.setUser(usersEndPoint.getUser(taskRequest.getUserId()).getBody());
            taskRepository.save(taskEntity);
            return ResponseEntity.ok().build();
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

}
