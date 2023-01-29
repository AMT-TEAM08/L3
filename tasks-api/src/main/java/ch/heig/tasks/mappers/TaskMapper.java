package ch.heig.tasks.mappers;

import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.model.TaskRequest;
import ch.heig.tasks.api.model.TaskResponse;

public class TaskMapper {

    public static TaskResponse mapTaskEntityToTaskResponse(TaskEntity taskEntity) {
        TaskResponse task = new TaskResponse();
        task.setId(taskEntity.getId());
        task.setName(taskEntity.getName());
        task.setDescription(taskEntity.getDescription());
        task.setDueDate(taskEntity.getDueDate());
        task.setUserId(taskEntity.getUser().getId());
        return task;
    }

    public static TaskEntity mapTaskRequestToTaskEntity(TaskRequest taskRequest, UserEntity user) {
        TaskEntity task = new TaskEntity();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setUser(user);
        return task;
    }
}
