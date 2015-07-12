package carfactory.application.merge;

import akka.actor.UntypedActor;
import carfactory.domain.car.Car;
import carfactory.infrastructure.message.CreateWheelMessage;
import carfactory.infrastructure.message.PerformanceCheckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import scala.concurrent.duration.Duration;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

@Slf4j
@Named("mergeActor")
@Scope("prototype")
public class MergeActor extends UntypedActor {

    int carsProduced = 0;
    boolean performanceCheckInitialized = false;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Car) {

            if (!performanceCheckInitialized) {
                performanceCheckInitialized = true;
                getContext().system().scheduler().schedule(Duration.create(1, TimeUnit.MINUTES), Duration.create(1, TimeUnit.MINUTES),
                        getSelf(), new PerformanceCheckMessage(), getContext().system().dispatcher(), getSelf());
            }

            log.debug("New Car assembled: " + message);
            carsProduced++;
        } else {
            log.info("Performance Check, number of cars produced in the last 1 minute: " + carsProduced);
            carsProduced = 0;
        }
    }

}
