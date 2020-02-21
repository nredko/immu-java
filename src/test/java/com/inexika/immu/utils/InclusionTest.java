package com.inexika.immu.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InclusionTest {

    @Test
    public void verifyPath_DefaultValues()
    {
        assertEquals(true, Inclusion.verifyPath(new ArrayList<>(), 0, 0, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void verifyPath_AtMoreThanI()
    {
        assertEquals(false, Inclusion.verifyPath(new ArrayList<>(), 0, 1, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void verifyPath_AtLessThanI()
    {
        assertEquals(false, Inclusion.verifyPath(new ArrayList<>(), 1, 0, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    public void verifyPath_AtEqualsI()
    {
        assertEquals(false, Inclusion.verifyPath(new ArrayList<>(), 1, 1, new ArrayList<>(), new ArrayList<>()));
    }
}