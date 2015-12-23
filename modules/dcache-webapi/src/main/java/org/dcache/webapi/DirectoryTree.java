package org.dcache.webapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import diskCacheV111.poolManager.PoolMonitorV5;
import diskCacheV111.util.CacheException;
import diskCacheV111.util.FsPath;
import org.dcache.auth.PasswordCredential;
import org.dcache.auth.UidPrincipal;
import org.dcache.auth.UserNamePrincipal;
import org.dcache.util.list.DirectoryEntry;
import org.dcache.util.list.DirectoryStream;
import org.dcache.util.list.ListDirectoryHandler;

import javax.security.auth.Subject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.dcache.namespace.FileAttribute.*;


@Path("/path")
public class DirectoryTree {
    @Context
    ServletContext ctx;
    public final static String DL = "org.dcache.webapi";

    String username = "admin";
    String password = "dickerelch";

    /*
    default behaviour
     */
    @Path("/root")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<DirectoryEntry> getRoot() throws InterruptedException, CacheException {
        Subject subject = new Subject();
        PasswordCredential pass =
                new PasswordCredential(username, String.valueOf(password));
        UserNamePrincipal principals = new UserNamePrincipal(username);
        UidPrincipal uidPrincipal = new UidPrincipal("0");
        subject.getPrincipals().add(principals);
        subject.getPublicCredentials().add(pass);
        subject.getPrivateCredentials().add(pass);
        subject.getPrincipals().add(uidPrincipal);


        FsPath userRoot = new FsPath("/");
        ListDirectoryHandler listDirectoryHandler = (ListDirectoryHandler) (ctx.getAttribute(DL));
        DirectoryStream stream = listDirectoryHandler.list(subject, userRoot, null, Range.<Integer>all(),
                EnumSet.copyOf(Sets.union(PoolMonitorV5.getRequiredAttributesForFileLocality(),
                        EnumSet.of(MODIFICATION_TIME, TYPE, SIZE, PNFSID))));

        return Lists.newArrayList(stream);
    }



    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{value}/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileAttributesObject> postDirectory(@PathParam("value") String value,
                                                    Person person) throws InterruptedException, CacheException {


        String userName = person.getUsername();
        String password = person.getPassword();
        String tocken = person.getToken();

        Subject subject = new Subject();
        PasswordCredential pass =
                new PasswordCredential(userName, String.valueOf(password));
        UserNamePrincipal principals = new UserNamePrincipal(userName);
        UidPrincipal uidPrincipal = new UidPrincipal("0");
        subject.getPrincipals().add(principals);
        subject.getPublicCredentials().add(pass);
        subject.getPrivateCredentials().add(pass);
        subject.getPrincipals().add(uidPrincipal);


        FsPath userRoot = new FsPath(value);
        ListDirectoryHandler listDirectoryHandler = (ListDirectoryHandler) (ctx.getAttribute(DL));
        DirectoryStream stream = listDirectoryHandler.list(subject, userRoot, null, Range.<Integer>all(),
                EnumSet.copyOf(Sets.union(PoolMonitorV5.getRequiredAttributesForFileLocality(),
                        EnumSet.of(MODIFICATION_TIME, TYPE, SIZE, PNFSID))));


        List<FileAttributesObject> fileAttributesList = new ArrayList<>();
        for (DirectoryEntry entry : stream) {
            FileAttributesObject fileAttributes = new FileAttributesObject();
            fileAttributes.set_mtime(entry.getFileAttributes().getModificationTime());
            fileAttributes.set_size(entry.getFileAttributes().getSize());
            fileAttributes.set_pnfsId(entry.getFileAttributes().getPnfsId().getId());
            fileAttributes.set_fileName(entry.getName());

            fileAttributesList.add(fileAttributes);
        }

        System.out.println("First Name = "+ person.getFirstName());
        System.out.println("Last Name  = "+ person.getLastName());
        System.out.println("First Name = "+ person.getPassword());
        System.out.println("PATH = "+ userRoot.getName());


        return fileAttributesList;
    }

    /*//@POST
    // @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{path}/{username}/{password}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileAttributesObject> getDirectory(@PathParam("path") String path,
                                                   @PathParam("username") String userName,
                                                   @PathParam("password") String passWord) throws InterruptedException, CacheException {
        Subject subject = new Subject();
        PasswordCredential pass =
                new PasswordCredential(userName, String.valueOf(passWord));
        UserNamePrincipal principals = new UserNamePrincipal(userName);
        UidPrincipal uidPrincipal = new UidPrincipal("0");
        subject.getPrincipals().add(principals);
        subject.getPublicCredentials().add(pass);
        subject.getPrivateCredentials().add(pass);
        subject.getPrincipals().add(uidPrincipal);


        FsPath userRoot = new FsPath(path);
        ListDirectoryHandler listDirectoryHandler = (ListDirectoryHandler) (ctx.getAttribute(DL));
        DirectoryStream stream = listDirectoryHandler.list(subject, userRoot, null, Range.<Integer>all(),
                EnumSet.copyOf(Sets.union(PoolMonitorV5.getRequiredAttributesForFileLocality(),
                        EnumSet.of(MODIFICATION_TIME, TYPE, SIZE, PNFSID))));


        List<FileAttributesObject> fileAttributesList = new ArrayList<>();
        for (DirectoryEntry entry : stream) {
            FileAttributesObject fileAttributes = new FileAttributesObject();
            fileAttributes.set_mtime(entry.getFileAttributes().getModificationTime());
            fileAttributes.set_size(entry.getFileAttributes().getSize());
            fileAttributes.set_pnfsId(entry.getFileAttributes().getPnfsId().getId());
            fileAttributes.set_fileName(entry.getName());

            fileAttributesList.add(fileAttributes);
        }
        return fileAttributesList;
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/path/{username}/{value}/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileAttributesObject> postDirectory(@PathParam("username") String userName,
                                                    @PathParam("value") String value,
                                                    Person person) throws InterruptedException, CacheException {


        Subject subject = new Subject();
        PasswordCredential pass =
                new PasswordCredential(userName, String.valueOf(""));
        UserNamePrincipal principals = new UserNamePrincipal(userName);
        UidPrincipal uidPrincipal = new UidPrincipal("0");
        subject.getPrincipals().add(principals);
        subject.getPublicCredentials().add(pass);
        subject.getPrivateCredentials().add(pass);
        subject.getPrincipals().add(uidPrincipal);


        FsPath userRoot = new FsPath(value);
        ListDirectoryHandler listDirectoryHandler = (ListDirectoryHandler) (ctx.getAttribute(DL));
        DirectoryStream stream = listDirectoryHandler.list(subject, userRoot, null, Range.<Integer>all(),
                EnumSet.copyOf(Sets.union(PoolMonitorV5.getRequiredAttributesForFileLocality(),
                        EnumSet.of(MODIFICATION_TIME, TYPE, SIZE, PNFSID))));


        List<FileAttributesObject> fileAttributesList = new ArrayList<>();
        for (DirectoryEntry entry : stream) {
            FileAttributesObject fileAttributes = new FileAttributesObject();
            fileAttributes.set_mtime(entry.getFileAttributes().getModificationTime());
            fileAttributes.set_size(entry.getFileAttributes().getSize());
            fileAttributes.set_pnfsId(entry.getFileAttributes().getPnfsId().getId());
            fileAttributes.set_fileName(entry.getName());

            fileAttributesList.add(fileAttributes);
        }

        System.out.println("First Name = "+ person.getFirstName());
        System.out.println("Last Name  = "+ person.getLastName());
        System.out.println("First Name = "+ person.getPassword());

        return fileAttributesList;
    }

*/

}