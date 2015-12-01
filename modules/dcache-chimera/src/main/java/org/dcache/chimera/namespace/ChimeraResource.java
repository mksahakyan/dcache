package org.dcache.chimera.namespace;

import com.google.common.base.Splitter;
import org.dcache.auth.Subjects;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;



/**
 * Created by sahakya on 11/18/15.
 */

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.google.common.base.Splitter;
import diskCacheV111.util.CacheException;
import diskCacheV111.util.FileExistsCacheException;
import diskCacheV111.util.PnfsId;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.dcache.chimera.namespace.ChimeraNameSpaceProvider;
import org.dcache.auth.Subjects;
import org.dcache.namespace.FileAttribute;

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
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> Chimera resource started");
        return "hello";
    }



    @Context
    ServletContext ctx;

    /*@Path("/mkdirs")
    @Produces("text/plain")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String mkdirs(@FormParam("path") String path,
                         @FormParam("uid") String uid,
                         @FormParam("gid") String gid)
            throws IOException, CacheException {

        ChimeraNameSpaceProvider nameSpaceProvider
                = (ChimeraNameSpaceProvider) ctx.getAttribute(FS);

        File asFile = new File(path);
        Splitter splitter = Splitter
                .on('/')
                .omitEmptyStrings();

        String root = "";
        for (String s : splitter.split(asFile.getParent())) {
            root = root + "/" + s;
            try {
                nameSpaceProvider.createEntry(Subjects.ROOT, root, 0, 0, 0700, true);
            } catch (FileExistsCacheException e) {
                // OK
            }
        }

        root = root + "/" + asFile.getName();
        PnfsId id = nameSpaceProvider.createEntry(Subjects.ROOT, root, Integer.parseInt(uid),
                Integer.parseInt(gid), 0700, true);
        return "OK: " + id;
    }*/

    @GET
    @Path("{quach}")
    public Response getASubResource() {
        System.out.println(ctx); // not null
        return Response.ok("OK").build();
    }
    @GET
    @Path("/stats/{fileid}")
    @Produces("text/plain")
    public String getFileStatus(@PathParam("fileid") String fileid) throws IOException, CacheException {


        ChimeraNameSpaceProvider nameSpaceProvider
                = (ChimeraNameSpaceProvider) ctx.getAttribute(FS);

        Set<FileAttribute> attrs = EnumSet.of(
                FileAttribute.MODE,
                FileAttribute.OWNER,
                FileAttribute.OWNER_GROUP,
                FileAttribute.PNFSID,
                FileAttribute.TYPE,
                FileAttribute.MODIFICATION_TIME,
                FileAttribute.CREATION_TIME);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> test for pnfsID" + ctx + ctx.getAttribute(FS));


       //String result = nameSpaceProvider.getFileAttributes(Subjects.ROOT, new PnfsId(fileid), attrs).toString();

        return "OK" ;

    }


}