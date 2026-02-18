package jp.swell.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;

public class FileDao implements Serializable {

    /** Derializable No. */
    private static final long serialVersionUID = 1L;

    /**
     * データアクセス権限のあるユーザリスト
     */
    private ArrayList<String> authorityUserList = null;

    /**
     * データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList() {
        return authorityUserList;
    }

    /** file_id */
    private String fileId = "";
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    /** user_info_id */
    private String userInfoId = "";
    public String getUserInfoId() { return userInfoId; }
    public void setUserInfoId(String userInfoId) { this.userInfoId = userInfoId; }

    /** file_name */
    private String fileName = "";
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    /** file_path */
    private String filePath = "";
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    /** upload_data */
    private String uploadDate = "";
    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }

    /** first_name */
    private String firstName = "";
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** last_name */
    private String lastName = "";
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** file_key */
    private String fileKey = "";
    public String getFileKey() { return fileKey; }
    public void setFileKey(String fileKey) { this.fileKey = fileKey; }

    /** mime_type */
    private String mimeType = "";
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    /** system_file_name */
    private String systemFileName = "";
    public String getSystemFileName() { return systemFileName; }
    public void setSystemFileName(String systemFileName) { this.systemFileName = systemFileName; }

    /** upload_user_id */
    private String uploadUserId = "";
    public String getUploadUserId() { return uploadUserId; }
    public void setUploadUserId(String uploadUserId) { this.uploadUserId = uploadUserId; }

    /** user_file_id */
    private String userFileId = "";
    public String getUserFileId() { return userFileId; }
    public void setUserFileId(String userFileId) { this.userFileId = userFileId; }

    /** searchFileName */
    private String searchFileName = "";
    public String getSearchFileName() { return searchFileName; }
    public void setSearchFileName(String searchFileName) { this.searchFileName = searchFileName; }

    /** uploaderFirstName */
    private String uploaderFirstName;
    public String getUploaderFirstName() { return uploaderFirstName; }
    public void setUploaderFirstName(String uploaderFirstName) { this.uploaderFirstName = uploaderFirstName; }

    /** uploaderLastName */
    private String uploaderLastName;
    public String getUploaderLastName() { return uploaderLastName; }
    public void setUploaderLastName(String uploaderLastName) { this.uploaderLastName = uploaderLastName; }

    /** expirationDate */
    private String expirationDate;
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    private String[] userIds;
    public void setUserIds(String[] userIds) { this.userIds = userIds; }
    public String[] getUserIds() { return this.userIds; }

    private String fileType;
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    /**
     * @return フルネームを返す
     */
    public String getSendUserName() {
        return (lastName != null ? lastName : "") + " " + (firstName != null ? firstName : "");
    }

    /**
     * @return アップロードユーザーフルネームを返す
     */
    public String getUploadUserName() {
        return (uploaderLastName != null ? uploaderLastName : "") + " " + (uploaderFirstName != null ? uploaderFirstName : "");
    }

    /**
     * files ファイルテーブルを検索し fileテーブルの１行を取得します。.
     */
    public boolean dbSelect(String pFileId) throws AtareSysException {
        String sql = "SELECT * FROM files WHERE file_id = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, pFileId);
            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (!rs.next()) return false;
                HashMap<String, String> map = new HashMap<>();
                // カラム名（小文字）で登録
                map.put("files___file_id", rs.getString("file_id"));
                map.put("files___user_info_id", rs.getString("user_info_id"));
                map.put("files___file_name", rs.getString("file_name"));
                map.put("files___file_path", rs.getString("file_path"));
                map.put("files___upload_date", rs.getString("upload_date"));
                map.put("files___file_key", rs.getString("file_key"));
                map.put("files___mime_type", rs.getString("mime_type"));
                map.put("files___system_file_name", rs.getString("system_file_name"));
                map.put("files___upload_user_id", rs.getString("upload_user_id"));
                map.put("files___expiration_date", rs.getString("expiration_date"));

                setFileDaoForJoin(map, this);
                return true;
            } catch (SQLException e) {
                throw new AtareSysException("DB Error: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new AtareSysException("Connection Error: " + e.getMessage(), e);
        }
    }

    /**
     * files ファイルテーブルを検索し fileテーブルの１行を取得します。.
     */
    public boolean dbSelect(String pFileId, String fileName) throws AtareSysException {
        String sql = "select * from files where file_id = " + DbS.chara(pFileId) + " and file_name = " + DbS.chara(fileName);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size()) return false;
        HashMap<String, String> map = rs.get(0);
        setFileDao(map, this);
        return true;
    }

    /**
     * FileDao にfiles ファイルテーブルから読み込んだデータを設定する。(単体用)
     */
    public void setFileDao(HashMap<String, String> map, FileDao dao) throws AtareSysException {
        dao.setFileId(DbI.chara(map.get("file_id")));
        dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
        dao.setFileName(DbI.chara(map.get("file_name")));
        dao.setFilePath(DbI.chara(map.get("file_path")));
        dao.setUploadDate(DbI.chara(map.get("upload_date")));
        dao.setFileKey(DbI.chara(map.get("file_key")));
        dao.setMimeType(DbI.chara(map.get("mime_type")));
        dao.setSystemFileName(DbI.chara(map.get("system_file_name")));
        dao.setUploadUserId(DbI.chara(map.get("upload_user_id")));
        dao.setExpirationDate(DbI.chara(map.get("expiration_date")));
    }

    /**
     * FileDao にfiles ファイルテーブルから読み込んだデータを設定する。(JOIN用)
     */
    public void setFileDaoForJoin(HashMap<String, String> map, FileDao dao) throws AtareSysException {
        // 下にある getVal メソッドを使って、安全に値を取り出します
        dao.setFileId(DbI.chara(getVal(map, "files___file_id", "file_id")));
        dao.setUserInfoId(DbI.chara(getVal(map, "files___user_info_id", "user_info_id")));
        dao.setFileName(DbI.chara(getVal(map, "files___file_name", "file_name")));
        dao.setFilePath(DbI.chara(getVal(map, "files___file_path", "file_path")));
        dao.setUploadDate(DbI.chara(getVal(map, "files___upload_date", "upload_date")));
        dao.setFileKey(DbI.chara(getVal(map, "files___file_key", "file_key")));
        dao.setMimeType(DbI.chara(getVal(map, "files___mime_type", "mime_type")));
        dao.setSystemFileName(DbI.chara(getVal(map, "files___system_file_name", "system_file_name")));
        dao.setUploadUserId(DbI.chara(getVal(map, "files___upload_user_id", "upload_user_id")));
        dao.setExpirationDate(DbI.chara(getVal(map, "files___expiration_date", "expiration_date")));
        
        // ユーザー名
        dao.setUploaderFirstName(getVal(map, "uploader_first_name", "first_name"));
        dao.setUploaderLastName(getVal(map, "uploader_last_name", "last_name"));
        dao.setFirstName(getVal(map, "user_first_name", "first_name"));
        dao.setLastName(getVal(map, "user_last_name", "last_name"));
    }

    private String getVal(HashMap<String, String> map, String keyAlias, String keyRaw) {
        String val = map.get(keyAlias);
        if (val != null) return val;
        
        val = map.get(keyAlias.toUpperCase());
        if (val != null) return val;
        
        val = map.get(keyRaw);
        if (val != null) return val;
        
        val = map.get(keyRaw.toUpperCase());
        if (val != null) return val;
        
        return ""; // なければ空文字
    }

    /**
     * 新しいトークンを生成するメソッド。
     */
    public static String FileKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * files ファイルテーブルにデータを挿入する
     */
    public boolean dbFileInsert(String pfileId, String puserInfoId, String pfullPath, String pfileName,
            String pmimeType, String psystemFileName, String puploadUserId, String skey, String pexpirationDate)
            throws AtareSysException {
        setUserInfoId(puserInfoId);
        setFilePath(pfullPath);
        setFileName(pfileName);
        setMimeType(pmimeType);
        setSystemFileName(psystemFileName);
        setUploadUserId(puploadUserId);
        setFileKey(skey);
        setExpirationDate(pexpirationDate);

        String sql = "insert into files ("
                + " file_id, user_info_id, file_name, file_path, upload_date, "
                + " file_key, mime_type, system_file_name, upload_user_id, expiration_date "
                + " ) values ( "
                + DbO.chara(pfileId) + "," + DbO.chara(puserInfoId) + "," + DbO.chara(getFileName()) + "," 
                + DbO.chara(pfullPath) + ", NOW(), " + DbO.chara(skey) + "," + DbO.chara(pmimeType) + "," 
                + DbO.chara(psystemFileName) + "," + DbO.chara(puploadUserId) + "," + DbO.chara(pexpirationDate)
                + " )";
        
        int ret = DbBase.dbExec(sql);
        if (ret != 1) throw new AtareSysException("dbInsert number or record exception.");
        return true;
    }

    public String getLastInsertedFileId() throws AtareSysException {
        String sql = "SELECT LAST_INSERT_ID()";
        int fileId = DbBase.dbExec(sql);
        return String.valueOf(fileId);
    }

    public boolean dbUpdate(String pFileId) throws AtareSysException {
        String sql = "update files set file_name = " + DbO.chara(getFileName()) + " where file_id = " + DbS.chara(pFileId);
        int ret = DbBase.dbExec(sql);
        if (ret != 1) throw new AtareSysException("dbupdate number or record exception");
        return true;
    }

    public boolean dbDelete(String pFileId) throws AtareSysException {
        String sqlDeleteUserFiles = "DELETE FROM user_files WHERE file_id = " + DbS.chara(pFileId);
        DbBase.dbExec(sqlDeleteUserFiles);
        String sqlDeleteFiles = "DELETE FROM files WHERE file_id = " + DbS.chara(pFileId);
        int retFiles = DbBase.dbExec(sqlDeleteFiles);
        if (retFiles != 1) throw new AtareSysException("dbDelete number or record exception");
        return true;
    }

    public ArrayList<FileDao> getAllFiles() throws AtareSysException {
        String sql = "SELECT file_id, file_name FROM files;";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<FileDao> files = new ArrayList<>();
        for (HashMap<String, String> map : rs) {
            FileDao dao = new FileDao();
            dao.setFileId(map.get("file_id"));
            dao.setFileName(map.get("file_name"));
            files.add(dao);
        }
        return files;
    }

    public static ArrayList<FileDao> dbSelectList(FileDao myclass, LinkedHashMap<String, String> sortKey,
            DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<FileDao> resultList = new ArrayList<>();

        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);

        int offset = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        int limit = daoPageInfo.getLineCount();

        // SQL (LEFT JOIN)
        String sql = "SELECT "
                + "files.file_id AS files___file_id, "
                + "files.user_info_id AS files___user_info_id, "
                + "files.file_name AS files___file_name, "
                + "files.file_path AS files___file_path, "
                + "files.upload_date AS files___upload_date, "
                + "files.file_key AS files___file_key, "
                + "files.mime_type AS files___mime_type, "
                + "files.system_file_name AS files___system_file_name, "
                + "files.upload_user_id AS files___upload_user_id, "
                + "files.expiration_date AS files___expiration_date, "
                + "uploader.first_name AS uploader_first_name, "
                + "uploader.last_name AS uploader_last_name, "
                + "user_info.first_name AS user_first_name, "
                + "user_info.last_name AS user_last_name "
                + "FROM files "
                + "LEFT JOIN user_info ON files.user_info_id = user_info.user_info_id "
                + "LEFT JOIN user_info AS uploader ON files.upload_user_id = uploader.user_info_id "
                + where + order
                + " LIMIT " + limit + " OFFSET " + offset;

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        for (HashMap<String, String> map : rs) {
            FileDao dao = new FileDao();
            dao.setFileDaoForJoin(map, dao);
            resultList.add(dao);
        }

        return resultList;
    }

    private String dbWhere() throws AtareSysException {
        StringBuffer where = new StringBuffer(1024);
        if (userIds != null && userIds.length > 0) {
            where.append(where.length() > 0 ? " OR " : "");
            where.append("files.user_info_id IN (");
            for (int i = 0; i < userIds.length; i++) {
                where.append(DbS.chara(userIds[i]));
                if (i < userIds.length - 1) where.append(", ");
            }
            where.append(") ");
        }
        if (getFileId().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("files.file_id = " + DbS.chara(getFileId()));
        }
        if (getUserInfoId().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("files.user_info_id = " + DbS.chara(getUserInfoId()));
        }
        if (getUploadUserId().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("files.upload_user_id = " + DbS.chara(getUploadUserId()));
        }
        if (getSearchFileName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("files.file_name LIKE " + DbS.chara("%" + getSearchFileName() + "%"));
        }
        if (where.length() > 0) {
            return "where " + where.toString();
        }
        return "";
    }

    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    public FileDao() {
        fieldsArray.put("file_id", "files.file_id");
        fieldsArray.put("user_info_id", "files.user_info_id");
        fieldsArray.put("file_name", "files.file_name");
        fieldsArray.put("file_path", "files.file_path");
        fieldsArray.put("upload_date", "files.upload_date");
        fieldsArray.put("file_key", "files.file_key");
        fieldsArray.put("mime_type", "files.mime_type");
        fieldsArray.put("system_file_name", "files.system_file_name");
        fieldsArray.put("upload_user_id", "files.upload_user_id");
        fieldsArray.put("expiration_date", "files.expiration_date");
    }

    private String dbOrder(LinkedHashMap<String, String> sortKey) {
        String str = "";
        if (sortKey == null) return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = i.next();
            if (null == fieldsArray.get(key)) continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++) {
                if (j != 0) str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }
}