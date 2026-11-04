package xyz.n501yhappy.carryyou.utils.state;

import org.bukkit.entity.Chicken;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
/**
 * Propagates the "a chicken is somewhere below this entity" state along carry
 * chains, so the configured effect can be applied to the topmost carrier.
 *
 * <p>Use {@link #getInstance()} to access the shared instance.
 */
public class ChickenState extends StatePusher {
    public static ChickenState instance;

    /**
     * Returns the singleton instance, creating it on first use.
     *
     * @return the shared {@code ChickenState} instance
     */
    public static ChickenState getInstance() {
        if(instance == null) instance = new ChickenState();
        return instance;
    }

    public ChickenState() {
        super(Chicken.class);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the chicken feature is enabled in the config
     */
    @Override
    public boolean trigger() {
        return ConfigLoader.WITH_CHICKEN;
    }
}