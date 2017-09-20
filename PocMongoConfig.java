package poc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages="package", mongoTemplateRef = "mongoTemplateTask")
public class PocMongoConfig {

    private static final String TASK_DB = "task";

    @Bean
    public MongoClient mongoClientTask() throws Exception {
        return new MongoClient(new MongoClientURI("mongodb://login:pwd@domain:port"));
    }
    @Bean
    public MongoDbFactory mongoDbFactoryTask() throws Exception {
        return new SimpleMongoDbFactory(mongoClientTask(), TASK_DB);
    }
    @Primary
    @Bean
    public MongoTemplate mongoTemplateTask() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactoryTask());
        return mongoTemplate;
    }