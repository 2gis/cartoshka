package com._2gis.cartoshka.tree;

public enum NodeType {
    BLOCK,
    ELEMENT,
    FILTER,
    RULE,
    RULESET,
    SELECTOR,
    ZOOM,

    /* Expressions. */
    BINARY_OPERATION,
    CALL,
    EXPANDABLE_TEXT,
    FIELD,
    UNARY_OPERATION,
    VALUE,
    VARIABLE,

    /* Literals. */
    BOOLEAN,
    COLOR,
    DIMENSION,
    IMAGE_FILTER,
    MULTI_LITERAL,
    NUMBER,
    TEXT;
}
