package ch.hepia.starter;

import io.vertx.ext.web.RoutingContext;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

public class MainHandler {

  // Singleton Design-pattern to allow only 1 instance of MainHandler in the whole app
  private static MainHandler instance = null;

  private MainHandler() { }

  public static MainHandler getInstance() {
    if(instance == null) {
      instance = new MainHandler();
    }

    return instance;
  }

  // Management of requests
  public void getRequestTitle(RoutingContext ctx) {
      HttpServerResponse response = ctx.response();
      response.putHeader("content-type", "application/json; charset=UTF8").end(this.formatAnswer(this.getTitle()));
  }

  public void getRequestQuote(RoutingContext ctx) {
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "application/json; charset=UTF8").end(this.formatAnswer(this.getQuote()));
  }

  public void getRequestAuthor(RoutingContext ctx) {
    HttpServerResponse response = ctx.response();
    response.putHeader("content-type", "application/json; charset=UTF8").end(this.formatAnswer(this.getAuthor()));
  }

  // Return values for GET requests
  // Modify these values to change the text on your frontend
  private String getTitle() {
    return "La citation du jour";
  }

  private String getQuote() {
    return "Si vous ne pouvez expliquer un concept à un enfant de six ans, c'est que vous ne le comprenez pas complètement.";
  }

  private String getAuthor() {
    return "Albert Einstein";
  }

  private String formatAnswer(String result) {
    return new JsonObject().put("result", result).encode();
  }

}
