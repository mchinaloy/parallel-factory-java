package carfactory.application.paint;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import carfactory.domain.car.Car;
import carfactory.domain.car.CarColor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Named("bluePaintActor")
@Scope("prototype")
public class BluePaintActor extends UntypedActor {

    @Inject
    private ApplicationContext applicationContext;

    @Override
    public void onReceive(Object message) throws Exception {
        ActorSystem system = applicationContext.getBean(ActorSystem.class);

        Car car = (Car) message;
        car.setColor(CarColor.BLUE);

        ActorRef mergeActor = system.actorFor("/user/mergeActor");
        mergeActor.tell(car, getSelf());
    }

}
