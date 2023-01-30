package ch.heig.tasks.spec;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.TaskEndPointApi;
import org.openapitools.client.model.TaskRequest;
import org.openapitools.client.model.TaskResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskSteps {

    private final TaskEndPointApi api = new TaskEndPointApi();
    private TaskRequest taskRequest;
    private int statusCode;

    private List<TaskResponse> tasks;
    private TaskResponse task;
    private String location;

    @Given("a task with the title {string} and the user id {int}")
    public void aTaskWithTheTitle(String title, int userId) {
        taskRequest = new TaskRequest();
        taskRequest.setName(title);
        taskRequest.setUserId(userId);
    }

    @When("I POST the task to the \\/tasks endpoint")
    public void iPOSTTheTaskToTheTasksEndpoint() {
        try {
            ApiResponse<Void> res = api.addTaskWithHttpInfo(taskRequest);
            statusCode = res.getStatusCode();
            location = res.getHeaders().get("Location").get(0);
        } catch (ApiException e) {
            statusCode = e.getCode();
        }
    }

    @Then("the server should respond with a {int} status code")
    public void theServerShouldRespondWithAStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, statusCode);
    }

    @When("I GET the \\/tasks endpoint")
    public void iGETTheTasksEndpoint() {
        try {
            ApiResponse<List<TaskResponse>> res = api.getTasksWithHttpInfo();
            statusCode = res.getStatusCode();
            tasks = res.getData();
        } catch (ApiException e) {
            statusCode = e.getCode();
        }
    }
    @When("I GET the created task endpoint")
    public void iGETTheCreatedTaskEndpoint() {
        try {
            String base = "http://localhost:9090/api/tasks/";
            String id = location.substring(base.length());
            ApiResponse<TaskResponse> res = api.getTaskWithHttpInfo(Integer.parseInt(id));
            statusCode = res.getStatusCode();
            task = res.getData();
        } catch (ApiException e) {
            statusCode = e.getCode();
        }
    }

    @When("I GET the \\/tasks\\/{int} endpoint")
    public void iGETTheTasksEndpoint(int id) {
        try {
            ApiResponse<TaskResponse> res = api.getTaskWithHttpInfo(id);
            statusCode = res.getStatusCode();
            task = res.getData();
        } catch (ApiException e) {
            statusCode = e.getCode();
        }
    }
    @Then("the response body should contain a task with the title {string}")
    public void theResponseBodyShouldContainATaskWithTheTitle(String title) {
        assertTrue(tasks.stream().anyMatch(t -> {
            assert t.getName() != null;
            return t.getName().equals(title);
        }));
    }

    @Then("the response body should be a task with the title {string}")
    public void theResponseBodyShouldBeATaskWithTheTitle(String title) {
        assertEquals(title, task.getName());
    }
}
