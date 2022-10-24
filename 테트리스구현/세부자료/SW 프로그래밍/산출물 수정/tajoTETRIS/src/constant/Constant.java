package constant;

import java.awt.Color;

public interface Constant {

	//게임 상태에 대한 변수 선언
	public enum GameStatus {
		PLAYING, END, PAUSE
		}

	//블럭 움직임에 대한 변수 선언 및 데이터 할당
	public interface Direction {
		public static final int SIZE = 4;

		public static final int UP = 0;
		public static final int RIGHT = 1;
		public static final int DOWN = 2;
		public static final int LEFT = 3;
		}

	//블럭이 움직이는 방법들에 대한 변수 선언
	public enum ProcessType {
		DIRECTION, DIRECT_DOWN, AUTO
		}

	//조작키에 대한 자바 키코드 값 할당
	public interface KeyCode {
		public static final int UP = 38;
		public static final int LEFT = 37;
		public static final int RIGHT = 39;
		public static final int DOWN = 40;
		public static final int SPACE_BAR = 32;
		public static final int ESC = 27;
		}

	//테트리스 게임 판을 만들기 위한 변수 선언
	public enum BoardType {
		EMPTY, MOVING_BLOCK, FIXED_BLOCK, LEFT_WALL, RIGHT_WALL, BOTTOM_WALL, TOP_WALL, LEFT_TOP_EDGE, RIGHT_TOP_EDGE, LEFT_BOTTOM_EDGE, RIGHT_BOTTOM_EDGE
		}

	//메인 화면의 버튼에 대한 데이터 할당
	public interface MainMenu {
		public static final int START = 1;
		public static final int EXIT = 2;
		}

	//일시정지 화면의 버튼에 대한 데이터 할당
	public interface PauseMenu {
		public static final int RESUME = 1;
		public static final int MAIN_MENU = 2;
		}

	//종료 화면의 버튼에 대한 데이터 할당
	public interface EndMenu {
		public static final int MAIN_MENU = 1;
		public static final int EXIT = 2;
		}

	//최고 속도와 최저 속도 변수 선언 및 값 할당
	public static final int MAX_SPEED_LEVEL = 10;
	public static final int MIN_SPEED_LEVEL = 1;

	//테트리스에서 사용할 무지개 색 선언
	public static final Color[] COLORS = { Color.RED, Color.ORANGE,
			Color.YELLOW, Color.GREEN, Color.BLUE, Color.decode("#4B0082"),
			Color.decode("#800080") };
	}

