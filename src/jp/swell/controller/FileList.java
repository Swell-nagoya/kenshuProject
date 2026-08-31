/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * ファイル名　　：RoomDetail.java
 * クラス名　　　：RoomDetail
 * 概要　　　　　：room 部屋情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.user.UserLoginInfo;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class FileList extends ControllerBase {
	/**
	 * jp.patasys.alumni.controller.HttpServlet のメソッドをオーバライドする。
	 * オーバライドしない場合は、デフォルトが設定される。.
	 * この処理にはログインが必要かどうか デフォルト true.
	 * この処理はhttpでなければならないか デフォルト false.
	 * この処理はhttps でなければならないか デフォルト false.
	 * この処理はクライアントのキャッシュを認めるか デフォルト false. 等を設定する。
	 * doActionの前に呼ばれる。
	 */
	@Override
	public void doInit() {
		setLoginNeeds(true); // この処理にはログインが必要かどうか
		setHttpNeeds(false); // この処理はhttpでなければならないか
		setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
		setUsecache(false); // この処理はクライアントのキャッシュを認めるか
	}

	/**
	 * jp.swell.cloudbiz.common.ControllerBase のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
	 * ここで、コントローラの処理を記述する.
	 * @throws Exception エラー
	 */
	@Override
	public void doActionProcess() throws AtareSysException {
		WebBean bean = getWebBean();

		//FileDetail.javaから送られた値を取得
		String downloadError = bean.value("download_error");

		if ("FileList".equals(bean.value("form_name"))) {
			bean.trimAllItem();
			if ("search".equals(bean.value("action_cmd"))) {
				bean.setValue("pageNo", "1");
				searchList();
			} else if ("next".equals(bean.value("action_cmd"))) {
				bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
				searchList();
			} else if ("jump".equals(bean.value("action_cmd"))) {
				searchList();
			} else if ("prior".equals(bean.value("action_cmd"))) {
				bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
				searchList();
			} else if ("sort".equals(bean.value("action_cmd"))) {
				searchList();
			} else if ("clear".equals(bean.value("action_cmd"))) {
				formClear();
				searchList();
			} else if ("return".equals(bean.value("action_cmd"))) {
				redirect("MenuAdmin.do");
				return;
			} else {
				searchList();
			}
			forward("FileList.jsp");
		} else if ("FileDetail".equals(bean.value("form_name")) || "FileDetail_2".equals(bean.value("form_name"))) {
			setWebBeanFromSerialize(bean.value("search_info"));
			bean = getWebBean();
			searchList();
			forward("FileList.jsp");
		} else {
			formInit();
			
			//権限エラーまたは期限切れ時有効なもののみに絞り込む
			if ("permission".equals(downloadError) || "expired" .equals(downloadError)) {
				bean.setValue("downloadable_only","1");
			}
			
			searchList();

			if ("permission".equals(downloadError)) {
				bean.setError(
						"このファイルをダウンロードする権限がありません。" + "ダウンロード可能なファイルのみ表示します。");
			}

			if ("expired".equals(downloadError)) {
				bean.setError("このファイルはダウンロード期限を過ぎています。" + "ダウンロード可能なファイルを表示しています。");
			}
			forward("FileList.jsp");
		}
	}

	/**
	 * 最初の画面を表示する。.
	 *
	 * @throws AtareSysException
	 */
	private void formInit() throws AtareSysException {
		WebBean bean = getWebBean();
		bean.setValue("sort_key", "file_name"); /* 初回のソートキーを入れる */
		bean.setValue("sort_order", "asc");
		bean.setValue("lineCount",
				SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "FileList", "lineCount", "100"));
	}

	/**
	 * フィールドをクリアする。.
	 */
	private void formClear() throws AtareSysException {
		WebBean bean = getWebBean();
		bean.setValue("list_search_file_name", "");
		bean.setValue("lineCount", "");
		String search_info = Sup.serialize(bean);
		bean.setValue("search_info", search_info);
	}

	/**
	 * 入力チェックを行う。.
	 *
	 * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
	 */
	private HashMap<String, String> inputCheck() {
		WebBean bean = getWebBean();
		HashMap<String, String> errors = bean.getItemErrors();
		if (bean.value("list_search_file_name").length() > 0) {
			if (100 < bean.value("list_search_file_name").length()) {
				errors.put("list_search_file_name", "氏名の入力内容が長すぎます。");
			}
		}
		return errors;
	}

	/**
	 * 検索を行いbeanに格納する。.
	 */
	private void searchList() throws AtareSysException {
		WebBean bean = getWebBean();
		UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
		HashMap<String, String> errors = inputCheck();
		if (errors.size() > 0) {
			bean.setValue("errors", errors);
			return;
		}

		LinkedHashMap<String, String> sortKey = sortKey();

		//表示件数
		int lineCount = 20;

		if (Validate.isInteger(bean.value("lineCount"))) {
			lineCount = Integer.parseInt(bean.value("lineCount"));
		}

		if (lineCount <= 0) {
			lineCount = 20;
		}

		//要求されたページ番号
		int requestedPage = 1;

		if (Validate.isInteger(bean.value("pageNo"))) {
			requestedPage = Integer.parseInt(bean.value("pageNo"));
		}

		if (requestedPage < 1) {
			requestedPage = 1;
		}

		DaoPageInfo allPageInfo = new DaoPageInfo();
		allPageInfo.setPageNo(1);
		allPageInfo.setLineCount(Integer.MAX_VALUE);

		//自分がアップロードしたファイル
		FileDao sentDao = new FileDao();

		sentDao.setUploadUserId(userLoginInfo.getUserInfoId());
		sentDao.setSearchFileName(bean.value("list_search_file_name"));

		List<FileDao> sentFiles = FileDao.dbSelectList(sentDao, sortKey, allPageInfo);

		for (FileDao file : sentFiles) {
			file.setFileType("sent");
		}

		//自分宛てファイル
		FileDao receivedDao = new FileDao();
		receivedDao.setUserInfoId(userLoginInfo.getUserInfoId());
		receivedDao.setSearchFileName(bean.value("list_search_file_name"));

		List<FileDao> receivedFiles = FileDao.dbSelectList(receivedDao, sortKey, allPageInfo);

		for (FileDao file : receivedFiles) {
			file.setFileType("received");
		}

		//全件をまとめる
		ArrayList<FileDao> allFiles = new ArrayList<>();
		allFiles.addAll(receivedFiles);
		allFiles.addAll(sentFiles);

		if ("1".equals(bean.value("downloadable_only"))) {
			allFiles.removeIf(file -> isExpired(file.getExpirationDate()));
		}

		//本当の総件数
		int recordCount = allFiles.size();

		//最大ページ数
		int maxPageNo = Math.max(1, (recordCount + lineCount - 1) / lineCount);

		//最大ページを超えないようにする
		int pageNo = Math.min(requestedPage, maxPageNo);

		//このページで表示する範囲
		int fromIndex = (pageNo - 1) * lineCount;

		int toIndex = Math.min(fromIndex + lineCount, recordCount);

		ArrayList<FileDao> pageFiles = new ArrayList<>(allFiles.subList(fromIndex, toIndex));

		//JSPへ渡す
		bean.setValue("list", pageFiles);
		bean.setValue("lineCount", lineCount);
		bean.setValue("pageNo", pageNo);
		bean.setValue("recordCount", recordCount);
		bean.setValue("maxPageNo", maxPageNo);

		SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "FileList", "lineCount", String.valueOf(lineCount));
	}

	/**
	 * ソート順番を求める
	 *
	 * @return ソート順を格納した配列を返す
	 */
	private LinkedHashMap<String, String> sortKey() {
		WebBean bean = getWebBean();
		String key = "upload_date"; // アップロード日時をキーに設定
		LinkedHashMap<String, String> sort_key = new LinkedHashMap<>();

		// 既存のソートロジックを調整
		if (bean.value("sort_key").length() == 0 && bean.value("sort_key_old").length() == 0)
			return null;

		if (bean.value("sort_key_old").length() > 0) {
			if (bean.value("sort_key").length() > 0) {
				if (bean.value("sort_key").equals(bean.value("sort_key_old"))) {
					// 同一ソートキー（フリップフロップ）
					key = bean.value("sort_key_old");
					if ("desc".equals(bean.value("sort_order"))) {
						sort_key.put(key, "asc");
					} else {
						sort_key.put(key, "desc");
					}
				} else {
					// 新たなソートキー
					key = bean.value("sort_key");
					sort_key.put(key, "asc");
				}
			} else {
				// 引き継ぎ
				key = bean.value("sort_key_old");
				sort_key.put(key, "asc".equals(bean.value("sort_order")) ? "asc" : "desc");
			}
		} else {
			// 初期値
			sort_key.put(key, "asc"); // 初期ソート順を指定
		}

		bean.setValue("sort_key", "");
		bean.setValue("sort_key_old", key);
		bean.setValue("sort_order", sort_key.get(key));
		return sort_key;
	}

	/**
	 * ページ番号を加算減算する
	 *
	 * @param $page_no
	 *        現在のページ番号
	 * @param $add
	 *        加算減算する値
	 * @return 結果のページを返す
	 */
	private String calcPageNo(String pageNo, int add) {
		int ret;
		if (null == pageNo) {
			pageNo = "1";
		} else if ("".equals(pageNo)) {
			pageNo = "1";
		} else if (!Validate.isInteger(pageNo)) {
			pageNo = "1";
		}
		ret = Integer.parseInt(pageNo);
		ret += add;
		return String.valueOf(ret);
	}

	private boolean isExpired(String expirationDateString) {
		if (expirationDateString == null || expirationDateString.isEmpty()) {
			return false;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date expirationDate = sdf.parse(expirationDateString);

			return expirationDate.before(new Date());
		} catch (ParseException e) {
			return false;
		}
	}
}