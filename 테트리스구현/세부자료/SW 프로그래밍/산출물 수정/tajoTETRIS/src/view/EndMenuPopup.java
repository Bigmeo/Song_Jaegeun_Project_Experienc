package view;

import java.awt.Color;
//사각형 그리기 위한 자바 기본 클래스
import java.awt.Graphics;
//버튼 클릭 시 발생하는 이벤트를 넣기 위한 자바 기본 클래스
import java.awt.event.ActionEvent;
//버튼 클릭의 이벤트를 받는 메소드 사용하기 위한 자바 기본 클래스
import java.awt.event.ActionListener;

//버튼 생성을 위한 자바 기본 클래스
import javax.swing.JButton;
//팝업 창을 띄우는 자바 기본 클래스
import javax.swing.JDialog;
//창 닫기 작업을 제어하기위한 자바 기본 클래스
import javax.swing.WindowConstants;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//화면 중앙으로 띄우기 위해 선언해둔 클래스 import
import util.ScreenUtil;

public class EndMenuPopup extends JDialog {

	//각 버튼 함수
	
	private JButton mjbMainMenu;
	private JButton mjbExit;

	public EndMenuPopup() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	//Setting화면 에 대한 함수
	private void initWholeSetting() {
		setTitle("TETRIS - END MENU");
		setModal(true);
		setLayout(null);
		setSize(500, 400);
		setLocation(ScreenUtil.getCenterPosition (this));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	//버튼 크기와 위치에 대한 함수
	private void initMembers() {
		MyMouseListener listener = new MyMouseListener();
		
		mjbMainMenu = new JButton("M A I N M E N U");
		mjbMainMenu.setBounds(150, 240, 200, 30);
		mjbMainMenu.setBackground(Color.WHITE);
		mjbMainMenu.addMouseListener(listener);
		add(mjbMainMenu);
		mjbExit = new JButton("E X I T");
		mjbExit.setBounds(150, 270, 200, 30);
		mjbExit.setBackground(Color.WHITE);
		mjbExit.addMouseListener(listener);
		add(mjbExit);
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
			
			JButton mjbMainMenu = (JButton)e.getSource();
			JButton mjbExit = (JButton)e.getSource();
			
			mjbMainMenu.setBackground(Color.YELLOW);
			mjbExit.setBackground(Color.YELLOW);
		}
	
		@Override
		public void mouseExited(MouseEvent e) {
			
			JButton mjbMainMenu = (JButton)e.getSource();
			JButton mjbExit = (JButton)e.getSource();
			
			mjbMainMenu.setBackground(Color.WHITE);
			mjbExit.setBackground(Color.WHITE); 
		}
	}
	
	//버튼 이벤트 출력하는 함수
	private void setEvents() {
		
		mjbMainMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainMenuPopup().setVisible(true);
			}
		});
		mjbExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	//T_T 표현
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// T
		g.fill3DRect(145, 90, 20, 20, true);
		g.fill3DRect(165, 90, 20, 20, true);
		g.fill3DRect(185, 90, 20, 20, true);
		g.fill3DRect(165, 110, 20, 20, true);
		g.fill3DRect(165, 130, 20, 20, true);
		g.fill3DRect(165, 150, 20, 20, true);
		g.fill3DRect(165, 170, 20, 20, true);

		// _
		g.fill3DRect(225, 170, 20, 20, true);
		g.fill3DRect(245, 170, 20, 20, true);
		g.fill3DRect(265, 170, 20, 20, true);

		// T
		g.fill3DRect(305, 90, 20, 20, true);
		g.fill3DRect(325, 90, 20, 20, true);
		g.fill3DRect(345, 90, 20, 20, true);
		g.fill3DRect(325, 110, 20, 20, true);
		g.fill3DRect(325, 130, 20, 20, true);
		g.fill3DRect(325, 150, 20, 20, true);
		g.fill3DRect(325, 170, 20, 20, true);
	}
}