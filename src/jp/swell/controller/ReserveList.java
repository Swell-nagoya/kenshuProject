package jp.swell.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.dao.ReserveDao;
import jp.swell.dao.ReserveFileDao;
import jp.swell.dao.RoomDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.dao.UserReserveDao;

public class ReserveList extends ControllerBase {
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
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();

        if ("ReserveList".equals(bean.value("form_name"))) {
            bean.trimAllItem();
            if ("top".equals(bean.value("action_cmd"))) {
                redirect("MenuAdmin.do");
                return; // メソッドを終了
            } else if ("sort".equals(bean.value("action_cmd"))) {
                searchReserve();
            } else if ("sub".equals(bean.value("action_cmd"))) {
                searchReserve();
                forward("UserSelect.jsp");
                return; // メソッドを終了
            } else if ("next".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchReserve();
            } else if ("jump".equals(bean.value("action_cmd"))) {
                searchReserve();
            } else if ("prior".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchReserve();
            }
            // 検索クリアする条件を追加
            else if ("clear".equals(bean.value("action_cmd"))) {
                formClear();

                // 予約情報管理画面移行する条件を追加
            } else if ("list".equals(bean.value("action_cmd"))) {
                searchReserve();

                // 予約情報管理画面の削除する条件を追加
            } else if ("delete".equals(bean.value("action_cmd"))) {
                setDb2Web1();
                deleteReseveInfo();
                searchReserve();

                // 検索する条件を追加
            } else if ("search".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", "1");
                searchReserve();

            }
            {
                searchReserve();
            }

            forward("ReserveList.jsp");
        }

        else if ("UserInfoDetail_1".equals(bean.value("form_name"))
                || "UserInfoDetail_2".equals(bean.value("form_name"))
                || "UserInfoDetail_3".equals(bean.value("form_name"))) {
            setWebBeanFromSerialize(bean.value("search_info"));
            bean = getWebBean();
            searchReserve();
            forward("ReserveList.jsp");
        }

        else {
            formInit();
            searchReserve();
            forward("ReserveList.jsp");
        }
    }

    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void formInit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "reservation_date"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("lineCount",
                SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "ReserveList", "lineCount", "100"));
    }

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     */
    private HashMap<String, String> inputCheck() {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("list_search").length() > 0) {
            if (100 < bean.value("list_search").length()) {
                errors.put("list_search", "氏名の入力内容が長すぎます。");
            }
        }
        return errors;
    }

    /**
     * フィールドをクリアする。.
     */
    private void formClear() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("list_search", "");
        bean.setValue("lineCount", "");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }

    /**
     * 予約情報を検索
     *
     * @return 予約ID順を格納した配列を返す
     */
    private void searchReserve() throws AtareSysException {
        WebBean bean = getWebBean();
        HashMap<String, String> errors;
        bean.setValue("list_search", "");
        
        errors = inputCheck();
        if (errors.size() > 0) {
            bean.setValue("errors", errors);
            return;
        }
        // 予約情報の検索
        LinkedHashMap<String, String> sortKey = sortKey();
        ReserveDao dao = new ReserveDao();
        dao.setReservationDate(bean.value("list_search"));
        dao.setRoomName(bean.value("list_search"));
        dao.setUserName(bean.value("list_search"));
        dao.setCheckinTime(bean.value("list_search"));
        dao.setCheckoutTime(bean.value("list_search"));

        // ユーザー情報の取得とセット
        UserInfoDao userInfoDao = new UserInfoDao();
        ArrayList<UserInfoDao> users = userInfoDao.getAllUsers();

        DaoPageInfo daoPageInfo = new DaoPageInfo();
        if (!Validate.isInteger(bean.value("lineCount"))) {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "ReserveList", "lineCount", bean.value("lineCount"));
        if (!Validate.isInteger(bean.value("pageNo"))) {
            daoPageInfo.setPageNo(1);
        } else {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }
        dao.setUserIds(users);
        ArrayList<ReserveDao> listData = ReserveDao.dbSelectList(dao, sortKey, daoPageInfo);

        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", daoPageInfo.getRecordCount());
        bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

        // ルーム情報の取得とセット
        RoomDao roomDao = new RoomDao();
        ArrayList<RoomDao> rooms = roomDao.getAllRooms();

        // 予約情報の取得とセット
        ReserveDao reserveDao = new ReserveDao();
        ArrayList<ReserveDao> reserve = reserveDao.getAllReserves();

        bean.getWebValues().remove("search_info");
        String search_info = Sup.serialize(bean);

        bean.setValue("users", users);
        bean.setValue("rooms", rooms);
        bean.setValue("list", listData);
        bean.setValue("reserve", reserve);
        bean.setValue("search_info", search_info);
    }

    /**
     * ソート順番を求める
     *
     * @return ソート順を格納した配列を返す
     */
    private LinkedHashMap<String, String> sortKey() {
        WebBean bean = getWebBean();
        String key = "";
        LinkedHashMap<String, String> sort_key = new LinkedHashMap<String, String>(); /* この配列にソートキーとソートオーダーを入れる */
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

    /**
     * データベースの内容を表示エリアに編集する。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    private boolean setDb2Web1() throws AtareSysException {
        WebBean bean = getWebBean();
        String reserveId = bean.value("main_key");
        ReserveDao dao = new ReserveDao();
        dao.setReserveId(reserveId); // 予約IDをDAOにセットして情報を取得
        if (!dao.dbSelect(reserveId)) {
            return false;
        }

        bean.setValue("reserve_id", dao.getReserveId());
        bean.setValue("user_info_id", dao.getUserInfoId());
        bean.setValue("room_id", dao.getRoomId());
        bean.setValue("reservation_date", dao.getReservationDate());
        bean.setValue("checkin_time", dao.getCheckinTime());
        bean.setValue("checkout_time", dao.getCheckoutTime());
        bean.setValue("input_text", dao.getInputText());
        bean.setValue("rgb_color", dao.getColor());
        bean.setValue("input_remark", dao.getInputRemark());
        bean.setValue("input_remark", dao.getInputRemark());
        bean.setValue("update_user_id", dao.getUpdateUserId());
        bean.setValue("user_reserve_id", dao.getUserReserveId());

        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }

    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private ReserveDao setWebDaoInputInfo() throws AtareSysException {
        WebBean bean = getWebBean();
        ReserveDao reserveDao = new ReserveDao();
        reserveDao.setUserInfoId(bean.value("user_info_id"));
        reserveDao.setRoomId(bean.value("room_id"));
        reserveDao.setReservationDate(bean.value("reservation_date"));
        reserveDao.setCheckinTime(bean.value("checkin_time"));
        reserveDao.setCheckoutTime(bean.value("checkout_time"));
        reserveDao.setInputText(bean.value("input_text"));
        reserveDao.setColor(bean.value("rgb_color"));
        reserveDao.setInputRemark(bean.value("input_remark"));
        reserveDao.setInsertUserId(bean.value("user_info_id"));
        reserveDao.setUpdateUserId(bean.value("update_user_id"));
        UserInfoDao userInfoDao = new UserInfoDao();
        userInfoDao.dbSelect(bean.value("user_info_id"));
        reserveDao.setUserName(userInfoDao.getLastName() + userInfoDao.getFirstName());
        userInfoDao.dbSelect(bean.value("update_user_id"));
        reserveDao.setUpdateUserName(userInfoDao.getLastName() + userInfoDao.getFirstName());
        RoomDao roomDao = new RoomDao();
        roomDao.dbSelect(bean.value("room_id"));
        reserveDao.setRoomName(roomDao.getRoomName());

        bean.setValue("input_info", Sup.serialize(reserveDao));
        return reserveDao;
    }

    /**
     * 予約情報を削除するメソッド
     * @throws AtareSysException
     */
    private void deleteReseveInfo() throws AtareSysException {
        WebBean bean = getWebBean();
        // main_keyの値を取得
        String reserveId = bean.value("main_key");

        ReserveDao reserveDao = setWebDaoInputInfo();
        reserveDao.setReserveId(reserveId); // reserve_idの設定

        UserReserveDao userReserveDao = new UserReserveDao(); // UserReserveDaoのインスタンスを作成
        userReserveDao.setReserveId(bean.value("reserve_id"));

        ReserveFileDao reserveFileDao = new ReserveFileDao();
        reserveFileDao.setReserveId(bean.value("reserve_id"));

        // ファイル情報を取得
        if (reserveFileDao.dbSelect(reserveId)) { // データが存在するかをチェック
            // ファイルIDを取得して削除
            String fileId = reserveFileDao.getFileId();
            reserveDao.setFileId(fileId); // ファイルIDをReserveDaoに設定

            FileDao fileDao = new FileDao();
            fileDao.setFileId(fileId);

            // `dbFileDelete`メソッドを呼び出す
            reserveFileDao.dbFileDelete(fileId);

            //`dbFileDelete`メソッドを呼び出す
            fileDao.dbDelete(fileId);
        }

        // `dbDeleteUserReserve`メソッドを呼び出す
        userReserveDao.dbDeleteUserReserve(reserveDao.getReserveId());

        // `dbDeleteReserve`メソッドを呼び出す
        reserveDao.dbDeleteReserve(reserveId);
        // 予約した部屋情報の取得
        getRequest().setAttribute("reserveDao", reserveDao);
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
}
