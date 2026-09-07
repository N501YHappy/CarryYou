package xyz.n501yhappy.carryyou.utils.methods;

import org.bukkit.entity.Entity;
import xyz.n501yhappy.carryyou.utils.CarryMethod;

public class RideMethod implements CarryMethod {
    @Override
    public boolean isEmpty(Entity entity) {
        return entity.getPassengers().isEmpty();
    }

    @Override
    public boolean carryOn(Entity carrier, Entity carried) {
        return carrier.addPassenger(carried);
    }

    @Override
    public boolean drop(Entity carrier, Entity carried) {
        return carrier.removePassenger(carried);
    }
}
