package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.scanners.Scanner;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

class CartoScanner extends Scanner {
    private final Reader source;
    private final Stack<Character> stack;
    private int position;
    private boolean eos;

    protected CartoScanner(Reader input) {
        this.source = input;
        this.position = -1;
        this.eos = false;
        this.stack = new Stack<>();
    }

    @Override
    protected boolean advance() {
        try {
            if (stack.isEmpty()) {
                int c = this.source.read();
                if (c < 0) {
                    this.eos = true;
                    return false;
                }

                this.c0_ = (char) c;
            } else {
                this.c0_ = stack.pop();
            }

            this.position++;
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected int getCurrentPosition() {
        return this.position;
    }

    @Override
    protected boolean isEOS() {
        return this.eos;
    }

    @Override
    protected void push(char c) {
        stack.push(c);
        position--;
    }
}