package com.example.teamcity.api.requests;

public interface CrudInterface {
    public Object create(Object o);
    public Object get(String id);
    public Object update(Object o);
    public Object delete(String id);
}
