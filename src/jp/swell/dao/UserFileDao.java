package jp.swell.dao;

import java.io.Serializable;
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

public class UserFileDao implements Serializable {
    
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
     * user_file_id ユーザーファイルID
     */
    private String userFilesId = "";
    /**
     * user_info_id ユーザー情報ID
     */
    private String userInfoId = "";
    /**
     * file_id　ファイルID
     */
    private String fileId = "";
    /**
     * ファイル紐づけIDを取得する
     * @return user_file_id
     */
    public String getUserFilesId() {
        return userFilesId;
    }
    /**
     * ファイル紐づけIDをセットする
     * @param user_file_id セットする
     */
    public void setUserFilesId(String userFilesId) {
        this.userFilesId = userFilesId;
    }
    /**
     * ユーザー情報IDを取得する
     * @return user_info_Id
     */
    public String getUserInfoId() {
        return userInfoId;
    }
    /**
     * ユーザー情報IDをセットする
     * @param user_info_id セットする
     */
    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }
    /**
     * ファイル情報IDを取得する
     * @return fileId
     */
    public String getFileId() {
        return fileId;
    }
    /**
     * ファイル情報IDをセットする
     * @param fileId セットする fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    
    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();
    /**
     * コンストラクタ。
     */
    public UserFileDao() {
        fieldsArray.put("user_files_id", "user_files.user_files_id");
        fieldsArray.put("user_info_id", "user_files.user_info_id");
        fieldsArray.put("file_id", "user_files.file_id");
        
    }
    
    /**
     * user_reserve ユーザ情報テーブルを検索しuser_reserve ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserReserveId   ユーザ予約ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pUserFilesId) throws AtareSysException
    {
        String sql =  "select "
                + " user_files.user_files_id as user_files___user_files_id"
                + ",user_files.user_info_id as user_files___user_info_id"
                + ",user_files.file_id as user_files___file_id"
        + " from user_reserve ";
        sql += ""
        + " where user_files_id = " + DbS.chara(pUserFilesId);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setUserFileDaoForJoin(map,this);
        return true;
    }
    /**
     * user_reserve ユーザ情報テーブルを検索しuser_reserve ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserReserveId   ユーザ予約ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pUserFilesId,String pas) throws AtareSysException
    {
        String sql =  "select "
                + " user_files.user_files_id as user_files___user_files_id"
                + ",user_files.user_info_id as user_files___user_info_id"
                + ",user_files.file_id as user_files___file_id"
        + " from user_files ";
        sql += ""
        + " where user_files_id = " + DbS.chara(pUserFilesId)
        + " and password = " + DbS.chara(pas);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setUserFileDaoForJoin(map,this);
        return true;
    }


    /**
     * UserReserveDao にuser_reserve情報テーブルから読み込んだデータを設定する。.
     *
     * @param map 読み込んだテーブルの１レコードが入っているHashMap
     * @param dao UserReserveDaoこのテーブルのインスタンス
     */
    public void setUserFileDao(HashMap<String, String> map, UserFileDao dao) throws AtareSysException {
      dao.setUserFilesId(DbI.chara(map.get("user_files_id")));
      dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
      dao.setFileId(DbI.chara(map.get("file_id")));
    }

    /**
     * UserReserveDao にuser_reserve情報テーブルから読み込んだデータを設定する。.
     *
     * @param map 読み込んだテーブルの１レコードが入っているHashMap
     * @param dao UserReserveDaoこのテーブルのインスタンス
     */
    public void setUserFileDaoForJoin(HashMap<String, String> map, UserFileDao dao) throws AtareSysException {
      dao.setUserFilesId(DbI.chara(map.get("user_files___user_files_id")));
      dao.setUserInfoId(DbI.chara(map.get("user_files___user_info_id")));
      dao.setFileId(DbI.chara(map.get("user_files___reserve_id")));
    }
    /**
     * user_files ユーザーファイルテーブルにデータを挿入する
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbUserFileInsert(String puserInfoId, String pfileId) throws AtareSysException
    {
        String userFileId = UUID.randomUUID().toString().substring(0, 13);
        String sql="insert into user_files ("
        + " user_files_id"
        + ",user_info_id"
        + ",file_id"
        + " ) values ( "
        + DbO.chara(userFileId)
        + "," + DbO.chara(puserInfoId)
        + "," + DbO.chara(pfileId)
        + " )";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbInsert number or record exception.") ;
        return true;
    }

    /**
     * user_file ユーザーファイルテーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdateUserFile() throws AtareSysException
    {
        String sql="update user_files set "
        + " user_files_id = " +    DbO.chara(getUserFilesId())
        + "," +" user_info_id = " +  DbO.chara(getUserInfoId())
        + "," + " file_id = " +    DbO.chara(getFileId())
        + " where user_files = " + DbS.chara(userFilesId)
        + "";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbUpdate number or record exception.") ;
        return true;
    }

    /**
     * user_file ユーザー情報テーブルからデータを削除する
     *
     * @param pUserReserveId   予約情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbDeleteUserFile(String pFileId) throws AtareSysException
    {
        String sql="delete from user_files "
        + " where file_id = " + DbS.chara(pFileId);
        int ret = DbBase.dbExec(sql);
        if(ret<1) throw new AtareSysException("dbDelete number or record exception.") ;
        return true;
    }


    /**
     * データベースからルーム名を取得するメソッド
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public ArrayList<UserFileDao> getAllUserFiles() throws AtareSysException {
      String sql = "SELECT user_files_id"
          + ", user_info_id"
          + ", file_id FROM user_files";
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      ArrayList<UserFileDao> userFiles = new ArrayList<>();
      for (HashMap<String, String> map : rs) {
        UserFileDao userFile = new UserFileDao();
          // ReserveDAOのインスタンスにデータを設定
          userFile.setUserFilesId(map.get("user_files_id"));
          userFile.setUserInfoId(map.get("user_info_id"));
          userFile.setFileId(map.get("file_id"));
          userFiles.add(userFile);
      }

      return userFiles; // 取得したルームリストを返す
  }
    /**
     * 予約情報を表示 ※
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public UserFileDao getMostUserFile() throws AtareSysException {
      // SQLクエリを最新の予約を取得する
      String sql = "SELECT * FROM user_files ORDER BY user_files_id DESC LIMIT 1";
      // クエリを実行して結果を取得
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      // 結果が空でないかをチェック
      if (rs.isEmpty()) {
          return null; // 予約が見つからなかった場合はnullを返す
      }
      // 最初の結果を取り出す
      HashMap<String, String> map = rs.get(0);
      // ReserveDaoのインスタンスを作成
      UserFileDao userFile = new UserFileDao();
      // ReserveDaoのインスタンスにデータを設定
      userFile.setUserFilesId(map.get("user_files_id"));
      userFile.setUserInfoId(map.get("user_info_id"));
      userFile.setFileId(map.get("file_id"));

      // 最新の予約を返す
      return userFile;
  }

    /**
     * user_reserve ユーザ情報テーブルを検索し指定されたレコードのリストを返す
     * @param myclass        検索条件をUserReserveDaoのインスタンスに入れて渡す
     * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
     * @param daoPageInfo   取得したいページの番やライン数を入れる。結果がここに帰ってくる
     *                       ライン数に-1を入れると全件取得になる
     * @return 取得したUserReserveDaoの配列
     * @throws AtareSysException エラー
     */
    static public ArrayList<UserFileDao> dbSelectList(UserFileDao myclass,LinkedHashMap<String,String> sortKey,DaoPageInfo daoPageInfo) throws AtareSysException
    {
        ArrayList<UserFileDao> array = new ArrayList<UserFileDao>();

        /* レコードの総件数を求める */
        String sql =  "select count(*) as count"
        + " from user_files "
        + " left join user_info on user_files.user_info_id = user_info.user_info_id "
        + " left join files on user_files.file_id = files.file_id "
        + myclass.dbWhere();
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if(len == 0)   return array;
        if(-1==daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double)len/(double)(daoPageInfo.getLineCount())));
        if(daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if(daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start  =   (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql =  "select "
            + " user_files.user_files_id as user_files___user_files_id"
            + ",user_files.user_info_id as user_files___user_info_id"
            + ",user_files.file_id as user_files___file_id"
            + " from user_files "
            + " left join user_info on user_files.user_info_id = user_info.user_info_id "
            + " left join files on user_files.file_id = files.file_id ";
        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);
        sql += where;
        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";
        rs  =  DbBase.dbSelect(sql);
        int cnt = rs.size();
        if(cnt < 1)    return array;
        for(int i=0;i<cnt;i++)
        {
            UserFileDao  dao  = new UserFileDao();
            map = rs.get(i);
            dao.setUserFileDaoForJoin(map,dao);
            array.add(dao);
        }
        return array;
    }

    /**
     * user_reserve ユーザ情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException
    {
        StringBuffer where = new StringBuffer(1024);

        if(getUserFilesId().length()>0)
        {
          where.append(where.length()>0 ? " OR " : "");
          where.append("user_files.user_files_id LIKE " + DbS.chara("%" + getUserFilesId() + "%"));
        }

        if(getUserInfoId().length()>0)
        {
            where.append(where.length()>0 ? " OR " : "");
            where.append("user_files.user_info_id LIKE " + DbS.chara("%" + getUserInfoId() + "%"));
        }
        if(getFileId().length()>0)
        {
            where.append(where.length()>0 ? " OR " : "");
            where.append("user_files.file_id LIKE " + DbS.chara("%" + getFileId() + "%"));
        }
        if(where.length()>0)
        {
            return "where " + where.toString();
        }
        return "";
    }
    /**
     * user_reserve ユーザ情報テーブルの並べ替え順序を設定する。.
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
}