package RhythmGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter{

	@Override
	public void keyPressed(KeyEvent e) {
		if(BasicBeat.game == null)	return;
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			BasicBeat.game.pressD();	//D키를 누르면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_F) {
			BasicBeat.game.pressF();	//F키를 누르면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_J) {
			BasicBeat.game.pressJ();	//J키를 누르면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_K) {
			BasicBeat.game.pressK();	//K키를 누르면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			BasicBeat.game.pressSPACEBAR();	//SPACEBAR키를 누르면 pressD메소드 호출
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(BasicBeat.game == null)	return;
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			BasicBeat.game.releaseD();	//D키를 떼면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_F) {
			BasicBeat.game.releaseF();	//F키를 떼면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_J) {
			BasicBeat.game.releaseJ();	//J키를 떼면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_K) {
			BasicBeat.game.releaseK();	//K키를 떼면 pressD메소드 호출
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			BasicBeat.game.releaseSPACEBAR();	//SPACEBAR키를 떼면 pressD메소드 호출
		}
	}
}
