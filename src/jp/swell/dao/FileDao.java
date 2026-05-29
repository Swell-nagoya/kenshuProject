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
     *  データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList() {
        return authorityUserList;
    }

    /**
     * file_id
     */
    private String fileId = "";

    /**
     * 
     * @return fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 
     * @param fileId セットする fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * user_info_id
     */
    private String userInfoId = "";

    /**
     * 
     * @return userInfoId
     */
    public String getUserInfoId() {
        return userInfoId;
    }

    /**
     * 
     * @param userInfoId セットする userInfoId
     */
    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    /**
     * file_name
     */
    private String fileName = "";

    /**
     * 
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 
     * @param fileName セットする fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * fail_path
     */
    private String filePath = "";

    /**
     * 
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 
     * @param filePath セットする filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * upload_data 登録日時
     */
    private String uploadDate = "";

    /**
     * 
     * @return uploadDate
     */
    public String getUploadDate() {
        return uploadDate;
    }

    /**
     * 
     * @param uploadDate セットする uploadDate
     */
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * first_name 名字
     */
    private String firstName = "";

    /**
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName セットする firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * last_name 名前
     */
    private String lastName = "";

    /**
     * 
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName セットする lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * file_name 名前
     */
    private String fileKey = "";

    /**
     * 
     * @return fileName
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * 
     * @param fileName セットする fileName
     */
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    /**
     * mime_type 名前
     */
    private String mimeType = "";

    /**
     * 
     * @return mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * 
     * @param mimeType セットする mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * system_file_name 名前
     */
    private String systemFileName = "";

    /**
     * 
     * @return SystemFileName
     */
    public String getSystemFileName() {
        return systemFileName;
    }

    /**
     * 
     * @param SystemFileName セットする SystemFileName
     */
    public void setSystemFileName(String systemFileName) {
        this.systemFileName = systemFileName;
    }

    /**
     * upload_user_id 名前
     */
    private String uploadUserId = "";

    /**
     * 
     * @return UploadUserId
     */
    public String getUploadUserId() {
        return uploadUserId;
    }

    /**
     * 
     * @param UploadUserId セットする UploadUserId
     */
    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    /**
     * user_file_id 
     */
    private String userFileId = "";

    /**
     * 
     * @return userFileId
     */
    public String getUserFileId() {
        return userFileId;
    }

    /**
     * 
     * @param userFileId セットする userFileId
     */
    public void setUserFileId(String userFileId) {
        this.userFileId = userFileId;
    }

    /**
     * 
     * @return フルネームを返す
     */
    public String getSendUserName() {
        return lastName + " " + firstName;
    }

    /**
     * searchFileName 検索用氏名
     */
    private String searchFileName = "";

    /**
     * searchFileName 検索用氏名を取得します。
     *
     * @return searchFileName 検索用氏名
     */
    public String getSearchFileName() {
        return searchFileName;
    }

    /**
     * searchFileName 検索用氏名を設定します。
     *
     * @param searchFileName 検索用氏名
     */
    public void setSearchFileName(String searchFileName) {
        this.searchFileName = searchFileName;
    }

    /**
     * uploaderFirstName アップロードユーザー名前
     */
    private String uploaderFirstName;

    /**
     * uploaderFirstName アップロードユーザー名前を取得します。
     *
     * @return uploaderFirstName アップロードユーザー名前
     */
    public String getUploaderFirstName() {
        return uploaderFirstName;
    }

    /**
     * uploaderFirstName アップロードユーザー名前を設定します。
     *
     * @param uploaderFirstName アップロードユーザー名前
     */
    public void setUploaderFirstName(String uploaderFirstName) {
        this.uploaderFirstName = uploaderFirstName;
    }

    /**
     * uploaderLastName アップロードユーザー名字
     */
    private String uploaderLastName;

    /**
     * uploaderLastName アップロードユーザー名字を取得します。
     *
     * @return uploaderLastName アップロードユーザー名字
     */
    public String getUploaderLastName() {
        return uploaderLastName;
    }

    /**
     * uploaderLastName アップロードユーザー名字を設定します。
     *
     * @param uploaderLastName アップロードユーザー名字
     */
    public void setUploaderLastName(String uploaderLastName) {
        this.uploaderLastName = uploaderLastName;
    }

    /**
     * uploaderLastName アップロードユーザー名字
     */
    private String expirationDate;

    /**
     * uploaderLastName アップロードユーザー名字を取得します。
     *
     * @return uploaderLastName アップロードユーザー名字
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * uploaderLastName アップロードユーザー名字を設定します。
     *
     * @param uploaderLastName アップロードユーザー名字
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * 
     * @return アップロードユーザーフルネームを返す
     */
    public String getUploadUserName() {
        return uploaderLastName + " " + uploaderFirstName;
    }

    private String[] userIds;

    // userIdsをセットするメソッド
    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    // userIdsを取得するメソッド
    public String[] getUserIds() {
        return this.userIds;
    }

    private String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * files ファイルテーブルを検索し fileテーブルの１行を取得します。.
     *
     * @param pfileId   部屋ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pFileId) throws AtareSysException {
        String sql = "SELECT "
                + "files.file_id as files___file_id, "
                + "files.user_info_id as files___user_info_id, "
                + "files.file_name as files___file_name, "
                + "files.file_path as files___file_path, "
                + "files.upload_date as files___upload_date, "
                + "files.file_key as files___file_key, "
                + "files.mime_type as files___mime_type, "
                + "files.system_file_name as files___system_file_name, "
                + "files.upload_user_id as files___upload_user_id, "
                + "files.expiration_date as files___expiration_date "
                + "FROM files "
                + "WHERE file_id = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, pFileId);

            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("files___file_id", rs.getString("files___file_id"));
                map.put("files___user_info_id", rs.getString("files___user_info_id"));
                map.put("files___file_name", rs.getString("files___file_name"));
                map.put("files___file_path", rs.getString("files___file_path"));
                map.put("files___upload_date", rs.getString("files___upload_date"));
                map.put("files___file_key", rs.getString("files___file_key"));
                map.put("files___mime_type", rs.getString("files___mime_type"));
                map.put("files___system_file_name", rs.getString("files___system_file_name"));
                map.put("files___upload_user_id", rs.getString("files___upload_user_id"));
                map.put("files___expiration_date", rs.getString("files___expiration_date"));

                setFileDaoForJoin(map, this);
                return true;
            } catch (SQLException e) {
                throw new AtareSysException("データベースクエリの実行中にエラーが発生しました: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new AtareSysException("データベース接続中にエラーが発生しました: " + e.getMessage(), e);
        }
    }

    /**
     * files ファイルテーブルを検索し fileテーブルの１行を取得します。.
     *
     * @param pFileId  部屋ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pFileId, String fileName) throws AtareSysException {
        String sql = "select "
                + "files.file_id as files___file_id, "
                + "files.user_info_id as files___user_info_id, "
                + "files.file_name as files___file_name, "
                + "files.file_path as files___file_path, "
                + "files.upload_date as files___upload_date, "
                + "files.file_key as files___file_file_key, "
                + "files.mime_type as files___mime_type, "
                + "files.system_file_name as files___system_file_name, "
                + "files.upload_user_id as files___upload_user_id, "
                + "files.expiration_date as files___expiration_date "
                + " from files ";
        sql += ""
                + " where file_id = " + DbS.chara(pFileId)
                + " and file_name = " + DbS.chara(fileName);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return false;
        HashMap<String, String> map = rs.get(0);
        setFileDaoForJoin(map, this);
        return true;
    }

    /**
     * FileDao にfiles ファイルテーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  FileDaoこのテーブルのインスタンス
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
     *  FileDao にfiles ファイルテーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  FileDaoこのテーブルのインスタンス
     */
    public void setFileDaoForJoin(HashMap<String, String> map, FileDao dao) throws AtareSysException {
        dao.setFileId(DbI.chara(map.get("files___file_id") != null ? map.get("files___file_id") : ""));
        dao.setUserInfoId(DbI.chara(map.get("files___user_info_id") != null ? map.get("files___user_info_id") : ""));
        dao.setFileName(DbI.chara(map.get("files___file_name") != null ? map.get("files___file_name") : ""));
        dao.setFilePath(DbI.chara(map.get("files___file_path") != null ? map.get("files___file_path") : ""));
        dao.setUploadDate(DbI.chara(map.get("files___upload_date") != null ? map.get("files___upload_date") : ""));
        dao.setFileKey(DbI.chara(map.get("files___file_key") != null ? map.get("files___file_key") : ""));
        dao.setMimeType(DbI.chara(map.get("files___mime_type") != null ? map.get("files___mime_type") : ""));
        dao.setSystemFileName(
                DbI.chara(map.get("files___system_file_name") != null ? map.get("files___system_file_name") : ""));
        dao.setUploadUserId(
                DbI.chara(map.get("files___upload_user_id") != null ? map.get("files___upload_user_id") : ""));
        dao.setExpirationDate(
                DbI.chara(map.get("files___expiration_date") != null ? map.get("files___expiration_date") : ""));
        dao.setUploaderFirstName(map.get("uploader_first_name"));
        dao.setUploaderLastName(map.get("uploader_last_name"));
    }

    /**
     * 新しいトークンを生成するメソッド。
     * @return 生成したトークン
     */
    public static String FileKey() {
        return UUID.randomUUID().toString(); // UUID でトークンを生成
    }

    /**
     * files ファイルテーブルにデータを挿入する
     * @param puserInfoId 
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
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
                + " file_id"
                + ",user_info_id"
                + ",file_name"
                + ",file_path"
                + ",upload_date"
                + ",file_key"
                + ",mime_type"
                + ",system_file_name"
                + ",upload_user_id"
                + ",expiration_date"
                + " ) values ( "
                + DbO.chara(pfileId)
                + "," + DbO.chara(puserInfoId)
                + "," + DbO.chara(getFileName())
                + "," + DbO.chara(pfullPath)
                + "," + "NOW()"
                + "," + DbO.chara(skey)
                + "," + DbO.chara(pmimeType)
                + "," + DbO.chara(psystemFileName)
                + "," + DbO.chara(puploadUserId)
                + "," + DbO.chara(pexpirationDate)
                + " )";
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbInsert number or record exception.");
        return true;
    }

    /**
     * DB登録直後に登録したfileIdを返す
     * @return fileId
     * @throws AtareSysException
     */
    public String getLastInsertedFileId() throws AtareSysException {
        String sql = "SELECT LAST_INSERT_ID()";
        int fileId = DbBase.dbExec(sql); // DB操作の結果を取得
        return String.valueOf(fileId); // 取得したIDを返す
    }

    /**
     * room ルームテーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdate(String pFileId) throws AtareSysException {
        String sql = "update files set "
                + " file_name = " + DbO.chara(getFileName())
                + " where file_id = " + DbS.chara(pFileId)
                + "";
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbupdate number or record exception");
        return true;
    }

    /**
     * room ルームテーブルからデータを削除する
     *
     * @param pRoomId   ユーザ情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラーs
     */
    public boolean dbDelete(String pFileId) throws AtareSysException {
        // user_filesテーブルから関連するレコードを削除
        String sqlDeleteUserFiles = "DELETE FROM user_files WHERE file_id = " + DbS.chara(pFileId);
        DbBase.dbExec(sqlDeleteUserFiles);

        // filesテーブルからレコードを削除
        String sqlDeleteFiles = "DELETE FROM files WHERE file_id = " + DbS.chara(pFileId);
        int retFiles = DbBase.dbExec(sqlDeleteFiles);

        if (retFiles != 1)
            throw new AtareSysException("dbDelete number or record exception");
        return true;
    }

    /**
     * データベースからルーム名を取得するメソッド
     * @return UserMenuに返す
     * @throws AtareSysException
     */
    public ArrayList<FileDao> getAllFiles() throws AtareSysException {
        String sql = "SELECT file_id, file_name FROM files;";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<FileDao> files = new ArrayList<>();
        for (HashMap<String, String> map : rs) {
            FileDao dao = new FileDao();
            // ルームDAOのインスタンスにデータを設定
            dao.setUserInfoId(map.get("file_id"));
            dao.setUserInfoId(map.get("user_info_id"));
            dao.setFileName(map.get("file_name"));
            dao.setFilePath(map.get("file_path"));
            dao.setUploadDate(map.get("upload_date"));
            dao.setFileKey(map.get("file_key"));
            dao.setMimeType(map.get("mime_type"));
            dao.setSystemFileName(map.get("system_file_name"));
            dao.setUploadUserId(map.get("upload_user_id"));
            dao.setExpirationDate(map.get("expiration_date"));
            files.add(dao);
        }

        return files; // 取得したルームリストを返す
    }

    public static ArrayList<FileDao> dbSelectList(FileDao myclass, LinkedHashMap<String, String> sortKey,
            DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<FileDao> resultList = new ArrayList<>();

        // WHERE句
        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);

        int offset = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        int limit = daoPageInfo.getLineCount();

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
                + "JOIN user_info ON files.user_info_id = user_info.user_info_id "
                + "JOIN user_info AS uploader ON files.upload_user_id = uploader.user_info_id "
                + where + order
                + " LIMIT " + limit + " OFFSET " + offset;

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        for (HashMap<String, String> map : rs) {
            FileDao dao = new FileDao();
            dao.setFileDaoForJoin(map, dao);
            dao.setFirstName(map.get("user_first_name"));
            dao.setLastName(map.get("user_last_name"));
            resultList.add(dao);
        }

        return resultList;
    }

    

    /**
     * files ファイル情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException {
        StringBuffer where = new StringBuffer(1024);
        if (userIds != null && userIds.length > 0) {
            where.append(where.length() > 0 ? " OR " : "");
            where.append("files.user_info_id IN (");

            for (int i = 0; i < userIds.length; i++) {
                where.append(DbS.chara(userIds[i]));
                if (i < userIds.length - 1) {
                    where.append(", ");
                }
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

    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    /**
     * コンストラクタ。
     */
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

    /**
     * files ファイル情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbOrder(LinkedHashMap<String, String> sortKey) {
        String str = "";
        if (sortKey == null)
            return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = i.next();
            if (null == fieldsArray.get(key))
                continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++) {
                if (j != 0)
                    str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }
}
