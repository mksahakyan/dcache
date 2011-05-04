/*
 * Automatically generated by jrpcgen 1.0.7 on 2/21/09 1:22 AM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package org.dcache.chimera.nfs.v4.xdr;
import org.dcache.xdr.*;
import java.io.IOException;

public class nfsace4 implements XdrAble {
    public acetype4 type;
    public aceflag4 flag;
    public acemask4 access_mask;
    public utf8str_mixed who;

    public nfsace4() {
    }

    public nfsace4(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        type.xdrEncode(xdr);
        flag.xdrEncode(xdr);
        access_mask.xdrEncode(xdr);
        who.xdrEncode(xdr);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        type = new acetype4(xdr);
        flag = new aceflag4(xdr);
        access_mask = new acemask4(xdr);
        who = new utf8str_mixed(xdr);
    }

}
// End of nfsace4.java
