package service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pojo.Article;
import service.IArticleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Negen
 * @Date: 2022/04/01/16:14
 * @Description:
 */
public class ArticleServiceImpl implements IArticleService {
    String baseUrl = "https://blog.csdn.net/community/home-api/v1/get-business-list?" +
            "page=1" +
            "&size=200" +
            "&businessType=blog" +
            "&orderby=" +
            "&noMore=false" +
            "&year=" +
            "&month=" +
            "&username=";


    @Override
    public List<Article> getArticleListByUserId(String username) {
        ArrayList<Article> articles = new ArrayList<>();
        Response response = null;
        try {
            String urlPath = baseUrl + username;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .url(urlPath)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0")
                    .build();
            response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String responseStr = responseBody.string();
            if (StrUtil.isEmpty(responseStr))return articles;
            JSONObject responseJson = JSON.parseObject(responseStr);
            JSONObject dataJson = (JSONObject)responseJson.get("data");
            JSONArray articleArr = (JSONArray)dataJson.get("list");
            for (int i = 0; i < articleArr.size(); i++) {
                JSONObject articleJson = (JSONObject)articleArr.get(i);
                String url = (String)articleJson.get("url");
                String title = (String)articleJson.get("title");
                Integer viewCount = (Integer)articleJson.get("viewCount");
                Article article = new Article();
                article.setTitle(title);
                article.setUrl(url);
                article.setViewCount(viewCount);
                articles.add(article);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            response.close();
        }
        return articles;
    }


    @Override
    public void readArticleByUrl(Article article) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String name = article.getTitle();
        String url = article.getUrl();
        Request req = new Request
                .Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:98.0) Gecko/20100101 Firefox/98.0")
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(req).execute();
            System.out.println(String.format("%s\t\t\t\tcode=%s", name, response.code()));
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            response.close();
        }
    }

    /**
     * 计算前后浏览量差异并打印出来
     */
    @Override
    public void caclViewCount(Map<String, Article> oldUrlArticleMap, Map<String, Article> newUrlArticleMap) {
        Set<String> articleUrls = oldUrlArticleMap.keySet();
        articleUrls.forEach(articleUrl -> {
            Article articleOld = oldUrlArticleMap.get(articleUrl);
            Article articleNew = newUrlArticleMap.get(articleUrl);
            if (null != articleOld && null != articleNew){
                String title = articleOld.getTitle();
                Integer viewCountOld = articleOld.getViewCount();
                Integer viewCountNew = articleNew.getViewCount();
                System.out.println(String.format("%s\t\t\t\t%d", title, (viewCountNew-viewCountOld)));
            }
        });
    }


    @Override
    public void autoReadArticleByUrl(List<Article> articles, int count) {
        int i = 0;
        int totalCout = count * articles.size();
        int counter = 0;
        while (i < count){
            for (int j = 0; j < articles.size(); j++) {
                Article article = articles.get(j);
                readArticleByUrl(article);
                counter++;
                float progress = (float)counter / (float)totalCout;
                System.out.println(String.format("当前进度%f%%", progress * 100));
            }
/*            articles.forEach(article -> {
                readArticleByUrl(article);
                counter++;
            });*/
            i++;
        }

    }
}
