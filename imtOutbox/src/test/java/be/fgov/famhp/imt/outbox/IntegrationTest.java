package be.fgov.famhp.imt.outbox;

import be.fgov.famhp.imt.outbox.ImtOutboxApp;
import be.fgov.famhp.imt.outbox.config.AsyncSyncConfiguration;
import be.fgov.famhp.imt.outbox.config.EmbeddedMongo;
import be.fgov.famhp.imt.outbox.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { ImtOutboxApp.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
