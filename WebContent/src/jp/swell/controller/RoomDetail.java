/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * ファイル名　　：RoomDetail.java
 * クラス名　　　：RoomDetail
 * 概要　　　　　：room 部屋情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.RoomDao;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class RoomDetail extends ControllerBase
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
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
    /**
     * jp.swell.cloudbiz.common.ControllerBase のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     * ここで、コントローラの処理を記述する.
     * @throws Exception エラー
     */
    @Override
    public void doActionProcess() throws AtareSysException
    {
      WebBean bean = getWebBean();

      try {
          String formName = bean.value("form_name");
          String actionCmd = bean.value("action_cmd");
          String requestCmd = bean.value("request_cmd");
          String mainKey = bean.value("main_key");
          String roomName = bean.value("room_name");
          String beforeName = bean.value("before_name");
          RoomDao dao = setWeb2Dao2InputInfo();
          bean.setValue("request_name", "修正する");
          if (beforeName == null || beforeName.trim().isEmpty()) {
              beforeName = roomName;
              bean.setValue("before_name", beforeName);
          }
          bean.setValue("before_name", beforeName);
          bean.setValue("room_name", roomName);
          if ("RoomDetail".equals(formName))
          {
              if ("go_next".equals(actionCmd))
              {
                  if ("ins".equals(requestCmd))
                  {
                      bean.setMessage("この内容で登録します。よろしいですか？");
                      bean.setValue("request_name", "登録する");
                      bean.setValue("room_name", roomName);
                      forward("RoomDetail_2.jsp");
                  }
                  else if ("update".equals(requestCmd))
                  {
                      bean.setMessage("この内容で修正します。よろしいですか？");
                      bean.setValue("request_name", "修正する");
                      bean.setValue("before_name", beforeName);
                      bean.setValue("room_name", roomName);
                      forward("RoomDetail_2.jsp");
                  }
              }
              else if ("return".equals(actionCmd))
              {
                  forward("RoomList.do");
              }
          }
          else if ("RoomList".equals(formName))
          {
              if ("go_next".equals(actionCmd)) 
              {
                  if ("ins".equals(requestCmd)) 
                  {
                      bean.setValue("request_name", "登録する");
                      forward("RoomDetail.jsp");
                  } 
                  else if ("update".equals(requestCmd)) 
                  {
                      if (!setDb2Web())
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("RoomList.jsp");
                      }
                      else
                      {
                          bean.setValue("request_name", "修正する");
                          bean.setValue("before_name", beforeName);
                          forward("RoomDetail.jsp");
                      }
                  } 
                  else if ("deletef".equals(requestCmd)) 
                  {
                      if (!dao.dbSelect(mainKey))
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("RoomList.jsp");
                      }
                      else
                      {
                          bean.setMessage("この部屋を削除します。よろしいですか？");
                          bean.setValue("request_name", "削除する");
                          bean.setValue("room_name", roomName);
                          forward("RoomDetail_2.jsp");
                      }
                  }
              }
          }
          else if ("RoomDetail_2".equals(formName))
          {
              if ("go_next".equals(actionCmd))
              {
                  if ("insEnter".equals(requestCmd))
                  {
                      dbRegistration();
                  }
                  else if ("updateEnter".equals(requestCmd))
                  {
                      dbEdit();
                  }
                  else if ("deleteEnter".equals(requestCmd))
                  {
                      dbDeletef();
                  }
              }
              redirect("RoomList.do");
          }
          else
          {
              bean.setValue("request_name", "修正する");
              bean.setValue(requestCmd, "update");
              if (!setDb2Web()) {
                  bean.setError("データの取得に失敗しました");
              }
              bean.setMessage("以下の項目を修正してください。");
              forward("RoomList.jsp");
          }
      } catch (Exception e) {
          bean.setError("処理中にエラーが発生しました: " + e.getMessage());
          forward("ErrorPage.jsp");
      }
    }
    /**
     * 新規登録の場合の処理
     * @throws AtareSysException
     */
    public void dbRegistration() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        RoomDao dao = setWeb2Dao2InputInfo();
        if (inputCheck(dao))
        {
            if(signUp())
            {
                redirect("RoomList.do");
            }
            else
            {
                bean.setError("登録に失敗しました");
                forward("RoomDetail.jsp");
            }
        }
        else
        {
            bean.setError("入力内容に誤りがあります");
            forward("RoomDetail.jsp");
        }
    }
    /**
     * 修正の場合
     * @throws AtareSysException
     */
    public void dbEdit() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        RoomDao dao = setWeb2Dao2InputInfo();
        String mainKey = bean.value("main_key");//RoomIdの取得
        if (inputCheck(dao))
        {
            try {
                DbBase.dbBeginTran();
                dao.dbUpdate(mainKey);
                DbBase.dbCommitTran();
                redirect("RoomList.do");
            } catch (Exception e) {
                DbBase.dbRollbackTran();
                forward("RoomDetail.jsp");
            }
        }
        else
        {
            String beforeName = bean.value("before_name").trim();
            bean.setValue("room_name", beforeName);
            bean.setValue("before_name", beforeName);

            bean.setError("入力項目にエラーがあります。下記事項をご確認ください。");
            forward("RoomDetail.jsp");
        }
    }
    
    /**
     * データベースから指定されたレコードを削除するメソッド
     * @throws AtareSysException
     */
    public void dbDeletef() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        RoomDao dao = setWeb2Dao2InputInfo();
        String mainKey = bean.value("main_key");//RoomIdの取得
            try {
                DbBase.dbBeginTran();
                dao.dbDelete(mainKey);
                DbBase.dbCommitTran();
                redirect("RoomList.do");
            } catch (Exception e) {
                DbBase.dbRollbackTran();
                forward("RoomDetail.jsp");
            }
    }
    /**
     * データベースの内容を表示エリアに編集する。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    private boolean setDb2Web() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        String mainKey = bean.value("main_key");//RoomIdの取得
        if (!dao.dbSelect(mainKey))
        {
            return false;
        }
        bean.setValue("room_id", dao.getRoomId());
        bean.setValue("room_name", dao.getRoomName());
        bean.setValue("insert_date", dao.getInsertDate());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("update_date", dao.getUpdateDate());
        bean.setValue("update_user_id", dao.getUpdateUserId());

        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }
    
    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     * @throws AtareSysException
     */
    private boolean inputCheck(RoomDao pRoomDao) throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        String roomName = bean.value("room_name").trim();
        String beforeName = bean.value("before_name").trim(); // ← hidden から来る

        if (roomName.length() == 0) {
            errors.put("room_name_empty", "部屋名を入力してください。");
        }
        if (roomName.equalsIgnoreCase(beforeName)) {
            errors.put("room_name_duplicate", "部屋名が以前と同じです。別の名前を入力してください。");
        }

        return errors.isEmpty();
    }
   
   
    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private RoomDao setWeb2Dao2InputInfo() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        dao.setRoomName(bean.value("room_name"));

        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }
    /**
     * 部屋登録処理を行うメソッド
     * @return 登録が成功：true、登録が失敗：false
     * @throws AtareSysException
     */
    
    private boolean signUp() throws AtareSysException {
      RoomDao dao = setWeb2Dao2InputInfo(); // setWebDaoInputInfoメソッドを呼び出してreserveDaoを設定する

        try {
            DbBase.dbBeginTran();
            dao.dbInsert();
            DbBase.dbCommitTran();
            return true;
        } catch (Exception e) {
            DbBase.dbRollbackTran();
        return false;
      }
    }
    
    /**
     * input_infoフィールドからクラスを取り出し、画面の項目に値を設定する
     *
     * @return なし
     * @throws AtareSysException
     */
    @SuppressWarnings("unused")
    private void setInputInfo2Dao2Web() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = (RoomDao) Sup.deserialize(bean.value("input_info"));
        bean.setValue("room_id", dao.getRoomId());
        bean.setValue("room_name", dao.getRoomName());
        bean.setValue("insert_date", dao.getInsertDate());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("update_date", dao.getUpdateDate());
        bean.setValue("update_user_id", dao.getUpdateUserId());
    }
}
