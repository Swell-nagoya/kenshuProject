package jp.swell.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;
import jp.patasys.common.db.GetNumber;

public class ReserveFileDao implements Serializable {

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
     * reserve_file_id 予約ファイルID
     */
    private String reserveFileId = "";
    /**
     * 予約ファイルIDを取得する。.
     * @return  reserve_file_id 予約ファイルID
     */
    public String getReserveFileId()
    {
      return reserveFileId;
    }
    /**
     * 予約ファイルIDをセットする。
     * @param reserve_file_id 予約ファイルID
     */
    public void setReserveFileId(String reserveFileId)
    {
      this.reserveFileId = reserveFileId;
    }
    /**
     * reserveId 予約情報ID
     */
    private String reserveId = "";
    /**
     * 予約情報IDを取得する。
     * @return reserveId ユーザ情報ID
     */
    public String getReserveId()
    {
      return reserveId;
    }
    /**
     * 予約情報IDをセットする
     * @param reserveId ユーザ情報ID
     */
    public void setReserveId(String reserveId)
    {
      this.reserveId = reserveId;
    }
    /**
     * fileId ファイルID
     */
    private String fileId = "";
    /**
     * ファイルIDを取得する
     * @return fileId
     */
    public String getFileId() 
    {
        return fileId;
    }
    /**
     * ファイルIDをセットする
     * @param fileId 
     */
    public void setFileId(String fileId) 
    {
        this.fileId = fileId;
    }
    /**
     * fileName ファイル名
     */
    private String fileName;
    /**
     * ファイルIDを取得する
     * @return fileId
     */
    public String getFileName()
    {
      return fileName;
    }
    /**
     * ファイルIDをセットする
     * @param fileId 
     */
    public void setFileName(String fileName)
    {
      this.fileName = fileName;
    }
    
    private FileDao fileDaos = new FileDao();
    /**
     * reserve_filesテーブルのfile_idをセット・取得
     * 
     * @return file_id
     */
    public FileDao getFileDaos() {
      return fileDaos;
    }
    public void setFileDaos(FileDao fileDaos) {
      this.fileDaos = fileDaos;
    }
    
    /**
     * reserve_fileテーブルを検索し fileテーブルの１行を取得します。.
     *
     * @param pReserveFileId   予約ファイルID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pReserveId) throws AtareSysException {
        String sql = "SELECT "
                + "reserve_file.reserve_file_id as reserve_file___reserve_file_id, "
                + "reserve_file.reserve_id as reserve_file___reserve_id, "
                + "reserve_file.file_id as reserve_file___file_id "
                + "FROM reserve_file "
                + "WHERE reserve_id = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, pReserveId);

            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("reserve_file___reserve_file_id", rs.getString("reserve_file___reserve_file_id"));
                map.put("reserve_file___reserve_id", rs.getString("reserve_file___reserve_id"));
                map.put("reserve_file___file_id", rs.getString("reserve_file___file_id"));

                setReserveFileDaoForJoin(map, this);
                return true;
            } catch (SQLException e) {
                throw new AtareSysException("データベースクエリの実行中にエラーが発生しました: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new AtareSysException("データベース接続中にエラーが発生しました: " + e.getMessage(), e);
        }
    }
    /**
     * reserve_fileテーブルを検索し fileテーブルの１行を取得します。.
     *
     * @param pRoomId  部屋ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pReserveFileId,String reserveId) throws AtareSysException
    {
        String sql =  "select "
                + "reserve_file.reserve_file_id as reserve_file___reserve_file_id, "
                + "reserve_file.reserve_id as reserve_file___reserve_id, "
                + "reserve_file.file_id as reserve_file___file_id, "
        + " from reserve_file ";
        sql += ""
        + " where reserve_file_id = " + DbS.chara(pReserveFileId)
        + " and reserve_id = " + DbS.chara(reserveId);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setReserveFileDaoForJoin(map,this);
        return true;
    }
    

    /**
     * ReserveFileDao に reserve_fileテーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  ReserveFileDaoこのテーブルのインスタンス
     */
    public void setReserveFileDao(HashMap<String, String> map,ReserveFileDao dao)  throws AtareSysException
    {
        dao.setReserveFileId(DbI.chara(map.get("reserve_file_id")));
        dao.setReserveId(DbI.chara(map.get("reserve_id")));
        dao.setFileId(DbI.chara(map.get("file_id")));
    }

    /**
     *  ReserveFileDao にreserve_file 部屋テーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  ReserveFileDaoこのテーブルのインスタンス
     */
    public void setReserveFileDaoForJoin(HashMap<String, String> map,ReserveFileDao dao)  throws AtareSysException
    {
        dao.setReserveFileId(DbI.chara(map.get("reserve_file___reserve_file_id") != null ? map.get("reserve_file___reserve_file_id") : ""));
        dao.setReserveId(DbI.chara(map.get("reserve_file___reserve_id") != null ? map.get("reserve_file___reserve_id") : ""));
        dao.setFileId(DbI.chara(map.get("reserve_file___file_id") != null ? map.get("reserve_file___file_id") : ""));
    }
    /**
     * reserve_file テーブルにデータを挿入する
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbReserveFileInsert() throws AtareSysException
    {
        setReserveFileId(GetNumber.getNumberChar("reserve_files"));
        String sql="insert into reserve_file ("
        + " reserve_file_id"
        + ",reserve_id"
        + ",file_id"
        + " ) values ( "
        + DbO.chara(getReserveFileId())
        + "," + (getReserveId().isEmpty() ? "null" : DbO.chara(getReserveId()))
        + "," + DbO.chara(getFileId())
        + " )";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbInsert number or record exception.") ;
        return true;
    }

    
    /**
     * reserve_file ルームテーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdate(String pReserveFileId) throws AtareSysException
    {
        String sql = "update reserve_file set "
        + " reserve_id = " + DbO.chara(getReserveId())
        + " where reserve_file_id = " + DbS.chara(pReserveFileId)
        + "";
        int ret =DbBase.dbExec(sql);
        if (ret != 1) throw new AtareSysException("dbupdate number or record exception");
        return true;
    }

    /**
     * reserve_file ルームテーブルからデータを削除する
     *
     * @param pReserveFileId   予約ファイル情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラーs
     */
    public boolean dbFileDelete(String pFileId) throws AtareSysException
    {
        String sql = "delete from reserve_file where "
        + "file_id = " + DbS.chara(pFileId)
        + "";
        int ret =DbBase.dbExec(sql);
        if (ret != 1) throw new AtareSysException("dbDelete number or record exception");
        return true;
    }
    /**
     * データベースからルーム名を取得するメソッド
     * @return UserMenuに返す
     * @throws AtareSysException
     */
    public ArrayList<ReserveFileDao> getAllReserveFiles() throws AtareSysException {
      String sql = "SELECT "
        + "reserve_file_id, "
        + "reserve_id, "
        + "reserve_file.file_id, "
        + "file_name "
        + "FROM reserve_file "
        + "JOIN files ON reserve_file.file_id = files.file_id;";
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      ArrayList<ReserveFileDao> reservesFiles = new ArrayList<>();
      for (HashMap<String, String> map : rs) {
          ReserveFileDao reservesFile = new ReserveFileDao();
          // ReserveFileDaoのインスタンスにデータを設定
          reservesFile.setReserveFileId(map.get("reserve_file_id"));
          reservesFile.setReserveId(map.get("reserve_id"));
          reservesFile.setFileId(map.get("file_id"));
          reservesFile.setFileName(map.get("file_name"));
          reservesFiles.add(reservesFile);
      }

      return reservesFiles; // 取得したルームリストを返す
    }
    public static ArrayList<ReserveFileDao> dbSelectList(ReserveFileDao myclass, LinkedHashMap<String, String> sortKey, DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<ReserveFileDao> array = new ArrayList<>();

        // レコードの総件数を求める
        String sql = "SELECT COUNT(*) AS count FROM reserve_file " + myclass.dbWhere();
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if (len == 0) return array;

        // ページネーション設定
        if (daoPageInfo.getLineCount() == -1) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double) len / (double) (daoPageInfo.getLineCount())));
        if (daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if (daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());

        int start = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql = "SELECT "
              + "reserve_file_id AS reserve_file___reserve_file_id, "
              + "reserve_id AS reserve_file___reserve_id, "
              + "file_id AS reserve_file___file_id, "
              + "FROM reserve_file " + myclass.dbWhere() + " " + myclass.dbOrder(sortKey)
              + " LIMIT " + daoPageInfo.getLineCount() + " OFFSET " + start + ";";

        rs = DbBase.dbSelect(sql);
        for (HashMap<String, String> resultMap : rs) {
            ReserveFileDao dao = new ReserveFileDao();
            dao.setReserveFileDaoForJoin(resultMap, dao);
            array.add(dao);
        }
        return array;
    }


    /**
     * user_info ユーザ情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException
    {
        StringBuffer where = new StringBuffer(1024);

        if(getReserveFileId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("reserve_file.reserve_file_id = " + DbS.chara(getReserveFileId()));
        }

        if(getReserveId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("reserve_file.reserve_id = " + DbS.chara(getReserveId()));
        }
        
        if(getFileId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("reserve_file.file_id = " + DbS.chara(getFileId()));
        }

        if(where.length()>0)
        {
            return "where " + where.toString();
        }
        return "";
    }

    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String,String> fieldsArray = new HashMap<String,String>();

    /**
     * コンストラクタ。
     */
    public ReserveFileDao()
    {
        fieldsArray.put("reserve_file_id","reserve_file.reserve_file_id");
        fieldsArray.put("reserve_id","reserve_file.reserve_id");
        fieldsArray.put("file_id","reserve_file.file_id");
    }
    
    /**
     * user_info ユーザ情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbOrder(LinkedHashMap<String, String> sortKey)
    {
        String str = "";
        if (sortKey == null) return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();)
        {
            String key = i.next();
            if (null == fieldsArray.get(key)) continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++)
            {
                if (j != 0) str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }
    /**
     * ファイル情報をデータベースに登録する。
     * @param reserveId 予約ID
     * @param fileName ファイル名
     * @param filePath ファイルパス
     * @return 成功した場合はtrue、それ以外はfalse
     * @throws AtareSysException エラー
     */
    public boolean dbFileInsert(String reserveFileId, String pReserveId, String fileId) throws AtareSysException {
        String sql = "INSERT INTO reserve_file ("
          + " reserve_file_id,"
          + " reserve_id,"
          + " file_id"
          + ") values ("
          + DbO.chara(reserveFileId)
          + "," + DbO.chara(pReserveId)
          + "," + DbO.chara(fileId)
          + " )";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbInsert number or record exception.") ;
        return true;
    }

    /**
     * ファイル情報をデータベースから削除する。
     * @param fileName ファイル名
     * @return 成功した場合はtrue、それ以外はfalse
     * @throws AtareSysException エラー
     */
    public boolean deleteFile(String fileId) throws AtareSysException {
        String sql = "DELETE FROM reserve_file WHERE file_Id = ?";
        try (Connection conn = (Connection) DbBase.getDbConnection();
             PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql)) {
            stmt.setString(1, fileId);
            int ret = stmt.executeUpdate();
            if (ret != 1) throw new AtareSysException("Delete operation affected an unexpected number of records.");
        } catch (SQLException e) {
            throw new AtareSysException("Database error during delete operation: " + e.getMessage(), e);
        }
        return true;
    }
    public ArrayList<ReserveFileDao> getFilesByReserveId(String reserveId) throws SQLException, AtareSysException {
        String sql = "SELECT * FROM reserve_files WHERE reserve_id = ?";
        ArrayList<ReserveFileDao> files = new ArrayList<>();
        try (Connection conn = (Connection) DbBase.getDbConnection();
             PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql)) {
            stmt.setString(1, reserveId);
            ResultSet rs = (ResultSet) stmt.executeQuery();
            while (rs.next()) {
                files.add(mapResultSetToReserveFile(rs));
            }
        }
        return files;
    }

    public ReserveFileDao mapResultSetToReserveFile(ResultSet rs) throws SQLException {
        ReserveFileDao reserveFileDao = new ReserveFileDao();
        reserveFileDao.setReserveFileId(rs.getString("file_id"));
        reserveFileDao.setReserveId(rs.getString("user_info_id"));
        reserveFileDao.setFileId(rs.getString("file_id"));
        return reserveFileDao;
    }

}

