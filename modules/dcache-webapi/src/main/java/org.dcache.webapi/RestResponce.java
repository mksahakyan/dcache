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

import org.dcache.cells.CellStub;
import org.dcache.namespace.FileAttribute;
import org.dcache.vehicles.FileAttributes;

@Path("ws")
public class RestResponce {

    @Context
    ServletContext ctx;

    public final static String FS = "org.dcache.webapi";

    public RestResponce() {
    }


    @GET
    @Path("/stats/{fileid}")
    @Produces({"application/xml", "application/json"})
    public FileAttributesInfo getFileStatus(@PathParam("fileid") String fileid) throws IOException, CacheException, ClassNotFoundException, IllegalAccessException, InstantiationException {

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

        FileAttributesInfo fileAttributesInfo = new FileAttributesInfo();
        fileAttributesInfo.setMode(fa.getMode());
        fileAttributesInfo.setOwner(fa.getOwner());
        fileAttributesInfo.setGroup(fa.getGroup());
        fileAttributesInfo.setPnfsId(fa.getPnfsId().toIdString());
        fileAttributesInfo.setMtime(fa.getModificationTime());
        fileAttributesInfo.setCreationTime(fa.getCreationTime());


        return fileAttributesInfo;
    }

}