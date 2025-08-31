import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class Player {
    public String name;
    public int num;
    Player(String name) {
        this.name = "Player" + name;
        this.num = 0;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void addNum(int num) {
        this.num += num;
    }
    public int getNum() {
        return this.num;
    }
}

public class SugorokuAnswer {
    List<Player> playerList = new ArrayList<Player>();
    int GOAL;
    static int count = 0;
    public static void main(String[] args) {
        try {
            System.out.printf("参加するプレイヤーの人数してください:");
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String input = br.readLine();
            int playerNum = Integer.parseInt(input);
            SugorokuAnswer sugo = new SugorokuAnswer();
            for (int i=0; i<playerNum; i++) {
                sugo.playerList.add(new Player(String.valueOf(i+1)));
            }
            System.out.printf("ゴールまでのマスの数:");
            input = br.readLine();
            sugo.GOAL = Integer.parseInt(input);
            System.out.println("");
            int isEnd = 0;
            while(isEnd == 0 && !input.equals("q")) {
                isEnd = sugo.sugoroku();
                System.out.printf("終了する場合は q を入力してください:");
                input = br.readLine();
                System.out.println("");
                count ++;
            }
            for(int i=0; i<sugo.playerList.size()-1; i++) {
                for(int j=0; j<sugo.playerList.size()-1; j++) {
                    if(sugo.playerList.get(j).getNum() < sugo.playerList.get(j+1).getNum()) {
                        Player temp = new Player("temp");
                        temp = sugo.playerList.get(j);
                        sugo.playerList.set(j,sugo.playerList.get(j+1));
                        sugo.playerList.set(j+1,temp);
                    }
                }
            }
            System.out.println("順位発表");
            for(int i=0;i<playerNum; i++) {
                System.out.println( (i+1) + "位:" + sugo.playerList.get(i).getName() + "," + sugo.playerList.get(i).getNum() + "マス");
            }
        } catch (Exception e) {
            System.out.println("例外："+ e);
        }
    }

    public int sugoroku(){
        int finish = 0;
        for(int i=0; i<playerList.size(); i++) {
            playerList.get(i).addNum(saikoro());
            System.out.println(playerList.get(i).getName() + " は " + playerList.get(i).getNum() + " まで進みました ");
            if(playerList.get(i).getNum() >= GOAL) {
                finish = 1;
            }
        }
        return finish;
    }

    public int saikoro() {
        return (count % 6);
    }
}
