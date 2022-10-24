package view;

import java.awt.Color;
//버튼 클릭 시 발생하는 이벤트를 넣기 위한 자바 기본 클래스
import java.awt.event.ActionEvent;
//버튼 클릭의 이벤트를 받는 메소드 사용하기 위한 자바 기본 클래스
import java.awt.event.ActionListener;

//버튼 생성을 위한 자바 기본 클래스
import javax.swing.JButton;
//팝업 창을 띄우는 자바 기본 클래스
import javax.swing.JDialog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//화면 중앙으로 띄우기 위해 선언해둔 클래스 import
import util.ScreenUtil;

public class PauseMenuPopup extends JDialog {

	private TetrisView tetrisView;

	private JButton mjbResume;
	private JButton mjbMainMenu;

	public PauseMenuPopup(TetrisView tetrisView) {
		initWholeSetting();
		initMembers(tetrisView);
		setEvents();
	}

	//PausePopup에 대한 함수
	private void initWholeSetting() {
		//상태바 생략
		setUndecorated(true);
		setModal(true);
		setLayout(null);
		setSize(200, 60);
		setLocation(ScreenUtil.getCenterPosition(this));
	}

	//버튼 크기와 위치에 대한 함수
	private void initMembers(TetrisView tetrisView) {
		this.tetrisView = tetrisView;
		MyMouseListener listener = new MyMouseListener();
		mjbResume = new JButton("R E S U M E");
		mjbResume.setBounds(0, 0, 200, 30);
		mjbResume.setBackground(Color.WHITE);
		mjbResume.addMouseListener(listener);
		add(mjbResume);
		mjbMainMenu = new JButton("M A I N M E N U");
		mjbMainMenu.setBounds(0, 30, 200, 30);
		mjbMainMenu.setBackground(Color.WHITE);
		mjbMainMenu.addMouseListener(listener);
		add(mjbMainMenu);
	}
	
class MyMouseListener implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JButton mjbResume = (JButton)e.getSource();
			JButton mjbMainMenu = (JButton)e.getSource();
			mjbResume.setBackground(Color.YELLOW);
			mjbMainMenu.setBackground(Color.YELLOW);
		}
	
		@Override
		public void mouseExited(MouseEvent e) {
			JButton mjbResume = (JButton)e.getSource();
			JButton mjbMainMenu = (JButton)e.getSource();
			mjbResume.setBackground(Color.WHITE);
			mjbMainMenu.setBackground(Color.WHITE);
		}
	}

	private void setEvents() {
		mjbResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mjbMainMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				tetrisView.end();
				new MainMenuPopup().setVisible(true);
			}
		});
	}
}