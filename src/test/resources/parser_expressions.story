Scenario: Unary Operation
Given a parser with folding
When the following source is parsed:
    @one: 1;
    y: -@one;
    z: -[two];
Then rule y is:
    -1
Then rule z is:
    -[two]

Scenario: Binary Operation
Given a parser with folding
When the following source is parsed:
    @one: 1.0;
    @two: 2.0;
    @three: 3.0;
    @four: 4.0;
    @five: 5.0;
    y: @one * ((@two + @three) - @four) / @five + [six];
    z: [six] + @one * ((@two + @three) - @four) / @five;
Then rule y is:
    0.2 + [six]
Then rule z is:
    [six] + 0.2

Scenario: Variable
Given a parser with folding
When the following source is parsed:
    @one: [field2];
    x: @one + [field];
Then rule x is:
    @one + [field]

Scenario: Undefined Variable
Given a parser without folding
When the following source is parsed:
    x: @one;
Then variable is undefined

Scenario: Expandable text
Given a parser with folding
When the following source is parsed:
    @one: 0.5 + 0.5;
    @two: @one * 2;
    x: '@ one = @{one}, two = @{two}, three = [field]';
    y: '@ one = \[one\], two = \@{two}';
    a: '@';
    b: '@{';
    c: '[';
    d: '@{two}';
    e: '[three] @{two}';
Then rule x is: @ one = 1.0, two = 2.0, three = [field]
Then rule y is: @ one = [one], two = @{two}
Then rule a is: @
Then rule b is: @{
Then rule c is: [
Then rule d is: 2.0
Then rule e is: [three] 2.0
