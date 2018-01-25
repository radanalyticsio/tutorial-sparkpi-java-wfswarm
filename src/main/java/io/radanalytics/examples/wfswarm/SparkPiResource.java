package io.radanalytics.examples.wfswarm;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.core.Response;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public class SparkPiResource {

    @GET
    @Produces("text/plain")
    public Response getDefault() {
        return Response.ok("WildFly Swarm SparkPi server ready.\nAdd /sparkpi to invoke Pi computation.\n").build();
    }

    @GET
    @Path("/sparkpi")
    @Produces("text/plain")
    public Response getPi(@DefaultValue("2") @QueryParam("scale") int scale) {
        SparkPiProducer pi = this.lookupBean();
        if (pi == null) {
            throw new InternalServerErrorException("No SparkPiProducer found in CDI lookup!");
        }
        return Response.ok(pi.getPi(scale)).build();
    }

    // injection of the Provider doesn't work here but
    // CDI gives us an alternative which is to look up the
    // singleton bean in the current CDI context
    private SparkPiProducer lookupBean() {
        return (SparkPiProducer)CDI.current().select(SparkPiProducer.class).get();
    }

}
