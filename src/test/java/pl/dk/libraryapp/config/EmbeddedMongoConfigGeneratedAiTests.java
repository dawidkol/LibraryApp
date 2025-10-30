package pl.dk.libraryapp.config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EmbeddedMongoConfigGeneratedAiTests {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private EmbeddedMongoConfig embeddedMongoConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkMongo_Success() {
        // GIVEN
        when(mongoTemplate.getDb().getName()).thenReturn("testDatabase");
        // WHEN
        embeddedMongoConfig.checkMongo(mongoTemplate);
        // THEN
        verify(mongoTemplate, times(1)).getDb();
        assertEquals("✅ Connected to MongoDB database: testDatabase", embeddedMongoConfig.logInfoMessage);
    }

    @Test
    void checkMongo_Failure() {
        // GIVEN
        when(mongoTemplate.getDb()).thenThrow(new RuntimeException("MongoDB connection failed"));
        // WHEN
        embeddedMongoConfig.checkMongo(mongoTemplate);
        // THEN
        verify(mongoTemplate, times(1)).getDb();
        assertEquals("❌ MongoDB connection failed: MongoDB connection failed", embeddedMongoConfig.logInfoMessage);
    }

}