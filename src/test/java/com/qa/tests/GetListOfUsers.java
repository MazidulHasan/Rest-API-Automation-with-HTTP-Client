package com.qa.tests;

import com.qa.TestUtils.TestUtil;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class GetListOfUsers extends TestBase {
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
    public void getListOfUsers(){
        HashMap<String ,String > headerMap = new HashMap<String,String>();
        headerMap.put("Content-Type","application/json"); //we can put as many headers as we like

        try {
            closeableHttpResponse = restClient.get(url,headerMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        a.status code
        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code is : "+ statusCode);

        //verify status code
        Assert.assertEquals(statusCode,Response_Status_code_200,"Status code did not match");

//        json String
        String responseString = null;
        try {
            responseString = EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Errro while getting response and converting it to string"+e);
        }
        System.out.println("Raw response is : "+ responseString);

        JSONObject responseJsonObject = new JSONObject(responseString);
        System.out.println("After jsno Object we get : "+ responseJsonObject);

//        All headers
        Header[] headersArray = closeableHttpResponse.getAllHeaders();
        HashMap<String,String> allHeaders = new HashMap<String,String>();

        for (Header header : headersArray){
            allHeaders.put(header.getName(), header.getValue());
        }
        System.out.println("All headers are : "+ allHeaders);

        String perPageValue = TestUtil.getValueByJPath(responseJsonObject,"/per_page");
        System.out.println("Value of per_page is :"+ perPageValue);

        Assert.assertEquals(Integer.parseInt(perPageValue) ,6);

        String totalValue = TestUtil.getValueByJPath(responseJsonObject,"/total");
        System.out.println("Value of totalValue is :"+ totalValue);
        Assert.assertEquals(Integer.parseInt(totalValue),12,"Total value did not match");


//        get values from json array
        String lastName = TestUtil.getValueByJPath(responseJsonObject,"/data/0/last_name");
        String id = TestUtil.getValueByJPath(responseJsonObject,"/data/0/id");
        System.out.println("Last Name is: "+lastName+" and id is: "+id);

        Assert.assertEquals(lastName,"Lawson","Last name do not match");
        Assert.assertEquals(id,"7","Id do not match");

    }
}
