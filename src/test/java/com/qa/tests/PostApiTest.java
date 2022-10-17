package com.qa.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PostApiTest extends TestBase {
    TestBase testBase;
    String serviceUrl;
    String apiURL;
    String url;
    RestClient restClient;
    CloseableHttpResponse closeableHttpResponse;

    @BeforeMethod
    public void setUp(){
        testBase = new TestBase();
        serviceUrl = prop.getProperty("URL");
        apiURL = prop.getProperty("listofUserURL");
        url = serviceUrl+apiURL;
        restClient = new RestClient();
    }
    @Test
    public void postApiTest() throws IOException {
        restClient = new RestClient();

        //for headers
        HashMap<String ,String > headerMap = new HashMap<String,String>();
        headerMap.put("Content-Type","application/json"); //we can put as many headers as we like

        //Jackson API
        ObjectMapper objectMapper = new ObjectMapper();
        Users users = new Users("Austin","Youtuber");

        String usersJsonString;
        try {
            //object to json file conversion
            objectMapper.writeValue(new File("src/main/java/com/qa/data/users.json"),users);
            //object to json in string
            usersJsonString =  objectMapper.writeValueAsString(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        closeableHttpResponse = restClient.post(url,usersJsonString,headerMap);

        //1.status code check
        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode,testBase.Response_Status_code_201);

        //Json String
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
        JSONObject responseJson = new JSONObject(responseString);
        System.out.println("The response from the API is : "+ responseJson);

        //json to java object
        Users userResponseObj = objectMapper.readValue(responseString,Users.class);
        System.out.println("userResponseObj is : "+ userResponseObj);

        Assert.assertTrue(users.getName().equals(userResponseObj.getName()));
        Assert.assertTrue(users.getJob().equals(userResponseObj.getJob()));
        System.out.println("User Id is :"+ userResponseObj.getId());
        System.out.println("User created at :"+ userResponseObj.getCreatedAt());
    }
}
