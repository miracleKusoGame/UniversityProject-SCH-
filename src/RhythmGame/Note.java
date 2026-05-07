package RhythmGame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import RhythmGame.BasicBeatGame;

//각각의 노트마다 떨어지는 역할을 수행해야 하므로 thread상속
public class Note extends Thread {
	private Image noteImage;	//노트 이미지 설정
	private int x;
	private int y = 970 - (1000/BasicBeatGame.SLEEP_TIME * BasicBeatGame.NOTE_SPEED)*BasicBeatGame.REACH_JUDGEBAR_TIME;	//노트가 떨어져서 1초 뒤에 판정선에 정확히 도달하기 위한 초기 y값
	private String noteKeyName;	//해당 노트의 영역에 맞는 키
	private String noteType = "short";	//해당 노트의 종류(초기값을 설정 안하면 null이 디폴트 값으로 들어가 nullPointerException발생)
	private long holding_time;	//롱노트의 경우 holding해야하는 시간
	private boolean proceeded = true;	//현재 노트의 진행(움직임) 여부
	
	public Note(String noteKeyName) {
		if(noteKeyName.equals("D")) {
			x = 225;
		}
		else if(noteKeyName.equals("F")) {
			x = 520;
		}
		else if(noteKeyName.equals("SPACE")) {
			x = 815;
		}
		else if(noteKeyName.equals("J")) {
			x = 1110;
		}
		else if(noteKeyName.equals("K")) {
			x = 1405;
		}
		
		this.setNoteKeyName(noteKeyName);
	}
	
	//노트 하나를 그려주는 메소드
	public void screenDraw(Graphics2D g) {
		noteImage = new ImageIcon(BasicBeatGame.class.getResource("../images/hitnote.png")).getImage();	//short노트 이미지 설정
		if(noteType.equals("long_start") || noteType.equals("long_end"))	noteImage = new ImageIcon(BasicBeatGame.class.getResource("../images/longhitnote.png")).getImage();	//long노트 이미지 설정
		g.drawImage(noteImage, x, y, null);	//각 노트의 이미지를 그려줌
	}
	
	//노트가 떨어지도록 하는 메소드
	public void drop() {
		y += BasicBeatGame.NOTE_SPEED;	//y좌표가 NOTE_SPEED만큼 증가해서 떨어짐을 나타냄
		if(y > 980) {
			System.out.println("Miss");
			close();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {//노트는 BasicBeatGame.SLEEP_TIME/1000초마다 BasicBeatGame.NOTE_SPEED만큼 떨어진다.
				drop();	//계속해서 노트를 떨어뜨림
				if(proceeded)	//현재 노트가 움직이는 중이면
					Thread.sleep(BasicBeatGame.SLEEP_TIME);	//노트를 떨어뜨리고 나서 SLEEP_TIME만큼 sleep후 다시 노트를 떨어뜨림(단, sleep()은 0.01초를 기준으로 하므로 1초에 100번 실행되어 y는 SLEEP_TIME*100pixel이동)
				else {	//현재 노트가 멈춘 상태라면
					interrupt();	//thread정지를 위한 interrupt. 더이상 해당 노트 인스턴스는 움직이지 않음
					break;
				}
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//현재 노트를 멈추는 메소드
	public void close() {
		proceeded = false;
	}
	
	public boolean isProceeded() {
		return proceeded;
	}
	
	//노트press 판정 계산 메소드
	public String pressJudge() {
		//judgement_line_y=980, image_vertical_size/2=96;
		final int PERFECT_JUDGE = 975-96;	//perfect판정라인의 y값
		final int JUDGE_LEVEL = 10;	//각 판정 간격
		
		if(y >= PERFECT_JUDGE+JUDGE_LEVEL*4) {
			System.out.println("Miss");
			close();
			return "Miss";
		}
		else if(y >= PERFECT_JUDGE+JUDGE_LEVEL*3) {
			System.out.println("Bad");
			close();
			return "Bad";
		}
		else if(y >= PERFECT_JUDGE+JUDGE_LEVEL*2) {
			System.out.println("Good");
			close();
			return "Good";
		}
		else if(y >= PERFECT_JUDGE+JUDGE_LEVEL) {
			System.out.println("Great");
			close();
			return "Bad";
		}
		else if(y >= PERFECT_JUDGE-JUDGE_LEVEL) {
			System.out.println("Perfect");
			close();
			return "Perfect";
		}
		else if(y >= PERFECT_JUDGE-JUDGE_LEVEL*2){
			System.out.println("Great");
			close();
			return "Great";
		}
		else if(y >= PERFECT_JUDGE-JUDGE_LEVEL*3){
			System.out.println("Good");
			close();
			return "Good";
		}
		else if(y >= PERFECT_JUDGE-JUDGE_LEVEL*4){
			System.out.println("Bad");
			close();
			return "Bad";
		}
		else {
			System.out.println("Miss");
			close();
			return "Miss";
		}
	}
	
	//노트release 판정 계산 메소드
	public String releaseJudge(long holding_time) {
		//현재 파일에서 holdingtime을 정해주지 않았으므로 press방식과 동일하게 처리
		return pressJudge();
		/*
		//user's holding 시간이 정해진 holding시간에서 2초 이상 벗어나면 Miss판정으로 처리
		if(holding_time >= this.holding_time+2000) {
			System.out.println("Miss");
			close();
			return "Miss";
		}
		else{
			return pressJudge();	//user's holding시간이 정해진 holding시간에서 2초 이내에 들면 노트를 눌렀을 때와 같은 방식으로 판정 처리
		}
		*/
	}
	
	public String getNoteKeyName() {
		return noteKeyName;
	}

	public void setNoteKeyName(String noteKeyName) {
		this.noteKeyName = noteKeyName;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public long getHolding_time() {
		return holding_time;
	}

	public void setHolding_time(long holding_time) {
		this.holding_time = holding_time;
	}


}
