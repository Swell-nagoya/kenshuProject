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
package jp.swell.validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.dao.UserInfoDao;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class UserInfoValidator {
   
    /**
     * 氏名 入力チェックを行う。.
     *
     * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     */
	    public HashMap<String, String> nameCheck(WebBean bean)
     {
       HashMap<String, String> errorSet = new HashMap<>();

       if (bean.value("last_name").length() == 0 && bean.value("first_name").length() == 0)
       {
          	errorSet.put("last_name", "氏名を入力してください。");
          	errorSet.put("first_name", "");
       } 
       else if (bean.value("last_name").length() == 0)
       {
          	errorSet.put("last_name", "名字を入力してください。");
       }
       else if (bean.value("first_name").length() == 0)
       {
       	   errorSet.put("first_name", "名前を入力してください。");
       }
       return errorSet;
     }

     /**
      * 氏名(かな) 入力チェックを行う。.
      *
      * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
      */
     public HashMap<String, String> nameKanaCheck(WebBean bean)
     {
       HashMap<String, String> errorSet = new HashMap<>();
       String lastNameKana = bean.value("last_name_kana");
       String firstNameKana = bean.value("first_name_kana");



       if (lastNameKana.length() == 0 && firstNameKana.length() == 0)
       {
          	errorSet.put("last_name_kana", "氏名のよみを入力してください。");
           errorSet.put("first_name_kana", "");
           return errorSet;
       }
       if (lastNameKana.length() == 0) {

           errorSet.put("last_name_kana", "名字のよみを入力してください。");
       }
        if (firstNameKana.length() == 0) {
           errorSet.put("first_name_kana", "名前のよみを入力してください。");
       }
       
       
       if (lastNameKana.length() > 0 || firstNameKana.length() > 0) {
           if (!isHiragana(lastNameKana) && !isHiragana(firstNameKana)) {
               errorSet.putIfAbsent("last_name_kana", "氏名のよみはひらがなで入力してください。");
           }
           if (!isHiragana(lastNameKana) ) {
              	errorSet.putIfAbsent("last_name_kana", "名字のよみはひらがなで入力してください。");
           }
           if (!isHiragana(firstNameKana)) {
               errorSet.putIfAbsent("first_name_kana", "名前のよみはひらがなで入力してください。");
           }
       }

       return errorSet;
       
     }

     /**
      * ミドルネーム 入力チェックを行う。.
      *
      * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
      */
     public HashMap<String, String> middleNameCheck(WebBean bean)
     {
       HashMap<String, String> errorSet = new HashMap<>();
      
       if (bean.value("middle_name").length() != 0)
       {
          if (bean.value("middle_name_kana").length() == 0)
          {
          	   errorSet.put("middle_name_kana", "ミドルネームのよみを入力してください。");
          }
          else if (!isHiragana(bean.value("middle_name_kana"))) 
          {
              errorSet.put("middle_name_kana", "ミドルネームのよみはひらがなで入力してください。");
          }
       } else {
          if (bean.value("middle_name_kana").length() != 0)
          {
    	         errorSet.put("middle_name", "ミドルネームを入力してください。");
          }
       }
  
       return errorSet;
     }
     

     /**
      * メールアドレス 入力チェックを行う。.
      *
      * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
      * @throws AtareSysException
      */
     public HashMap<String, String> memailCheck(WebBean bean, UserInfoDao pUserInfoDao) throws AtareSysException
     {

      HashMap<String, String> errorSet = new HashMap<>();
      
      String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
      Pattern pattern = Pattern.compile(emailRegex);
      Matcher matcher = pattern.matcher(bean.value("memail"));
      if (bean.value("memail").length() == 0)
      {
      	   errorSet.put("memail", "メールアドレスを入力してください。");
      }
      else if (!matcher.matches()) // メアドに使用できる半角英数記号以外のチェック
      {  
      	   errorSet.put("memail", "正しいメールアドレスを入力してください。");
      }
      else if ("ins".equals(bean.value("request_cmd"))) 
      {
          if (pUserInfoDao.isEmailExists(bean.value("memail")))
          {
              // 重複している場合のエラーメッセージ設定
            	errorSet.put("memail", "このメールアドレスは既に登録されています。");
          }
      }
      else if ("update".equals(bean.value("request_cmd"))) 
      {
          if (pUserInfoDao.isEmailExists(bean.value("memail"), bean.value("main_key")))
          {
              // 重複している場合のエラーメッセージ設定
              errorSet.put("memail", "このメールアドレスは既に登録されています。");
          }
      }
      
      return errorSet;
     }

     /**
      * ユーザー区分 入力チェックを行う。.
      *
      * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
      */
     public HashMap<String, String> adminCheck(WebBean bean)
     {

        HashMap<String, String> errorSet = new HashMap<>();
      
        if (bean.value("admin").length() == 0)
        {
      	     errorSet.put("admin", "ユーザー区分を選択してください。");
        }
        return errorSet;
     }


     /**
      * 旧姓 入力チェックを行う。.
      *
      * @return errorSet HashMapにエラーフィールドをキーとしてエラーメッセージを返す
      */
     public HashMap<String, String> maidenNameCheck(WebBean bean)
     {
       HashMap<String, String> errorSet = new HashMap<>();
      
  
       if (bean.value("maiden_name").length() != 0)
       {
          if (bean.value("maiden_name_kana").length() == 0)
          {
             	errorSet.put("maiden_name_kana", "旧姓のよみを入力してください。");
          }
          else if (!isHiragana(bean.value("maiden_name_kana"))) 
          {
             	errorSet.put("maiden_name_kana", "旧姓のよみはひらがなで入力してください。");
          }
       } else {
          if (bean.value("maiden_name_kana").length() != 0)
          {
              errorSet.put("maiden_name", "旧姓を入力してください。");
          }
       }
       return errorSet;
     }
     
     
     /**
       *  任意ID エラーチェックを行う。.
       *
       * @return errorSet
       */
     public HashMap<String, String> insertUserIdCheck(WebBean bean, UserInfoDao pUserInfoDao) throws AtareSysException {
         HashMap<String, String> errorSet = new HashMap<>();
         
         String insert_user_id = bean.value("insert_user_id");
         String request_cmd = bean.value("request_cmd");

         // 未入力時
         if (insert_user_id.length() == 0) {
             return errorSet;
         }

         // 入力値の文字数と形式チェック
         boolean isFormatOk = isValidIdFormat(insert_user_id, errorSet);

         // insert_user_idの重複チェック
         if (isFormatOk) {
         	   // 新規登録画面
             if ("ins".equals(request_cmd)) {
              checkDuplicateForInsert(insert_user_id, pUserInfoDao, errorSet);

             // 編集画面
             } else if ("update".equals(request_cmd)) {
                 String mainKey = bean.value("main_key");
                 checkDuplicateForInsert(insert_user_id, mainKey, pUserInfoDao, errorSet);
             }
         }

         return errorSet;
     }

     /**
      * 入力値の文字数と形式チェックを行う。.
      *
      * @return true, false
      */
      private boolean isValidIdFormat(String userId, HashMap<String, String> errorSet) {
     	   if (userId.length() < 6 || userId.length() > 12) {
             errorSet.put("insert_user_id", "ＩＤは６文字以上１２文字以下で入力してください。");
             return false;
         }
     	   if (!userId.matches("^[a-zA-Z0-9]+$")) {
             errorSet.put("insert_user_id", "ＩＤは半角英数で入力してください。");
             return false;
         }
         return true;
     }

     /**
      * insert_user_idの重複チェックを行う。.
      *
      */
      private void checkDuplicateForInsert(String userId, UserInfoDao pUserInfoDao, HashMap<String, String> errorSet) throws AtareSysException {
       checkDuplicateForInsert(userId, "", pUserInfoDao, errorSet); 
      }

      private void checkDuplicateForInsert(String userId, String mainKey, UserInfoDao pUserInfoDao, HashMap<String, String> errorSet) throws AtareSysException {

      	if (pUserInfoDao.isIdExists(userId, mainKey)) {
          errorSet.put("insert_user_id", "このＩＤは既に登録されています。");
        }
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
    

    public HashMap<String, String> leaveDateCheck(WebBean bean)
    {

     HashMap<String, String> errorSet = new HashMap<>();
     

     // 日付フォーマットの指定
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
     String leaveDateStr = bean.value("leave_date");

     // `leave_date` が数字でない場合
     if (!isNumeric(leaveDateStr)) 
     {
        	errorSet.put("leave_date", "数字を入力してください");
     } 
     else 
     {
         try 
         {
             // `leave_date` が空文字でないかチェック
             if (leaveDateStr == null || leaveDateStr.trim().isEmpty()) {
                 // 空文字の場合はエラーメッセージを設定せずに `空のerrorSet` を返す
//                 return true;
                 return errorSet;
             } else {
                 // `leave_date` を Date 型に変換
                 Date leaveDate = dateFormat.parse(leaveDateStr);

                 // カレンダーを使用して昨日の日付を取得
                 Calendar calendar = Calendar.getInstance();
                 calendar.add(Calendar.DATE, -1); // 昨日の日付に設定
                 Date yesterday = calendar.getTime(); // 昨日の日付を取得

                 // `leave_date` が昨日以前の日付である場合
                 if (leaveDate.before(yesterday)) {
                    	errorSet.put("leave_date", "本日以降の日付を入力してください");
                 }
             }
         } 
         catch (ParseException e) 
         {
             // `leave_date` の解析に失敗した場合
             errorSet.put("leave_date", "日付の形式が不正です");
         }
     }
     
     return errorSet;
    }
    
}