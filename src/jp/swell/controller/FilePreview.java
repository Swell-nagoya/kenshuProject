package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;

public class FilePreview extends ControllerBase {
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
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
  @Override
  public void doActionProcess() throws AtareSysException {
    WebBean bean = getWebBean();

    if ("FilePreview".equals(bean.value("form_name"))) {
      bean.trimAllItem();
      
      
     if ("go_next".equals(bean.value("action_cmd"))) {
       if ("preview".equals(bean.value("request_cmd"))) {
       	
       	FileDao dao = new FileDao(); 

        dao.dbSelect(bean.value("main_key"));
        String filePath = dao.getFilePath();
        bean.setValue( "filePathData", fileAsDataUrl(filePath) );
        
        
         forward("FilePreview.jsp");
       }
      
        return;
      }

    }
  }


  /**
   * DBに登録したfileのデータURLスキーム形式を取得.
   *
   * @return PDFまたは画像のデータURLスキーム形式
   */
  public String fileAsDataUrl(String filePath) {

   // ファイルパスが空、またはファイルが存在しない場合のチェック
   if (filePath == null || filePath.isEmpty()) {
      return "";
   }

   java.io.File targetFile = new java.io.File(filePath);
   if (!targetFile.exists()) {
      return "";
   }

   try {
       // ファイルのMIMEタイプを自動判定
       String contentType = java.nio.file.Files.probeContentType(targetFile.toPath());
       if (contentType == null) {
          // 拡張子から簡易判定（probeContentTypeがnullを返した場合のフォールバック）
          String lowerPath = filePath.toLowerCase();
        
          if (lowerPath.endsWith(".pdf")) {
              contentType = "application/pdf";
          } else if (lowerPath.endsWith(".png")) {
              contentType = "image/png";
          } else if (lowerPath.endsWith(".gif")) {
              contentType = "image/gif";
          } else if (lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")) {
              contentType = "image/jpeg";
          } else {
          	   return "";
          }
       }

       // ファイルをbyte配列に変換する
       byte[] bytes = java.nio.file.Files.readAllBytes(targetFile.toPath());

       // byte配列をbase64に変換する
       String base64 = java.util.Base64.getEncoder().encodeToString(bytes);

       // データURLスキーム形式
       return String.format("data:%s;base64,%s", contentType, base64);

     } catch (java.io.IOException e) {
         e.printStackTrace();
         return "";
     }
  }
}
