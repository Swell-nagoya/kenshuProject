package jp.swell.dao;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;
import jp.patasys.common.util.Digest;

/**
 * user_info ユーザ情報テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class ShiftDAO implements Serializable {
    /** Derializable No. */
    private static final long serialVersionUID = 1L;
    /**
     * データアクセス権限のあるユーザリスト
     */
    private ArrayList<String> authorityUserList = null;

    /**
     * 姓名取得する
     *
     * @return fulltName 姓名
     */
    public String getFullName() {
        String str = "";
        if (lastName.length() != 0)
            str = lastName;
        str += !(middleName.length() == 0) ? " " : "";
        str += middleName;
        str += !(firstName.length() == 0) ? " " : "";
        str += firstName;
        return str;
    }

    /**
     * 姓名かなを取得する
     *
     * @return fulltName 姓名
     */
    public String getFullNameKana() {
        String str = "";
        if (lastNameKana.length() != 0)
            str = lastNameKana;
        str += !(middleNameKana.length() == 0) ? " " : "";
        str += middleNameKana;
        str += !(firstNameKana.length() == 0) ? " " : "";
        str += firstNameKana;
        return str;
    }

    /**
     * user_info ユーザ情報テーブルで使用するメンバー変数。
     */
    /**
     * userInfoId  ユーザ情報ID
     */
    private String userInfoId = "";

    /**
     * ユーザ情報IDを取得する。.
     * @return  userInfoId ユーザ情報ID
     */
    public String getUserInfoId() {
        return userInfoId;
    }

    /**
     * ユーザ情報IDをセットする。.
     * @param userInfoId ユーザ情報ID
     */
    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    /**
     * stateFlg  状態フラグ
     */
    private int stateFlg;

    /**
     * フラグを取得する。
     * @return  stateFlg 状態フラグ
     */
    public int getStateFlg() {
        return stateFlg;
    }

    /**
     * フラグをセットする。.
     * @param stateFlg 状態フラグ
     */
    public void setStateFlg(int stateFlg) {
        this.stateFlg = stateFlg;
    }

    /**
     * passwordUser  ユーザーパスワード
     */
    private String passwordUser = "";

    /**
     * ユーザーパスワードを取得する。.
     * @return  passwordUser ユーザーパスワード
     */
    public String getPasswordUser() {
        return passwordUser;
    }

    /**
     * ユーザーパスワードをセットする。.
     * @param passwordUser ユーザーパスワード
     */
    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    /**
     * password  パスワード
     */
    private String password = "";

    /**
     * パスワードを取得する。.
     * @return  password パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードをセットする。.
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * lastName  姓
     */
    private String lastName = "";

    /**
     * 姓を取得する。.
     * @return  lastName 姓
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 姓をセットする。.
     * @param lastName 姓
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * middleName  ミドルネーム
     */
    private String middleName = "";

    /**
     * ミドルネームを取得する。.
     * @return  middleName ミドルネーム
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * ミドルネームをセットする。.
     * @param middleName ミドルネーム
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * firstName  名
     */
    private String firstName = "";

    /**
     * 名を取得する。.
     * @return  firstName 名
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 名をセットする。.
     * @param firstName 名
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * maidenName  旧姓
     */
    private String maidenName = "";

    /**
     * 旧姓を取得する。.
     * @return  maidenName 旧姓
     */
    public String getMaidenName() {
        return maidenName;
    }

    /**
     * 旧姓をセットする。.
     * @param maidenName 旧姓
     */
    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    /**
     * lastNameKana  姓よみ
     */
    private String lastNameKana = "";

    /**
     * 姓よみを取得する。.
     * @return  lastNameKana 姓よみ
     */
    public String getLastNameKana() {
        return lastNameKana;
    }

    /**
     * 姓よみをセットする。.
     * @param lastNameKana 姓よみ
     */
    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = lastNameKana;
    }

    /**
     * middleNameKana  ミドルネームよみ
     */
    private String middleNameKana = "";

    /**
     * ミドルネームよみを取得する。.
     * @return  middleNameKana ミドルネームよみ
     */
    public String getMiddleNameKana() {
        return middleNameKana;
    }

    /**
     * ミドルネームよみをセットする。.
     * @param middleNameKana ミドルネームよみ
     */
    public void setMiddleNameKana(String middleNameKana) {
        this.middleNameKana = middleNameKana;
    }

    /**
     * firstNameKana  名よみ
     */
    private String firstNameKana = "";

    /**
     * 名よみを取得する。.
     * @return  firstNameKana 名よみ
     */
    public String getFirstNameKana() {
        return firstNameKana;
    }

    /**
     * 名よみをセットする。.
     * @param firstNameKana 名よみ
     */
    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = firstNameKana;
    }

    /**
     * maidenNameKana  旧姓よみ
     */
    private String maidenNameKana = "";

    /**
     * 旧姓よみを取得する。.
     * @return  maidenNameKana 旧姓よみ
     */
    public String getMaidenNameKana() {
        return maidenNameKana;
    }

    /**
     * 旧姓よみをセットする。.
     * @param maidenNameKana 旧姓よみ
     */
    public void setMaidenNameKana(String maidenNameKana) {
        this.maidenNameKana = maidenNameKana;
    }

    /**
     * insertDate  入力日時
     */
    private String insertDate = "";

    /**
     * 入力日時を取得する。.
     * @return  insertDate 入力日時
     */
    public String getInsertDate() {
        return insertDate;
    }

    /**
     * 入力日時をセットする。.
     * @param insertDate 入力日時
     */
    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * insertUserId  入力ユーザーＩＤ
     */
    private String insertUserId = "";

    /**
     * 入力ユーザーＩＤを取得する。.
     * @return  insertUserId 入力ユーザーＩＤ
     */
    public String getInsertUserId() {
        return insertUserId;
    }

    /**
     * 入力ユーザーＩＤをセットする。
     * @param insertUserId 入力ユーザーＩＤ
     */
    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    /**
     * updateDate  アップデート日時
     */
    private String updateDate = "";

    /**
     * アップデート日時を取得する。.
     * @return  updateDate アップデート日時
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * アップデート日時をセットする。
     * @param updateDate アップデート日時
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * updateUserId  アップデートユーザーＩＤ
     */
    private String updateUserId = "";

    /**
     * アップデートユーザーＩＤを取得する。.
     * @return  updateUserId アップデートユーザーＩＤ
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * アップデートユーザーＩＤをセットする。
     * @param updateUserId アップデートユーザーＩＤ
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * leaveDate  退職予定日
     */
    private String leaveDate = "";

    /**
     * 退職予定日を取得する。.
     * @return  leaveDate 退職予定日
     */
    public String getLeaveDate() {
        return leaveDate;
    }

    /**
     * 退職予定日をセットする。
     * @param leaveDate 退職予定日
     */
    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    /**
     * zipcode  郵便番号
     */
    private String zipcode = "";

    /**
     * 郵便番号を取得する。.
     * @return  zipcode 郵便番号
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * 郵便番号をセットする。.
     * @param zipcode 郵便番号
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * address  住所
     */
    private String address = "";

    /**
     * 住所を取得する。.
     * @return  address 住所
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住所をセットする。.
     * @param address 住所
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * station  最寄りの駅
     */
    private String station = "";

    /**
     * 最寄りの駅を取得する。.
     * @return  station 最寄りの駅
     */
    public String getStation() {
        return station;
    }

    /**
     * 最寄りの駅をセットする。.
     * @param station 最寄りの駅
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     * tel  電話番号
     */
    private String tel = "";

    /**
     * 電話番号を取得する。.
     * @return  tel 電話番号
     */
    public String getTel() {
        return tel;
    }

    /**
     * 電話番号をセットする。.
     * @param tel 電話番号
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * fax  ＦＡＸ
     */
    private String fax = "";

    /**
     * ＦＡＸを取得する。.
     * @return  fax ＦＡＸ
     */
    public String getFax() {
        return fax;
    }

    /**
     * ＦＡＸをセットする。.
     * @param fax ＦＡＸ
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * mtel  携帯電話番号
     */
    private String mtel = "";

    /**
     * 携帯電話番号を取得する。.
     * @return  mtel 携帯電話番号
     */
    public String getMtel() {
        return mtel;
    }

    /**
     * 携帯電話番号をセットする。.
     * @param mtel 携帯電話番号
     */
    public void setMtel(String mtel) {
        this.mtel = mtel;
    }

    /**
     * memail  携帯Eメール
     */
    private String memail = "";

    /**
     * 携帯Eメールを取得する。.
     * @return  memail 携帯Eメール
     */
    public String getMemail() {
        return memail;
    }

    /**
     * 携帯Eメールをセットする。.
     * @param memail 携帯Eメール
     */
    public void setMemail(String memail) {
        this.memail = memail;
    }

    /**
     * adminFlag　管理者権限
     */
    private String admin = "";

    /**
     * 管理者権限を取得する。
     * @return　adminFlag　管理者権限
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * 管理者権限をセットする。.
     * @param adminFlag　管理者権限
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * passwordModifyDate  パスワード変更日時
     */
    private String passwordModifyDate = "";

    /**
     * パスワード変更日時を取得する。.
     * @return  passwordModifyDate パスワード変更日時
     */
    public String getPasswordModifyDate() {
        return passwordModifyDate;
    }

    /**
     * パスワード変更日時をセットする。.
     * @param passwordModifyDate パスワード変更日時
     */
    public void setPasswordModifyDate(String passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    /**
     * loginEnableFrom  ログイン可能期間FROM
     */
    private String loginEnableFrom = "";

    /**
     * ログイン可能期間FROMを取得する。.
     * @return  loginEnableFrom ログイン可能期間FROM
     */
    public String getLoginEnableFrom() {
        return loginEnableFrom;
    }

    /**
     * ログイン可能期間FROMをセットする。.
     * @param loginEnableFrom ログイン可能期間FROM
     */
    public void setLoginEnableFrom(String loginEnableFrom) {
        this.loginEnableFrom = loginEnableFrom;
    }

    /**
     * loginEnableTo  ログイン可能期間TO
     */
    private String loginEnableTo = "";

    /**
     * ログイン可能期間TOを取得する。.
     * @return  loginEnableTo ログイン可能期間TO
     */
    public String getLoginEnableTo() {
        return loginEnableTo;
    }

    /**
     * ログイン可能期間TOをセットする。.
     * @param loginEnableTo ログイン可能期間TO
     */
    public void setLoginEnableTo(String loginEnableTo) {
        this.loginEnableTo = loginEnableTo;
    }

    /**
     * searchFullName 検索用氏名
     */
    private String searchFullName = "";
    /**
     * searchFullName 検索用氏名かな
     */
    private String searchFullNameKana = "";

    /**
     * searchFullName 検索用氏名を取得します。
     *
     * @return searchFullName 検索用氏名
     */
    public String getSearchFullName() {
        return searchFullName;
    }

    /**
     * searchFullName 検索用氏名を設定します。
     *
     * @param searchFullName
     *        searchFullName 検索用氏名
     */
    public void setSearchFullName(String searchFullName) {
        this.searchFullName = searchFullName;
    }

    /**
     * searchFullName 検索用氏名かなを取得します。
     *
     * @return searchFullName 検索用氏名かな
     */
    public String getSearchFullNameKana() {
        return searchFullNameKana;
    }

    /**
     * searchFullName 検索用氏名かなを設定します。
     *
     * @param searchFullNameKana
     *        searchFullName 検索用氏名かな
     */
    public void setSearchFullNameKana(String searchFullNameKana) {
        this.searchFullNameKana = searchFullNameKana;
    }

    private String[] userIds;

    // userIdsをセットするメソッド
    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    // userIdsを取得するメソッド
    public String[] getUserIds() {
        return this.userIds;
    }

    /**
     * searchName  検索名
     */
    private String searchName = "";

    /**
     * 検索名を取得する。.
     * @return  searchName　検索名
     */
    public String getSearchName() {
        return searchName;
    }

    /**
     * 検索名をセットする。.
     * @param searchName 検索名
     */
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    /**
     *  データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList() {
        return authorityUserList;
    }

    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    /**
     * コンストラクタ。.
     */
    private String id;
    private String name;
    private String email;
    private String startTime;
    private String endTime;
    private String workPlace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartTime() {
        return startTime.substring(0, 5);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime.substring(0, 5);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public ShiftDAO() {
        fieldsArray.put("id", "employee_shifts.id");
        fieldsArray.put("name", "employee_shifts.name");
        fieldsArray.put("email", "employee_shifts.email");
        fieldsArray.put("start_time", "employee_shifts.start_time");
        fieldsArray.put("end_time", "employee_shifts.end_time");
        fieldsArray.put("work_place", "employee_shifts.work_place");
    }

    /**
     * employee_shifts ユーザ情報テーブルを検索しemployee_shifts ユーザ情報テーブルの１行を取得します。
     *
     * @param pId   ユーザ情報ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pId) throws AtareSysException {
        String sql = "SELECT "
                + "employee_shifts.id AS employee_shifts___id, "
                + "employee_shifts.name AS employee_shifts___name, "
                + "employee_shifts.email AS employee_shifts___email, "
                + "CONVERT(start_time, CHAR)  AS employee_shifts___start_time, "
                + "CONVERT(end_time, CHAR)  AS employee_shifts___end_time, "
                + "employee_shifts.work_place AS employee_shifts___work_place "
                + "FROM testdb.employee_shifts "
                + "WHERE id = " + DbS.chara(pId);

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.size() == 0)
            return false;

        HashMap<String, String> map = rs.get(0);
        setEmployeeShift(map, this);
        return true;
    }

    /**
    
    /**
    * UserInfoDao にuser_info ユーザ情報テーブルから読み込んだデータを設定する。.
    *
    * @param map  読み込んだテーブルの１レコードが入っているHashMap
    * @param dao  UserInfoDaoこのテーブルのインスタンス
    */
    public void setEmployeeShift(HashMap<String, String> map, ShiftDAO dao) throws AtareSysException {
        dao.setId(DbI.chara(map.get("employee_shifts___id")));
        dao.setName(DbI.chara(map.get("employee_shifts___name")));
        dao.setEmail(DbI.chara(map.get("employee_shifts___email")));
        dao.setStartTime(DbI.chara(map.get("employee_shifts___start_time")));
        dao.setEndTime(DbI.chara(map.get("employee_shifts___end_time")));
        dao.setWorkPlace(DbI.chara(map.get("employee_shifts___work_place")));
    }

    /** 
     * user_info ユーザ情報テーブルにデータを挿入する 
     * 
     * @return true:成功 false:失敗 
     * @throws AtareSysException エラー 
     */
    public boolean dbInsert() throws AtareSysException {

        String sql = "insert into employee_shifts ("
                + " id"
                + ",name"
                + ",email"
                + ",start_time"
                + ",end_time"
                + ",work_place"
                + " ) values ( "
                + DbO.chara(getId())
                + "," + DbO.chara(getName())
                + "," + DbO.chara(getEmail())
                + "," + DbO.chara(getStartTime())
                + "," + DbO.chara(getEndTime())
                + "," + DbO.chara(getWorkPlace())
                + " )";

        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbInsert number or record exception.");

        return true;
    }

    /**
     * user_info ユーザ情報テーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdate(String id) throws AtareSysException {
        String sql = "update employee_shifts set "
                + "name = " + DbO.chara(getName())
                + "," + " email = " + DbO.chara(getEmail())
                + "," + " start_time = " + DbO.chara(getStartTime())
                + "," + " end_time = " + DbO.chara(getEndTime())
                + "," + " work_place = " + DbO.chara(getWorkPlace())
                + " where id = " + DbS.chara(id)
                + "";

        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbUpdate number or record exception.");
        return true;
    }

    /**
     * user_info ユーザ情報テーブルからデータを削除する
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbDelete(String id) throws AtareSysException {
        String sql = "DELETE FROM employee_shifts "
                + " where id = " + DbS.chara(id);
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception.");
        return true;
    }

    /**
     * user_info ユーザ情報テーブルからデータの削除をキャンセルする
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbCancelDelete(String userInfoId) throws AtareSysException {
        String sql = "update user_info set "
                + " state_flg = 1 "
                + " where user_info_id = " + DbS.chara(userInfoId);
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception.");
        return true;
    }

    /**
     * メールアドレスの重複確認を行うメソッド(新規登録時)
     * @param email 確認するメールアドレス
     * @return メールアドレスが重複していれば true、そうでなければ false
     * @throws AtareSysException
     */
    public boolean isEmailExists(String email) throws AtareSysException {
        // メールアドレスが存在するかのカウントを行う
        String sql = "SELECT COUNT(*) FROM employee_shifts "
                + "WHERE email = " + DbS.chara(email);

        // SQL 実行して結果を取得する
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        // 結果が空でなければメールアドレスが存在するか確認
        if (rs.isEmpty()) {
            return false;
        }

        // カウントが1以上なら重複しているとみなす
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    /**
     * メールアドレスの重複確認を行うメソッド(ユーザー情報編集時)
     * @param email 確認するメールアドレス
     * @return メールアドレスが重複していれば true、そうでなければ false
     * @throws AtareSysException
     */
    public boolean isEmailExists(String email, String ShiftId) throws AtareSysException {
        // メールアドレスが存在するかのカウントを行う
        String sql = "SELECT COUNT(*) FROM employee_shifts "
                + "WHERE email = " + DbS.chara(email)
                + "and id NOT IN (" + DbS.chara(ShiftId) + ")";

        // SQL 実行して結果を取得する
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        // 結果が空でなければメールアドレスが存在するか確認
        if (rs.isEmpty()) {
            return false;
        }

        // カウントが1以上なら重複しているとみなす
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    /**
     * ＩＤの重複確認を行うメソッド(新規登録時)
     * @param id 確認するＩＤ
     * @return ＩＤが重複していれば true、そうでなければ false
     * @throws AtareSysException
     */
    public boolean isIdExists(String id) throws AtareSysException {
        // ＩＤが存在するかのカウントを行う
        String sql = "SELECT COUNT(*) FROM employee_shifts "
                + "WHERE id = " + DbS.chara(id);

        // SQL 実行して結果を取得する
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        // 結果が空でなければＩＤが存在するか確認
        if (rs.isEmpty()) {
            return false;
        }

        // カウントが1以上なら重複しているとみなす
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    /**
     * ＩＤの重複確認を行うメソッド(ユーザー情報編集時)
     * @param id 確認するＩＤ
     * @return ＩＤが重複していれば true、そうでなければ false
     * @throws AtareSysException
     */
    public boolean isIdExists(String id, String ShiftId) throws AtareSysException {
        // ＩＤが存在するかのカウントを行う
        String sql = "SELECT COUNT(*) FROM employee_shifts "
                + "WHERE id = " + DbS.chara(id)
                + "and id NOT IN (" + DbS.chara(ShiftId) + ")";

        // SQL 実行して結果を取得する
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);

        // 結果が空でなければＩＤが存在するか確認
        if (rs.isEmpty()) {
            return false;
        }

        // カウントが1以上なら重複しているとみなす
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    /**
     * user_info ユーザ情報テーブルを検索し指定されたレコードのリストを返す
     * @param myclass        検索条件をUserInfoDaoのインスタンスに入れて渡す
     * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
     * @return 取得したUserInfoDaoの配列
     * @throws AtareSysException エラー
     */
    static public ArrayList<ShiftDAO> dbSelectListAll() throws AtareSysException {
        ArrayList<ShiftDAO> array = new ArrayList<ShiftDAO>();

        List<HashMap<String, String>> rs;
        HashMap<String, String> map;
        String sql = "select * "
                + " FROM employee_shifts ";
        rs = DbBase.dbSelect(sql);
        int cnt = rs.size();
        if (cnt < 1)
            return array;
        for (int i = 0; i < cnt; i++) {
            ShiftDAO dao = new ShiftDAO();
            map = rs.get(i);
            dao.setEmployeeShift(map, dao);
            array.add(dao);
        }
        return array;
    }

    /**
     * user_info ユーザ情報テーブルを検索し指定されたレコードのリストを返す
     * @param myclass        検索条件をUserInfoDaoのインスタンスに入れて渡す
     * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
     * @param daoPageInfo   取得したいページの番やライン数を入れる。結果がここに帰ってくる
     *                       ライン数に-1を入れると全件取得になる
     * @return 取得したUserInfoDaoの配列
     * @throws AtareSysException エラー
     */
    static public ArrayList<ShiftDAO> dbSelectList(ShiftDAO myclass, LinkedHashMap<String, String> sortKey,
            DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<ShiftDAO> array = new ArrayList<ShiftDAO>();

        // レコードの総件数を求める*/
        String sql = "select count(*) as count"
                + " from employee_shifts "
                + myclass.dbWhere();
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if (len == 0)
            return array;
        if (-1 == daoPageInfo.getLineCount())
            daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double) len / (double) (daoPageInfo.getLineCount())));
        if (daoPageInfo.getPageNo() < 1)
            daoPageInfo.setPageNo(1);
        if (daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo())
            daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql = "select "
                + "id as employee_shifts___id"
                + ",name as employee_shifts___name"
                + ",email as employee_shifts___email"
                + ",CONVERT(start_time, CHAR) as employee_shifts___start_time"
                + ",CONVERT(end_time, CHAR) as employee_shifts___end_time"
                + ",work_place as employee_shifts___work_place"
                + " from employee_shifts";

        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);
        sql += where;
        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";

        rs = DbBase.dbSelect(sql);
        int cnt = rs.size();
        if (cnt < 1)
            return array;

        for (int i = 0; i < cnt; i++) {
            map = rs.get(i);
            ShiftDAO dao = new ShiftDAO();
            dao.setEmployeeShift(map, dao);
            array.add(dao);
        }
        return array;
    }

    /**
     * user_info ユーザ情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException {
        StringBuilder where = new StringBuilder();

        if (getName() != null && !getName().isEmpty()) {
            where.append("name LIKE " + DbS.chara("%" + getName() + "%"));
        }
        if (getSearchName() != null && !getSearchName().isEmpty()) {
            where.append("name LIKE " + DbS.chara("%" + getSearchName() + "%"));
        }

        if (getWorkPlace() != null && !getWorkPlace().isEmpty()) {
            if (where.length() > 0) where.append(" AND ");
            where.append("work_place LIKE " + DbS.chara("%" + getWorkPlace() + "%"));
        }

        if (where.length() > 0) {
            return " WHERE " + where.toString();
        }

        return "";
    }

    /**
     * user_info ユーザ情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbOrder(LinkedHashMap<String, String> sortKey) {
        String str = "";
        if (sortKey == null)
            return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = i.next();
            if (null == fieldsArray.get(key))
                continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++) {
                if (j != 0)
                    str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }

    /**
     * ログイン処理のチェックを行う。
     *
     * @param pAccount アカウントまたはメールアドレス
     * @param pPassword パスワード
     * @return 0::失敗 1:成功 2:管理者ログイン
     * @throws AtareSysException
     *         エラー
     */
    public boolean login(String pAccount, String pPassword) throws AtareSysException {
        String sql = "";
        sql = " SELECT user_info.*"
                + " FROM user_info "
                + " WHERE "
                + " ( user_info_id  = " + DbS.chara(pAccount)
                + " or memail = " + DbS.chara(pAccount) + " ) ";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (1 != rs.size())
            return false;
        HashMap<String, String> map = rs.get(0);
        setEmployeeShift(map, this);
        String password = Digest.hex(Digest.SHA512, pPassword);
        if (!password.equals(DbI.chara(map.get("password")))) {
            return false;
        }
        return true;
    }

    /**
     * データベースからユーザー名を取得するメソッド
     * @return UserMenuに返す
     * @throws AtareSysException
     */
    public ArrayList<ShiftDAO> getAllUsers() throws AtareSysException {
        String sql = "SELECT * FROM user_info";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<ShiftDAO> users = new ArrayList<>();
        // 本日の日付を取得
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        for (HashMap<String, String> map : rs) {
            ShiftDAO user = new ShiftDAO();
            String stateFlg = map.get("state_flg");
            String leaveDateStr = map.get("leave_date");

            // `state_flg` が "9" かつ `leave_date` が本日より前の場合は表示しない
            boolean isStateFlgNine = "9".equals(stateFlg);
            boolean isLeaveDateBeforeToday = false;

            if (leaveDateStr != null && leaveDateStr.length() >= 8) {
                try {
                    Date leaveDate = dateFormat.parse(leaveDateStr);
                    isLeaveDateBeforeToday = !leaveDate.after(today);
                } catch (ParseException e) {
                    // `leave_date` の解析に失敗した場合は無視する
                    e.printStackTrace();
                }
            }
            if (isStateFlgNine && isLeaveDateBeforeToday) {
                continue;
            }
            // ユーザーDAOのインスタンスにデータを設定
            user.setUserInfoId(map.get("user_info_id"));
            user.setStateFlg(Integer.parseInt(stateFlg));
            user.setPasswordUser(map.get("password_user"));
            user.setPassword(map.get("password"));
            user.setLastName(map.get("last_name"));
            user.setMiddleName(map.get("middle_name"));
            user.setFirstName(map.get("first_name"));
            user.setMaidenName(map.get("maiden_name"));
            user.setLastNameKana(map.get("last_name_kana"));
            user.setMiddleNameKana(map.get("middle_name_kana"));
            user.setFirstNameKana(map.get("first_name_kana"));
            user.setMaidenNameKana(map.get("maiden_name_kana"));
            user.setInsertDate(map.get("insert_date"));
            user.setInsertUserId(map.get("insert_user_id"));
            user.setUpdateDate(map.get("update_date"));
            user.setUpdateUserId(map.get("update_user_id"));
            user.setMemail(map.get("memail"));
            user.setAdmin(map.get("admin"));
            users.add(user);
        }

        return users; // 取得したユーザーリストを返す
    }

    /**
     * メールアドレスからユーザーIDを取得します。
     *
     * @param email メールアドレス
     * @return ユーザーID。存在しない場合はnullを返します。
     * @throws AtareSysException データベースエラー
     */
    public String getUserInfoIdByEmail(String email) throws AtareSysException {
        String sql = "SELECT user_info_id FROM user_info WHERE memail = ?";
        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_info_id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new AtareSysException("メールアドレスからユーザーIDを取得中にエラーが発生しました: " + e.getMessage(), e);
        }
    }

    /**
     * トークンをDBへ保存するメソッド
     * @param email
     * @param token
     * @param expirationTime
     * @throws AtareSysException
     */
    public void saveToken(String email, String token, long expirationTime) throws AtareSysException {
        String userId = null;
        String getUserIdSql = "SELECT user_info_id FROM user_info WHERE memail = ?";

        try (PreparedStatement pstmtGetUserId = (PreparedStatement) DbBase.getDbConnection()
                .prepareStatement(getUserIdSql)) {

            pstmtGetUserId.setString(1, email);
            try (ResultSet rs = (ResultSet) pstmtGetUserId.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("user_info_id");
                } else {
                    throw new AtareSysException("User not found for email: " + email);
                }
            }

            String insertSql = "INSERT INTO repassword (user_info_id, memail, token, expires_at) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmtInsert = (PreparedStatement) DbBase.getDbConnection()
                    .prepareStatement(insertSql)) {
                pstmtInsert.setString(1, userId); // 取得した user_info_id を設定
                pstmtInsert.setString(2, email); // メールアドレス
                pstmtInsert.setString(3, token); // トークン
                pstmtInsert.setTimestamp(4, new Timestamp(expirationTime)); // expires_at カラムに保存
                pstmtInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new AtareSysException("Failed to save token", e);
        }
    }

    /**
     * トークンが有効化検証する
     * @param token
     * @return トークンが有効であれば true、無効であれば false
     * @throws AtareSysException
     */
    public boolean isValidToken(String token) throws AtareSysException {
        //トークンと対応する有効期限を取得するクエリ
        String sql = "SELECT expires_at FROM repassword WHERE token = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {

            pstmt.setString(1, token);

            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (rs.next()) {
                    //有効期限が現在時刻を超えていないか確認
                    Timestamp expiresAt = rs.getTimestamp("expires_at");
                    return expiresAt != null && System.currentTimeMillis() <= expiresAt.getTime();
                }
                return false;
            }

        } catch (SQLException e) {
            throw new AtareSysException("Failed to validate token", e);
        }
    }

    public String getUserIdByToken(String token) throws AtareSysException {
        String sql = "SELECT user_info.user_info_id FROM user_info " +
                "JOIN repassword ON user_info.user_info_id = repassword.user_info_id " +
                "WHERE repassword.token = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, token);
            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_info_id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new AtareSysException("Failed to get user ID by token", e);
        }
    }

    /**
     * トークンを使用してユーザーのパスワードを更新するメソッド。
     * 更新後にトークンを無効にする。
     * @param token
     * @param newPassword
     * @return パスワードの更新に成功した場合は true、失敗した場合は false
     * @throws AtareSysException
     */
    public boolean updatePassword(String token, String newPassword) throws AtareSysException {
        // パスワードのハッシュ化
        String hashedPassword;
        try {
            hashedPassword = hashPassword(newPassword);
        } catch (Exception e) {
            throw new AtareSysException(e);
        }

        // ユーザーのパスワードを更新し、トークンを無効にするSQLクエリ
        String updatePasswordSql = "UPDATE user_info JOIN repassword ON user_info.user_info_id = repassword.user_info_id "
                +
                "SET user_info.password = ?, repassword.expires_at = ? " +
                "WHERE repassword.token = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection()
                .prepareStatement(updatePasswordSql)) {
            pstmt.setString(1, hashedPassword); // 新しいパスワード
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis() - 1000)); // トークンを無効化するために過去の日付
            pstmt.setString(3, token); // トークン

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AtareSysException("Failed to update password", e);
        }
    }

    public String hashPassword(String password) throws Exception {
        // ソルトを生成
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // ソルトとパスワードを組み合わせてハッシュ化
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));

        // ソルトとハッシュ化されたパスワードを結合
        byte[] combined = new byte[salt.length + hashedPassword.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

        // Base64でエンコードして返す
        return java.util.Base64.getEncoder().encodeToString(combined);
    }

    private static final long EXPIRATION_TIME_MINUTES = 30; // 30分

    /**
     * 新しいトークンを生成するメソッド。
     * @return 生成したトークン
     */
    public static String generateToken() {
        return UUID.randomUUID().toString(); // UUID でトークンを生成
    }

    /**
     * トークンの有効期限をチェックするメソッド。
     * @param tokenGenerationTime トークンが生成された時刻
     * @return トークンが期限切れであれば true、そうでなければ false
     */
    public static boolean isTokenExpired(Instant tokenGenerationTime) {
        return Instant.now().isAfter(tokenGenerationTime.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES));
    }

}
