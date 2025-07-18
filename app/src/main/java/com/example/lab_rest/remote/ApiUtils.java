package com.example.lab_rest.remote;

public class ApiUtils {

    // REST API server URL
    public static final String BASE_URL = "http://178.128.220.20/db_pushandpray/api/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    public static ItemService getItemService(){
        return RetrofitClient.getClient(BASE_URL).create(ItemService.class);
    }

    public static RequestService getRequestService(){
        return RetrofitClient.getClient(BASE_URL).create(RequestService.class);
    }



}