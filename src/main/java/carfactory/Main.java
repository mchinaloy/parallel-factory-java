package carfactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import carfactory.infrastructure.message.StartProductionMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

import static carfactory.extension.SpringExtension.SpringExtProvider;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Inject
    private ApplicationContext context;

    private ActorSystem actorSystem;

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        actorSystem = context.getBean(ActorSystem.class);
        ActorRef componentFactory = actorSystem.actorOf(SpringExtProvider.get(actorSystem).props("componentFactory"), "componentFactory");
        componentFactory.tell(new StartProductionMessage(), null);
    }

}
