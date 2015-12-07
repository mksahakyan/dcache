package org.dcache.webapi;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by sahakya on 12/4/15.
 */

@XmlRootElement
public class FileAttributesInfo {

    /**
     * file's size
     */
    @XmlElement
    private long size;

    /**
     * file's attribute change time
     */
    @XmlElement
    private long ctime;

    /**
     * file's creation time
     */
    @XmlElement
    private long creationTime;

    /**
     * file's last access time
     */
    @XmlElement
    private long atime;

    /**
     * file's last modification time
     */
    @XmlElement
    private long mtime;


    /**
     * file's owner's id
     */
    @XmlElement
    private int owner;

    /**
     * file's group id
     */
    @XmlElement
    private int group;

    /**
     * POSIX.1 file mode
     */
    @XmlElement
    private int mode;

    /**
     * type of the file ( e.g. REG, DIR, LINK, SPECIAL )
     */
    @XmlElement
    private String fileType;


    @XmlElement
    private String pnfsId;


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getAtime() {
        return atime;
    }

    public void setAtime(long atime) {
        this.atime = atime;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPnfsId() {
        return pnfsId;
    }

    public void setPnfsId(String pnfsId) {
        this.pnfsId = pnfsId;
    }

    // JAXB needs this
    public FileAttributesInfo() {
    }


    public FileAttributesInfo(String fileType, String pnfsId) {
        super();

        this.fileType = fileType.toString();
        this.pnfsId = pnfsId;


    }

    @Override
    public String toString() {
        return "FileAttributesInfo{" +
                "size=" + size +
                ", ctime=" + ctime +
                ", creationTime=" + creationTime +
                ", atime=" + atime +
                ", mtime=" + mtime +
                ", owner=" + owner +
                ", group=" + group +
                ", mode=" + mode +
                ", fileType='" + fileType + '\'' +
                ", pnfsId='" + pnfsId + '\'' +
                '}';
    }
}