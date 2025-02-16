package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.TagEntity;
import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.TasksApi;
import ch.heig.tasks.api.exceptions.TaskNotFoundException;
import ch.heig.tasks.api.exceptions.UserNotFoundException;
import ch.heig.tasks.api.model.*;
import ch.heig.tasks.mappers.TaskMapper;
import ch.heig.tasks.repositories.TagRepository;
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

    @Autowired
    private TagRepository tagRepository;

    @Override
    public ResponseEntity<List<TaskResponse>> getTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        List<TaskResponse> tasks  = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            tasks.add(TaskMapper.mapTaskEntityToTaskResponse(taskEntity));
        }
        return new ResponseEntity<>(tasks,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest task) {
        if (task.getUserId() == null) {
            throw new UserNotFoundException(null);
        }
        User user = usersEndPoint.getUser(task.getUserId()).getBody();
        if (user == null) {
            throw new UserNotFoundException(task.getUserId());
        }
        if (task.getName() == null || task.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TaskEntity taskEntity = new TaskEntity(
                task.getName(),
                task.getDescription(),
                task.getDueDate(),
                new UserEntity(user),
                new ArrayList<>());

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

    @Override
    public ResponseEntity<Void> tasksTaskIdPatch(Integer taskId, TaskRequest taskRequest) {
        Optional<TaskEntity> opt = taskRepository.findById(taskId);

        if (opt.isPresent()) {
            User user = usersEndPoint.getUser(taskRequest.getUserId()).getBody();
            if (user == null) {
                throw new UserNotFoundException(taskRequest.getUserId());
            }
            TaskEntity taskEntity = opt.get().update(TaskMapper.mapTaskRequestToTaskEntity(taskRequest,new UserEntity(user)));
            taskRepository.save(taskEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    /**
     * PUT /tasks/{task_id}/assign : Assign a user to a task
     *
     * @param taskId                      The ID of the task (required)
     * @param tasksTaskIdAssignPutRequest (required)
     * @return User assigned successfully (status code 204)
     * or Task or user not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<Void> tasksTaskIdAssignPut(Integer taskId, TasksTaskIdAssignPutRequest tasksTaskIdAssignPutRequest) {
        Optional<TaskEntity> opt = taskRepository.findById(taskId);

        if (opt.isPresent()) {
            User user = usersEndPoint.getUser(tasksTaskIdAssignPutRequest.getUserId()).getBody();
            if (user == null) {
                throw new UserNotFoundException(tasksTaskIdAssignPutRequest.getUserId());
            }
            TaskEntity taskEntity = opt.get();
            UserEntity userEntity = new UserEntity(user);
            taskEntity.setUser(userEntity);
            taskRepository.save(taskEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    /**
     * PUT /tasks/{task_id}/tags : Add tags to a task
     *
     * @param taskId                    The ID of the task (required)
     * @param tasksTaskIdTagsPutRequest (required)
     * @return Tags added successfully (status code 204)
     * or Task not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<Void> tasksTaskIdTagsPut(Integer taskId, TasksTaskIdTagsPutRequest tasksTaskIdTagsPutRequest) {
        Optional<TaskEntity> opt = taskRepository.findById(taskId);

        if (opt.isPresent()) {
            TaskEntity taskEntity = opt.get();
            List<TagEntity> tagEntities = tagRepository.findAllById(tasksTaskIdTagsPutRequest.getTags());
            taskEntity.setTags(tagEntities);
            taskRepository.save(taskEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    /**
     * GET /tasks/{tag_name} : Retrieve a list of all tasks by tag name
     *
     * @param tagName The name of the tag (required)
     * @return List of tasks retrieved successfully (status code 200)
     * or Tag not found (status code 404)
     */
    @Override
    public ResponseEntity<List<TaskResponse>> getTasksByTagName(String tagName) {
        List<TaskEntity> taskEntities = taskRepository.findAllByTags_Name(tagName);
        List<TaskResponse> tasks  = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            tasks.add(TaskMapper.mapTaskEntityToTaskResponse(taskEntity));
        }
        return new ResponseEntity<>(tasks,HttpStatus.OK);
    }
}
