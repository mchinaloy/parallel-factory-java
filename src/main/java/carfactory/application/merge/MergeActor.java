package carfactory.application.merge;

import akka.actor.UntypedActor;
import carfactory.domain.car.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;

@Slf4j
@Named("mergeActor")
@Scope("prototype")
public class MergeActor extends UntypedActor {

    int carsProduced = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        carsProduced++;
        log.info("New Car fully assembled: " + message + " is number: " + carsProduced);
    }

}
