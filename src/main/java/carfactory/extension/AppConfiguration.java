package carfactory.extension;

import akka.actor.ActorSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

import static carfactory.extension.SpringExtension.SpringExtProvider;

/**
 * The parallelcarfactory.application configuration.
 */
@Configuration
class AppConfiguration {

  // the parallelcarfactory.application context is needed to initialize the Akka Spring Extension
  @Inject
  private ApplicationContext applicationContext;

  /**
   * Actor system singleton for this parallelcarfactory.application.
   */
  @Bean
  public ActorSystem actorSystem() {
    ActorSystem system = ActorSystem.create("AkkaJavaSpring");
    // initialize the parallelcarfactory.application context in the Akka Spring Extension
    SpringExtProvider.get(system).initialize(applicationContext);
    return system;
  }
}
