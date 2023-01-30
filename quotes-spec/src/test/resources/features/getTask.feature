Feature: Retrieving tasks from the task list

  Scenario: Retrieving all tasks from the task list of user
    Given a task with the title "My first task" and the user id 1
    And I POST the task to the /tasks endpoint
    When I GET the /tasks endpoint
    Then the server should respond with a 200 status code
    And the response body should contain a task with the title "My first task"

  Scenario: Retrieving a task from the task list of user
    Given a task with the title "My first task" and the user id 1
    And I POST the task to the /tasks endpoint
    When I GET the created task endpoint
    Then the server should respond with a 200 status code
    And the response body should be a task with the title "My first task"

  Scenario: Retrieving a task from the task list of user that does not exist
    When I GET the /tasks/0 endpoint
    Then the server should respond with a 404 status code