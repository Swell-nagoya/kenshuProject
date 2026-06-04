package jp.swell.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 一時ファイルを定期的に監視し、古いファイルを削除するクラス
 * 
 * @author Shusei Tanaka
 */

@Component
public class TempFileCleaner {

	@Scheduled(fixedRate = 1800000)//30分おきに実行する
    public void deleteOldTempFiles() {

        Path tempFile = Paths.get("C:\\kenshuProject\\WebContent\\upload\\temp");//監視先のパス

        try (Stream<Path> files = Files.list(tempFile)) {//中のファイルを取得

            files.forEach(path -> {//ファイル全てに対して実行

                try {

                    FileTime lastModified =
                        Files.getLastModifiedTime(path);//更新時間を取得

                    long diff =
                        System.currentTimeMillis()
                        - lastModified.toMillis();//現在時間から更新時間を引く

                    if (diff > 30 * 60 * 1000) {//30分差の場合

                        Files.deleteIfExists(path);//ファイルを削除

                        System.out.println("削除:" + path);//ログに記載

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
