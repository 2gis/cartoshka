Scenario: Simple Rule
Given a parser without folding
When the following source is parsed:
    x: 1;
Then rule x is:
    1

Scenario: Variable Declaration
Given a parser without folding
When the following source is parsed:
    @x: 1;
Then rule @x is:
    1

Scenario: Multiple value
Given a parser without folding
When the following source is parsed:
    x: 1, 'hello', 'world';
Then rule x is:
    1, hello, world

Scenario: Multiple value variable
Given a parser without folding
When the following source is parsed:
    @x: 1, 'hello', 'world';
Then rule @x is:
    1, hello, world
