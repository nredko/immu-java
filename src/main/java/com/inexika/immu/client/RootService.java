package com.inexika.immu.client;

import com.codenotary.immudb.schema.ImmuServiceGrpc;
import com.codenotary.immudb.schema.Schema;
import com.google.protobuf.Empty;

import java.io.IOException;

public class RootService {
    private final ImmuServiceGrpc.ImmuServiceBlockingStub immuC;
    final RootCache cache;

    public RootService(ImmuServiceGrpc.ImmuServiceBlockingStub immuC) {
        this.immuC = immuC;
        this.cache = new RootCache();
    }

    public void init() throws Exception {
        Schema.Root root = immuC.currentRoot(Empty.getDefaultInstance());
        cache.set(root);
    }

    public Schema.Root getRoot() throws Exception {
        try {
            return cache.get();
        } catch (IOException e) {
            Schema.Root root = immuC.currentRoot(Empty.getDefaultInstance());

            try {
                cache.set(root);
            } catch (IOException ex) {
                throw ex;
            }

            return root;
        }
    }

    public void setRoot(Schema.Root root) throws IOException {
        cache.set(root);
    }
}
