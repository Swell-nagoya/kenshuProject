package jp.swell.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import jp.swell.dao.UserFileDao;
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
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();
        try {
        UserLoginInfo login = (UserLoginInfo)getLoginInfo();
        // 追加：JSP 上で使うためのログインユーザー名・ID
        bean.setValue("loginUserName", login.getLastName() + " " + login.getFirstName());
        bean.setValue("loginUserId", login.getUserInfoId());
        
        // デバッグログ：どのフォーム／コマンドで呼ばれたか
        String form = bean.value("form_name");
        String actionCmd = bean.value("action_cmd");
        String requestCmd = bean.value("request_cmd");

        // 共通取得
        String mainKey = bean.value("main_key");
        String fileName = bean.value("file_name");
        FileDao dao = setWeb2Dao2InputInfo();
        

        if ("FileDetail".equals(form)) {
            // ① upload ボタン押下 → 確認画面へ
            if ("upload".equals(actionCmd)) {
             System.out.println("hit");
            	
            	  if(inputCheck(dao)) {

                 byte[] fileData = (byte[]) bean.object("file");
                 bean.setValue("fileData", java.util.Base64.getEncoder().encodeToString(fileData));
                 
                 
            	    dao.setUserInfoId(bean.value("user_info_id"));
                 bean.setValue("input_info", Sup.serialize(dao));

                 bean.setValue("request_name", "登録する");
                 bean.setMessage("この内容で登録します。よろしいですか？");
                 forward("FileDetail_2.jsp");
            	}
             else 
             {
                 bean.setError("入力内容に誤りがあります");
                 forward("FileDetail.jsp");
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
                   try {
                   	  dbEdit(getRequest());
                   } catch (IOException | ServletException e) {
                      throw new AtareSysException(e);
                   }
                    searchList();
                    redirect("FileList.do");


                } else if ("deleteEnter".equals(requestCmd)) {
                     bean.rtrimAllItem();
                	    forward(dbDeletef(mainKey));
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
        } catch (Exception e) {
         bean.setError("処理中にエラーが発生しました: " + e.getMessage());
         forward("ErrorPage.jsp");
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
        System.out.println(sortKey);

        ArrayList<FileDao> fileList = FileDao.dbSelectList(fileDao, sortKey, daoPageInfo);
        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", fileList.size());
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


        // ユーザーをすべて取得 
        String allUserName = null;

         int index = 0;

         for (UserInfoDao user : allUsers) {
             String userInfoId = user.getUserInfoId();
             String lastName = user.getLastName();
             String firstName = user.getFirstName();

             if(index == 0) {
              	allUserName = "{";
             } else {
               allUserName += ",{";
             }
             
             
             allUserName +=  "id:";

             allUserName += '"' + userInfoId + '"';
             
             allUserName += " ,name:";
             // 2. 文字列を作成し、配列の現在の位置に格納する
             allUserName += '"' + lastName + " " + firstName + '"';
               
             allUserName += "}";
             
             index++;
             
         }

        bean.setValue("allUserName", allUserName);


         
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
    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する。.
     *
     * @return dao 
     * @throws AtareSysException フレームワーク共通例外
     */
    FileDao setWeb2Dao2InputInfo() throws AtareSysException {
     WebBean bean = getWebBean();
     FileDao dao = new FileDao();
     dao.setUserInfoId(bean.value("user_info_id"));


     bean.setValue("input_info", Sup.serialize(dao));
     bean.setValue("dao", dao);
     return dao;
    }
    /**
     * ファイルアップロード
     *
     * @return dao
     * @throws ServletException 
     * @throws IOException 
     * @throws AtareSysException フレームワーク共通例外
     */
    private FileDao dbEdit(HttpServletRequest request)
            throws AtareSysException, IOException, ServletException {
        WebBean bean = getWebBean();
        FileDao dao = new FileDao();

        FileUpload(request, dao.getUserInfoId());

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

        // user.dir でカレントディレクトリ（プロジェクトのルート）を取得
        String projectPath = System.getProperty("user.dir");

        String projectPathResult = projectPath.replace("\\", "/");
        
        
        String filePath = projectPathResult + "/WebContent/upload"; //保存先フォルダのパス設定
        String skey = GetNumber.getRandomNo(16); //file_key生成

        // ファイルデータを取得
        FileUtil fileUtil = new FileUtil();
        
        String base64String = bean.value("fileData");

        byte[] fileData = java.util.Base64.getDecoder().decode(base64String);
     
     

        String file_value = bean.value("file_value");
        String fileExtension = file_value.replaceAll("^.*\\.", "");
        
        //String mimeType = getMimeTypeFromBytes(fileData); //ファイルデータからmimetypeを取得
        //String fileExtension = getExtensionFromMimeType(mimeType); //拡張子取得

        String mimeType = getExtensionFromFileMimeType(fileExtension); //ファイルデータからmimetypeを取得
        String fileName = bean.value("input_name") + "." + fileExtension; // ファイル名を取得
        String systemFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8); //system_file_id生成
        
        
        // 拡張子を一度だけ追加
        if (fileExtension != null && !fileExtension.isEmpty()) {
            systemFileName += "." + fileExtension;
        }
        
        
        // 完全なファイルパスの生成
        String fullPath = filePath + "/" + systemFileName;
        

        if ( "/".equals(fullPath) ) {
          return null;
        }
        
        if (!fileUtil.outputFile(fullPath, fileData)) {
            return null;
        }

/*
        // アップロード期限を設定
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 1); // 現在の日時に1週間追加
        java.util.Date expirationDate = calendar.getTime(); // Date型を取得

        // expirationDateをString型に変換
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expirationDateString = sdf.format(expirationDate);
*/      

        // 各送信先ユーザーに対してデータベースにファイル情報を登録
        String fileId = UUID.randomUUID().toString().substring(0, 13);
        FileDao fileDao = new FileDao();
        String expirationDate = bean.value("expiration_data");
        

//      元のフォーマット定義と解析（日付のみを取得）
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        LocalDate parsedDate = LocalDate.parse(expirationDate, inputFormatter);

//      現在の「時間・分・秒」のみを取得
        LocalTime nowTime = LocalTime.now();

//      解析した日付と、現在の時間を結合する
        LocalDateTime combinedDateTime = parsedDate.atTime(nowTime);

//      目的の "yyyy/MM/dd HH:mm:ss" 形式に変換
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String expirationDateString = combinedDateTime.format(outputFormatter);
     
      /*
        fileDao.dbFileInsert(fileId, userInfoId, fullPath, fileName, mimeType, systemFileName, senderUserId, skey,
        expirationDateString);
        */
        // fileテーブルにユーザー情報を挿入
       /* fileDao.dbFileInsert(fileId, sourceUserInfoIdsString, fullPath, fileName, mimeType, systemFileName, senderUserId, skey,
        expirationDateString);
*/
        fileDao.dbFileInsert(fileId, sourceUserInfoIdsString, fullPath, fileName, mimeType, systemFileName, senderUserId, skey,
          expirationDateString);

        fileDaos.add(fileDao);

        UserFileDao userFileDao = new UserFileDao();
        
        for (String userInfoId : destinationUserInfoIds) { // 送信先ユーザーIDを使用
          // user_filesテーブルにユーザー情報を挿入
          userFileDao.dbUserFileInsert(userInfoId,fileId);
        }
        

        return fileDaos;
    }

    /**
     * MIMEタイプを取得するメソッド
     * @param fileData
     * @return
     */
    /*
    private String getMimeTypeFromBytes(byte[] fileData) {
        if (fileData.length >= 8) {
         String header = new String(fileData, 0, 8, java.nio.charset.StandardCharsets.ISO_8859_1);
         
         
            if (header.startsWith("\u00D0\u00CF\u0011")) { // Wordファイルの判定
                return "application/msword"; // .doc
            } else if (header.startsWith("\u00D0\u00CF\u0011")) {
                return "application/vnd.ms-excel"; // .xls
            } else if (header.startsWith("\u00FF\u00D8")) {
                return "image/jpeg"; // JPEG画像
            } else if (header.startsWith("\u0089PNG\r\n\u001A\n")) {
                return "image/png"; // PNG画像
            } else if (header.startsWith("GIF87a") || header.startsWith("GIF89a")) {
                return "image/gif"; // GIF画像
            } else if (header.startsWith("\u00D0\u00CF\u0011\u00E0")) {
                return "application/msword"; 
            } else if (header.startsWith("PK")) { // Word 2007以降のファイル
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; // .docx
            } else if (header.startsWith("PK")) { // Excelファイルの判定
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // .xlsx
            }
        }
        return ""; // デフォルト
    }
    */

    /**
     * MIMEタイプから拡張子を取得するメソッド
     * @param mimeType
     * @return
     */
    /*
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
        case "image/jpeg":
            return ".jpg"; // JPEG画像
        case "image/png":
            return ".png"; // PNG画像
        case "image/gif":
            return ".gif"; // GIF画像
        default:
            return ""; // デフォルトは空文字
        }
    }
    */

    private String getExtensionFromFileMimeType(String fileExtension) {

        switch (fileExtension) {
        case "doc":
            return "application/msword";
        case "docx":
         return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        case "xls":
         return "application/vnd.ms-excel";
        case "xlsx":
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        case "pptx":
            return"application/vnd.openxmlformats-officedocument.presentationml.presentation";
        case "pdf":
            return "application/pdf";
        case "png":
            return "image/png";
        case "jpg":
            return "image/jpeg";
        case "jpeg":
            return "image/jpeg";
        case "txt":
            return "text/plain"; 
        default:
             return "application/octet-stream"; // デフォルトは空文字
        }
    }
    

    /**
     * データベースから指定されたレコードを削除するメソッド
     * @return 
     * @throws AtareSysException
     */
    public String dbDeletef(String mainKey) throws AtareSysException {
        WebBean bean = getWebBean();
        bean.rtrimAllItem();
        FileDao dao = setWeb2Dao2InputInfo();
        
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();

        try {
            // ファイルの存在を確認
            if (!dao.dbSelect(mainKey)) {

                bean.setError("ファイルが見つかりませんでした。");
                return "FileList.jsp";
            }

            // ファイル情報を取得
            FileDao fileData = dao; // ここは前の行で dao を設定したので、そのまま使用

            String fileOwnerId = fileData.getUploadUserId(); // ファイルの所有者ID
            
            // 所有者が現在のユーザーと一致するか確認
            if (!userLoginInfo.getUserInfoId().equals(fileOwnerId)) {
                bean.setError("このファイルを削除する権限がありません。");
                return "FileDetail_3.jsp";
            }

            // 所有者が一致する場合は削除処理を実行
            DbBase.dbBeginTran();
            dao.dbDelete(mainKey);
            DbBase.dbCommitTran();
            

            // ファイルのパスを取得
            String filePath = fileData.getFilePath();
            // アップロードした実体ファイルを削除
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return "FileList.do";
        } catch (Exception e) {
            DbBase.dbRollbackTran();
            return "FileDetail.jsp";
        }
    }
    
    

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     * @throws AtareSysException
     */
     boolean inputCheck(FileDao pFileDao) throws AtareSysException {
       	
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        
        
        // ファイル名の入力
        String inputName = bean.value("input_name").trim();
        if (inputName.length() == 0) {
          errors.put("input_name_empty", "ファイル名を入力してください。");
        }
        
        // ファイルのリンク
        String fileValue = bean.value("file_value").trim();
        if (fileValue.length() == 0) {
          errors.put("file_value_empty", "ファイルリンクを入力してください。");
        }
        
        // ダウンロードの有効期限が本日よりも後か判定
        String expirationData = bean.value("expiration_data");
        String resultData = expirationData.replaceAll("[年月日]", "");
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);
        
        
        int comparison = resultData.compareTo(formattedDate);

        if (comparison < 0) {
          errors.put("expiration_data_empty", "本日または後日を登録してください。");
       
        }
        
        
        return errors.isEmpty();
    }
    
    

}
