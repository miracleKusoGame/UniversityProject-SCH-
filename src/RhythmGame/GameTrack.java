package RhythmGame;

//곡에 대한 정보를 담는 클래스
public class GameTrack {
	private String titleImage;	//노래 제목 이미지
	private String albumImage;	//게임 선택 창 표지 이미지
	private String gameImage;	//해당 곡을 실행했을 때 표지 이미지
	private String startMusic;	//게임 선택 창 음악
	private String gameMusic;	//해당 곡을 실행했을 때 음악
	private String titleName;	//노래 제목
	private int musicPlayTime;	//총 노래시간(sec)
	
	public GameTrack(String titleImage, String startImage, String gameImage, String startMusic, String gameMusic, String titleName, int musicPlayTime) {
		super();
		this.titleImage = titleImage;
		this.albumImage = startImage;
		this.gameImage = gameImage;
		this.startMusic = startMusic;
		this.gameMusic = gameMusic;
		this.titleName = titleName;
		this.musicPlayTime = musicPlayTime;
	}
	
	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getTitleImage() {
		return titleImage;
	}
	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	public String getAlbumImage() {
		return albumImage;
	}
	public void setAlbumImage(String startImage) {
		this.albumImage = startImage;
	}
	public String getGameImage() {
		return gameImage;
	}
	public void setGameImage(String gameImage) {
		this.gameImage = gameImage;
	}
	public String getStartMusic() {
		return startMusic;
	}
	public void setStartMusic(String startMusic) {
		this.startMusic = startMusic;
	}
	public String getGameMusic() {
		return gameMusic;
	}
	public void setGameMusic(String gameMusic) {
		this.gameMusic = gameMusic;
	}

	public int getMusicPlayTime() {
		return musicPlayTime;
	}

	public void setMusicPlayTime(int musicPlayTime) {
		this.musicPlayTime = musicPlayTime;
	}
	
	
}
