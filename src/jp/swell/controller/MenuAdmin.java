package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;
import jp.swell.user.UserLoginInfo;

/**
 * ログイン時に管理者権限のあるユーザーの遷移先
 * 管理メニュー
 */
public class MenuAdmin extends ControllerBase {

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
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();
        UserLoginInfo loginInfo = (UserLoginInfo) getLoginInfo();
        if (!"1".equals(loginInfo.getAdmin())) {
            redirect("UserMenuHome.do");
            return;
        }

        // フォーム名が 'admin' であるか確認
        if ("admin".equals(bean.value("form_name"))) {
            if ("home".equals(bean.value("action_cmd"))) {
                redirect("UserMenu.do");
            } else if ("user".equals(bean.value("action_cmd"))) {
                redirect("ViewUserList.do");
            } else if ("room".equals(bean.value("action_cmd"))) {
                redirect("RoomList.do");
            } else if ("reserve".equals(bean.value("action_cmd"))) {
                redirect("ReserveList.do");
            } else if ("file".equals(bean.value("action_cmd"))) {
                redirect("FileList.do");
            } else if ("schedule".equals(bean.value("action_cmd"))) {
                redirect("Schedule.do");
            } else if ("calendar".equals(bean.value("action_cmd"))) {
                redirect("Calendar.do");
            } else if ("Shift".equals(bean.value("action_cmd"))) {
                redirect("Shift.do");
            } else if ("Contact".equals(bean.value("action_cmd"))) {
                redirect("ContactList.do");
            } else {
                // アクションコマンドが一致しない場合、メインメニューに遷移
                forward("MenuAdmin.jsp");
            }
        } else {
            forward("MenuAdmin.jsp");
        }
    }
}