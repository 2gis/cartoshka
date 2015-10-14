# cartoshka

[CartoCSS (carto)](https://github.com/mapbox/carto) parser for Java. All it does is translate CartoCSS to a traversable hierarchy of Java classes.

[![GitHub license](https://img.shields.io/badge/license-MPL 2.0-blue.svg?style=flat-square)](LICENSE)
[![Build status](https://travis-ci.org/2gis/cartoshka.svg?branch=master)](https://travis-ci.org/2gis/cartoshka)
[![Coverage Status](https://coveralls.io/repos/2gis/cartoshka/badge.svg?branch=master&service=github)](https://coveralls.io/github/2gis/cartoshka?branch=master)

## Features

*   Light weight
*   High Performance
*   Full support of CartoCSS syntax
*   Single jar with no additional dependencies

## Setup

The project binaries are not published yet since development is in progress.

## Quickstart
Look at the simple example below
```Java
CartoParser parser = new CartoParser();
try (FileReader reader = new FileReader(file)) {
    // parsing the file
    Block style = parser.parse(file.getName(), reader);

    // constant folding
    style.accept(new ConstantFoldVisitor(), null);

    // pretty print
    String pretty = style.accept(new PrintVisitor(2), null);
    System.out.println(pretty);
} catch (IOException e) {
    e.printStackTrace();
}
```
