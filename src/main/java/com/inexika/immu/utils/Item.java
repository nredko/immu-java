package com.inexika.immu.utils;

import com.codenotary.immudb.schema.Schema;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class Item {
    // Hash returns the computed hash of _Item_.
    public static byte[] hash(Schema.Item item) {
        if (item == null) {
            return null;
        }

        return Digest.calc(
                item.getIndex(),
                item.getKey().toByteArray(),
                item.getValue().toByteArray()
        );
    }
}
