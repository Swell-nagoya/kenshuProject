/*
* (c)2010 2023 PATAPATA Corp. Corp. All Rights Reserved
*
* 機能名　　　　：DAOクラス
* ファイル名　　：Reserve.java
* クラス名　　　：ReserveDao
* 概要　　　　　：Reserve Reserve情報テーブルのDAOを提供する。
* バージョン　　：
*
* 改版履歴　　　：
* 2024/07/10 <新規>    新規作成
*
*/
package jp.swell.dao;

import java.io.Serializable;
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

/**
 * UserReserveDao  UserReserveDao 情報テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class UserReserveDao implements Serializable {
  /** Derializable No. */
  private static final long serialVersionUID = 1L;
  /**
   * データアクセス権限のあるユーザリスト
   */
  private ArrayList<String> authorityUserList = null;
  /**
   * userReserveId ユーザー予約ID
   */
  private String userReserveId = "";
  /**
   * reserveId 予約情報ID
   */
  private String reserveId = "";
  /**
   * userInfoId ユーザー情報ID
   */
  private String userInfoId = "";
  /**
   * 予約紐づけIDを取得する。
   * @return userReserveId 予約紐づけID
   */
  public String getUserReserveId()
  {
    return userReserveId;
  }
  /**
   * 予約紐づけIDをセットする。.
   * @param userReserveId 予約紐づけID
   */
  public void setUserReserveId(String userReserveId)
  {
    this.userReserveId = userReserveId;
  }
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
   * ユーザ情報IDを取得する。
   * @return userInfoId ユーザ情報ID
   */
  public String getUserInfoId()
  {
    return userInfoId;
  }
  /**
   * ユーザ情報IDをセットする。
   * @param userInfoId ユーザ情報ID
   */
  public void setUserInfoId(String userInfoId)
  {
    this.userInfoId = userInfoId;
  }
  
  private ReserveFileDao reserveFileDaos = new ReserveFileDao();
  /**
   * reserve_filesテーブルのfile_idをセット・取得
   * 
   * @return file_id
   */
  public ReserveFileDao getReserveFileDaos() {
    return reserveFileDaos;
  }
  public void setReserveFileDaos(ReserveFileDao reserveFileDaos) {
    this.reserveFileDaos = reserveFileDaos;
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
  
  private ReserveDao reserveDaos = new ReserveDao();
  /**
   * reserve_filesテーブルのfile_idをセット・取得
   * 
   * @return file_id
   */
  public ReserveDao getReserveDaos() {
    return reserveDaos;
  }
  public void setReserveDaos(ReserveDao reserveDaos) {
    this.reserveDaos = reserveDaos;
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
  
  /**
   * データアクセス権限のあるユーザリストを取得する。.
   */
  public ArrayList<String> getAuthorityUserList() {
    return authorityUserList;
  }

  /**
   * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
   */
  private HashMap<String, String> fieldsArray = new HashMap<String, String>();

  /**
   * コンストラクタ。
   */
  public UserReserveDao() {
    fieldsArray.put("user_reserve_id", "user_reserve.user_reserve_id");
    fieldsArray.put("user_info_id", "user_reserve.user_info_id");
    fieldsArray.put("reserve_id", "user_reserve.reserve_id");

  }
  /**
   * user_reserve ユーザ情報テーブルを検索しuser_reserve ユーザ情報テーブルの１行を取得します。.
   *
   * @param pUserReserveId   ユーザ予約ID
   * @return true:読み込み成功 false:存在しない
   * @throws AtareSysException フレームワーク共通例外
   */
  public boolean dbSelect(String pUserReserveId) throws AtareSysException
  {
      String sql =  "select "
              + " user_reserve.user_reserve_id as user_reserve___user_reserve_id"
              + ",user_reserve.user_info_id as user_reserve___user_info_id"
              + ",user_reserve.reserve_id as user_reserve___reserve_id"
      + " from user_reserve ";
      sql += ""
      + " where user_reserve_id = " + DbS.chara(pUserReserveId);
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      if(0==rs.size())   return false;
      HashMap<String, String> map = rs.get(0);
      setUserReserveDaoForJoin(map,this);
      return true;
  }
  /**
   * user_reserve ユーザ情報テーブルを検索しuser_reserve ユーザ情報テーブルの１行を取得します。.
   *
   * @param pUserReserveId   ユーザ予約ID
   * @return true:読み込み成功 false:存在しない
   * @throws AtareSysException フレームワーク共通例外
   */
  public boolean dbSelect(String pUserReserveId,String pas) throws AtareSysException
  {
      String sql =  "select "
              + " user_reserve.user_reserve_id as user_reserve___user_reserve_id"
              + ",user_reserve.user_info_id as user_reserve___user_info_id"
              + ",user_reserve.reserve_id as user_reserve___reserve_id"
      + " from user_reserve ";
      sql += ""
      + " where user_reserve_id = " + DbS.chara(pUserReserveId)
      + " and password = " + DbS.chara(pas);
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      if(0==rs.size())   return false;
      HashMap<String, String> map = rs.get(0);
      setUserReserveDaoForJoin(map,this);
      return true;
  }

  /**
   * UserReserveDao にuser_reserve情報テーブルから読み込んだデータを設定する。.
   *
   * @param map 読み込んだテーブルの１レコードが入っているHashMap
   * @param dao UserReserveDaoこのテーブルのインスタンス
   */
  public void setUserReserveDao(HashMap<String, String> map, UserReserveDao dao) throws AtareSysException {
    dao.setUserReserveId(DbI.chara(map.get("user_reserve_id")));
    dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
    dao.setReserveId(DbI.chara(map.get("reserve_id")));
    

  }

  /**
   * UserReserveDao にuser_reserve情報テーブルから読み込んだデータを設定する。.
   *
   * @param map 読み込んだテーブルの１レコードが入っているHashMap
   * @param dao UserReserveDaoこのテーブルのインスタンス
   */
  public void setUserReserveDaoForJoin(HashMap<String, String> map, UserReserveDao dao) throws AtareSysException {
    dao.setUserReserveId(DbI.chara(map.get("user_reserve___user_reserve_id")));
    dao.setUserInfoId(DbI.chara(map.get("user_reserve___user_info_id")));
    dao.setReserveId(DbI.chara(map.get("user_reserve___reserve_id")));
    // link_user_idに対応するreserve_idを取得し、セット
    ReserveDao reserve = new ReserveDao();
    reserve.dbSelect(DbI.chara(map.getOrDefault("user_reserve___reserve_id", "")));
    dao.setReserveDaos(reserve);
    // link_user_idに対応するreserve_idを取得し、セット
    ReserveFileDao reserveFile = new ReserveFileDao();
    reserveFile.dbSelect(DbI.chara(map.getOrDefault("user_reserve___reserve_id", "")));
    dao.setReserveFileDaos(reserveFile);
  }
  /**
   * user_reserve 予約情報テーブルにデータを挿入する
   *
   * @return true:成功 false:失敗
   * @throws AtareSysException エラー
   */
  public boolean dbInsertUserReserve() throws AtareSysException {
      String sql = "insert into user_reserve ("
              + " user_reserve_id"
              + ", user_info_id"
              + ", reserve_id"
              + ") values ("
              +  DbO.chara(getUserReserveId())
              + "," + DbO.chara(getUserInfoId())
              + "," + DbO.chara(getReserveId())
              + ")";
      int ret = DbBase.dbExec(sql);
      if (ret != 1) throw new AtareSysException("dbInsertReserve number or record exception.");
      return true;
  }

  /**
   * user_reserve 予約情報テーブルのデータを更新する。.
   *
   * @return true:成功 false:失敗
   * @throws AtareSysException フレームワーク共通例外
   */
  public boolean dbUpdateUserReserve() throws AtareSysException
  {
      String sql="update user_reserve set "
      + " user_reserve_id = " +    DbO.chara(getUserReserveId())
      + "," +" user_info_id = " +  DbO.chara(getUserInfoId())
      + "," + " reserve_id = " +    DbO.chara(getReserveId())
      + " where user_reserve = " + DbS.chara(userReserveId)
      + "";
      int ret = DbBase.dbExec(sql);
      if(ret!=1) throw new AtareSysException("dbUpdate number or record exception.") ;
      return true;
  }

  /**
   * user_reserve 予約情報テーブルからデータを削除する
   *
   * @param pUserReserveId   予約情報ID
   * @return true:成功 false:失敗
   * @throws AtareSysException エラー
   */
  public boolean dbDeleteUserReserve(String pReserveId) throws AtareSysException
  {
      String sql="delete from user_reserve "
      + " where reserve_id = " + DbS.chara(pReserveId);
      int ret = DbBase.dbExec(sql);
      if(ret<1) throw new AtareSysException("dbDelete number or record exception.") ;
      return true;
  }


  /**
   * データベースからルーム名を取得するメソッド
   * @return UserYoyakuDetailに返す
   * @throws AtareSysException
   */
  public ArrayList<UserReserveDao> getAllUserReserves() throws AtareSysException {
    String sql = "SELECT user_reserve_id"
        + ", user_info_id"
        + ", reserve_id FROM user_reserve";
    List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
    ArrayList<UserReserveDao> userReserves = new ArrayList<>();
    for (HashMap<String, String> map : rs) {
      UserReserveDao userReserve = new UserReserveDao();
        // ReserveDAOのインスタンスにデータを設定
        userReserve.setUserReserveId(map.get("user_reserve_id"));
        userReserve.setUserInfoId(map.get("user_info_id"));
        userReserve.setReserveId(map.get("reserve_id"));
        userReserves.add(userReserve);
    }

    return userReserves; // 取得したルームリストを返す
}
  
  
  
  /**
   * 予約情報を表示
   * @return UserYoyakuDetailに返す
   * @throws AtareSysException
   */
  public UserReserveDao getMostUserReserve() throws AtareSysException {
    // SQLクエリを最新の予約を取得する
    String sql = "SELECT * FROM user_reserve ORDER BY user_reserve_id DESC LIMIT 1";
    // クエリを実行して結果を取得
    List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
    // 結果が空でないかをチェック
    if (rs.isEmpty()) {
        return null; // 予約が見つからなかった場合はnullを返す
    }
    // 最初の結果を取り出す
    HashMap<String, String> map = rs.get(0);
    // ReserveDaoのインスタンスを作成
    UserReserveDao userReserve = new UserReserveDao();
    // ReserveDaoのインスタンスにデータを設定
    userReserve.setUserReserveId(map.get("user_reserve_id"));
    userReserve.setUserInfoId(map.get("user_info_id"));
    userReserve.setReserveId(map.get("reserve_id"));

    // 最新の予約を返す
    return userReserve;
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
  static public ArrayList<UserReserveDao> dbSelectList(UserReserveDao myclass, LinkedHashMap<String,String> sortKey,DaoPageInfo daoPageInfo) throws AtareSysException
  {
      ArrayList<UserReserveDao> array = new ArrayList<UserReserveDao>();

      /* レコードの総件数を求める */
      String sql =  "select count(*) as count"
      + " from user_reserve "
      + " left join user_info on user_reserve.user_info_id = user_info.user_info_id "
      + " left join reserve on user_reserve.reserve_id = reserve.reserve_id "
      + " left join room on reserve.room_id = room.room_id "
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
          + " user_reserve.user_reserve_id as user_reserve___user_reserve_id"
          + ",user_reserve.user_info_id as user_reserve___user_info_id"
          + ",user_reserve.reserve_id as user_reserve___reserve_id"
          + " from user_reserve "
          + " left join user_info on user_reserve.user_info_id = user_info.user_info_id "
          + " left join reserve on user_reserve.reserve_id = reserve.reserve_id "
          + " left join room on reserve.room_id = room.room_id ";
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
          UserReserveDao  dao  = new UserReserveDao();
          map = rs.get(i);
          dao.setUserReserveDaoForJoin(map,dao);
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
      if (userIds != null && userIds.length > 0) {
        where.append(where.length() > 0 ? " OR " : "");
        where.append("user_reserve.user_info_id IN (");
        
        for (int i = 0; i < userIds.length; i++) {
            where.append(DbS.chara(userIds[i]));
            if (i < userIds.length - 1) {
                where.append(", ");
            }
        }
        
        where.append(") ");
      }

      if(getUserReserveId().length()>0)
      {
        where.append(where.length()>0 ? " OR " : "");
        where.append("user_reserve.user_reserve_id LIKE " + DbS.chara("%" + getUserReserveId() + "%"));
      }

      if(getUserInfoId().length()>0)
      {
          where.append(where.length()>0 ? " OR " : "");
          where.append("user_reserve.user_info_id LIKE " + DbS.chara("%" + getUserInfoId() + "%"));
      }
      if(getReserveId().length()>0)
      {
          where.append(where.length()>0 ? " OR " : "");
          where.append("user_reserve.reserve_id LIKE " + DbS.chara("%" + getReserveId() + "%"));
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
