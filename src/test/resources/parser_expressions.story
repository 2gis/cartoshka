Scenario: Unary Operation
Given a parser with folding
When the following source is parsed:
    @one: 1;
    y: -@one;
    z: +(-[two]);
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

Scenario: Multiple Values Evaluation
Given a parser without folding
When the following source is parsed:
    xyz: 1 + 2, 2 + 3, 3 + 4;
Then rule xyz is: 1 + 2, 2 + 3, 3 + 4
Then rule xyz evaluates into: [3, 5, 7]

Scenario: Function call
Given a parser with folding
When the following source is parsed:
    @one: 1;
    x: rgb(@one, [two], 3);
Then rule x is: rgb(1, [two], 3)

Scenario: Dimension
Given a parser with folding
When the following source is parsed:
    a: 10%, 10m, 10cm, 10in, 10mm, 10pt, 10pc, 10px;
    b: 10.0%, 10.0m, 10.0cm, 10.0in, 10.0mm, 10.0pt, 10.0pc, 10.0px;
    c: 10% + 20%;
    d: 10% - 20.0%;
    e: 10% * 20%;
    f: 10% / 20%;
    g: 10% / 20.0%;
    h: 26% % 23%;
    i: -100%;
Then rule a is: 10%, 10m, 10cm, 10in, 10mm, 10pt, 10pc, 10px
Then rule b is: 10.0%, 10.0m, 10.0cm, 10.0in, 10.0mm, 10.0pt, 10.0pc, 10.0px
Then rule c is: 30%
Then rule d is: -10.0%
Then rule e is: 200%
Then rule f is: 0%
Then rule g is: 0.5%
Then rule h is: 3%
Then rule i is: -100%
