Scenario: Simple Unary Expression
Given a parser without folding
When the following source is parsed:
    x: -1;
Then rule x is:
    -1

Scenario: Simple Binary Expression
Given a parser without folding
When the following source is parsed:
    x: 1 + 2;
Then rule x is:
    1 + 2
