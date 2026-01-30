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
 * schedule_access スケジュール管理テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class ScheduleDao implements Serializable {
  /** Derializable No. */
  private static final long serialVersionUID = 1L;
  
  /**
   * schedule_access スケジュール管理テーブルで使用するメンバー変数。
   */
  /**
   * linkUserId メインユーザ名
   */
  private String mainUserName = "";

  /**
   * メインユーザ名を取得する。.
   * 
   * @return linkUserId メインユーザ名
   */
  public String getMainUserName() {
    return mainUserName;
  }

  /**
   * メインユーザ名をセットする。.
   * 
   * @param linkUserName メインユーザ名
   */
  public void setMainUserName(String mainUserName) {
    this.mainUserName = mainUserName;
  }
  
  /**
   * schedule_access スケジュール管理テーブルで使用するメンバー変数。
   */
  /**
   * linkUserId 選択したユーザ名
   */
  private String linkUserName = "";

  /**
   * 選択したユーザ名を取得する。.
   * 
   * @return linkUserId 選択したユーザ名
   */
  public String getLinkUserName() {
    return linkUserName;
  }

  /**
   * 選択したユーザ名をセットする。.
   * 
   * @param linkUserName 選択したユーザ名
   */
  public void setLinkUserName(String linkUserName) {
    this.linkUserName = linkUserName;
  }
  
  /**
   * schedule_access スケジュール管理テーブルで使用するメンバー変数。
   */
  /**
   * mainUserId メインユーザ情報ID
   */
  private String mainUserId = "";

  /**
   * メインユーザ情報IDを取得する。.
   * 
   * @return mainUserId メインユーザ情報ID
   */
  public String getMainUserId() {
    return mainUserId;
  }

  /**
   * メインユーザ情報IDをセットする。.
   * 
   * @param mainUserId メインユーザ情報ID
   */
  public void setMainUserId(String mainUserId) {
    this.mainUserId = mainUserId;
  }

  /**
   * schedule_access スケジュール管理テーブルで使用するメンバー変数。
   */
  /**
   * linkUserId リンクユーザ情報ID
   */
  private String linkUserId = "";

  /**
   * リンクユーザ情報IDを取得する。.
   * 
   * @return linkUserId リンクユーザ情報ID
   */
  public String getLinkUserId() {
    return linkUserId;
  }

  /**
   * リンクユーザ情報IDをセットする。.
   * 
   * @param linkUserId リンクユーザ情報ID
   */
  public void setLinkUserId(String linkUserId) {
    this.linkUserId = linkUserId;
  }
  
  /**
   * schedule_access スケジュール管理テーブルで使用するメンバー変数。
   */
  /**
   * linkUserId リンクユーザ情報ID
   */
  private String priority = "";

  /**
   * リンクユーザ情報IDを取得する。.
   * 
   * @return priority 表示優先度
   */
  public String getPriority() {
    return priority;
  }
  
  /**
   * searchFullName 検索用氏名
   */
  private String searchFullName = "";
  /**
   * searchFullName 検索用氏名かな
   */
  private String searchFullNameKana = "";

  /**
   * searchFullName 検索用氏名を取得します。
   *
   * @return searchFullName 検索用氏名
   */
  public String getSearchFullName()
  {
      return searchFullName;
  }

  /**
   * searchFullName 検索用氏名を設定します。
   *
   * @param searchFullName
   *        searchFullName 検索用氏名
   */
  public void setSearchFullName(String searchFullName)
  {
      this.searchFullName = searchFullName;
  }

  /**
   * searchFullName 検索用氏名かなを取得します。
   *
   * @return searchFullName 検索用氏名かな
   */
  public String getSearchFullNameKana()
  {
      return searchFullNameKana;
  }

  /**
   * searchFullName 検索用氏名かなを設定します。
   *
   * @param searchFullNameKana
   *        searchFullName 検索用氏名かな
   */
  public void setSearchFullNameKana(String searchFullNameKana)
  {
      this.searchFullNameKana = searchFullNameKana;
  }


  /**
   * リンクユーザ情報IDをセットする。.
   * 
   * @param priority リンクユーザ情報ID
   */
  public void setPriority(String priority) {
    this.priority = priority;
  }
  
  private UserInfoDao linkUserInfoDaos = new UserInfoDao();
  
  /**
   * schedule_accessテーブルの選択ユーザーの情報をセット・取得
   * 
   * @return 選択したユーザ情報
   */
  public UserInfoDao getLinkUserInfoDaos() {
    return linkUserInfoDaos;
  }
  public void setLinkUserInfoDaos(UserInfoDao linkUserInfoDaos) {
    this.linkUserInfoDaos = linkUserInfoDaos;
  }
  
  private UserInfoDao mainUserInfoDaos = new UserInfoDao();
  
  /**
   * schedule_accessテーブルのメインユーザーの情報をセット・取得
   * 
   * @return メインユーザ情報
   */
  public UserInfoDao getMainUserInfoDaos() {
    return mainUserInfoDaos;
  }
  public void setMainUserInfoDaos(UserInfoDao mainUserInfoDaos) {
    this.mainUserInfoDaos = mainUserInfoDaos;
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
   * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
   */
  private HashMap<String,String> fieldsArray = new HashMap<String,String>();

  /**
   * コンストラクタ。.
   */
  public ScheduleDao()
  {
      fieldsArray.put("user_info_id","user_info.user_info_id");
      fieldsArray.put("last_name","user_info.last_name");
      fieldsArray.put("middle_name","user_info.middle_name");
      fieldsArray.put("first_name","user_info.first_name");
      fieldsArray.put("last_name_kana","user_info.last_name_kana");
      fieldsArray.put("middle_name_kana","user_info.middle_name_kana");
      fieldsArray.put("first_name_kana","user_info.first_name_kana");
      fieldsArray.put("admin","user_info.admin");
  }


  /**
   * ScheduleDao にユーザ情報テーブルから読み込んだデータを設定する。.
   *
   * @param map  読み込んだテーブルの１レコードが入っているHashMap
   * @param dao  UserInfoDaoこのテーブルのインスタンス
   */
  public void setScheduleDao(HashMap<String, String> map,ScheduleDao dao)  throws AtareSysException
  {
//      dao.setAdmin(DbI.chara(map.get("admin")));
  }

  /**
   * ScheduleDao にユーザ情報テーブルから読み込んだデータを設定する。.
   *
   * @param map  読み込んだテーブルの１レコードが入っているHashMap
   * @param dao  UserInfoDaoこのテーブルのインスタンス
   */
  public void setScheduleDaoForJoin(HashMap<String, String> map, ScheduleDao dao) throws AtareSysException {
    dao.setMainUserId(DbI.chara(map.getOrDefault("main_user_id", "")));
    dao.setLinkUserId(DbI.chara(map.getOrDefault("link_user_id", "")));
    dao.setPriority(DbI.chara(map.getOrDefault("priority", "")));
    // main_user_idに対応するUserInfoDaoを取得し、セット
    UserInfoDao MainUserDao = new UserInfoDao();
    MainUserDao.dbSelect(DbI.chara(map.getOrDefault("main_user_id", "")));
    dao.setMainUserInfoDaos(MainUserDao);
    // link_user_idに対応するUserInfoDaoを取得し、セット
    UserInfoDao linkUserDao = new UserInfoDao();
    linkUserDao.dbSelect(DbI.chara(map.getOrDefault("link_user_id", "")));
    dao.setLinkUserInfoDaos(linkUserDao);
  }
  
  
  
  /**
   * データベースからユーザのアクセス情報を取得するメソッド
   * @return UserYoyakuDetailに返す
   * @throws AtareSysException
   */
  public ArrayList<ScheduleDao> getAllUsers() throws AtareSysException {
    String sql = "SELECT main_user_id"
        + ", link_user_id FROM schedule_access";
    List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
    ArrayList<ScheduleDao> schedules = new ArrayList<>();
    for (HashMap<String, String> map : rs) {
      ScheduleDao schedule = new ScheduleDao();
        // ReserveDAOのインスタンスにデータを設定
        schedule.setMainUserId(map.get("main_user_id"));
        schedule.setLinkUserId(map.get("link_user_id"));
        schedules.add(schedule);
    }
    return schedules; // 取得したユーザのアクセス情報を返す  

  }
  
/** 
   * schedule_access スケジュールテーブルにデータを挿入する 
   * 
   * @return true:成功 false:失敗 
   * @throws AtareSysException エラー 
   */ 
  public boolean dbInsert() throws AtareSysException 
  { 
      String linkUserIds = getLinkUserId(); // 複数のリンクユーザIDを取得
      String priorities = getPriority();
      
      // カンマで区切って配列に分割
      String[] linkUserIdArray = linkUserIds.split(",");
      String[] priorityArray = priorities.split(",");

      // 挿入成功フラグ
      boolean isInserted = true;
      
      
      // 既存のmain_user_idに基づいてデータを削除
      if(getMainUserId().equals(getLinkUserId())) {
        String deleteSql = "DELETE FROM schedule_access WHERE main_user_id = " + DbO.chara(getMainUserId());
        int deleteRet = DbBase.dbExec(deleteSql);
  
        // 削除結果の確認
        if (deleteRet < 0) {
            // 削除処理が失敗した場合のエラーハンドリング
            throw new AtareSysException("Failed to delete existing records for main user ID: " + getMainUserId());
        }
      }

      // 各リンクユーザIDについてデータベースに挿入
      for (int i = 0; i < linkUserIdArray.length; i++) {
        String sql = "INSERT INTO schedule_access (" 
            + " main_user_id, " 
            + " link_user_id, "  // カンマを追加
            + " priority " 
            + " ) VALUES ( " 
            + DbO.chara(getMainUserId()) 
            + "," + DbO.chara(linkUserIdArray[i]) 
            + "," + DbO.chara(priorityArray[i])
            + " )"; 
          
          int ret = DbBase.dbExec(sql); 
          
          // 挿入結果が1以外の場合、挿入失敗とみなす
          if (ret != 1) {
              isInserted = false; 
              throw new AtareSysException("dbInsert number of record exception for link user ID: " + linkUserId);
          }
      }

      return isInserted; // 全ての挿入が成功した場合はtrueを返す
  }
  
  /**
   * schedule_access スケジュールテーブルからデータを削除する
   *
   * @param pId   ユーザID
   * @return true:成功 false:失敗
   * @throws AtareSysException エラーs
   */
  public boolean dbDelete_1(String pId) throws AtareSysException
  {
      String sql = "delete from schedule_access where "
      + "main_user_id = " + DbS.chara(pId);
      int ret =DbBase.dbExec(sql);
      if (ret < 0) throw new AtareSysException("dbDelete number or record exception");
      return true;
  }
  
  public boolean dbDelete_2(String pId) throws AtareSysException
  {
      String sql = "delete from schedule_access where "
      + "link_user_id = " + DbS.chara(pId);
      int ret =DbBase.dbExec(sql);
      if (ret < 0) throw new AtareSysException("dbDelete number or record exception");
      return true;
  }
  
  /**
   * schedule_access スケジュールテーブルを検索し指定されたレコードのリストを返す
   * @param myclass        検索条件をUserInfoDaoのインスタンスに入れて渡す
   * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
   * @param daoPageInfo   取得したいページの番やライン数を入れる。結果がここに帰ってくる
   *                       ライン数に-1を入れると全件取得になる
   * @return 取得したUserInfoDaoの配列
   * @throws AtareSysException エラー
   */
  public static ArrayList<ScheduleDao> dbSelectList(ScheduleDao myclass, LinkedHashMap<String, String> sortKey, DaoPageInfo daoPageInfo) throws AtareSysException {
    ArrayList<ScheduleDao> array = new ArrayList<ScheduleDao>();

    // レコードの総件数を求める*/
    String sql = "select count(*) as count"
    + " from schedule_access "
    + myclass.dbWhere();
    List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
    if (0 == rs.size()) return array;
    HashMap<String, String> map = rs.get(0);
    int len = Integer.parseInt(map.get("count"));
    daoPageInfo.setRecordCount(len);
    if (len == 0) return array;
    if (-1 == daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
    daoPageInfo.setMaxPageNo((int) Math.ceil((double) len / (double) (daoPageInfo.getLineCount())));
    if (daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
    if (daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
    int start = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
    sql = "select "
            + "schedule_access.main_user_id as main_user_id"
            + ",schedule_access.link_user_id as link_user_id"
            + ",schedule_access.priority as priority"
            + " from schedule_access ";
    
    String where = myclass.dbWhere();
    String order = myclass.dbOrder(sortKey);
    sql += where;
    sql += order;
    sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";

    rs = DbBase.dbSelect(sql);
    int cnt = rs.size();
    if (cnt < 1) return array;

    for (int i = 0; i < cnt; i++) {
        map = rs.get(i);
            ScheduleDao scheduleDao = new ScheduleDao();
            scheduleDao.setScheduleDaoForJoin(map, scheduleDao);
            array.add(scheduleDao);
    }
    return array;
}

  /**
   * schedule_access スケジュールテーブルの検索条件を設定する。.
   *
   * @return String where句の文字列
   * @throws AtareSysException フレームワーク共通例外
   */
  private String dbWhere() throws AtareSysException
  {
      StringBuffer where = new StringBuffer(1024);
      
      if(getMainUserId().length()>0)
      {
          where.append(where.length()>0 ? " AND " : "");
          where.append("schedule_access.main_user_id = " + DbS.chara(getMainUserId()));
      }

      if(getLinkUserId().length()>0)
      {
          where.append(where.length()>0 ? " AND " : "");
          where.append("schedule_access.link_user_id = " + DbS.chara(getLinkUserId()));
      }
      if(getPriority().length()>0)
      {
          where.append(where.length()>0 ? " AND " : "");
          where.append("schedule_access.priority = " + DbS.chara(getPriority()));
      }
      
      if(where.length()>0)
      {
          return "where " + where.toString();
      }
      return "";
  }

  /**
   * schedule_access スケジュールテーブルの並べ替え順序を設定する。.
   *
   * @param sortKey
   * @return Stringソート句の文字列
   */
  private String dbOrder(LinkedHashMap<String, String> sortKey) {
    String str = "";
    if (sortKey == null) return "";
    
    Set<String> keySet = sortKey.keySet();
    for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
        String key = i.next();
        
        // カラム名とソート順（ASC / DESC）を処理
        str += !"".equals(str) ? " , " : "";
        str += key + ' ' + sortKey.get(key);  // 'priority desc'
    }
    
    str = "".equals(str) ? "" : (" order by " + str);
    return str;
}

}
