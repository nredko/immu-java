package com.inexika.immu.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsistencyTest {

    @Test
    public void Verify_DefaultValues() {
        assertEquals(true, Consistency.verifyPath(new ArrayList<>(), 0, 0, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void Verify_SinglePathSecondEqualsFirst() {
        assertEquals(true, Consistency.verifyPath(new ArrayList<>(), 1, 1, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void Verify_SinglePathFirstMoreThanSecond() {
        assertEquals(false, Consistency.verifyPath(new ArrayList<>(), 0, 1, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void Verify_SinglePathSecondMoreThanFirst() {
        assertEquals(false, Consistency.verifyPath(new ArrayList<>(), 1, 0, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void Verify_SinglePathSecondHashNotEqualsFirstHash() {
        assertEquals(false, Consistency.verifyPath(new ArrayList<>(), 0, 0, new ArrayList<Byte>() {{
            add((byte) 1);
        }}, new ArrayList<Byte>() {{
            add((byte) 2);
        }}));
    }

    @Test
    public void Verify_ManyPathSecondMoreThanFirst() {
        assertEquals(false, Consistency.verifyPath(new ArrayList<List<Byte>>() {{
            add(new ArrayList<>());
            add(new ArrayList<>());
            add(new ArrayList<>());
        }}, 2, 1, new ArrayList<>(), new ArrayList<>()));
    }
}