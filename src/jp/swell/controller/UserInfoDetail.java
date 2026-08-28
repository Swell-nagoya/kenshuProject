/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * ファイル名　　：UserInfoDetail.java
 * クラス名　　　：UserInfoDetail
 * 概要　　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.security.SecureRandom;
import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;
import jp.swell.validator.UserInfoValidator;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class UserInfoDetail extends ControllerBase
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
    public void doActionProcess() throws AtareSysException {
      WebBean bean = getWebBean();
      

      try {
          if ("ViewUserList".equals(bean.value("form_name"))) 
          {
              if ("go_next".equals(bean.value("action_cmd"))) 
              {
                  if ("ins".equals(bean.value("request_cmd"))) 
                  {
                      bean.setValue("input_info", Sup.serialize(new UserInfoDao()));
                      bean.setValue("request_name", "登録");
                      forward("UserInfoDetail.jsp");
                  } 
                  else if ("update".equals(bean.value("request_cmd"))) 
                  {
                      if (!setDb2Web()) 
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("ViewUserList.jsp");
                      }
                      else 
                      {
                          bean.setValue("request_name", "修正");
                          forward("UserInfoDetail.jsp");
                      }
                  }
                  else if ("delete".equals(bean.value("request_cmd"))) 
                  {
                      if (!setDb2Web()) 
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("ViewUserList.jsp");
                      } 
                      else
                      {
                          bean.setValue("request_name", "削除");
                          bean.setMessage("退職予定日を入力してください。");
                          forward("UserInfoDetail.jsp");
                      }
                  }
                  else if ("check".equals(bean.value("request_cmd"))) 
                  {
                      if (!setDb2Web()) 
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("ViewUserList.jsp");
                      } 
                      else 
                      {
                          bean.setValue("request_name", "メール送信");
                          forward("UserInfoDetail.jsp");
                      }
                  }
                  else if ("access".equals(bean.value("request_cmd"))) 
                  {
                      if (!setDb2Web()) 
                      {
                          bean.setError("データの取得に失敗しました");
                          forward("ViewUserList.jsp");
                      } 
                      else 
                      {
                          setWeb2Dao2InputInfo();
                          forward("Schedule.do");
                      }
                  }
                  else 
                  {
                      forward("ViewUserList.jsp");
                  }
              }
              else  if ("menu".equals(bean.value("action_cmd"))) 
              {
                redirect("MenuAdmin.do");
              }
          }
          
          else if ("UserInfoDetail".equals(bean.value("form_name"))) 
          {
              if ("go_next".equals(bean.value("action_cmd"))) 
              {
                  
                  if ("ins".equals(bean.value("request_cmd"))) 
                  {
                      insUserPass();
                      bean.rtrimAllItem();
                      UserInfoDao dao = setWeb2Dao2InputInfo();
                      if (inputCheck(dao)) 
                      {
                          bean.setMessage("この内容で登録します。よろしいですか？");
                          bean.setValue("request_name", "登録確定");
                          forward("UserInfoDetail.jsp"); 
                      }
                      else 
                      {
                          bean.setError("入力内容に誤りがあります");
                          forward("UserInfoDetail.jsp");
                      }
                  } 
                  else if ("update".equals(bean.value("request_cmd"))) 
                  {
                      setUser();
                      bean.rtrimAllItem();
                      UserInfoDao dao = setWeb2Dao2InputInfo();
                      if (inputCheck(dao)) 
                      {
                        
                          bean.setMessage("この内容で修正します。よろしいですか？");
                          bean.setValue("request_name", "修正確定");
                          forward("UserInfoDetail.jsp"); 
                      }
                      else 
                      {
                        bean.setError("入力内容に誤りがあります");
                        forward("UserInfoDetail.jsp");
                      }
                  }
                  else if ("delete".equals(bean.value("request_cmd"))) 
                  {
                      setInputInfo2Dao2WebDelete();
                      bean.rtrimAllItem();
                      UserInfoDao dao = setWeb2Dao2InputInfo();
                      if (inputCheck(dao)) 
                      {
                          bean.setMessage("退職予定日を確定します。よろしいですか？");
                          bean.setValue("request_name", "確定");
                          forward("UserInfoDetail.jsp");  
                      }
                      else 
                      {
                          bean.setError("入力内容に誤りがあります");
                          forward("UserInfoDetail.jsp"); 
                      }
                  }
                  else if ("insConfirm".equals(bean.value("request_cmd"))) 
                  {
                   setInputInfo2Dao2Web();
                   
                   if (
                   		(signUp() == false) || 
                   		(scheduleInsert() == false)
                    ) {
                    forward("ViewUserList.jsp");
                    return;
                   }

                   redirect("ViewUserList.do");
                  }
                  else if ("updateConfirm".equals(bean.value("request_cmd"))) 
                  {
                       if (checkDataMatching())
                       {
                           setInputInfo2Dao2Web();
                           dbEdit();
                       }
                       else 
                       {
                           bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                           setDb2Web();
                           forward("UserInfoDetail.jsp");
                       }
                  }
                  else if ("deleteConfirm".equals(bean.value("request_cmd"))) 
                  {
                    if (checkDataMatching())
                    {
                       setInputInfo2Dao2Web();
                       delete();
                    }
                    else 
                     {
                       bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                       setDb2Web();
                       forward("UserInfoDetail.jsp");
                     }
                   }
              }
              else if ("return".equals(bean.value("action_cmd"))) 
              {
               if ("insConfirm".equals(bean.value("request_cmd"))) 
               {
                   bean.setValue("request_cmd", "ins");
                   bean.setValue("request_name", "登録");
                   setInputInfo2Dao2Web();
                   forward("UserInfoDetail.jsp");
               }
               else if ("updateConfirm".equals(bean.value("request_cmd"))) 
               {
                   bean.setValue("request_cmd", "update");
                   bean.setValue("request_name", "修正");
                   setInputInfo2Dao2Web();
                   setWeb2Dao2InputInfo();
                   forward("UserInfoDetail.jsp");
               }
               else if ("deleteConfirm".equals(bean.value("request_cmd"))) 
               {   
               	   bean.setValue("request_cmd", "delete");
                   bean.setValue("request_name", "削除");
                   setInputInfo2Dao2Web();
                   setWeb2Dao2InputInfo();
                   forward("UserInfoDetail.jsp");
               }
               else if ("send".equals(bean.value("request_cmd"))) 
               {
                   redirect("ViewUserList.do");
               }else {

                forward("ViewUserList.do");
               }
              }
          }
          else 
          {
              bean.setValue("request_name", "修正");
              bean.setValue("request_cmd", "update");
              if (!setDb2Web()) {
                  bean.setError("データの取得に失敗しました");
              }
              bean.setMessage("以下の項目を修正してください。");
              forward("UserInfoDetail.jsp");
          }
      }
      catch (Exception e) 
      {
          bean.setError("処理中にエラーが発生しました: " + e.getMessage());
          forward("ErrorPage.jsp");
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
        UserInfoDao dao = new UserInfoDao();
        if (!dao.dbSelect(bean.value("main_key")))
        {
            return false;
        }
        bean.setValue("user_info_id", dao.getUserInfoId());
        bean.setValue("state_flg", dao.getStateFlg());
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("maiden_name", dao.getMaidenName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("maiden_name_kana", dao.getMaidenNameKana());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("memail", dao.getMemail());
        bean.setValue("password_user", dao.getPasswordUser());
        bean.setValue("password", dao.getPassword());
        bean.setValue("admin", dao.getAdmin());
        bean.setValue("leave_date", dao.getLeaveDate());

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
    private boolean inputCheck(UserInfoDao pUserInfoDao) throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        UserInfoValidator validator = new UserInfoValidator();

        
        if ("ins".equals(bean.value("request_cmd")) || "update".equals(bean.value("request_cmd"))) 
        {
        	   // 氏名  エラーチェック
            errors.putAll(validator.nameCheck(bean));
        	   // 氏名 （かな）エラーチェック
            errors.putAll(validator.nameKanaCheck(bean));
            // ミドルネーム エラーチェック
            errors.putAll(validator.middleNameCheck(bean));
            // 旧姓 エラーチェック
            errors.putAll(validator.maidenNameCheck(bean));
            // 任意ID エラーチェック
            errors.putAll(validator.insertUserIdCheck(bean,pUserInfoDao));
            // メールアドレスエラーチェック
            errors.putAll(validator.memailCheck(bean,pUserInfoDao));
            // ユーザー区分チェック
            errors.putAll(validator.adminCheck(bean));
        }
        else if ("delete".equals(bean.value("request_cmd"))) 
        {
            // 退職予定日 エラーチェック
            errors.putAll(validator.leaveDateCheck(bean));
        }
            
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    /**
     * データベース処理を行う。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    @SuppressWarnings("unused")
    private void processDb() throws AtareSysException
    {
        WebBean bean = getWebBean();
        try
        {
            DbBase.dbBeginTran();
            if ("update".equals(bean.value("request_cmd")))
            {
                UserLoginInfo userInfo = (UserLoginInfo) getLoginInfo();
                userInfo.resetUserInfo();
                setLoginInfo(userInfo);
                bean.setMessage("修正を完了しました。");
            }
            DbBase.dbCommitTran();
        } catch (AtareSysException e)
        {
            DbBase.dbRollbackTran();
            e.printStackTrace();
            throw new AtareSysException(e.getMessage());
        }
    }

 // ランダム英数字生成用のメソッド
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
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
      UserInfoDao dao = new UserInfoDao();
      if (!dao.dbSelect(bean.value("main_key")))
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
    private UserInfoDao setWeb2Dao2InputInfo() throws AtareSysException {
      WebBean bean = getWebBean();
      UserInfoDao dao = new UserInfoDao();

      dao.setUserInfoId(bean.value("user_info_id"));
      dao.setLastName(bean.value("last_name"));
      dao.setMiddleName(bean.value("middle_name"));
      dao.setFirstName(bean.value("first_name"));
      dao.setMaidenName(bean.value("maiden_name"));
      dao.setLastNameKana(bean.value("last_name_kana"));
      dao.setMiddleNameKana(bean.value("middle_name_kana"));
      dao.setFirstNameKana(bean.value("first_name_kana"));
      dao.setMaidenNameKana(bean.value("maiden_name_kana"));
      dao.setInsertUserId(bean.value("insert_user_id"));
      dao.setMemail(bean.value("memail"));
      dao.setPasswordUser(bean.value("password_user"));
      dao.setAdmin(bean.value("admin"));
      dao.setPassword(bean.value("password"));
      dao.setLeaveDate(bean.value("leave_date"));
      
      bean.setValue("input_info", Sup.serialize(dao));  // DAOオブジェクトをシリアライズしてWebBeanに保存
      return dao;
  }

    
    private boolean signUp() throws AtareSysException {
      UserInfoDao dao = setWeb2Dao2InputInfo();

      try {
        // 入力内容をデータベースに保存
        dao.dbInsert();
        
        return true;
        
      } catch (Exception e) {
        return false;
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
        UserInfoDao dao = setWeb2Dao2InputInfo();
        String userInfoId = bean.value("user_info_id");//userIdの取得
           
        try {
            DbBase.dbBeginTran();
            dao.dbUpdate(userInfoId);
            DbBase.dbCommitTran();
            redirect("ViewUserList.do");
        } catch (Exception e) {
            DbBase.dbRollbackTran();
            forward("ViewUserList.jsp");
        }
        
    }

    /**
     * 削除の場合
     * @throws AtareSysException
     */
    public void delete() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = setWeb2Dao2InputInfo();
        String userInfoId = bean.value("user_info_id");//userIdの取得
        String leaveDate = bean.value("leave_date");     // leave_dateの取得

        try {
          dao.dbUpdate(userInfoId);
          if (leaveDate == null || leaveDate.trim().isEmpty()) {
            dao.dbCancelDelete(userInfoId);
            redirect("ViewUserList.do");
          }
          else {
            dao.dbDelete(userInfoId);
            redirect("ViewUserList.do");
          }
        }catch (Exception e) {
          forward("ViewUserList.do");
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
        UserInfoDao dao = (UserInfoDao) Sup.deserialize(bean.value("input_info"));
        bean.setValue("user_info_id", dao.getUserInfoId());
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("maiden_name", dao.getMaidenName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("maiden_name_kana", dao.getMaidenNameKana());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("memail", dao.getMemail());
        bean.setValue("password_user", dao.getPasswordUser());
        bean.setValue("password", dao.getPassword());
        bean.setValue("admin", dao.getAdmin());
        bean.setValue("leave_date", dao.getLeaveDate());
    }
    
    private void setInputInfo2Dao2WebDelete() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = (UserInfoDao) Sup.deserialize(bean.value("input_info"));
        bean.setValue("user_info_id", dao.getUserInfoId());
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("maiden_name", dao.getMaidenName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("maiden_name_kana", dao.getMaidenNameKana());
        bean.setValue("insert_user_id", dao.getInsertUserId());
        bean.setValue("memail", dao.getMemail());
        bean.setValue("password_user", dao.getPasswordUser());
        bean.setValue("password", dao.getPassword());
        bean.setValue("admin", dao.getAdmin());
    }
    
    /**
     * 既に設定されているパスワードを取得してbeanにセットする
     *  
     * @return
     * @throws AtareSysException
     */
    private boolean insUserPass() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = new UserInfoDao();
        String userInfoId = GetNumber.getNumberChar("user_info"); // ユーザーIDを新規作成
        bean.setValue("user_info_id", userInfoId);
        
        String password = generateRandomPassword(8); // ランダムパスワードを新規作成
        bean.setValue("password", password);
        
        bean.setValue("input_info", Sup.serialize(dao));  // DAOオブジェクトをシリアライズしてWebBeanに保存
        return true;
    }
    
    private boolean setUser() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = new UserInfoDao();
        if (!dao.dbSelect(bean.value("main_key")))
        {
            return false;
        }
        bean.setValue("user_info_id", dao.getUserInfoId());
        bean.setValue("password", dao.getPassword());
        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }
    
    /**
     * DAOクラスに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private ScheduleDao setWeb2Dao2() throws AtareSysException {
      WebBean bean = getWebBean();
      ScheduleDao scheduledao = new ScheduleDao();

      scheduledao.setMainUserId(bean.value("user_info_id"));
      scheduledao.setLinkUserId(bean.value("user_info_id"));
      scheduledao.setPriority("3");

      bean.setValue("input_info", Sup.serialize(scheduledao)); // DAOオブジェクトをシリアライズしてWebBeanに保存
      return scheduledao;
    }
    
    private boolean scheduleInsert() throws AtareSysException {
      ScheduleDao scheduledao = setWeb2Dao2();

      try {
        // 入力内容をデータベースに保存
        scheduledao.dbInsert();
        
        return true;
        
      } catch (Exception e) {
        return false;
      }
    }
    
    
}