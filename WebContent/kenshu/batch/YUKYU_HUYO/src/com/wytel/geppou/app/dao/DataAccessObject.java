package com.wytel.geppou.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author s-hasumi
 *
 */
public abstract class DataAccessObject<T> {
	/**
	 * コネクション
	 */
	protected Connection conn = null;
	/**
	 * DB接続用URLWWW
	 */
	private String url = null;
	/**
	 * DBログイン用ID
	 */
	private static final String ID = "ginga";
	/**
	 * DBログイン用PW
	 */
	private static final String PASS = "gingastudy";

	/**
	 * コンストラクタ
	 * @param schema スキーマ名
	 */
	public DataAccessObject(String schema) {
		this.url = "jdbc:mysql://192.168.20.248:3306/" + schema + "?useUnicode=true&characterEncoding=utf8";
	}

	/**
	 * DB接続処理
	 * @throws Exception
	 */
	private void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// コネクション開始
		conn = DriverManager.getConnection(url, ID, PASS);
		// トランザクション開始 オートコミットをオフにする
		conn.setAutoCommit(false);
	}

	/**
	 * DB切断処理
	 * @throws Exception
	 */
	private void close() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		// コネクション開始
		conn.close();
	}

	/**
	 * 検索を行い、結果を返却する
	 * @param sql SQL文
	 * @return 検索結果
	 * @throws Exception
	 */
	protected ArrayList<T> select(String sql) throws Exception {
		ArrayList<T> list = new ArrayList<T>();
		connect();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		list = createList(rs);
		close();
		return list;
	}

	/**
	 * 更新（登録、削除）を行い、処理件数を返却する
	 * @param sql SQL文
	 * @return 処理を行ったレコード件数
	 * @throws Exception
	 */
	protected int update(List<String> sqlList) throws Exception {
		int ret = 0;
		connect();
		try {
			for (String sql : sqlList) {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ret = pstmt.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			close();
		}
		return ret;
	}

	/**
	 * 検索結果をオブジェクトのリストで返却する
	 * @param rs 検索結果
	 * @return 検索結果をオブジェクトのリストに入れたもの
	 */
	protected abstract ArrayList<T> createList(ResultSet rs) throws SQLException;
}