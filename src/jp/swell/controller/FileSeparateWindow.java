package jp.swell.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.dao.FileDownloadsDao;

public class FileSeparateWindow extends ControllerBase {
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
        FileDao fileDao = new FileDao();
        WebBean bean = getWebBean();
        bean.trimAllItem();


        if ("FileDownloads".equals(bean.value("form_name"))) {
          
          if ("next".equals(bean.value("action_cmd")))
          {
              bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
              searchFilesDownloadsList();
          }
          else if ("jump".equals(bean.value("action_cmd")))
          {
              	searchFilesDownloadsList();
          }
          else if ("prior".equals(bean.value("action_cmd")))
          {
              bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
              searchFilesDownloadsList();
          } else {
           

           bean.setValue("pageNo", "1");
           searchFilesDownloadsList();
          }

          forward("FileDownloads.jsp");
          return;
   
        } else if ("FilePreview".equals(bean.value("form_name"))) {
       	
        	 fileDao.dbSelect(bean.value("main_key"));
          String filePath = fileDao.getFilePath();
          bean.setValue( "filePathData", fileAsDataUrl(filePath) );
          String baseFileName = fileDao.getFileName();
        
        

          // ユーザーエージェントを取得
          String ua = this.getRequest().getHeader("user-agent");
          String attachmentFileName = ""; // 添付ファイル名を初期化
          // ブラウザによってファイル名の設定を分岐
          if (ua.indexOf("MSIE") == -1) {
            // Firefox, Opera 11など
              try {
												   	attachmentFileName = String.format(Locale.JAPAN, "inline; filename*=utf-8'jp'%s",
													        URLEncoder.encode(baseFileName, "utf-8"));
												  } catch (UnsupportedEncodingException e) {
												   	e.printStackTrace();
												  }
           } else {
              // IE7, 8, 9用の処理
              try {
													    attachmentFileName = String.format(Locale.JAPAN, "inline; filename=\"%s\"",
													        new String(baseFileName.getBytes("MS932"), "ISO8859_1"));
												  } catch (UnsupportedEncodingException e) {
												    	// TODO 自動生成された catch ブロック
												    	e.printStackTrace();
												  }
          }

          // レスポンスの文字エンコーディングを設定
          this.getResponse().setCharacterEncoding("UTF-8");

          this.getResponse().setHeader("pragma", "no-store");
          this.getResponse().setHeader("Cache-Control", "no-store");

          // 添付ファイル名をレスポンスヘッダーに設定
          this.getResponse().setHeader("Content-Disposition", attachmentFileName);
   
          forward("FilePreview.jsp");
      
          return;

        }
    }

    /**
     * ファイルダウンロード履歴一覧の検索を行いbeanに格納する。.
     */
    private void searchFilesDownloadsList() throws AtareSysException
    {
        WebBean bean = getWebBean();

        LinkedHashMap<String, String> sortKey = new LinkedHashMap<>();
        sortKey.put("downloads_date", "asc");
        

        FileDownloadsDao dao = new FileDownloadsDao();
        dao.setFileId(bean.value("main_key"));
        
        
        DaoPageInfo daoPageInfo = new DaoPageInfo();
        if (!Validate.isInteger(bean.value("lineCount")))
        {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "FileDownloadsList", "lineCount", bean.value("lineCount"));
        if (!Validate.isInteger(bean.value("pageNo")))
        {
            daoPageInfo.setPageNo(1);
        }
        else
        {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }
        ArrayList<FileDownloadsDao> listData = FileDownloadsDao.dbSearchFilesDownloadsList(dao, sortKey, daoPageInfo);
        
        
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
  

  /**
   * ページ番号を加算減算する
   *
   * @param $page_no
   *        現在のページ番号
   * @param $add
   *        加算減算する値
   * @return 結果のページを返す
   */
  private String calcPageNo(String pageNo, int add)
  {
      int ret;
      if (null == pageNo)
      {
          pageNo = "1";
      }
      else if ("".equals(pageNo))
      {
          pageNo = "1";
      }
      else if (!Validate.isInteger(pageNo))
      {
          pageNo = "1";
      }
      ret = Integer.parseInt(pageNo);
      ret += add;
      return String.valueOf(ret);
  }
}

