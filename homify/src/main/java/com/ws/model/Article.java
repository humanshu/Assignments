package com.ws.model;

public class Article {

    private  long article_id;
    private  double weight;
    private  String type;
    private  long parent_id;

    /**
	 * @param article_id the article_id to set
	 */
	public void setArticle_id(long article_id) {
		this.article_id = article_id;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param parent_id the parent_id to set
	 */
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	public Article(long article_id, double weight, String type, long parent_id) {
        this.article_id = article_id;
        this.weight = weight;
        this.type = type;
        this.parent_id = parent_id;
    }

	public long getArticle_id() {
		return article_id;
	}

	public double getWeight() {
		return weight;
	}

	public String getType() {
		return type;
	}

	public long getParent_id() {
		return parent_id;
	}

}
