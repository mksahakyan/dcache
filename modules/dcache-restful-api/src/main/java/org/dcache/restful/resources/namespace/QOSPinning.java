package org.dcache.restful.resources.namespace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Range;
import diskCacheV111.util.*;
import diskCacheV111.vehicles.DCapProtocolInfo;
import org.dcache.cells.CellStub;
import org.dcache.namespace.FileAttribute;
import org.dcache.namespace.FileType;
import org.dcache.pinmanager.PinManagerCLI;
import org.dcache.pinmanager.PinManagerPinMessage;
import org.dcache.poolmanager.RemotePoolMonitor;
import org.dcache.restful.providers.JsonFileAttributes;
import org.dcache.restful.util.ServletContextHandlerAttributes;
import org.dcache.util.list.DirectoryEntry;
import org.dcache.util.list.DirectoryStream;
import org.dcache.util.list.ListDirectoryHandler;
import org.dcache.vehicles.FileAttributes;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by sahakya on 4/26/16.
 */
@Path("/qos")
public class QOSPinning {
    @Context
    ServletContext ctx;

    @Context
    HttpServletRequest request;


    @POST
    @Path("{value : .*}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void pin(@PathParam("value") String value, String request) throws CacheException {

        org.json.JSONObject json = new org.json.JSONObject(request.toString());
        String locality = (String) json.get("locality");
        String lifeTime = (String) json.get("lifeTime");

        Set<FileAttribute> attributes = EnumSet.allOf(FileAttribute.class);
        PnfsHandler handler = ServletContextHandlerAttributes.getPnfsHandler(ctx);

        FsPath path;
        if (value == null || value.isEmpty()) {
            path = FsPath.ROOT;
        } else {
            path = FsPath.create(FsPath.ROOT + value);
        }


        try {

            FileAttributes namespaceAttrributes = handler.getFileAttributes(path, attributes);
            String fileLocality = getLocality(namespaceAttrributes);
            if (fileLocality.equals("NEARLINE") && locality.equals("ONLINE")) {
                pin(namespaceAttrributes, Long.parseLong(lifeTime));
            }


        } catch (FileNotFoundCacheException e) {
            throw new NotFoundException(e);
        } catch (PermissionDeniedCacheException e) {
            throw new NotAllowedException(e);
        } catch (CacheException ex) {
            throw new InternalServerErrorException(ex);
        }

    }


    private String getLocality(FileAttributes namespaceAttrributes) throws CacheException {
        String locality = null;


        // we can pin only files not  directories
        if (namespaceAttrributes.getFileType() != FileType.DIR) {

            String client = request.getRemoteHost();
            RemotePoolMonitor remotePoolMonitor = ServletContextHandlerAttributes.getRemotePoolMonitor(ctx);
            //get locality of the specified file
            FileLocality fileLocality = remotePoolMonitor.getFileLocality(namespaceAttrributes, client);
            locality = fileLocality.toString();
        }
        return locality;
    }

    private void pin(FileAttributes attributes, long lifeTime) {

        if (attributes.getFileType() != FileType.DIR) {
            CellStub pinManagerStub = ServletContextHandlerAttributes.getPinMang(ctx);

            long millis = (lifeTime == -1) ? -1 : TimeUnit.SECONDS.toMillis(lifeTime);
            String client = request.getRemoteHost();

            DCapProtocolInfo protocolInfo =
                    new DCapProtocolInfo("DCap", 3, 0, new InetSocketAddress(client, 0));
            PinManagerPinMessage message =
                    new PinManagerPinMessage(attributes, protocolInfo, null, millis);
            pinManagerStub.notify(message);
        }


    }

}


