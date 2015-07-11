package carfactory.application.filter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import carfactory.domain.component.Coachwork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@Named("coachworkFilterActor")
@Scope("prototype")
public class CouchworkFilterActor extends UntypedActor {

    @Inject
    private ApplicationContext context;

    @Override
    public void onReceive(Object message) throws Exception {
        ActorSystem system = context.getBean(ActorSystem.class);

        if(((Coachwork) message).isFaulty()){
            log.debug("Received New Faulty Coachwork=" + message.toString());
        } else{
            log.debug("Received New Coachwork=" + message.toString());
            ActorRef assembleActor = system.actorFor("/user/assembleActor");
            assembleActor.tell(message, getSelf());
        }
    }

}
