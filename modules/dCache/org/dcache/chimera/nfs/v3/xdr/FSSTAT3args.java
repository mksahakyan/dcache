/*
 * Automatically generated by jrpcgen 1.0.7 on 2/21/09 1:22 AM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package org.dcache.chimera.nfs.v3.xdr;
import org.dcache.chimera.nfs.v3.*;
import org.dcache.xdr.*;
import java.io.IOException;

public class FSSTAT3args implements XdrAble {
    public nfs_fh3 fsroot;

    public FSSTAT3args() {
    }

    public FSSTAT3args(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        fsroot.xdrEncode(xdr);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        fsroot = new nfs_fh3(xdr);
    }

}
// End of FSSTAT3args.java
