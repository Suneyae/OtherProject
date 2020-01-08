package com.test.javaAPI.wechat.entity;

import java.util.List;

import com.test.javaAPI.wechat.MessagePub;

/**
 * 图文消息的实体对象
 * 
 * @author Wei
 * @time 2016年10月14日 下午10:32:15
 */
public class ArticleMessage extends MessagePub {
	private int ArticleCount;
	private List<Articles> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Articles> getArticles() {
		return Articles;
	}

	public void setArticles(List<Articles> articles) {
		Articles = articles;
	}

}
