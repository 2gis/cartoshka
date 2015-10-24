Scenario: HSLA and HSL
Given a parser with constant folding
When the following source is parsed:
    a: hsla(222, 71%, 50%, 0.97);
    b: hsl(222, 71%, 50%);
    c: hsla(49, .88, .50, .65);
    d: hsl(49, .88, .50);
Then rule a is: rgba(37, 91, 218, 0.97)
Then rule b is: rgb(37, 91, 218)
Then rule c is: rgba(240, 199, 15, 0.65)
Then rule d is: rgb(240, 199, 15)

Scenario: RGBA and RGB
Given a parser with constant folding
When the following source is parsed:
    a: rgba(37, 91, 218, 97%);
    b: rgb(37, 91, 218);
    c: rgba(.942, .782, .06, .65);
    d: rgb(240, 199, 15);
Then rule a is: rgba(37, 91, 218, 0.97)
Then rule b is: rgb(37, 91, 218)
Then rule c is: rgba(240, 199, 15, 0.65)
Then rule d is: rgb(240, 199, 15)

Scenario: Hue, Saturation, Lightness, Alpha
Given a parser with constant folding
When the following source is parsed:
    @color: hsla(222, 71%, 50%, 0.97);
    a: hue(@color);
    b: saturation(@color);
    c: lightness(@color);
    d: alpha(@color);
Then rule a is: 222
Then rule b is: 71%
Then rule c is: 50%
Then rule d is: 97%

Scenario: Saturate, Desaturate, Lighten, Darken, Fadein, Fadeout, Greyscale
Given a parser with constant folding
When the following source is parsed:
    @color: hsla(222, 71%, 50%, 0.97);
    a: saturate(@color, 10%);
    b: desaturate(@color, .1);
    c: lighten(@color, 10%);
    d: darken(@color, .1);
    e: fadein(@color, 10%);
    f: fadeout(@color, 10%);
    g: greyscale(@color);
Then rule a is: rgba(24, 86, 231, 0.97)
Then rule b is: rgba(50, 96, 205, 0.97)
Then rule c is: rgba(81, 124, 225, 0.97)
Then rule d is: rgba(30, 73, 174, 0.97)
Then rule e is: rgb(37, 91, 218)
Then rule f is: rgba(37, 91, 218, 0.87)
Then rule g is: rgba(128, 128, 128, 0.97)

Scenario: Spin
Given a parser with constant folding
When the following source is parsed:
    @color: hsla(222, 71%, 50%, 0.97);
    a: spin(@color, 100);
    b: spin(@color, 460);
    c: spin(@color, 360);
    d: spin(@color, -100);
Then rule a is: rgba(218, 37, 152, 0.97)
Then rule b is: rgba(218, 37, 152, 0.97)
Then rule c is: rgba(37, 91, 218, 0.97)
Then rule d is: rgba(37, 218, 43, 0.97)

Scenario: Mix
Given a parser with constant folding
When the following source is parsed:
    a: mix(rgba(100,0,0,1.0), rgba(0,100,0,0.5), 50%);
    b: mix(#ff0000, #0000ff, 50%);
Then rule a is: rgba(75, 25, 0, 0.75)
Then rule b is: rgb(128, 0, 128)

Scenario: Filters
Given a parser with constant folding
When the following source is parsed:
    a1: emboss();
    a2: blur();
    a3: gray();
    a4: sobel();
    a5: edge-detect();
    a6: x-gradient();
    a7: y-gradient();
    a8: sharpen();
    b1: agg-stack-blur(1,2);
    b2: scale-hsla(1,2,3,4,5,6,7,8);
Then rule a1 is: emboss()
Then rule a2 is: blur()
Then rule a3 is: gray()
Then rule a4 is: sobel()
Then rule a5 is: edge-detect()
Then rule a6 is: x-gradient()
Then rule a7 is: y-gradient()
Then rule a8 is: sharpen()
Then rule b1 is: agg-stack-blur(1, 2)
Then rule b2 is: scale-hsla(1, 2, 3, 4, 5, 6, 7, 8)
