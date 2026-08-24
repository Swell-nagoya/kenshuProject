package jp.swell.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.swell.common.ControllerBase;
import jp.swell.dao.RoomDao;

public class ApiRoomDetailController extends ControllerBase {

    @Override
    public void doInit() {
        setLoginNeeds(false); // APIの動作確認を簡単にするためfalseに設定
        setHttpNeeds(false);
        setHttpsNeeds(false);
        setUsecache(false);
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        try {
            String method = this.getRequest().getMethod();
            this.getResponse().setContentType("application/json; charset=UTF-8");
            PrintWriter out = this.getResponse().getWriter();
            Gson gson = new Gson();
            
            if ("POST".equalsIgnoreCase(method)) {
                // 新規登録処理
                BufferedReader reader = this.getRequest().getReader();
                RoomDao inputDao = gson.fromJson(reader, RoomDao.class);
                
                // 簡易バリデーション
                if (inputDao == null || inputDao.getRoomName() == null || inputDao.getRoomName().trim().isEmpty()) {
                    this.getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    HashMap<String, String> error = new HashMap<String, String>();
                    error.put("message", "部屋名が入力されていません。");
                    out.print(gson.toJson(error));
                } else {
                    DbBase.dbBeginTran();
                    try {
                        // DB登録
                        inputDao.setInsertUserId(getLoginUserId());
                        inputDao.dbInsert();
                        DbBase.dbCommitTran();
                        
                        this.getResponse().setStatus(HttpServletResponse.SC_CREATED);
                        HashMap<String, String> success = new HashMap<String, String>();
                        success.put("message", "登録成功");
                        out.print(gson.toJson(success));
                    } catch (Exception e) {
                        DbBase.dbRollbackTran();
                        throw e;
                    }
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                // 削除処理
                String roomId = this.getRequest().getParameter("room_id");
                
                if (roomId == null || roomId.trim().isEmpty()) {
                    this.getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    HashMap<String, String> error = new HashMap<String, String>();
                    error.put("message", "削除対象のIDが指定されていません。");
                    out.print(gson.toJson(error));
                } else {
                    DbBase.dbBeginTran();
                    try {
                        RoomDao dao = new RoomDao();
                        dao.dbDelete(roomId);
                        DbBase.dbCommitTran();
                        
                        this.getResponse().setStatus(HttpServletResponse.SC_OK);
                        HashMap<String, String> success = new HashMap<String, String>();
                        success.put("message", "削除成功");
                        out.print(gson.toJson(success));
                    } catch (Exception e) {
                        DbBase.dbRollbackTran();
                        throw e;
                    }
                }
            } else {
                this.getResponse().setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                HashMap<String, String> error = new HashMap<String, String>();
                error.put("message", "許可されていないメソッドです。");
                out.print(gson.toJson(error));
            }
            
            out.flush();
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            } catch(Exception ex) {
                // Ignore
            }
        }
    }
}
