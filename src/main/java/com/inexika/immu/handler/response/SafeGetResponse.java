package com.inexika.immu.handler.response;

import com.google.protobuf.ByteString;

public final class SafeGetResponse {
    private long index;

    public long getIndex() {
        return index;
    }

    private ByteString key;

    public ByteString getKey() {
        return key;
    }

    private ByteString value;

    public ByteString getValue() {
        return value;
    }

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    private boolean verified;

    public boolean getVerified() {
        return verified;
    }

    public SafeGetResponse(){

    }

    public SafeGetResponse(long index, ByteString key, ByteString value, long timestamp, boolean verified){
        this.index = index;
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
        this.verified = verified;
    }
}
