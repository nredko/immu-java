package com.inexika.immu.utils;

import com.codenotary.immudb.grpc.Schema;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Consistency {
    private static byte NODE_PREFIX = 1;

    public static Schema.ConsistencyProof getVerifiedProof(Schema.ConsistencyProof proof, Schema.Root prevRoot) {
        if (verify(proof, prevRoot)) {
            //proof.getFirstRoot() = prevRoot.getRoot();
            return proof.toBuilder().setFirstRoot(prevRoot.getRoot()).build();
        }

        return proof;
    }

    public static boolean verify(Schema.ConsistencyProof proof, Schema.Root prevRoot) {
        if (proof.getFirst() != prevRoot.getIndex())
            return false;

        return verifyPath(
                proof.getPathList().stream().map(bs -> Arrays.asList(ArrayUtils.toObject(bs.toByteArray()))).collect(Collectors.toList()),
                proof.getSecond(),
                proof.getFirst(),
                Arrays.asList(ArrayUtils.toObject(proof.getSecondRoot().toByteArray())),
                Arrays.asList(ArrayUtils.toObject(prevRoot.getRoot().toByteArray()))
        );
    }

    public static boolean verifyPath(List<List<Byte>> path, long second, long first, List<Byte> secondHash, List<Byte> firstHash) {
        int l = path.size();
        if (first == second && firstHash.equals(secondHash) && l == 0)
            return true;

        if (!(first < second) || l == 0)
            return false;

        List<List<Byte>> pp = new ArrayList();
        if (isPowerOfTwo(first + 1)) {
            pp.add(firstHash);
        }
        pp.addAll(path);

        long fn = first;
        long sn = second;

        while ((fn % 2) == 1) {
            fn >>= 1;
            sn >>= 1;
        }

        List<Byte> fr = pp.get(0);
        List<Byte> sr = pp.get(0);

        boolean isFirst = true;
        for (List<Byte> c : pp) {
            if (isFirst) {
                isFirst = false;
                continue;
            }

            if (sn == 0)
                return false;

            if (fn % 2 == 1 || fn == sn) {
                List<Byte> tmp = new ArrayList<>();
                tmp.add(NODE_PREFIX);
                tmp.addAll(c);
                tmp.addAll(fr);
                fr = Arrays.asList(ArrayUtils.toObject(DigestUtils.sha256(ArrayUtils.toPrimitive(tmp.toArray(new Byte[0])))));

                tmp.clear();
                tmp.add(NODE_PREFIX);
                tmp.addAll(c);
                tmp.addAll(sr);
                sr = Arrays.asList(ArrayUtils.toObject(DigestUtils.sha256(ArrayUtils.toPrimitive(tmp.toArray(new Byte[0])))));

                while ((fn % 2) == 0 && fn != 0) {
                    fn >>= 1;
                    sn >>= 1;
                }
            } else {
                List<Byte> tmp = new ArrayList<>();
                tmp.add(NODE_PREFIX);
                tmp.addAll(sr);
                tmp.addAll(c);
                sr = Arrays.asList(ArrayUtils.toObject(DigestUtils.sha256(ArrayUtils.toPrimitive(tmp.toArray(new Byte[0])))));
            }

            fn >>= 1;
            sn >>= 1;
        }

        return fr.equals(firstHash) && sr.equals(secondHash) && sn == 0;
    }

    private static boolean isPowerOfTwo(long x) {
        return x != 0 && (x & (x - 1)) == 0;
    }
}
