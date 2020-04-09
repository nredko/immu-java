package com.inexika.immu.handler;

import com.codenotary.immudb.schema.ImmuServiceGrpc;
import com.codenotary.immudb.schema.Schema;
import com.inexika.immu.client.RootService;
import com.inexika.immu.handler.response.SafeGetResponse;
import com.inexika.immu.utils.Item;
import com.inexika.immu.utils.Proofs;

import java.nio.ByteBuffer;

public class SafeGet {
    public static SafeGetResponse call(ImmuServiceGrpc.ImmuServiceBlockingStub immuS, RootService rs, Schema.SafeGetOptions request) throws Exception {
        Schema.Root root = rs.getRoot();

        Schema.Index index = Schema.Index.newBuilder()
                .setIndex(root.getIndex())
                .build();

        Schema.SafeGetOptions protoReq = Schema.SafeGetOptions.newBuilder()
                .mergeFrom(request)
                .setRootIndex(index)
                .build();

        Schema.SafeItem msg = immuS.safeGet(protoReq);
        boolean verified = Proofs.verify(msg.getProof(), Item.hash(msg.getItem()), root);

        if (verified) {
            Schema.Root toCache = Schema.Root.newBuilder()
                    .setIndex(msg.getProof().getAt())
                    .setRoot(msg.getProof().getRoot())
                    .build();

            try {
                rs.setRoot(toCache);
            } catch (Exception e) {
                throw e;
            }
        }

        Schema.Item i = msg.getItem();

        return new SafeGetResponse(
                i.getIndex(),
                i.getKey(),
                i.getValue().substring(8),
                ByteBuffer.wrap(i.getValue().substring(0, 8).toByteArray()).getLong(),
                verified
        );
    }
}
