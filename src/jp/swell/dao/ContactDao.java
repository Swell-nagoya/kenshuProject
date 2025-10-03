/*
* (c)2010 2023 PATAPATA Corp. Corp. All Rights Reserved
*
* 機能名　　　　：DAOクラス
* ファイル名　　：ContactDao.java
* クラス名　　　：ContactDao
* 概要　　　　　：user_contact_info 連絡先情報テーブルのDAOを提供する。
* バージョン　　：
*
* 改版履歴　　　：
* 2018/09/21 <新規>    新規作成
* 2025/08/13 <改修>    user_contact_info専用に整理・堅牢化
*
*/
package jp.swell.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;

/**
 * user_contact_info 連絡先情報テーブルのDAOを提供する。
 */
public class ContactDao implements Serializable {
    private static final long serialVersionUID = 1L;

    // ====== フィールド（テーブルカラム対応） =====================================

    private int id; // 主キー
    private String lastName = "";
    private String middleName = "";
    private String firstName = "";
    private String lastNameKana = "";
    private String middleNameKana = "";
    private String firstNameKana = "";
    private String phoneNumber = "";
    private String email = "";
    private String insertDate = ""; // DBのNOW()で設定
    private int insertId; // 挿入ユーザID（数値）
    // 以降は必要に応じて拡張
    private String updateDate = "";
    private String updateUserId = "";
    private String loginEnableFrom = "";
    private String loginEnableTo = "";

    // ====== アクセサ ============================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = n(lastName);
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = n(middleName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = n(firstName);
    }

    public String getLastNameKana() {
        return lastNameKana;
    }

    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = n(lastNameKana);
    }

    public String getMiddleNameKana() {
        return middleNameKana;
    }

    public void setMiddleNameKana(String middleNameKana) {
        this.middleNameKana = n(middleNameKana);
    }

    public String getFirstNameKana() {
        return firstNameKana;
    }

    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = n(firstNameKana);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = n(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = n(email);
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = n(insertDate);
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = n(updateDate);
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = n(updateUserId);
    }

    public String getLoginEnableFrom() {
        return loginEnableFrom;
    }

    public void setLoginEnableFrom(String loginEnableFrom) {
        this.loginEnableFrom = n(loginEnableFrom);
    }

    public String getLoginEnableTo() {
        return loginEnableTo;
    }

    public void setLoginEnableTo(String loginEnableTo) {
        this.loginEnableTo = n(loginEnableTo);
    }

    // ====== 互換用のユーティリティ（既存JSP/コードの呼び方に合わせるため） ========

    /** 氏名を連結して返す（空は無視して結合） */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (!lastName.isEmpty())
            sb.append(lastName);
        if (!middleName.isEmpty()) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(middleName);
        }
        if (!firstName.isEmpty()) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(firstName);
        }
        return sb.toString();
    }

    /** 氏名かなを連結して返す */
    public String getFullNameKana() {
        StringBuilder sb = new StringBuilder();
        if (!lastNameKana.isEmpty())
            sb.append(lastNameKana);
        if (!middleNameKana.isEmpty()) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(middleNameKana);
        }
        if (!firstNameKana.isEmpty()) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(firstNameKana);
        }
        return sb.toString();
    }

    // 互換エイリアス（UserInfo系テンプレートを流用した箇所に合わせる）
    public String getUserInfoId() {
        return String.valueOf(this.id);
    }

    public void setUserInfoId(String userInfoId) {
        this.id = parseIntSafe(userInfoId, 0);
    }

    public String getMemail() {
        return this.email;
    }

    public void setMemail(String memail) {
        this.email = n(memail);
    }

    public String getTel() {
        return this.phoneNumber;
    }

    public void setTel(String tel) {
        this.phoneNumber = n(tel);
    }

    public String getContactId() {
        return String.valueOf(this.id);
    }

    public void setContactId(String contactId) {
        this.id = parseIntSafe(contactId, 0);
    }

    public String getInsertUserId() {
        return String.valueOf(this.insertId);
    }

    public void setInsertUserId(String insertUserId) {
        this.insertId = parseIntSafe(insertUserId, 0);
    }

    // ====== DB I/O =============================================================

    /** 単件 SELECT（id指定） */
    public boolean dbSelectByContactId(int id) throws AtareSysException {
        String sql = "SELECT * FROM user_contact_info WHERE id = " + id;
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty())
            return false;
        fillFromMap(rs.get(0));
        return true;
    }

    /** 全件 SELECT */
    public ArrayList<ContactDao> getAllContacts() throws AtareSysException {
        String sql = "SELECT * FROM user_contact_info";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<ContactDao> contacts = new ArrayList<>();

        for (HashMap<String, String> map : rs) {
            ContactDao c = new ContactDao();
            c.fillFromMap(map);
            contacts.add(c);
        }
        return contacts;
    }

    /** INSERT */
    public boolean dbInsert() throws AtareSysException {
        // insert_id は数値。0ならNULLで入れる（必要に応じて仕様変更可）
        String insertIdSql = (getInsertId() == 0) ? "NULL" : String.valueOf(getInsertId());

        String sql = "INSERT INTO user_contact_info ("
                + " last_name"
                + ", last_name_kana"
                + ", middle_name"
                + ", middle_name_kana"
                + ", first_name"
                + ", first_name_kana"
                + ", phone_number"
                + ", email"
                + ", insert_id"
                + ", insert_date"
                + ") VALUES ("
                + DbO.chara(getLastName())
                + ", " + DbO.chara(getLastNameKana())
                + ", " + DbO.chara(getMiddleName())
                + ", " + DbO.chara(getMiddleNameKana())
                + ", " + DbO.chara(getFirstName())
                + ", " + DbO.chara(getFirstNameKana())
                + ", " + DbO.chara(getPhoneNumber())
                + ", " + DbO.chara(getEmail())
                + ", " + insertIdSql
                + ", NOW()"
                + ")";

        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbInsert number or record exception.");
        return true;
    }

    /** UPDATE（主キーid指定） */
    public boolean dbUpdate(int id) throws AtareSysException {
        String sql = "UPDATE user_contact_info SET "
                + " last_name = " + DbO.chara(getLastName())
                + ", middle_name = " + DbO.chara(getMiddleName())
                + ", first_name = " + DbO.chara(getFirstName())
                + ", last_name_kana = " + DbO.chara(getLastNameKana())
                + ", middle_name_kana = " + DbO.chara(getMiddleNameKana())
                + ", first_name_kana = " + DbO.chara(getFirstNameKana())
                + ", phone_number = " + DbO.chara(getPhoneNumber())
                + ", email = " + DbO.chara(getEmail())
                + " WHERE id = " + id;

        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbUpdate number or record exception.");
        return true;
    }

    /** DELETE（主キーid指定） */
    public boolean dbDelete(int id) throws AtareSysException {
        String sql = "DELETE FROM user_contact_info WHERE id = " + id;
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception. id=" + id + ", ret=" + ret);
        return true;
    }

    // ====== 検索系ユーティリティ ================================================

    /** メール重複（新規用） */
    public boolean isEmailExists(String email) throws AtareSysException {
        String sql = "SELECT COUNT(*) AS cnt FROM user_contact_info WHERE email = " + DbS.chara(n(email));
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty())
            return false;
        String cntStr = rs.get(0).get("cnt");
        return cntStr != null && Integer.parseInt(cntStr) > 0;
    }

    /** メール重複（更新用：自分のIDを除外） */
    public boolean isEmailExists(String email, int excludeId) throws AtareSysException {
        String sql = "SELECT COUNT(*) AS cnt FROM user_contact_info "
                + "WHERE email = " + DbS.chara(n(email)) + " AND id <> " + excludeId;
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty())
            return false;
        String cntStr = rs.get(0).get("cnt");
        return cntStr != null && Integer.parseInt(cntStr) > 0;
    }

    // ====== 内部ヘルパ ==========================================================

    /** NULL安全な文字列化 */
    private static String n(String s) {
        return (s == null) ? "" : s;
    }

    /** 文字列→int（失敗時は既定値） */
    private static int parseIntSafe(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    /** DB結果1行を this に反映（キー欠落・NULLでも落ちない） */
    private void fillFromMap(HashMap<String, String> map) {
        if (map == null)
            return;
        this.id = parseIntSafe(map.get("id"), 0);
        this.lastName = n(map.get("last_name"));
        this.middleName = n(map.get("middle_name"));
        this.firstName = n(map.get("first_name"));
        this.lastNameKana = n(map.get("last_name_kana"));
        this.middleNameKana = n(map.get("middle_name_kana"));
        this.firstNameKana = n(map.get("first_name_kana"));
        this.phoneNumber = n(map.get("phone_number"));
        this.email = n(map.get("email"));
        this.insertId = parseIntSafe(map.get("insert_id"), 0);
        this.insertDate = n(map.get("insert_date"));
        // 任意の拡張カラムがあるならここで拾う
        this.updateDate = n(map.get("update_date"));
        this.updateUserId = n(map.get("update_user_id"));
    }

    // 既存互換：命名違いの単件SELECT（中身はdbSelectByContactIdと同じ）
    public boolean dbSelectContact(int id) throws AtareSysException {
        return dbSelectByContactId(id);
    }
}
