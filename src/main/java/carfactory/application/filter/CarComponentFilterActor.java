package carfactory.application.filter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import carfactory.domain.component.CarComponent;
import carfactory.domain.component.Coachwork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@Named("carComponentFilterActor")
@Scope("prototype")
public class CarComponentFilterActor extends UntypedActor {

    @Inject
    private ApplicationContext context;

    @Override
    public void onReceive(Object message) throws Exception {
        ActorSystem system = context.getBean(ActorSystem.class);

        if(((CarComponent) message).isFaulty()){
            log.debug("Received New Faulty Component=" + message.toString());
        } else{
            log.debug("Received New Working Component=" + message.toString());
            ActorRef assembleActor = system.actorFor("/user/assembleActor");
            assembleActor.tell(message, getSelf());
        }
    }

}
