Feature: EliteA Smoke Suite

  Background:
    Given I open 'https://nexus.elitea.ai' url
    Then I type "alita@elitea.ai" to "EliteaLoginPage -> usernameInput"
    Then I type "rokziJ-nuvzo4-hucmih" to "EliteaLoginPage -> passwordInput"
    Then I click "EliteaLoginPage -> signInBtn"
    Then I expect 'EliteaMainPage -> avatar' to be visible
    # TODO: review why left panel is re-loading
    And I wait 5 seconds
    And I click "EliteaMainPage -> projectSelector"
    And I click "EliteaMainPage -> privateProjectListItem"
    And I click "EliteaMainPage -> menuButton"
    And I click "EliteaMainPage -> promptMenuItem"
    
  Scenario: TC01 - Create Prompt with All Mandatory Fields
    And I click "EliteaPromptsPage -> addPromptButton"
    Then I type "TestPrompt1" to "EliteaEditPromptPage -> promptName"
    Then I type "Test Description" to "EliteaEditPromptPage -> promptDescription"
    Then I type "Test Context" to "EliteaEditPromptPage -> contextTextarea"
    And I click "EliteaEditPromptPage -> saveButton"
    Then I expect 'EliteaEditPromptPage -> versionDropdown' to be visible
    Then I expect 'EliteaEditPromptPage -> saveButton' to be not clickable
    Then I expect 'EliteaEditPromptPage -> runTab' to be visible

  Scenario: TC02 - Create Prompt Skipping Mandatory Fields
    And I click "EliteaPromptsPage -> addPromptButton"
    Then I type "Test Description" to "EliteaEditPromptPage -> promptDescription"
    Then I type "Test Context" to "EliteaEditPromptPage -> contextTextarea"
    And I click "EliteaEditPromptPage -> saveButton"
    Then I expect 'EliteaEditPromptPage -> errorMessage' to be visible
    Then I expect 'EliteaEditPromptPage -> errorMessage' has exact text value 'Name is required'

  Scenario: TC03 - Filter Prompt by Tags
    And I click 'docsbot_library' text in 'EliteaPromptsPage -> tags' collection
    Then I expect text of elements in 'EliteaPromptsPage -> promptCards' collection contains 'docsbot_library'

#  # Deprecated functionality. Keeping this to visualize cross-steps variables interaction.
#  Scenario: TC04 - Filter Prompt by Owner
#    And I save '1st' element text of 'EliteaPromptsPage -> ownerName' collection to '${OWNER}' variable
#    And I click '1st' element in 'EliteaPromptsPage -> promptCards' collection
#    And I wait 5 seconds
#    And I expect text of elements in 'EliteaPromptsPage -> ownerName' collection equals to '${OWNER}'
