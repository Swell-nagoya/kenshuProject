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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

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
                      forward("UserInfoDetail_1.jsp");
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
                          forward("UserInfoDetail_1.jsp");
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
                          bean.setMessage("退職予定日を入力してください。");
                          forward("UserInfoDetail_2.jsp");
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
                          forward("UserInfoDetail_3.jsp");
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
          
          else if ("UserInfoDetail_1".equals(bean.value("form_name"))) 
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
                          bean.setValue("request_name", "登録");
                          forward("UserInfoDetail_3.jsp"); 
                      }
                      else 
                      {
                          bean.setError("入力内容に誤りがあります");
                          forward("UserInfoDetail_1.jsp");
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
                          bean.setValue("request_name", "修正");
                          forward("UserInfoDetail_3.jsp"); 
                      }
                      else 
                      {
                        bean.setError("入力内容に誤りがあります");
                        forward("UserInfoDetail_1.jsp");
                      }
                  }
              } 
              else if ("return".equals(bean.value("action_cmd"))) 
              {
                  forward("ViewUserList.do");
              }
          }
          
          else if ("UserInfoDetail_2".equals(bean.value("form_name")))  
          {  
              if ("go_next".equals(bean.value("action_cmd"))) 
              {
                  if ("delete".equals(bean.value("request_cmd"))) 
                  {
                      setInputInfo2Dao2WebDelete();
                      bean.rtrimAllItem();
                      UserInfoDao dao = setWeb2Dao2InputInfo();
                      if (inputCheck(dao)) 
                      {
                          bean.setMessage("退職予定日を確定します。よろしいですか？");
                          bean.setValue("request_name", "確定");
                          forward("UserInfoDetail_3.jsp");  
                      }
                      else 
                      {
                          bean.setError("入力内容に誤りがあります");
                          forward("UserInfoDetail_2.jsp"); 
                      }
                  }
              }
              else if ("return".equals(bean.value("action_cmd"))) 
              {
                  forward("ViewUserList.do");
              }
          }
          
          else if ("UserInfoDetail_3".equals(bean.value("form_name"))) 
          {
              if ("go_next".equals(bean.value("action_cmd"))) 
              {
                  if ("ins".equals(bean.value("request_cmd"))) 
                  {
                      setInputInfo2Dao2Web();
                      signUp();
                      scheduleInsert();
                      redirect("ViewUserList.do");
                  }
                  else if ("update".equals(bean.value("request_cmd"))) 
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
                          forward("UserInfoDetail_1.jsp");
                      }
                  }
                  else if ("delete".equals(bean.value("request_cmd"))) 
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
                          forward("UserInfoDetail_1.jsp");
                      }
                  }
              }
              
              else if ("return".equals(bean.value("action_cmd"))) 
              {
                  if ("ins".equals(bean.value("request_cmd"))) 
                  {
                      bean.setValue("request_name", "登録");
                      setInputInfo2Dao2Web();
                      forward("UserInfoDetail_1.jsp");
                  }
                  else if ("update".equals(bean.value("request_cmd"))) 
                  {
                      bean.setValue("request_name", "修正");
                      setInputInfo2Dao2Web();
                      setWeb2Dao2InputInfo();
                      forward("UserInfoDetail_1.jsp");
                  }
                  else if ("delete".equals(bean.value("request_cmd"))) 
                  {
                      bean.setValue("request_name", "修正");
                      setInputInfo2Dao2Web();
                      setWeb2Dao2InputInfo();
                      forward("UserInfoDetail_2.jsp");
                  }
                  else if ("send".equals(bean.value("request_cmd"))) 
                  {
                      redirect("ViewUserList.do");
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
              forward("UserInfoDetail_1.jsp");
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
       
        if ("ins".equals(bean.value("request_cmd")) || "update".equals(bean.value("request_cmd"))) 
        {
            if (bean.value("last_name").length() == 0 && bean.value("first_name").length() == 0)
            {
                errors.put("last_name", "氏名を入力してください。");
                errors.put("first_name", "");
            } 
            else if (bean.value("last_name").length() == 0)
            {
                errors.put("last_name", "名字を入力してください。");
            }
            else if (bean.value("first_name").length() == 0)
            {
                errors.put("first_name", "名前を入力してください。");
            }
        
            if (bean.value("last_name_kana").length() == 0 && bean.value("first_name_kana").length() == 0)
            {
                errors.put("last_name_kana", "氏名のよみを入力してください。");
                errors.put("first_name_kana", "");
            }
            else  if (bean.value("last_name_kana").length() == 0)
            {
                errors.put("last_name_kana", "名字のよみを入力してください。");
            }
            else if (bean.value("first_name_kana").length() == 0)
            {
                errors.put("first_name_kana", "名前のよみを入力してください。");
            }
        
            if (bean.value("last_name_kana").length() > 0 || bean.value("first_name_kana").length() > 0)
            {
                if (!isHiragana(bean.value("last_name_kana")) && !isHiragana(bean.value("first_name_kana"))) 
                {
                    errors.put("last_name_kana", "氏名のよみはひらがなで入力してください。");
                }
                else if (!isHiragana(bean.value("last_name_kana"))) 
                {
                    errors.put("last_name_kana", "名字のよみはひらがなで入力してください。");
                }
                else if (!isHiragana(bean.value("first_name_kana"))) 
                {
                    errors.put("first_name_kana", "名前のよみはひらがなで入力してください。");
                }
            }    
        
            if (bean.value("middle_name").length() != 0)
            {
                if (bean.value("middle_name_kana").length() == 0)
                {
                    errors.put("middle_name_kana", "ミドルネームよみを入力してください。");
                }
                else if (!isHiragana(bean.value("middle_name_kana"))) 
                {
                    errors.put("middle_name_kana", "ミドルネームよみはひらがなで入力してください。");
                }
            }
        
            if (bean.value("maiden_name").length() != 0)
            {
                if (bean.value("maiden_name_kana").length() == 0)
                {
                    errors.put("maiden_name_kana", "旧姓よみを入力してください。");
                }
                else if (!isHiragana(bean.value("maiden_name_kana"))) 
                {
                    errors.put("maiden_name_kana", "旧姓よみはひらがなで入力してください。");
                }
            }
            
            if (bean.value("admin").length() == 0)
            {
                errors.put("admin", "ユーザー区分を選択してください。");
            }
            
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(bean.value("memail"));
            if (bean.value("memail").length() == 0)
            {
                errors.put("memail", "メールアドレスを入力してください。");
            }
            else if (!matcher.matches()) // メアドに使用できる半角英数記号以外のチェック
            {  
                errors.put("memail", "正しいメールアドレスを入力してください。");
            }
            else if ("ins".equals(bean.value("request_cmd"))) 
            {
                if (pUserInfoDao.isEmailExists(bean.value("memail")))
                {
                    // 重複している場合のエラーメッセージ設定
                    errors.put("memail", "このメールアドレスは既に登録されています。");
                }
            }
            else if ("update".equals(bean.value("request_cmd"))) 
            {
                if (pUserInfoDao.isEmailExists(bean.value("memail"), bean.value("main_key")))
                {
                    // 重複している場合のエラーメッセージ設定
                    errors.put("memail", "このメールアドレスは既に登録されています。");
                }
            }
 
            if (bean.value("insert_user_id").length() != 0)
            {
                if (bean.value("insert_user_id").length() < 6 || bean.value("insert_user_id").length() > 12)
                {
                    errors.put("insert_user_id", "ＩＤは６文字以上１２文字以下で入力してください。");
                }
                else if (!bean.value("insert_user_id").matches("^[a-zA-Z0-9]+$")) // メアドに使用できる半角英数記号以外のチェック
                {  
                    errors.put("insert_user_id", "ＩＤは半角英数で入力してください。");
                }
                else if ("ins".equals(bean.value("request_cmd"))) 
                {
                    if (pUserInfoDao.isIdExists(bean.value("insert_user_id"))) 
                    {
                        // 重複している場合のエラーメッセージ設定
                        errors.put("insert_user_id", "このＩＤは既に登録されています。");
                    }
                }
                else if ("update".equals(bean.value("request_cmd"))) 
                {
                    if (pUserInfoDao.isIdExists(bean.value("insert_user_id"), bean.value("main_key"))) 
                    {
                        // 重複している場合のエラーメッセージ設定
                        errors.put("insert_user_id", "このＩＤは既に登録されています。");
                    }
                }
            }
        }
        else if ("delete".equals(bean.value("request_cmd"))) 
        {
            // 日付フォーマットの指定
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String leaveDateStr = bean.value("leave_date");

            // `leave_date` が数字でない場合
            if (!isNumeric(leaveDateStr)) 
            {
                errors.put("leave_date", "数字を入力してください");
            } 
            else 
            {
                try 
                {
                    // `leave_date` が空文字でないかチェック
                    if (leaveDateStr == null || leaveDateStr.trim().isEmpty()) {
                        // 空文字の場合はエラーメッセージを設定せずに `true` を返す
                        return true;
                    } else {
                        // `leave_date` を Date 型に変換
                        Date leaveDate = dateFormat.parse(leaveDateStr);

                        // カレンダーを使用して昨日の日付を取得
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, -1); // 昨日の日付に設定
                        Date yesterday = calendar.getTime(); // 昨日の日付を取得

                        // `leave_date` が昨日以前の日付である場合
                        if (leaveDate.before(yesterday)) {
                            errors.put("leave_date", "本日以降の日付を入力してください");
                        }
                    }
                } 
                catch (ParseException e) 
                {
                    // `leave_date` の解析に失敗した場合
                    errors.put("leave_date", "日付の形式が不正です");
                }
            }
        }

            
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    /**
     * 文字列が数字で構成されているかをチェックするメソッド.
     *
     * @param value チェック対象の文字列
     * @return 文字列が数字で構成されている場合はtrue、それ以外はfalse
     */
    private boolean isNumeric(String value) {
      if (!(value == null || value.trim().isEmpty())) {
        try {
          Integer.parseInt(value);
        } catch (NumberFormatException e) {
          return false;
        }
      }  
        return true;
    }
    
    /**
     * 文字列がひらがなで構成されているかをチェックするメソッド.
     *
     * @param input チェック対象の文字列
     * @return 文字列がひらがなで構成されている場合はtrue、それ以外はfalse
     */
    private boolean isHiragana(String input) {
        return input.matches("^[\\u3040-\\u309Fー]+$");
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
            forward("UserPassReset.jsp");
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