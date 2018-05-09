package hu.seacon.utils.rs.webservices;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("rest/getRobotIds")
public class GetRobotIds {

    @GET
    @Produces("text/plain; charset=UTF-8")
    public String getTodayEvents(String content) {
        InitialContext ic;
        String result = "[]";
        try {
            ic = new InitialContext();
            Object urlLunchService = ic.lookup("java:global/Homework4UniversalRobotWebServer/URLunchService!hu.seacon.middle.service.URLunchService");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
