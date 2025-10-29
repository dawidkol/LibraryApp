package pl.dk.libraryapp.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile({"dev"})
@ConditionalOnClass(MongoAutoConfiguration.class)
@Slf4j
class EmbeddedMongoConfig {

    @Bean
    public CommandLineRunner checkMongo(MongoTemplate mongoTemplate) {
        return args -> {
            log.info("Checking MongoDB connection...");
            try {
                String dbName = mongoTemplate.getDb().getName();
                log.info("✅ Connected to MongoDB database: {} ", dbName);
            } catch (Exception e) {
                log.error("❌ MongoDB connection failed: {} ", e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
