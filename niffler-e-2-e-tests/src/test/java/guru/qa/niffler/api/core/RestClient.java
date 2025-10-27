package guru.qa.niffler.api.core;

import guru.qa.niffler.config.Config;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  private final OkHttpClient client;
  private final Retrofit retrofit;

  public RestClient(String baseUrl) {
    this(baseUrl, false, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory) {
    this(baseUrl, followRedirect, converterFactory, HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, Converter.Factory converterFactory) {
    this(baseUrl, false, converterFactory, HttpLoggingInterceptor.Level.HEADERS, null);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, Interceptor... interceptors) {
    this(baseUrl, followRedirect, converterFactory, HttpLoggingInterceptor.Level.HEADERS, interceptors);
  }

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level level, @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
        .followRedirects(followRedirect);

    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        clientBuilder.addNetworkInterceptor(interceptor);
      }
    }

    clientBuilder
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(level))
        .addNetworkInterceptor(new AllureOkHttp3()
            .setRequestTemplate("request-attachment.ftl")
            .setResponseTemplate("response-attachment.ftl")
        )
        .cookieJar(
            new JavaNetCookieJar(
                new CookieManager(
                    ThreadSafeCookieStore.INSTANCE,
                    CookiePolicy.ACCEPT_ALL
                )
            )
        );

    this.client = clientBuilder.build();
    this.retrofit = new Retrofit.Builder()
        .client(this.client)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build();
  }

  @Nonnull
  protected <T> T create(final Class<T> service) {
    return this.retrofit.create(service);
  }

  @Nonnull
  protected <T> Response<T> execute(Call<T> request) {
    Response<T> response;
    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertTrue(response.isSuccessful());
    return response;
  }

  public static class DefaultRestClient extends RestClient {

    public DefaultRestClient(String baseUrl) {
      super(baseUrl);
    }

    public DefaultRestClient(String baseUrl, boolean followRedirect) {
      super(baseUrl, followRedirect);
    }

    public DefaultRestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory) {
      super(baseUrl, followRedirect, converterFactory);
    }

    public DefaultRestClient(String baseUrl, Converter.Factory converterFactory) {
      super(baseUrl, converterFactory);
    }

    public DefaultRestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, Interceptor... interceptors) {
      super(baseUrl, followRedirect, converterFactory, interceptors);
    }

    public DefaultRestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level level, @org.jetbrains.annotations.Nullable Interceptor... interceptors) {
      super(baseUrl, followRedirect, converterFactory, level, interceptors);
    }

    @Override
    @NotNull
    public <T> T create(Class<T> service) {
      return super.create(service);
    }
  }
}
