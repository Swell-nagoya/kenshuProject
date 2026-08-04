package jp.swell.controller;

/**
 * Servlet implementation class UserMenuResarve
 */

import java.util.ArrayList;
import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ReserveDao;
import jp.swell.dao.RoomDao;

public class RoomYoyakuList extends ControllerBase {
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
  public void doActionProcess() throws AtareSysException {
    WebBean bean = getWebBean();
    if ("RoomYoyakuList".equals(bean.value("form_name"))) {
      bean.trimAllItem();
      // 部屋の予約確認
      if ("reservationConfirmation".equals(bean.value("action_cmd")))
      {
      	
        searchList();
        forward("Reserve_Room.jsp");
        return; // メソッドを終了
      }

    }
    else {
      forward("UserMenuHome.do");
    }
  }


  /**
   * 入力チェックを行う。.
   *
   * @param dao
   *
   * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
   */
  private boolean inputCheck(ReserveDao dao) {

    WebBean bean = getWebBean();
    HashMap<String, String> errors = bean.getItemErrors();
   // reservation_dateがYYYY年MM月DD日の形式であるかをチェック
    String reservationDate = bean.value("reservation_date");
    if (!reservationDate.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
        errors.put("reservation_date", "YYYY年MM月DD日形式で入力してください");
    }

//    //checkin_timeがHH:MMの形式であるかをチェック
//    String checkinTime = bean.value("checkin_time");
//    if (!checkinTime.matches("^\\d{2}:\\d{2}$")) {
//        errors.put("checkin_time", "HH:MM形式で入力してください");
//    }
//
//    // checkout_timeがHH:MMの形式であるかをチェック
//    String checkoutTime = bean.value("checkout_time");
//    if (!checkoutTime.matches("^\\d{2}:\\d{2}$")) {
//        errors.put("checkout_time", "HH:MM形式で入力してください");
//    }

    // 会議室が選択されているかをチェック
    String roomId = bean.value("room_id");
    if (roomId == null || roomId.isEmpty() || "会議室選択".equals(roomId)) {
      errors.put("room_id", "会議室を選択してください。");
    }
    // 予約者が選択されているかをチェック
    String userId = bean.value("user_info_id");
    if (userId == null || userId.isEmpty() || "選択してください".equals(userId)) {
      errors.put("user_info_id", "予約者を選択してください。");
    }
    // 色が選択されているかをチェック
    String colorId = bean.value("rgb_color");
    if (colorId == null || colorId.isEmpty() || "#87ceeb".equals(colorId)) {
      errors.put("rgb_color", "色を選択してください。");
    }

    if (errors.size() > 0) {
      return false;
    }
    return true;
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

    ReserveDao reserveDao = new ReserveDao();
    reserveDao.setRoomId(bean.value("room_id"));
    reserveDao.setRoomName(bean.value("room_name"));
    
    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount"))) {
      bean.setValue("lineCount", "20");
    }
    daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
    SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "UserMenuHome", "lineCount", bean.value("lineCount"));
    if (!Validate.isInteger(bean.value("pageNo"))) {
      daoPageInfo.setPageNo(1);
    } else {
      daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
    }


    ArrayList<ReserveDao> listData = ReserveDao.dbSelectListRoomYoyaku(reserveDao, daoPageInfo);

    
    
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

    // ルーム情報の取得とセット
    RoomDao roomDao = new RoomDao();
    ArrayList<RoomDao> rooms = roomDao.getAllRooms();

    bean.getWebValues().remove("search_info");
    String search_info = Sup.serialize(bean);
    bean.setValue("search_info", search_info);
    bean.setValue("rooms", rooms);
    bean.setValue("list", listData);
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
}
