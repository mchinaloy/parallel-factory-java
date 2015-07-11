package carfactory.application.assemble;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.routing.*;
import carfactory.domain.component.Coachwork;
import carfactory.domain.component.Engine;
import carfactory.domain.component.Wheel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import carfactory.domain.car.Car;

@Slf4j
@Named("assembleActor")
@Scope("prototype")
public class AssembleActor extends UntypedActor {

    @Inject
    private ApplicationContext context;

    private ActorSystem system;

    private Router roundRobinRouter;
    private List<Routee> routes = new ArrayList<>();

    private List<Wheel> wheels = new LinkedList<>();
    private List<Engine> engines = new ArrayList<>();
    private List<Coachwork> coachworks = new ArrayList<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if(roundRobinRouter == null){
            system = context.getBean(ActorSystem.class);
            initializeRouter();
        }

        if(message instanceof Coachwork){
            log.debug("Received New Coachwork ready for assembly=" + message.toString());
            coachworks.add((Coachwork) message);
        } else if(message instanceof Engine){
            log.debug("Received New Engine ready for assembly=" + message.toString());
            engines.add((Engine) message);
        } else if(message instanceof Wheel){
            log.debug("Received New Wheel ready for assembly=" + message.toString());
            wheels.add((Wheel) message);
        }
        canAssembleCar();
    }

    private void initializeRouter(){
        ActorRef redPaintActor = system.actorFor("/user/redPaintActor");
        ActorRef bluePaintActor = system.actorFor("/user/bluePaintActor");
        ActorRef greenPaintActor = system.actorFor("/user/greenPaintActor");
        routes.add(new ActorRefRoutee(redPaintActor));
        routes.add(new ActorRefRoutee(bluePaintActor));
        routes.add(new ActorRefRoutee(greenPaintActor));
        roundRobinRouter = new Router(new RoundRobinRoutingLogic(), routes);
    }

    private void canAssembleCar(){
        if(wheels.size() >= 4 && engines.size() >= 1 && coachworks.size() >= 1){
            List<Wheel> wheelsToUse = new LinkedList<>();
            for(int i=0; i < 4; i++){
                wheelsToUse.add(wheels.remove(0));
            }
            roundRobinRouter.route(new Car(UUID.randomUUID(), engines.remove(0), coachworks.remove(0), wheelsToUse), getSelf());
        }
    }

}
