import pojo.Article;
import service.impl.ArticleServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Negen
 * @Date: 2022/04/01/10:51
 * @Description: csdn 自动浏览脚本
 */
public class MainApp {
    static final String USERNAME = "qq_36657751";
    public static void main(String[] args) {
        ArticleServiceImpl articleService = new ArticleServiceImpl();
        List<Article> oldArticles = articleService.getArticleListByUserId(USERNAME);

        Map<String, Article> oldUrlArticleMap
                = oldArticles.stream().collect(Collectors.toMap(Article::getUrl, article -> article));

        articleService.autoReadArticleByUrl(oldArticles, 100);

        List<Article> newArticles = articleService.getArticleListByUserId(USERNAME);

        Map<String, Article> newUrlArticleMap
                = newArticles.stream().collect(Collectors.toMap(Article::getUrl, article -> article));

        articleService.caclViewCount(oldUrlArticleMap, newUrlArticleMap);

    }
}
