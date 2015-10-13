Scenario: Simple
Given a parser
When the following source is parsed:
    a: true;
    b: false;
    c: #aabbcc;
    d: #abc;
Then rule a is: true
Then rule b is: false
Then color c as hex is: #AABBCC
Then color d as hex is: #AABBCC

Scenario: Ruleset
Given a parser with constant folding
When the following source is parsed:
    @some-color: #abc;
    .class-1, .class-2, .class-3 {
      #id1::x/attachment-1, #id2::attachment-2, ::attachment-3 {
        [zoom >= 10][f1 = 'v1'][f2 > 10] {
          // comment
          line-color: @some-color;
          line-width: 2.2;
          line-opacity: 0.4;

          // comment
          a/line-join: miter;
          b/line-join: round;
          * {
            just: test;
          }
        }
      }
    }
Then ruleset 1 contains classes: class-1, class-2, class-3
Then ruleset 2 contains ids: id1, id2
Then ruleset 2 contains attachments: x/attachment-1, attachment-2, attachment-3
Then ruleset 3 contains filters: [[f1] = v1], [[f2] > 10]
Then color line-color as hex is: #AABBCC
Then rule line-width is: 2.2
Then rule line-opacity is: 0.4
Then rule a/line-join is: miter
Then rule b/line-join is: round

Scenario: Map
Given a parser with constant folding
When the following source is parsed:
    Map {
      a/line-join: miter;
      b/line-join: round;
    }
Then rule a/line-join is: miter
Then rule b/line-join is: round
