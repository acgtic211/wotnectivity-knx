package es.ual.acg.utils;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.io.ByteArrayInputStream;

import javax.json.bind.*;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
    private final Jsonb jsonb;
    private final Class<T> type;
  
    
    /** 
     * @param type
     * @return JsonBodyHandler<T>
     */
    public static <T> JsonBodyHandler<T> jsonBodyHandler(final Class<T> type) {
      return jsonBodyHandler(JsonbBuilder.create(), type);
    }
  
    
    /** 
     * @param jsonb
     * @param type
     * @return JsonBodyHandler<T>
     */
    public static <T> JsonBodyHandler<T> jsonBodyHandler(final Jsonb jsonb,
        final Class<T> type) {
      return new JsonBodyHandler<>(jsonb, type);
    }
  
    
    /** 
     * @param jsonb
     * @param type
     * @return 
     */
    private JsonBodyHandler(Jsonb jsonb, Class<T> type) {
      this.jsonb = jsonb;
      this.type = type;
    }
  
    
    /** 
     * @param responseInfo
     * @return BodySubscriber<T>
     */
    @Override
    public HttpResponse.BodySubscriber<T> apply(
        final HttpResponse.ResponseInfo responseInfo) {
      return BodySubscribers.mapping(BodySubscribers.ofByteArray(),
          byteArray -> this.jsonb.fromJson(new ByteArrayInputStream(byteArray), this.type));
    }
  }