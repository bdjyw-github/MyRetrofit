package com.idophin.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataFactory {
    public static Object getInstanceByJson(Class<?> clazz, String json) {
        Object obj = null;
        Gson gson = new Gson();
        try {
            obj = gson.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * @param json
     * @param clazz
     * @return
     * @author I321533
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        List<T> list = null;
        Gson gson = new Gson();
        try {
            T[] array = gson.fromJson(json, clazz);
            list = Arrays.asList(array);
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<T>();
        try {
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * javabean to json
     *
     * @param clazz
     * @return
     */
    public static <T> String javabeanToJson(T clazz) {
        Gson gson = new Gson();
        String json = gson.toJson(clazz);
        return json;
    }

    /**
     * list to json
     *
     * @param list
     * @return
     */
    public static <T> String listToJson(List<Class<T>> list) {

        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    /**
     * map to json
     *
     * @param map
     * @return
     */
    public static <T> String mapToJson(Map<String, Class<T>> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    public interface GsonCallback{
        boolean shouldSkipField(FieldAttributes attr);
    }

    public static <T> ArrayList<T> customArrayFromJson(String json, Class<T> clazz, final GsonCallback callback){
        Gson gson = new GsonBuilder()
                .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes arg0) {
                        return callback.shouldSkipField(arg0);
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> arg0) {
                        return false;
                    }
                }).create();

        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<T>();
        try {
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(gson.fromJson(jsonObject, clazz));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }
}