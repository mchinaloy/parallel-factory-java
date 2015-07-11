package carfactory.application.filter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import carfactory.domain.component.Engine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@Named("engineFilterActor")
@Scope("prototype")
public class EngineFilterActor extends UntypedActor {

    @Inject
    private ApplicationContext context;

    @Override
    public void onReceive(Object message) throws Exception {
        ActorSystem system = context.getBean(ActorSystem.class);

        if(((Engine) message).isFaulty()){
            log.debug("Received New Faulty Engine=" + message.toString());
        } else{
            log.debug("Received New Engine=" + message.toString());
            ActorRef assembleActor = system.actorFor("/user/assembleActor");
            assembleActor.tell(message, getSelf());
        }
    }

}
