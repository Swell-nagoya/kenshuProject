package jp.swell.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.dao.ReserveDao;
import jp.swell.dao.RoomDao;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.dao.UserReserveDao;
import jp.swell.user.UserLoginInfo;

public class UserMenu extends ControllerBase
{
    /**
     * jp.patasys.alumni.controller.HttpServlet のメソッドをオーバライドする。
     * オーバライドしない場合は、デフォルトが設定される。.
     * この処理にはログインが必要かどうか デフォルト true.
     * この処理はhttpでなければならないか デフォルト false.
     * この処理はhttps でなければならないか デフォルト false.
     * この処理はクライアントのキャッシュを認めるか デフォルト false. 等を設定する。
     * doActionの前に呼ばれる。
     */
    @Override
    public void doInit()
    {
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();

        if ("UserMenuHome".equals(bean.value("form_name")))
        {
            bean.trimAllItem();
            if("top".equals(bean.value("action_cmd"))) {
                formInit();
                searchList();
                bean.setValue("schedule", "メイン");
                ReserveDao reserveDao = new ReserveDao();
                ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
                bean.setValue("reserves", reserves);
                forward("UserMenuHome.jsp");
            }

            else if ("home".equals(bean.value("action_cmd")))
            {
                searchList();
            }

            else if ("jump".equals(bean.value("action_cmd")))
            {
                searchList();
            }

            else if ("sort".equals(bean.value("action_cmd")))
            {
                searchList();
            }
            // 検索クリアする条件を追加
            else if ("clear".equals(bean.value("action_cmd")))
            {
              formClear();
            }
            // 検索する条件を追加
            else if ("search".equals(bean.value("action_cmd")))
            {
              bean.setValue("pageNo", "1");
              searchList();
            }
            else if ("sub".equals(bean.value("action_cmd")))
            {
              formInit();
              searchList();
              forward("UserSelect.jsp");
              return; // メソッドを終了
            
            }
            else if ("search_sub".equals(bean.value("action_cmd")))
            {
              bean.setValue("pageNo", "1");
              searchList();
              ReserveDao reserveDao = new ReserveDao();
              ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
              bean.setValue("reserves", reserves);
              forward("UserMenuHome.jsp");
            }
            // 部屋の情報を新規登録する条件を追加
            else if ("insertRoom".equals(bean.value("form_name")))
            {
                insertRoomInfo();
            }
            // 新規予約する条件を追加
            else if ("reserve".equals(bean.value("action_cmd")))
            {
                searchList();
                //setWebDaoInputInfo();
                forward("UserMenuReserve.jsp");
                return; // メソッドを終了
            // 予約情報を編集する条件を追加
            }
            else if ("edit".equals(bean.value("action_cmd")))
            {
                searchList();
                setDb2Web1();
                forward("ReserveEdit.jsp");
                return; // メソッドを終了
            // ユーザー情報管理画面移行する条件を追加
            }
            else if ("admin1".equals(bean.value("action_cmd")))
            {
                redirect("MenuAdmin.do");
            }
            else if ("admin2".equals(bean.value("action_cmd")))
            {
                searchList();
                ReserveDao reserveDao = new ReserveDao();
                ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
                bean.setValue("reserves", reserves);
                forward("UserMenuHome.jsp");
            }
            else if ("file".equals(bean.value("action_cmd")))
            {
                searchList();
                forward("FileList.jsp");
                return; // メソッドを終了
            }
            else if ("submitAction".equals(bean.value("action_cmd")))
            {
                formInit();
                searchList();
                searchListReserve();  
                bean.setValue("name", "linkName");
                ReserveDao reserveDao = new ReserveDao();
                ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
                bean.setValue("reserves", reserves);
                forward("UserMenuHome.jsp");
            }
        }   
        else if ("UserInfoDetail_1".equals(bean.value("form_name")) || "UserInfoDetail_2".equals(bean.value("form_name")) || "UserInfoDetail_3".equals(bean.value("form_name")))
        {
            setWebBeanFromSerialize(bean.value("search_info"));
            bean = getWebBean();
            searchList();
            ReserveDao reserveDao = new ReserveDao();
            ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
            bean.setValue("reserves", reserves);
            forward("UserMenuHome.jsp");
        }
        else
        {
            formInit();
            searchList();
            bean.setValue("schedule", "メイン");
            ReserveDao reserveDao = new ReserveDao();
            ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
            bean.setValue("reserves", reserves);
            RoomDao roomDao = new RoomDao();
            ArrayList<RoomDao> rooms = roomDao.getAllRooms();
            bean.setValue("rooms", rooms);
            forward("UserMenuHome.jsp");
        }
    }



    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void formInit() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "full_name_kana"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        bean.setValue("user_info_id", userLoginInfo.getLoginId());
        bean.setValue("login_user_name", userLoginInfo.getFullName());
        bean.setValue("lineCount", SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "ViewUserList", "lineCount", "100"));
    }

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     */
    private HashMap<String, String> inputCheck()
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("list_search_full_name").length() > 0)
        {
            if (100 < bean.value("list_search_full_name").length())
            {
                errors.put("list_search_full_name", "氏名の入力内容が長すぎます。");
            }
        }
        if (bean.value("list_search_full_name_kana").length() > 0)
        {
            if (100 < bean.value("list_search_full_name_kana").length())
            {
                errors.put("list_search_full_name_kana", "氏名よみの入力内容が長すぎます。");
            }
        }
        return errors;
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
 * 検索を行いbeanに格納する。.
 */
private void searchList() throws AtareSysException
{
    WebBean bean = getWebBean();
    HashMap<String, String> errors;

    errors = inputCheck();
    if (errors.size() > 0)
    {
        bean.setValue("errors", errors);
        return;
    }
    // ユーザー情報の検索
    LinkedHashMap<String, String> sortKey = sortKey();
    LinkedHashMap<String, String> scheduleSortKey = new LinkedHashMap<>();
    scheduleSortKey.put("priority", "desc");

    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount")))
    {
        bean.setValue("lineCount", "20");
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
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());
    String userInfoId = bean.value("user_info_id");
    bean.setValue("user_info_id", userInfoId);

    // ルーム情報の取得とセット
    RoomDao roomDao = new RoomDao();
    ArrayList<RoomDao> rooms = roomDao.getAllRooms();

    // ユーザー情報の取得とセット
    UserInfoDao userInfoDao = new UserInfoDao();
    ArrayList<UserInfoDao> users = userInfoDao.getAllUsers();
    
    // ユーザセレクトの一覧を取得とセット
    ScheduleDao scheduleDao = new ScheduleDao();
    scheduleDao.setMainUserId(getLoginUserId());
    ArrayList<ScheduleDao> scheduleDaos = ScheduleDao.dbSelectList(scheduleDao, scheduleSortKey, daoPageInfo);
    
    // 予約情報の一覧とファイルを取得とセット
    UserReserveDao userReserveDao = new UserReserveDao();
    daoPageInfo.setLineCount(100);
    userReserveDao.setUserInfoId(getLoginUserId());
    ArrayList<UserReserveDao> userReserveDaos = UserReserveDao.dbSelectList(userReserveDao, sortKey, daoPageInfo);

    // ファイルを取得とセット
    FileDao fileDao = new FileDao();
    fileDao.setUserInfoId(getLoginUserId());
    ArrayList<FileDao> fileDaos = FileDao.dbSelectList(fileDao, sortKey, daoPageInfo);

    String linkUserReserveDaos = "none";
    
    bean.getWebValues().remove("search_info");
    String search_info = Sup.serialize(bean);
    bean.setValue("search_info", search_info);
    bean.setValue("rooms", rooms);
    bean.setValue("users", users);
    bean.setValue("scheduleDaos", scheduleDaos);
    bean.setValue("userReserveDaos", userReserveDaos);
    bean.setValue("fileDaos", fileDaos);
    bean.setValue("linkUserReserveDaos", linkUserReserveDaos);
}

/**
 * 検索を行いbeanに格納する。.
 */
private void searchListReserve() throws AtareSysException
{
    WebBean bean = getWebBean();
    HashMap<String, String> errors;

    errors = inputCheck();
    if (errors.size() > 0)
    {
        bean.setValue("errors", errors);
        return;
    }
    // ユーザー情報の検索
    LinkedHashMap<String, String> sortKey = new LinkedHashMap<>();

    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount")))
    {
        bean.setValue("lineCount", "20");
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
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());
    String userInfoId = bean.value("user_info_id");
    bean.setValue("user_info_id", userInfoId);

    String selectedUserIds = getRequest().getParameter("selected_user_ids");
    String[] userIdsArray = selectedUserIds != null ? selectedUserIds.split(",") : new String[0];
    // ルーム情報の取得とセット
    RoomDao roomDao = new RoomDao();
    ArrayList<RoomDao> rooms = roomDao.getAllRooms();

    // ユーザー情報の取得とセット
    UserInfoDao dao = new UserInfoDao();
    dao.setUserIds(userIdsArray);
    ArrayList<UserInfoDao> dbResults = UserInfoDao.dbSelectList(dao, sortKey, daoPageInfo);
    // 取得したデータを一時的に user_info_id をキーとして Map に保存
    Map<String, UserInfoDao> userInfoMap = new HashMap<>();
    for (UserInfoDao userInfo : dbResults) {
      userInfoMap.put(userInfo.getUserInfoId(), userInfo); // getUserId() で user_info_id を取得
    }
    // userIdsArray の順番に並び替えた linkUserNames を作成
    ArrayList<UserInfoDao> linkUserNames = new ArrayList<>();
    for (String userId : userIdsArray) {
      if (userInfoMap.containsKey(userId)) {
        linkUserNames.add(userInfoMap.get(userId));
      }
    }
    
    // 予約の一覧を取得とセット
    UserReserveDao userReserveDao = new UserReserveDao();
    daoPageInfo.setLineCount(100);
    userReserveDao.setUserIds(userIdsArray);
    ArrayList<UserReserveDao> linkUserReserveDaos = UserReserveDao.dbSelectList(userReserveDao, sortKey, daoPageInfo);

    // ファイルを取得とセット
    FileDao fileDao = new FileDao();
    fileDao.setUserIds(userIdsArray);
    ArrayList<FileDao> fileDaos = FileDao.dbSelectList(fileDao, sortKey, daoPageInfo);

    
    bean.getWebValues().remove("search_info");
    String search_info = Sup.serialize(bean);
    bean.setValue("search_info", search_info);
    bean.setValue("rooms", rooms);
    bean.setValue("linkUserNames", linkUserNames);
    bean.setValue("linkUserReserveDaos", linkUserReserveDaos);
    bean.setValue("fileDaos", fileDaos);
}

/**
 * ソート順番を求める
 *
 * @return ソート順を格納した配列を返す
 */
private LinkedHashMap<String, String> sortKey()
{
    WebBean bean = getWebBean();
    String key = "";
    LinkedHashMap<String, String> sort_key = new LinkedHashMap<String, String>(); /* この配列にソートキーとソートオーダーを入れる */
    if (bean.value("sort_key").length() == 0 && bean.value("sort_key_old").length() == 0) return null;
    if (bean.value("sort_key_old").length() > 0)
    {
        if (bean.value("sort_key").length() > 0)
        {
            if (bean.value("sort_key").equals(bean.value("sort_key_old")))
            {
                // 同一ソートキー（フリップフロップ）
                key = bean.value("sort_key_old");
                if ("desc".equals(bean.value("sort_order")))
                {
                    sort_key.put(key, "asc");
                }
                else
                {
                    sort_key.put(key, "desc");
                }
            }
            else
            {
                // 新たなソートキー
                key = bean.value("sort_key");
                sort_key.put(key, "asc");
            }
        }
        else
        {
            // 引き継ぎ
            key = bean.value("sort_key_old");
            if ("asc".equals(bean.value("sort_order")))
            {
                sort_key.put(key, "asc");
            }
            else
            {
                sort_key.put(key, "desc");
            }
        }
    }
    else
    {
        // 初期値
        key = bean.value("sort_key");
        if ("asc".equals(bean.value("sort_order")))
        {
            sort_key.put(key, "asc");
        }
        else
        {
            sort_key.put(key, "desc");
        }
    }
    bean.setValue("sort_key", "");
    bean.setValue("sort_key_old", key);
    bean.setValue("sort_order", sort_key.get(key));
    return sort_key;
}

/**
 * 部屋情報を更新するメソッド
 * @throws AtareSysException エラー
 */
private void insertRoomInfo() throws AtareSysException {
    WebBean bean = getWebBean();
    RoomDao roomDao = new RoomDao();

    roomDao.setRoomId(bean.value("room_id"));
    roomDao.setInsertUserId(getLoginUserId());
    roomDao.setUpdateUserId(getLoginUserId());

    // `dbInsert`メソッドを呼び出す
    roomDao.dbInsert();
}
/**
 * データベースの内容を表示エリアに編集する。.
 *
 * @return boolean
 * @throws AtareSysException エラー
 */
private boolean setDb2Web1() throws AtareSysException
{
    WebBean bean = getWebBean();
    String reserveId = bean.value("main_key");
    ReserveDao dao = new ReserveDao();
    dao.setReserveId(reserveId); // 予約IDをDAOにセットして情報を取得
    if (!dao.dbSelect(reserveId)) {
      return false;
  }

    bean.setValue("reserve_id", dao.getReserveId());
    bean.setValue("user_info_id", dao.getUserInfoId());
    bean.setValue("room_id", dao.getRoomId());
    bean.setValue("reservation_date", dao.getReservationDate());
    bean.setValue("checkin_time", dao.getCheckinTime());
    bean.setValue("checkout_time", dao.getCheckoutTime());
    bean.setValue("input_text", dao.getInputText());
    bean.setValue("rgb_color", dao.getColor());
    bean.setValue("input_remark", dao.getInputRemark());
    bean.setValue("input_remark", dao.getInputRemark());
    bean.setValue("update_user_id", dao.getUpdateUserId());
    bean.setValue("user_reserve_id", dao.getUserReserveId());

    bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
    bean.setValue("input_info", Sup.serialize(dao));
    return true;
}
private ReserveDao setWebDaoInputInfo() throws AtareSysException {
  WebBean bean = getWebBean();
  ReserveDao reserveDao = new ReserveDao();
  // reservation_dateを変換: YYYY年MM月DD日 → YYYYMMDD
  String reservationDateStr = bean.value("reservation_date");
  SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
  SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMdd");
  Date reservationDate = null;
  try {
    reservationDate = inputDateFormat.parse(reservationDateStr);
  } catch (ParseException e) {
    // TODO 自動生成された catch ブロック
    e.printStackTrace();
  } // 入力フォーマットでパース
  String formattedReservationDate = outputDateFormat.format(reservationDate); // 出力フォーマットでフォーマット
  // checkin_time, checkout_timeを変換: HH:MM → HHMM
  String checkinTime = bean.value("checkin_time");
  String checkoutTime = bean.value("checkout_time");
  // 単純な文字列操作で":"を除去
  String formattedCheckinTime = checkinTime.replace(":", "");
  String formattedCheckoutTime = checkoutTime.replace(":", "");
  
  reserveDao.setRoomId(bean.value("room_id"));
  reserveDao.setReservationDate(formattedReservationDate);
  reserveDao.setCheckinTime(formattedCheckinTime);
  reserveDao.setCheckoutTime(formattedCheckoutTime);

  bean.setValue("input_info", Sup.serialize(reserveDao));
  return reserveDao;
}
/**
 * 部屋情報を削除するメソッド
 * @throws AtareSysException
 */
}