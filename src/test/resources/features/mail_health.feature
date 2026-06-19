# This feature checks that the email health check is available
Feature: Mail health monitoring
  The application must report whether its configured SMTP service is reachable.

  Scenario: Actuator registers the mail health check
    Given mail health monitoring is enabled
    When Actuator initializes its health contributors
    Then a mail health contributor is available