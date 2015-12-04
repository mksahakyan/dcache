package org.dcache.webapi;

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
import javax.ws.rs.core.MediaType;


import net.sf.saxon.instruct.Template;
import org.dcache.cells.CellStub;
import org.dcache.namespace.FileAttribute;
import org.dcache.vehicles.FileAttributes;

@Path("ws")
public class RestResponce {

    private static final String TEMPLATE = "Hello, %s!";

    public final static String FS = "org.dcache.webapi";
    public RestResponce() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Chimera resource started");


    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_XML)
    public JsonObject sayHello(@PathParam("name") String name) {
        JsonObject jsonObject = new JsonObject(String.format(TEMPLATE, name));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> NAME" + name);


        //return jsonObject;
        return  jsonObject;
    }


    @Context
    ServletContext ctx;




    /*@GET
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
*/

    @GET
    @Path("/stats/{fileid}")
    @Produces({"application/xml", "application/json"})
    public JsonObject getFileStatus(@PathParam("fileid") String fileid) throws IOException, CacheException, ClassNotFoundException, IllegalAccessException, InstantiationException {

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

        JsonObject jsonObject = new JsonObject(fa.getFileType().toString(), fa.getMode());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> jsonObject" + jsonObject.toString());


        return jsonObject;
    }

}