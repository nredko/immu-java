package com.inexika.immu.handler;

import com.codenotary.immudb.schema.ImmuServiceGrpc;
import com.codenotary.immudb.schema.Schema;
import com.google.protobuf.ByteString;
import com.inexika.immu.client.RootService;
import com.inexika.immu.handler.response.SafeSetResponse;
import com.inexika.immu.utils.Item;
import com.inexika.immu.utils.Proofs;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class SafeSet {
    public static SafeSetResponse call(ImmuServiceGrpc.ImmuServiceBlockingStub immuS, RootService rs, Schema.SafeSetOptions request) throws Exception {
        Schema.Root root = rs.getRoot();

        Schema.Index index = Schema.Index.newBuilder()
                .setIndex(root.getIndex())
                .build();

        ByteBuffer valueBytes = ByteBuffer.allocate(8 + request.getKv().getValue().size());
        valueBytes.putLong(new Date().getTime());
        valueBytes.position(8);
        valueBytes.put(request.getKv().getValue().toByteArray());

        Schema.KeyValue reqKV = Schema.KeyValue.newBuilder()
                .setKey(request.getKv().getKey())
                .setValue(ByteString.copyFrom(valueBytes.array()))
                .build();

        Schema.SafeSetOptions protoReq = Schema.SafeSetOptions.newBuilder()
                .setKv(reqKV)
                .setRootIndex(index)
                .build();

        Schema.Proof msg = immuS.safeSet(protoReq);

        Schema.Item item = Schema.Item.newBuilder()
                .setKey(protoReq.getKv().getKey())
                .setValue(protoReq.getKv().getValue())
                .setIndex(msg.getIndex())
                .build();

        if (!Arrays.equals(Item.hash(item), msg.getLeaf().toByteArray()))
            throw new Exception("Proof does not match the given item");

        boolean verified = Proofs.verify(msg, msg.getLeaf().toByteArray(), root);

        if (verified) {
            Schema.Root toCache = Schema.Root.newBuilder()
                    .setIndex(msg.getIndex())
                    .setRoot(msg.getRoot())
                    .build();

            try {
                rs.setRoot(toCache);
            } catch (Exception e) {
                throw e;
            }
        }

        return new SafeSetResponse(
                msg.getIndex(),
                msg.getLeaf(),
                msg.getRoot(),
                msg.getAt(),
                msg.getInclusionPathList(),
                msg.getConsistencyPathList(),
                verified
        );
    }
}
