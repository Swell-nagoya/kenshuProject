package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;
import jp.swell.user.UserLoginInfo;

/**
 * ログインコントロールクラス
 *
 * @author 2023 PATAPATA Corp. Corp
 * @since 1.5
 * @version 1.0
 * @since 1.0
 */
public class UserLogin extends ControllerBase {
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
    public void doInit() {
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    /**
     * jp.it_person.controller.HttpServlet のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     *
     * @throws Exception
     */
    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.trimAllItem();

        if ("UserLogin".equals(bean.value("form_name"))) {
            // ログインボタンが押されたときの処理
            if ("login".equals(bean.value("action_cmd"))) {
                this.setLoginInfo(null);
                if (!inputCheck()) {
                    this.forward("/UserLogin.jsp");
                    return; // 入力チェックが失敗した場合は、これ以降の処理を行わない
                }

                UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();

                //管理者権限の有無を確認し遷移先を判断
                if ("1".equals(userLoginInfo.getAdmin())) {
                    redirect("MenuAdmin.do");//管理者メニュー
                } else {
                    redirect("Calendar.do");
                }
                return;
            } else if ("repassword".equals(bean.value("action_cmd"))) {
                redirect("SendPassMail.do");
            }
        } else if ("UserMenuHome".equals(bean.value("form_name"))) {
            // ログインボタンが押されたときの処理
            if ("logout".equals(bean.value("action_cmd"))) {
                doLogout(); // ログアウト処理
                return;
            }
        } else {
            this.forward("/UserLogin.jsp");
        }
    }

    /**
     * 入力チェック
     *
     * @return
     * @throws AtareSysException
     */
    private boolean inputCheck() throws AtareSysException {

        WebBean bean = getWebBean();
        if (bean.value("ac").length() == 0) {
            bean.setError("ac", "未入力");
            return false;
        }
        if (bean.value("ko").length() == 0) {
            bean.setError("ko", "未入力");
            return false;
        }

        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        if (userLoginInfo == null) {
            userLoginInfo = new UserLoginInfo();
        }
        if (!userLoginInfo.login(bean.value("ac"), bean.value("ko"))) {
            bean.setError("ac", "usernameかpasswordが違います");
            return false;
        }
        userLoginInfo.setUserInfo(userLoginInfo.getUserInfoDao());
        setLoginInfo(userLoginInfo);
        bean.setValue("user_info_id", userLoginInfo.getUserInfoId());
        return true;
    }

    // ログイン情報をクリアするメソッド
    private void clearLoginInfo() {
        setLoginInfo(null); // ログイン情報をクリア
    }

    // ログアウト処理を行うメソッド
    private void doLogout() throws AtareSysException {
        clearLoginInfo(); // ログイン情報をクリア
        redirect("UserLogin.do"); // ログインページにリダイレクト
    }

}
