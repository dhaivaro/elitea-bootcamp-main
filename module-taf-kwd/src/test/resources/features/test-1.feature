Feature: Test

#  Scenario: Open page and click element
#    When I open 'https://google.com' url
#    Then I click "$x(//*[text()='Accept all'])"
#    Then I click "$(.FPdoLc .RNmpXc)"
#    Then I expect '$(.glue-header__bar--desktop .glue-header__logo--product)' to be not visible
#    Then I expect url has "https://doodles.google/"


  Scenario: TC01 - Create Prompt with All Mandatory Fields
#I open Elitea application
    When I open 'https://nexus.elitea.ai' url
#I log in with username 'alita@elitea.ai' and password 'rokziJ-nuvzo4-hucmih' to Elitea app
    Then I type "alita@elitea.ai" to "EliteaLoginPage -> usernameInput"
    Then I type "rokziJ-nuvzo4-hucmih" to "EliteaLoginPage -> passwordInput"
    Then I click "EliteaLoginPage -> signInBtn"
#I should see the user avatar
    Then I expect 'EliteaMainPage -> avatar' to be visible
#  I switch to the Private project
    And I click "EliteaMainPage -> projectSelector"
    And I click "EliteaMainPage -> privateProjectListItem"
# I navigate to the Prompts menu
    And I click "EliteaMainPage -> menuButton"
    And I click "EliteaMainPage -> promptMenuItem"
# I click the Add Prompt button
    And I click "EliteaPromptsPage -> addPromptButton"
# I put Name "TestPrompt1" and Description "Test Description"
    Then I type "TestPrompt1" to "EliteaEditPromptPage -> promptName"
    Then I type "Test Description" to "EliteaEditPromptPage -> promptDescription"
#  I change context to "Test Context"
    Then I type "Test Context" to "EliteaEditPromptPage -> contextTextarea"
# I click the Save button
    And I click "EliteaEditPromptPage -> saveButton"
# Prompt version is displayed
    Then I expect 'EliteaEditPromptPage -> versionDropdown' to be visible
#Save button is not clickable
    Then I expect 'EliteaEditPromptPage -> saveButton' to be not clickable
# Run tab is displayed
    Then I expect 'EliteaEditPromptPage -> runTab' to be visible

  Scenario: TC02 - Create Prompt Skipping Mandatory Fields
    #I open Elitea application
    When I open 'https://nexus.elitea.ai' url
#I log in with username 'alita@elitea.ai' and password 'rokziJ-nuvzo4-hucmih' to Elitea app
    Then I type "alita@elitea.ai" to "EliteaLoginPage -> usernameInput"
    Then I type "rokziJ-nuvzo4-hucmih" to "EliteaLoginPage -> passwordInput"
    Then I click "EliteaLoginPage -> signInBtn"
#I should see the user avatar
    Then I expect 'EliteaMainPage -> avatar' to be visible
#  I switch to the Private project
    And I click "EliteaMainPage -> projectSelector"
    And I click "EliteaMainPage -> privateProjectListItem"
# I navigate to the Prompts menu
    And I click "EliteaMainPage -> menuButton"
    And I click "EliteaMainPage -> promptMenuItem"
# I click the Add Prompt button
    And I click "EliteaPromptsPage -> addPromptButton"
# I put Name "TestPrompt1" and Description "Test Description"
    Then I type "TestPrompt1" to "EliteaEditPromptPage -> promptName"
    Then I type "Test Description" to "EliteaEditPromptPage -> promptDescription"
#  I change context to "Test Context"
    Then I type "Test Context" to "EliteaEditPromptPage -> contextTextarea"
# I click the Save button
    And I click "EliteaEditPromptPage -> saveButton"
#  The field error message is displayed
    Then I expect 'EliteaEditPromptPage -> errorMessage' to be visible
#  The message should have text 'Name is required'
  Then I expect 'EliteaEditPromptPage -> errorMessage' has exact text value 'Name is required'