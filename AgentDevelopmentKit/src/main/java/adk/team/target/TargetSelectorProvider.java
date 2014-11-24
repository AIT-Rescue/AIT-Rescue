package comlib.adk.team.tactics;

import comlib.adk.util.target.TargetSelector;

public interface TargetSelectorProvider<T extends TargetSelector> {

    //public void initTargetSelector();

    public T getTargetSelector();
}
