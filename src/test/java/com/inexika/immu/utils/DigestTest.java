package com.inexika.immu.utils;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DigestTest {

    @Test
    public void Verify_EmptyLeafReturnTrue() {
        byte[] emptyLeaf = new byte[]{
                0xa, (byte) 0x88, 0x11, 0x18, 0x52, 0x9, 0x5c, (byte) 0xae, 0x4, 0x53, 0x40, (byte) 0xea, 0x1f, 0xb, 0x27, (byte) 0x99, 0x44, (byte) 0xb2, (byte) 0xa7, 0x56, (byte) 0xa2, 0x13, (byte) 0xd9, (byte) 0xb5, 0x1, 0x7, (byte) 0xd7, 0x48, (byte) 0x97, 0x71, (byte) 0xe1, 0x59
        };

        byte[] d = Digest.calc(0, new byte[]{}, new byte[]{});

        assertEquals(true, Arrays.equals(emptyLeaf, d));
    }

    @Test
    public void Verify_TestLeafReturnTrue() {
        byte[] testLeaf =  new byte[]{
                0x62, 0x2e, (byte)0x82, (byte)0xa6, 0x42, 0x48, 0x2c, 0x58, (byte)0x96, (byte)0x92, 0x3, (byte)0xba, (byte)0xda, 0x74, 0x40, (byte)0x97, (byte)0xc9, (byte)0xdf, (byte)0xff, 0x2f, (byte)0xf3, 0x14, 0x36, (byte)0xc7, (byte)0xd9, 0x57, 0x21, 0x73, 0x7c, 0x5e, (byte)0xed, (byte)0xa9
        };

        byte[] d = Digest.calc(1, "key".getBytes(StandardCharsets.UTF_8), "value".getBytes(StandardCharsets.UTF_8));

        assertEquals(true, Arrays.equals(testLeaf, d));
    }
}