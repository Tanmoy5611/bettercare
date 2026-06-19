# This feature checks the advice shown for a high UV value
Feature: UV health advice
  Users receive clear guidance when UV exposure is dangerous.

  Scenario: Very high UV index produces protective advice
    Given the UV index is above 8
    When the system generates UV advice
    Then the user should receive the advice "Very high UV level. Avoid direct sunlight and wear protective clothing."