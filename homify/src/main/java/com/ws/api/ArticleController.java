package com.ws.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ws.model.Article;
import com.ws.model.Sum;
import com.ws.model.Status;
import com.ws.service.ArticleService;

@RestController
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	
	@RequestMapping(value = "/articleservice/article/{article_id}",
    		method = RequestMethod.PUT,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Status> putArticle(@PathVariable long article_id, @RequestBody String articleParameters) {
	Article article = articleService.putArticle(article_id, articleParameters);
	Status status = new Status();
	if(article != null)
		status.setStatus("ok");
	else
		status.setStatus("internal-error");
    return new ResponseEntity<Status>(status,HttpStatus.OK);
	}


    @RequestMapping(
    		value = "/articleservice/article/{article_id}",
    		method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Article> getArticleByID(@PathVariable long article_id) {       	    	  	
    	Article article = articleService.getArticleByID(article_id);
    	if(article!=null)
    		return new ResponseEntity<Article>(article,HttpStatus.OK);
    	else	
        	return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);   	
    }
    
    
    @RequestMapping(
    		value = "/articleservice/type/{type}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> getArticleIdsByType(@PathVariable String type) {     
    	List<Long> articleIds = articleService.getArticleIdsByType(type);
    	return new ResponseEntity<List<Long>>(articleIds,HttpStatus.OK);  	 
    }
    
    
    @RequestMapping(
    		value = "/articleservice/sum/{article_id}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sum> getSumArticleWeight(@PathVariable long article_id) {        
    Sum totalSum = articleService.getArticleWeightSum(article_id);
    return new ResponseEntity<Sum>(totalSum,HttpStatus.OK);
      	
    }
}
