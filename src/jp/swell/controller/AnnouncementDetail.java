package jp.swell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbS;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.AnnouncementDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

/**
 * ファイル情報を登録・更新・削除するためのコントローラクラス
 */
public class AnnouncementDetail extends ControllerBase {

	/**
	 * コントローラの初期設定を行う
	 */
	@Override
	public void doInit() {
		setLoginNeeds(false); // この処理にはログインが必要かどうか
		setHttpNeeds(false); // この処理はhttpでなければならないか
		setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
		setUsecache(false); // この処理はクライアントのキャッシュを認めるか
	}

	@Override
	public void doActionProcess() throws AtareSysException {
		WebBean bean = getWebBean();
		UserLoginInfo login = (UserLoginInfo) getLoginInfo();
		// 追加：JSP 上で使うためのログインユーザー名・ID
		bean.setValue("loginUserName", login.getLastName() + " " + login.getFirstName());
		bean.setValue("loginUserId", login.getUserInfoId());

		// デバッグログ：どのフォーム／コマンドで呼ばれたか
		String form = bean.value("form_name");
		String actionCmd = bean.value("action_cmd");
		String requestCmd = bean.value("request_cmd");

		// 共通取得
		String inputName = bean.value("input_name");
		String anno_id = bean.value("main_key");
		String flg = bean.value("flg");

		if ("AnnouncementDetail".equals(form)) {
			// ① upload ボタン押下 → 確認画面へ
			if ("upload".equals(actionCmd)) {
				bean = getWebBean();
				try {
					setWeb2Dao2InputInfo();
					bean.setValue("request_name", "投稿する");
					bean.setMessage("この内容で投稿します。よろしいですか？");
					bean.setValue("input_name", inputName);
					forward("AnnouncementDetail_2.jsp");

				} catch (AtareSysException | IOException | ServletException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				// ③ return ボタン押下 → 一覧画面へ戻す
			} else if ("return".equals(actionCmd)) {
				forward("Announcement.jsp");
			}

		} else if ("Announcement".equals(form)) {
			if ("go_next".equals(actionCmd)) {
				if ("ins".equals(requestCmd)) {
					bean.setValue("request_name", "投稿する");
					forward("AnnouncementDetail_1.jsp");

				} else if ("delete".equals(requestCmd)) {

					bean.setValue("request_name", "削除する");
					bean.setMessage("この投稿を削除します。よろしいですか？");
					bean.setValue("anno_id", anno_id);
					forward("AnnouncementDetail_2.jsp");

				}
			} else if ("go_show".equals(actionCmd)) {
				bean.setValue("anno_id", anno_id);
				forward("AnnouncementShow.jsp");
			}
			if ("go_sort".equals(actionCmd)) {
				if(flg.equals("1")) {
					flg="0";
				}else {
					flg="1";
				}
				bean.setValue("flg",flg);
				forward("Announcement.jsp");
			}

		} else if ("AnnouncementDetail_2".equals(form)) {
			if ("go_next".equals(actionCmd)) {
				if ("insEnter".equals(requestCmd)) {

					redirect("AnnouncementList.do");

				} else if ("delete".equals(requestCmd)) {
					dbDelete(anno_id);
					redirect("AnnouncementList.do");
				}
			} else if ("return".equals(actionCmd)) {
				if ("ins".equals(requestCmd)) {

					forward("AnnouncementDetail_1.jsp");
				}
			}

		} else if ("AnnouncementDetail_3".equals(form)) {
			if ("return".equals(actionCmd)) {
		
				redirect("AnnouncementList.do");
			}
		}
	}

	/**
	 * 検索を行いbeanに格納する。
	 */
	/*private void searchList() throws AtareSysException {
		WebBean bean = getWebBean();
		UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();

		LinkedHashMap<String, String> sortKey = sortKey();
		AnnouncementDao AnnouncementDao = new AnnouncementDao();
		AnnouncementDao.setUserInfoId(userLoginInfo.getUserInfoId());
		AnnouncementDao.setTitle(bean.value("title"));

		DaoPageInfo daoPageInfo = new DaoPageInfo();
		if (!Validate.isInteger(bean.value("lineCount"))) {
			bean.setValue("lineCount", "20");
		}
		daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
		SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "FileList", "lineCount", bean.value("lineCount"));
		if (!Validate.isInteger(bean.value("pageNo"))) {
			daoPageInfo.setPageNo(1);
		} else {
			daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
		}

		ArrayList<AnnouncementDao> AnnouncementList = jp.swell.dao.AnnouncementDao.dbSelectList(AnnouncementDao,
				sortKey, daoPageInfo);
		bean.setValue("lineCount", daoPageInfo.getLineCount());
		bean.setValue("pageNo", daoPageInfo.getPageNo());
		bean.setValue("recordCount", AnnouncementList.size()); // ファイルリストのサイズ
		bean.setValue("maxPageNo", (AnnouncementList.size() / Integer.parseInt(bean.value("lineCount")) + 1));

		// ルーム情報の取得とセット
		ArrayList<AnnouncementDao> Announcement = AnnouncementDao.getAllNews();

		List<UserInfoDao> allUsers = new UserInfoDao().getAllUsers();

		// ページ番号取得（デフォルト 1）
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(bean.value("pageNo"));
		} catch (Exception ignored) {
		}

		// １ページあたり件数
		final int pageSize = 10;
		int total = allUsers.size();
		int maxPage = (total + pageSize - 1) / pageSize;

		// 切り出し位置を計算
		int from = Math.min((pageNo - 1) * pageSize, total);
		int to = Math.min(from + pageSize, total);
		ArrayList<UserInfoDao> pageUsers = new ArrayList<>(allUsers.subList(from, to));

		// WebBean にセット
		bean.setValue("user_data", pageUsers);
		bean.setValue("pageNo", String.valueOf(pageNo));
		bean.setValue("maxPageNo", String.valueOf(maxPage));

		// 既存の他の値はそのまま残す
		bean.getWebValues().remove("search_info");
		String search_info = Sup.serialize(bean);
		bean.setValue("search_info", search_info);
		bean.setValue("title", Announcement);
		bean.setValue("list", AnnouncementList);
	}
*/
	/**
	 * ソート順番を求める
	 *
	 * @return ソート順を格納した配列を返す
	 */
	/*private LinkedHashMap<String, String> sortKey() {
		WebBean bean = getWebBean();
		String key = "";
		LinkedHashMap<String, String> sort_key = new LinkedHashMap<String, String>(); /
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
				if ("asc".equals(bean.value("sort_order"))) {
					sort_key.put(key, "asc");
				} else {
					sort_key.put(key, "desc");
				}
			}
		} else {
			// 初期値
			key = bean.value("sort_key");
			if ("asc".equals(bean.value("sort_order"))) {
				sort_key.put(key, "asc");
			} else {
				sort_key.put(key, "desc");
			}
		}
		bean.setValue("sort_key", "");
		bean.setValue("sort_key_old", key);
		bean.setValue("sort_order", sort_key.get(key));
		return sort_key;
	}
*/
	/**
	 * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する。.
	 *
	 * @return なし
	 * @throws ServletException 
	 * @throws IOException 
	 * @throws AtareSysException フレームワーク共通例外
	 */
	private UserInfoDao setWeb2Dao2InputInfo()
			throws AtareSysException, IOException, ServletException {
		WebBean bean = getWebBean();
		UserInfoDao dao = new UserInfoDao();
		dao.setUserInfoId(bean.value("user_info_id"));

		AnnouncementUpload(dao.getUserInfoId());

		bean.setValue("input_info", Sup.serialize(dao));
		bean.setValue("dao", dao);
		return dao;
	}

	/**
	 * ファイルデータを取得し、アップロードした後にデータベースへ登録
	 * @param pUserInfoId
	 * @return
	 * @throws AtareSysException
	 * @throws IOException
	 * @throws ServletException
	 */
	private ArrayList<AnnouncementDao> AnnouncementUpload(String pUserInfoId)
			throws AtareSysException, IOException, ServletException {
		WebBean bean = getWebBean();
		ArrayList<AnnouncementDao> AnnouncementDaos = new ArrayList<>();

		// 送信元ユーザーIDを取得
		String user_info_id = bean.value("login_user"); // 送信元ユーザーIDを取得

		// ファイルデータを取得
		String title = bean.value("title");
		String text = bean.value("text");
		String admin = bean.value("admin");
		String anno_id = UUID.randomUUID().toString().substring(0, 13);
		AnnouncementDao AnnouncementDao = new AnnouncementDao();

		AnnouncementDao.dbAnnousementInsert(anno_id, user_info_id, admin, text, title);
		AnnouncementDaos.add(AnnouncementDao);
		return AnnouncementDaos;
	}

	public boolean dbDelete(String pAnnoId) throws AtareSysException {
		// annoテーブルからレコードを削除

		String sqlDeleteAnno = "DELETE FROM anno WHERE anno_id = " + DbS.chara(pAnnoId);
		int retAnno = DbBase.dbExec(sqlDeleteAnno);

		if (retAnno != 1)
			throw new AtareSysException("dbDelete number or record exception");
		return true;
	}
}