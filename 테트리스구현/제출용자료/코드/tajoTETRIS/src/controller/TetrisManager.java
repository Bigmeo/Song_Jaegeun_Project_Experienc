package controller;

//블럭 색상 입력에 필요한 자바 기본 클래스
import java.awt.Color;
//인게임 화면 우측 문자 추가 시 필요한 자바 기본 클래스
import java.awt.Font;
//블럭 소거 시 반짝임 효과를 주고  위한 자바 기본 클래스
import java.awt.Graphics;
//키를 눌러 발생하는 이벤트를 넣기 위한 자바 기본 클래스
import java.awt.event.ActionEvent;
//키의 이벤트를 받는 메소드 사용하기 위한 자바 기본 클래스
import java.awt.event.ActionListener;
//테트리스 판을 만들 때 배열로 만들기 위한 자바 기본 클래스
import java.util.ArrayList;
//판제작 시 객체 배열과 관련한 기능을 사용하기 위한 자바 기본 클래스
import java.util.Arrays;

//줄 소거 시 하이라이트 효과를 주기 위한 자바 기본 클래스
import javax.swing.Timer;

//선언만 해둔 변수들을 정의 및 사용하기 위해 import
import constant.Constant;
import constant.Constant.BoardType;
import constant.Constant.GameStatus;
//정의해둔 블럭을 사용하기 위해 import
import block.Block;

//테트리스를 구성하는 클래스
public class TetrisManager {

	//테트리스 판, 블럭 속도, 줄 소거 시 효과들을 위한 변수 선언
	private static final int POSITIONS_SIZE = 4;
	private static final int BOARD_ROW_SIZE = 24;
	private static final int BOARD_COL_SIZE = 14;
	private static final int INITIAL_SPEED = 300;
	private static final int SPEED_LEVEL_OFFSET = 40;
	private static final int LEVEL_UP_CONDITION = 3;
	private static final int LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND = 10;

	private Constant.BoardType mBoard[][];
	private Block mBlock;
	private int mDeletedLineCount;
	private int mSpeedLevel;
	private int mColorIndex = 0; 
	
	
	public TetrisManager(int speedLevel) {
		mBoard = new Constant.BoardType[BOARD_ROW_SIZE][BOARD_COL_SIZE];
		
		//BOARD_ROW_SIZE 배열을 반복문을 돌려 mBoard[i]를 BoardType.EMPY로 초기화 시킴
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			Arrays.fill(mBoard[i], BoardType.EMPTY);
		}
		//테트리스 판, 블럭, 소거라인 수, 낙하 속도 초기화
		clearBoard();
		mBlock = new Block(null);
		mDeletedLineCount = 0;
		mSpeedLevel = speedLevel;
	}

	
	public Constant.BoardType checkValidPosition(int direction) {
		Block temp = new Block();
		temp.copyOf(mBlock);
		temp.move(direction);
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = temp.getPositions()[temp.getDirection()][i].x;
			int y = temp.getPositions()[temp.getDirection()][i].y;

			//
			if (x <= 0) {
				return Constant.BoardType.TOP_WALL;
			}

			if (!(mBoard[x][y] == Constant.BoardType.EMPTY || mBoard[x][y] == Constant.BoardType.MOVING_BLOCK)) {
				return mBoard[x][y];
			}
		}
		return Constant.BoardType.EMPTY;
	}
	// 블럭이 벽을 넘을 수 있는 상황에 대한 함수 정의
	public void changeBoardByDirection(int direction) {
		int tempDirection = Constant.Direction.DOWN;
		Constant.BoardType tempCheckResult = Constant.BoardType.EMPTY;
		clearBoard();
		Constant.BoardType checkResult = checkValidPosition(direction);
		if (checkResult == Constant.BoardType.EMPTY) {
			mBlock.move(direction);
		} else {
			if (direction == Constant.Direction.UP
					&& checkResult != Constant.BoardType.FIXED_BLOCK) {
				if (checkResult == Constant.BoardType.TOP_WALL) {
					tempDirection = Constant.Direction.DOWN;
					tempCheckResult = Constant.BoardType.TOP_WALL;
				} else if (checkResult == Constant.BoardType.RIGHT_WALL) {
					tempDirection = Constant.Direction.LEFT;
					tempCheckResult = Constant.BoardType.RIGHT_WALL;
				} else if (checkResult == Constant.BoardType.LEFT_WALL) {
					tempDirection = Constant.Direction.RIGHT;
					tempCheckResult = Constant.BoardType.LEFT_WALL;
				}
				do {
					mBlock.move(tempDirection);
				} while (checkValidPosition(direction) == tempCheckResult);
				mBlock.move(direction);
			}
		}
		changeBoardByStatus(Constant.BoardType.MOVING_BLOCK);
	}

	public void changeBoardByAuto() {
		changeBoardByDirection(Constant.Direction.DOWN);
	}

	public void processDirectDown() {
		while (!isReachedToBottom()) {
			changeBoardByDirection(Constant.Direction.DOWN);
		}
	}

	//줄 소거에 대한 함수 정의
	public void processDeletingLines(Graphics g) {
		Color highlightingColors[] = { Color.GRAY, Color.WHITE };
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		searchLineIndexesToDelete(indexes);
		if (indexes.size() > 0) {
			Timer timer = new Timer(LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND,
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							highlightLinesToDelete(g,
									highlightingColors[mColorIndex], indexes);
							mColorIndex = 1 - mColorIndex;
						}
					});
			timer.start();
			try {
				Thread.sleep(LINES_TO_DELETE_HIGHLIGHTING_MILLISECOND * 40);
			} catch (InterruptedException e1) {

			}
			timer.stop();
			deleteLines(indexes);
			for (int i = mSpeedLevel; i <= mDeletedLineCount
					/ LEVEL_UP_CONDITION; i++) {
				upSpeedLevel();
			}
		}
	}
	
	//블럭 움직임 범위 함수 정의
	public boolean isReachedToBottom() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = mBlock.getPositions()[mBlock.getDirection()][i].x;
			int y = mBlock.getPositions()[mBlock.getDirection()][i].y;
			if (mBoard[x + 1][y] != Constant.BoardType.EMPTY
					&& mBoard[x + 1][y] != Constant.BoardType.MOVING_BLOCK) {
				return true;
			}
		}
		return false;
	}
	
	//fix된 블럭의 높이에 따른 게임 상태 함수 정의
	public Constant.GameStatus processReachedCase() {
		changeBoardByStatus(Constant.BoardType.FIXED_BLOCK);
		mBlock = new Block(mBlock);
		if (isReachedToBottom()) {
			return GameStatus.END;
		} else {
			return GameStatus.PLAYING;
		}
	}

	public void sleep() {
		try {
			Thread.sleep(getDownMilliSecond());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//오른쪽에 다음 나온 테트리미노 출력
	public void print(Graphics graphics) {
		int x;
		int y = 60;
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			x = 30;
			for (int j = 0; j < BOARD_COL_SIZE; j++) {
				switch (mBoard[i][j]) {
				case LEFT_TOP_EDGE:
				case RIGHT_TOP_EDGE:
				case LEFT_BOTTOM_EDGE:
				case RIGHT_BOTTOM_EDGE:
				case LEFT_WALL:
				case RIGHT_WALL:
				case TOP_WALL:
				case BOTTOM_WALL:
					graphics.fill3DRect(x, y, 25, 25, true);
					break;
				case EMPTY:
					break;
				case MOVING_BLOCK:
					graphics.setColor(Constant.COLORS[mBlock.getColor()]);
					graphics.fill3DRect(x, y, 25, 25, true);
					graphics.setColor(Color.BLACK);
					break;
				case FIXED_BLOCK:
					graphics.setColor(Color.GRAY);
					graphics.fill3DRect(x, y, 25, 25, true);
					graphics.setColor(Color.BLACK);
					break;
				}
				x += 25;
			}
			y += 25;
		}
		
		//조작키 설명
		x = 460;
		y = 150;
		Font font = graphics.getFont();
		graphics.setFont(new Font(font.getName(), Font.BOLD, 20));
		
		y += 80;
		graphics.drawString("[Key Description]", x, y);
		y += 30;
		graphics.drawString("←", x, y);
		x = 560;
		graphics.drawString(": 왼쪽 이동", x, y);
		x = 460;
		y += 30;
		graphics.drawString("→", x, y);
		x = 560;
		graphics.drawString(": 오른쪽 이동", x, y);
		x = 460;
		y += 30;
		graphics.drawString("↓", x, y);
		x = 560;
		graphics.drawString(": 아래 이동", x, y);
		x = 460;
		y += 30;
		graphics.drawString("↑", x, y);
		x = 560;
		graphics.drawString(": 블럭 회전", x, y);
		x = 460;
		y += 30;
		graphics.drawString("SpaceBar", x, y);
		x = 560;
		graphics.drawString(": 블럭 낙하", x, y);
		x = 460;
		y += 30;
		graphics.drawString("ESC", x, y);
		x = 560;
		graphics.drawString(": pause", x, y);
		x = 460;
		mBlock.printNext(graphics, x, y + 80);
	}

	public void setBoard(Constant.BoardType[][] board) {
		mBoard = board;
	}

	public Constant.BoardType[][] getBoard() {
		return mBoard;
	}

	public void setBlock(Block block) {
		mBlock = block;
	}

	public Block getBlock() {
		return mBlock;
	}

	public void setDeletedLineCount(int deletedLineCount) {
		mDeletedLineCount = deletedLineCount;
	}

	public int getDeletedLineCount() {
		return mDeletedLineCount;
	}

	public void setSpeedLevel(int speedLevel) {
		mSpeedLevel = speedLevel;
	}

	public int getSpeedLevel() {
		return mSpeedLevel;
	}

	//블럭 떨어지는 속도 조절
	public long getDownMilliSecond() {
		long milliSecond = INITIAL_SPEED;
		for (int i = Constant.MIN_SPEED_LEVEL; i < mSpeedLevel; i++) {
			if (i < Constant.MAX_SPEED_LEVEL / 2) {
				milliSecond -= SPEED_LEVEL_OFFSET;
			} else {
				milliSecond -= (SPEED_LEVEL_OFFSET / 5);
			}
		}
		return milliSecond;
	}
	//블록이 없는 테트리스 판에 대한 함수 정의
	private void clearBoard() {
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			mBoard[i][0] = Constant.BoardType.LEFT_WALL;
			mBoard[i][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_WALL;
		}
		for (int i = 0; i < BOARD_COL_SIZE; i++) {
			mBoard[0][i] = Constant.BoardType.TOP_WALL;
			mBoard[BOARD_ROW_SIZE - 1][i] = Constant.BoardType.BOTTOM_WALL;
		}
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				if (mBoard[i][j] != Constant.BoardType.FIXED_BLOCK) {
					mBoard[i][j] = Constant.BoardType.EMPTY;
				}
			}
		}
		mBoard[0][0] = Constant.BoardType.LEFT_TOP_EDGE;
		mBoard[0][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_TOP_EDGE;
		mBoard[BOARD_ROW_SIZE - 1][0] = Constant.BoardType.LEFT_BOTTOM_EDGE;
		mBoard[BOARD_ROW_SIZE - 1][BOARD_COL_SIZE - 1] = Constant.BoardType.RIGHT_BOTTOM_EDGE;
	}

	//블럭 모양 바꿔진 상태
	private void changeBoardByStatus(Constant.BoardType status) {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			int x = mBlock.getPositions()[mBlock.getDirection()][i].x;
			int y = mBlock.getPositions()[mBlock.getDirection()][i].y;
			mBoard[x][y] = status;
		}
	}

	//블럭 떨어지는 속도 증가시킴
	private void upSpeedLevel() {
		if (mSpeedLevel < Constant.MAX_SPEED_LEVEL) {
			mSpeedLevel++;
		}
	}

	//아래에 쌓인 블럭 중 지워진 블럭을 검색
	private void searchLineIndexesToDelete(ArrayList<Integer> indexes) {
		indexes.clear();
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			boolean toDelete = true;
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				if (mBoard[i][j] != Constant.BoardType.FIXED_BLOCK) {
					toDelete = false;
					break;
				}
			}
			if (toDelete) {
				indexes.add(i);
			}
		}
	}

	//아래에 쌓인 블럭 지움
	private void deleteLines(ArrayList<Integer> indexes) {
		int k = BOARD_ROW_SIZE - 2;
		Constant.BoardType[][] temp = new Constant.BoardType[BOARD_ROW_SIZE][BOARD_COL_SIZE];
		for (int i = 0; i < BOARD_ROW_SIZE; i++) {
			Arrays.fill(temp[i], Constant.BoardType.EMPTY);
		}
		for (int i = BOARD_ROW_SIZE - 2; i > 0; i--) {
			boolean toDelete = false;
			for (int j = 0; j < indexes.size(); j++) {
				if (i == indexes.get(j)) {
					toDelete = true;
					break;
				}
			}
			if (!toDelete) {
				for (int j = 0; j < BOARD_COL_SIZE; j++) {
					temp[k][j] = mBoard[i][j];
				}
				k--;
			}
		}
		for (int i = 1; i < BOARD_ROW_SIZE - 1; i++) {
			for (int j = 1; j < BOARD_COL_SIZE - 1; j++) {
				mBoard[i][j] = temp[i][j];
			}
		}
		mDeletedLineCount += indexes.size();
	}

	//줄 완성 시 소거되는 줄 반짝임 효과
	private void highlightLinesToDelete(Graphics g, Color color,
			ArrayList<Integer> indexes) {
		g.setColor(color);
		int x = 55;
		int y = 60 + indexes.get(0) * 25;
		g.fill3DRect(x, y, 25 * (BOARD_COL_SIZE - 2), 25 * indexes.size(), true);
	}
}