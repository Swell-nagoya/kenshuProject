package jp.swell.controller;

/**
 * Servlet implementation class UserMenuResarve
 */

import java.util.ArrayList;

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

     if ("reservationConfirmation".equals(bean.value("action_cmd")))
      {
        searchList();
        forward("RoomYoyakuList.jsp");
        return; // メソッドを終了
      }

    } else 
     if ("RoomYoyaku".equals(bean.value("form_name"))) {

      if ("next".equals(bean.value("action_cmd")))
      {
          bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
          searchList();
          forward("RoomYoyakuList.jsp");
      }
      else if ("jump".equals(bean.value("action_cmd")))
      {
          searchList();
          forward("RoomYoyakuList.jsp");
      }
      else if ("prior".equals(bean.value("action_cmd")))
      {
          bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
          searchList();
          forward("RoomYoyakuList.jsp");
      }
     } else {
      forward("RoomYoyakuList.do");
    }
  }


  /**
   * 検索を行いbeanに格納する。.
   */
  private void searchList() throws AtareSysException {
    WebBean bean = getWebBean();


    ReserveDao reserveDao = new ReserveDao();
    reserveDao.setRoomId(bean.value("main_key"));
    reserveDao.setRoomName(bean.value("room_name"));
    
    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount"))) {
      bean.setValue("lineCount", "20");
    }

    daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
    SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "UserMenuHome", "lineCount", bean.value("lineCount"));
    if (!Validate.isInteger(bean.value("pageNo"))) {
      daoPageInfo.setPageNo(20);
    } else {
      daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
    }


    ArrayList<ReserveDao> listData = ReserveDao.dbSelectListRoomYoyaku(reserveDao, daoPageInfo);

    
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());
    bean.setValue("lineCount", 1);

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
}
