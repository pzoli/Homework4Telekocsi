package hu.seacon.utils.rs.webservices;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("rest/setURContent")
public class SetURContent {

    @POST()
    @Produces("text/plain; charset=UTF-8")
    @Consumes("application/json")
    public String getTodayEvents(String content) {
        InitialContext ic;
        try {
            ic = new InitialContext();
            Object urlLunchService = ic.lookup("java:global/Homework4UniversalRobotWebServer/URLunchService!hu.seacon.middle.service.URLunchService");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
