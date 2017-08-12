package com.nightcat.im.web;

import com.google.gson.Gson;
import com.nightcat.common.utility.Util;
import com.nightcat.common.utility.wangyi.CheckSumBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nightcat.common.utility.Util.uuid;

public class ImController {

    private String appKey = "47045ca57e2ca57f805cb24563e34160";
    private String appSecret = "818942ff3fac";

    String nonce = "12345";

    /**
    *{"info":{"token":"c38f44b2227761fedfccfa52c6949df0","accid":"cefc9d621eff42e4916fbc3813ca12c7","name":""},"code":200}
    */

    public static void main(String[] args) {
        new ImController().registerIm();
    }

    private HttpPost getPost() {
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码


        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //todo

        return httpPost;
    }

    public boolean registerIm() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpPost post = getPost();

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", uuid()));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;

        }

        // 执行请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        // 打印执行结果
        try {
            System.out.println("输出");
            String json = EntityUtils.toString(response.getEntity(), "utf-8");
            ImResponse imRep = Util.fromJson(json, ImResponse.class);
            System.out.println(Util.toJson(imRep));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static class ImUser {
        private String token;
        private String accid;
        private String name;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ImResponse {
        private String desc;
        private ImUser info;
        private int code;


        public ImUser getInfo() {
            return info;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setInfo(ImUser info) {
            this.info = info;
        }
    }

}
