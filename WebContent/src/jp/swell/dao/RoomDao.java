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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * room 部屋テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class RoomDao implements Serializable
{
     /** Derializable No. */
    private static final long serialVersionUID = 1L;
    /**
     * データアクセス権限のあるユーザリスト
     */
    private ArrayList<String> authorityUserList = null;
    /**
     * roomId
     */
    private String roomId = "";
    /**
     * roomName
     */
    private String roomName = "";
    /**
     * insertDate 入力日時
     */
    private String insertDate = "";
    /**
     * insertUserId
     */
    private String insertUserId = "";
    /**
     * updateDate
     */
    private String updateDate = "";
    /**
     * updateUserId
     */
    private String updateUserId = "";
    /**
     * 予約用id
     */
    private String reserveId = "";
    /**
     * 予約用ユーザーID
     */
    private String userInfoId = "";
    /**
     * 予約用データ
     */
    private String reservationDate = "";
    /**
     * 予約用スタート時間
     */
    private String checkinTime = "";
    /**
     * 予約用終了時間
     */
    private String checkoutTime = "";
    /**
     * 予約用テキスト
     */
    private String inputText = "";
    /**
     * 予約用ユーザーID
     */
    private String userId = "";
    /**
     * 予約用 色
     */
    private String color = "";
    /**
     * 予約用 備考
     */
    private String inputRemark = "";


    /**
     *  データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList()
    {
        return authorityUserList;
    }
    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String,String> fieldsArray = new HashMap<String,String>();

    /**
     * コンストラクタ。
     */
    public RoomDao()
    {
        fieldsArray.put("room_id","room.room_id");
        fieldsArray.put("room_name","room.room_name");
        fieldsArray.put("insert_date","room.insert_date");
        fieldsArray.put("insert_user_id","room.insert_user_id");
        fieldsArray.put("update_date","room.update_date");
        fieldsArray.put("update_user_id","room.update_user_id");

    }
    /**
     * @return roomId
     */
    public String getRoomId() {
        return roomId;
    }
    /**
     * @param roomId セットする roomId
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * @return roomName
     */
    public String getRoomName() {
        return roomName;
    }
    /**
     * @param roomName セットする roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * @return insertDate
     */
    public String getInsertDate() {
        return insertDate;
    }
    /**
     * @param insertDate セットする insertDate
     */
    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    /**
     * @return insertUserId
     */
    public String getInsertUserId() {
        return insertUserId;
    }
    /**
     * @param insertUserId セットする insertUserId
     */
    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId;
    }

    /**
     * @return updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }
    /**
     * @param updateDate セットする updateDate
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return updateUserId
     */
    public String getUpdateUserId() {
        return updateUserId;
    }
    /**
     * @param updateUserId セットする updateUserId
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
    /**
     * @return reserveId
     */
    public String getReserveId() {
        return reserveId;
    }
    /**
     *
     * @param reserveId セットする reserveId
     */
    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }
    /**
     * @return userInfoId
     */
    public String getUserInfoId() {
        return userInfoId;
    }
    /**
     * @param userInfoId セットする userInfoId
     */
    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }
    /**
     * @return reservationDate
     */
    public String getReservationDate() {
        return reservationDate;
    }
    /**
     * @param reservationDate セットする reservationDate
     */
    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }
    /**
     * @return checkinTime
     */
    public String getCheckinTime() {
        return checkinTime;
    }
    /**
     * @param checkinTime セットする checkinTime
     */
    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }
    /**
     * @return checkOutTime
     */
    public String getCheckoutTime() {
        return checkoutTime;
    }
    /**
     * @param checkOutTime セットする checkOutTime
     */
    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }
    /**
     * @return inputText
     */
    public String getInputText() {
        return inputText;
    }
    /**
     * @param inputText セットする inputText
     */
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
    /**
     * @return userId
     */
    public String getUserId() {
        return userId;
    }
    /**
     * @param userId セットする userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * @return color
     */
    public String getColor() {
      return color;
    }
    /**
     * @param color セットする color
     */
    public void setColor(String color) {
      this.color = color;
    }
    /**
     * @return inputRemark
     */
    public String getInputRemark() {
      return inputRemark;
    }
    /**
     * @param inputRemark セットする inputRemar
     */
    public void setInputRemark(String inputRemark) {
      this.inputRemark = inputRemark;
    }

    /**
     * room 部屋テーブルを検索しroom 部屋テーブルの１行を取得します。.
     *
     * @param pRoomId   部屋ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pRoomId) throws AtareSysException {
        String sql = "SELECT "
                + "room.room_id as room___room_id, "
                + "room.room_name as room___room_name, "
                + "room.insert_date as room___insert_date, "
                + "room.insert_user_id as room___insert_user_id, "
                + "room.update_date as room___update_date, "
                + "room.update_user_id as room___update_user_id "
                + "FROM room "
                + "WHERE room_id = ?";

        try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
            pstmt.setString(1, pRoomId);

            try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("room___room_id", rs.getString("room___room_id"));
                map.put("room___room_name", rs.getString("room___room_name"));
                map.put("room___insert_date", rs.getString("room___insert_date"));
                map.put("room___insert_user_id", rs.getString("room___insert_user_id"));
                map.put("room___update_date", rs.getString("room___update_date"));
                map.put("room___update_user_id", rs.getString("room___update_user_id"));

                setRoomDaoForJoin(map, this);
                return true;
            } catch (SQLException e) {
                throw new AtareSysException("データベースクエリの実行中にエラーが発生しました: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new AtareSysException("データベース接続中にエラーが発生しました: " + e.getMessage(), e);
        }
    }

    /**
     * room 部屋テーブルを検索しroom 部屋テーブルの１行を取得します。.
     *
     * @param pRoomId  部屋ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pRoomId,String roomName) throws AtareSysException
    {
        String sql =  "select "
                + " room.room_id as room___room_id"
                + ",room.room_name as room___room_name"
                + ",room.insert_date as room___insert_date"
                + ",room.insert_user_id as room___insert_user_id"
                + ",room.update_date as room___update_date"
                + ",room.update_user_id as room___update_user_id"
        + " from room ";
        sql += ""
        + " where room_id = " + DbS.chara(pRoomId)
        + " and room_name = " + DbS.chara(roomName);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setRoomDaoForJoin(map,this);
        return true;
    }

    /**
     * RoomDao にroom 部屋テーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  RoomDaoこのテーブルのインスタンス
     */
    public void setRoomDao(HashMap<String, String> map,RoomDao dao)  throws AtareSysException
    {
        dao.setRoomId(DbI.chara(map.get("room_id")));
        dao.setRoomName(DbI.chara(map.get("room_name")));
        dao.setInsertDate(DbI.chara(map.get("insert_date")));
        dao.setInsertUserId(DbI.chara(map.get("insert_user_id")));
        dao.setUpdateDate(DbI.chara(map.get("update_date")));
        dao.setUpdateUserId(DbI.chara(map.get("update_user_id")));
    }

    /**
     *     RoomDao にroom 部屋テーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  RoomDaoこのテーブルのインスタンス
     */
    public void setRoomDaoForJoin(HashMap<String, String> map,RoomDao dao)  throws AtareSysException
    {
        dao.setRoomId(DbI.chara(map.get("room___room_id") != null ? map.get("room___room_id") : ""));
        dao.setRoomName(DbI.chara(map.get("room___room_name") != null ? map.get("room___room_name") : ""));
        dao.setInsertDate(DbI.chara(map.get("room___insert_date") != null ? map.get("room___insert_date") : ""));
        dao.setInsertUserId(DbI.chara(map.get("room___insert_user_id") != null ? map.get("room___insert_user_id") : ""));
        dao.setUpdateDate(DbI.chara(map.get("room___update_date") != null ? map.get("room___update_date") : ""));
        dao.setUpdateUserId(DbI.chara(map.get("room___update_user_id") != null ? map.get("room___update_user_id") : ""));
    }
    /**
     * room 部屋テーブルにデータを挿入する
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbInsert() throws AtareSysException
    {
        setRoomId(GetNumber.getNumberChar("room"));
        String sql="insert into room ("
        + " room_id"
        + ",room_name"
        + ",insert_date"
        + ",insert_user_id"
        + ",update_date"
        + ",update_user_id"
        + " ) values ( "
        + DbO.chara(getRoomId())
        + "," + DbO.chara(getRoomName())
        + "," + (getInsertDate().isEmpty() ? "null" : DbO.chara(getInsertDate()))
        + "," + (getInsertUserId().isEmpty() ? "null" : DbO.chara(getInsertUserId()))
        + "," + (getUpdateDate().isEmpty() ? "null" : DbO.chara(getUpdateDate()))
        + "," + (getUpdateUserId().isEmpty() ? "null" : DbO.chara(getUpdateUserId()))
        + " )";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbInsert number or record exception.") ;
        return true;
    }

    
    /**
     * room ルームテーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdate(String pRoomId) throws AtareSysException
    {
        String sql = "update room set "
        + " room_name = " + DbO.chara(getRoomName())
        + " where room_id = " + DbS.chara(pRoomId)
        + "";
        int ret =DbBase.dbExec(sql);
        if (ret != 1) throw new AtareSysException("dbupdate number or record exception");
        return true;
    }

    /**
     * room ルームテーブルからデータを削除する
     *
     * @param pRoomId   ユーザ情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラーs
     */
    public boolean dbDelete(String pRoomId) throws AtareSysException
    {
        String sql = "UPDATE room set "
        + "is_deleted = true WHERE room_id = " //"delete from room where "以前までは物理削除になっていた   //"UPDATE room set is_deleted = true"論理削除に変更
        + DbS.chara(pRoomId)
        +"";
        
        int ret =DbBase.dbExec(sql);
        
        if (ret != 1)
        	throw new AtareSysException("dbDelete number or record exception");
        return true;
    }
    /**
     * データベースからルーム名を取得するメソッド
     * @return UserMenuに返す
     * @throws AtareSysException
     */
    public ArrayList<RoomDao> getAllRooms() throws AtareSysException {
      String sql = "SELECT room_id, room_name FROM room;";
      List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
      ArrayList<RoomDao> rooms = new ArrayList<>();
      for (HashMap<String, String> map : rs) {
          RoomDao room = new RoomDao();
          // ルームDAOのインスタンスにデータを設定
          room.setRoomId(map.get("room_id"));
          room.setRoomName(map.get("room_name"));
          room.setInsertDate(map.get("insert_date"));
          room.setInsertUserId(map.get("insert_user_id"));
          room.setUpdateDate(map.get("update_date"));
          room.setUpdateUserId(map.get("update_user_id"));
          rooms.add(room);
      }

      return rooms; // 取得したルームリストを返す
    }
    static public ArrayList<RoomDao> dbSelectList(RoomDao myclass,LinkedHashMap<String,String> sortKey,DaoPageInfo daoPageInfo) throws AtareSysException
    {
        ArrayList<RoomDao> array = new ArrayList<RoomDao>();

        /* レコードの総件数を求める */
        String sql =  "select count(*) as count"
        + " from room "
        + myclass.dbWhere();
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if(len == 0)   return array;
        if(-1==daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double)len/(double)(daoPageInfo.getLineCount())));
        if(daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if(daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start  =   (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql =  "select "
                + " room.room_id room___room_id"
                + ",room.room_name room___room_name"
                + ",room.insert_date as room___insert_date"
                + ",room.insert_user_id as room___insert_user_id"
                + ",room.update_date as room___update_date"
                + ",room.update_user_id as room___update_user_id"
                + " from room ";
        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);
        sql += where;
        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";
        rs  =  DbBase.dbSelect(sql);
        int cnt = rs.size();
        if(cnt < 1)    return array;
        for(int i=0;i<cnt;i++)
        {
            RoomDao dao  = new RoomDao();
            map = rs.get(i);
            dao.setRoomDaoForJoin(map,dao);
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
    private String dbWhere() throws AtareSysException
    {
        StringBuffer where = new StringBuffer(1024);
        
        where.append("room.is_deleted = false"); //削除されたtrue以外を画面上に表示 (追加)

        if(getRoomId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.room_id LIKE " + DbS.chara("%" + getRoomId() + "%"));
        }

        if(getRoomName().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.room_name LIKE " + DbS.chara("%" + getRoomName() + "%"));
        }

        if(getInsertDate().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.insert_date LIKE " + DbS.chara("%" + getInsertDate() + "%"));
        }

        if(getInsertUserId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.insert_user_id LIKE " + DbS.chara("%" + getInsertUserId() + "%"));
        }

        if(getUpdateDate().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.update_date LIKE " + DbS.chara("%" + getUpdateDate() + "%"));
        }

        if(getUpdateUserId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("room.update_user_id LIKE " + DbS.chara("%" + getUpdateUserId() + "%"));
        }

        if(where.length()>0)
        {
            return "where " + where.toString();
        }
        return "";
    }

    /**
     * user_info ユーザ情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbOrder(LinkedHashMap<String, String> sortKey)
    {
        String str = "";
        if (sortKey == null) return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();)
        {
            String key = i.next();
            if (null == fieldsArray.get(key)) continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++)
            {
                if (j != 0) str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }

    
}