package org.dcache.webapi;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by sahakya on 12/21/15.
 */
public class FileAttributesObject {

    /**
     * NFSv4 Access control list.
     */
    private String _acl;

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
    private long _creationTime;

    /**
     * file's last access time
     */
    private long _atime;

    /**
     * file's last modification time
     */
    private long _mtime;

    /**
     * file's known checksums
     */
    private Set<String> _checksums;

    /**
     * file's owner's id
     */
    private int _owner;

    /**
     * file's group id
     */
    private int _group;

    /**
     * POSIX.1 file mode
     */
    private int _mode;

    /**
     * file's access latency ( e.g. ONLINE/NEARLINE )
     */
    private String _accessLatency;

    /**
     * file's retention policy ( e.g. CUSTODIAL/REPLICA )
     */
    private String _retentionPolicy;

    /**
     * type of the file ( e.g. REG, DIR, LINK, SPECIAL )
     */
    private String _fileType;

    /**
     * File locations within dCache.
     */
    private Collection<String> _locations;

    /**
     * Key value map of flags associated with the file.
     */
    private Map<String, String> _flags;

    /**
     * The unique PNFS ID of a file.
     */
    private String _pnfsId;

    /**
     * The storage info of a file.
     */
    private String _storageInfo;

    /**
     * The storage class of a file.
     */
    private String _storageClass;

    /**
     * The HSM of a file.
     */
    private String _hsm;

    /**
     * The cache class of a file.
     */
    private String _cacheClass;

    /**
     * The cache class of a file.
     */
    private String _fileName;


    public String get_acl() {
        return _acl;
    }

    public void set_acl(String _acl) {
        this._acl = _acl;
    }

    public long get_size() {
        return _size;
    }

    public void set_size(long _size) {
        this._size = _size;
    }

    public long get_ctime() {
        return _ctime;
    }

    public void set_ctime(long _ctime) {
        this._ctime = _ctime;
    }

    public long get_creationTime() {
        return _creationTime;
    }

    public void set_creationTime(long _creationTime) {
        this._creationTime = _creationTime;
    }

    public long get_atime() {
        return _atime;
    }

    public void set_atime(long _atime) {
        this._atime = _atime;
    }

    public long get_mtime() {
        return _mtime;
    }

    public void set_mtime(long _mtime) {
        this._mtime = _mtime;
    }

    public Set<String> get_checksums() {
        return _checksums;
    }

    public void set_checksums(Set<String> _checksums) {
        this._checksums = _checksums;
    }

    public int get_owner() {
        return _owner;
    }

    public void set_owner(int _owner) {
        this._owner = _owner;
    }

    public int get_group() {
        return _group;
    }

    public void set_group(int _group) {
        this._group = _group;
    }

    public int get_mode() {
        return _mode;
    }

    public void set_mode(int _mode) {
        this._mode = _mode;
    }

    public String get_accessLatency() {
        return _accessLatency;
    }

    public void set_accessLatency(String _accessLatency) {
        this._accessLatency = _accessLatency;
    }

    public String get_retentionPolicy() {
        return _retentionPolicy;
    }

    public void set_retentionPolicy(String _retentionPolicy) {
        this._retentionPolicy = _retentionPolicy;
    }

    public String get_fileType() {
        return _fileType;
    }

    public void set_fileType(String _fileType) {
        this._fileType = _fileType;
    }

    public Collection<String> get_locations() {
        return _locations;
    }

    public void set_locations(Collection<String> _locations) {
        this._locations = _locations;
    }

    public Map<String, String> get_flags() {
        return _flags;
    }

    public void set_flags(Map<String, String> _flags) {
        this._flags = _flags;
    }

    public String get_pnfsId() {
        return _pnfsId;
    }

    public void set_pnfsId(String _pnfsId) {
        this._pnfsId = _pnfsId;
    }

    public String get_storageInfo() {
        return _storageInfo;
    }

    public void set_storageInfo(String _storageInfo) {
        this._storageInfo = _storageInfo;
    }

    public String get_storageClass() {
        return _storageClass;
    }

    public void set_storageClass(String _storageClass) {
        this._storageClass = _storageClass;
    }

    public String get_hsm() {
        return _hsm;
    }

    public void set_hsm(String _hsm) {
        this._hsm = _hsm;
    }

    public String get_cacheClass() {
        return _cacheClass;
    }

    public void set_cacheClass(String _cacheClass) {
        this._cacheClass = _cacheClass;
    }

    public String get_fileName() {
        return _fileName;
    }

    public void set_fileName(String _fileName) {
        this._fileName = _fileName;
    }
}
