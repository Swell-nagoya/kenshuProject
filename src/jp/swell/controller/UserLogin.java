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
	    setLoginNeeds(true);      // ログインが必要
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    /**
     * jp.it_person.controller.HttpServlet のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     *
     * @throws Exception
     */
//    @Override
//    public void doActionProcess() throws AtareSysException {
//        WebBean bean = getWebBean();
//        bean.trimAllItem();
//
//        if ("UserLogin".equals(bean.value("form_name"))) {
//            // ログインボタンが押されたときの処理
//            if ("login".equals(bean.value("action_cmd"))) {
//                this.setLoginInfo(null);
//                if (!inputCheck()) {
//                    this.forward("/UserLogin.jsp");
//                    return; // 入力チェックが失敗した場合は、これ以降の処理を行わない
//                }
//
//                UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
//
//                // ★ 管理者権限を判定して遷移
//                if (userLoginInfo.getAdminFlag() == 1) {
//                    redirect("MenuAdmin.do");
//                } else {
//                    redirect("UserMenu.do");
//                }
//
//                return;
//            } else if ("repassword".equals(bean.value("action_cmd"))) {
//                redirect("SendPassMail.do");
//            }
//        } else if ("UserMenuHome".equals(bean.value("form_name"))) {
//            // ログインボタンが押されたときの処理
//            if ("logout".equals(bean.value("action_cmd"))) {
//                doLogout(); // ログアウト処理
//                return;
//            }
//        } else {
//            this.forward("/UserLogin.jsp");
//        }
//    }
     /**
      * jp.it_person.controller.HttpServlet のメソッドをオーバライド
      */
     @Override
     public void doActionProcess() throws AtareSysException {
         WebBean bean = getWebBean();

         // ログインボタンが押された場合
         if ("login".equals(bean.value("action_cmd"))) {

             // 1, 2, 3の処理: 画面入力値チェックとDB照合
             if (inputCheck()) { // 認証成功
                 
                 // 4. 管理者/一般ユーザーの区別と 5. 遷移の振り分け
                 UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
                 
                 if (userLoginInfo != null && userLoginInfo.isSystemManager()) {
                     // 管理者の場合: 管理メニューへ遷移
                     this.redirect("MenuAdmin.do");
                 } else {
                     // 一般ユーザーの場合: 一般メニューへ遷移
                     this.redirect("UserMenu.jsp");
                 }
                 return; // 成功したらここで処理を終了

             } else {
                 // ログイン失敗時（inputCheck内でエラーメッセージ設定済み）
                 this.forward("UserLogin.jsp");
                 return;
             }
         }

         // 既にログイン済みの場合の処理（リフレッシュなど）
         if (isLogin()) {
             if ("logout".equals(bean.value("action_cmd"))) {
                 doLogout();
                 return;
             }
             
          // ログイン済みの場合も権限に応じてページを振り分け
             UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
             if (userLoginInfo != null && userLoginInfo.isSystemManager()) {
                  this.redirect("MenuAdmin.do");
             } else {
                  this.redirect("UserMenu.jsp");
             }
             return;
         }

         // それ以外（未ログイン状態）はログイン画面へ
         this.forward("/TopPage.do");
     }

 // ... (省略) ...
    /**
     * 入力チェック
     *
     * @return
     * @throws AtareSysException
     */
    private boolean inputCheck() throws AtareSysException {

        WebBean bean = getWebBean();
        if (bean.value("ac").length() == 0) {	// ユーザー名が未入力
            bean.setError("ac", "未入力");
            return false;
        }
        if (bean.value("ko").length() == 0) {	// パスワードが未入力
            bean.setError("ko", "未入力");
            return false;
        }

        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        if (userLoginInfo == null) {
            userLoginInfo = new UserLoginInfo();
        }
        if (!userLoginInfo.login(bean.value("ac"), bean.value("ko"))) {	// ログイン失敗
            bean.setError("ac", "usernameかpasswordが違います");		// エラーメッセージ表示
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
        redirect("/TopPage.do"); // ログインページにリダイレクト
    }

}
