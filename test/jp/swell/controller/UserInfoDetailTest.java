package jp.swell.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

/**
 * UserInfoDetail に今回追加したユーザー一括編集・一括削除機能のうち、
 * WebBean/DB に依存しない純粋なロジック(isBulkPagingAction, isBulkPreviewAction)のテストケース。.
 * どちらも private メソッドのためリフレクション経由で呼び出す。
 */
class UserInfoDetailTest
{
    // ---- isBulkPagingAction (UserInfoDetail_1/2 画面でのページング操作判定) ----

    @Test
    void isBulkPagingAction_bulk_nextはページング操作()
    {
        assertTrue(invokeIsBulkPagingAction("bulk_next"));
    }

    @Test
    void isBulkPagingAction_bulk_priorはページング操作()
    {
        assertTrue(invokeIsBulkPagingAction("bulk_prior"));
    }

    @Test
    void isBulkPagingAction_bulk_confirmはページング操作()
    {
        assertTrue(invokeIsBulkPagingAction("bulk_confirm"));
    }

    @Test
    void isBulkPagingAction_go_nextはページング操作()
    {
        assertTrue(invokeIsBulkPagingAction("go_next"));
    }

    @Test
    void isBulkPagingAction_無関係なコマンドはページング操作ではない()
    {
        assertFalse(invokeIsBulkPagingAction("return"));
    }

    @Test
    void isBulkPagingAction_nullはページング操作ではない()
    {
        assertFalse(invokeIsBulkPagingAction(null));
    }

    // ---- isBulkPreviewAction (UserInfoDetail_3 画面でのプレビュー操作判定) ----

    @Test
    void isBulkPreviewAction_bulk_preview_nextはプレビュー操作()
    {
        assertTrue(invokeIsBulkPreviewAction("bulk_preview_next"));
    }

    @Test
    void isBulkPreviewAction_bulk_preview_priorはプレビュー操作()
    {
        assertTrue(invokeIsBulkPreviewAction("bulk_preview_prior"));
    }

    @Test
    void isBulkPreviewAction_go_nextはプレビュー操作ではない()
    {
        // go_next は確定操作であり、プレビュー(1名ずつ閲覧)操作とは区別される
        assertFalse(invokeIsBulkPreviewAction("go_next"));
    }

    @Test
    void isBulkPreviewAction_nullはプレビュー操作ではない()
    {
        assertFalse(invokeIsBulkPreviewAction(null));
    }

    // ---- リフレクションヘルパー ----

    private static boolean invokeIsBulkPagingAction(String actionCmd)
    {
        return invokeBooleanMethod("isBulkPagingAction", actionCmd);
    }

    private static boolean invokeIsBulkPreviewAction(String actionCmd)
    {
        return invokeBooleanMethod("isBulkPreviewAction", actionCmd);
    }

    private static boolean invokeBooleanMethod(String methodName, String actionCmd)
    {
        try
        {
            UserInfoDetail target = new UserInfoDetail();
            Method method = UserInfoDetail.class.getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(target, actionCmd);
        }
        catch (ReflectiveOperationException e)
        {
            fail(methodName + " の呼び出しに失敗しました: " + e);
            return false;
        }
    }
}
