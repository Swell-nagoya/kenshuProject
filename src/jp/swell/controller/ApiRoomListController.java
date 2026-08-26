package jp.swell.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.swell.common.ControllerBase;
import jp.swell.dao.RoomDao;
import jp.swell.dao.RoomDao.RoomApiDto;

public class ApiRoomListController extends ControllerBase {

    @Override
    public void doInit() {
        setLoginNeeds(true); 
        setHttpNeeds(false);
        setHttpsNeeds(false);
        setUsecache(false);
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        try {
            // RoomDao を使って一覧取得
            RoomDao dao = new RoomDao();
            dao.setRoomName("%%"); // 全件検索

            DaoPageInfo daoPageInfo = new DaoPageInfo();
            daoPageInfo.setLineCount(100); // 100件まで取得
            daoPageInfo.setPageNo(1);

            LinkedHashMap<String, String> sortKey = new LinkedHashMap<String, String>();
            sortKey.put("room_id", "asc"); // IDで昇順ソート

            ArrayList<RoomDao> listData = RoomDao.dbSelectList(dao, sortKey, daoPageInfo);
            
            int cnt = listData.size();

            ArrayList<RoomApiDto> apiDtoList = new ArrayList<>();
            
            for(int i = 0; i < cnt; i++)
            {

            	  RoomApiDto dto = new RoomApiDto(listData.get(i).getRoomId(),listData.get(i).getRoomName());
            	  apiDtoList.add(dto);
            }
            
            // Gson を用いてリストをJSON文字列に変換
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(apiDtoList);

            // レスポンスヘッダーの設定
            this.getResponse().setContentType("application/json; charset=UTF-8");
            
            // JSONの出力
            PrintWriter out = this.getResponse().getWriter();
            out.print(jsonResponse);
            out.flush();
            out.close();
            

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.getResponse().sendError(500, "Internal Server Error");
            } catch(Exception ex) {
                // Ignore
            }
        }
    }
}
