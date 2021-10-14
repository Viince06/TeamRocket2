Feature: Referee Feature

  Background:
    Given a player with his inventory and name "TestPlayer"

  Scenario: He chooses to play a card
    When The player plays a card "FreeCard" with cost set to null
    Then The move is a valid one as cost is "null"

  Scenario: He chooses to sacrifice a card
    When The player sacrifices a card "SacrificeCard"
    Then The move is a valid and cost is "null"


  Scenario: He chooses to play a card but does not have enough resources to do so
    When The player play a card "MyCard" with resource Wood of quantity 1 is needed and he has no Wood resource
    Then The move is invalid as cost is 1


  Scenario: He chooses to play a card and trade resources
    When The player play a card "TestCard" with Wood 1 is needed but he has no Wood resource
    Then He proposes a Trade with the player on the left for 1 Wood resource, the move is then valid as he has 1 Wood
