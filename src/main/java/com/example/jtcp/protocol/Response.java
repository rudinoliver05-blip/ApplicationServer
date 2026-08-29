package com.example.jtcp.protocol;

public class Response {
    private final String version;
    private final Status status;
    private final String message;
    public Response(String version,Status status,String message){
        this.version=version;
        this.status=status;
        this.message=message;
    }
    public String getVersion(){
        return this.version;
    }
    public Status getStatus(){
        return this.status;
    }
    public String getMessage(){
        return this.message;
    }
}
