package jp.swell.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;

  public class FileDownloadsDao implements Serializable {


   /**
    * file_downloads_id
    */
   private String fileDownloadsId = "";

   /**
    * 
    * @return fileDownloadsId
    */
   public String getFileDownloadsId() {
       return fileDownloadsId;
   }

   /**
    * 
    * @param fileDownloadsId セットする fileDownloadsId
    */
   public void setFileDownloadsId(String fileDownloadsId) {
       this.fileDownloadsId = fileDownloadsId;
   }
   
   
   /**
    * user_info_id
    */
   private String userInfoId = "";

   /**
    * 
    * @return userInfoId
    */
   public String getUserInfoId() {
       return userInfoId;
   }

   /**
    * 
    * @param userInfoId セットする userInfoId
    */
   public void setUserInfoId(String userInfoId) {
       this.userInfoId = userInfoId;
   }

   /**
     * file_id
    */
   private String fileId = "";

   /**
    * 
    * @return fileId
    */
   public String getFileId() {
       return fileId;
   }

   /**
    * 
    * @param fileId セットする fileId
    */
   public void setFileId(String fileId) {
       this.fileId = fileId;
   }



   /**
    * downloadsDate ダウンロード日時
    */
   private String downloadsDate = "";

   /**
    * 
    * @return downloadsDate
    */
   public String getDownloadsDate() {
       return downloadsDate;
   }

   /**
    * 
    * @param downloadsDate セットする uploadDate
    */
   public void setDownloadsDate(String downloadsDate) {
       this.downloadsDate = downloadsDate;
   }
   
    /**
     * files_downloads ダウンロード履歴テーブルにデータを挿入する
     * @param puserInfoId 
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbFileDownloadsInsert(String downloadsId, String puserInfoId, String pfileId)
     throws AtareSysException {
     setUserInfoId(puserInfoId);

     String createTableSql = "CREATE TABLE IF NOT EXISTS files_downloads ("
         + " file_downloads_id VARCHAR(13) NOT NULL,"
         + " user_info_id VARCHAR(13) NOT NULL,"
         + " file_id VARCHAR(13) NOT NULL,"
         + " downloads_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
         + " PRIMARY KEY (file_downloads_id)"
         + " );";

     DbBase.dbExec(createTableSql);


     String sql = "insert into files_downloads ("
         + " file_downloads_id"
         + ",user_info_id"
         + ",file_id"
         + ",downloads_date"
         + " ) values ( "
         + DbO.chara(downloadsId)
         + "," + DbO.chara(puserInfoId)
         + "," + DbO.chara(pfileId)
         + "," + "NOW()"
         + " );";
    int ret = DbBase.dbExec(sql);

    if (ret != 1)
       throw new AtareSysException("dbInsert number or record exception.");
       return true;
    }
    


    /**
     * files ファイル情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException {
        StringBuffer where = new StringBuffer(1024);
        
        if (getFileId().length() > 0) {
            where.append(where.length() > 0 ? " AND " : "");
            where.append("files_downloads.file_id = " + DbS.chara(getFileId()));
        }

        if(where.length()>0)
        {
            return "where " + where.toString();
        }
        return "";
    }
    
    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String, String> fieldsArray = new HashMap<String, String>();

    /**
     * コンストラクタ。
     */
    public FileDownloadsDao() {
        fieldsArray.put("file_downloads_id", "files_downloads.file_downloads_id");
        fieldsArray.put("user_info_id", "files_downloads.user_info_id");
        fieldsArray.put("file_id", "files_downloads.file_id");
        fieldsArray.put("downloads_date", "files_downloads.downloads_date");
    }


    /**
     * データベースからファイル名を取得するメソッド
     * @return UserMenuに返す
     */
    static public ArrayList<FileDownloadsDao> dbSearchFilesDownloadsList(FileDownloadsDao myclass,LinkedHashMap<String,String> sortKey,DaoPageInfo daoPageInfo) throws AtareSysException
    {
        ArrayList<FileDownloadsDao> array = new ArrayList<FileDownloadsDao>();

        /* レコードの総件数を求める */
        String sql =  "select count(distinct files_downloads.user_info_id) as count"
        + " from files_downloads "
        + myclass.dbWhere();
        
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if(len == 0)   return array;
        if(-1==daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double)len/(double)(daoPageInfo.getLineCount())));
        if(daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if(daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start  =   (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql =  "select "
            + "max( files_downloads.file_downloads_id ) as files_downloads___file_downloads_id"
            + ",files_downloads.user_info_id as files_downloads___user_info_id"
            + ",files_downloads.file_id as files_downloads___file_id"
            + ",max( files_downloads.downloads_date ) as files_downloads___insert_date"
            + " from files_downloads ";
        String where = myclass.dbWhere();
        String order = myclass.dbSearchFilesDownloadsOrder(sortKey);
        sql += where;

        sql += " group by "; 
        sql += " files_downloads.user_info_id,";
        sql += " files_downloads.file_id ";

        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";
        rs  =  DbBase.dbSelect(sql);
        
        
        int cnt = rs.size();
        
        if(cnt < 1)    return array;
        FileDownloadsDao dao  = new FileDownloadsDao();
        for(int i=0;i<cnt;i++)
        {
            dao  = new FileDownloadsDao();
            map = rs.get(i);
            dao.setFileDownloadHistoryDaoForJoin(map,dao);
            

            array.add(dao);
        }
        return array;
    }
    
    /**
     * files ファイル情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbSearchFilesDownloadsOrder(LinkedHashMap<String, String> sortKey) {
        String str = "";
        if (sortKey == null)
            return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = i.next();
            if (null == fieldsArray.get(key))
                continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] =  fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++) {
                if (j != 0)
                    str += ",";
                str += "max(" + ss[j] + ")" + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str );
        return str;
    }

    /**
     *  FileDao にfiles_downloads ファイルテーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  FileDaoこのテーブルのインスタンス
     */
    public void setFileDownloadHistoryDaoForJoin(HashMap<String, String> map, FileDownloadsDao dao) throws AtareSysException {
        dao.setFileDownloadsId(DbI.chara(map.get("file_downloads_id") != null ? map.get("file_downloads_id") : ""));
        dao.setUserInfoId(DbI.chara(map.get("user_info_id") != null ? map.get("user_info_id") : ""));
        dao.setFileId(DbI.chara(map.get("file_id") != null ? map.get("file_id") : ""));
        dao.setDownloadsDate(DbI.chara(map.get("downloads_date") != null ? map.get("downloads_date") : ""));
    }
    
}
