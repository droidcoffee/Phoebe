package phoebe.frame.view;

import java.lang.ref.WeakReference;

import phoebe.frame.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class MagicProgressCircle extends View implements ISmoothTarget {

	// ColorInt
	private Integer startColor;
	// ColorInt
	private Integer endColor;
	// ColorInt
	private int defaultColor;
	// 渐变颜色数组
	private int[] customColors;

	private int strokeWidth;
	private float percent;

	// 用于渐变
	private Paint paint;
	// 圆点
	private Paint circlePaint;

	private SmoothHandler smoothHandler;

	public MagicProgressCircle(Context context) {
		super(context);
		init(context, null);
	}

	public MagicProgressCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MagicProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	// public MagicProgressCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	// super(context, attrs, defStyleAttr, defStyleRes);
	// init(context, attrs);
	// }

	private int dp2px(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density + 0.5F);
	}

	private void init(final Context context, final AttributeSet attrs) {
		float defaultPercent = -1;
		if (isInEditMode()) {
			defaultPercent = 0.6f;
		}

		final int strokeWdithDefaultValue = (int) (4 * getResources().getDisplayMetrics().density + 0.5f);
		startColor = android.R.color.holo_red_light;
		endColor = android.R.color.holo_green_light;
		defaultColor = android.R.color.holo_blue_bright;
		if (context == null || attrs == null) {
			strokeWidth = strokeWdithDefaultValue;
			percent = defaultPercent;
		} else {
			TypedArray typedArray = null;
			try {
				typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicProgressCircle);
				percent = typedArray.getFloat(R.styleable.MagicProgressCircle_mpc_percent, defaultPercent);
				strokeWidth = (int) typedArray.getDimension(R.styleable.MagicProgressCircle_mpc_stroke_width, strokeWdithDefaultValue);
				startColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_start_color, startColor);
				endColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_end_color, endColor);
				defaultColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_default_color, defaultColor);
			} finally {
				if (typedArray != null) {
					typedArray.recycle();
				}
			}
		}

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(endColor);
		circlePaint.setStyle(Paint.Style.FILL);
		//
		customColors = new int[] { startColor, endColor, endColor, startColor, defaultColor, defaultColor };
	}

	/**
	 * @param percent
	 *            FloatRange(from = 0.0, to = 1.0)
	 */
	public void setPercent(float percent) {
		percent = Math.min(1, percent);
		percent = Math.max(0, percent);

		if (smoothHandler != null) {
			smoothHandler.commitPercent(percent);
		}

		if (this.percent != percent) {
			this.percent = percent;
			invalidate();
		}
	}

	public float getPercent() {
		return this.percent;
	}

	/**
	 * @param color
	 *            ColorInt
	 */
	public void setStartColor(final int color) {
		if (this.startColor != color) {
			this.startColor = color;
			customColors = new int[] { startColor, endColor, endColor, startColor, defaultColor, defaultColor };
			invalidate();
		}
	}

	/**
	 * @param color
	 *            ColorInt
	 */
	public void setEndColor(final int color) {
		if (this.endColor != color) {
			this.endColor = color;
			customColors = new int[] { startColor, endColor, endColor, startColor, defaultColor, defaultColor };
			circlePaint.setColor(endColor);
			invalidate();
		}
	}

	/**
	 * @param color
	 *            ColorInt
	 */
	public void setDefaultColor(final int color) {
		if (this.defaultColor != color) {
			this.defaultColor = color;
			// 渐变后半部分
			customColors[2] = color;
			customColors[3] = color;

			invalidate();
		}
	}

	/**
	 * @param width
	 *            px
	 */
	public void setStrokeWidth(final int width) {
		if (this.strokeWidth != width) {
			this.strokeWidth = width;
			// 画描边的描边变化
			paint.setStrokeWidth(width);
			// 会影响measure
			requestLayout();
		}
	}

	// 目前由于SweepGradient赋值只在构造函数，无法pre allocate & reuse instead
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final int cx = getMeasuredWidth() / 2;
		final int cy = getMeasuredHeight() / 2;
		// 半径 - 4 两边留着
		final int radius = getMeasuredWidth() / 2 - strokeWidth / 2 - dp2px(getPaddingTop());

		float[] positions = new float[] { 0, percent / 2 + 0.01F, percent / 2, percent, percent, 1F };
		// positions = null;
		// SweepGradient 默认从0°(时钟3点那个方向 水平逆时针渐变)
		final SweepGradient sweepGradient = new SweepGradient(cx, cy, customColors, positions);
		Matrix matrix = new Matrix();
		matrix.setRotate(-90, cx, cy);// 以cx,xy为中心轴，逆时针旋转90
		sweepGradient.setLocalMatrix(matrix);
		//
		paint.setShader(sweepGradient);
		canvas.drawCircle(cx, cy, radius, paint);

		// 绘制进度的半圆
		if (circlePaint != null) {
			if (this.percent <= 1) {
				circlePaint.setStrokeWidth(strokeWidth * 2);
				// circlePaint.setColor(Color.RED);
				float angle = this.percent * 360;
				int x1 = (int) (cx + radius * Math.sin(Math.toRadians(angle)));
				int y1 = (int) (cy - radius * Math.cos(Math.toRadians(angle)));

				canvas.drawCircle(x1, y1, strokeWidth * 2, circlePaint);
				// canvas.restore();
			}
		}
		canvas.save();
	}

	/**
	 * 设置"点点"是否显示
	 */
	public void setCircelShow(boolean visible) {
		if (visible == false) {
			circlePaint = null;
			invalidate();
		} else {
			// 默认就是显示的
		}
	}

	/*************** 平滑转动start ***************/
	@Override
	public void setSmoothPercent(float percent) {
		getSmoothHandler().loopSmooth(percent);
	}

	@Override
	public void setSmoothPercent(float percent, long durationMillis) {
		getSmoothHandler().loopSmooth(percent, durationMillis);
	}

	private SmoothHandler getSmoothHandler() {
		if (smoothHandler == null) {
			smoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
		}
		return smoothHandler;
	}
	/*************** 平滑转动end ***************/

}
