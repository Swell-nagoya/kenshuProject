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
import jp.swell.dao.RoomDao;
import jp.swell.dao.UserInfoDao;


public class UserMenuReserve extends ControllerBase {

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
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
  @Override
  public void doActionProcess() throws AtareSysException
  {
      WebBean bean = getWebBean();

      if ("UserMenuReserve".equals(bean.value("form_name")))
      {
          bean.trimAllItem();
          if("top".equals(bean.value("action_cmd"))) {
              searchList();
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

          else if ("clear".equals(bean.value("action_cmd")))
          {
              searchList();
          }

       // 部屋の情報を新規登録する条件を追加
          else if ("insertRoom".equals(bean.value("form_name")))
          {
              insertRoomInfo();
          }

          else if ("reserve".equals(bean.value("action_cmd")))
          {
            searchList();
          }
          else if ("edit".equals(bean.value("action_cmd")))
          {
              forward("UserMenuReserve.jsp");
              return; // メソッドを終了
          }
          {
              searchList();
          }

          forward("UserMenuReserve.jsp");
      }

      else if ("UserInfoDetail_1".equals(bean.value("form_name")) || "UserInfoDetail_2".equals(bean.value("form_name")) || "UserInfoDetail_3".equals(bean.value("form_name")))
      {
          setWebBeanFromSerialize(bean.value("search_info"));
          bean = getWebBean();
          searchList();
          forward("UserMenuReserve.jsp");
      }

      else
      {
          formInit();
          searchList();
          forward("UserMenuReserve.jsp");
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
  LinkedHashMap<String, String> sortKey = sortKey();
  UserInfoDao dao = new UserInfoDao();
  dao.setSearchFullName(bean.value("list_search_full_name"));
  dao.setSearchFullNameKana(bean.value("list_search_full_name_kana"));

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
  ArrayList<UserInfoDao> listData = UserInfoDao.dbSelectList(dao, sortKey, daoPageInfo);
  bean.setValue("lineCount", daoPageInfo.getLineCount());
  bean.setValue("pageNo", daoPageInfo.getPageNo());
  bean.setValue("recordCount", daoPageInfo.getRecordCount());
  bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

  bean.getWebValues().remove("search_info");
  String search_info = Sup.serialize(bean);
  bean.setValue("search_info", search_info);
  bean.setValue("list", listData);
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


//部屋情報を更新するメソッド
private void insertRoomInfo() throws AtareSysException {
  WebBean bean = getWebBean();
  RoomDao roomDao = new RoomDao();

  roomDao.setRoomId(bean.value("room_id"));
  roomDao.setInsertUserId(getLoginUserId());
  roomDao.setUpdateUserId(getLoginUserId());

  // `dbInsert`メソッドを呼び出す
  roomDao.dbInsert();
}
}