package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;

public interface CarryMethod {
    boolean isEmpty(Entity entity);
    boolean carryOn(Entity carrier,Entity carried);
    boolean drop(Entity carrier,Entity carried);
}
