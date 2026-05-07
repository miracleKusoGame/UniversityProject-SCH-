package RhythmGame;

public class BasicBeatGame{
	
	public static final int SCREEN_WIDTH = 1920;	//frame's width size
	public static final int SCREEN_HEIGHT = 1080;	//frame's height size
	public static final int NOTE_SPEED = 10;	//노트가 떨어지는 속도
	public static final int SLEEP_TIME = 10;	//노트가 떨어지는 주기(노트가 무한정 떨어지지 않도록)
	public static final int REACH_JUDGEBAR_TIME = 1;	//한 노트가 판정선에 도달하기까지의 시간(sec)
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BasicBeat();
	}

}
