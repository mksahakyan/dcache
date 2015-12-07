package org.dcache.webapi;

import org.dcache.util.Version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class Info {

    @GET
    @Path("/info")
    @Produces({"application/xml", "application/json"})
    public Version getDcahceVersion() {
        return Version.of(Info.class);
    }

}
