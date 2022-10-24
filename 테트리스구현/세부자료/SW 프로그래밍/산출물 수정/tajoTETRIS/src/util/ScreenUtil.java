package util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;


public class ScreenUtil {

	//모니터 중앙 값을 찾아서 창을 모니터 중앙에 띄우는 함수
	public static Point getCenterPosition(Window window) {
		Dimension wholeScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension currentWindowSize = window.getSize();
		int left = (wholeScreenSize.width / 2) - (currentWindowSize.width / 2);
		int top = (wholeScreenSize.height / 2) - (currentWindowSize.height / 2);
		return new Point(left, top);
	}
}

