package jp.swell.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

/**
 * ReserveDao#dbWhere() (検索条件からWHERE句を組み立てるロジック)のテストケース。.
 * 部屋一覧の「予約確認」ボタンから room_id で絞り込む機能のために追加した条件を検証する。
 * DB接続は行わず、SQL文字列の組み立てのみを検証する。
 */
class ReserveDaoTest
{
    @Test
    void dbWhere_roomIdを指定すると完全一致条件が含まれる() throws Exception
    {
        ReserveDao dao = new ReserveDao();
        dao.setRoomId("5");

        String where = invokeDbWhere(dao);

        assertTrue(where.contains("reserve.room_id = '5'"), "room_idの絞り込み条件が必要: [" + where + "]");
    }

    @Test
    void dbWhere_roomId未指定なら条件が含まれない() throws Exception
    {
        ReserveDao dao = new ReserveDao();

        String where = invokeDbWhere(dao);

        assertFalse(where.contains("reserve.room_id"));
    }

    private static String invokeDbWhere(ReserveDao dao) throws Exception
    {
        Method method = ReserveDao.class.getDeclaredMethod("dbWhere");
        method.setAccessible(true);
        return (String) method.invoke(dao);
    }
}
