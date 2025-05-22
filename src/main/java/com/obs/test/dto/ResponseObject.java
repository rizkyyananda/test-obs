package com.obs.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject<T> {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getCode() {
        return code;
    }

    public void setCode(String[] code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T content;

    @JsonIgnore
    HttpStatus statusCode;

    public ResponseObject(HttpStatus statusCode, String status){
        this.statusCode = statusCode;
        this.status = status;
        this.message = "";
    }

    public ResponseObject(HttpStatus statusCode, String status, T content){
        this.statusCode = statusCode;
        this.status = status;
        this.content = content;
        this.message = "";
    }

    public ResponseObject(HttpStatus statusCode, String status, String message){
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }

    public ResponseObject(HttpStatus statusCode, String status, String message, T content){
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
        this.content = content;
    }

    public ResponseObject(HttpStatus statusCode, String status, String message, String[]code){
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public ResponseObject(HttpStatus statusCode, String status, String message, String[]code, T content){
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
        this.content = content;
        this.code = code;
    }
}