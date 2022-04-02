package pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Negen
 * @Date: 2022/04/01/15:20
 * @Description:
 */
@Data
public class Article {
    /**
     * 文章标题
     */
    String title;
    /**
     * 文章地址
     */
    String url;
    /**
     * 阅读数
     */
    Integer viewCount;
}
