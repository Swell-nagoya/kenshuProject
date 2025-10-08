package jp.swell.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class UserMenuResarve
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.FileUtil;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.dao.ReserveDao;
import jp.swell.dao.ReserveFileDao;
import jp.swell.dao.RoomDao;
import jp.swell.dao.UserFileDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.dao.UserReserveDao;

public class UserYoyakuDetail extends ControllerBase {
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
    public void doInit()
    {
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
  @Override
  public void doActionProcess() throws AtareSysException {
    WebBean bean = getWebBean();
    if ("UserYoyakuDetail".equals(bean.value("form_name"))) {
      bean.trimAllItem();
      if ("top".equals(bean.value("action_cmd"))) {
        searchList();
      }

      else if ("home".equals(bean.value("action_cmd"))) {
        searchList();
      }

      else if ("jump".equals(bean.value("action_cmd"))) {
        searchList();
      }

      else if ("sort".equals(bean.value("action_cmd"))) {
        searchList();
      }

      else if ("clear".equals(bean.value("action_cmd"))) {
        searchList();
      }

      // 部屋の情報を新規登録する条件を追加
      else if ("insertRoom".equals(bean.value("form_name"))) {
        insertRoomInfo();
      }

      else if ("reserve".equals(bean.value("action_cmd"))) {
        ReserveDao reserveDao = setWebDaoInputInfo();
        if (!reserveDao.Duplication()) {
          // 重複している場合のエラーメッセージ設定
          forward("ReserveError.jsp");
          return;
        }else if (inputCheck(reserveDao)) {
          processReservation();
          bean.setMessage("この内容で修正します。よろしいですか？");
          forward("ReserveOk.jsp");
        } else {
          bean.setError("入力内容に誤りがあります");
          setInputInfoDaoWeb();
          forward("UserMenuReserve.jsp");
        }
      // 部屋の情報の編集
      } else if ("edit".equals(bean.value("action_cmd"))) {
        ReserveDao reserveDao = setWebDaoInputInfo();
        if (inputCheck(reserveDao)) {
          updateReseveInfo();
          forward("ResEditComp.jsp");
          return; // メソッドを終了
        } else {
          bean.setError("入力内容に誤りがあります");
          setInputInfoDaoWeb();
          forward("ReserveEdit.jsp");
          return; // メソッドを終了
        }
      }
      // 部屋の情報の削除
      else if ("delete".equals(bean.value("action_cmd"))) {
        setInputInfoDaoWeb();
        deleteReseveInfo();
        forward("ResDelComp.jsp");
        return; // メソッドを終了
      }
      else if ("sub".equals(bean.value("action_cmd")))
      {
        searchList();
        forward("Reserve_User.jsp");
        return; // メソッドを終了
      }

    }
    else  if ("ReserveError".equals(bean.value("form_name"))) {
      if ("return".equals(bean.value("action_cmd"))) {
        setInputInfoDaoWeb();
        bean.setValue("request_name", "戻る");
        forward("UserMenuReserve.jsp");
      }
    }

    else if ("UserInfoDetail_1".equals(bean.value("form_name")) || "UserInfoDetail_2".equals(bean.value("form_name"))
        || "UserInfoDetail_3".equals(bean.value("form_name"))) {
      setWebBeanFromSerialize(bean.value("search_info"));
      bean = getWebBean();
      searchList();
      forward("UserMenuHome.jsp");
    }

    else {
      forward("UserMenuHome.do");
    }
  }


  /**
   * 入力チェックを行う。.
   *
   * @param dao
   *
   * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
   */
  private boolean inputCheck(ReserveDao dao) {

    WebBean bean = getWebBean();
    HashMap<String, String> errors = bean.getItemErrors();
   // reservation_dateがYYYY年MM月DD日の形式であるかをチェック
    String reservationDate = bean.value("reservation_date");
    if (!reservationDate.matches("^\\d{4}年\\d{2}月\\d{2}日$")) {
        errors.put("reservation_date", "YYYY年MM月DD日形式で入力してください");
    }

//    //checkin_timeがHH:MMの形式であるかをチェック
//    String checkinTime = bean.value("checkin_time");
//    if (!checkinTime.matches("^\\d{2}:\\d{2}$")) {
//        errors.put("checkin_time", "HH:MM形式で入力してください");
//    }
//
//    // checkout_timeがHH:MMの形式であるかをチェック
//    String checkoutTime = bean.value("checkout_time");
//    if (!checkoutTime.matches("^\\d{2}:\\d{2}$")) {
//        errors.put("checkout_time", "HH:MM形式で入力してください");
//    }

    // 会議室が選択されているかをチェック
    String roomId = bean.value("room_id");
    if (roomId == null || roomId.isEmpty() || "会議室選択".equals(roomId)) {
      errors.put("room_id", "会議室を選択してください。");
    }
    // 予約者が選択されているかをチェック
    String userId = bean.value("user_info_id");
    if (userId == null || userId.isEmpty() || "選択してください".equals(userId)) {
      errors.put("user_info_id", "予約者を選択してください。");
    }
    // 色が選択されているかをチェック
    String colorId = bean.value("rgb_color");
    if (colorId == null || colorId.isEmpty() || "#87ceeb".equals(colorId)) {
      errors.put("rgb_color", "色を選択してください。");
    }

    if (errors.size() > 0) {
      return false;
    }
    return true;
  }

  /**
   * 検索を行いbeanに格納する。.
   */
  private void searchList() throws AtareSysException {
    WebBean bean = getWebBean();
    HashMap<String, String> errors;

    errors = inputCheck();
    if (errors.size() > 0) {
      bean.setValue("errors", errors);
      return;
    }
    LinkedHashMap<String, String> sortKey = sortKey();
    UserInfoDao dao = new UserInfoDao();
    dao.setSearchFullName(bean.value("list_search_full_name"));
    dao.setSearchFullNameKana(bean.value("list_search_full_name_kana"));

    DaoPageInfo daoPageInfo = new DaoPageInfo();
    if (!Validate.isInteger(bean.value("lineCount"))) {
      bean.setValue("lineCount", "20");
    }
    daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
    SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "UserMenuHome", "lineCount", bean.value("lineCount"));
    if (!Validate.isInteger(bean.value("pageNo"))) {
      daoPageInfo.setPageNo(1);
    } else {
      daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
    }
    ArrayList<UserInfoDao> listData = UserInfoDao.dbSelectList(dao, sortKey, daoPageInfo);
    bean.setValue("lineCount", daoPageInfo.getLineCount());
    bean.setValue("pageNo", daoPageInfo.getPageNo());
    bean.setValue("recordCount", daoPageInfo.getRecordCount());
    bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

    // ルーム情報の取得とセット
    RoomDao roomDao = new RoomDao();
    ArrayList<RoomDao> rooms = roomDao.getAllRooms();

    bean.getWebValues().remove("search_info");
    String search_info = Sup.serialize(bean);
    bean.setValue("search_info", search_info);
    bean.setValue("rooms", rooms);
    bean.setValue("list", listData);
  }

  /**
   * 入力チェックを行う。.
   *
   * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
   */
  private HashMap<String, String> inputCheck()
  {
      WebBean bean = getWebBean();
      HashMap<String, String> errors = bean.getItemErrors();
      if (bean.value("list_search_full_name").length() > 0)
      {
          if (100 < bean.value("list_search_full_name").length())
          {
              errors.put("list_search_full_name", "氏名の入力内容が長すぎます。");
          }
      }
      if (bean.value("list_search_full_name_kana").length() > 0)
      {
          if (100 < bean.value("list_search_full_name_kana").length())
          {
              errors.put("list_search_full_name_kana", "氏名よみの入力内容が長すぎます。");
          }
      }
      return errors;
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
   * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
   *
   * @return なし
   * @throws AtareSysException エラー
   */
  private ReserveDao setWebDaoInputInfo() throws AtareSysException {
    WebBean bean = getWebBean();
    ReserveDao reserveDao = new ReserveDao();
    // reservation_dateを変換: YYYY年MM月DD日 → YYYYMMDD
    String reservationDateStr = bean.value("reservation_date");
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMdd");
    Date reservationDate = null;
    try {
      reservationDate = inputDateFormat.parse(reservationDateStr);
    } catch (ParseException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    } // 入力フォーマットでパース
    String formattedReservationDate = outputDateFormat.format(reservationDate); // 出力フォーマットでフォーマット
    // checkin_time, checkout_timeを変換: HH:MM → HHMM
    String checkinTime = bean.value("checkin_time");
    String checkoutTime = bean.value("checkout_time");
    // 単純な文字列操作で":"を除去
    String formattedCheckinTime = checkinTime.replace(":", "");
    String formattedCheckoutTime = checkoutTime.replace(":", "");
    
    // 複数のuser_info_idがある場合、最初の1つを使用する
    String[] userInfoIds = bean.value("user_info_id").split(",");
    reserveDao.setUserInfoIds(userInfoIds);
    
    reserveDao.setUserInfoId(userInfoIds[0].trim());
    reserveDao.setRoomId(bean.value("room_id"));
    reserveDao.setReservationDate(formattedReservationDate);
    reserveDao.setCheckinTime(formattedCheckinTime);
    reserveDao.setCheckoutTime(formattedCheckoutTime);
    reserveDao.setInputText(bean.value("input_text"));
    reserveDao.setColor(bean.value("rgb_color"));
    reserveDao.setInputRemark(bean.value("input_remark"));
    reserveDao.setInsertUserId(userInfoIds[0].trim());
    reserveDao.setUpdateUserId(bean.value("update_user_id"));
    reserveDao.setUserReserveId(bean.value("user_reserve_id"));
    UserInfoDao userInfoDao = new UserInfoDao();
    String[] userNames = new String[userInfoIds.length];
    for (int i = 0; i < userInfoIds.length; i++) {
      userInfoDao.dbSelect(userInfoIds[i].trim());
      userNames[i] = (userInfoDao.getLastName() != null ? userInfoDao.getLastName() : "") + " " + (userInfoDao.getMiddleName() != null ? userInfoDao.getMiddleName() : "") + " " + (userInfoDao.getFirstName() != null ? userInfoDao.getFirstName() : "");
    }
    reserveDao.setUserNames(userNames);
    userInfoDao.dbSelect(userInfoIds[0].trim());
    reserveDao.setUserName(userInfoDao.getLastName() + " " + userInfoDao.getMiddleName() + " " + userInfoDao.getFirstName());
    userInfoDao.dbSelect(bean.value("update_user_id"));
    reserveDao.setUpdateUserName(userInfoDao.getLastName() + " " + userInfoDao.getMiddleName() + " " + userInfoDao.getFirstName());
    RoomDao roomDao = new RoomDao();
    roomDao.dbSelect(bean.value("room_id"));
    reserveDao.setRoomName(roomDao.getRoomName());

    bean.setValue("input_info", Sup.serialize(reserveDao));
    return reserveDao;
  }
  
/**
 * 予約、紐づけテーブルのデータベースに情報を登録する。
 * @return
 * @throws AtareSysException
 */
  private boolean processReservation() throws AtareSysException {
    WebBean bean = getWebBean();
    ReserveDao reserveDao = setWebDaoInputInfo(); // setWebDaoInputInfoメソッドを呼び出してreserveDaoを設定する
    
    
    // 予約の重複チェック
    try {

      // 予約詳細をデータベースに保存
      reserveDao.dbInsertReserve();
      
      for (String userInfoId : reserveDao.getUserInfoIds()) {
        // UserReserveDaoを作成
        UserReserveDao userReserveDao = new UserReserveDao();
        userReserveDao.setUserReserveId(GetNumber.getNumberChar("userReserve")); // user_reserve_idの作成
        userReserveDao.setUserInfoId(userInfoId.trim());
        userReserveDao.setReserveId(reserveDao.getReserveId());
        // 予約詳細をデータベースに保存
        userReserveDao.dbInsertUserReserve();
      }
      // ファイルデータの存在チェック
      byte[] fileData = (byte[]) bean.object("file");
      if (fileData != null && fileData.length > 0) {
          // ファイル処理の追加
          FileUpload(getRequest(), reserveDao.getUserInfoId(), reserveDao.getReserveId());
      }
      // 予約した部屋情報の取得
      getRequest().setAttribute("reserveDao", reserveDao);

      // 成功メッセージを設定するなどのオプション
      bean.setValue("reservationStatus", "予約が成功しました！");
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  /**
   * ファイルデータを取得し、アップロードした後にデータベースへ登録
   *
   * @return 
   * @throws ServletException 
   * @throws IOException 
   * @throws AtareSysException
   */
  private FileDao FileUpload(HttpServletRequest request, String pUserInfoId , String pReserveId) throws AtareSysException, IOException, ServletException {
    WebBean bean = getWebBean();
    FileDao fileDao = new FileDao();
    UserFileDao userFileDao = new UserFileDao();
    ReserveFileDao reserveFileDao = new ReserveFileDao();
    
    String fileId = UUID.randomUUID().toString().substring(0, 13);
    String filePath = "C:/git/training/kenshuProject/WebContent/upload";
    String userInfoId = bean.value("user_info_id");
    String reserveFileId = GetNumber.getNumberChar("reserveFile"); // reserveFileの作成
    
    // ファイルデータを取得
    FileUtil fileUtil = new FileUtil();
    byte[] fileData = (byte[]) bean.object("file");
    String mimeType = getMimeTypeFromBytes(fileData);
    String fileExtension = getExtensionFromMimeType(mimeType);
    String systemFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
    // 完全なファイルパスの生成
    String fullPath = filePath + "/" + systemFileName + fileExtension;
    if (!fileUtil.outputFile(fullPath, fileData)) {
        return null;
    }
        
    // データベースにファイル情報を登録
    //fileDao.dbFileInsert(fileId, userInfoId, fullPath, fileName, mimeType, systemFileName, uploadUserId, skey);
    userFileDao.dbUserFileInsert(userInfoId, fileId);
    reserveFileDao.dbFileInsert(reserveFileId, pReserveId, fileId);
    return fileDao;
}

/**
 * MIMEタイプを取得するメソッド
 * @param fileData
 * @return
 */
private String getMimeTypeFromBytes(byte[] fileData) {
    if (fileData.length >= 4) {
        String header = new String(fileData, 0, 4);

        // Wordファイルの判定
        if (header.startsWith("\u00D0\u00CF\u0011")) {
            return "application/msword"; // .doc
        }
        // Word 2007以降のファイル
        else if (header.startsWith("PK")) { 
       // ZIP形式のファイルとして解釈
          try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileData))) {
              ZipEntry entry;
              while ((entry = zis.getNextEntry()) != null) {
                  String entryName = entry.getName();

                  // .docxファイルの場合
                  if (entryName.contains("word/")) {
                      return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; // .docx
                  }

                  // .xlsxファイルの場合
                  if (entryName.contains("xl/")) {
                      return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // .xlsx
                  }
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
        else if (header.startsWith("\u00D0\u00CF\u0011")) {
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
   * input_infoフィールドからクラスを取り出し、画面の項目に値を設定する
   *
   * @return なし
   * @throws AtareSysException
   */
  private void setInputInfoDaoWeb() throws AtareSysException {
    WebBean bean = getWebBean();
    ReserveDao reserveDao = (ReserveDao) Sup.deserialize(bean.value("input_info"));
    // 複数のユーザーIDを取得
    if (reserveDao.getUserInfoIds() != null) 
    {
      String userInfoIds = String.join(",", reserveDao.getUserInfoIds()); 
      bean.setValue("user_info_ids", userInfoIds); // 複数のユーザーIDを保存
    }
    // 複数のユーザー名を取得
    if (reserveDao.getUserNames() != null) 
    {
      String userNames = String.join(",", reserveDao.getUserNames());
      bean.setValue("user_names", userNames); // 複数のユーザーIDを保存
    }
    
    bean.setValue("user_info_id", reserveDao.getUserInfoId()); 
    bean.setValue("room_id", reserveDao.getRoomId());
    bean.setValue("checkin_time", reserveDao.getCheckinTime());
    bean.setValue("checkout_time", reserveDao.getCheckoutTime());
    bean.setValue("input_text", reserveDao.getInputText());
    bean.setValue("rgb_color", reserveDao.getColor());
    bean.setValue("input_remark", reserveDao.getInputRemark());
    bean.setValue("user_info_id", reserveDao.getInsertUserId());
    bean.setValue("update_user_id", reserveDao.getUpdateUserId());
    bean.setValue("user_name", reserveDao.getUserName());
    bean.setValue("room_name", reserveDao.getRoomName());
    bean.setValue("user_reserve_id", reserveDao.getUserReserveId());
    // ルーム情報の取得とセット
    RoomDao roomDao = new RoomDao();
    ArrayList<RoomDao> rooms = roomDao.getAllRooms();
    bean.setValue("rooms", rooms);
    // ユーザー情報の取得とセット
    UserInfoDao userInfoDao = new UserInfoDao();
    ArrayList<UserInfoDao> users = userInfoDao.getAllUsers();
    bean.setValue("users", users);
  }

//部屋情報を更新するメソッド
  private void insertRoomInfo() throws AtareSysException {
    WebBean bean = getWebBean();
    RoomDao roomDao = new RoomDao();

    roomDao.setRoomId(bean.value("room_id"));
    roomDao.setInsertUserId(getLoginUserId());
    roomDao.setUpdateUserId(getLoginUserId());

    // `dbInsert`メソッドを呼び出す
    roomDao.dbInsert();
  }
  /**
   * 予約情報を更新するメソッド
   * @throws AtareSysException
   */
  private void updateReseveInfo() throws AtareSysException {
    WebBean bean = getWebBean();
    // main_keyの値を取得
    String reserveId = bean.value("main_key");

    ReserveDao reserveDao = setWebDaoInputInfo();
    reserveDao.setReserveId(reserveId); // reserve_idの設定
    
    // `dbUpdateReserve`メソッドを呼び出す
    reserveDao.dbUpdateReserve();
    
    // 予約した部屋情報の取得
    getRequest().setAttribute("reserveDao", reserveDao);
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
}
