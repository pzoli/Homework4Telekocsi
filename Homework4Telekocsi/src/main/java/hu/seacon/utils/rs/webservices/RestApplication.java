package hu.seacon.utils.rs.webservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    public RestApplication(){
         singletons.add(new SetURContent());
         singletons.add(new GetRobotIds());
    }
    @Override
    public Set<Class<?>> getClasses() {
         return empty;
    }
    @Override
    public Set<Object> getSingletons() {
         return singletons;
    }

}
