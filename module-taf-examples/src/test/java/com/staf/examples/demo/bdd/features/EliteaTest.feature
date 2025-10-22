Feature: Elitea demo tests

  Background:
    Given I open Elitea application
    When I log in with username 'alita@elitea.ai' and password 'rokziJ-nuvzo4-hucmih' to Elitea app
    Then I should see the user avatar
    And I switch to the Private project

  Scenario: TC00 - Session initiation [issue with 1st run]
    Given I navigate to the Prompts menu

  Scenario: TC01 - Create Prompt with All Mandatory Fields
    When I navigate to the Prompts menu
    And I click the Add Prompt button
    And I put Name "TestPrompt1" and Description "Test Description"
    And I change context to "Test Context"
    And I click the Save button
    Then Prompt version is displayed
    And Save button is not clickable
    And Run tab is displayed

  Scenario: TC02 - Create Prompt Skipping Mandatory Fields
    When I navigate to the Prompts menu
    When I click the Add Prompt button
    And I put Name "" and Description "Test Description"
    And I change context to "Test Context"
    And I click the Save button
    Then The field error message is displayed
    And The message should have text 'Name is required'

  Scenario: TC03 - Filter Prompt by Tags
    When I navigate to the Prompts menu
    And I choose tag 'docsbot_library'
    Then only prompts with tag 'docsbot_library' are visible

  Scenario: TC04 - Filter Prompt by Owner
    When I navigate to the Prompts menu
    And I click on owner name of first card
    Then only prompts with chosen owner are visible

  Scenario: TC05 - Edit Existing Prompt
    When I navigate to the Prompts menu
    And I select prompt 'TestPrompt1'
    And I select the Configuration tab
    And I change context to "Updated Test Context"
    And I click the Save button
    Then Prompt is updated

  Scenario: TC06 - Attempt to Edit Non-Editable Fields
    When I navigate to the Prompts menu
    And I select prompt 'TestPrompt1'
    And I select the Configuration tab
    Then Name input is disabled
    And Description input is disabled

  Scenario: TC07 - Save New Version of Prompt
    When I navigate to the Prompts menu
    And I select prompt 'TestPrompt1'
    And I click the Save New Version button
    And I set the dialog input to "Version Note"
    And I click the dialog Save button
    Then Notification appeared

  Scenario: TC08 - Delete Existing Prompt
    When I navigate to the Prompts menu
    And I select prompt 'TestPrompt1'
    And I click Delete Prompt button
    And I confirm prompt 'TestPrompt1' deletion
    Then Check Prompt deleted with message 'Delete the prompt successfully'

  Scenario: TC09 - Switch from Card List View to Table View
    When I navigate to the Prompts menu
    And I click the Table View button
    Then Table grid is displayed