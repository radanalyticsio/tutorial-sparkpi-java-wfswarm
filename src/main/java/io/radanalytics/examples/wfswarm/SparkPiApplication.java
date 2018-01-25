package io.radanalytics.examples.wfswarm;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/app")
public class SparkPiApplication extends Application {

    private Set<Object> singletons = new HashSet<Object>();

    public SparkPiApplication()
    {
        singletons.add(new SparkPiResource());
    }

    @Override
    public Set<Object> getSingletons()
    {
        return singletons;
    }
}
