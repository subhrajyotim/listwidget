package com.listapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class JsonRestTemplate extends RestTemplate {

    public static final String TAG = "JsonRestTemplate";

    public JsonRestTemplate() {
        super();
    }

    
    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables)
            throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
//        headers.set("Cookie", cookie);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
        ResponseEntity<T> responseEntity = this.exchange(url, HttpMethod.GET, requestEntity, responseType);
        T body = responseEntity.getBody();
        return body;
    }
    
}
