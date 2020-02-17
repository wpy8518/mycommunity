package com.wpy.mycommunity.provider;

import com.alibaba.fastjson.JSON;
import com.wpy.mycommunity.dto.AccesstokenDTO;
import com.wpy.mycommunity.dto.GithubUser;

import okhttp3.*;
import org.springframework.stereotype.Component;

/**
 * Created by codedrinker on 2019/4/24.
 */
@Component

public class GithubProvider {
    public String getAccessToken(AccesstokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

}