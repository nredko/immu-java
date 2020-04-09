package com.inexika.immu;

import com.codenotary.immudb.schema.ImmuServiceGrpc;
import com.codenotary.immudb.schema.Schema;
import com.inexika.immu.client.RootService;
import com.inexika.immu.handler.SafeGet;
import com.inexika.immu.handler.response.SafeGetResponse;
import com.inexika.immu.handler.response.SafeSetResponse;
import com.inexika.immu.handler.SafeSet;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ImmuClient {
    private final ImmuServiceGrpc.ImmuServiceBlockingStub stub;
    private final ManagedChannel channel;
    private final RootService rootService;

    public ImmuClient(String immudbUrl) throws Exception {
        this.channel = ManagedChannelBuilder.forTarget(immudbUrl)
                .usePlaintext()
                .build();

        this.stub = ImmuServiceGrpc.newBlockingStub(channel);
        this.rootService = new RootService(stub);

        this.rootService.init();
    }

    public ImmuServiceGrpc.ImmuServiceBlockingStub getInstance() {
        return stub;
    }

    public void shutdown() {
        channel.shutdown();
    }

    public SafeGetResponse safeGet(Schema.SafeGetOptions request) throws Exception {
        return SafeGet.call(stub, this.rootService, request);
    }

    public SafeSetResponse safeSet(Schema.SafeSetOptions request) throws Exception {
        return SafeSet.call(stub, this.rootService, request);
    }

    public static void main(String[] args) {

    }
}
