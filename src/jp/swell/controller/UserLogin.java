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
public class UserLogin extends ControllerBase
{

    /**
     * jp.it_person.controller.HttpServlet のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     *
     * @throws Exception
     */
    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.trimAllItem();
        if ("E".equals(bean.value("form_name")))
        {
            // ログインボタンが押されたときの処理
            if ("123456".equals(bean.value("action_cmd")))
            {
                this.setLoginInfo(null);
                if (false == inputCheck())
                {
                    this.forward("/UserLogin.jsp");
                }
                else
                {
                    redirect("ViewUserList.do");
                }
                return;
            }
        }
        else
        {
            if ("logout".equals(bean.value("action_cmd")))
            {
                this.setLoginInfo(null);
            }
            if (this.isLogin())
            {
                redirect("UserMenu.do");
                return;
            }
            this.forward("/UserLogin.jsp");
        }
    }

    /**
     * 入力チェック
     *
     * @return
     * @throws AtareSysException
     */
    private boolean inputCheck() throws AtareSysException
    {

        WebBean bean = getWebBean();
        if (bean.value("ac").length() == 0)
        {
            bean.setError("ac", "未入力");
            return false;
        }
        if (bean.value("ko").length() == 0)
        {
            bean.setError("ko", "未入力");
            return false;
        }
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        if (userLoginInfo == null) userLoginInfo = new UserLoginInfo();
        if (!userLoginInfo.login(bean.value("ac"), bean.value("ko")))
        {
            bean.setError("ac", "？？");
            return false;
        }
        setLoginInfo(userLoginInfo);
        return true;
    }

}
