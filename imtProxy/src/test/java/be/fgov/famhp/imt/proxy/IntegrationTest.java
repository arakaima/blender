package be.fgov.famhp.imt.proxy;

import be.fgov.famhp.imt.proxy.ImtProxyApp;
import be.fgov.famhp.imt.proxy.config.AsyncSyncConfiguration;
import be.fgov.famhp.imt.proxy.config.EmbeddedMongo;
import be.fgov.famhp.imt.proxy.config.TestSecurityConfiguration;
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
@SpringBootTest(classes = { ImtProxyApp.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedMongo
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
