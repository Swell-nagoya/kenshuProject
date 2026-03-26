/*
 * (c)2010 2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * 機能名　　　　：DAOクラス
 * ファイル名　　：UserInfoDao.java
 * クラス名　　　：UserInfoDao
 * 概要　　　　　：user_info ユーザ情報テーブルのDAOを提供する。
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2018/09/21 <新規>    新規作成
 *
 */
package jp.swell.dao;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class UserInfoDao implements Serializable {
    /** Derializable No. */
    private static final long serialVersionUID = 1L;

    // データアクセス権限のあるユーザリスト
    private ArrayList<String> authorityUserList = null;

    // 姓名取得する
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

    // 姓名かなを取得する
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

    // user_info ユーザ情報テーブルで使用するメンバー変数

    // userInfoId  ユーザ情報ID
    private String userInfoId = "";

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    // stateFlg  状態フラグ
    private int stateFlg;

    public int getStateFlg() {
        return stateFlg;
    }

    public void setStateFlg(int stateFlg) {
        this.stateFlg = stateFlg;
    }

    // passwordUser  ユーザーパスワード
    private String passwordUser = "";

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    // password  パスワード
    private String password = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // lastName  姓
    private String lastName = "";

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // middleName  ミドルネーム
    private String middleName = "";

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    // firstName  名
    private String firstName = "";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // maidenName  旧姓
    private String maidenName = "";

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    // lastNameKana  姓よみ
    private String lastNameKana = "";

    public String getLastNameKana() {
        return lastNameKana;
    }

    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = lastNameKana;
    }

    // middleNameKana  ミドルネームよみ
    private String middleNameKana = "";

    public String getMiddleNameKana() {
        return middleNameKana;
    }

    public void setMiddleNameKana(String middleNameKana) {
        this.middleNameKana = middleNameKana;
    }

    // firstNameKana  名よみ
    private String firstNameKana = "";

    public String getFirstNameKana() {
        return firstNameKana;
    }

    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = firstNameKana;
    }

    // maidenNameKana  旧姓よみ
    private String maidenNameKana = "";

    public String getMaidenNameKana() {
        return maidenNameKana;
    }

    public void setMaidenNameKana(String maidenNameKana) {
        this.maidenNameKana = maidenNameKana;
    }

    // insertDate  入力日時
    private String insertDate = "";

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    // insertUserId  入力ユーザーＩＤ
    private String insertUserId = "";

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    // updateDate  アップデート日時
    private String updateDate = "";

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    // updateUserId  アップデートユーザーＩＤ
    private String updateUserId = "";

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    // leaveDate  退職予定日
    private String leaveDate = "";

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    // zipcode  郵便番号
    private String zipcode = "";

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    // address  住所
    private String address = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // station  最寄りの駅
    private String station = "";

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    // tel  電話番号
    private String tel = "";

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    // fax  ＦＡＸ
    private String fax = "";

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    // mtel  携帯電話番号
    private String mtel = "";

    public String getMtel() {
        return mtel;
    }

    public void setMtel(String mtel) {
        this.mtel = mtel;
    }

    // memail  携帯Eメール
    private String memail = "";

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    // adminFlag　管理者権限
    private String admin = "";

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    // passwordModifyDate  パスワード変更日時
    private String passwordModifyDate = "";

    public String getPasswordModifyDate() {
        return passwordModifyDate;
    }

    public void setPasswordModifyDate(String passwordModifyDate) {
        this.passwordModifyDate = passwordModifyDate;
    }

    // loginEnableFrom  ログイン可能期間FROM
    private String loginEnableFrom = "";

    public String getLoginEnableFrom() {
        return loginEnableFrom;
    }

    public void setLoginEnableFrom(String loginEnableFrom) {
        this.loginEnableFrom = loginEnableFrom;
    }

    // loginEnableTo  ログイン可能期間TO
    private String loginEnableTo = "";

    public String getLoginEnableTo() {
        return loginEnableTo;
    }

    public void setLoginEnableTo(String loginEnableTo) {
        this.loginEnableTo = loginEnableTo;
    }

    // searchFullName 検索用氏名
    private String searchFullName = "";
    // searchFullName 検索用氏名かな
    private String searchFullNameKana = "";

    public String getSearchFullName() {
        return searchFullName;
    }

    public void setSearchFullName(String searchFullName) {
        this.searchFullName = searchFullName;
    }

    public String getSearchFullNameKana() {
        return searchFullNameKana;
    }

    public void setSearchFullNameKana(String searchFullNameKana) {
        this.searchFullNameKana = searchFullNameKana;
    }

    private String[] userIds;

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String[] getUserIds() {
        return this.userIds;
    }

    // searchName  検索名
    private String searchName = "";

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    // データアクセス権限のあるユーザリストを取得する
    public ArrayList<String> getAuthorityUserList() {
        return authorityUserList;
    }

    // ソートフィールドのチェック時に使う。SQLインジェクション対策用
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    // ユーザー情報を全件取得する
    public List<UserInfoDao> getAllUsers1() throws Exception {
        List<UserInfoDao> userList = new ArrayList<>();

        // JDBCドライバ読み込み（初回のみ必要）
        Class.forName("com.mysql.jdbc.Driver");

        // DBに接続
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/your_database", "db_user", "db_password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_info");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserInfoDao user = new UserInfoDao();
                user.setUserInfoId(rs.getString("user_info_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                userList.add(user);
            }
        }

        return userList;
    }

    // コンストラクタ
    public UserInfoDao() {
        fieldsArray.put("user_info_id", "user_info.user_info_id");
        fieldsArray.put("password", "user_info.password");
        fieldsArray.put("last_name", "user_info.last_name");
        fieldsArray.put("middle_name", "user_info.middle_name");
        fieldsArray.put("first_name", "user_info.first_name");
        fieldsArray.put("maiden_name", "user_info.maiden_name");
        fieldsArray.put("last_name_kana", "user_info.last_name_kana");
        fieldsArray.put("middle_name_kana", "user_info.middle_name_kana");
        fieldsArray.put("first_name_kana", "user_info.first_name_kana");
        fieldsArray.put("maiden_name_kana", "user_info.maiden_name_kana");
        fieldsArray.put("insert_user_id", "user_info.insert_user_id");
        fieldsArray.put("admin", "user_info.admin");
    }

    // ユーザ情報テーブルを検索し1行を取得します（修正版）
    public boolean dbSelect(String pUserInfoId) throws AtareSysException {
        // エイリアス（user_info___...）を使わず、素直に全部取るSQLにする
        String sql = "SELECT * FROM user_info "
                + "WHERE user_info_id = " + DbS.chara(pUserInfoId);

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return false;

        HashMap<String, String> map = rs.get(0);
        
        // 単純な setUserInfoDao を使う
        setUserInfoDao(map, this);
        
        return true;
    }

    // ユーザ情報テーブルを検索し1行を取得します（パスワードチェック付き）
    public boolean dbSelect(String pUserInfoId, String pas) throws AtareSysException {
        // ここもシンプルに修正
        String sql = "SELECT * FROM user_info "
                + "WHERE user_info_id = " + DbS.chara(pUserInfoId)
                + " AND password = " + DbS.chara(pas);

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return false;

        HashMap<String, String> map = rs.get(0);
        setUserInfoDao(map, this);
        return true;
    }

    // 読み込んだデータを設定する（シンプル版）
    public void setUserInfoDao(HashMap<String, String> map, UserInfoDao dao) throws AtareSysException {
        dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
        dao.setPassword(DbI.chara(map.get("password")));
        dao.setLastName(DbI.chara(map.get("last_name")));
        dao.setMiddleName(DbI.chara(map.get("middle_name")));
        dao.setFirstName(DbI.chara(map.get("first_name")));
        dao.setMaidenName(DbI.chara(map.get("maiden_name")));
        dao.setLastNameKana(DbI.chara(map.get("last_name_kana")));
        dao.setMiddleNameKana(DbI.chara(map.get("middle_name_kana")));
        dao.setFirstNameKana(DbI.chara(map.get("first_name_kana")));
        dao.setMaidenNameKana(DbI.chara(map.get("maiden_name_kana")));
        dao.setAdmin(DbI.chara(map.get("admin")));
        dao.setLeaveDate(DbI.chara(map.get("leave_date")));
        dao.setMemail(DbI.chara(map.get("memail")));
        dao.setInsertUserId(DbI.chara(map.get("insert_user_id")));
        dao.setPasswordUser(DbI.chara(map.get("password_user")));
    }

    // 読み込んだデータを設定する（結合用・今回は使わないが残しておく）
    public void setUserInfoDaoForJoin(HashMap<String, String> map, UserInfoDao dao) throws AtareSysException {
        dao.setUserInfoId(DbI.chara(map.getOrDefault("user_info___user_info_id", "")));
        dao.setPassword(DbI.chara(map.getOrDefault("user_info___password", "")));
        dao.setLastName(DbI.chara(map.getOrDefault("user_info___last_name", "")));
        dao.setMiddleName(DbI.chara(map.getOrDefault("user_info___middle_name", "")));
        dao.setFirstName(DbI.chara(map.getOrDefault("user_info___first_name", "")));
        dao.setMaidenName(DbI.chara(map.getOrDefault("user_info___maiden_name", "")));
        dao.setLastNameKana(DbI.chara(map.getOrDefault("user_info___last_name_kana", "")));
        dao.setMiddleNameKana(DbI.chara(map.getOrDefault("user_info___middle_name_kana", "")));
        dao.setFirstNameKana(DbI.chara(map.getOrDefault("user_info___first_name_kana", "")));
        dao.setMaidenNameKana(DbI.chara(map.getOrDefault("user_info___maiden_name_kana", "")));
        dao.setInsertUserId(DbI.chara(map.getOrDefault("user_info___insert_user_id", "")));
        dao.setMemail(DbI.chara(map.getOrDefault("user_info___memail", "")));
        dao.setAdmin(DbI.chara(map.getOrDefault("user_info___admin", "")));
        dao.setLeaveDate(DbI.chara(map.getOrDefault("user_info___leave_date", "")));
    }

    // データを挿入する
    public boolean dbInsert() throws AtareSysException {
        // パスワードをSHA-512でハッシュ化
        String hashedPassword = Digest.hex(Digest.SHA512, this.password);

        // デバッグログ
        System.out.println("DAO_DEBUG: Insertする名前=[" + this.lastName + " " + this.firstName + "]");

        String sql = "insert into user_info ("
                + " user_info_id"
                + ",state_flg"
                + ",password_user"
                + ",password"
                + ",last_name"
                + ",middle_name"
                + ",first_name"
                + ",maiden_name"
                + ",last_name_kana"
                + ",middle_name_kana"
                + ",first_name_kana"
                + ",maiden_name_kana"
                + ",insert_date"
                + ",insert_user_id"
                + ",update_user_id"
                + ",memail"
                + ",admin"
                + ",leave_date"
                + " ) values ( "
                + DbO.chara(this.userInfoId)
                + ",1"
                + "," + DbO.chara(this.passwordUser)
                + "," + DbO.chara(hashedPassword) 
                + "," + DbO.chara(this.lastName)
                + "," + DbO.chara(this.middleName)
                + "," + DbO.chara(this.firstName)
                + "," + DbO.chara(this.maidenName)
                + "," + DbO.chara(this.lastNameKana)
                + "," + DbO.chara(this.middleNameKana)
                + "," + DbO.chara(this.firstNameKana)
                + "," + DbO.chara(this.maidenNameKana)
                + "," + "NOW(),"
                + DbO.chara(this.insertUserId)
                + "," + DbO.chara(this.updateUserId)
                + "," + DbO.chara(this.memail)
                + "," + DbO.chara(this.admin)
                + "," + DbO.chara(this.leaveDate)
                + " )";

        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbInsert number or record exception.");

        return true;
    }

    // データを更新する
    public boolean dbUpdate(String userInfoId) throws AtareSysException {
        String sql = "update user_info set "
                + " last_name = " + DbO.chara(getLastName())
                + "," + " middle_name = " + DbO.chara(getMiddleName())
                + "," + " first_name = " + DbO.chara(getFirstName())
                + "," + " maiden_name = " + DbO.chara(getMaidenName())
                + "," + " last_name_kana = " + DbO.chara(getLastNameKana())
                + "," + " middle_name_kana = " + DbO.chara(getMiddleNameKana())
                + "," + " first_name_kana = " + DbO.chara(getFirstNameKana())
                + "," + " maiden_name_kana = " + DbO.chara(getMaidenNameKana())
                + "," + " admin = " + DbO.chara(getAdmin())
                + "," + " update_date = NOW()"
                + "," + " update_user_id = " + DbO.chara(getUpdateUserId())
                + "," + " memail = " + DbO.chara(getMemail())
                + "," + " leave_date = " + DbO.chara(getLeaveDate())
                + " where user_info_id = " + DbS.chara(userInfoId)
                + "";
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbUpdate number or record exception.");
        return true;
    }

    // データを論理削除する（フラグを9にする）
    public boolean dbDelete(String userInfoId) throws AtareSysException {
        String sql = "update user_info set "
                + " state_flg = 9 "
                + " where user_info_id = " + DbS.chara(userInfoId);
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception.");
        return true;
    }

    // 削除をキャンセルする（フラグを1に戻す）
    public boolean dbCancelDelete(String userInfoId) throws AtareSysException {
        String sql = "update user_info set "
                + " state_flg = 1 "
                + " where user_info_id = " + DbS.chara(userInfoId);
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception.");
        return true;
    }

    // メールアドレスの重複確認（新規）
    public boolean isEmailExists(String email) throws AtareSysException {
        String sql = "SELECT COUNT(*) FROM user_info "
                + "WHERE memail = " + DbS.chara(email);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) {
            return false;
        }
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    // メールアドレスの重複確認（編集）
    public boolean isEmailExists(String email, String pUserInfoId) throws AtareSysException {
        String sql = "SELECT COUNT(*) FROM user_info "
                + "WHERE memail = " + DbS.chara(email)
                + "and user_info_id NOT IN (" + DbS.chara(pUserInfoId) + ")";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) {
            return false;
        }
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    // ＩＤの重複確認（新規）
    public boolean isIdExists(String id) throws AtareSysException {
        String sql = "SELECT COUNT(*) FROM user_info "
                + "WHERE insert_user_id = " + DbS.chara(id);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) {
            return false;
        }
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    // ＩＤの重複確認（編集）
    public boolean isIdExists(String id, String pUserInfoId) throws AtareSysException {
        String sql = "SELECT COUNT(*) FROM user_info "
                + "WHERE insert_user_id = " + DbS.chara(id)
                + "and user_info_id NOT IN (" + DbS.chara(pUserInfoId) + ")";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) {
            return false;
        }
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) > 0;
    }

    // 全件取得（未使用？）
    static public ArrayList<UserInfoDao> dbSelectListAll() throws AtareSysException {
        ArrayList<UserInfoDao> array = new ArrayList<UserInfoDao>();
        List<HashMap<String, String>> rs;
        HashMap<String, String> map;
        String sql = "select * from user_info ";
        rs = DbBase.dbSelect(sql);
        int cnt = rs.size();
        if (cnt < 1)
            return array;
        for (int i = 0; i < cnt; i++) {
            UserInfoDao dao = new UserInfoDao();
            map = rs.get(i);
            dao.setUserInfoDao(map, dao);
            array.add(dao);
        }
        return array;
    }

    // 指定された条件でレコードのリストを返す（一覧表示用）
    static public ArrayList<UserInfoDao> dbSelectList(UserInfoDao myclass, LinkedHashMap<String, String> sortKey,
            DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<UserInfoDao> array = new ArrayList<UserInfoDao>();

        // 1. レコード件数の取得
        String sqlCount = "select count(*) as count from user_info " + myclass.dbWhere();
        List<HashMap<String, String>> rsCount = DbBase.dbSelect(sqlCount);
        if (0 == rsCount.size()) return array;
        
        int len = Integer.parseInt(rsCount.get(0).get("count"));
        daoPageInfo.setRecordCount(len);
        if (len == 0) return array;

        // ページ計算
        if (-1 == daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double) len / (double) (daoPageInfo.getLineCount())));
        if (daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if (daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();

        // 2. データ取得（シンプル版）
        String sql = "select * from user_info ";
        
        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);
        sql += where;
        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start;

        // デバッグ出力
        System.out.println("DAO_SELECT_SQL: " + sql);

        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        int cnt = rs.size();
        if (cnt < 1) return array;

        for (int i = 0; i < cnt; i++) {
            HashMap<String, String> map = rs.get(i);
            UserInfoDao dao = new UserInfoDao();
            // 単純な setUserInfoDao を使う
            dao.setUserInfoDao(map, dao);
            array.add(dao);
        }
        return array;
    }

    // 検索条件を設定する
    String dbWhere() throws AtareSysException {
        StringBuffer where = new StringBuffer(1024);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String todayStr = dateFormat.format(today);

        if (getUserInfoId().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.user_info_id = " + DbS.chara(getUserInfoId()));
        }

        if (getLastName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.last_name = " + DbS.chara(getLastName()));
        }

        if (getMiddleName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.middle_name = " + DbS.chara(getMiddleName()));
        }

        if (getFirstName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.first_name = " + DbS.chara(getFirstName()));
        }

        if (getMaidenName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.maiden_name = " + DbS.chara(getMaidenName()));
        }

        if (getSearchFullName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("CONCAT(" + "IFNULL(user_info.last_name, ''), " + "IFNULL(user_info.first_name, ''), "
                    + "IFNULL(user_info.middle_name, ''), " + "IFNULL(user_info.maiden_name, '')" + ") LIKE "
                    + DbS.chara("%" + getSearchFullName() + "%"));
        }

        if (getLastNameKana().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.last_name_kana = " + DbS.chara(getLastNameKana()));
        }

        if (getMiddleNameKana().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.middle_name_kana = " + DbS.chara(getMiddleNameKana()));
        }

        if (getFirstNameKana().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.first_name_kana = " + DbS.chara(getFirstNameKana()));
        }

        if (getMaidenNameKana().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.maiden_name_kana = " + DbS.chara(getMaidenNameKana()));
        }

        if (getSearchFullNameKana().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append(
                    "CONCAT(" + "IFNULL(user_info.last_name_kana, ''), " + "IFNULL(user_info.middle_name_kana, ''), "
                            + "IFNULL(user_info.first_name_kana, ''), " + "IFNULL(user_info.maiden_name_kana, '')"
                            + ") LIKE " + DbS.chara("%" + getSearchFullNameKana() + "%"));
        }

        if (getSearchName().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("CONCAT(" + "IFNULL(user_info.last_name, ''), " + "IFNULL(user_info.first_name, ''), "
                    + "IFNULL(user_info.middle_name, ''), " + "IFNULL(user_info.maiden_name, ''), " +
                    "IFNULL(user_info.last_name_kana, ''), " + "IFNULL(user_info.middle_name_kana, ''), "
                    + "IFNULL(user_info.first_name_kana, ''), " + "IFNULL(user_info.maiden_name_kana, '')" + ") LIKE "
                    + DbS.chara("%" + getSearchName() + "%"));
        }

        if (userIds != null && userIds.length > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("user_info.user_info_id IN (");
            for (int i = 0; i < userIds.length; i++) {
                where.append(DbS.chara(userIds[i]));
                if (i < userIds.length - 1) {
                    where.append(", ");
                }
            }
            where.append(")");
        }

        where.append(where.length() > 0 ? " AND " : "");
        where.append("(state_flg != '9' OR (state_flg = '9' AND leave_date >= '" + todayStr + "'))");

        if (where.length() > 0) {
            return "where " + where.toString();
        }
        return "";
    }

    // 並べ替え順序を設定する
    String dbOrder(LinkedHashMap<String, String> sortKey) {
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

    // ログイン処理のチェック
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
        setUserInfoDao(map, this);
        String password = Digest.hex(Digest.SHA512, pPassword);
        if (!password.equals(DbI.chara(map.get("password")))) {
            return false;
        }
        return true;
    }

    // 全ユーザーを取得する
    public ArrayList<UserInfoDao> getAllUsers() throws AtareSysException {
        String sql = "SELECT * FROM user_info";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<UserInfoDao> users = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        for (HashMap<String, String> map : rs) {
            UserInfoDao user = new UserInfoDao();
            String stateFlg = map.get("state_flg");
            String leaveDateStr = map.get("leave_date");

            boolean isStateFlgNine = "9".equals(stateFlg);
            boolean isLeaveDateBeforeToday = false;

            if (leaveDateStr != null && leaveDateStr.length() >= 8) {
                try {
                    Date leaveDate = dateFormat.parse(leaveDateStr);
                    isLeaveDateBeforeToday = !leaveDate.after(today);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (isStateFlgNine && isLeaveDateBeforeToday) {
                continue;
            }
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

        return users;
    }

    // メールアドレスからユーザーIDを取得
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

    // トークンをDBへ保存する
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
                pstmtInsert.setString(1, userId);
                pstmtInsert.setString(2, email);
                pstmtInsert.setString(3, token);
                pstmtInsert.setTimestamp(4, new Timestamp(expirationTime));
                pstmtInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new AtareSysException("Failed to save token", e);
        }
    }

    // トークンが有効か検証する
    public boolean isValidToken(String token) throws AtareSysException {
        String sql = "SELECT expires_at FROM repassword WHERE token = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, token);
            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp expiresAt = rs.getTimestamp("expires_at");
                    return expiresAt != null && System.currentTimeMillis() <= expiresAt.getTime();
                }
                return false;
            }
        } catch (SQLException e) {
            throw new AtareSysException("Failed to validate token", e);
        }
    }

    // トークンからユーザーIDを取得
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

    // トークンを使用してパスワードを更新する
    public boolean updatePassword(String token, String newPassword) throws AtareSysException {
        String hashedPassword;
        try {
            hashedPassword = hashPassword(newPassword);
        } catch (Exception e) {
            throw new AtareSysException(e);
        }

        String updatePasswordSql = "UPDATE user_info JOIN repassword ON user_info.user_info_id = repassword.user_info_id "
                + "SET user_info.password = ?, repassword.expires_at = ? " +
                "WHERE repassword.token = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection()
                .prepareStatement(updatePasswordSql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis() - 1000));
            pstmt.setString(3, token);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AtareSysException("Failed to update password", e);
        }
    }

    // パスワードをハッシュ化する
    public String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));

        byte[] combined = new byte[salt.length + hashedPassword.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

        return java.util.Base64.getEncoder().encodeToString(combined);
    }

    private static final long EXPIRATION_TIME_MINUTES = 30;

    // 新しいトークンを生成
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // トークンの有効期限をチェック
    public static boolean isTokenExpired(Instant tokenGenerationTime) {
        return Instant.now().isAfter(tokenGenerationTime.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES));
    }

    public List<UserInfoDao> selectListWithPaging(int offset, int i) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}