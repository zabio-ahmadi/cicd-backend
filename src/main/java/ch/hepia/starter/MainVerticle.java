package ch.hepia.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

public class MainVerticle extends AbstractVerticle {

  final int PORT = 8080;

  // On start of the app
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    // Create server & router, instantiate a handler for requests
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route()
      .handler(
        CorsHandler.create("*")
          .allowedMethod(HttpMethod.GET));

    MainHandler mainHandler = MainHandler.getInstance();

    // Definition of routes and handling of requests
    router.get("/title").handler(ctx -> {
      System.out.println("GET /title");
      mainHandler.getRequestTitle(ctx);
    });

    router.get("/quote").handler(ctx -> {
      System.out.println("GET /quote");
      mainHandler.getRequestQuote(ctx);
    });

    router.get("/author").handler(ctx -> {
      System.out.println("GET /author");
      mainHandler.getRequestAuthor(ctx);
    });

    // If none of the above route is called, throw a 404 error
    router.route().last().handler(ctx -> {
      System.out.println("404 ERROR");
      ctx.response().setStatusCode(404).end();
    });

    // Start of the HTTP server
    server.requestHandler(router).listen(PORT, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port " + PORT);
      } else {
        startPromise.fail(http.cause());
      }
    });

  }

}
