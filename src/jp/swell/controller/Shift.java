package jp.swell.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ShiftDAO;

/**
 * ：user_info ユーザ情報テーブルデータをLIST表示するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class Shift extends ControllerBase {
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

    /**
     * jp.patasys.cloudbiz.common.ControllerBase のメソッドをオーバライドする。
     * ここで、コントローラの処理を記述する.
     *
     * @throws Exception エラー
     */
    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();
        if ("Shift".equals(bean.value("form_name"))) 
        {
            bean.trimAllItem();
            if ("go_next".equals(bean.value("action_cmd"))) 
            {
                if ("ins".equals(bean.value("request_cmd"))) 
                {
                    bean.setValue("input_info", Sup.serialize(new ShiftDAO()));
                    bean.setValue("request_name", "登録");
                    forward("ShiftDetail.jsp");
                    return;
                } 
                else if ("update".equals(bean.value("request_cmd"))) 
                {
                    if (!setDb2Web()) {
                        bean.setError("データの取得に失敗しました");
                        forward("Shift.jsp");
                    } else {
                        bean.setValue("request_name", "修正");
                        forward("ShiftDetail.jsp");
                        return;
                    }
                } 
                else if ("delete".equals(bean.value("request_cmd"))) 
                {
                    if (!setDb2Web()) {
                        bean.setError("データの取得に失敗しました");
                        forward("Shift.jsp");
                    } 
                    else 
                    {
                        bean.setValue("request_name", "削除");
                        forward("ShiftDetail_2.jsp");
                        return;
                    }
                }
            }
            else if ("search".equals(bean.value("action_cmd"))) 
            {
                bean.setValue("pageNo", "1");
                searchList();
            }
            else if ("next".equals(bean.value("action_cmd"))) 
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchList();
            }
            else if ("jump".equals(bean.value("action_cmd"))) 
            {
                searchList();
            }
            else if ("prior".equals(bean.value("action_cmd"))) 
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchList();
            } 
            else if ("sort".equals(bean.value("action_cmd"))) 
            {
                searchList();
            } 
            else if ("clear".equals(bean.value("action_cmd"))) 
            {
                formClear();
                searchList();
            } 
            else if ("return".equals(bean.value("action_cmd"))) 
            {
                redirect("MenuAdmin.do");
                return;
            }
            formInit();
            searchList();
            forward("Shift.jsp");
            return;
        }
        
        if ("ShiftDetail".equals(bean.value("form_name"))) 
        {
            ShiftDAO dao = setWeb2Dao2InputInfo();
            
            if ("go_next".equals(bean.value("action_cmd"))) 
            {
                if (inputCheck(dao)) 
                {
                bean.setValue("request_cmd", bean.value("request_cmd"));
                bean.setValue("request_name", bean.value("request_name"));
                forward("ShiftDetail_2.jsp");
                return;
            }
                else 
                {
                    bean.setError("入力内容に誤りがあります");
                    forward("ShiftDetail.jsp");
                    return;
                }
            }
            else if ("return".equals(bean.value("action_cmd")));
            {
                formInit();
                searchList();
                forward("Shift.jsp");
                return;
            }
        }
        
        if ("ShiftDetail_2".equals(bean.value("form_name"))) 
        {
            if ("go_next".equals(bean.value("action_cmd"))) 
            {
                if ("ins".equals(bean.value("request_cmd"))) 
                {
                    insUserid();
                    bean.rtrimAllItem();
                    bean.setValue("request_name", "新規登録が完了しました");
                    setInputInfo2Dao2Web();
                    signUp();
                    forward("ShiftDetail_3.jsp");
                    return;
                } 
                else if ("update".equals(bean.value("request_cmd")))
                {
                   if (checkDataMatching())
                   {
                       setInputInfo2Dao2Web();
                       bean.setValue("request_name", "登録が完了しました");
                       dbEdit();
                       return;
                   }
                   else
                   {
                       bean.setError("処理中に別のユーザーがデータを変更しました。再度処理を行ってください。");
                       setDb2Web();
                       forward("ShiftDetail.jsp");
                       return;
                   }
                }
                else if ("delete".equals(bean.value("request_cmd"))) 
                {
                    bean.setValue("request_name", "削除が完了しました");
                    setInputInfo2Dao2Web();
                    delete();
                    return;
                }
            }
            if ("go_back".equals(bean.value("action_cmd")))
            {
                bean.setValue("request_cmd", bean.value("request_cmd"));
                setInputInfo2Dao2Web();
                forward("ShiftDetail.jsp");
                return;
            }
        }
        
        if ("ShiftDetail_3".equals(bean.value("form_name"))) 
        {
            if ("go_back".equals(bean.value("action_cmd")))
            {
                if ("return".equals(bean.value("request_cmd"))) 
                {
                    formInit();
                    searchList();
                    forward("Shift.jsp");
                    return;
                }
            }
        }
        
        formInit();
        searchList();
        forward("Shift.jsp");
        return;
    }

    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    public void formInit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "full_name_kana"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("lineCount", SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "Shift", "lineCount", "100"));
    }

    /**
     * フィールドをクリアする。.
     */
    private void formClear() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("list_search_full_name", "");
        bean.setValue("list_search_full_name_kana", "");
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
        if (bean.value("list_search_full_name").length() > 0) {
            if (100 < bean.value("list_search_full_name").length()) {
                errors.put("list_search_full_name", "氏名の入力内容が長すぎます。");
            }
        }
        if (bean.value("list_search_full_name_kana").length() > 0) {
            if (100 < bean.value("list_search_full_name_kana").length()) {
                errors.put("list_search_full_name_kana", "氏名よみの入力内容が長すぎます。");
            }
        }
        return errors;
    }

    /**
     * 検索を行いbeanに格納する。.
     */
    public void searchList() throws AtareSysException {
        WebBean bean = getWebBean();
        HashMap<String, String> errors;

        errors = inputCheck();
        if (errors.size() > 0) 
        {
            bean.setValue("errors", errors);
            return;
        }
        LinkedHashMap<String, String> sortKey = sortKey();
        ShiftDAO dao = new ShiftDAO();
        dao.setSearchName(bean.value("list_search_full_name"));

        DaoPageInfo daoPageInfo = new DaoPageInfo();
        if (!Validate.isInteger(bean.value("lineCount"))) 
        {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "Shift", "lineCount", bean.value("lineCount"));
        if (!Validate.isInteger(bean.value("pageNo"))) 
        {
            daoPageInfo.setPageNo(1);
        } 
        else 
        {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }
        ArrayList<ShiftDAO> listData = ShiftDAO.dbSelectList(dao, sortKey, daoPageInfo);
        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", daoPageInfo.getRecordCount());
        bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

        bean.getWebValues().remove("search_info");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
        bean.setValue("list", listData);
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
    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     * @throws AtareSysException
     */
    private boolean inputCheck(ShiftDAO ShiftDao) throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
       
        if ("ins".equals(bean.value("request_cmd")) || "update".equals(bean.value("request_cmd"))) 
        {
            String nameRegex = "^^[ぁ-んァ-ヶーa-zA-Z"+
                               "\\u30a0-\\u30ff\\u3040-\\u309f\\u3005-\\u3006\\u30e0-\\u9fcf]*$";
            Pattern pattern = Pattern.compile(nameRegex);
            Matcher matcher = pattern.matcher(bean.value("name"));
            if (bean.value("name").length() == 0 )
            {
                errors.put("name", "氏名を入力してください。");
            } 
            if (bean.value("name").length() > 20 )
            {
                errors.put("name", "氏名が長すぎます。");
            }
            else if (!matcher.matches()) // 数字が含まれているかチェック
            {  
                errors.put("name", "正しい氏名を入力してください。");
            }
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern epattern = Pattern.compile(emailRegex);
            Matcher ematcher = epattern.matcher(bean.value("email"));
            if (bean.value("email").length() == 0)
            {
                errors.put("email", "メールアドレスを入力してください。");
            }
            else if (!ematcher.matches()) // メアドに使用できる半角英数記号以外のチェック
            {  
                errors.put("email", "正しいメールアドレスを入力してください。");
            }
            }
            else if (bean.value("start_time").length() == 0)
            {
                errors.put("start_time", "始業時間を入力してください。");
            }
        
            if (bean.value("end_time").length() == 0)
            {
                errors.put("start_time", "終業時間を入力してください。");            
                }
            String workRegex = "^^[ぁ-んァ-ヶーa-zA-Z0-9"+
                               "\\u30a0-\\u30ff\\u3040-\\u309f\\u3005-\\u3006\\u30e0-\\u9fcf]*$";
            Pattern wpattern = Pattern.compile(workRegex);
            Matcher wmatcher = wpattern.matcher(bean.value("work_place"));
            if (bean.value("work_place").length() == 0)
            {
                errors.put("work_place", "配属先を入力してください");
            }
            else if (!wmatcher.matches()) // 全角数字が含まれているかチェック
            {  
                errors.put("work_place", "数字を半角にしてください。");
            }
            else if ("update".equals(bean.value("request_cmd"))) 
            {
                if (ShiftDao.isEmailExists(bean.value("email"), bean.value("main_key")))
                {
                    // 重複している場合のエラーメッセージ設定
                    errors.put("email", "このメールアドレスは既に登録されています。");
                }
            }
                if (ShiftDao.isIdExists(bean.value("id"), bean.value("main_key"))) 
                {
                    // 重複している場合のエラーメッセージ設定
                    errors.put("id", "このＩＤは既に登録されています。");
                }
                else if ("ins".equals(bean.value("request_cmd"))) 
                {
                    if (ShiftDao.isEmailExists(bean.value("email"), bean.value("main_key")))
                    {
                        // 重複している場合のエラーメッセージ設定
                        errors.put("email", "このメールアドレスは既に登録されています。");
                    }
                }
                    if (ShiftDao.isIdExists(bean.value("id"), bean.value("main_key"))) 
                    {
                        // 重複している場合のエラーメッセージ設定
                        errors.put("id", "このＩＤは既に登録されています。");
                    }
                
            
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }
    private boolean insUserid() throws AtareSysException
    {
        WebBean bean = getWebBean();
        String SHIFTid = GetNumber.getNumberChar("shift_id"); // ユーザーIDを新規作成
        bean.setValue("id", SHIFTid);
        
        return true;
    }
    
    public void dbEdit() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        ShiftDAO dao = setWeb2Dao2InputInfo();
        String Main_key = bean.value("main_key");//主キーの取得
           
        try {
            DbBase.dbBeginTran();
            dao.dbUpdate(Main_key);
            DbBase.dbCommitTran();
            forward("ShiftDetail_3.jsp");
        } catch (Exception e) {
            DbBase.dbRollbackTran();
            redirect("Shift.do");
        }
        
    }

    /**
     * 削除の場合
     * @throws AtareSysException
     */
    public void delete() throws AtareSysException
    {
        WebBean bean = getWebBean();
        ShiftDAO dao = setWeb2Dao2InputInfo();
        String id = bean.value("main_key");

        try {
            dao.dbDelete(id);
            forward("ShiftDetail_3.jsp");
        }
        catch (Exception e) 
        {
            redirect("Shift.do");
        }
    }
    /**
     * データベースの内容を表示エリアに編集する。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    private boolean setDb2Web() throws AtareSysException {
        WebBean bean = getWebBean();
        ShiftDAO dao = new ShiftDAO();
        if (!dao.dbSelect(bean.value("main_key"))) {
            return false;
        }
        bean.setValue("name", dao.getName());
        bean.setValue("email", dao.getEmail());
        bean.setValue("start_time", dao.getStartTime());
        bean.setValue("end_time", dao.getEndTime());
        bean.setValue("work_place", dao.getWorkPlace());
        bean.setValue("main_key", dao.getId());
        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }

    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *　main_keyを使用したい場合のメソッド
     * @return なし
     * @throws AtareSysException エラー
     */
    private ShiftDAO setWeb2Dao2InputInfo() throws AtareSysException {
        WebBean bean = getWebBean();
        ShiftDAO dao = new ShiftDAO();

        dao.setName(bean.value("name"));
        dao.setEmail(bean.value("email"));
        dao.setStartTime(bean.value("start_time"));
        dao.setEndTime(bean.value("end_time"));
        dao.setWorkPlace(bean.value("work_place"));
        dao.setId(bean.value("main_key"));

        bean.setValue("input_info", Sup.serialize(dao)); // DAOオブジェクトをシリアライズしてWebBeanに保存
        return dao;
    }
   /**
    * すでにDAOクラスに格納されてる項目をデシリアライズしてbeanにセットする
    * @throws AtareSysException エラー
    */
    private void setInputInfo2Dao2Web() throws AtareSysException {
        WebBean bean = getWebBean();
        ShiftDAO dao = (ShiftDAO) Sup.deserialize(bean.value("input_info"));
        bean.setValue("name", dao.getName());
        bean.setValue("email", dao.getEmail());
        bean.setValue("start_time", dao.getStartTime());
        bean.setValue("end_time", dao.getEndTime());
        bean.setValue("work_place", dao.getWorkPlace());
        bean.setValue("main_key", dao.getId());
    }

    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     * idの項目を利用したい場合のメソッド
     * @return なし
     * @throws AtareSysException エラー
     */
    private ShiftDAO setWeb2Dao2InputInfoid2() throws AtareSysException {
        WebBean bean = getWebBean();
        ShiftDAO dao = new ShiftDAO();

        dao.setName(bean.value("name"));
        dao.setEmail(bean.value("email"));
        dao.setStartTime(bean.value("start_time"));
        dao.setEndTime(bean.value("end_time"));
        dao.setWorkPlace(bean.value("work_place"));
        dao.setId(bean.value("id"));

        bean.setValue("input_info", Sup.serialize(dao)); // DAOオブジェクトをシリアライズしてWebBeanに保存
        return dao;
    }
    /**
     * @false エラーが起きShift.jspに飛ぶ
     * @return　
     */
    private boolean checkDataMatching() throws AtareSysException {
        WebBean bean = getWebBean();
        ShiftDAO dao = new ShiftDAO();
        if (!dao.dbSelect(bean.value("main_key")))
        {
            return false;
        }
        return Sup.serializeIsEquals(bean.value("select_info"), dao);
    }

   private boolean signUp() throws AtareSysException {
       ShiftDAO dao = setWeb2Dao2InputInfoid2();
       try {
      // 入力内容をデータベースに保存
           dao.dbInsert();
           return true;
           
       } catch (Exception e) {
      return false;
    }
  }
}