Feature: User Registration
  As a new user
  I want to register on OpenCart
  So that I can make purchases and manage my account

  Background:
    Given I am on the OpenCart registration page

  @Positive @Registration
  Scenario: Successful registration with valid credentials
    When I register with the following details:
      | Field            | Value            |
      | First Name       | John             |
      | Last Name        | Mis              |
      | Email            | unique@email.com |
      | Password         | Password123!     |
    Then I should see the registration success message

  @Validation @Registration
  Scenario Outline: Required field validation
    When I attempt to register with the following details:
      | Field            | Value            |
      | First Name       | <FirstName>      |
      | Last Name        | <LastName>       |
      | Email            | <Email>          |
      | Password         | <Password>       |
      | Privacy Policy   | <PrivacyPolicy>  |
    Then I should see the following errors:
      | Field          | Error Message                                 |
      | <ErrorField>   | <ErrorMessage>                                |

    Examples:
      | FirstName | LastName | Email         |  Password     | PrivacyPolicy | ErrorField     | ErrorMessage                                     |
      |           | Mis      | test@test.com |  Password123  | true          | First Name     | First Name must be between 1 and 32 characters!  |
      | John      |          | test@test.com |  Password123  | true          | Last Name      | Last Name must be between 1 and 32 characters!   |
      | John      | Mis      |               |  Password123  | true          | Email          | E-Mail Address does not appear to be valid!      |
      | John      | Mis      | test@test.com |               | true          | Password       | Password must be between 4 and 20 characters!    |
      | John      | Mis      | test@test.com |  Password123  | false         | Privacy Policy | Warning: You must agree to the Privacy Policy!   |

  @Negative @Registration
  Scenario: Registration with existing email
    Given I register with the following details:
      | Field            | Value            |
      | First Name       | John             |
      | Last Name        | Mis              |
      | Email            | existing@test.com|
      | Password         | Password123!     |
    And I am on the OpenCart registration page
    When I register with the following details:
      | Field            | Value            |
      | First Name       | John             |
      | Last Name        | Mis              |
      | Email            | existing@test.com|
      | Password         | Password123!     |
    Then I should see the following error:
      | Field | Error Message                                  |
      | Email | Warning: E-Mail Address is already registered! |


    # I assumed that the requirement was to have these password rules:
   # Password must contain at least one uppercase and one number!
  # Password must contain at least one lowercase letter!
  @Validation @Registration
  Scenario Outline: Password validation
    When I attempt to register with the following details:
      | Field            | Value            |
      | First Name       | John             |
      | Last Name        | Mis              |
      | Email            | test@test.com    |
      | Password         | <Password>       |
      | Privacy Policy   | true             |
    Then I should see the following errors:
      | Field    | Error Message                                  |
      | Password | <ErrorMessage>                                 |

    Examples:
      | Password           | ErrorMessage                                                 |
      | 123                | Password must be between 4 and 20 characters!                |
      #the example bellow will fail due to the above assumptions
      | password           | Password must contain at least one uppercase and one number! |
      | PASSWORD1          | Password must contain at least one lowercase letter!         |

  @EdgeCase @Registration
  Scenario: Registration with boundary values
    When I register with the following details:
      | Field            | Value            |
      | First Name       | J                |
      | Last Name        | D                |
      | Email            | a@b.c            |
      | Password         | Pass1!           |
    Then I should see the registration success message