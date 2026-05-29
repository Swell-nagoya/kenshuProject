package jp.swell.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.dao.UserReserveDao;


public class Schedule extends ControllerBase {
  
  /**
   * jp.patasys.alumni.controller.HttpServlet のメソッドをオーバライドする。
   * オーバライドしない場合は、デフォルトが設定される。. この処理にはログインが必要かどうか デフォルト true. この処理はhttpでなければならないか
   * デフォルト false. この処理はhttps でなければならないか デフォルト false. この処理はクライアントのキャッシュを認めるか デフォルト
   * false. 等を設定する。 doActionの前に呼ばれる。
   */
  @Override
  public void doInit() {
    setLoginNeeds(false); // この処理にはログインが必要かどうか
    setHttpNeeds(false); // この処理はhttpでなければならないか
    setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
    setUsecache(false); // この処理はクライアントのキャッシュを認めるか
  }
  
  
  

  @Override
  public void doActionProcess() throws AtareSysException {
    WebBean bean = getWebBean();

    if ("Schedule".equals(bean.value("form_name"))) 
    {
      bean.trimAllItem();
      if ("menu".equals(bean.value("action_cmd"))) 
      {
        redirect("ViewUserList.do");
      } 
      else if ("search".equals(bean.value("action_cmd")))
      {
        bean.setValue("pageNo", "1");
        formInit();
        searchList();
        forward("Schedule.jsp");
      }
      else if ("next".equals(bean.value("action_cmd")))
      {
          bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
          searchList();
          forward("Schedule.jsp");
      }
      else if ("jump".equals(bean.value("action_cmd")))
      {
          searchList();
          forward("Schedule.jsp");
      }
      else if ("prior".equals(bean.value("action_cmd")))
      {
          bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
          searchList();
          forward("Schedule.jsp");
      }
      else if ("clear".equals(bean.value("action_cmd")))
      {
        formClear();
        searchList();
        forward("Schedule.jsp");
      }
      else if ("go_next".equals(bean.value("action_cmd"))) 
      {
        setWeb2Dao2InputInfo();
        bean.setMessage("この内容で登録します。よろしいですか？");
        bean.setValue("request_name", "登録");
        forward("ScheduleCheck.jsp");
      }
      else if ("sort".equals(bean.value("action_cmd")))
      {
          searchList();
          forward("Schedule.jsp");
      }
    }
    else if ("ScheduleCheck".equals(bean.value("form_name"))) 
    {
      if ("return".equals(bean.value("action_cmd"))) 
      {
        setInputInfo2Dao2Web();
        searchList();
        forward("Schedule.jsp");
      }
      else if ("go_next".equals(bean.value("action_cmd"))) 
      {
        if ("ins".equals(bean.value("request_cmd"))) 
        {
          bean.setMessage("この内容で登録しました。");
          bean.setValue("request_name", "ＯＫ");
          setInputInfo2Dao2Web();
          insert();
          forward("ScheduleCheck.jsp");
        }
        else if ("ok".equals(bean.value("request_cmd"))) 
        {
          redirect("ViewUserList.do");
        }
      }
    }
    else 
    {
      formInit();
      formClear();
      searchList();
      forward("Schedule.jsp");
    }
  }

  /**
   * 最初の画面を表示する。.
   *
   * @throws AtareSysException
   */
  private void formInit() throws AtareSysException {
    WebBean bean = getWebBean();
    UserInfoDao dao = (UserInfoDao) Sup.deserialize(bean.value("input_info"));

    bean.setValue("sort_key", "list_search"); /* 初回のソートキーを入れる */
    bean.setValue("sort_order", "asc");
    bean.setValue("user_info_id", dao.getUserInfoId());
    bean.setValue("main_user_name", dao.getFullName());
    bean.setValue("lineCount",
    SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "ViewUserList", "lineCount", "100"));
  }

  /**
   * フィールドをクリアする。.
   */
  private void formClear() throws AtareSysException
  {
      WebBean bean = getWebBean();
      bean.setValue("list_search_full_name", "");
      bean.setValue("list_search_full_name_kana", "");
      bean.setValue("lineCount", "");
      String search_info = Sup.serialize(bean);
      bean.setValue("search_info", search_info);
  }
  
  /**
   * 入力チェックを行う。.
   *
   * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
   */
  private HashMap<String, String> inputCheck() {
    WebBean bean = getWebBean();
    HashMap<String, String> errors = bean.getItemErrors();
    if (bean.value("list_search").length() > 0) {
      if (100 < bean.value("list_search").length()) {
        errors.put("list_search", "氏名の入力内容が長すぎます。");
      }
    }
    return errors;
  }

  /**
   * 検索を行いbeanに格納する。.
   */
  private void searchList() throws AtareSysException {
    WebBean bean = getWebBean();
    HashMap<String, String> errors;

    errors = inputCheck();
    if (errors.size() > 0) {
      bean.setValue("errors", errors);
      return;
    }

    // ユーザー情報の検索
    LinkedHashMap<String, String> sortKey = sortKey();
    LinkedHashMap<String, String> scheduleSortKey = scheduleSortKey();
    UserInfoDao dao = new UserInfoDao();
    dao.setSearchName(bean.value("list_search_full_name"));
    
    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount"))) 
    {
      bean.setValue("lineCount", "200");
    }
    daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
    SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "UserMenuHome", "lineCount", bean.value("lineCount"));
    if (!Validate.isInteger(bean.value("pageNo"))) 
    {
      daoPageInfo.setPageNo(1);
    } 
    else
    {
      daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
    }
    ArrayList<UserInfoDao> listData = UserInfoDao.dbSelectList(dao, sortKey, daoPageInfo);
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

    // ユーザー情報の取得とセット
    UserInfoDao userInfoDao = new UserInfoDao();
    ArrayList<UserInfoDao> users = userInfoDao.getAllUsers();
    
    // 紐づけ情報の取得とセット
    UserReserveDao userReserveDao = new UserReserveDao();
    ArrayList<UserReserveDao> userReserves = userReserveDao.getAllUserReserves();
    
    // ユーザセレクトの一覧を取得とセット
    ScheduleDao scheduleDao = new ScheduleDao();
    ArrayList<ScheduleDao> scheduleDaos = ScheduleDao.dbSelectList(scheduleDao, scheduleSortKey, daoPageInfo);

    bean.getWebValues().remove("search_info");
    String search_info = Sup.serialize(bean);
    bean.setValue("search_info", search_info);
    bean.setValue("users", users);
    bean.setValue("userReserves", userReserves);
    bean.setValue("list", listData);
    bean.setValue("scheduleDaos", scheduleDaos);
  }

  /**
   * ユーザー名のソート順番を求める
   *
   * @return ソート順を格納した配列を返す
   */
  private LinkedHashMap<String, String> sortKey() {
    WebBean bean = getWebBean();
    String key = "";
    LinkedHashMap<String, String> sort_key = new LinkedHashMap<String, String>(); /* この配列にソートキーとソートオーダーを入れる */
    if (bean.value("sort_key").length() == 0 && bean.value("sort_key_old").length() == 0)
      return null;
    if (bean.value("sort_key_old").length() > 0) {
      if (bean.value("sort_key").length() > 0) {
        if (bean.value("sort_key").equals(bean.value("sort_key_old"))) {
          // 同一ソートキー（フリップフロップ）
          key = bean.value("sort_key_old");
          if ("desc".equals(bean.value("sort_order"))) {
            sort_key.put(key, "asc");
          } else {
            sort_key.put(key, "desc");
          }
        } else {
          // 新たなソートキー
          key = bean.value("sort_key");
          sort_key.put(key, "asc");
        }
      } else {
        // 引き継ぎ
        key = bean.value("sort_key_old");
        if ("asc".equals(bean.value("sort_order"))) {
          sort_key.put(key, "asc");
        } else {
          sort_key.put(key, "desc");
        }
      }
    } else {
      // 初期値
      key = bean.value("sort_key");
      if ("asc".equals(bean.value("sort_order"))) {
        sort_key.put(key, "asc");
      } else {
        sort_key.put(key, "desc");
      }
    }
    bean.setValue("sort_key", "");
    bean.setValue("sort_key_old", key);
    bean.setValue("sort_order", sort_key.get(key));
    return sort_key;
  }

  /**
   * ソート順番を求める
   *
   * @return ソート順を格納した配列を返す
   */
  private LinkedHashMap<String, String> scheduleSortKey() {
    return new LinkedHashMap<>();
  }
  
  /**
   * ページ番号を加算減算する
   *
   * @param $page_no
   *        現在のページ番号
   * @param $add
   *        加算減算する値
   * @return 結果のページを返す
   */
  private String calcPageNo(String pageNo, int add)
  {
      int ret;
      if (null == pageNo)
      {
          pageNo = "1";
      }
      else if ("".equals(pageNo))
      {
          pageNo = "1";
      }
      else if (!Validate.isInteger(pageNo))
      {
          pageNo = "1";
      }
      ret = Integer.parseInt(pageNo);
      ret += add;
      return String.valueOf(ret);
  }

  

  /**
   * DAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
   *
   * @return なし
   * @throws AtareSysException エラー
   */
  private ScheduleDao setWeb2Dao2InputInfo() throws AtareSysException {
    WebBean bean = getWebBean();
    ScheduleDao scheduledao = new ScheduleDao();
    

    scheduledao.setMainUserId(bean.value("user_info_id"));
    scheduledao.setMainUserName(bean.value("main_user_name"));
    scheduledao.setLinkUserId(bean.value("selected_user_ids"));
    scheduledao.setLinkUserName(bean.value("selected_user_names"));
    scheduledao.setPriority(bean.value("selected_user_priorities"));
    scheduledao.setSearchFullName(bean.value("list_search_full_name"));
    scheduledao.setSearchFullNameKana(bean.value("list_search_full_name_kana"));

    bean.setValue("input_info", Sup.serialize(scheduledao)); // DAOオブジェクトをシリアライズしてWebBeanに保存
    
    return scheduledao;
  }
  
  private ScheduleDao setWeb2Dao2() throws AtareSysException {
    WebBean bean = getWebBean();
    ScheduleDao schedule = new ScheduleDao();

    schedule.setMainUserId(bean.value("user_info_id"));
    schedule.setLinkUserId(bean.value("user_info_id"));
    schedule.setPriority("3");

    bean.setValue("input_info", Sup.serialize(schedule)); // DAOオブジェクトをシリアライズしてWebBeanに保存
    return schedule;
  }
  /**
   * input_infoフィールドからクラスを取り出し、画面の項目に値を設定する
   *
   * @return なし
   * @throws AtareSysException
   */
  private void setInputInfo2Dao2Web() throws AtareSysException
  {
      WebBean bean = getWebBean();
      ScheduleDao scheduledao = (ScheduleDao) Sup.deserialize(bean.value("input_info"));
      bean.setValue("user_info_id", scheduledao.getMainUserId());
      bean.setValue("main_user_name", scheduledao.getMainUserName());
      bean.setValue("selected_user_ids", scheduledao.getLinkUserId());
      bean.setValue("selected_user_names", scheduledao.getLinkUserName());
      bean.setValue("selected_user_priorities", scheduledao.getPriority());
      bean.setValue("list_search_full_name", scheduledao.getSearchFullName());
      bean.setValue("list_search_full_name_kana", scheduledao.getSearchFullNameKana());
  }
  
  private boolean insert() throws AtareSysException {
    ScheduleDao schedule = setWeb2Dao2();
    ScheduleDao scheduledao = setWeb2Dao2InputInfo();

    try {
      // 入力内容をデータベースに保存
      schedule.dbInsert();
      scheduledao.dbInsert();
      
      return true;
      
    } catch (Exception e) {
      return false;
    }
  }
  
  
}