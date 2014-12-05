/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.moviemastery.moviemastery.client;

import java.util.concurrent.Executor;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.converter.Converter;

/**
 * A Builder class for a Retrofit REST Adapter. Extends the default implementation by providing logic to
 * handle an OAuth 2.0 password grant login flow. The RestAdapter that it produces uses an interceptor
 * to automatically obtain a bearer token from the authorization server and insert it into all client
 * requests.
 * 
 * You can use it like this:
 * 
  	private VideoSvcApi videoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);
 * 
 * @author Jules, Mitchell
 *
 */
public class TokenInterceptorBuilder extends RestAdapter.Builder {

 	private class OAuthHandler implements RequestInterceptor {

		private Client client;
		private String tokenIssuingEndpoint;
		private String accessToken;

		public OAuthHandler(Client client, String tokenIssuingEndpoint, String accessToken) {
			this.client = client;
			this.tokenIssuingEndpoint = tokenIssuingEndpoint;
            this.accessToken = accessToken;
		}

		/**
		 * Every time a method on the client interface is invoked, this method is
		 * going to get called. The method checks if the client has previously obtained
		 * an OAuth 2.0 bearer token. If not, the method obtains the bearer token by
		 * sending a password grant request to the server. 
		 * 
		 * Once this method has obtained a bearer token, all future invocations will
		 * automatically insert the bearer token as the "Authorization" header in 
		 * outgoing HTTP requests.
		 * 
		 */
		@Override
		public void intercept(RequestFacade request) {
            request.addHeader("Authorization", "Bearer " + accessToken);
        }

    }

	private String loginUrl;
	private Client client;
    private String accessToken;
	
	public TokenInterceptorBuilder setLoginEndpoint(String endpoint){
		loginUrl = endpoint;
		return this;
	}

	@Override
	public TokenInterceptorBuilder setEndpoint(String endpoint) {
		return (TokenInterceptorBuilder) super.setEndpoint(endpoint);
	}

	@Override
	public TokenInterceptorBuilder setEndpoint(Endpoint endpoint) {
		return (TokenInterceptorBuilder) super.setEndpoint(endpoint);
	}

	@Override
	public TokenInterceptorBuilder setClient(Client client) {
		this.client = client;
		return (TokenInterceptorBuilder) super.setClient(client);
	}

	@Override
	public TokenInterceptorBuilder setClient(Provider clientProvider) {
		client = clientProvider.get();
		return (TokenInterceptorBuilder) super.setClient(clientProvider);
	}

	@Override
	public TokenInterceptorBuilder setErrorHandler(ErrorHandler errorHandler) {

		return (TokenInterceptorBuilder) super.setErrorHandler(errorHandler);
	}

	@Override
	public TokenInterceptorBuilder setExecutors(Executor httpExecutor,
			Executor callbackExecutor) {

		return (TokenInterceptorBuilder) super.setExecutors(httpExecutor,
				callbackExecutor);
	}

	@Override
	public TokenInterceptorBuilder setRequestInterceptor(
			RequestInterceptor requestInterceptor) {

		return (TokenInterceptorBuilder) super
				.setRequestInterceptor(requestInterceptor);
	}

	@Override
	public TokenInterceptorBuilder setConverter(Converter converter) {

		return (TokenInterceptorBuilder) super.setConverter(converter);
	}

	@Override
	public TokenInterceptorBuilder setProfiler(@SuppressWarnings("rawtypes") Profiler profiler) {

		return (TokenInterceptorBuilder) super.setProfiler(profiler);
	}

//	@Override
//	public SecuredRestBuilder setLog(Log log) {
//
//		return (SecuredRestBuilder) super.setLog(log);
//	}

	@Override
	public TokenInterceptorBuilder setLogLevel(LogLevel logLevel) {

		return (TokenInterceptorBuilder) super.setLogLevel(logLevel);
	}

	public TokenInterceptorBuilder setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	@Override
	public RestAdapter build() {
//		if (username == null || password == null) {
//			throw new OutOfMemoryError(
//					"You must specify both a username and password for a "
//							+ "SecuredRestBuilder before calling the build() method.");
//		}

//		if (client == null) {
//			client = new OkClient();
//		}
		OAuthHandler hdlr = new OAuthHandler(client, loginUrl, accessToken);
		setRequestInterceptor(hdlr);

		return super.build();
	}
}