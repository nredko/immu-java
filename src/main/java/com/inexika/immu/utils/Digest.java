package com.inexika.immu.utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.ByteBuffer;

public class Digest {
    private static final byte LEAF_PREFIX = 0;

    public static byte[] calc(long index, byte[] key, byte[] value) {
        ByteBuffer k = ByteBuffer.wrap(key);
        ByteBuffer v = ByteBuffer.wrap(value);

        ByteBuffer c = ByteBuffer.allocate(1 + 8 + 8 + key.length + value.length);
        c.put(0, LEAF_PREFIX);

        ByteBuffer buf_index = ByteBuffer.allocate(8);
        buf_index.putLong(0, index);

        c.position(1);
        c.put(buf_index);

        ByteBuffer buf_key = ByteBuffer.allocate(8);
        buf_key.putLong(0, key.length);

        c.position(1 + 8);
        c.put(buf_key);
        c.position(1 + 8 + 8);
        c.put(k);
        c.position(1 + 8 + 8 + key.length);
        c.put(v);

        return DigestUtils.sha256(c.array());
    }
}
