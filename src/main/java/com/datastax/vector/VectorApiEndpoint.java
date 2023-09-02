package com.datastax.vector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Simple HTTP Web UI along with gRPC, alternative for access
 */
@Path("/vectorize")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MEDIA_TYPE_WILDCARD)
public class VectorApiEndpoint {
    @GrpcClient("vectorClient")
    VectorApiGrpc.VectorApiBlockingStub blockingClient;

    @GrpcClient("vectorClient")
    VectorApi asyncClient;

    private final ProtobufToJsonMapper protobufToJsonMapper = new ProtobufToJsonMapper();

    @GET
    @Path("/blocking")
    public JsonNode vectorizeBlocking(
            @QueryParam("contents") String contents) {
        VectorResponse resp = blockingClient.vectorize(VectorRequest.newBuilder().setContents(contents).build());
        return generateResponse(resp);
    }

    @GET
    @Path("/async")
    public Uni<JsonNode> vectorizeAsync(
            @QueryParam("contents") String contents) {
        return asyncClient.vectorize(VectorRequest.newBuilder().setContents(contents).build())
                .onItem().transform(resp -> generateResponse(resp));
    }

    public JsonNode generateResponse(VectorResponse resp) {
        return protobufToJsonMapper.valueToTree(resp);
    }
}
