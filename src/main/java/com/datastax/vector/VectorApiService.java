package com.datastax.vector;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

import java.util.Arrays;
import java.util.List;

/**
 * gRPC service implementation for the Vector API.
 */
@GrpcService
public class VectorApiService implements VectorApi {
    @Override
    public Uni<VectorResponse> vectorize(VectorRequest request) {
        String contents = request.getContents();
        List<String> tokens = Arrays.asList(contents.split("\\s+"));
        VectorResponse response = VectorResponse.newBuilder()
                .setContents(contents)
                .addAllTokens(tokens)
                .setCount(tokens.size())
                .build();
        return Uni.createFrom().item(response);
    }
}
