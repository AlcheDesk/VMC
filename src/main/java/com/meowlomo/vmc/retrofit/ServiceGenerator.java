package com.meowlomo.vmc.retrofit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceGenerator {  
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceGenerator.class); 
	
    private  String apiBaseUrl = "http://localhost:8080/";
    
    private  retrofit2.Retrofit.Builder retrofitBuilder;

    private Builder httpClientBuilder;

    // No need to instantiate this class.
    private ServiceGenerator() {
        //create http log interceptor
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        
        //create http client and apply logIterceptor
        this.httpClientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(logInterceptor);
        
        //create retrofit client
        this.retrofitBuilder = new Retrofit.Builder()
                .baseUrl(this.apiBaseUrl)
                .client(this.httpClientBuilder.build())
                .addConverterFactory(JacksonConverterFactory.create());
    }

    public  void changeApiBaseUrl(String newApiBaseUrl) {
        this.apiBaseUrl = newApiBaseUrl;
        
        //recreate retrofit client
        this.retrofitBuilder = new Retrofit.Builder()
                .baseUrl(this.apiBaseUrl)
                .client(this.httpClientBuilder.build())
                .addConverterFactory(JacksonConverterFactory.create());
    }

    public  <S> S createService(Class<S> serviceClass) {
    	return this.retrofitBuilder.build().create(serviceClass);
    }
    
    public  <S> S createService(Class<S> serviceClass, final String authToken) {
    	if (!authToken.isEmpty()) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
            if (!this.httpClientBuilder.interceptors().contains(interceptor)) {
            	logger.info("add auth token to http client header");
                this.httpClientBuilder.addInterceptor(interceptor);
                this.retrofitBuilder = retrofitBuilder.client(httpClientBuilder.build());
            }
        }
    	return this.retrofitBuilder.build().create(serviceClass);
    }
}
