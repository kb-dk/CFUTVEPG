package testing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testing.model.ReducedRitzauProgram;
import testing.service.CfuTvService;
import testing.service.ServiceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 17-08-12
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
@Path("/metadata")
public class CfuTvServlet {
    private Logger log;
    private static CfuTvService service;

    public CfuTvServlet(){
        service = new CfuTvService();
        log = LoggerFactory.getLogger(CfuTvServlet.class);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_XML)
    public List<ReducedRitzauProgram> search(@QueryParam("channel_name") String channel_name,
                                             @QueryParam("from") String fromInput,
                                             @QueryParam("to") String toInput,
                                             @QueryParam("title") String title,
                                             @QueryParam("description") String description){
        Date from = null;
        Date to = null;
        if(fromInput != null && toInput != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            try{
                from = format.parse(fromInput);
                to = format.parse(toInput);
            } catch(ParseException ex){
                log.debug("Date parse error: From: " + fromInput + " | To: " + toInput);
                log.debug("Date parse error stacktrace: " + ex.getStackTrace());
            }
            if(from.compareTo(to) > 0){
                throw new InvalidIntervalException("from date " + from + " must be before to date " + to);
            }
        }

        try{
            return service.search(channel_name,from,to,title,description);
        } catch(ServiceException ex){
            throw new WebApplicationException(ex,Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/fullpost")
    @Produces("application/xml")
    public Response fullPost(@QueryParam("id") String programIdRaw){
        try{
            Long programId = Long.parseLong(programIdRaw);
            try{
                String result = service.getFullPost(programId);
                log.info("----------------------FULLPOST SUCCESS--------------------");
                return Response.status(200).entity(result).build();
            } catch(ServiceException ex){
                throw new WebApplicationException(ex,Response.Status.INTERNAL_SERVER_ERROR);
            }
        } catch(NumberFormatException ex){
            throw new WebApplicationException(ex,Response.Status.BAD_REQUEST);
        }
    }

    @GET
    @Path("/test")
    @Produces("application/xml")
    public Response getTest(){
        String output;
        output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>Test: " + new Date() + "</test>";
        log.info("getTest: " + output);
        return Response.status(200).entity(output).build();
    }
}
