// ContactListDetail.java
package jp.swell.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ContactDao;

/**
 * 連絡先(Contact)の「一覧 → 入力 → 確認 → 確定」フローを司るコントローラ。
 * 
 * ■方針（UserInfoDetail と同じ運び方）
 *   - 一覧(=ContactList.jsp)から「編集/削除/確認」で飛んでくるときは main_key を受け取り、
 *     DBから読み込み → 画面項目へ反映 → select_info / input_info に同一内容をシリアライズして持ち回す
 *   - 入力画面(=ContactListDetail_1.jsp)で編集し、次へ進むときは
 *     画面値 → DAO → input_info に保存してから確認画面へ
 *   - 確認画面(=ContactListDetail_3.jsp)の確定押下で
 *     input_info を画面に復元（安全のため）→ DB処理(insert/update/delete) → 一覧へリダイレクト
 *   - 競合チェックは select_info（最初に読んだ姿）と最新DBの比較で行う
 * 
 * ■隠し項目の持ち方（重要）
 *   - main_key: 一覧から来た主キー（id と同義だが一覧遷移時の受け渡しに使う）
 *   - id: 画面/確認で持ち回すID（空なら main_key をフォールバック）
 *   - input_info: 編集途中のDAOスナップショット（確定直前の復元元）
 *   - select_info: 編集開始時点のDAOスナップショット（楽観ロック用途）
 */
public class ContactListDetail extends ControllerBase {

    @Override
    public void doInit() {
        // 認証/通信要件は既存の規約に合わせる
        setLoginNeeds(false);
        setHttpNeeds(false);
        setHttpsNeeds(false);
        setUsecache(false);
    }

    /**
     * 画面遷移のハブ処理。
     * form_name / action_cmd / request_cmd の組み合わせで分岐する。
     */
    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();
        try {
            // === 一覧からの遷移（ContactList.jsp） ===
            if ("ContactList".equals(bean.value("form_name"))) {
                if ("go_next".equals(bean.value("action_cmd"))) {
                    // 新規登録
                    if ("ins".equals(bean.value("request_cmd"))) {
                        bean.setValue("input_info", Sup.serialize(new ContactDao()));
                        bean.setValue("request_name", "登録");
                        forward("ContactListDetail_1.jsp");

                        // 修正
                    } else if ("update".equals(bean.value("request_cmd"))) {
                        if (!setDb2Web()) {
                            bean.setError("連絡先データの取得に失敗しました");
                            forward("ContactList.jsp");
                        } else {
                            bean.setValue("request_name", "修正");
                            forward("ContactListDetail_1.jsp");
                        }

                        // 削除（UserInfo流：確認画面(3)で確定させる）
                    } else if ("delete".equals(bean.value("request_cmd"))) {
                        if (!setDb2Web()) {
                            bean.setError("連絡先データの取得に失敗しました");
                            forward("ContactList.jsp");
                        } else {
                            bean.setValue("request_name", "削除");
                            forward("ContactListDetail_3.jsp");
                        }

                        // 確認
                    } else if ("check".equals(bean.value("request_cmd"))) {
                        if (!setDb2Web()) {
                            bean.setError("連絡先データの取得に失敗しました");
                            forward("ContactList.jsp");
                        } else {
                            bean.setValue("request_name", "確認");
                            forward("ContactListDetail_3.jsp");
                        }

                    } else {
                        // 不明な request_cmd は一覧へ
                        forward("ContactList.jsp");
                    }

                } else if ("menu".equals(bean.value("action_cmd"))) {
                    redirect("MenuAdmin.do");
                    return;

                } else {
                    // 既定は一覧へ
                    forward("ContactList.jsp");
                }
                return;
            }

            // === 入力画面（ContactListDetail_1.jsp） ===
            if ("ContactListDetail_1".equals(bean.value("form_name"))) {
                if ("go_next".equals(bean.value("action_cmd"))) {
                    // 新規 or 修正 → 入力チェック → 確認へ
                    if ("ins".equals(bean.value("request_cmd")) || "update".equals(bean.value("request_cmd"))) {
                        bean.rtrimAllItem(); // 末尾空白除去（既存規約）
                        ContactDao dao = setWeb2Dao2InputInfo(); // 画面→DAO→input_info
                        HashMap<String, String> errors = inputCheck(dao);
                        if (errors.isEmpty()) {
                            String action = "ins".equals(bean.value("request_cmd")) ? "登録" : "修正";
                            bean.setMessage("この内容で" + action + "します。よろしいですか？");
                            bean.setValue("request_name", action);
                            forward("ContactListDetail_3.jsp"); // 確認へ
                        } else {
                            bean.setMessage("入力内容に誤りがあります"); // 画面に残して再表示
                            forward("ContactListDetail_1.jsp");
                        }
                        return;
                    }
                } else if ("return".equals(bean.value("action_cmd"))) {
                    // 一覧へ戻る
                    forward("ContactList.do");
                    return;
                }
            }

            // === 確認/確定画面（ContactListDetail_3.jsp） ===
            if ("ContactListDetail_3".equals(bean.value("form_name"))) {
                if ("go_next".equals(bean.value("action_cmd"))) {
                    final String req = bean.value("request_cmd");

                    // 新規確定
                    if ("ins".equals(req)) {
                        setInputInfo2Dao2Web(); // 念のため input_info を画面に復元
                        signUp();
                        redirect("ContactList.do");
                        return;

                        // 修正確定（楽観ロック）
                    } else if ("update".equals(req)) {
                        if (checkDataMatching()) {
                            setInputInfo2Dao2Web(); // 復元してから更新
                            dbEdit();
                            return;
                        } else {
                            bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                            setDb2Web(); // 最新DBを再読込
                            forward("ContactListDetail_1.jsp");
                            return;
                        }

                        // 削除確定（楽観ロック）
                    } else if ("delete".equals(req)) {
                        if (checkDataMatching()) {
                            setInputInfo2Dao2Web(); // 復元してから削除
                            delete();
                            return;
                        } else {
                            bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                            setDb2Web(); // 最新DBを再読込
                            forward("ContactListDetail_1.jsp");
                            return;
                        }
                    }

                } else if ("return".equals(bean.value("action_cmd"))) {
                    // 確認 → 入力へ戻る運び（UserInfo と同じ）
                    final String req = bean.value("request_cmd");
                    if ("ins".equals(req)) {
                        bean.setValue("request_name", "登録");
                        setInputInfo2Dao2Web();
                        forward("ContactListDetail_1.jsp");
                    } else if ("update".equals(req)) {
                        bean.setValue("request_name", "修正");
                        setInputInfo2Dao2Web();
                        setWeb2Dao2InputInfo(); // input_info を更新
                        forward("ContactListDetail_1.jsp");
                    } else if ("delete".equals(req)) {
                        bean.setValue("request_name", "修正");
                        setInputInfo2Dao2Web();
                        setWeb2Dao2InputInfo();
                        forward("ContactListDetail_1.jsp");
                    } else if ("send".equals(req)) {
                        redirect("ContactList.do");
                    }
                    return;
                }
            }

            // === 直叩きなどのデフォルト動作：修正画面へ ===
            bean.setValue("request_name", "修正");
            bean.setValue("request_cmd", "update");
            if (!setDb2Web()) {
                bean.setError("データの取得に失敗しました");
            }
            bean.setMessage("以下の項目を修正してください。");
            forward("ContactListDetail_1.jsp");

        } catch (Exception e) {
            // 想定外例外は一覧へ
            WebBean b = getWebBean();
            b.setError("処理中にエラーが発生しました: " + e.getMessage());
            forward("ContactList.jsp");
        }
    }

    /**
     * DBの1件を読み込み、画面項目へ展開すると同時に
     * select_info / input_info に同一のスナップショットを積む。
     * @return 取得できた場合 true
     */
    private boolean setDb2Web() throws AtareSysException {
        WebBean bean = getWebBean();
        String key = bean.value("main_key");
        if (key == null || key.isEmpty())
            return false;

        ContactDao dao = new ContactDao();
        if (!dao.dbSelectByContactId(Integer.parseInt(key)))
            return false;

        // 画面項目へ展開
        bean.setValue("id", dao.getId());
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("phone_number", dao.getPhoneNumber());
        bean.setValue("email", dao.getEmail());
        bean.setValue("insert_id", dao.getInsertId());
        bean.setValue("insert_date", dao.getInsertDate());

        // スナップショット（楽観ロック・確認画面復元に使用）
        bean.setValue("select_info", Sup.serialize(dao));
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }

    /**
     * 画面の値を DAO に詰め直し、input_info にシリアライズして持ち回す。
     * id が空の場合は main_key をフォールバック。
     */
    private ContactDao setWeb2Dao2InputInfo() throws AtareSysException {
        WebBean bean = getWebBean();
        ContactDao dao;

        // 既に input_info があればその内容をベースにする（UserInfo流）
        String inputInfo = bean.value("input_info");
        if (inputInfo != null && !inputInfo.isEmpty()) {
            dao = (ContactDao) Sup.deserialize(inputInfo);
        } else {
            dao = new ContactDao();
        }

        // id は main_key で補完
        String idStr = bean.value("id");
        if (idStr == null || idStr.isEmpty())
            idStr = bean.value("main_key");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                dao.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException ignore) {
            }
        }

        // 氏名/連絡先
        dao.setLastName(bean.value("last_name"));
        dao.setMiddleName(bean.value("middle_name"));
        dao.setFirstName(bean.value("first_name"));
        dao.setLastNameKana(bean.value("last_name_kana"));
        dao.setMiddleNameKana(bean.value("middle_name_kana"));
        dao.setFirstNameKana(bean.value("first_name_kana"));
        dao.setPhoneNumber(bean.value("phone_number"));
        dao.setEmail(bean.value("email"));

        // 登録者情報
        if (bean.value("insert_id") != null && !bean.value("insert_id").isEmpty()) {
            try {
                dao.setInsertId(Integer.parseInt(bean.value("insert_id")));
            } catch (NumberFormatException ignore) {
            }
        }
        if (bean.value("insert_date") != null && !bean.value("insert_date").isEmpty()) {
            dao.setInsertDate(bean.value("insert_date"));
        }

        // 更新日時（表示用途。DB更新は DAO 側でやる前提もありうる）
        dao.setUpdateDate(new Date().toString());

        // シリアライズして持ち回す
        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }

    /**
     * input_info（編集中スナップショット）から画面へ復元する。
     * id が未POSTなら input_info の id を優先して補完する。
     */
    private void setInputInfo2Dao2Web() throws AtareSysException {
        WebBean bean = getWebBean();
        ContactDao dao = (ContactDao) Sup.deserialize(bean.value("input_info"));
        if (dao == null)
            throw new AtareSysException("input_info is invalid");

        String postedId = bean.value("id");
        if (postedId == null || postedId.isEmpty()) {
            bean.setValue("id", dao.getId());
        }
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("phone_number", dao.getPhoneNumber());
        bean.setValue("email", dao.getEmail());
        bean.setValue("insert_id", dao.getInsertId());
        bean.setValue("insert_date", dao.getInsertDate());
    }

    /**
     * 楽観ロック用：編集開始時点（select_info）と最新DBの内容を比較し一致を確認する。
     * @return 一致していれば true
     */
    private boolean checkDataMatching() throws AtareSysException {
        WebBean bean = getWebBean();
        ContactDao dao = new ContactDao();
        String key = bean.value("main_key");
        if (key == null || key.isEmpty())
            return false;
        if (!dao.dbSelectByContactId(Integer.parseInt(key)))
            return false;
        return Sup.serializeIsEquals(bean.value("select_info"), dao);
    }

    /**
     * 入力チェック（UserInfo と同等の粒度）
     * @return エラーMap（空ならOK）
     */
    private HashMap<String, String> inputCheck(ContactDao pContactDao) throws AtareSysException {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        errors.clear();

        // 新規/修正時のチェック
        if ("ins".equals(bean.value("request_cmd")) || "update".equals(bean.value("request_cmd"))) {
            // 氏名
            if (bean.value("last_name").length() == 0 && bean.value("first_name").length() == 0) {
                errors.put("last_name", "氏名を入力してください。");
                errors.put("first_name", "");
            } else if (bean.value("last_name").length() == 0) {
                errors.put("last_name", "名字を入力してください。");
            } else if (bean.value("first_name").length() == 0) {
                errors.put("first_name", "名前を入力してください。");
            }

            // 氏名よみ
            if (bean.value("last_name_kana").length() == 0 && bean.value("first_name_kana").length() == 0) {
                errors.put("last_name_kana", "氏名のよみを入力してください。");
                errors.put("first_name_kana", "");
            } else if (bean.value("last_name_kana").length() == 0) {
                errors.put("last_name_kana", "名字のよみを入力してください。");
            } else if (bean.value("first_name_kana").length() == 0) {
                errors.put("first_name_kana", "名前のよみを入力してください。");
            }
            if (bean.value("last_name_kana").length() > 0 || bean.value("first_name_kana").length() > 0) {
                if (!isHiragana(bean.value("last_name_kana")) && !isHiragana(bean.value("first_name_kana"))) {
                    errors.put("last_name_kana", "氏名のよみはひらがなで入力してください。");
                } else if (!isHiragana(bean.value("last_name_kana"))) {
                    errors.put("last_name_kana", "名字のよみはひらがなで入力してください。");
                } else if (!isHiragana(bean.value("first_name_kana"))) {
                    errors.put("first_name_kana", "名前のよみはひらがなで入力してください。");
                }
            }

            // ミドルネームよみ（任意だが入力があればよみ必須）
            if (bean.value("middle_name").length() != 0) {
                if (bean.value("middle_name_kana").length() == 0) {
                    errors.put("middle_name_kana", "ミドルネームよみを入力してください。");
                } else if (!isHiragana(bean.value("middle_name_kana"))) {
                    errors.put("middle_name_kana", "ミドルネームよみはひらがなで入力してください。");
                }
            }

            // 電話番号
            if (bean.value("phone_number").length() == 0) {
                errors.put("phone_number", "電話番号を入力してください。");
            } else if (!bean.value("phone_number").matches("^0\\d{1,4}-\\d{1,4}-\\d{3,4}$")) {
                errors.put("phone_number", "正しい電話番号を入力してください。");
            }

            // メールアドレス
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(bean.value("email"));
            if (bean.value("email").length() == 0) {
                errors.put("email", "メールアドレスを入力してください。");
            } else if (!matcher.matches()) {
                errors.put("email", "正しいメールアドレスを入力してください。");
            } else if ("ins".equals(bean.value("request_cmd"))) {
                // 新規時のみメール重複チェック（更新時は別メソッドがある仕様なら条件分岐で追加）
                if (pContactDao.isEmailExists(bean.value("email"))) {
                    errors.put("email", "このメールアドレスは既に登録されています。");
                }
            }
        }

        return errors;
    }

    /** ひらがな判定（ーを許容） */
    private boolean isHiragana(String input) {
        return input != null && input.matches("^[\\u3040-\\u309Fー]+$");
    }

    /** 新規登録（エラーは WebBean に詰め返して false） */
    private boolean signUp() throws AtareSysException {
        WebBean bean = getWebBean();
        ContactDao dao = setWeb2Dao2InputInfo();
        HashMap<String, String> errors = inputCheck(dao);
        for (Map.Entry<String, String> e : errors.entrySet())
            bean.setError(e.getKey(), e.getValue());
        if (!errors.isEmpty())
            return false;

        if (dao.getInsertId() == 0) {
            dao.setInsertId(9999); // 管理者IDなど適当な固定値
        }
        if (dao.getInsertDate() == null || dao.getInsertDate().isEmpty()) {
            dao.setInsertDate(new Date().toString()); // 現在時刻を補完
        }

        try {
            DbBase.dbBeginTran();
            boolean ok = dao.dbInsert();
            if (ok) {
                DbBase.dbCommitTran();
                return true;
            } else {
                DbBase.dbRollbackTran();
                return false;
            }
        } catch (Exception ex) {
            DbBase.dbRollbackTran();
            throw new AtareSysException("連絡先の登録中にエラーが発生しました。");
        }
    }

    /** 更新（成功で一覧へ redirect、失敗は1.jsp へ forward） */
    public void dbEdit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        ContactDao dao = setWeb2Dao2InputInfo();
        HashMap<String, String> errors = inputCheck(dao);
        for (Map.Entry<String, String> e : errors.entrySet())
            bean.setError(e.getKey(), e.getValue());
        if (!errors.isEmpty()) {
            forward("ContactListDetail_1.jsp");
            return;
        }

        String id = bean.value("id");
        if (id == null || id.isEmpty())
            throw new AtareSysException("更新対象の連絡先IDが指定されていません。");

        try {
            DbBase.dbBeginTran();
            boolean ok = dao.dbUpdate(Integer.parseInt(id));
            if (ok) {
                DbBase.dbCommitTran();
                redirect("ContactList.do");
            } else {
                DbBase.dbRollbackTran();
                bean.setError("update_failed", "連絡先の更新に失敗しました。");
                forward("ContactListDetail_1.jsp");
            }
        } catch (Exception ex) {
            DbBase.dbRollbackTran();
            throw new AtareSysException("連絡先の更新中にエラーが発生しました。");
        }
    }

    /** 削除（成功で一覧へ redirect、失敗は2.jsp を想定して forward） */
    public void delete() throws AtareSysException {
        WebBean bean = getWebBean();
        String id = bean.value("id");
        if (id == null || id.isEmpty())
            throw new AtareSysException("削除対象の連絡先IDが指定されていません。");

        try {
            DbBase.dbBeginTran();
            ContactDao dao = new ContactDao();
            boolean ok = dao.dbDelete(Integer.parseInt(id));
            if (ok) {
                DbBase.dbCommitTran();
                redirect("ContactList.do");
            } else {
                DbBase.dbRollbackTran();
                bean.setError("delete_failed", "連絡先の削除に失敗しました。");
                forward("ContactListDetail_3.jsp");
            }
        } catch (Exception ex) {
            DbBase.dbRollbackTran();
            throw new AtareSysException("連絡先の削除中にエラーが発生しました: " + ex.getMessage());
        }
    }
}
