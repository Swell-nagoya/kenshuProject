/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除ためのコントローラクラス
 * ファイル名　　：RoomDetail.java
 * クラス名　　　：RoomDetail
 * 概要　　　　　：room 部屋情報テーブルデータを登録・更新・削除ためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.RoomDao;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除ためのコントローラクラス
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
        setLoginNeeds(true); // この処理にはログインが必要かどうか
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
          /*
           * bean.setValue("request_name", "修正");
          if (beforeName == null || beforeName.trim().isEmpty()) {
              beforeName = roomName;
              bean.setValue("before_name", beforeName);
          }
          */
          
          bean.setValue("before_name", beforeName);
          bean.setValue("room_name", roomName);
          
          if ("RoomList".equals(formName))
          {
              if ("go_next".equals(actionCmd)) 
              {
                  if ("ins".equals(requestCmd)) 
                  {
                      bean.setValue("input_info", Sup.serialize(new RoomDao()));
                      bean.setValue("request_name", "登録");
                      forward("RoomDetail.jsp");
                  }
                  else if ("update".equals(requestCmd)) 
                  {
                      setRoom();
                      bean.rtrimAllItem();
                    
                      if (!setDb2Web())
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("RoomList.jsp");
                      }
                      else
                      {
                          bean.setValue("request_name", "修正");
                          bean.setValue("before_name", beforeName);
                          forward("RoomDetail.jsp");
                      }
                  } 
                  else if ("delete".equals(requestCmd)) 
                  {
                   if (!setDb2Web()) 
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("RoomList.jsp");
                      }
                      else
                      {

                   	   bean.setMessage("この部屋を削除します。よろしいですか？");
                       bean.setValue("request_name", "削除");
                       bean.setValue("room_name", roomName);
                       forward("RoomDetail.jsp");
                      }
                  }
                  else 
                  {
                      forward("RoomList.jsp");
                  }
              }
          } 
          else if ("RoomDetail".equals(formName))
          {
              if ("go_next".equals(actionCmd))
              {
                  if ("ins".equals(requestCmd))
                  {
                   RoomDao dao = setWeb2Dao2InputInfo();

             //      insUserPass();
                  	insRoomId();
                   bean.rtrimAllItem();
                   if (inputCheck(dao)) 
                   {
                  
                       bean.setMessage("この内容で登録します。よろしいですか？");
                       bean.setValue("request_name", "登録確定");
                       bean.setValue("room_name", roomName);
                       forward("RoomDetail.jsp");
                   }
                   else 
                   {
                       bean.setError("入力内容に誤りがあります");
                       forward("RoomDetail.jsp");
                   }
                   
                  }
                  else if ("update".equals(requestCmd))
                  {
                      
                      setRoom();
                      bean.rtrimAllItem();
                      RoomDao dao = setWeb2Dao2InputInfo();
                      if (inputCheck(dao)) 
                      {

                          bean.setMessage("この内容で修正します。よろしいですか？");
                          bean.setValue("request_name", "修正確定");
                          forward("RoomDetail.jsp"); 
                      }
                      else 
                      {
                        bean.setError("入力内容に誤りがあります");
                        forward("RoomDetail.jsp");
                      }
                      
                  }
                  else if ("insConfirm".equals(requestCmd))
                  {
                      dbRegistration();
                  }
                  else if ("updateConfirm".equals(requestCmd))
                  {

                      if (checkDataMatching())
                      {
                          setInputInfo2Dao2Web();
                          setWeb2Dao2InputInfo();
                          dbEdit();
                      }
                      else 
                      {
                          bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                          setDb2Web();
                          forward("RoomDetail.jsp");
                      }
                  }
                  else if ("deleteConfirm".equals(requestCmd))
                  {
                      
                      if (checkDataMatching())
                      {
                         setInputInfo2Dao2Web();
                         setWeb2Dao2InputInfo();
                         dbDelete();
                      }
                      else 
                       {
                         bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                         setDb2Web();
                         forward("RoomDetail.jsp");
                       }
                  }
              }
              else if ("return".equals(actionCmd))
              {

               if ("insConfirm".equals(bean.value("request_cmd"))) {
                bean.setValue("request_cmd", "ins");
                bean.setValue("request_name", "登録");
                setInputInfo2Dao2Web();
                forward("RoomDetail.jsp");
                
               }
                else if ("updateConfirm".equals(bean.value("request_cmd"))) 
               {
                    bean.setValue("request_cmd", "update");
                    bean.setValue("request_name", "修正");
                    setInputInfo2Dao2Web();
                    setWeb2Dao2InputInfo();
                    forward("RoomDetail.jsp");
                
               } else {
                forward("RoomList.do");
               }
              }
              redirect("RoomList.do");
          }
          else
          {
              bean.setValue("request_name", "修正");
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
     * データベースから指定されたレコードを削除メソッド
     * @throws AtareSysException
     */
    public void dbDelete() throws AtareSysException
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

        if (
             (roomName == "" && beforeName == "") != true &&
        		   (roomName.equalsIgnoreCase(beforeName))
        ) {
            errors.put("room_name_duplicate", "部屋名が以前と同じです。別の名前を入力してください。");
        }

        return errors.isEmpty();
    }

    /**
     * 修正・削除をする前にそのレコードが最初に読み込んだ内容と一緒かどうかをチェックする。 途中で修正されていた場合はエラーを返す。
     * ここまで、厳密なチェックがいらない場合は、この関数の処理を消して、return true にすればよい。
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private boolean checkDataMatching() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        String mainKey = bean.value("main_key");//RoomIdの取得
        if (!dao.dbSelect(mainKey))
        {
            return false;
        }
        return Sup.serializeIsEquals(bean.value("select_info"), dao);
    }
    
    
    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private RoomDao setWeb2Dao2InputInfo() throws AtareSysException
    { 
    	   // 現在の日時を取得し、DBに代入
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
     
        String formattedDateTime = now.format(formatter);
     
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        String mainKey = bean.value("main_key");//RoomIdの取得
        dao.setRoomId(mainKey);
        dao.setRoomName(bean.value("room_name"));
        
        String requestCmd = bean.value("request_cmd");
        String loggedInUserId = getLoginUserId();
        
        // 入力完了時に現在の時刻を代入（user_id, insert_date, update_user_id , update_date）
        if ("insConfirm".equals(requestCmd))
        {
        	
          dao.setInsertUserId(loggedInUserId);
          dao.setInsertDate(formattedDateTime);
          dao.setUpdateUserId(loggedInUserId);
          dao.setUpdateDate(formattedDateTime);
          
        // 更新完了時に現在の時刻を代入（update_user_id , update_date）
        } 
        else if ("updateConfirm".equals(requestCmd)) 
        {
        	System.out.println(formattedDateTime + "日時");
         System.out.println("ログイン中のユーザー: " + loggedInUserId);
         dao.setUpdateUserId(loggedInUserId);
         dao.setUpdateDate(formattedDateTime);
        } 
        else
        {
        	
        }

        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }
    
    

    /**
     * 既に設定されているパスワードを取得してbeanにセットする
     *  
     * @return
     * @throws AtareSysException
     */

    /*
    private boolean insUserPass() throws AtareSysException
    {
        
    }
    */
    
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
    private void setInputInfo2Dao2Web() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = (RoomDao) Sup.deserialize(bean.value("input_info"));
     //   bean.setValue("room_id", dao.getRoomId());
        bean.setValue("room_name", dao.getRoomName());
     /*   bean.setValue("insert_date", dao.getInsertDate());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("update_date", dao.getUpdateDate());
        bean.setValue("update_user_id", dao.getUpdateUserId());
        */
    }
    

    /**
     * 既に設定されているroom_idを取得してbeanにセットする
     *  
     * @return
     * @throws AtareSysException
     */
    private boolean insRoomId() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        String roomId = GetNumber.getNumberChar("room_id"); // ユーザーIDを新規作成
        bean.setValue("room_id", roomId);
        bean.setValue("input_info", Sup.serialize(dao));  // DAOオブジェクトをシリアライズしてWebBeanに保存
        return true;
    }
    
    private boolean setRoom() throws AtareSysException
    {
        WebBean bean = getWebBean();
        RoomDao dao = new RoomDao();
        if (!dao.dbSelect(bean.value("main_key")))
        {
            return false;
        }
        bean.setValue("room_id", dao.getRoomId());
        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
   
        return true;
    }
    
    
}
