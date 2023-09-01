package com.datastax.vector;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class VectorApiServiceTest {
    private final static String TEST_CONTENTS = "some contents   to\tsplit";

    private final static List<String> TEST_CONTENTS_TOKENIZED = Arrays.asList("some", "contents", "to", "split");

    private ManagedChannel channel;

    @BeforeEach
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9001).usePlaintext().build();
    }

    @AfterEach
    public void cleanup() throws InterruptedException {
        channel.shutdown();
        channel.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void testServiceUsingBlockingStub() {
        VectorApiGrpc.VectorApiBlockingStub client = VectorApiGrpc.newBlockingStub(channel);
        VectorResponse resp = client
                .vectorize(VectorRequest.newBuilder().setContents(TEST_CONTENTS).build());
        _assertResponse(resp);
    }

    @Test
    public void testServiceUsingMutinyStub() {
        VectorResponse resp = MutinyVectorApiGrpc.newMutinyStub(channel)
                .vectorize(VectorRequest.newBuilder().setContents(TEST_CONTENTS).build())
                .await()
                .atMost(Duration.ofSeconds(5));
        _assertResponse(resp);
    }

    private void _assertResponse(VectorResponse resp) {
        assertThat(resp.getContents()).isEqualTo(TEST_CONTENTS);
        assertThat(resp.getTokensList()).isEqualTo(TEST_CONTENTS_TOKENIZED);
        assertThat(resp.getCount()).isEqualTo(TEST_CONTENTS_TOKENIZED.size());
    }
}