package phoebe.frame.view;

import phoebe.frame.util.Log;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

	private Paint paint;

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
	}

	private String getModeName(int mode) {
		if (mode == MeasureSpec.AT_MOST) {
			return "AT_MOST";
		} else if (mode == MeasureSpec.EXACTLY) {
			return "EXACTLY";
		} else if (mode == MeasureSpec.UNSPECIFIED) {
			return "UNSPECIFIED";
		} else {
			return "";
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// int mode = MeasureSpec.getMode(widthMeasureSpec);
		// int size = MeasureSpec.getSize(widthMeasureSpec);
		// Log.d("measure_spec", mode + " " + size);
		String widthSpec = MeasureSpec.toString(widthMeasureSpec);// MeasureSpec: EXACTLY 720
		String heightSpec = MeasureSpec.toString(heightMeasureSpec);// MeasureSpec: AT_MOST 535
		Log.d("measure_spec", widthSpec + " " + heightSpec);
		// int width = getMeasuredWidth();
		// int height = getMeasuredHeight();
		// Log.d("measure_spec", width + " " + height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("measure_spec", getMeasuredWidth() + " " + getMeasuredHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawText("hello world", 100, 100, paint);
		super.onDraw(canvas);
	}
}
