package com.ws.utils;

import com.google.gson.Gson;

public final class JsonUtils {  
    private JsonUtils() {}  
    public static <T> T getObject(final String jsonString, final Class<T> objectClass) {  
         Gson gson = new Gson();  
         return gson.fromJson(jsonString, objectClass);  
    }  
    public static String getString(final Object object) {  
         Gson gson = new Gson();  
         return gson.toJson(object);  
    }  
}  