package jp.swell.controller;


import java.util.HashMap;

import javax.servlet.annotation.WebServlet;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.common.SendMailCommon;
import jp.swell.dao.UserInfoDao;

@WebServlet("/SendRecoveryEmailServlet")
public class SendPassMail extends ControllerBase
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
        
        if ("UserLogin".equals(bean.value("form_name")))
        {
            if("repassword".equals(bean.value("action_cmd"))) {
              
        }
        bean.setMessage("登録したメールアドレスを入力してください");
        forward("SendPassMail.jsp");
        }
       
        else if ("SendPassMail".equals(bean.value("form_name")))
        {
            if("go_next".equals(bean.value("action_cmd"))) 
            {
                UserInfoDao dao = setWeb2Dao2InputInfo();
                if (inputCheck(dao)) 
                {
                    SendMailCommon mailCommon = new SendMailCommon();
                    String userEmail = bean.value("memail"); // 送信先のメールアドレス

                    try {
                        mailCommon.sendPassMail(userEmail);
                    } catch (AtareSysException e) {
                        System.err.println("メール送信中にエラーが発生しました: " + e.getMessage());
                    }
                }
            forward("UserLogin.jsp");
            }
            else if ("return".equals(bean.value("action_cmd"))) 
            {
                forward("UserLogin.jsp");
            }
        }
        else if ("UserInfoDetail_3".equals(bean.value("form_name")))
        {
            if("go_next".equals(bean.value("action_cmd"))) 
            {
                setDb2Web();
                SendMailCommon mailCommon = new SendMailCommon();
                String userEmail = bean.value("memail"); // 送信先のメールアドレス

                try {
                    mailCommon.sendPassMail(userEmail);
                } catch (AtareSysException e) {
                    System.err.println("メール送信中にエラーが発生しました: " + e.getMessage());
                }
            }
        forward("ViewUserList.do");
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
        bean.setValue("memail", dao.getMemail());

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
       
        if (bean.value("memail").length() == 0)
        {
            errors.put("memail", "メールアドレスを入力してください。");
        }
        else if (!(pUserInfoDao.isEmailExists(bean.value("memail")))) 
        {
            // メールアドレス登録されていない場合のエラーメッセージ設定
            errors.put("memail", "このメールアドレスは登録されていません。");
        }
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }
    
    private UserInfoDao setWeb2Dao2InputInfo() throws AtareSysException {
      WebBean bean = getWebBean();
      UserInfoDao dao = new UserInfoDao();

      dao.setMemail(bean.value("memail"));
      
      bean.setValue("input_info", Sup.serialize(dao));  // DAOオブジェクトをシリアライズしてWebBeanに保存
      return dao;
  }
    
}