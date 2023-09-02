package com.datastax.vector;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

/**
 * Convenience class that will allow serializing Protobuf-generated Java value classes
 * to JSON. This is done by using {@code jackson-datatype-protobuf} with regular
 * {@link JsonMapper}.
 */
public class ProtobufToJsonMapper extends JsonMapper {
    public ProtobufToJsonMapper() {
        registerModule(new ProtobufModule());
    }
}
