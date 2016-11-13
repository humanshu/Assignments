package com.ws.service;

import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ws.model.Article;
import com.ws.model.Sum;


public interface ArticleService {

	Article  putArticle(long article_id, String articleParameters);
	Article  getArticleByID( long article_id);
	List<Long> getArticleIdsByType(String type);
	Sum getArticleWeightSum(long article_id);	    
	    
}
