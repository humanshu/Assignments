Artcle webservice api

Project is created using Springboot in Spring tool suits and can be imported Spring Boot App

How to execute and test?
	-- Right click on root project and select Run as -- > Spring Boot App
Test: select src.test.java -> ArticleControllerTest.java class then right click and select Run as 
Junit Test

Asymptotic behavior which refers to the independent variables of a function (f(x)) to increase or decrease dynamically. WebService should be implemented in a way so that it can handle varying no of arguments of particular entity sent by users. In current implementation user can send different parameters of Article entity. Default values are provided for fields which are not provided by the user.
for example if user put only parent_id, webservice will insert other fields default values.
Default values:  "weight": -9999.999, "type": "not defined"

PUT /articleservice/article/111 
Body:
{ "parent_id": 10 }

GET /articleservice/article/111
{
  "article_id": 111,
  "weight": -9999.999,
  "type": "not defined",
  "parent_id": 10
}

Api specification:

Default values: { "weight": -9999.999, "type": "not defined", "parent_id": -99999 } inserted if user
did not send any parameter value of Article entity.

PUT /articleservice/article/$article_id
Body:
		{ "weight":double,"type":string,"parent_id":long }
where:
	article_id is a long specifying a new article
	weight is a double specifying the evaluation weight
	type is a string specifying a type of the article.
	parent_id is an optional long that may specify the parent article of this article, which this article
	extends.

GET /articleservice/article/$article_id
Returns:
	{ "weight":double,"type":string,"parent_id":long }GET /articleservice/types/$type

GET /articleservice/types/$type
Returns:
	[ long, long, .... ]
	A json list of all article ids that share the same type $type.

GET /articleservice/sum/$article_id
	Returns
	{ "sum", double }
	A sum of all articles that are transitively linked by their parent_id to $article_id.

Some simple examples would be:

	PUT /articleservice/article/10 { "weight": 5000, "type": "What are Transactions" }
	=> { "status": "ok" }
	PUT /articleservice/article/11 { "weight": 10000, "type": "Spring Transactional Mechanisms",
	"parent_id": 10 }
	=> { "status": "ok" }
	GET /articleservice/types/cars 
		10
	GET /articleservice/sum/10
		{"sum":15000}
	GET /articleservice/sum/11
	 {"sum":10000}