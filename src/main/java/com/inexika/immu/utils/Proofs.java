package com.inexika.immu.utils;

import com.codenotary.immudb.schema.Schema;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Proofs {
    // FromSlice sets _Path_ from the give _slice_.
    private static List<List<Byte>> fromSlice(List<ByteString> slice) {
        return slice.stream().map(bs -> Arrays.asList(ArrayUtils.toObject(bs.toByteArray()))).collect(Collectors.toList());
    }

    public static boolean verify(Schema.Proof proof, byte[] leaf, Schema.Root prevRoot) {
        if (proof == null || !Arrays.equals(leaf, proof.getLeaf().toByteArray())) {
            return false;
        }

        List<List<Byte>> path = fromSlice(proof.getInclusionPathList());
        List<Byte> rt = Arrays.asList(ArrayUtils.toObject(proof.getRoot().toByteArray()));
        List<Byte> lf = Arrays.asList(ArrayUtils.toObject(proof.getLeaf().toByteArray()));

        boolean verifiedInclusion = Inclusion.verifyPath(
                path,
                proof.getAt(),
                proof.getIndex(),
                rt,
                lf
        );

        if (!verifiedInclusion) {
            return false;
        }

        // we cannot check consistency when the previous root is not provided
        if (prevRoot.getIndex() == 0 && prevRoot.getRoot().size() == 0) {
            return true;
        }

        path = fromSlice(proof.getConsistencyPathList());

        //var firstRoot, secondRoot[ sha256.Size]byte;
        List<Byte> firstRoot = Arrays.asList(ArrayUtils.toObject(prevRoot.getRoot().toByteArray()));
        List<Byte> secondRoot = Arrays.asList(ArrayUtils.toObject(proof.getRoot().toByteArray()));

        return Consistency.verifyPath(path, proof.getAt(), prevRoot.getIndex(), secondRoot, firstRoot);
    }
}
