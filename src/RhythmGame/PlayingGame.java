package RhythmGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class PlayingGame extends Thread{
	
	private Image playInfoImage = new ImageIcon(BasicBeatGame.class.getResource("../images/infobar.png")).getImage();	//게임 플레이 화면 하단 정보 이미지 설정
	private Image judgementLineImage = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementLine.png")).getImage();	//게임 플레이 판정라인 이미지 설정
	//노트가 움직일 공간 이미지 설정
	private Image jNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();
	private Image kNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();
	private Image dNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();
	private Image fNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();
	private Image spaceBarNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
	private Image contour = new ImageIcon(BasicBeatGame.class.getResource("../images/contour.png")).getImage();	//각 노트가 움직일 공간 구분선 이미지 설정
	private Image judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/nojudgestate.png")).getImage();	//노트 hit시 judge effect image
	//judge line이 눌렸을 때 image
	private Image judgementLinePressedImage_D = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
	private Image judgementLinePressedImage_F = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
	private Image judgementLinePressedImage_SPACE = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
	private Image judgementLinePressedImage_J = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
	private Image judgementLinePressedImage_K = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
	
	private String titleName;	//앨범 이름
	private String musicTitle;	//곡의 이름
	private String userName;	//user's name
	private GameMusic gameMusic;	//메인화면에서 선택된 곡
	
	private long pressedTime_D;	//key pressed time
	private long releasedTime_D;	//key released time
	private long pressedTime_F;	//key pressed time
	private long releasedTime_F;	//key released time
	private long pressedTime_SPACE;	//key pressed time
	private long releasedTime_SPACE;	//key released time
	private long pressedTime_J;	//key pressed time
	private long releasedTime_J;	//key released time
	private long pressedTime_K;	//key pressed time
	private long releasedTime_K;	//key released time
	
	private int gameScore = 0;	//게임 플레이동안 얻어지는 game score
	private int musicPlayTime = 0;	//게임 플레이동안 재생되는 곡의 길이
	private int noteCount = 0;	//현재 곡의 노트의 총 개수
	private int calibration = 0;	//calibration
	
	//타격음이 한번 재생되면 더이상 안나오도록 하는 역할
	private boolean hitSoundJudge_D = false;
	private boolean hitSoundJudge_F = false;
	private boolean hitSoundJudge_SPACE = false;
	private boolean hitSoundJudge_J = false;
	private boolean hitSoundJudge_K = false;

	private ArrayList<Note> noteList = new ArrayList<>();	//노트들은 나중에 배열추가 순으로 screenDraw에서 그려진다.
	private ArrayList<HitPoint> hitList = new ArrayList<>();	//여러 타격포인트가 들어갈 채보

	public PlayingGame(String titleName, String musicTitle, int musicPlayTime) {
		super();
		this.titleName = titleName;
		this.musicTitle = musicTitle;
		gameMusic = new GameMusic(this.musicTitle, false);	//게임 플레이 중 곡은 한번만 실행
		this.musicPlayTime = musicPlayTime;
	}

	public void screenDraw(Graphics2D g) {
		//총 5개의 노트라인
		g.drawImage(contour, 220, 30, null);	//구분선을 그려줌
		g.drawImage(dNotePassingSection, 225, 30, null);	//노트가 움직일 공간을 그려줌
		g.drawImage(contour, 515, 30, null);	//구분선을 그려줌
		g.drawImage(fNotePassingSection, 520, 30, null);	//노트가 움직일 공간을 그려줌
		g.drawImage(contour, 810, 30, null);	//구분선을 그려줌
		g.drawImage(spaceBarNotePassingSection, 815, 30, null);	//노트가 움직일 공간을 그려줌
		g.drawImage(contour, 1105, 30, null);	//구분선을 그려줌
		g.drawImage(jNotePassingSection, 1110, 30, null);	//노트가 움직일 공간을 그려줌
		g.drawImage(contour, 1400, 30, null);	//구분선을 그려줌
		g.drawImage(kNotePassingSection, 1405, 30, null);	//노트가 움직일 공간을 그려줌
		g.drawImage(contour, 1695, 30, null);	//구분선을 그려줌

		g.drawImage(judgementLineImage, 220, 970, null);	//게임 플레이 화면 판정라인을 그려줌
		g.drawImage(playInfoImage, 0, 980, null);	//게임 플레이 화면 하단 정보 이미지를 그려줌
		
		//noteList에 추가된 노트들을 각각 그려줌
		for(int idx = 0;idx < noteList.size();idx++) {
			Note note = noteList.get(idx);
			
			if(!note.isProceeded()) {	//해당 노트가 진행중이 아니라면
				noteList.remove(idx);	//해당 인덱스의 노트를 제거
				idx--;	//제거된 노트 이전위치로 이동
			}
			else	note.screenDraw(g);	//judgementLineImage보다 위쪽에 그려져야 하므로 여기에 위치
		}

		//하단 게임 플레이 정보
		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	//텍스트 안티 앨리어싱 적용
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString(titleName, 30, 1040);	//게임 플레이 화면 하단에 곡의 이름을 그려줌
		g.drawString("Player: " + userName, 1500, 1040);	//게임 플레이 화면 하단에 user's name을 그려줌

		g.setFont(new Font("Elephant", Font.BOLD, 30));
		
		//현재 game score를 그려줌
		if(gameScore/100000 > 0)
			g.drawString(""+gameScore, 910, 1040);
		else if((gameScore/10000 > 0))
			g.drawString("0"+gameScore, 910, 1040);
		else if((gameScore/1000 > 0))
			g.drawString("00"+gameScore, 910, 1040);
		else if((gameScore/100 > 0))
			g.drawString("000"+gameScore, 910, 1040);
		else if((gameScore/10 > 0))
			g.drawString("0000"+gameScore, 910, 1040);
		else
			g.drawString("00000"+gameScore, 910, 1040);
		
		g.drawImage(judge_image, 760, 600, null);	//게임플레이 화면 중앙 판정이미지
		
		//키패드가 눌린 상태의 judgementLineImage
		g.drawImage(judgementLinePressedImage_D, 270, 880, null);
		g.drawImage(judgementLinePressedImage_F, 565, 880, null);
		g.drawImage(judgementLinePressedImage_SPACE, 860, 880, null);
		g.drawImage(judgementLinePressedImage_J, 1155, 880, null);
		g.drawImage(judgementLinePressedImage_K, 1450, 880, null);
		
		//(BasicBeatGame.SCREEN_WIDTH/1000)*(gameMusic.getTime()/musicPlayTime)
		//gameMusic.getTime()은 millisecond, musicPlayTime은 second단위이므로 BasicBeatGame.SCREEN_WIDTH을 1000으로 나눠야 함
		//BasicBeatGame.SCREEN_WIDTH/1000=1.92이나 int형이므로 1이 됨. 그러므로 1.92에 가까운 2를 사용
		g.setColor(Color.GREEN);
		g.fillRect(0, BasicBeatGame.SCREEN_HEIGHT-10, 2*(gameMusic.getTime()/musicPlayTime), BasicBeatGame.SCREEN_HEIGHT);	//time line in playing game
	}

	//각 노트들이 떨어지도록 하는 메소드
	public void dropNotes() {
		int offset = 1000;	//java player's delay
		
		//각 노래에 따른 노트 채보
		if(titleName.equals("fly↑high")) {
			hitListMaker("flyhigh.txt", offset);	
		}
		else if(titleName.equals("My Life is for You")) {
			hitListMaker("MyLifeisforYou.txt", offset);
		}
		else if(titleName.equals("Battleworn Insomniac")) {
			hitListMaker("battleworninsomniac.txt", offset);
		}
		else if(titleName.equals("Brain Power")) {
			hitListMaker("BrainPower.txt", offset);
		}
		
		gameMusic.start();	//이 위치에서 곡을 재생해야 배열이 초기화되는 시간에서의 격차를 줄여 초기화 되자마자 곡이 재생된다.

		int idx=0; 
		
		while(idx<hitList.size() && !isInterrupted()) {
			boolean dropped = false;	//현재 노트가 떨어지지 않은 상태
			if(hitList.get(idx).getTime() <= gameMusic.getTime()) {
				Note note = new Note(hitList.get(idx).getNoteKeyName());	//노트 생성
				
				//노트가 long note면 그에 맞는 noteType설정
				if(hitList.get(idx).getNoteType().equals("long_start") || hitList.get(idx).getNoteType().equals("long_end"))	note.setNoteType(hitList.get(idx).getNoteType());
				
				//노트가 long note의 end지점이면 start~end동안 holding해야 하는 시간 설정
				if(hitList.get(idx).getNoteType().equals("long_end"))	note.setHolding_time(hitList.get(idx).getHolding_time());
				
				note.start();	//Note클래스 내 run()실행. 노트가 떨어짐
				noteList.add(note);
				idx++;
				dropped = true;	//현재 노트가 떨어지는 상태
			}
			if(!dropped) {//현재 상태에서 노트가 떨어지지 않은 경우에는 무한으로 떨어뜨리는 것이 아닌 0.005초라는 간격을 두면서 노트를 떨어뜨린다.(자원 낭비를 막음)
				try {
					Thread.sleep(5);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//PlaingGame thread종료
	public void close() {
		gameMusic.close();
		this.interrupt();
	}
	
	//PlaingGame thread실행 시 노트가 떨어지도록 함
	@Override
	public void run() {
		dropNotes();
	}
	
	//게임 플레이 중 D키를 누르면 호출되는 메소드
	public void pressD() {
		pressJudge("D");
		dNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/pressednotesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_D = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlinepressed.gif")).getImage();
		
		if(hitSoundJudge_D == false) {		
			pressedTime_D = System.currentTimeMillis();
			GameMusic hitSound = new GameMusic("perfect.mp3", false);
			hitSound.start();	//소리 재생
			
			hitSoundJudge_D = true;	//소리가 한번 재생된 상태
		}
	}
	
	//게임 플레이 중 D키를 떼면 호출되는 메소드
	public void releaseD() {
		dNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_D = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
		
		releasedTime_D = System.currentTimeMillis();
		releaseJudge("D", releasedTime_D-pressedTime_D);
		hitSoundJudge_D = false;	//키를 떼면 소리 재생된 상태 초기화
	}

	//게임 플레이 중 F키를 누르면 호출되는 메소드
	public void pressF() {
		pressJudge("F");
		fNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/pressednotesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_F = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlinepressed.gif")).getImage();
		
		if(hitSoundJudge_F == false) {
			pressedTime_F = System.currentTimeMillis();
			GameMusic hitSound = new GameMusic("perfect.mp3", false);
			hitSound.start();	//소리 재생
			
			hitSoundJudge_F = true;	//소리가 한번 재생된 상태
		}
	}

	//게임 플레이 중 D키를 떼면 호출되는 메소드
	public void releaseF() {
		fNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_F = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
		
		releasedTime_F = System.currentTimeMillis();
		releaseJudge("F", releasedTime_F-pressedTime_F);
		hitSoundJudge_F = false;	//키를 떼면 소리 재생된 상태 초기화
	}

	//게임 플레이 중 SPACEBAR키를 누르면 호출되는 메소드
	public void pressSPACEBAR() {
		pressJudge("SPACE");
		spaceBarNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/pressednotesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_SPACE = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlinepressed.gif")).getImage();
		
		if(hitSoundJudge_SPACE == false) {
			pressedTime_SPACE = System.currentTimeMillis();
			GameMusic hitSound = new GameMusic("perfect.mp3", false);
			hitSound.start();	//소리 재생
			
			hitSoundJudge_SPACE = true;	//소리가 한번 재생된 상태
		}
	}

	//게임 플레이 중 SPACEBAR키를 떼면 호출되는 메소드
	public void releaseSPACEBAR() {
		spaceBarNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_SPACE = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
		
		releasedTime_SPACE = System.currentTimeMillis();
		releaseJudge("SPACE", releasedTime_SPACE-pressedTime_SPACE);
		hitSoundJudge_SPACE = false;	//키를 떼면 소리 재생된 상태 초기화
	}

	//게임 플레이 중 J키를 누르면 호출되는 메소드
	public void pressJ() {
		pressJudge("J");
		jNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/pressednotesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_J = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlinepressed.gif")).getImage();
		
		if(hitSoundJudge_J == false) {
			pressedTime_J = System.currentTimeMillis();
			GameMusic hitSound = new GameMusic("perfect.mp3", false);
			hitSound.start();	//소리 재생
			
			hitSoundJudge_J = true;	//소리가 한번 재생된 상태
		}
	}

	//게임 플레이 중 J키를 떼면 호출되는 메소드
	public void releaseJ() {
		jNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_J = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
		
		releasedTime_J = System.currentTimeMillis();
		releaseJudge("J", releasedTime_J-pressedTime_J);
		hitSoundJudge_J = false;	//키를 떼면 소리 재생된 상태 초기화
	}

	//게임 플레이 중 K키를 누르면 호출되는 메소드
	public void pressK() {
		pressJudge("K");
		kNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/pressednotesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_K = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlinepressed.gif")).getImage();
		
		if(hitSoundJudge_K == false) {
			pressedTime_K = System.currentTimeMillis();
			GameMusic hitSound = new GameMusic("perfect.mp3", false);
			hitSound.start();	//소리 재생
			
			hitSoundJudge_K = true;	//소리가 한번 재생된 상태
		}
	}

	//게임 플레이 중 K키를 떼면 호출되는 메소드
	public void releaseK() {
		kNotePassingSection = new ImageIcon(BasicBeatGame.class.getResource("../images/notesection.png")).getImage();	//노트가 움직일 공간 이미지 설정
		judgementLinePressedImage_K = new ImageIcon(BasicBeatGame.class.getResource("../images/judgementlineunpressed.png")).getImage();
		
		releasedTime_K = System.currentTimeMillis();
		releaseJudge("K", releasedTime_K-pressedTime_K);
		hitSoundJudge_K = false;	//키를 떼면 소리 재생된 상태 초기화
	}
	
	//노트 press판정 메소드
	public void pressJudge(String input) {
		for(int idx = 0;idx < noteList.size();idx++) {
			if(input.equals(noteList.get(idx).getNoteKeyName()) && !noteList.get(idx).getNoteType().equals("long_end")) {
				judgeEffect(noteList.get(idx).pressJudge());
				break;
			}
		}
	}
	
	//노트 release판정 메소드
	public void releaseJudge(String input, long holding_time) {
		for(int idx = 0;idx < noteList.size();idx++) {
			if(input.equals(noteList.get(idx).getNoteKeyName()) && noteList.get(idx).getNoteType().equals("long_end")) {
				judgeEffect(noteList.get(idx).releaseJudge(holding_time));
				break;
			}
		}
	}
	
	//노트 judge effect메소드
	public void judgeEffect(String judge) {
		//현재 판정에 맞게 판정 이미지 설정 및 game score값 증가
		if(judge.equals("Bad")) {
			judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/bad_judge.png")).getImage();
			gameScore += (int)(100000/noteCount/4);
		}
		else if(judge.equals("Good")) {
			judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/good_judge.png")).getImage();
			gameScore += (int)(100000/noteCount/3);
		}
		else if(judge.equals("Great")) {
			judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/great_judge.png")).getImage();
			gameScore += (int)(100000/noteCount/2);
		}
		else if(judge.equals("Perfect")) {
			judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/perfect_judge.png")).getImage();
			gameScore += (int)(100000/noteCount/1);
		}
		else if(judge.equals("Miss")) {
			judge_image = new ImageIcon(BasicBeatGame.class.getResource("../images/miss_judge.png")).getImage();
		}
		
	}
	
	
	//해당 곡의 beatmap에서 읽어들인 데이터로 비트(hitpoint) 생성
	public void hitListMaker(String beatmapName, int offset) {

		try{
            //파일 객체 생성
            File file = new File(PlayingGame.class.getResource("../beatmap/" + beatmapName).getPath());
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            String line;
            while((line = bufReader.readLine()) != null){	//readLine()은 끝에 개행문자를 읽지 않는다. 
            	int timing = Integer.parseInt(line.substring(0, line.length()-3));
            	char noteKey = line.charAt(line.length()-1);	//line문자열 중 마지막 문자
            	char noteType = line.charAt(line.length()-2);	//line문자열 중 마지막 전 문자
            	String noteKeyName="D";	//note's key
            	String noteTypeName;	//note's type
            	
            	switch(noteKey) {
            	case 'D':
            		noteKeyName = "D";
            		break;
            	case 'F':
            		noteKeyName = "F";
            		break;
            	case 'S':
            		noteKeyName = "SPACE";
            		break;
            	case 'J':
            		noteKeyName = "J";
            		break;
            	case 'K':
            		noteKeyName = "K";
            		break;
            	default:
            		break;
            	}
            	
            	switch(noteType) {
            	case 'l':
            		noteTypeName = "long_start";
            		break;
            	case 'L':
            		noteTypeName = "long_end";
            		break;
            	default:
            		noteTypeName = "short";
            		break;
            	}
            	hitList.add(new HitPoint(timing - offset + calibration, noteKeyName, noteTypeName));
            	noteCount++;
            }
            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        	e.printStackTrace();
        }catch(IOException e){
        	e.printStackTrace();
        }
	}
	
	public int getGameScore() {
		return gameScore;
	}
	
	public void setUserName(String userName) {
		if(userName == null)	userName = "Rhythm Gamer";	//user's name 설정이 안되어있으면 Rhythm Gamer로 설정
		this.userName = userName;
	}

	public void setCalibration(int calibration) {
		this.calibration = calibration;
	}
}
