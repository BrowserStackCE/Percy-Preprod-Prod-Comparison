Feature: BrowserStack Demo

  @BrowserStack-pre-prod
  Scenario: BStackdemo Sample Test
    Given I navigate to "https://bstackdemo.com/" website
    Then I add products to cart
    And I validate the if the cart is open
    And I validate the product added to cart

  @BrowserStack-prod
  Scenario: BStackdemo Sample Test
    Given I navigate to "https://bstackdemo.com/" website
    Then I add products to cart
    And I validate the if the cart is open
    And I validate the product added to cart

  @VFC-Pre-Prod
  Scenario Outline: VFC Sample Test
    Given I navigate to "<brand>" website and "<env>"
    #Then I take screenshot of the page
    Examples:
      | brand        | env      |
      | VANS-NORA-US | Pre-prod |
      | TBL-NORA-US  | Pre-prod |
      | TNF-NORA-US  | Pre-prod |

  @VFC-Prod
  Scenario Outline: VFC Sample Test
    Given I navigate to "<brand>" website and "<env>"
    #Then I take screenshot of the page
    Examples:
      | brand        | env  |
      | VANS-NORA-US | Prod |
      | TBL-NORA-US  | Prod |
      | TNF-NORA-US  | Prod |