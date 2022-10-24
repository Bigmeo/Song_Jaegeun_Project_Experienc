package view;

//테트리스 판 색을 입히기 위한 자바 기본 클래스
import java.awt.Color;
//화면에 글씨를 넣기 위한 자바 기본 클래스
import java.awt.Font;
//줄 소거 시 사각형들의 색 변화를 위한 자바 기본 클래스
import java.awt.Graphics;
//프레임 이미지 크기 설정을 위한 자바 기본 클래스
import java.awt.Image;
//키를 눌러 발생하는 이벤트를 넣기 위한 자바 기본 클래스
import java.awt.event.KeyEvent;
//키의 이벤트를 받는 메소드 사용하기 위한 자바 기본 클래스
import java.awt.event.KeyListener;
//프레임 창을 끌 때 사용하기 위한 자바 기본 클래스
import java.awt.event.WindowAdapter;
//윈도우 창 상태가 바뀌는 이벤트를 나타내기 위한 자바 기본 클래스
import java.awt.event.WindowEvent;
//음악 파일을 가져오기 위한 자바 기본 클래스
import java.io.File;
//IO에 대한 예외를 처리하기 위한 자바 기본 클래스
import java.io.IOException;

//음악 관련 처리를 위한 자바 기본 클래스
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//윈도우 창을 불러오기 위한 자바 기본 클래스
import javax.swing.JFrame;

//선언만 해둔 변수들을 정의 및 사용하기 위해 import
import constant.Constant;
//선언해둔 테트리스 판 상태를 사용하기 위해 import
import controller.TetrisManager;
//화면 중앙으로 띄우기 위해 선언해둔 클래스 import
import util.ScreenUtil;

//테트리스 구동
public class TetrisView extends JFrame {

	private final Object mMonitorObject = new Object(); // to pause <-> resume

	private static final String TETRIS_BACKGROUND_MUSIC_FILE_NAME = ".\\res\\tetris_background_music.wav";
	private static final int PROCESS_REACHED_CASE_COUNT = 2;

	private long mCurrentTimeMilliSecond;
	private Clip mSoundClip;
	private TetrisManager mTetrisManager;
	private Constant.ProcessType mProcessType;
	private int mDirection;
	private Constant.GameStatus mGameStatus;
	private boolean mIsKeyPressed;
	private int mProcessReachedCaseCount; 

	public TetrisView() {
		initWholeSetting();
		initMembers();
		setEvents();
	}

	//시작할 때 음악 재생을 위한 함수
	public void start() {
		mSoundClip.start();
		mSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
		repaint();
	}

	//블럭 낙하 상태에 따른 함수 정의
	public void process(Constant.ProcessType processType, int direction) {
		if (processType == Constant.ProcessType.DIRECTION) {
			mTetrisManager.changeBoardByDirection(direction);
		} else if (processType == Constant.ProcessType.DIRECT_DOWN) {
			mTetrisManager.processDirectDown();
		} else if (processType == Constant.ProcessType.AUTO) {
			mTetrisManager.changeBoardByAuto();
		}
		//블럭이 바닥에 닿았는지 체크 후 블럭 처리
		if (mTetrisManager.isReachedToBottom()) {
			if (processType == Constant.ProcessType.DIRECT_DOWN) {
				mProcessReachedCaseCount = 0;
				if (mTetrisManager.processReachedCase() == Constant.GameStatus.END) {
					end();
					new EndMenuPopup().setVisible(true);
					return;
				}
			} else {
				//블록이 fix될 자리가 없을 때
				if (mProcessReachedCaseCount == PROCESS_REACHED_CASE_COUNT) {
					if (mTetrisManager.processReachedCase() == Constant.GameStatus.END) {
						//종료 로직 수행
						end();
						new EndMenuPopup().setVisible(true);
						return;
					}
					mProcessReachedCaseCount = 0;
				} else {
					mProcessReachedCaseCount++;
				}
			}
		}
		repaint();
		mTetrisManager.processDeletingLines(getGraphics());
	}
	
	//종료 로직
	public void end() {
		mGameStatus = Constant.GameStatus.END;
		mSoundClip.stop();
		dispose();
	}
	
	//일시정지 로직(콘솔이 바로 닫히지 않도록 프로그램 일시정지)
	public void pause() {
		mGameStatus = Constant.GameStatus.PAUSE;
		mSoundClip.stop();
		new PauseMenuPopup(this).setVisible(true);
		synchronized (mMonitorObject) {
			mMonitorObject.notify();
		}
		if (mGameStatus != Constant.GameStatus.END) {
			mGameStatus = Constant.GameStatus.PLAYING;
			mSoundClip.start();
			mSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	//테트리스 판 검정틀과 글씨 추가에 대한 그래픽 함수
	@Override
	public void paint(Graphics g) {
		Image buffer = createImage(getWidth(), getHeight());
		Graphics graphics = buffer.getGraphics();
		graphics.setColor(Color.black);
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), Font.BOLD, 30));
		mTetrisManager.print(graphics);
		g.drawImage(buffer, 0, 0, this);
	}

	//창 크기 세팅 및 창이 화면 중앙으로 오도록 하는 함수
	private void initWholeSetting() {
		setTitle("TETRIS - ING...");
		getContentPane().setLayout(null);
		setSize(800, 700);
		setLocation(ScreenUtil.getCenterPosition(this));
		setResizable(false);
	}

	//음악파일 처리 함수
	private void initMembers() {
		try {
			mSoundClip = AudioSystem.getClip();
			mSoundClip.open(AudioSystem.getAudioInputStream(new File(
					TETRIS_BACKGROUND_MUSIC_FILE_NAME)));
		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedAudioFileException uafe) {
			uafe.printStackTrace();
		}
		mTetrisManager = new TetrisManager(Constant.MIN_SPEED_LEVEL);
		new Thread(new Runnable() {

			//게임 진행 중 게임 상태에 대한 함수 정의
			@Override
			public void run() {
				start();
				mGameStatus = Constant.GameStatus.PLAYING;
				mIsKeyPressed = false;
				mCurrentTimeMilliSecond = System.currentTimeMillis();
				//게임상태가 end가 아니고 일시정지인 상태
				while (mGameStatus != Constant.GameStatus.END) {
					if (mGameStatus == Constant.GameStatus.PAUSE) {
						//스레드 동기화(해당 작업이 끝날 때까지는 타스레드가 사용 불가능)
						synchronized (mMonitorObject) {
							try {
								//일시정지 동안 객체 대기
								mMonitorObject.wait();
							} catch (InterruptedException e) {

							}
						}
					}
					mProcessType = Constant.ProcessType.AUTO;
					mDirection = Constant.Direction.DOWN;
					while (true) {
						if (mIsKeyPressed) {
							mIsKeyPressed = false;
							break;
						}
						if (!mIsKeyPressed
								&& System.currentTimeMillis()
										- mCurrentTimeMilliSecond > getDownMilliSecond()) {
							mProcessType = Constant.ProcessType.AUTO;
							mDirection = Constant.Direction.DOWN;
							mCurrentTimeMilliSecond = System
									.currentTimeMillis();
							break;
						}
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					process(mProcessType, mDirection);
				}
			}
		}).start();
	}

	//키보드 입력 시 발생하는 이벤트
	/* 1. keyPressed : key 누르는 순간 발생
	 * 2. keyTyped : 누른 key를 떼는 순간 발생(유니코드 키의 경우 추가 이벤트 발생할 수 있음)
	 * 3. keyReleased : 누른 key를 떼는 순간
	 *  - 실행 순서 : keyPressed -> keyTyped -> keyReleased
	 */
	private void setEvents() {
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			//키를 누르는 순간 발생하는 이벤트
			@Override
			public void keyPressed(KeyEvent e) {
				//위
				if (e.getKeyCode() == Constant.KeyCode.UP) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.UP;
				//아래
				} else if (e.getKeyCode() == Constant.KeyCode.DOWN) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.DOWN;
				//왼쪽
					mCurrentTimeMilliSecond = System.currentTimeMillis();
				} else if (e.getKeyCode() == Constant.KeyCode.LEFT) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.LEFT;
				//오른쪽
				} else if (e.getKeyCode() == Constant.KeyCode.RIGHT) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECTION;
					mDirection = Constant.Direction.RIGHT;
				//바로 낙하
				} else if (e.getKeyCode() == Constant.KeyCode.SPACE_BAR) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.DIRECT_DOWN;
					mCurrentTimeMilliSecond = System.currentTimeMillis();
				//일시 정지
				} else if (e.getKeyCode() == Constant.KeyCode.ESC) {
					mIsKeyPressed = true;
					mProcessType = Constant.ProcessType.AUTO;
					mCurrentTimeMilliSecond = System.currentTimeMillis();
					pause();
				}
			}
		});
		//프레임 창을 끌 때 발생하는 이벤트
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				end();
			}
		});
	}

	public long getDownMilliSecond() {
		return mTetrisManager.getDownMilliSecond();
	}
}