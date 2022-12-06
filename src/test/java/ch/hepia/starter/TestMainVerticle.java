package ch.hepia.starter;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * This file automate the testing of our app
 *
 * Each request is tested by a method with the @Test annotation
 *
 * If the return value is changed in the code, it must change in the tests too
 */

@ExtendWith(VertxExtension.class)
class TestMainVerticle {

  final String host = "localhost";
  final int port = 8080;

  // Deploy the app before each test
  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void testGetTitle(Vertx vertx, VertxTestContext vertxTestContext) {

    WebClient webClient = WebClient.create(vertx);
    Checkpoint requestCheckpoint = vertxTestContext.checkpoint(1);

    webClient.get(port, host, "/title")
      .as(BodyCodec.string())
      .send(vertxTestContext.succeeding(resp -> {
        vertxTestContext.verify(() -> {
          assertThat(resp.statusCode()).isEqualTo(200);
          assertThat(resp.body()).contains("La citation du jour");
          requestCheckpoint.flag();
        });
      }));
  }

  @Test
  void testGetQuote(Vertx vertx, VertxTestContext vertxTestContext) {

    WebClient webClient = WebClient.create(vertx);
    Checkpoint requestCheckpoint = vertxTestContext.checkpoint(1);

    webClient.get(port, host, "/quote")
      .as(BodyCodec.string())
      .send(vertxTestContext.succeeding(resp -> {
        vertxTestContext.verify(() -> {
          assertThat(resp.statusCode()).isEqualTo(200);
          assertThat(resp.body()).contains("Si vous ne pouvez expliquer un concept à un enfant de six ans, c'est que vous ne le comprenez pas complètement.");
          requestCheckpoint.flag();
        });
      }));
  }

  @Test
  void testGetAuthor(Vertx vertx, VertxTestContext vertxTestContext) {

    WebClient webClient = WebClient.create(vertx);
    Checkpoint requestCheckpoint = vertxTestContext.checkpoint(1);

    webClient.get(port, host, "/author")
      .as(BodyCodec.string())
      .send(vertxTestContext.succeeding(resp -> {
        vertxTestContext.verify(() -> {
          assertThat(resp.statusCode()).isEqualTo(200);
          assertThat(resp.body()).contains("Albert Einstein");
          requestCheckpoint.flag();
        });
      }));
  }

  @Test
  void test404Error(Vertx vertx, VertxTestContext vertxTestContext) {

    WebClient webClient = WebClient.create(vertx);
    Checkpoint requestCheckpoint = vertxTestContext.checkpoint(1);

    webClient.get(port, host, "/does-not-exist")
      .as(BodyCodec.string())
      .send(vertxTestContext.succeeding(resp -> {
        vertxTestContext.verify(() -> {
          assertThat(resp.statusCode()).isEqualTo(404);
          requestCheckpoint.flag();
        });
      }));
  }
}
