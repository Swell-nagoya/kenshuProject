package jp.swell.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

/**
 * RoomDao#dbWhere() (検索条件からWHERE句を組み立てるロジック)のテストケース。.
 * DB接続は行わず、SQL文字列の組み立てのみを検証する。
 */
class RoomDaoTest
{
    @Test
    void dbWhere_デフォルトで削除済みとステータス削除の部屋を除外する() throws Exception
    {
        RoomDao dao = new RoomDao();

        String where = invokeDbWhere(dao);

        assertTrue(where.contains("room.is_deleted = false"));
        assertTrue(where.contains("room.status <> '9'"), "ステータス削除(9)の部屋を除外する条件が必要: [" + where + "]");
    }

    @Test
    void dbWhere_部屋名検索条件が含まれる() throws Exception
    {
        RoomDao dao = new RoomDao();
        dao.setRoomName("会議室");

        String where = invokeDbWhere(dao);

        assertTrue(where.contains("room.room_name LIKE"));
        assertTrue(where.contains("%会議室%"));
    }

    private static String invokeDbWhere(RoomDao dao) throws Exception
    {
        Method method = RoomDao.class.getDeclaredMethod("dbWhere");
        method.setAccessible(true);
        return (String) method.invoke(dao);
    }
}
