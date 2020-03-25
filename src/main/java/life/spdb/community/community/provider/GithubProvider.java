package life.spdb.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.spdb.community.community.dto.AccessTokenDTO;
import life.spdb.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        // post方法
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            // access_token=2694cf018905a3aca36b6ff704a8c8707e36983a&scope=user&token_type=bearer
            // 获取2694cf018905a3aca36b6ff704a8c8707e36983a
            String token = result.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据github返回的accessToken获取用户的信息，用于前台的展示
     * @param accessToken github返回的token
     * @return github用户
     */
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            GithubUser githubUser = JSON.parseObject(result,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
