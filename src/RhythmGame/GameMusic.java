package RhythmGame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class GameMusic extends Thread{
	private Player player;	//jlayer libraries중 하나
	private boolean isLoop;	//현재 곡이 무한반복인지 일정 횟수로 재생되는지 판별
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	
	public GameMusic(String name, boolean isLoop) {
		try {
			this.isLoop = isLoop;
			file = new File(BasicBeatGame.class.getResource("../GameSounds/" + name).toURI());	//해당 음악파일을 가져옴
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);	//해당 음악파일을 버퍼에 담아서 읽어올 수 있도록 함
			player = new Player(bis);	//해당 음악파일을 담을 수 있도록 함
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//현재 실행중인 음악이 얼마나(어느 위치까지) 실행되었는지 알려주는 메소드
	public int getTime() {
		if(player == null)
			return 0;
		return player.getPosition();	//0.001초단위까지 알려줌
	}
	
	//해당 곡이 중간에 안정적으로 종료될 수 있도록 하는 메소드
	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();	//현재 thread를 하나 중지시킴
	}
	
	@Override
	public void run() {
		try {
			do {
				player.play();	//곡을 재생하는 메소드
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);	//해당 음악파일을 버퍼에 담아서 읽어올 수 있도록 함
				player = new Player(bis);	//해당 음악파일을 담을 수 있도록 함
			}while(isLoop);	//isLoop=true인 경우 해당 곡은 무한반복하여 실행
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
