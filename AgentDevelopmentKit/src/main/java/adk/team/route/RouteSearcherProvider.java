package comlib.adk.team.tactics;

import comlib.adk.util.route.IRouteSearcher;

public interface RouteSearcherProvider {

    //public void initRouteSearcher();

    public IRouteSearcher getRouteSearcher();
}
