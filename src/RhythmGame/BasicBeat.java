package RhythmGame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class BasicBeat extends JFrame{
	public static PlayingGame game;	//PlayingGame.class는 현재 프로젝트 전체에서 사용됨
	
	//double buffering을 위한 전체 화면에 대한 이미지를 담는 인스턴스 screenGraphic, screenImage
	private Image screenImage;
	private Graphics screenGraphic;
	
	private Image background;	//game's background image
	
	private Image rank;	//rank image in result
	private Image clearedLogo;	//clear logo in result
	private Image titleImage;	//selected song's title image
	private Image selectedImage;	//selected song's album image
	private Image activePanel;	//현재 선택된 곡을 표시하는 이미지
	
	private JLabel menuBar;
	
	
	private JButton exitButton;	//종료버튼
	//buttons in game's introduction
	private JButton freeplayButton;
	private JButton optionButton;
	private JButton creditButton;
	//buttons in game's main
	private JButton selectSongsLeft, selectSongsRight;	//left, right button
	private JButton startButton;	//game start button
	//button in playing game
	private JButton pauseButton;
	//button in result
	private JButton returnToMain;

	//마우스가 이미지 안으로 들어올 때 버튼 이미지:~EnteredButton, 마우스가 이미지 밖에 위치할 때 버튼 이미지:~UnenteredButton
	private ImageIcon exitEnteredButton;
	private ImageIcon exitUnenteredButton;
	private ImageIcon freeplayUnenteredButton;
	private ImageIcon freeplayEnteredButton;
	private ImageIcon optionUnenteredButton;
	private ImageIcon optionEnteredButton;
	private ImageIcon creditUnenteredButton;
	private ImageIcon creditEnteredButton;
	private ImageIcon leftUiButton, rightUiButton;
	private ImageIcon leftEnteredUiButton, rightEnteredUiButton;
	private ImageIcon startUnenteredButton, startEnteredButton;
	private ImageIcon pauseEnteredButton, pauseUnenteredButton;
	private ImageIcon returnToMainEnteredButton, returnToMainUnenteredButton;

	private GameMusic introMusic;	//game's introduction BGM
	private GameMusic selectedMusic;	//selected song
	
	private String userName;	//user's name
	private int calibration;	//calibration(millisecond)
	
	private int mouseX, mouseY;
	private final int Song_SIZE = 300;
	private boolean isMainScreen;
	private boolean isGamePlayingScreen;
	private boolean isResultScreen;
	private int nowSelectedsong = 0;	//현재 선택이 된 트랙의 번호. 처음에는 0번 트랙으로 초기화
	private boolean play_count = false;

	private ArrayList<Image> song_title = new ArrayList<>();
	private ArrayList<Image> selectSongsImage = new ArrayList<Image>();
	private ArrayList<GameTrack> trackList = new ArrayList<>();

	public BasicBeat(){
		//프로그램 특성상 loading(위에서부터 순차적으로)이 길어지면 오류 발생 확률이 높아진다. trackList에 add가 이루어지지 않은 상태에서 버튼 이벤트가 발생해서 해당 곡을 실행하면 프로그램에 오류가 발생할 수 있으므로, 처음부터 trackList에 gametrack객체들을 add함
		//trackList에 현재 곡들에 대한 정보 추가
		trackList.add(new GameTrack("fly↑high_title.png", "fly↑high.png", "gameplayimage.png", "fly↑high.mp3", "fly↑high.mp3", "fly↑high", 67));
		trackList.add(new GameTrack("mylifeisforyou_title.png", "mylifeisforyou.png", "gameplayimage.png", "My Life is for You.mp3", "My Life is for You.mp3", "My Life is for You", 111));
		trackList.add(new GameTrack("rhythmdoctorboss_title.png", "rhythmdoctorboss.png", "gameplayimage.png", "Battleworn Insomniac.mp3", "Battleworn Insomniac.mp3", "Battleworn Insomniac", 168));
		trackList.add(new GameTrack("brainpower_title.png", "brainpower.png", "gameplayimage.png", "Brain Power.mp3", "Brain Power.mp3", "Brain Power", 115));

		setTitle("BasicBeat");	//game's name
		setSize(BasicBeatGame.SCREEN_WIDTH, BasicBeatGame.SCREEN_HEIGHT);	//frame의 size를 FHD(1980x1080)로 설정
		setResizable(false);	//사용자는 정해진 size의 frame크기를 임의로 조정할 수 없도록 함
		setLocationRelativeTo(null);	//프로그램 실행 시 화면이 정중앙에 위치하도록 함
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);	//기본적으로 존재하는 메뉴 바를 보이지 않도록 함
		setBackground(new Color(0, 0, 0, 0));	//투명한 색
		setLayout(null);	//절대 위치
		setVisible(true);

		addKeyListener(new KeyListener());	//keyListener in frame

		background = new ImageIcon(BasicBeatGame.class.getResource("../images/introduction.png")).getImage();	//BasicBeatGame의 리소스를 가져와서 이를 통해 images package에 있는 게임 시작화면 image를 가져옴. 그리고 이를 background에 초기화
		activePanel = new ImageIcon(BasicBeatGame.class.getResource("../images/panel_selected.png")).getImage();	//현재 선택된 곡의 이미지에 겹쳐지는 패널 이미지.
		
		//각 리스트에 노래 앨범 이미지, 노래 제목 이미지를 add
		song_title.add(new ImageIcon(BasicBeatGame.class.getResource("../images/fly↑high_title.png")).getImage());	//song's title image
		song_title.add(new ImageIcon(BasicBeatGame.class.getResource("../images/mylifeisforyou_title.png")).getImage());	//song's title image
		song_title.add(new ImageIcon(BasicBeatGame.class.getResource("../images/rhythmdoctorboss_title.png")).getImage());	//song's title image
		song_title.add(new ImageIcon(BasicBeatGame.class.getResource("../images/brainpower_title.png")).getImage());	//song's title image
		selectSongsImage.add(new ImageIcon(BasicBeatGame.class.getResource("../images/fly↑high.png")).getImage());	//song's album image
		selectSongsImage.add(new ImageIcon(BasicBeatGame.class.getResource("../images/mylifeisforyou.png")).getImage());	//song's album image
		selectSongsImage.add(new ImageIcon(BasicBeatGame.class.getResource("../images/rhythmdoctorboss.png")).getImage());	//song's album image
		selectSongsImage.add(new ImageIcon(BasicBeatGame.class.getResource("../images/brainpower.png")).getImage());	//song's album image
		
		//현재 배경이미지는 main-background, game playing background, result background이 아님
		isMainScreen = false;
		isGamePlayingScreen = false;
		isResultScreen = false;

		introMusic = new GameMusic("HeartPoundingFlight.mp3", true);	//game's introduction BGM
		introMusic.start();	//소리 재생

		menuBar = new JLabel(new ImageIcon(BasicBeatGame.class.getResource("../images/menubar.png")));	//game's top menu bar

		menuBar.setBounds(0, 0, 1920, 30);	//menubar의 위치와 크기를 정해주는 메소드

		//마우스로 메뉴 바를 눌렀을 때 마우스의 위치mouseX, mouseY를 구해주는Mouse Listener를 menuBar에 add
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		//마우스로 메뉴 바를 드래그했을 때 프레임 전체가 마우스 드래그를 한 곳으로 움직이도록 해주는 MouseMotionListener를 menuBar에 add
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			//마우스 드래그 시 호출되는 메소드
			@Override
			public void mouseDragged(MouseEvent e) {
				//마우스로 드래그하는 순간마다 마우스의x, y좌표를 구함
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);	//자동으로 현재 프레임의 위치를 parameter에 해당하는 곳으로 바꿔주는 메소드
			}
		});

		//exit버튼 이미지 설정(기본적으로 JButton에는 눌리지 않은(unentered)버튼을 넣음)
		exitEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/exitenteredbutton.jpg"));
		exitUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/exitbutton.jpg"));
		exitButton = new JButton(exitUnenteredButton);
		//button image setting in introduction(기본적으로 JButton에는 눌리지 않은(unentered)버튼을 넣음)
		freeplayEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/freeplay_pressedbutton.png"));
		freeplayUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/freeplay_unpressedbutton.png"));
		freeplayButton = new JButton(freeplayUnenteredButton);
		optionEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/options_pressedbutton.png"));
		optionUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/options_unpressedbutton.png"));
		optionButton = new JButton(optionUnenteredButton);
		creditEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/credit_pressedbutton.png"));
		creditUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/credit_unpressedbutton.png"));
		creditButton = new JButton(creditUnenteredButton);

		//setting to make button in introduction visible
		exitButton.setVisible(true);
		freeplayButton.setVisible(true);
		optionButton.setVisible(true);
		creditButton.setVisible(true);

		//button image setting in main(기본적으로 JButton에는 눌리지 않은(unentered)버튼을 넣음)
		leftUiButton = new ImageIcon(BasicBeatGame.class.getResource("../images/uileftbutton.png"));	//곡 선택 메인화면 왼쪽 화살표 이미지
		rightUiButton = new ImageIcon(BasicBeatGame.class.getResource("../images/uirightbutton.png"));	//곡 선택 메인화면 오른쪽 화살표 이미지
		leftEnteredUiButton = new ImageIcon(BasicBeatGame.class.getResource("../images/uileftenteredbutton.png"));	//곡 선택 메인화면 왼쪽 화살표 이미지(눌린 상태)
		rightEnteredUiButton = new ImageIcon(BasicBeatGame.class.getResource("../images/uirightenteredbutton.png"));	//곡 선택 메인화면 오른쪽 화살표 이미지(눌린 상태)
		selectSongsLeft = new JButton(leftUiButton);
		selectSongsRight = new JButton(rightUiButton);
		startUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/startbutton_unpressed.png"));
		startEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/startbutton_pressed.png"));
		startButton = new JButton(startUnenteredButton);
		
		//button image setting in playing game(기본적으로 JButton에는 눌리지 않은(unentered)버튼을 넣음)
		pauseEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/pauseunenteredbutton.png"));
		pauseUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/pauseenteredbutton.png"));
		pauseButton = new JButton(pauseUnenteredButton);
		
		//button image setting in result(기본적으로 JButton에는 눌리지 않은(unentered)버튼을 넣음)
		returnToMainEnteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/pressed_return.png"));
		returnToMainUnenteredButton = new ImageIcon(BasicBeatGame.class.getResource("../images/unpressed_return.png"));
		returnToMain = new JButton(returnToMainUnenteredButton);

		//setting to make buttons in main, game, result invisible
		selectSongsLeft.setVisible(false);
		selectSongsRight.setVisible(false);
		startButton.setVisible(false);
		pauseButton.setVisible(false);
		returnToMain.setVisible(false);


		//setting exit button
		exitButton.setBounds(1890, 0, 30, 30);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(exitEnteredButton);	//마우스가 exit버튼 위에 위치하면 바뀌는 버튼의 이미지
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 exit버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 exit버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(exitUnenteredButton);	////마우스가 exit버튼을 벗어날 때 버튼의 이미지
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 exit버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("mousepressedsound.mp3", false);	//마우스가 exit버튼을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				//exit버튼을 누르자마자 프로그램이 종료되면 소리를 재생하는 의미가 없어지므로, 버튼을 누르고나서 마우스 효과음이 재생되고 난 후 프로그램 종료하도록 만들어야 함
				try {
					Thread.sleep(1000);	//exit버튼을 누르고나서 1000milliseconds만큼 thread를 sleep시킨다(exit버튼을 누르고 1초동안 프로그램 종료하지 않고 가만히 있도록 함)
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
				System.exit(0);	//system terminate method
			}
		});
		//이 순서대로 frame에 추가해야 exit버튼이 메뉴 바 위에 제대로 위치
		add(exitButton);
		add(menuBar);

		//setting freeplay button
		freeplayButton.setBounds(1350, 450, 396, 90);
		freeplayButton.setBorderPainted(false);
		freeplayButton.setContentAreaFilled(false);
		freeplayButton.setFocusPainted(false);
		freeplayButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				freeplayButton.setIcon(freeplayEnteredButton);	//마우스가 freeplay버튼 위에 위치하면 바뀌는 버튼의 이미지
				freeplayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 freeplay버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 freeplay버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				freeplayButton.setIcon(freeplayUnenteredButton);	////마우스가 freeplay버튼을 벗어날 때 버튼의 이미지
				freeplayButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 freeplay버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("startscreenEnter.mp3", false);	//마우스가 freeplay버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
				introToMain();
			}
		});
		//add in frame
		add(freeplayButton);

		//setting option button
		optionButton.setBounds(1350, 550, 396, 90);
		optionButton.setBorderPainted(false);
		optionButton.setContentAreaFilled(false);
		optionButton.setFocusPainted(false);
		optionButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				optionButton.setIcon(optionEnteredButton);	//마우스가 option버튼 위에 위치하면 바뀌는 버튼의 이미지
				optionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 option버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 option버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				optionButton.setIcon(optionUnenteredButton);	////마우스가 option버튼을 벗어날 때 버튼의 이미지
				optionButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 option버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("startscreenEnter.mp3", false);	//마우스가 option버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
				calibration = Integer.parseInt(JOptionPane.showInputDialog(null, "Input Calibration(millisecond) :", "0"));//credit button이 눌리면 user's name을 설정하기 위한 입력 창을 띄워줌
				setFocusable(true);	//키보드 이벤트가 항상 오류 없이 정확히 동작하도록 함
			}
		});
		//add in frame
		add(optionButton);

		//setting credit button
		creditButton.setBounds(1350, 650, 396, 90);
		creditButton.setBorderPainted(false);
		creditButton.setContentAreaFilled(false);
		creditButton.setFocusPainted(false);
		creditButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				creditButton.setIcon(creditEnteredButton);	//마우스가 credit버튼 위에 위치하면 바뀌는 버튼의 이미지
				creditButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 credit버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 credit버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				creditButton.setIcon(creditUnenteredButton);	////마우스가 credit버튼을 벗어날 때 버튼의 이미지
				creditButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 credit버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("startscreenEnter.mp3", false);	//마우스가 credit버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
				userName = JOptionPane.showInputDialog(null, "Input User's Name(Max size:15) :", "Rhythm Gamer");	//credit button이 눌리면 user's name을 설정하기 위한 입력 창을 띄워줌
				setFocusable(true);	//키보드 이벤트가 항상 오류 없이 정확히 동작하도록 함
			}
		});
		//add in frame
		add(creditButton);

		//setting left button
		selectSongsLeft.setBounds(0, 500, 70, 110);
		selectSongsLeft.setBorderPainted(false);
		selectSongsLeft.setContentAreaFilled(false);
		selectSongsLeft.setFocusPainted(false);
		selectSongsLeft.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				selectSongsLeft.setIcon(leftEnteredUiButton);	//마우스가 왼쪽 화살표 버튼 위에 위치하면 바뀌는 버튼의 이미지
				selectSongsLeft.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 왼쪽 화살표 버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 왼쪽 화살표 버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				selectSongsLeft.setIcon(leftUiButton);	////마우스가 왼쪽 화살표 버튼을 벗어날 때 버튼의 이미지
				selectSongsLeft.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 왼쪽 화살표 버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("mousepressedsound.mp3", false);	//마우스가 왼쪽 화살표 버튼을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				leftArrowSelect();	//마우스로 왼쪽 화살표 버튼 클릭 시 이전 곡으로 이동
			}
		});
		//add in frame
		add(selectSongsLeft);

		//setting right button
		selectSongsRight.setBounds(1850, 500, 70, 110);
		selectSongsRight.setBorderPainted(false);
		selectSongsRight.setContentAreaFilled(false);
		selectSongsRight.setFocusPainted(false);
		selectSongsRight.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				selectSongsRight.setIcon(rightEnteredUiButton);	//마우스가 오른쪽 화살표 버튼 위에 위치하면 바뀌는 버튼의 이미지
				selectSongsRight.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 오른쪽 화살표 버튼 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 오른쪽 화살표 버튼 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				selectSongsRight.setIcon(rightUiButton);	////마우스가 오른쪽 화살표 버튼을 벗어날 때 버튼의 이미지
				selectSongsRight.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 오른쪽 화살표 버튼을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("mousepressedsound.mp3", false);	//마우스가 오른쪽 화살표 버튼을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				rightArrowSelect();	//마우스로 오른쪽 화살표 버튼 클릭 시 다음 곡으로 이동
			}
		});
		//add in frame
		add(selectSongsRight);

		//setting start button
		startButton.setBounds(465, 800, 990, 240);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(startEnteredButton);	//마우스가 start button 위에 위치하면 바뀌는 버튼의 이미지
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 start button 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 start button 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				startButton.setIcon(startUnenteredButton);	////마우스가 start button을 벗어날 때 버튼의 이미지
				startButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 start button을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("confirmMenu.mp3", false);	//마우스가 start button을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				gameStart(nowSelectedsong);	//마우스를 눌르면 선택된 곡으로 게임 시작
			}
		});
		//add in frame
		add(startButton);

		//setting pause button
		pauseButton.setBounds(1840, 50, 60, 60);
		pauseButton.setBorderPainted(false);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setFocusPainted(false);
		pauseButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				pauseButton.setIcon(pauseEnteredButton);	//마우스가 pause button 위에 위치하면 바뀌는 버튼의 이미지
				pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 pause button 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 pause button 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pauseButton.setIcon(pauseUnenteredButton);	////마우스가 pause button을 벗어날 때 버튼의 이미지
				pauseButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 pause button을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("mousepressedsound.mp3", false);	//마우스가 pause button을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				gameToMain();
			}
		});
		//add in frame
		add(pauseButton);

		
		//setting return-to-main button
		returnToMain.setBounds(1500, 850, 400, 225);
		returnToMain.setBorderPainted(false);
		returnToMain.setContentAreaFilled(false);
		returnToMain.setFocusPainted(false);
		returnToMain.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				returnToMain.setIcon(returnToMainEnteredButton);	//마우스가 return-to-main button 위에 위치하면 바뀌는 버튼의 이미지
				returnToMain.setCursor(new Cursor(Cursor.HAND_CURSOR));	//마우스가 return-to-main button 위에 위치하면 마우스 커서의 모양이 손바닥으로 바뀜
				GameMusic pointedMouse = new GameMusic("mouseclickedsound.mp3", false);	//마우스가 return-to-main button 위에 위치하면 나는 소리
				pointedMouse.start();	//소리 재생
			}

			@Override
			public void mouseExited(MouseEvent e) {
				returnToMain.setIcon(returnToMainUnenteredButton);	////마우스가 return-to-main button을 벗어날 때 버튼의 이미지
				returnToMain.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	//마우스가 return-to-main button을 벗어날 때 마우스 커서의 모양이 원래대로 돌아옴
			}

			@Override
			public void mousePressed(MouseEvent e) {
				GameMusic pointedMouse = new GameMusic("mousepressedsound.mp3", false);	//마우스가 return-to-main button을 누르면 나는 소리
				pointedMouse.start();	//소리 재생
				gameToMain();
			}
		});
		//add in frame
		add(returnToMain);
		
	}

	@Override
	public void paint(Graphics g) {
		screenImage = createImage(BasicBeatGame.SCREEN_WIDTH, BasicBeatGame.SCREEN_HEIGHT);	//FHD의 이미지를 만들어 screenImage에 저장
		screenGraphic = screenImage.getGraphics();	//screenImage를 이용해 그래픽 객체를 얻음
		screenDraw((Graphics2D)screenGraphic);	//이미지 그려줌
		g.drawImage(screenImage, 0, 0, null);	//투명이미지 그려줌
	}


	public void screenDraw(Graphics2D g) {
		g.drawImage(background, 0, 0, null);	//introBackground를 screenImage에 그려줌

		//현재 화면이 메인화면일 때, 메인화면 구성요소들을 그려줌
		if(isMainScreen) {
			int x=150;	//selectSongsImage가 그려질 위치의 초기 x좌표
			int y = 30;	//selectSongsImage가 그려질 위치의 초기 y좌표
			for(int idx = 0;idx < selectSongsImage.size();idx++) {
				if(y > 690-Song_SIZE) {
					x += Song_SIZE + 10;	//selectSongsImage의 y좌표가 화면의 크기를 벗어나면 이미지 크기+10만큼 x좌표를 늘려준다.
					y = 30;
				}
				
				g.drawImage(selectSongsImage.get(idx), x, y, null);	//selectSongsImage(곡의 앨범 이미지들)을 그려준다.
				g.drawImage(song_title.get(idx), x, y + 300, null);	//song_title(곡의 제목 이미지들)을 그려준다.
				
				if(idx == nowSelectedsong)	g.drawImage(activePanel, x, y, null);	//현재 선택된 곡에는 패널 이미지를 겹쳐 그린다(선택된 곡 표시)
				
				y += Song_SIZE + 60;	//selectSongsImage의 크기 + 10(이미지 사이 간격)에 맞춰 곡의 이미지를 그릴 y좌표를 구함(겹치지 않게)
			}
		}

		//현재 게임을 플레이할 때, 게임 플레이 화면의 구성요소들을 그려줌
		if(isGamePlayingScreen) {	
			game.screenDraw(g);
		}

		//현재 게임이 종료되어 결과가 나올 때, 결과 화면의 구성요소들을 그려줌
		if(isResultScreen) {
			g.drawImage(rank, 660, 400, null);
			g.drawImage(clearedLogo, 560, 200, null);
		}
		
		
		paintComponents(g);	//image를 screenImage변수에 넣어주는 것 외에 JLabel객체 등을 추가하면 이들도 그려주는 메소드(항상 움직임이 없는 이미지의 경우 사용하면 좋음), add()메소드에 의해 추가된 것들을 그려줌

		try {
			Thread.sleep(5);	//안정적인 애니메이션 효과를 위해 0.005초라는 term을 주도록 함.
		}catch(Exception e) {
			e.printStackTrace();
		}

		this.repaint();	//paint메소드를 호출
	}

	//선택된 곡을 재생시키는 메소드
	public void selectTrack(int nowSelectedsong) {
		//선택된 곡이 있다면 재생을 종료시킴
		if(selectedMusic != null) {
			selectedMusic.close();
		}

		titleImage = new ImageIcon(BasicBeatGame.class.getResource("../images/" + trackList.get(nowSelectedsong).getTitleImage())).getImage();	//해당 track의 title image를 구함
		selectedImage = new ImageIcon(BasicBeatGame.class.getResource("../images/" + trackList.get(nowSelectedsong).getAlbumImage())).getImage();	//해당 track의 start image를 구함
		selectedMusic = new GameMusic(trackList.get(nowSelectedsong).getStartMusic(), true);	//trackList에 저장된 곡들 중 현재 선택된 곡을 selectedMusic에 저장
		selectedMusic.start();	//selectedMusic에 저장된 곡 재생(gamemusic에 2번째 parameter가 true이므로 무한재생)
	}

	//메인 화면의 왼쪽 화살표를 선택한 경우
	public void leftArrowSelect() {
		//제일 첫번째 곡이 선택된 상태인 경우
		if(nowSelectedsong == 0)
			nowSelectedsong = trackList.size() - 1;	//마지막 곡으로 선택하도록 함
		else
			nowSelectedsong--;	//현재 곡의 이전 곡을 선택
		selectTrack(nowSelectedsong);
	}

	//메인 화면의 오른쪽 화살표를 선택한 경우
	public void rightArrowSelect() {
		//마지막 곡이 선택된 상태인 경우
		if(nowSelectedsong == trackList.size() - 1)
			nowSelectedsong = 0;	//처음 곡으로 선택하도록 함
		else
			nowSelectedsong++;	//현재 곡의 다음 곡을 선택
		selectTrack(nowSelectedsong);
	}

	//메인화면에서 게임 시작화면으로 이동
	public void gameStart(int nowSelected) {
		if(selectedMusic != null) {
			selectedMusic.close();	//메인화면에서 선택된 곡 재생을 종료
		}
		isMainScreen = false;	//현재 메인화면이 아님
		isGamePlayingScreen = true;	//현재 게임 플레이 화면이 맞음

		//메인화면의 모든 버튼을 안보이도록 함
		selectSongsLeft.setVisible(false);
		selectSongsRight.setVisible(false);
		startButton.setVisible(false);

		background = new ImageIcon(BasicBeatGame.class.getResource("../images/" + trackList.get(nowSelected).getGameImage())).getImage();	//change main background to playing game background
		pauseButton.setVisible(true);	//set pause button visible in playing game

		game = new PlayingGame(trackList.get(nowSelectedsong).getTitleName(), trackList.get(nowSelectedsong).getGameMusic(), trackList.get(nowSelectedsong).getMusicPlayTime());
		game.setUserName(userName);	//setting user's name in playing game
		game.setCalibration(calibration);	//setting calibration in playing game
		
		game.start();	//PlayingGame의 run()메소드 실행
		setFocusable(true);	//키보드 이벤트가 항상 오류 없이 정확히 동작하도록 함
		
		// 일정시간 지나면 현재 Thread 를 종료시키도록 하기 위한 killerThread
        Thread killerThread = new Thread() {
            @Override
            public void run() {
                try {
                    // 해당 곡이 끝나는 시간에 종료
                    Thread.sleep(1000*trackList.get(nowSelectedsong).getMusicPlayTime());
                    
                } catch (InterruptedException e) {
                    // 킬러 Thread 종료(killerThread.interrupt())하면 이곳에 도달
                    System.out.println("프로세스 종료");
                    return;
                    
                } catch (Exception e) {
                    // 무시
                }
                
                try {
                    // 일정시간이 지나면 이곳에 도달
                    System.out.println("Now Game is Cleared!");
                    
                    // 현재 Thread 를 종료
                    gameClear();
                } catch (Exception e) {
                    // 무시
                }
            }
        };
		
        killerThread.start();	//killerThread의 run()메소드 실행
	}

	//게임 플레이 화면에서 메인화면으로 이동
	public void gameToMain() {
		//메인화면의 버튼들은 모두 보이도록 설정
		selectSongsLeft.setVisible(true);
		selectSongsRight.setVisible(true);
		startButton.setVisible(true);
		//게임 플레이 중이나 결과에서의 버튼들은 모두 안보이도록 설정
		pauseButton.setVisible(false);
		returnToMain.setVisible(false);

		background = new ImageIcon(BasicBeatGame.class.getResource("../images/musedashcharactermain.jpg")).getImage();	//현재 배경이미지를 메인 화면의 배경이미지로 변경
		selectTrack(nowSelectedsong);	//메인화면에서 곡이 선택되도록 함
		isMainScreen = true;	//현재 메인화면임을 나타냄
		//현재 게임 플레이 화면, 결과화면이 아님을 나타냄
		isGamePlayingScreen = false;
		isResultScreen = false;

		game.close();	//현재 플레이 중인 게임 종료
	}

	//intro화면에서 메인화면으로 이동
	public void introToMain() {
		background = new ImageIcon(BasicBeatGame.class.getResource("../images/musedashcharactermain.jpg")).getImage();	//처음 배경이미지를 메인 화면의 배경이미지로 변경
		isMainScreen = true;	//메인화면의 이미지임을 나타냄
		introMusic.close();	//game introduction BGM을 끔

		if(play_count == false) {
			selectTrack(nowSelectedsong);//메인화면에서 처음에는 왼쪽에서 첫번째 곡 재생(초기 nowSelectedsong=0)
			play_count = true;	//메인화면을 다시 눌러도 0번 트랙에 해당하는 곡이 재생되지 않도록 설정
		}

		//메인화면의 양쪽에 위치한 화살표 버튼을 보이도록 함
		selectSongsLeft.setVisible(true);
		selectSongsRight.setVisible(true);

		//메인화면에서 난이도 설정 버튼을 보이도록 함
		startButton.setVisible(true);

		//메인화면에서 introduction화면에 있던 버튼을 보이지 않도록 함
		freeplayButton.setVisible(false);
		optionButton.setVisible(false);
		creditButton.setVisible(false);
	}
	
	//게임 플레이가 끝난 후 결과화면으로 이동
	public void gameClear() {
		background = new ImageIcon(BasicBeatGame.class.getResource("../images/resultbackground.png")).getImage();	//결과창 배경이미지

		//플레이한 게임의 스코어에 맞는 rank, clear logo의 이미지 설정
		if(game.getGameScore() == 100000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/ex_rank.png")).getImage();	
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 99000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/sss_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 95000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/ss_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 90000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/s_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 85000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/a_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 70000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/b_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 50000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/c_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/cleared.png")).getImage();
		}
		else if(game.getGameScore() >= 30000) {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/d_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/failed.png")).getImage();
		}
		else {
			rank = new ImageIcon(BasicBeatGame.class.getResource("../images/e_rank.png")).getImage();
			clearedLogo = new ImageIcon(BasicBeatGame.class.getResource("../images/failed.png")).getImage();
		}

		//현재 화면이 결과 화면임을 나타냄
		isMainScreen = false;
		isGamePlayingScreen = false;
		isResultScreen = true;

		returnToMain.setVisible(true);	//결과 화면에서 필요한 버튼이 보이도록 설정

		game.close();	//현재 플레이중인 게임 종료
	}
	
}
