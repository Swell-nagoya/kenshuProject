package jp.swell.user;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.LoginInfo;
import jp.swell.dao.UserInfoDao;

/**
 * ログイン中のユーザー情報クラス
 *
 * @author 2023 PATAPATA Corp. Corp
 * @since 1.5
 * @version 1.0
 * @since 1.0
 */
public class UserLoginInfo extends LoginInfo implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5742515121815551173L;

    /** お知らせ通知を受け取る必要がある通知数 */
    private Integer must_recognize_notice_count = 0;

    /** ログインユーザ情報 */
    private UserInfoDao userInfoDao = null;

    /**
     * userInfoの取得
     *
     * @return userInfo
     */
    public UserInfoDao getUserInfo() {
        return userInfoDao;
    }

    /**
     * userInfoの設定
     *
     * @return userInfo
     */
    public void setUserInfo(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /**
     * must_recognize_notice_countの取得
     *
     * @return must_recognize_notice_count
     */
    public Integer getMust_recognize_notice_count() {
        return must_recognize_notice_count;
    }

    /**
     * must_recognize_notice_countの設定
     *
     * @param mustRecognizeNoticeCount
     */
    public void setMust_recognize_notice_count(Integer mustRecognizeNoticeCount) {
        must_recognize_notice_count = mustRecognizeNoticeCount;
    }

    public UserLoginInfo() {
    }

    public UserLoginInfo(String userInfoId, String password) {
        userInfoDao = new UserInfoDao();
        userInfoDao.setUserInfoId(userInfoId);
        userInfoDao.setPassword(password);
    }

    /**
     * ユーザーIDの取得
     *
     * @return ユーザーId
     */
    public String getUserId() {
        return userInfoDao.getUserInfoId();
    }

    /**
     * パスワードの取得
     *
     * @return パスワード
     */
    public String getPassword() {
        return userInfoDao.getPassword();
    }

    /**
     * idを取得
     *
     * @return id
     */
    public String getUserInfoId() {
        return userInfoDao.getUserInfoId();
    }

    /**
     * 名の取得 *
     *
     * @return 名
     */
    public String getLastName() {
        return userInfoDao.getLastName();
    }

    /**
     * 姓の取得
     *
     * @return 姓
     */
    public String getFirstName() {
        return userInfoDao.getFirstName();
    }

    /**
     * 氏名の取得
     *
     * @return 名
     */
    public String getFullName() {
        String fullName = userInfoDao.getFullName();
        return fullName;
    }

    /**
     * かな名の取得
     *
     * @return かな名
     */
    public String getLastNameKana() {
        return userInfoDao.getLastNameKana();
    }

    /**
     * かな姓の取得
     *
     * @return かな姓
     */
    public String getFirstNameKana() {
        return userInfoDao.getFirstNameKana();
    }

    /**
     * 郵便番号の取得
     *
     * @return 郵便番号
     */
    public String getZipcode() {
        return userInfoDao.getZipcode();
    }

    /**
     * 住所の取得
     *
     * @return 住所
     */
    public String getAddress() {
        return userInfoDao.getAddress();
    }

    /**
     * 電話番号の取得
     *
     * @return 電話番号
     */
    public String getTel() {
        return userInfoDao.getTel();
    }

    public String getAdmin() {
        return userInfoDao.getAdmin();
    }

    /**
     * ユーザー名の取得
     *
     * @return ユーザー名
     */
    public String getUserName() {
        return (userInfoDao.getLastName()) + " " + userInfoDao.getFirstName();
    }

    /**
     * must_recognize_notice_countの取得
     *
     * @return 0より大きければ：true、それ以外なら：false
     */
    public boolean getMustRecognizeFlg() {
        if (this.must_recognize_notice_count > 0) {
            return true;
        }
        return false;
    }

    /**
     * ログイン処理を行う.
     *
     * @param pAccount アカウントまたはメールアドレス
     * @param pPassword パスワード
     * @return ログインに成功：true、失敗：false
     */
    public boolean resetUserInfo() {
        try {
            UserInfoDao dao = new UserInfoDao();
            boolean flg = dao.dbSelect(this.userInfoDao.getUserInfoId());
            if (flg) {
                userInfoDao = dao;
                return flg;
            }
            return flg;
        } catch (AtareSysException e) {
            userInfoDao = null;
            e.printStackTrace();
            return false;
        }
    }

    public UserInfoDao getUserInfoDao() {
        return this.userInfoDao;
    }

    public boolean isAdmin() {
        return userInfoDao != null && "1".equals(userInfoDao.getAdmin());
    }


    @Override
    public boolean isNoticeArrive() {
        return true;
    }

    @Override
    public boolean limitation(String loginId) {
        return false;
    }

    @Override
    public boolean isSystemManager() {
        return true;
    }

    @Override
    public String getLoginId() {
        return userInfoDao.getUserInfoId();
    }

    @Override
    public boolean login(String pAccount, String pPassword) {
        try {
            userInfoDao = new UserInfoDao();
            boolean flg = userInfoDao.login(pAccount, pPassword);
            if (!flg) {
                return false;
            }
            return true;
        } catch (AtareSysException e) {
            userInfoDao = null;
            e.printStackTrace();
            return false;
        }
    }
}
