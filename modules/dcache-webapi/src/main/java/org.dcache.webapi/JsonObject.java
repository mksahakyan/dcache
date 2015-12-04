package org.dcache.webapi;


import org.dcache.vehicles.FileAttributes;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by sahakya on 12/4/15.
 */

@XmlRootElement
public class JsonObject {


    /**
     * file's size
     */
    private long _size;

    /**
     * file's attribute change time
     */
    private long _ctime;

    /**
     * file's creation time
     */
    private String _creationTime;

    /**
     * file's last access time
     */
    private String _atime;

    /**
     * file's last modification time
     */
    private String _mtime;

    /**
     * file's owner's id
     */
    private String _owner;

    /**
     * file's group id
     */
    private int _group;

    /**
     * POSIX.1 file mode
     */
    private int _mode;

    /**
     * type of the file ( e.g. REG, DIR, LINK, SPECIAL )
     */
    private String _fileType;
    //@XmlElement
    private String name;

    public JsonObject() {
    } // JAXB needs this

    public JsonObject(String name) {
        this.name  = name;

    }

    public JsonObject(String fileType, int mode) {
        super();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> test for pnfsID" + fileType);

        this._fileType = fileType.toString();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> test for pnfsID" + _fileType);
    }

    @Override
    public String toString() {

        return new StringBuffer(" _fileType : ").append(this._fileType).toString();

    }
}