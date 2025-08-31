package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;
import jp.swell.dao.UserInfoDao;

public class UserMenu extends ControllerBase
{
    /**
     * myUserInfoDao ログインユーザのUserInfoDao
     */
    private UserInfoDao myUserInfoDao = null;


    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.setValue("scroll_info", SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "UserMenu", "scroll_info", "0"));
        bean.setValue("userInfoDao", myUserInfoDao);
//        UserInfoDao userInfoDao = ((jp.swell.user.UserLoginInfo) getLoginInfo()).getUserInfo();
//        bean.setValue("full_name", userInfoDao.getFullName() + "さん");

        bean.setValue("menu_flg", bean.value("menu_cmd"));
            forward("UserMenuHome.jsp");
    }


}
