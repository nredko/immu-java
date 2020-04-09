package com.inexika.immu.handler.response;

import com.google.protobuf.ByteString;

import java.util.List;

public class SafeSetResponse {
    private ByteString leaf;
    private long index;
    private ByteString root;
    private long at;
    private List<ByteString> inclusionPath;
    private List<ByteString> consistencyPath;
    private boolean verified;

    public SafeSetResponse(){}

    public SafeSetResponse(long index, ByteString leaf, ByteString root, long at, List<ByteString> inclusionPathList, List<ByteString> consistencyPathList, boolean verified) {
        this.index = index;
        this.leaf = leaf;
        this.root = root;
        this.at = at;
        this.inclusionPath = inclusionPathList;
        this.consistencyPath = consistencyPathList;
        this.verified = verified;
    }
}
