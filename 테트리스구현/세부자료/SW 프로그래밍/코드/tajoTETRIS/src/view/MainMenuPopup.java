package view;

import java.awt.Color;
//사각형 그리기 위한 자바 기본 클래스
import java.awt.Graphics;
//버튼 클릭 시 발생하는 이벤트를 넣기 위한 자바 기본 클래스
import java.awt.event.ActionEvent;
//버튼 클릭의 이벤트를 받는 메소드 사용하기 위한 자바 기본 클래스
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//버튼 생성을 위한 자바 기본 클래스
import javax.swing.JButton;
//팝업 창을 띄우는 자바 기본 클래스
import javax.swing.JDialog;
//창 닫기 작업을 제어하기위한 자바 기본 클래스
import javax.swing.WindowConstants;

//화면 중앙으로 띄우기 위해 선언해둔 클래스 import
import util.ScreenUtil;

public class MainMenuPopup extends JDialog {

	//각 버튼 함수
	private JButton mjbStart;
	
	private JButton mjbExit;

	public MainMenuPopup() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	//Mainmenu에 대한 함수
	private void initWholeSetting() {
		setTitle("TETRIS - MAIN MENU");
		setModal(true);
		setLayout(null);
		setSize(500, 400); 
		setLocation(ScreenUtil.getCenterPosition(this));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	//버튼 크기와 위치에 대한 함수
	private void initMembers() {
		MyMouseListener listener = new MyMouseListener();
		mjbStart = new JButton("S T A R T");
		mjbStart.setBounds(150, 230, 200, 30);
		mjbStart.setBackground(Color.WHITE);
		mjbStart.addMouseListener(listener);
		add(mjbStart);
		
		mjbExit = new JButton("E X I T");
		mjbExit.setBounds(150, 260, 200, 30);
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
			JButton mjbStart = (JButton)e.getSource();
			
			JButton mjbExit = (JButton)e.getSource();
			mjbStart.setBackground(Color.YELLOW);
			
			mjbExit.setBackground(Color.YELLOW);
		}
	
		@Override
		public void mouseExited(MouseEvent e) {
			JButton mjbStart = (JButton)e.getSource();
			
			JButton mjbExit = (JButton)e.getSource();
			mjbStart.setBackground(Color.WHITE);
			
			mjbExit.setBackground(Color.WHITE);
		}
	}
	
	//버튼 이벤트 출력하는 함수
	private void setEvents() {
		mjbStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new TetrisView().setVisible(true);
			}
		});
		
		mjbExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	//TETRIS 표현
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// T
		g.setColor(Color.RED);
		g.fill3DRect(40, 70, 20, 20, true);
		g.fill3DRect(60, 70, 20, 20, true);
		g.fill3DRect(80, 70, 20, 20, true);
		g.fill3DRect(60, 90, 20, 20, true);
		g.fill3DRect(60, 110, 20, 20, true);
		g.fill3DRect(60, 130, 20, 20, true);
		g.fill3DRect(60, 150, 20, 20, true);

		// E
		g.setColor(Color.ORANGE);
		g.fill3DRect(120, 70, 20, 20, true);
		g.fill3DRect(140, 70, 20, 20, true);
		g.fill3DRect(160, 70, 20, 20, true);
		g.fill3DRect(120, 90, 20, 20, true);
		g.fill3DRect(120, 110, 20, 20, true);
		g.fill3DRect(140, 110, 20, 20, true);
		g.fill3DRect(160, 110, 20, 20, true);
		g.fill3DRect(120, 130, 20, 20, true);
		g.fill3DRect(120, 150, 20, 20, true);
		g.fill3DRect(140, 150, 20, 20, true);
		g.fill3DRect(160, 150, 20, 20, true);

		// T
		g.setColor(Color.YELLOW);
		g.fill3DRect(200, 70, 20, 20, true);
		g.fill3DRect(220, 70, 20, 20, true);
		g.fill3DRect(240, 70, 20, 20, true);
		g.fill3DRect(220, 90, 20, 20, true);
		g.fill3DRect(220, 110, 20, 20, true);
		g.fill3DRect(220, 130, 20, 20, true);
		g.fill3DRect(220, 150, 20, 20, true);

		// R
		g.setColor(Color.GREEN);
		g.fill3DRect(280, 70, 20, 20, true);
		g.fill3DRect(300, 70, 20, 20, true);
		g.fill3DRect(280, 90, 20, 20, true);
		g.fill3DRect(310, 90, 20, 20, true);
		g.fill3DRect(280, 110, 20, 20, true);
		g.fill3DRect(300, 110, 20, 20, true);
		g.fill3DRect(280, 130, 20, 20, true);
		g.fill3DRect(310, 130, 20, 20, true);
		g.fill3DRect(280, 150, 20, 20, true);
		g.fill3DRect(320, 150, 20, 20, true);

		// I
		g.setColor(Color.BLUE);
		g.fill3DRect(360, 70, 20, 20, true);
		g.fill3DRect(360, 90, 20, 20, true);
		g.fill3DRect(360, 110, 20, 20, true);
		g.fill3DRect(360, 130, 20, 20, true);
		g.fill3DRect(360, 150, 20, 20, true);

		// S
		g.setColor(Color.PINK);
		g.fill3DRect(410, 70, 20, 20, true);
		g.fill3DRect(430, 70, 20, 20, true);
		g.fill3DRect(400, 90, 20, 20, true);
		g.fill3DRect(400, 110, 20, 20, true);
		g.fill3DRect(420, 110, 20, 20, true);
		g.fill3DRect(440, 110, 20, 20, true);
		g.fill3DRect(440, 130, 20, 20, true);
		g.fill3DRect(430, 150, 20, 20, true);
		g.fill3DRect(410, 150, 20, 20, true);
	}
}