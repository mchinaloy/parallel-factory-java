package carfactory.domain.car;

import lombok.Data;
import carfactory.domain.component.Coachwork;
import carfactory.domain.component.Engine;
import carfactory.domain.component.Wheel;

import java.util.List;
import java.util.UUID;

@Data
public class Car {

    private final UUID serialNumber;
    private CarColor color;
    private final Engine engine;
    private final Coachwork coachwork;
    private final List<Wheel> wheels;

}
