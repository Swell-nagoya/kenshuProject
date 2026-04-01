/*
* (c)2010 2023 PATAPATA Corp. Corp. All Rights Reserved
*
* 機能名　　　　：DAOクラス
* ファイル名　　：Reserve.java
* クラス名　　　：ReserveDao
* 概要　　　　　：Reserve Reserve情報テーブルのDAOを提供する。
* バージョン　　：
*
* 改版履歴　　　：
* 2024/07/10 <新規>    新規作成
*
*/
package jp.swell.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;
import jp.patasys.common.db.GetNumber;

/**
 * Reserve Reserve情報テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class ReserveDao implements Serializable {
    /** Derializable No. */
    private static final long serialVersionUID = 1L;
    /**
     * データアクセス権限のあるユーザリスト
     */
    private ArrayList<String> authorityUserList = null;
    /**
     * reserveId 予約情報ID
     */
    private String reserveId = "";
    /**
     * userInfoId ユーザー情報ID
     */
    private String userInfoId = "";
    /**
     * roomId ルーム情報ID
     */
    private String roomId = "";
    /**
     * reservationDate 予約日情報
     */
    private String reservationDate = "";
    /**
     * checkinTime 開始時間
     */
    private String checkinTime = "";
    /**
     * checkOutTime 終了時間
     */
    private String checkoutTime = "";
    /**
     * inputText テキスト情報
     */
    private String inputText = "";
    /**
     * color 色
     */
    private String color = "";
    /**
     * inputRemark 備考情報
     */
    private String inputRemark = "";
    /**
     * insertDate 入力日時
     */
    private String insertDate = "";
    /**
     * insertUserId 入力ユーザーID
     */
    private String insertUserId = "";
    /**
     * updateDate アップデート日時
     */
    private String updateDate = "";
    /**
     * updateUserId アップデートユーザーID
     */
    private String updateUserId = "";
    /**
     * updateUserName アップデートユーザー名
     */
    private String updateUserName = "";
    /**
     * roomName 部屋名
     */
    private String roomName = "";
    /**
     * roomName ユーザー名
     */
    private String userName = "";
    /**
     * adminFlag 管理者権限
     */
    private String admin = "";
    /**
     * userReserveId ユーザー予約ID
     */
    private String userReserveId = "";
    /**
     * userInfoIds 複数ユーザーID
     */
    private String[] userInfoIds;
    /**
     * userNames 複数ユーザー名
     */
    private String[] userNames;
    /**
     * fileId ファイルID
     */
    private String fileId = "";

    /**
     * 予約情報IDを取得する。
     * @return reserveId ユーザ情報ID
     */
    public String getReserveId() {
        return reserveId;
    }

    /**
     * 予約情報IDをセットする
     * @param reserveId ユーザ情報ID
     */
    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    /**
     * ユーザ情報IDを取得する。
     * @return userInfoId ユーザ情報ID
     */
    public String getUserInfoId() {
        return userInfoId;
    }

    /**
     * ユーザ情報IDをセットする。
     * @param userInfoId ユーザ情報ID
     */
    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    /**
     * ルーム情報IDを取得する。
     * @return roomId ユーザ情報ID
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * ルーム情報IDをセットする。
     * @return roomId ユーザ情報ID
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * 予約日情報を取得する。
     * @return reservationDate 予約日情報
     */
    public String getReservationDate() {
        return reservationDate;
    }

    /**
     * 予約日情報をセットする。
     * @return reservationDate 予約日情報
     */
    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * 開始時間を取得する。
     * @return checkinTime 開始時間
     */
    public String getCheckinTime() {
        return checkinTime;
    }

    /**
     * 開始時間をセットする。
     * @return checkinTime 開始時間
     */
    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    /**
     * 終了時間を取得する。
     * @return checkOutTime 終了時間
     */
    public String getCheckoutTime() {
        return checkoutTime;
    }

    /**
     * 終了時間をセットする。
     * @return checkOutTime 終了時間
     */
    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    /**
     * テキスト情報を取得する。
     * @return inputText テキスト情報
     */
    public String getInputText() {
        return inputText;
    }

    /**
     * テキスト情報をセットする。
     * @return inputText テキスト情報
     */
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    /**
     * 色情報を取得する。
     * @return color 色
     */
    public String getColor() {
        return color;
    }

    /**
     * 色情報をセットする。
     * @return color 色
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 備考情報を取得する。
     * @return inputRemark 備考情報
     */
    public String getInputRemark() {
        return inputRemark;
    }

    /**
     * 備考情報をセットする。
     * @return inputRemark 備考情報
     */
    public void setInputRemark(String inputRemark) {
        this.inputRemark = inputRemark;
    }

    /**
     * 入力日時情報を取得する。
     * @return insertDate 入力日時
     */
    public String getInsertDate() {
        return insertDate;
    }

    /**
     * 入力日時情報をセットする。
     * @return insertDate 入力日時
     */
    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * 入力ユーザーIDを取得する。
     * @return insertUserId 入力ユーザーID
     */
    public String getInsertUserId() {
        return insertUserId;
    }

    /**
     * 入力ユーザーIDをセットする。
     * @return insertUserId 入力ユーザーID
     */
    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    /**
     * アップデート日時を取得する。
     * @return updateDate アップデート日時
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * アップデート日時をセットする。
     * @return updateDate アップデート日時
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * アップデートユーザーID情報を取得する。
     * @return updateUserId アップデートユーザーID
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * アップデートユーザーID情報をセットする。
     * @return updateUserId アップデートユーザーID
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * アップデートユーザー名の情報を取得する。
     * @return updateUserName アップデートユーザー名
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * アップデートユーザー名の情報をセットする。
     * @return updateUserName アップデートユーザー名
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    /**
     * 部屋名情報を取得する。
     * @return roomName 部屋名
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * 部屋名情報をセットする。
     * @param roomName 部屋名
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * ユーザー名情報を取得する。
     * @return userName ユーザー名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * ユーザー名情報をセットする。
     * @param userName ユーザー名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 管理者権限を取得する。
     * @return adminFlag 管理者権限
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * 管理者権限をセットする。.
     * @param adminFlag 管理者権限
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * 予約紐づけIDを取得する。
     * @return userReserveId 予約紐づけID
     */
    public String getUserReserveId() {
        return userReserveId;
    }

    /**
     * 予約紐づけIDをセットする。.
     * @param userReserveId 予約紐づけID
     */
    public void setUserReserveId(String userReserveId) {
        this.userReserveId = userReserveId;
    }

    /**
     * 複数ユーザーIDを取得する。
     * @return getUserInfoIds 複数ユーザーID
     */
    public String[] getUserInfoIds() {
        return userInfoIds;
    }

    /**
     * 複数ユーザーIDをセットする。.
     * @param setUserInfoIds 複数ユーザーID
     */
    public void setUserInfoIds(String[] userInfoId) {
        this.userInfoIds = userInfoId;
    }

    /**
     * 複数ユーザー名を取得する。
     * @return getUserNames 複数ユーザー名
     */
    public String[] getUserNames() {
        return userNames;
    }

    /**
     * 複数ユーザー名をセットする。.
     * @param setUserNames 複数ユーザー名
     */
    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }

    /**
     * ファイルIDを取得する。
     * @return getFileId ファイルID
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * ファイルIDをセットする。.
     * @param setFileId ファイルID
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private ArrayList<UserInfoDao> userIds;

    // userIdsをセットするメソッド
    public void setUserIds(ArrayList<UserInfoDao> users) {
        this.userIds = users;
    }

    // userIdsを取得するメソッド
    public ArrayList<UserInfoDao> getUserIds() {
        return this.userIds;
    }

    /**
     * データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList() {
        return authorityUserList;
    }

    public String selectMonth = "";

    /**
     * テキスト情報を取得する。
     * @return inputText テキスト情報
     */
    public String getSelectMonth() {
        return selectMonth;
    }

    /**
     * テキスト情報をセットする。
     * @return inputText テキスト情報
     */
    public void setSelectMonth(String selectMonth) {
        this.selectMonth = selectMonth;
    }

    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    /**
     * コンストラクタ。
     */
    public ReserveDao() {
        fieldsArray.put("reserve_id", "reserve.reserve_id");
        fieldsArray.put("user_info_id", "reserve.user_info_id");
        fieldsArray.put("user_full_name", "reserve.user_full_name");
        fieldsArray.put("room_id", "reserve.room_id");
        fieldsArray.put("room_name", "reserve.room_name");
        fieldsArray.put("reservation_date", "reserve.reservation_date");
        fieldsArray.put("checkin_time", "reserve.checkin_time");
        fieldsArray.put("checkout_time", "reserve.checkout_time");
        fieldsArray.put("input_text", "reserve.input_text");
        fieldsArray.put("rgb_color", "reserve.rgb_color");
        fieldsArray.put("input_remark", "reserve.input_remark");
        fieldsArray.put("insert_date", "reserve.insert_date");
        fieldsArray.put("insert_user_id", "reserve.insert_user_id");
        fieldsArray.put("update_date", "reserve.update_date");
        fieldsArray.put("update_user_id", "reserve.update_user_id");
        fieldsArray.put("last_name", "user_info.last_name");
        fieldsArray.put("middle_name", "user_info.middle_name");
        fieldsArray.put("first_name", "user_info.first_name");
        fieldsArray.put("room_name", "room.room_name");
        fieldsArray.put("user_reserve_id", "reserve.user_reserve_id");
        fieldsArray.put("file_id", "reserve_file.file_id");

    }

    /**
     * user_info ユーザ情報テーブルを検索しuser_info ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pReserveId) throws AtareSysException {
        String sql = "select "
                + " reserve.reserve_id as reserve___reserve_id"
                + ",reserve.user_info_id as reserve___user_info_id"
                + ",reserve.room_id as reserve___room_id"
                + ",reserve.reservation_date as reserve___reservation_date"
                + ",reserve.checkin_time as reserve___checkin_time"
                + ",reserve.checkout_time as reserve___checkout_time"
                + ",reserve.input_text as reserve___input_text"
                + ",reserve.rgb_color as reserve___rgb_color"
                + ",reserve.input_remark as reserve___input_remark"
                + ",reserve.insert_date as reserve___insert_date"
                + ",reserve.insert_user_id as reserve___insert_user_id"
                + ",reserve.update_date as reserve___update_date"
                + ",reserve.update_user_id as reserve___update_user_id"
                + ",reserve.user_reserve_id as reserve___user_reserve_id"
                + " from reserve ";
        sql += ""
                + " where reserve_id = " + DbS.chara(pReserveId);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return false;
        HashMap<String, String> map = rs.get(0);
        setReserveDaoForJoin(map, this);
        return true;
    }

    /**
     * user_info ユーザ情報テーブルを検索しuser_info ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pReserveId, String pas) throws AtareSysException {
        String sql = "select "
                + " reserve.reserve_id as reserve___reserve_id"
                + ",reserve.user_info_id as reserve___user_info_id"
                + ",reserve.room_id as reserve___room_id"
                + ",reserve.reservation_date as reserve___reservation_date"
                + ",reserve.checkin_time as reserve___checkin_time"
                + ",reserve.checkout_time as reserve___checkout_time"
                + ",reserve.input_text as reserve___input_text"
                + ",reserve.rgb_color as reserve___rgb_color"
                + ",reserve.input_remark as reserve___input_remark"
                + ",reserve.insert_date as reserve___insert_date"
                + ",reserve.insert_user_id as reserve___insert_user_id"
                + ",reserve.update_date as reserve___update_date"
                + ",reserve.update_user_id as reserve___update_user_id"
                + ",reserve.user_reserve_id as reserve___user_reserve_id"
                + " from reserve ";
        sql += ""
                + " where reserve_id = " + DbS.chara(pReserveId)
                + " and password = " + DbS.chara(pas);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (0 == rs.size())
            return false;
        HashMap<String, String> map = rs.get(0);
        setReserveDaoForJoin(map, this);
        return true;
    }

    /**
     * ReserveDao にreserve情報テーブルから読み込んだデータを設定する。.
     *
     * @param map 読み込んだテーブルの１レコードが入っているHashMap
     * @param dao ReserveDaoこのテーブルのインスタンス
     */
    public void setReserveDao(HashMap<String, String> map, ReserveDao dao) throws AtareSysException {
        dao.setReserveId(DbI.chara(map.get("reserve_id")));
        dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
        dao.setRoomId(DbI.chara(map.get("room_id")));
        dao.setReservationDate(DbI.chara(map.get("reservation_date")));
        dao.setCheckinTime(DbI.chara(map.get("checkin_time")));
        dao.setCheckoutTime(DbI.chara(map.get("checkout_time")));
        dao.setInputText(DbI.chara(map.get("input_text")));
        dao.setColor(DbI.chara(map.get("rgb_color")));
        dao.setInputRemark(DbI.chara(map.get("input_remark")));
        dao.setInsertDate(DbI.chara(map.get("insert_date")));
        dao.setInsertUserId(DbI.chara(map.get("insert_user_id")));
        dao.setUpdateDate(DbI.chara(map.get("update_date")));
        dao.setUpdateUserId(DbI.chara(map.get("update_user_id")));
        dao.setUserReserveId(DbI.chara(map.get("user_reserve_id")));
    }

    /**
     * ReserveDao にreserve情報テーブルから読み込んだデータを設定する。.
     *
     * @param map 読み込んだテーブルの１レコードが入っているHashMap
     * @param dao ReserveDaoこのテーブルのインスタンス
     */
    public void setReserveDaoForJoin(HashMap<String, String> map, ReserveDao dao) throws AtareSysException {
        dao.setReserveId(DbI.chara(map.get("reserve___reserve_id")));
        dao.setUserInfoId(DbI.chara(map.get("reserve___user_info_id")));
        dao.setRoomId(DbI.chara(map.get("reserve___room_id")));
        dao.setReservationDate(DbI.chara(map.get("reserve___reservation_date")));
        dao.setCheckinTime(DbI.chara(map.get("reserve___checkin_time")));
        dao.setCheckoutTime(DbI.chara(map.get("reserve___checkout_time")));
        dao.setInputText(DbI.chara(map.get("reserve___input_text")));
        dao.setColor(DbI.chara(map.get("reserve___rgb_color")));
        dao.setInputRemark(DbI.chara(map.get("reserve___input_remark")));
        dao.setInsertDate(DbI.chara(map.get("reserve___insert_date")));
        dao.setInsertUserId(DbI.chara(map.get("reserve___insert_user_id")));
        dao.setUpdateDate(DbI.chara(map.get("reserve___update_date")));
        dao.setUpdateUserId(DbI.chara(map.get("reserve___update_user_id")));
        dao.setUserReserveId(DbI.chara(map.get("reserve___user_reserve_id")));
    }

    /**
     * reserve 予約情報テーブルにデータを挿入する
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbInsertReserve() throws AtareSysException {
        setReserveId(GetNumber.getNumberChar("reserve"));
        setUserReserveId(GetNumber.getNumberChar("userReserve"));
        String sql = "insert into reserve ("
                + " reserve_id"
                + ", user_info_id"
                + ", room_id"
                + ", reservation_date"
                + ", checkin_time"
                + ", checkout_time"
                + ", input_text"
                + ", insert_user_id"
                + ", rgb_color"
                + ", input_remark"
                + ", insert_date"
                + ", update_user_id"
                + ", user_reserve_id"
                + ") values ("
                + DbO.chara(getReserveId())
                + "," + DbO.chara(getUserInfoId())
                + "," + DbO.chara(getRoomId())
                + "," + DbO.chara(getReservationDate())
                + "," + DbO.chara(getCheckinTime())
                + "," + DbO.chara(getCheckoutTime())
                + "," + DbO.chara(getInputText())
                + "," + DbO.chara(getInsertUserId())
                + "," + DbO.chara(getColor())
                + "," + DbO.chara(getInputRemark())
                + ", NOW()" // データベース側で現在日時を取得
                + "," + DbO.chara(getUpdateUserId())
                + "," + DbO.chara(getUserReserveId())
                + ")";
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbInsertReserve number or record exception.");
        return true;
    }

    /**
     * reserve 予約情報テーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdateReserve() throws AtareSysException {
        String sql = "update reserve set "
                + " user_info_id = " + DbO.chara(getUserInfoId())
                + "," + " room_id = " + DbO.chara(getRoomId())
                + "," + " reservation_date = " + DbO.chara(getReservationDate())
                + "," + " checkin_time = " + DbO.chara(getCheckinTime())
                + "," + " checkout_time = " + DbO.chara(getCheckoutTime())
                + "," + " input_text = " + DbO.chara(getInputText())
                + "," + " insert_user_id = " + DbO.chara(getInsertUserId())
                + "," + " rgb_color = " + DbO.chara(getColor())
                + "," + " input_remark = " + DbO.chara(getInputRemark())
                + "," + " update_date = NOW()" // データベース側で現在日時を取得
                + "," + " update_user_id = " + DbO.chara(getUpdateUserId())
                + " where reserve_id = " + DbS.chara(reserveId)
                + "";
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbUpdate number or record exception.");
        return true;
    }

    /**
     * reserve 予約情報テーブルからデータを削除する
     *
     * @param pReserveId   予約情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbDeleteReserve(String pReserveId) throws AtareSysException {
        String sql = "delete from reserve "
                + " where reserve_id = " + DbS.chara(reserveId);
        int ret = DbBase.dbExec(sql);
        if (ret != 1)
            throw new AtareSysException("dbDelete number or record exception.");
        return true;
    }

    /**
     * 予約日時の重複していないかの確認するメソッド
     * @param roomId 部屋のID
     * @param reservationDate 予約の日付
     * @param checkinTime 予約の開始時間
     * @param checkoutTime 予約に終了時間
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public boolean Duplication() throws AtareSysException {
        // 被る予約数のカウントする
        String sql = "SELECT COUNT(*) FROM reserve "
                + "WHERE room_id = " + DbS.chara(getRoomId())
                + " AND reservation_date = " + DbS.chara(getReservationDate())
                + " AND ("
                + " (checkin_time < " + DbS.chara(getCheckoutTime()) + " AND checkout_time > "
                + DbS.chara(getCheckinTime()) + ")"
                + ")";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if (rs.isEmpty()) {
            return false;
        }
        return Integer.parseInt(rs.get(0).get("COUNT(*)")) == 0;
    }

    /**
     * データベースからルーム名を取得するメソッド
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public ArrayList<ReserveDao> getAllReserves() throws AtareSysException {
        String sql = "SELECT reserve_id"
                + ", user_info_id"
                + ", room_id"
                + ", reservation_date"
                + ", checkin_time"
                + ", checkout_time"
                + ", input_text"
                + ", insert_user_id"
                + ", rgb_color"
                + ", input_remark"
                + ", insert_user_id"
                + ", update_user_id"
                + ", user_reserve_id FROM reserve";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<ReserveDao> reserves = new ArrayList<>();
        for (HashMap<String, String> map : rs) {
            ReserveDao reserve = new ReserveDao();
            // ReserveDAOのインスタンスにデータを設定
            reserve.setReserveId(map.get("reserve_id"));
            reserve.setUserInfoId(map.get("user_info_id"));
            reserve.setRoomId(map.get("room_id"));
            reserve.setReservationDate(map.get("reservation_date"));
            reserve.setCheckinTime(map.get("checkin_time"));
            reserve.setCheckoutTime(map.get("checkout_time"));
            reserve.setInputText(map.get("input_text"));
            reserve.setColor(map.get("rgb_color"));
            reserve.setInputRemark(map.get("input_remark"));
            reserve.setInsertUserId(map.get("insert_user_id"));
            reserve.setUpdateUserId(map.get("update_user_id"));
            reserve.setUserReserveId(map.get("user_reserve_id"));
            reserves.add(reserve);
        }

        return reserves; // 取得したルームリストを返す
    }

    /**
     * データベースからカレンダー用に全ての予約を取得するメソッド
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public ArrayList<ReserveDao> getCalendarReserves() throws AtareSysException {
        String sql = "SELECT reserve_id"
                + ", reserve.user_info_id"
                + ", last_name"
                + ", middle_name"
                + ", first_name"
                + ", reserve.room_id"
                + ", room_name"
                + ", reservation_date"
                + ", checkin_time"
                + ", checkout_time\r\n"
                + "FROM reserve\r\n"
                + "JOIN user_info on reserve.user_info_id = user_info.user_info_id\r\n"
                + "JOIN room on reserve.room_id = room.room_id\r\n"
                + "ORDER BY reservation_date ASC, checkin_time ASC;";
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        ArrayList<ReserveDao> reserves = new ArrayList<>();
        for (HashMap<String, String> map : rs) {
            ReserveDao reserve = new ReserveDao();
            // ReserveDAOのインスタンスにデータを設定
            reserve.setReserveId(map.get("reserve_id"));
            reserve.setUserInfoId(map.get("user_info_id"));
            reserve.setUserName(map.get("last_name") + map.get("middle_name") + map.get("first_name"));
            reserve.setRoomId(map.get("room_id"));
            reserve.setRoomName(map.get("room_name"));
            reserve.setReservationDate(map.get("reservation_date"));
            reserve.setCheckinTime(map.get("checkin_time"));
            reserve.setCheckoutTime(map.get("checkout_time"));
            reserves.add(reserve);
        }

        return reserves; // 取得したルームリストを返す
    }

    /**
     * 予約情報を表示
     * @return UserYoyakuDetailに返す
     * @throws AtareSysException
     */
    public ReserveDao getMostReserve() throws AtareSysException {
        // SQLクエリを最新の予約を取得する
        String sql = "SELECT * FROM reserve ORDER BY reservation_date DESC LIMIT 1";
        // クエリを実行して結果を取得
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        // 結果が空でないかをチェック
        if (rs.isEmpty()) {
            return null; // 予約が見つからなかった場合はnullを返す
        }
        // 最初の結果を取り出す
        HashMap<String, String> map = rs.get(0);
        // ReserveDaoのインスタンスを作成
        ReserveDao reserve = new ReserveDao();
        // ReserveDaoのインスタンスにデータを設定
        reserve.setUserInfoId(map.get("user_info_id"));
        reserve.setRoomId(map.get("room_id"));
        reserve.setReservationDate(map.get("reservation_date"));
        reserve.setCheckinTime(map.get("checkin_time"));
        reserve.setCheckoutTime(map.get("checkout_time"));
        reserve.setColor(map.get("rgb_color"));
        reserve.setRoomName(map.get("room_name"));

        // 最新の予約を返す
        return reserve;
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
    static public ArrayList<ReserveDao> dbSelectList(ReserveDao myclass, LinkedHashMap<String, String> sortKey,
            DaoPageInfo daoPageInfo) throws AtareSysException {
        ArrayList<ReserveDao> array = new ArrayList<ReserveDao>();

        /* レコードの総件数を求める */
        String sql = "select count(*) as count"
                + " from reserve "
                + " join user_info on reserve.user_info_id = user_info.user_info_id "
                + " join room on reserve.room_id = room.room_id "
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
                + " reserve.reserve_id as reserve___reserve_id"
                + ",reserve.user_info_id as reserve___user_info_id"
                + ",reserve.room_id as reserve___room_id"
                + ",reserve.reservation_date as reserve___reservation_date"
                + ",reserve.checkin_time as reserve___checkin_time"
                + ",reserve.checkout_time as reserve___checkout_time"
                + ",reserve.input_text as reserve___input_text"
                + ",reserve.rgb_color as reserve___rgb_color"
                + ",reserve.input_remark as reserve___input_remark"
                + ",reserve.insert_date as reserve___insert_date"
                + ",reserve.insert_user_id as reserve___insert_user_id"
                + ",reserve.update_date as reserve___update_date"
                + ",reserve.update_user_id as reserve___update_user_id"
                + ",reserve.user_reserve_id as reserve___user_reserve_id"
                + ",user_info.last_name as user_info___last_name"
                + ",user_info.middle_name as user_info___middle_name"
                + ",user_info.first_name as user_info___first_name"
                + ",user_info.state_flg as user_info___state_flg"
                + ",room.room_name as room___room_name"
                + " from reserve "
                + " join user_info on reserve.user_info_id = user_info.user_info_id "
                + " join room on reserve.room_id = room.room_id ";
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
            {
                ReserveDao dao = new ReserveDao();
                dao.setReserveDaoForJoin(map, dao);

                array.add(dao);
            }
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
        StringBuilder where = new StringBuilder(" where 1=1 ");

        // ユーザーが退会済み（state_flg = 9）じゃないことを追加
        where.append(" and user_info.state_flg != '9'");

        if (getReservationDate() != null && !getReservationDate().isEmpty()) {
            where.append(" AND reserve.reservation_date LIKE ").append(DbS.chara("%" + getReservationDate() + "%"));
        }

        if (getUserName() != null && !getUserName().isEmpty()) {
            String keyword = DbS.chara("%" + getUserName() + "%");
            where.append(" AND (user_info.last_name LIKE ").append(keyword)
                    .append(" OR user_info.middle_name LIKE ").append(keyword)
                    .append(" OR user_info.first_name LIKE ").append(keyword).append(")");
        }

        if (getRoomName() != null && !getRoomName().isEmpty()) {
            where.append(" AND room.room_name LIKE ").append(DbS.chara("%" + getRoomName() + "%"));
        }

        if (getCheckinTime() != null && !getCheckinTime().isEmpty()) {
            where.append(" AND reserve.checkin_time LIKE ").append(DbS.chara("%" + getCheckinTime() + "%"));
        }

        if (getCheckoutTime() != null && !getCheckoutTime().isEmpty()) {
            where.append(" AND reserve.checkout_time LIKE ").append(DbS.chara("%" + getCheckoutTime() + "%"));
        }

        if (getUserInfoId() != null && !getUserInfoId().isEmpty()) {
            where.append(" AND user_info.user_info_id = ").append(DbS.chara(getUserInfoId()));
        }

        return where.toString();
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

}