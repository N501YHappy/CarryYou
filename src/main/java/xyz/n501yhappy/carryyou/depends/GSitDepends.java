package xyz.n501yhappy.carryyou.depends;

public class GSitDepends extends DependLoader {

    public GSitDepends() {
        super("GSit");
    }

    @Override
    protected void onLoad() {
        // GSit integration is purely event-based, no flag registration needed
    }
}
