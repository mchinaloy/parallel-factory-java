package carfactory.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import carfactory.infrastructure.message.CreateCoachworkMessage;
import carfactory.infrastructure.message.CreateEngineMessage;
import carfactory.infrastructure.message.CreateWheelMessage;
import org.springframework.context.annotation.Scope;
import carfactory.domain.component.Coachwork;
import carfactory.domain.component.Engine;
import carfactory.domain.component.Wheel;
import org.springframework.context.ApplicationContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static carfactory.extension.SpringExtension.SpringExtProvider;

@Named("componentFactory")
@Scope("prototype")
public class ComponentFactory extends UntypedActor {

    @Inject
    private ApplicationContext context;

    private ActorSystem system;
    private ActorRef wheelFilter;
    private ActorRef engineFilter;
    private ActorRef coachworkFilter;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof CreateCoachworkMessage){
            coachworkFilter.tell(new Coachwork("coachwork", UUID.randomUUID()), getSelf());
        } else if(message instanceof CreateWheelMessage){
            wheelFilter.tell(new Wheel("wheel", UUID.randomUUID()), getSelf());
        } else if(message instanceof CreateEngineMessage){
            engineFilter.tell(new Engine("engine", UUID.randomUUID()), getSelf());
        } else{
            system = context.getBean(ActorSystem.class);
            initializeActors();
            scheduleProduction();
        }
    }

    private void initializeActors() {
        system.actorOf(SpringExtProvider.get(system).props("assembleActor"), "assembleActor");
        system.actorOf(SpringExtProvider.get(system).props("redPaintActor"), "redPaintActor");
        system.actorOf(SpringExtProvider.get(system).props("bluePaintActor"), "bluePaintActor");
        system.actorOf(SpringExtProvider.get(system).props("greenPaintActor"), "greenPaintActor");
        system.actorOf(SpringExtProvider.get(system).props("mergeActor"), "mergeActor");
    }

    private void scheduleProduction(){
        wheelFilter = system.actorOf(SpringExtProvider.get(system).props("wheelFilterActor"), "wheelFilterActor");
        engineFilter = system.actorOf(SpringExtProvider.get(system).props("engineFilterActor"), "engineFilterActor");
        coachworkFilter = system.actorOf(SpringExtProvider.get(system).props("coachworkFilterActor"), "coachworkFilterActor");

        getContext().system().scheduler().schedule(Duration.create(0, TimeUnit.NANOSECONDS), Duration.create(0, TimeUnit.NANOSECONDS),
                getSelf(), new CreateWheelMessage(), getContext().system().dispatcher(), getSelf());
        getContext().system().scheduler().schedule(Duration.create(0, TimeUnit.NANOSECONDS), Duration.create(0, TimeUnit.NANOSECONDS),
                getSelf(), new CreateEngineMessage(), getContext().system().dispatcher(), getSelf());
        getContext().system().scheduler().schedule(Duration.create(0, TimeUnit.NANOSECONDS), Duration.create(0, TimeUnit.NANOSECONDS),
                getSelf(), new CreateCoachworkMessage(), getContext().system().dispatcher(), getSelf());
    }

}
