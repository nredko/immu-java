package com.inexika.immu.utils;

import com.codenotary.immudb.schema.Schema;
import com.google.protobuf.ByteString;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Inclusion {
    private static byte NODE_PREFIX = 1;

    public static boolean verify(Schema.InclusionProof inclusionProof, long index, ByteString leaf) {
        if (inclusionProof.getIndex() != index || inclusionProof.getLeaf() != leaf)
            return false;

        return verifyPath(
                inclusionProof.getPathList().stream().map(bs -> Arrays.asList(ArrayUtils.toObject(bs.toByteArray()))).collect(Collectors.toList()),
                inclusionProof.getAt(),
                inclusionProof.getIndex(),
                Arrays.asList(ArrayUtils.toObject(inclusionProof.getRoot().toByteArray())),
                Arrays.asList(ArrayUtils.toObject(leaf.toByteArray()))
        );
    }

    public static boolean verifyPath(List<List<Byte>> path, long at, long i, List<Byte> root, List<Byte> leaf) {
        if (i > at || (at > 0 && path.size() == 0))
            return false;

        List<Byte> h = leaf;
        for (List<Byte> v : path) {
            List<Byte> c = new ArrayList<Byte>();
            c.add(NODE_PREFIX);

            if (i % 2 == 0 && i != at) {
                c.addAll(h);
                c.addAll(v);
            } else {
                c.addAll(v);
                c.addAll(h);
            }
            h = Arrays.asList(ArrayUtils.toObject(DigestUtils.sha256(ArrayUtils.toPrimitive(c.toArray(new Byte[0])))));
            i /= 2;
            at /= 2;
        }

        return at == i && h.equals(root);
    }
}
