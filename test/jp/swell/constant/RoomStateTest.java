package jp.swell.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * RoomState(部屋の利用ステータス定数)のテストケース。.
 */
class RoomStateTest
{
    @Test
    void getStateName_1は利用可()
    {
        assertEquals("利用可", new RoomState().getStateName("1"));
    }

    @Test
    void getStateName_2は使用中()
    {
        assertEquals("使用中", new RoomState().getStateName("2"));
    }

    @Test
    void getStateName_3はメンテナンス中()
    {
        assertEquals("メンテナンス中", new RoomState().getStateName("3"));
    }

    @Test
    void getStateName_9は削除()
    {
        assertEquals("削除", new RoomState().getStateName("9"));
    }

    @Test
    void getStateName_未定義の値は空文字()
    {
        assertEquals("", new RoomState().getStateName("999"));
    }
}
