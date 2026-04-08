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
import java.util.UUID;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;

public class AnnouncementDao implements Serializable {

	/** Derializable No. */
	private static final long serialVersionUID = 1L;

	/**
	 * データアクセス権限のあるユーザリスト
	 */
	private ArrayList<String> authorityUserList = null;

	/**
	 *  データアクセス権限のあるユーザリストを取得する。.
	 */
	public ArrayList<String> getAuthorityUserList() {
		return authorityUserList;
	}

	/**
	 * file_id
	 */
	private String AnnoId = "";

	/**
	 * 
	 * @return fileId
	 */
	public String getAnnoId() {
		return AnnoId;
	}

	/**
	 * 
	 * @param fileId セットする fileId
	 */
	public void setAnnoId(String AnnoId) {
		this.AnnoId = AnnoId;
	}

	/**
	 * user_info_id
	 */
	private String userInfoId = "";

	/**
	 * 
	 * @return userInfoId
	 */
	public String getUserInfoId() {
		return userInfoId;
	}

	/**
	 * 
	 * @param userInfoId セットする userInfoId
	 */
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	/**
	 * upload_data 登録日時
	 */
	private String updateDate = "";

	/**
	 * 
	 * @return uploadDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * 
	 * @param uploadDate セットする uploadDate
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * first_name 名字
	 */
	private String firstName = "";

	/**
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName セットする firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * last_name 名前
	 */
	private String lastName = "";

	/**
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName セットする lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * last_name 名前
	 */
	private String middleName = "";

	/**
	 * 
	 * @return lastName
	 */
	public String getmiddleName() {
		return middleName;
	}

	/**
	 * 
	 * @param lastName セットする lastName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * searchFileName 検索用氏名
	 */
	private String searchFileName = "";

	/**
	 * searchFileName 検索用氏名を取得します。
	 *
	 * @return searchFileName 検索用氏名
	 */
	public String getSearchFileName() {
		return searchFileName;
	}

	/**
	 * searchFileName 検索用氏名を設定します。
	 *
	 * @param searchFileName 検索用氏名
	 */
	public void setSearchFileName(String searchFileName) {
		this.searchFileName = searchFileName;
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

	private String admin;

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getAdmin() {
		return admin;
	}

	private String title;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	private String text;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	/**
	 * anno テーブルを検索し １行を取得します。.
	 *
	 * @param pannoId
	 * @return true:読み込み成功 false:存在しない
	 * @throws AtareSysException フレームワーク共通例外
	 */
	public boolean dbSelect(String pAnnoId) throws AtareSysException {
		String sql = "SELECT "
				+ "anno_id, "
				+ "user_info_id, "
				+ "title , "
				+ "update_date, "
				+ "admin, "
				+ "text "
				+ "from anno "
				+ "where anno_id = ? ";
		try (PreparedStatement pstmt = (PreparedStatement) DbBase.getDbConnection().prepareStatement(sql)) {
			pstmt.setString(1, pAnnoId);

			try (ResultSet rs = (ResultSet) pstmt.executeQuery()) {
				if (!rs.next()) {
					return false;
				}

				HashMap<String, String> map = new HashMap<>();
				map.put("anno_id", rs.getString("anno_id"));
				map.put("user_info_id", rs.getString("user_info_id"));
				map.put("title", rs.getString("title"));
				map.put("update_date", rs.getString("update_date"));
				map.put("admin", rs.getString("admin"));
				map.put("text", rs.getString("text"));

				setAnnousementDao(map, this);
				return true;
			} catch (SQLException e) {
				throw new AtareSysException("データベースクエリの実行中にエラーが発生しました: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			throw new AtareSysException("データベース接続中にエラーが発生しました: " + e.getMessage(), e);
		}
	}

	/**
	 * files ファイルテーブルを検索し fileテーブルの１行を取得します。.
	 *
	 * @param pAnnoId  部屋ID
	 * @return true:読み込み成功 false:存在しない
	 * @throws AtareSysException フレームワーク共通例外
	 */
	public boolean dbSelect(String pAnnoId, String title) throws AtareSysException {
		String sql = "select "
				+ "anno.anno_id, "
				+ "anno.user_info_id, "
				+ "anno.title , "
				+ "anno.update_date, "
				+ "anno.admin, "
				+ "anno.text ,"
				+ "FROM anno  ";

		sql += ""
				+ " where anno_id = " + DbS.chara(pAnnoId)
				+ " and title = " + DbS.chara(title);
		List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
		if (0 == rs.size())
			return false;
		HashMap<String, String> map = rs.get(0);
		setAnnousementDao(map, this);
		return true;
	}

	/**
	 * FileDao にfiles ファイルテーブルから読み込んだデータを設定する。.
	 *
	 * @param map  読み込んだテーブルの１レコードが入っているHashMap
	 * @param dao  FileDaoこのテーブルのインスタンス
	 */
	public void setAnnousementDao(HashMap<String, String> map, AnnouncementDao dao) throws AtareSysException {
		dao.setAnnoId(DbI.chara(map.get("anno_id")));
		dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
		dao.setTitle(DbI.chara(map.get("title")));
		dao.setUpdateDate(DbI.chara(map.get("update_date")));
		dao.setAdmin(DbI.chara(map.get("admin")));
		dao.setText(DbI.chara(map.get("text")));

	}

	/**
	 * 新しいトークンを生成するメソッド。
	 * @return 生成したトークン
	 */
	public static String FileKey() {
		return UUID.randomUUID().toString(); // UUID でトークンを生成
	}

	public boolean dbAnnousementInsert(String pannoId, String puserInfoId, String padmin, String ptext, String ptitle)
			throws AtareSysException {
		setAnnoId(pannoId);
		setUserInfoId(puserInfoId);
		setAdmin(padmin);
		setText(ptext);
		setTitle(ptitle);

		String sql = "insert into anno ("
				+ " anno_id"
				+ ",user_info_id"
				+ ",update_date"
				+ ",admin "
				+ ",text"
				+ ",title "
				+ " ) values ( "
				+ DbO.chara(pannoId)
				+ "," + DbO.chara(puserInfoId)
				+ "," + "NOW()"
				+ "," + DbO.chara(padmin)
				+ "," + DbO.chara(ptext)
				+ "," + DbO.chara(ptitle)
				+ " )";
		int ret = DbBase.dbExec(sql);
		if (ret != 1)
			throw new AtareSysException("dbInsert number or record exception.");
		return true;
	}

	/**
	 * DB登録直後に登録したfileIdを返す
	 * @return fileId
	 * @throws AtareSysException
	 */
	public String getLastInsertedAnnoId() throws AtareSysException {
		String sql = "SELECT LAST_INSERT_ID()";
		int anno_id = DbBase.dbExec(sql); // DB操作の結果を取得
		return String.valueOf(anno_id); // 取得したIDを返す
	}

	/**
		 * room ルームテーブルからデータを削除する
		 *
		 * @param pRoomId   ユーザ情報ID
		 * @return true:成功 false:失敗
		 * @throws AtareSysException エラーs
		 */
	public boolean dbDelete(String pannoId) throws AtareSysException {
		// filesテーブルからレコードを削除
		String sqlDeleteAnno = "DELETE FROM anno WHERE anno_id = " + DbS.chara(pannoId);
		int retAnno = DbBase.dbExec(sqlDeleteAnno);
		if (retAnno != 1)
			throw new AtareSysException("dbDelete number or record exception");
		return true;
	}

	/**
		 * データベースからルーム名を取得するメソッド
		 * @return UserMenuに返す
		 * @throws AtareSysException
		 */

	public static ArrayList<AnnouncementDao> dbSelectList(AnnouncementDao myclass,
			LinkedHashMap<String, String> sortKey,
			DaoPageInfo daoPageInfo) throws AtareSysException {
		ArrayList<AnnouncementDao> resultList = new ArrayList<>();
		// WHERE句
		String where = myclass.dbWhere();
		String order = myclass.dbOrder(sortKey);
		int offset = (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
		int limit = daoPageInfo.getLineCount();

		String sql = "SELECT "
				+ "anno.anno_id, "
				+ "anno.user_info_id, "
				+ "anno.title , "
				+ "anno.upate, "
				+ "anno.admin, "
				+ "anno.text, "
				+ "user_info.last_name, "
				+ "user_info.middle_name, "
				+ "user_info.first_name "
				+ "FROM anno "
				+ "JOIN user_info ON anno.user_info_id = user_info.user_info_id "
				+ where + order
				+ " LIMIT " + limit + " OFFSET " + offset;
		List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
		for (HashMap<String, String> map : rs) {
			AnnouncementDao dao = new AnnouncementDao();
			dao.setAnnousementDao(map, dao);
			dao.setFirstName(map.get("user_first_name"));
			dao.setLastName(map.get("user_last_name"));
			resultList.add(dao);
		}

		return resultList;
	}

	/**
	 * files ファイル情報テーブルの検索条件を設定する。.
	 *
	 * @return String where句の文字列
	 * @throws AtareSysException フレームワーク共通例外
	 */
	private String dbWhere() throws AtareSysException {
		StringBuffer where = new StringBuffer(1024);
		if (userIds != null && userIds.length > 0) {
			where.append(where.length() > 0 ? " OR " : "");
			where.append("anno.user_info_id IN (");
			for (int i = 0; i < userIds.length; i++) {
				where.append(DbS.chara(userIds[i]));
				if (i < userIds.length - 1) {
					where.append(", ");
				}
			}

			where.append(") ");
		}

		if (getAnnoId().length() > 0) {
			where.append(where.length() > 0 ? " AND " : "");
			where.append("anno.anno_id = " + DbS.chara(getAnnoId()));
		}
		if (getUserInfoId().length() > 0) {
			where.append(where.length() > 0 ? " AND " : "");
			where.append("anno.user_info_id = " + DbS.chara(getUserInfoId()));
		}
		if (getSearchFileName().length() > 0) {
			where.append(where.length() > 0 ? " AND " : "");
			where.append("anno.file_name LIKE " + DbS.chara("%" + getSearchFileName() + "%"));
		}

		if (where.length() > 0) {
			return "where " + where.toString();
		}
		return "";
	}

	/**
	 * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
	 */
	private HashMap<String, String> fieldsArray = new HashMap<String, String>();

	/**
	 * コンストラクタ。
	 */
	public AnnouncementDao() {
		fieldsArray.put("anno_id", "anno.anno_id");
		fieldsArray.put("user_info_id", "anno.user_info_id");
		fieldsArray.put("title", "anno.title");
		fieldsArray.put("update_date", "anno.update_date");
		fieldsArray.put("admin", "anno.admin");
		fieldsArray.put("text", "anno.text");

	}

	/**
	 * files ファイル情報テーブルの並べ替え順序を設定する。.
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
	 * データベースからルーム名を取得するメソッド
	 * @return UserMenuに返す
	 * @throws AtareSysException
	 */
	public ArrayList<AnnouncementDao> getAllNews(String flg) throws AtareSysException {
		String sql = "SELECT "
				+ " anno.anno_id, "
				+ " anno.admin ,"
				+ " anno.title ,"
				+ " anno.update_date ,"
				+ " anno.text , "
				+ " user_info.last_name, "
				+ " user_info.first_name , "
				+ " anno.user_info_id "
				+ " FROM anno  "
				+ " join user_info on anno.user_info_id = user_info.user_info_id"
				+ " order by update_date " + selectorder(flg);

		List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
		ArrayList<AnnouncementDao> Anno = new ArrayList<>();
		for (HashMap<String, String> map : rs) {
			AnnouncementDao dao = new AnnouncementDao();
			// ルームDAOのインスタンスにデータを設定
			dao.setAnnoId(map.get("anno_id"));//UserInfoId->FileId
			dao.setUserInfoId(map.get("user_info_id"));
			dao.setAdmin(map.get("admin"));
			dao.setTitle(map.get("title"));
			dao.setUpdateDate(map.get("update_date"));
			dao.setText(map.get("text"));
			dao.setLastName(map.get("last_name"));
			dao.setFirstName(map.get("first_name"));
			Anno.add(dao);
		}
		return Anno; // 取得したルームリストを返す
	}

	private String selectorder(String a) {
		String str = null;
		if(a.equals("1")) {
			str = " asc ";
		}
		else {
			str =" desc ";
		}
		return str;
	}
}
