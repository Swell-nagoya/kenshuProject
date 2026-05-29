package jp.swell.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
/**
 * 10分ごとにrepasswordテーブルの古い情報を削除していくクラス
 */
public class TokenDelete {
    // クリーンアップの間隔（5分）
    private static final long CLEANUP_INTERVAL_MINUTES = 5;
    //定期的なタスクをスケジュール
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    /**
     * クリーンアップタスクを開始するメソッド。
     * 定期的にトークンを削除する処理をスケジュールします。
     */
    public void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupTokens();
            } catch (AtareSysException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }, 0, CLEANUP_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }
    
    /**
     * トークンのクリーンアップを実行するメソッド。
     * 有効期限が切れたトークンをデータベースから削除します。
     * @throws AtareSysException クリーンアップ処理中に発生する可能性のあるカスタム例外
     */
    private void cleanupTokens() throws AtareSysException {
        
        // 有効期限が切れたトークンを削除するSQLクエリ
        String sql = "DELETE FROM repassword WHERE expires_at < NOW()";

        try (Connection conn = DbBase.getDbConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // SQLクエリを実行し、削除された行数を取得
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("Expired tokens cleaned up. Rows deleted: " + rowsDeleted);

        } catch (SQLException e) {
            e.printStackTrace(); // またはログに記録
        }
    }

    /**
     * クリーンアップタスクを停止するメソッド。
     * スケジューラーをシャットダウンし、タスクが完了するのを待ちます。
     */
    public void stopCleanup() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
