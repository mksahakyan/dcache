package org.dcache.chimera.namespace;
/**
 * Created by sahakya on 11/18/15.
 */

import diskCacheV111.util.PnfsHandler;

import javax.ws.rs.core.*;

import java.io.IOException;


import diskCacheV111.util.CacheException;
import diskCacheV111.util.PnfsId;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.ServletContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import org.dcache.cells.CellStub;
import org.dcache.namespace.FileAttribute;
import org.dcache.vehicles.FileAttributes;

@Path("ws")
public class ChimeraResource {

    public final static String FS = "org.dcache.chimera.namespace";

    public ChimeraResource() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Chimera resource started");


    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello(@PathParam("name") String name) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Chimera resource started" + name);
        return "hello";
    }


    @Context
    ServletContext ctx;


    @GET
    @Path("{quach}")
    public Response getASubResource() {
        System.out.println(ctx); // not null
        return Response.ok("OK").build();
    }

    @GET
    @Path("/stats/{fileid}")
    @Produces("text/plain")
    public String getFileStatus(@PathParam("fileid") String fileid) throws IOException, CacheException, ClassNotFoundException, IllegalAccessException, InstantiationException {


        CellStub nameCellStub = (CellStub) (ctx.getAttribute(FS));

        PnfsHandler pnfs = new PnfsHandler(nameCellStub);
        Set<FileAttribute> attrs = EnumSet.of(
                FileAttribute.MODE,
                FileAttribute.OWNER,
                FileAttribute.OWNER_GROUP,
                FileAttribute.PNFSID,
                FileAttribute.TYPE,
                FileAttribute.MODIFICATION_TIME,
                FileAttribute.CREATION_TIME);
        FileAttributes fa = pnfs.getFileAttributes(new PnfsId(fileid), attrs);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> test for pnfsID" + fa);

        return "OK";
    }

}