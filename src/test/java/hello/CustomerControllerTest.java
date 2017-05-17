package hello;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfiguration;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.SpringMongoDbRule;
import com.mongodb.MockMongoClient;
import com.mongodb.MongoClient;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private ApplicationContext applicationContext;

	@LocalServerPort
	private int port;

	@Rule
	public MongoDbRule mongoDbRule = getSpringMongoDbRule();

	private String endpoint;
	
	public static SpringMongoDbRule getSpringMongoDbRule() {
		MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration();
		mongoDbConfiguration.setDatabaseName("test");
		MongoClient mongo = MockMongoClient.create(new Fongo("this-fongo-instance-is-ignored"));
		mongoDbConfiguration.setMongo(mongo);
		return new SpringMongoDbRule(mongoDbConfiguration);
	}	
	
	@Before
	public void setUp() {	
		endpoint = String.format("http://localhost:%d/v1/customers", port);
	}
	
	@Test
	public void create() {
		given().
		contentType(ContentType.JSON).
		body(new Customer().setFirstName("Angulo").setLastName("Agudo")).
	when().
		post(endpoint).
	then().
		statusCode(201).
		body("firstName", equalTo("Angulo")).
		body("lastName", equalTo("Agudo"));		
	}
	
	@Test
	@UsingDataSet(locations="initialData.json", loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
	public void getAll() {
		given().
			contentType(ContentType.JSON).
		when().
			get(endpoint).
		then().
			statusCode(200).
			body("firstName", contains("Miguel", "Joel", "Gabriel")).
			body("lastName", contains("Kaleu", "Cri", "Shh"));
	}	
}
