package jp.swell.common;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.HttpServlet;
import jp.patasys.common.http.WebBean;
import jp.swell.user.UserLoginInfo;

public abstract class ControllerBase extends HttpServlet {
    public ControllerBase() {
    }

    @Override
    public final void doAction() throws AtareSysException {
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        WebBean bean = getWebBean();

        if (userLoginInfo != null) {
            bean.setValue("menu_cmd", bean.value("menu_cmd"));
            bean.setValue("login_user", userLoginInfo.getUserInfoId());
        }
        doActionProcess();
    }

    @Override
    public boolean checkAuthority() throws AtareSysException {
        return true;
    }

    public abstract void doActionProcess() throws AtareSysException;

    @Override
    public void doInit() {
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか
        setUsecache(true); // この処理はクライアントのキャッシュを認めるか
    }

}
