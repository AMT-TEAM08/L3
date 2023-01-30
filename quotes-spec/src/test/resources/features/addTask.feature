Feature: Adding tasks to the task list

  Scenario: Adding a task to the task list of user
    Given a task with the title "My first task" and the user id 1
    When I POST the task to the /tasks endpoint
    Then the server should respond with a 201 status code

  Scenario: Adding a task with an empty title
    Given a task with the title "" and the user id 1
    When I POST the task to the /tasks endpoint
    Then the server should respond with a 400 status code

  Scenario: Adding a task with a user id that does not exist
    Given a task with the title "My first task" and the user id 0
    When I POST the task to the /tasks endpoint
    Then the server should respond with a 404 status code