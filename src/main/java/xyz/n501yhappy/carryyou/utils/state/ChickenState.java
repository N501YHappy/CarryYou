package xyz.n501yhappy.carryyou.utils.state;

import org.bukkit.entity.Chicken;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
public class ChickenState extends StatePusher {
    public static ChickenState instance;

    public static ChickenState getInstance() {
        if(instance == null) instance = new ChickenState();
        return instance;
    }

    public ChickenState() {
        super(Chicken.class);
    }

    @Override
    public boolean trigger() {
        return ConfigLoader.WITH_CHICKEN;
    }
}