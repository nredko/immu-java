package com.inexika.immu;

import com.codenotary.immudb.grpc.ImmuServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ImmuClient {
    private ImmuServiceGrpc.ImmuServiceBlockingStub stub;
    private ManagedChannel channel;

    public ImmuClient(String serverPath, int port) throws Exception {
        channel = ManagedChannelBuilder.forAddress(serverPath, port)
                .usePlaintext()
                .build();

        stub = ImmuServiceGrpc.newBlockingStub(channel);
    }

    public ImmuServiceGrpc.ImmuServiceBlockingStub getInstance() {
        return stub;
    }

    public void shutdown() {
        channel.shutdown();
    }

    public static void main(String[] args){

    }
}
