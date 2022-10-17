package com.qa.client;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RestClient {
//    1.Get Method
    public CloseableHttpResponse get(String url,HashMap<String,String> headerMap) throws IOException {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //for headers
        for (Map.Entry<String,String> entry : headerMap.entrySet()){
            httpGet.addHeader(entry.getKey(),entry.getValue());
        }
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
        return closeableHttpResponse;
    }

//    Post Method
    public CloseableHttpResponse post(String url, String entityString, HashMap<String,String> headerMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);//for request

        try {
            httpPost.setEntity(new StringEntity(entityString));//for payload
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //for headers
        for (Map.Entry<String,String> entry : headerMap.entrySet()){
            httpPost.addHeader(entry.getKey(),entry.getValue());
        }

        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return closeableHttpResponse;
    }

}
