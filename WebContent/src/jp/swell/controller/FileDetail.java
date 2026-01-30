package jp.swell.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.icu.text.SimpleDateFormat;

import jp.patasys.common.AtareSysCalendar;
import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.FileUtil;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

/**
 * ファイル情報を登録・更新・削除するためのコントローラクラス
 */
public class FileDetail extends ControllerBase {

    /**
     * コントローラの初期設定を行う。
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
        UserLoginInfo login = (UserLoginInfo)getLoginInfo();
        // 追加：JSP 上で使うためのログインユーザー名・ID
        bean.setValue("loginUserName", login.getLastName() + " " + login.getFirstName());
        bean.setValue("loginUserId", login.getUserInfoId());
        
        // デバッグログ：どのフォーム／コマンドで呼ばれたか
        String form = bean.value("form_name");
        String actionCmd = bean.value("action_cmd");
        String requestCmd = bean.value("request_cmd");

        // 共通取得
        String inputName = bean.value("input_name");
        String mainKey = bean.value("main_key");
        String fileName = bean.value("file_name");
        FileDao dao = setWeb2Dao2InputInfo();

        if ("FileDetail".equals(form)) {
            // ① upload ボタン押下 → 確認画面へ
            if ("upload".equals(actionCmd)) {
                try {
                    setWeb2Dao2InputInfo(getRequest());
                    bean.setValue("request_name", "登録する");
                    bean.setMessage("この内容で登録します。よろしいですか？");
                    bean.setValue("input_name", inputName);
                    forward("FileDetail_2.jsp");
                } catch (IOException | ServletException e) {
                    throw new AtareSysException(e);
                }

             // ② sub ボタン押下 → サブ画面（ユーザー選択）へ（送信先のみ）
            } else if ("sub".equals(actionCmd)) {
                searchUserList();
                bean.setValue("request_name", "送信先");
                forward("FileUserList.jsp");
                return;   // 忘れずに戻す


                // ③ return ボタン押下 → 一覧画面へ戻す
            } else if ("return".equals(actionCmd)) {
                forward("FileList.do");
            }

        } else if ("FileList".equals(form)) {
            if ("go_next".equals(actionCmd)) {
                if ("ins".equals(requestCmd)) {
                    bean.setValue("request_name", "登録する");
                    searchList();
                    forward("FileDetail.jsp");

                } else if ("download".equals(requestCmd)) {
                    dao.dbSelect(mainKey);
                    downloadFileWrite(dao);

                } else if ("deletef".equals(requestCmd)) {
                    if (!dao.dbSelect(mainKey)) {
                        bean.setError("データの取得に失敗しました");
                        forward("FileList.jsp");
                    } else {
                        bean.setValue("request_name", "削除する");
                        bean.setMessage("このファイルを削除します。よろしいですか？");
                        bean.setValue("file_name", fileName);
                        forward("FileDetail_2.jsp");
                    }
                }
            }

        } else if ("FileDetail_2".equals(form)) {
            if ("go_next".equals(actionCmd)) {
                if ("insEnter".equals(requestCmd)) {
                    searchList();
                    redirect("FileList.do");

                } else if ("download".equals(requestCmd)) {
                    dao.dbSelect(mainKey);
                    downloadFileWrite(dao);

                } else if ("deleteEnter".equals(requestCmd)) {
                    dbDeletef();
                }

            } else if ("return".equals(actionCmd)) {
                if ("ins".equals(requestCmd)) {
                    forward("FileDetail.jsp");
                } else if ("delete".equals(requestCmd)) {
                    searchList();
                    redirect("FileList.do");
                }
            }

        } else if ("FileDetail_3".equals(form)) {
            if ("return".equals(actionCmd)) {
                searchList();
                redirect("FileList.do");
            }
        }
    }

    /**
     * 検索を行いbeanに格納する。
     */
    private void searchList() throws AtareSysException {
        WebBean bean = getWebBean();
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();

        LinkedHashMap<String, String> sortKey = sortKey();
        FileDao fileDao = new FileDao();
        fileDao.setUserInfoId(userLoginInfo.getUserInfoId());
        fileDao.setFileName(bean.value("file_name"));

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

        ArrayList<FileDao> fileList = FileDao.dbSelectList(fileDao, sortKey, daoPageInfo);
        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", fileList.size()); // ファイルリストのサイズ
        bean.setValue("maxPageNo", (fileList.size() / Integer.parseInt(bean.value("lineCount")) + 1));

        // ルーム情報の取得とセット
        ArrayList<FileDao> files = fileDao.getAllFiles();

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
        bean.setValue("files", files);
        bean.setValue("list", fileList);
    }

    /**
     * サブ画面（送信元／送信先ユーザー選択）用に、
     * ユーザー一覧をページ単位で取得して WebBean にセットする。
     */
    private void searchUserList() throws AtareSysException {
        WebBean bean = getWebBean();
        
        String req = bean.value("request_name");
        bean.setValue("request_name", req);
        
        String selectedIds = bean.value("selectedIds");
        if (selectedIds == null) {
            selectedIds = "";
        }

        // 全ユーザーを取得
        List<UserInfoDao> allUsers = new UserInfoDao().getAllUsers();

        // 現在のページ番号を取得（未設定時は 1）
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(bean.value("pageNo"));
        } catch (NumberFormatException ignored) {
        }

        // 1ページあたり表示件数
        final int pageSize = 10;

        // 総ページ数を計算
        int total = allUsers.size();
        int maxPage = (total + pageSize - 1) / pageSize;

        // 表示範囲を計算してサブリストを取得
        int fromIndex = Math.min((pageNo - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<UserInfoDao> pageUsers = new ArrayList<>(allUsers.subList(fromIndex, toIndex));

        // ページング結果を WebBean に格納
        bean.setValue("user_data", pageUsers);
        bean.setValue("pageNo", String.valueOf(pageNo));
        bean.setValue("maxPageNo", String.valueOf(maxPage));
        bean.setValue("selectedIds", selectedIds);
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

    private FileDao setWeb2Dao2InputInfo() throws AtareSysException {
        WebBean bean = getWebBean();
        FileDao dao = new FileDao();
        dao.setFileName(bean.value("file_name"));

        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }

    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する。.
     *
     * @return なし
     * @throws ServletException 
     * @throws IOException 
     * @throws AtareSysException フレームワーク共通例外
     */
    private UserInfoDao setWeb2Dao2InputInfo(HttpServletRequest request)
            throws AtareSysException, IOException, ServletException {
        WebBean bean = getWebBean();
        UserInfoDao dao = new UserInfoDao();
        dao.setUserInfoId(bean.value("user_info_id"));

        FileUpload(request, dao.getUserInfoId());

        bean.setValue("input_info", Sup.serialize(dao));
        bean.setValue("dao", dao);
        return dao;
    }

    /**
     * ファイルデータを取得し、アップロードした後にデータベースへ登録
     * @param request
     * @param pUserInfoId
     * @return
     * @throws AtareSysException
     * @throws IOException
     * @throws ServletException
     */
    private ArrayList<FileDao> FileUpload(HttpServletRequest request, String pUserInfoId)
            throws AtareSysException, IOException, ServletException {
        WebBean bean = getWebBean();
        ArrayList<FileDao> fileDaos = new ArrayList<>();

        // 送信元ユーザーIDを取得
        String sourceUserInfoIdsString = bean.value("user_info_id"); // 送信元ユーザーIDを取得
        String[] sourceUserInfoIds = sourceUserInfoIdsString.split(",");

        // 送信先ユーザーIDを取得
        String destinationUserInfoIdsString = bean.value("destination_user_info_id"); // 送信先ユーザーID
        String[] destinationUserInfoIds = destinationUserInfoIdsString.split(",");

        // 送信元ユーザーのIDを取得
        String senderUserId = sourceUserInfoIds.length > 0 ? sourceUserInfoIds[0] : null; // 最初のユーザーを送信元として選択

        String filePath = "C:/git/training/kenshuProject/WebContent/upload"; //保存先フォルダのパス設定
        String skey = GetNumber.getRandomNo(16); //file_key生成

        // ファイルデータを取得
        FileUtil fileUtil = new FileUtil();
        byte[] fileData = (byte[]) bean.object("file");
        String mimeType = getMimeTypeFromBytes(fileData); //ファイルデータからmimetypeを取得
        String fileExtension = getExtensionFromMimeType(mimeType); //拡張子取得
        String fileName = bean.value("input_name") + fileExtension; // ファイル名を取得
        String systemFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8); //system_file_id生成

        // 拡張子を一度だけ追加
        if (fileExtension != null && !fileExtension.isEmpty()) {
            systemFileName += fileExtension;
        }

        // 完全なファイルパスの生成
        String fullPath = filePath + "/" + systemFileName;
        if (!fileUtil.outputFile(fullPath, fileData)) {
            return null;
        }

        // アップロード期限を設定
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 1); // 現在の日時に1週間追加
        java.util.Date expirationDate = calendar.getTime(); // Date型を取得

        // 各送信先ユーザーに対してデータベースにファイル情報を登録
        for (String userInfoId : destinationUserInfoIds) { // 送信先ユーザーIDを使用
            String fileId = UUID.randomUUID().toString().substring(0, 13);
            FileDao fileDao = new FileDao();

            // expirationDateをString型に変換
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String expirationDateString = sdf.format(expirationDate);

            fileDao.dbFileInsert(fileId, userInfoId, fullPath, fileName, mimeType, systemFileName, senderUserId, skey,
                    expirationDateString);
            fileDaos.add(fileDao);
        }
        return fileDaos;
    }

    /**
     * MIMEタイプを取得するメソッド
     * @param fileData
     * @return
     */
    private String getMimeTypeFromBytes(byte[] fileData) {
        if (fileData.length >= 4) {
            String header = new String(fileData, 0, 4);

            if (header.startsWith("\u00D0\u00CF\u0011")) { // Wordファイルの判定
                return "application/msword"; // .doc
            } else if (header.startsWith("PK")) { // Word 2007以降のファイル
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; // .docx
            } else if (header.startsWith("PK")) { // Excelファイルの判定
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // .xlsx
            } else if (header.startsWith("\u00D0\u00CF\u0011")) {
                return "application/vnd.ms-excel"; // .xls
            }
        }
        return ""; // デフォルト
    }

    /**
     * MIMEタイプから拡張子を取得するメソッド
     * @param mimeType
     * @return
     */
    private String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
        case "application/msword":
            return ".doc"; // Word 97-2003
        case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            return ".docx"; // Word 2007+
        case "application/vnd.ms-excel":
            return ".xls"; // Excel 97-2003
        case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            return ".xlsx"; // Excel 2007+
        case "application/pdf":
            return ".pdf"; // PDFファイル
        default:
            return ""; // デフォルトは空文字
        }
    }

    /**
     * データベースから指定されたレコードを削除するメソッド
     * @throws AtareSysException
     */
    public void dbDeletef() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        FileDao dao = setWeb2Dao2InputInfo();
        String mainKey = bean.value("main_key"); // fileIdの取得
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo(); // 現在のユーザー情報を取得

        try {
            // ファイルの存在を確認
            if (!dao.dbSelect(mainKey)) {
                bean.setError("ファイルが見つかりませんでした。");
                forward("FileList.jsp");
                return;
            }

            // ファイル情報を取得
            FileDao fileData = dao; // ここは前の行で dao を設定したので、そのまま使用

            String fileOwnerId = fileData.getUploadUserId(); // ファイルの所有者ID

            // 所有者が現在のユーザーと一致するか確認
            if (!userLoginInfo.getUserInfoId().equals(fileOwnerId)) {
                bean.setError("このファイルを削除する権限がありません。");
                forward("FileDetail_3.jsp");
                return;
            }

            // 所有者が一致する場合は削除処理を実行
            DbBase.dbBeginTran();
            dao.dbDelete(mainKey);
            DbBase.dbCommitTran();
            redirect("FileList.do");
        } catch (Exception e) {
            DbBase.dbRollbackTran();
            forward("FileDetail.jsp");
        }
    }

    /**
     * ファイルをダウンロードしてきた時の処理
     *
     * @return なし
     * @throws AtareSysException フレームワーク共通例外
     */
    private void downloadFileWrite(FileDao dao) throws AtareSysException {
        // ファイルを保存するフォルダ名を取得
        ServletOutputStream out = null; // 出力ストリームを初期化
        String baseFileName = dao.getFileName(); // 基本ファイル名を取得
        String mimeType = dao.getMimeType(); // MIMEタイプを取得
        String filePath = dao.getFilePath();// フルファイルパスを取得

        try {
            // 期限チェック
            if (isExpired(dao.getExpirationDate())) {
                // 期限が過ぎている場合の処理
                this.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN, "このファイルのダウンロードは期限が切れています。");
                return; // 処理を中止
            }

            // ユーザーエージェントを取得
            String ua = this.getRequest().getHeader("user-agent");
            String attachmentFileName = ""; // 添付ファイル名を初期化

            // ブラウザによってファイル名の設定を分岐
            if (ua.indexOf("MSIE") == -1) {
                // Firefox, Opera 11など
                attachmentFileName = String.format(Locale.JAPAN, "attachment; filename*=utf-8'jp'%s",
                        URLEncoder.encode(baseFileName, "utf-8"));
            } else {
                // IE7, 8, 9用の処理
                attachmentFileName = String.format(Locale.JAPAN, "attachment; filename=\"%s\"",
                        new String(baseFileName.getBytes("MS932"), "ISO8859_1"));
            }

            // レスポンスの文字エンコーディングを設定
            this.getResponse().setCharacterEncoding("UTF-8");

            // 現在日時を取得し、レスポンスのヘッダーに設定
            Calendar now = AtareSysCalendar.getInstance();
            Calendar exp = AtareSysCalendar.getInstance();
            exp.set(1970, 0, 1, 0, 0, 0); // 過去の日付を設定（無期限のキャッシュを防ぐ）
            this.getResponse().setDateHeader("Last-Modified", now.getTime().getTime());
            this.getResponse().setDateHeader("Expires", exp.getTime().getTime());
            this.getResponse().setHeader("pragma", "no-cache");
            this.getResponse().setHeader("Cache-Control", "no-cache");

            // コンテンツタイプを設定（例: Excelファイル）
            this.getResponse().setContentType(mimeType);

            // 添付ファイル名をレスポンスヘッダーに設定
            this.getResponse().setHeader("Content-Disposition", attachmentFileName);

            // レスポンスの出力ストリームを取得
            out = this.getResponse().getOutputStream();

            // ファイルを出力ストリームに書き込み
            FileUtil fileUtil = new FileUtil();
            fileUtil.inputFile(filePath, out);

            out.flush(); // 出力ストリームをフラッシュ
        } catch (IOException e) {
            // I/Oエラーが発生した場合
            e.printStackTrace();
            throw new AtareSysException(e.getMessage()); // カスタム例外を投げる
        } finally {
            try {
                // 出力ストリームを閉じる
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                // クローズ時にエラーが発生した場合
                e.printStackTrace();
                throw new AtareSysException(e.getMessage()); // カスタム例外を投げる
            }
        }
    }

    /**
     * ファイルの期限が切れているかをチェック
     *
     * @param expirationDate 期限日
     * @return 期限切れの場合はtrue、そうでなければfalse
     */
    private boolean isExpired(String expirationDateString) {
        if (expirationDateString == null || expirationDateString.isEmpty()) {
            return false; // 期限が設定されていない場合は未期限とする
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expirationDate = sdf.parse(expirationDateString);
            return expirationDate.before(new Date()); // 現在の日時と比較
        } catch (ParseException e) {
            e.printStackTrace(); // 解析エラーの処理
            return false; // 解析に失敗した場合、期限切れとしない
        }
    }
}
