package service;

import pojo.Article;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Negen
 * @Date: 2022/04/01/16:13
 * @Description:
 */
public interface IArticleService {
    /**
     * 获取文章列表
     */
    List<Article> getArticleListByUserId(String username);

    /**
     * 阅读文章
     * @param article
     */
    void readArticleByUrl(Article article);

    /**
     * 计算阅读增长量
     */
     void caclViewCount(Map<String, Article> newUrlArticleMap, Map<String, Article> oldUrlArticleMap);


    /**
     * 自动阅读文章
     * @param articles 文章列表
     * @param count 要循环的次数
     */
    void autoReadArticleByUrl(List<Article> articles, int count);

}
