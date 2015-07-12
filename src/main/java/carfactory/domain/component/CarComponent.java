package carfactory.domain.component;

import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
public abstract class CarComponent {

    private static int FAULTY_RATE = -1;

    protected final String type;
    protected final UUID serialNumber;
    protected final boolean faulty;

    public CarComponent(String type, UUID serialNumber){
        this.type = type;
        this.serialNumber = serialNumber;
        faulty = calculateFaultiness();
    }

    private boolean calculateFaultiness(){
        Random faulty = new Random();
        int randomFaulty = faulty.nextInt(10);
        return randomFaulty <= FAULTY_RATE;
    }
}
