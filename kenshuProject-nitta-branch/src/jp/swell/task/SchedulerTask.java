package jp.swell.task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import jp.patasys.common.AtareSysException;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;

/**
 * SchedulerTaskクラスは、Scheduleクラスに基づいて定期的にタスクを実行するためのクラスです。
 * タイマーを使用して指定された時間にタスクを実行します。
 */
public class SchedulerTask extends TimerTask {
    
  // タイマーオブジェクト
  private Timer timer = null;

  static public void main(String argv[])
  {
    SchedulerTask obj = new SchedulerTask();
    obj.startTimer();
  }

  /**
   * コンストラクタ。指定されたScheduleオブジェクトを使用してSchedulerTaskを初期化します。
   *
   * @param schedule Scheduleオブジェクト
   */
  SchedulerTask() {
    timer = new Timer(); // 新しいタイマーを作成
  }

  /**
   * タイマーを開始します。最初の実行時刻を設定し、その後24時間ごとにタスクを実行します。
   */
  public void startTimer() {
      // 現在の時刻を取得
      Calendar calendar = Calendar.getInstance();
      // 実行時間を設定
      calendar.set(Calendar.HOUR_OF_DAY, 10);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      // 最初の実行時刻を取得
      Date firstExecutionTime = calendar.getTime();
      // 現在時刻が既に実行時間を過ぎている場合は、翌日に設定
      if (firstExecutionTime.before(new Date())) {
          calendar.add(Calendar.DAY_OF_MONTH, 1);
          firstExecutionTime = calendar.getTime();
      }

      // タイマーで指定された時刻から24時間ごとにタスクを実行
      timer.scheduleAtFixedRate(this, firstExecutionTime, 24 * 60 * 60 * 1000L);
  }

  /**
   * タイマータスクが実行されるメソッド。ここではScheduleクラスのdeleteメソッドを呼び出します。
   */
  @Override
  public void run() {
      try
      {
        // スケジュールを削除する処理を呼び出す
        dataManagement();
      } 
      catch (AtareSysException e) 
      {
        e.printStackTrace();
      }
  }
  /**
   * 削除の場合
   * @throws AtareSysException
   */
  private void dataManagement() throws AtareSysException
  {
    // ユーザー情報の取得とセット
    UserInfoDao userInfoDao = new UserInfoDao();
    ArrayList<UserInfoDao> users = userInfoDao.getAllUsers();
    // ユーザセレクトの一覧を取得とセット
    ScheduleDao scheduleDao = new ScheduleDao();
    ArrayList<ScheduleDao> scheduleDaos = scheduleDao.getAllUsers();
    
    // 各ScheduleDaoオブジェクトからmainUserIdとlinkUserIdを取得
    for (ScheduleDao schedule : scheduleDaos) {
      String mainUserId = schedule.getMainUserId();
      String linkUserId = schedule.getLinkUserId();
      
      // カウントを毎回リセット
      int mainSize = 0;
      int linkSize = 0;

      for (UserInfoDao user : users) {
        if (user.getUserInfoId().equals(mainUserId)) {
          mainSize++;
        }
        if (user.getUserInfoId().equals(linkUserId)) {
          linkSize++;
        }
      }  

      if (mainSize == 0) {
        scheduleDao.dbDelete_1(mainUserId);
      }
      if (linkSize == 0) { // "else if" ではなく "if" にすることで両方チェック
        scheduleDao.dbDelete_2(linkUserId);
      }
    }
    System.out.println("削除しました");
  }
  /**
   * ソート順番を求める
   *
   * @return ソート順を格納した配列を返す
   */
  private LinkedHashMap<String, String> sortKey() {
    return new LinkedHashMap<>();
  }
}

