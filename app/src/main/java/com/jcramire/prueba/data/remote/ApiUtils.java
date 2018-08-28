package com.jcramire.prueba.data.remote;

public class ApiUtils {

    private ApiUtils(){}

    public static final String BASE_URL = "https://stage.allrideapp.com/api/v1/test/";

    public static ApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
