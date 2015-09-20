package com.github.tartakynov.cartoshka;

import com.github.tartakynov.cartoshka.scanners.Scanner;

import java.io.IOException;
import java.io.Reader;

class CartoScanner extends Scanner {
    private final Reader source;
    private int position;
    private boolean eos;

    protected CartoScanner(Reader input) {
        this.source = input;
        this.position = -1;
        this.eos = false;
    }

    @Override
    protected boolean advance() {
        try {
            int c = this.source.read();
            if (c < 0) {
                this.eos = true;
                return false;
            }

            this.c0_ = (char) c;
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
}