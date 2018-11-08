package com.dekinci.bot;

import org.junit.jupiter.api.Test;

import static com.dekinci.bot.DoubleToGrayCode.*;
import static org.junit.jupiter.api.Assertions.*;

class DoubleToGrayCodeTest {

    @Test
    void test() {

    }

    public static void main(String[] args) {
        System.out.println(gCToDouble(doubleToGC(52)));
    }
}