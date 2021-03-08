package com.meowlomo.vmc.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meowlomo.vmc.retrofit.AuthenticationInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Component
public class RetrofitApi {
	private static final Logger logger = LoggerFactory.getLogger(RetrofitApi.class); 
    
    @Value("${vmc.config.retrofit.ems.baseUrl}")
    private  String emsApiBaseUrl ;//= "http://10.10.168.79:10100/";
    
    @Value("${vmc.config.retrofit.atm.baseUrl}")
    private  String atmApiBaseUrl ;
    
    private  retrofit2.Retrofit.Builder emsRetrofitBuilder;
    private  retrofit2.Retrofit.Builder atmRetrofitBuilder;
    
    private Builder httpClientBuilder;

    // No need to instantiate this class.
    @PostConstruct
    private void init() {
        //create http log interceptor
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        
        //create http client and apply logIterceptor
        this.httpClientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(logInterceptor);
        
        //create retrofit client
        this.emsRetrofitBuilder = new Retrofit.Builder()
                .baseUrl(this.emsApiBaseUrl)
                .client(this.httpClientBuilder.build())
                .addConverterFactory(JacksonConverterFactory.create());
        
        this.atmRetrofitBuilder = new Retrofit.Builder()
        		.baseUrl(this.atmApiBaseUrl)
        		.client(this.httpClientBuilder.build())
        		.addConverterFactory(JacksonConverterFactory.create());
    }

    public  <S> S createEmsService(Class<S> serviceClass) {
    	return this.emsRetrofitBuilder.build().create(serviceClass);
    }
    
    public  <S> S createEmsService(Class<S> serviceClass, final String authToken) {
    	if (!authToken.isEmpty()) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!this.httpClientBuilder.interceptors().contains(interceptor)) {
            	logger.info("add auth token to http client header");
                this.httpClientBuilder.addInterceptor(interceptor);
                this.emsRetrofitBuilder = emsRetrofitBuilder.client(httpClientBuilder.build());
            }
        }
    	return this.emsRetrofitBuilder.build().create(serviceClass);
    }
    
    public  <S> S createAtmService(Class<S> serviceClass) {
    	return this.atmRetrofitBuilder.build().create(serviceClass);
    }
    
    public  <S> S createAtmService(Class<S> serviceClass, final String authToken) {
    	if (!authToken.isEmpty()) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!this.httpClientBuilder.interceptors().contains(interceptor)) {
            	logger.info("add auth token to http client header");
                this.httpClientBuilder.addInterceptor(interceptor);
                this.atmRetrofitBuilder = atmRetrofitBuilder.client(httpClientBuilder.build());
            }
        }
    	return this.atmRetrofitBuilder.build().create(serviceClass);
    }
}