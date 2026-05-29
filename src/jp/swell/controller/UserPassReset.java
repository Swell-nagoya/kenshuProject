package jp.swell.controller;

import java.security.SecureRandom;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

public class UserPassReset extends ControllerBase
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
        HttpServletRequest request = getRequest();
        
        String newPass = bean.value("new_password");
        String checkPass = bean.value("check_password");
        String token = request.getParameter("key");

        if ("UserPassReset".equals(bean.value("form_name"))) {
            if (newPass == null || newPass.isEmpty() || checkPass == null || checkPass.isEmpty()) {
                bean.setValue("result", "パスワードを入力してください");
            } else if (!newPass.equals(checkPass)) {
                bean.setValue("result", "パスワードが一致しません");
            } else {
                UserInfoDao dao = new UserInfoDao();
                if (dao.isValidToken(token)) {
                    try {
                        String hashedPassword = dao.hashPassword(newPass);
                        if (dao.updatePassword(token, hashedPassword)) {
                            String userId = dao.getUserIdByToken(token);
                            if (!loginInputCheck(userId, newPass)) {
                                forward("UserMenuHome.jsp");
                            } else {
                                bean.setValue("result", "ログイン中にエラーが発生しました");
                            }
                        } else {
                            bean.setValue("result", "パスワード変更中にエラーが発生しました");
                        }
                    } catch (Exception e) {
                        bean.setValue("result", "パスワードのハッシュ化中にエラーが発生しました");
                    }
                } else {
                    bean.setValue("result", "無効なトークンまたはトークンの有効期限が切れています");
                }
            }
        }
        forward("UserPassReset.jsp");
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
        String mainKey = bean.value("main_key");
        if (inputCheck(dao))
        {
            try {
                DbBase.dbBeginTran();
                dao.dbUpdate(mainKey);
                DbBase.dbCommitTran();
            } catch (Exception e) {
                DbBase.dbRollbackTran();
            }
        }
        else
        {
            bean.setError("入力内容に誤りがあります");
        }
    }
    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private UserInfoDao setWeb2Dao2InputInfo() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = new UserInfoDao();
        dao.setPasswordUser(bean.value("new_password"));

        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }
    
    
    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     * @throws AtareSysException
     */
    private boolean inputCheck(UserInfoDao dao) throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("new_password").length() == 0)
        {
            errors.put("new_password", "新しいパスワードを入力してください。");
        } 
                
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }
   
    /**
     * 情報の確認とログイン処理
     * @param userId
     * @param newPass
     * @return
     * @throws AtareSysException
     */
    private boolean loginInputCheck(String userId, String newPass) throws AtareSysException {
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        if (userLoginInfo == null) {
            userLoginInfo = new UserLoginInfo();
        }
        if (!userLoginInfo.login(userId, newPass)) {
            return false;
        }
        setLoginInfo(userLoginInfo);
        return true;
    }
    /**
     *  ランダム英数字生成用のメソッド
     * @param length
     * @return
     */
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

    
}