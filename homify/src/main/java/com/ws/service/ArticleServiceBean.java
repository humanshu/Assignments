package com.ws.service;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ws.model.Article;
import com.ws.model.Sum;
import com.ws.utils.JsonUtils;

@Service
public class ArticleServiceBean implements ArticleService{

    private static Map<Long, Article> articleMap = new HashMap<Long, Article>(); ;  
		
    public Article  putArticle(long article_id, String articleParameters) {		
		Article article = JsonUtils.getObject(articleParameters, Article.class);		
			article.setArticle_id(article_id);
			if(article.getWeight() == 0.0)
				article.setWeight(-9999.999);
			if(article.getParent_id() == 0)
				article.setParent_id(-99999);
			if(article.getType()==null || article.getType().equals(""))
				article.setType("not defined");
			articleMap.put(article.getArticle_id(), article);

		return article;
	}

    public Article  getArticleByID(long article_id) {       	
    	if(articleMap != null && articleMap.containsKey(article_id))
    		return articleMap.get(article_id);
    	else	
    	return null;
    }
    
    
    public List<Long> getArticleIdsByType(String type) {     
    	List<Long> articleIds = new ArrayList<Long>();				
		for(Article article : articleMap.values()){			
			if(article.getType().equalsIgnoreCase(type))
				articleIds.add(article.getArticle_id());
		}	
		return articleIds;
    }
    
    
     public Sum getArticleWeightSum(long article_id) {        
    	Sum totalSum = new Sum();
		double sum = 0;
		for(Article article : articleMap.values()){			
			if(article.getParent_id() == article_id || article.getArticle_id() == article_id)				
				sum = sum + article.getWeight();				
		}		
		totalSum.setSum(sum);
		return totalSum;	
    }
}
