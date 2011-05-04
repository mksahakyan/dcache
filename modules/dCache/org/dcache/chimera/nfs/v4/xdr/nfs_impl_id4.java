/*
 * Automatically generated by jrpcgen 1.0.7 on 2/21/09 1:22 AM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package org.dcache.chimera.nfs.v4.xdr;
import org.dcache.xdr.*;
import java.io.IOException;

public class nfs_impl_id4 implements XdrAble {
    public utf8str_cis nii_domain;
    public utf8str_cs nii_name;
    public nfstime4 nii_date;

    public nfs_impl_id4() {
    }

    public nfs_impl_id4(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        nii_domain.xdrEncode(xdr);
        nii_name.xdrEncode(xdr);
        nii_date.xdrEncode(xdr);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        nii_domain = new utf8str_cis(xdr);
        nii_name = new utf8str_cs(xdr);
        nii_date = new nfstime4(xdr);
    }

}
// End of nfs_impl_id4.java
