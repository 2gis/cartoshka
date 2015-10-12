Scenario: Simple Unary Expression
Given a parser with folding
When the following source is parsed:
    x: -1;
Then rule x is:
    -1

Scenario: Simple Binary Expression
Given a parser with folding
When the following source is parsed:
    x: 1 + 2;
Then rule x is:
    3

Scenario: Complex Expression
Given a parser with folding
When the following source is parsed:
    @one: 1.0;
    @two: 2.0;
    @three: 3.0;
    @four: 4.0;
    @five: 5.0;
    y: @one * ((@two + @three) - @four) / @five + [six];
Then rule y is:
    0.2 + [six]
