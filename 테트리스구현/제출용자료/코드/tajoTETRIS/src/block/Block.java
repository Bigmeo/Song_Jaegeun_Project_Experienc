package block;

//블록 색과 글씨 색을 위한 자바 기본 클래스
import java.awt.Color;
//다음 블록 예고에 대한 문자 추가를 위한 자바 기본 클래스
import java.awt.Graphics;
//블록 좌표 값을 잡기 위한 자바 기본 클래스
import java.awt.Point;
//블록 랜덤 생성을 위한 자바 기본 클래스
import java.util.Random;

//Constant Class에서 선언한 변수들을 사용하기 위해 import
import constant.Constant;

public class Block {

	//블록 종류와 테트리미노의 크기 값 할당
	private static final int BLOCK_EXAMPLES_SIZE = 7;
	private static final int POSITIONS_SIZE = 4;
	
	
	private static final Point BLOCK_EXAMPLES[][][] = {
			{
					// I형 테트리미노
					{ new Point(0, 5), new Point(0, 6), new Point(0, 7),
							new Point(0, 8) },
					{ new Point(-1, 6), new Point(0, 6), new Point(1, 6),
							new Point(2, 6) },
					{ new Point(0, 5), new Point(0, 6), new Point(0, 7),
							new Point(0, 8) },
					{ new Point(-1, 6), new Point(0, 6), new Point(1, 6),
							new Point(2, 6) } },
			{
					// L형 테트리미노
					{ new Point(0, 8), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 6) },
					{ new Point(-1, 6), new Point(-1, 7), new Point(0, 7),
							new Point(1, 7) } },
			{
					// S형 테트리미노
					{ new Point(0, 7), new Point(0, 8), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(-1, 6), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 7), new Point(0, 8), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(-1, 6), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) } },
			{
					// Z형 테트리미노
					{ new Point(0, 6), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(0, 8), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(0, 8), new Point(0, 7),
							new Point(1, 7) } },
			{
					// T형 테트리미노
					{ new Point(0, 7), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(0, 8),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 7) },
					{ new Point(-1, 7), new Point(0, 6), new Point(0, 7),
							new Point(1, 7) } },
			{
					// J형 테트리미노
					{ new Point(0, 6), new Point(1, 6), new Point(1, 7),
							new Point(1, 8) },
					{ new Point(-1, 8), new Point(-1, 7), new Point(0, 7),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(0, 8),
							new Point(1, 8) },
					{ new Point(-1, 7), new Point(0, 7), new Point(1, 7),
							new Point(1, 6) } },
			{
					// O형 테트리미노
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) },
					{ new Point(0, 6), new Point(0, 7), new Point(1, 6),
							new Point(1, 7) } } };
	
	//블럭 생성과 이동을 위한 변수 선언
	private Point mPositions[][];
	private int mCurrent;
	private int mNext;
	private int mDirection;
	private int mColor;

	public Block() {
		mPositions = new Point[POSITIONS_SIZE][POSITIONS_SIZE];
		mCurrent = 0;
		mNext = 0;
		mDirection = Constant.Direction.UP;
		mColor = 0;
	}

	//나올 블럭을 난수로 출력
	public Block(Block block) {
		if (block == null) {
			mCurrent = new Random().nextInt(BLOCK_EXAMPLES_SIZE);
		} else {
			mCurrent = block.getNext();
		}
		mPositions = new Point[POSITIONS_SIZE][POSITIONS_SIZE];
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j] = new Point(BLOCK_EXAMPLES[mCurrent][i][j]);
			}
		}
		//
		mNext = new Random().nextInt(BLOCK_EXAMPLES_SIZE);
		mDirection = Constant.Direction.UP;
		mColor = new Random().nextInt(Constant.COLORS.length);
	}
	
	public void copyOf(Block src) {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j] = new Point(src.getPositions()[i][j]);
			}
		}
		mCurrent = src.getCurrent();
		mNext = src.getNext();
		mDirection = src.getDirection();
	}
	// move함수 정의
	// direction에서 각 함수를 호출한 후 반환 값 할당
	public void move(int direction) {
		switch (direction) {
		case Constant.Direction.LEFT:
			moveToLeft();
			break;
		case Constant.Direction.RIGHT:
			moveToRight();
			break;
		case Constant.Direction.DOWN:
			moveToDown();
			break;
		case Constant.Direction.UP:
			rotateRight();
			break;
		}
	}

	//인게임 화면 우측 하단에 들어갈 다음 블록 예고에 대한 문자 및 위치 값 설정
	public void printNext(Graphics graphics, int x, int y) {
		graphics.drawString("[Next block]", x, y);
		y += 30;
		graphics.setColor(Color.LIGHT_GRAY);
		switch (mNext) {
		// I형 테트리미노
		case 0:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x + 60, y, 20, 20, true);
			break;
		// L형 테트리미노
		case 1:
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		// S형 테트리미노
		case 2:
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 40, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			break;
		// Z형 테트리미노
		case 3:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		// T형 테트리미노
		case 4:
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		// J형 테트리미노
		case 5:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 40, y + 20, 20, 20, true);
			break;
		// O형 테트리미노
		case 6:
			graphics.fill3DRect(x, y, 20, 20, true);
			graphics.fill3DRect(x + 20, y, 20, 20, true);
			graphics.fill3DRect(x, y + 20, 20, 20, true);
			graphics.fill3DRect(x + 20, y + 20, 20, 20, true);
			break;
		}
		graphics.setColor(Color.BLACK);
	}

	public void setPositions(Point[][] positions) {
		mPositions = positions;
	}

	public Point[][] getPositions() {
		return mPositions;
	}

	public void setCurrent(int current) {
		mCurrent = current;
	}

	public int getCurrent() {
		return mCurrent;
	}

	public void setNext(int next) {
		mNext = next;
	}

	public int getNext() {
		return mNext;
	}

	public void setDirection(int direction) {
		mDirection = direction;
	}

	public int getDirection() {
		return mDirection;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public int getColor() {
		return mColor;
	}

	// ↓ 방향키 입력 시 블럭 이동
	private void moveToDown() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].x++;
			}
		}
	}
	
	// ← 방향키 입력 시 블럭 이동
	private void moveToLeft() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].y--;
			}
		}
	}
	// → 방향키 입력 시 블럭 이동
	private void moveToRight() {
		for (int i = 0; i < POSITIONS_SIZE; i++) {
			for (int j = 0; j < POSITIONS_SIZE; j++) {
				mPositions[i][j].y++;
			}
		}
	}
	// ↑ 방향키 입력 시 블럭 회전
	private void rotateRight() {
		mDirection = (mDirection + 1) % Constant.Direction.SIZE;
	}
}