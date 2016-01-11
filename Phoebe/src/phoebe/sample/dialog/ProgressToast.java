package phoebe.sample.dialog;

import phoebe.frame.util.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 
 * @author coffee <br>
 *         2016-1-11下午12:20:19
 */
public class ProgressToast extends View {

	/**
	 * 0画圆, 1绿色勾
	 */
	private int status = -1;

	/**
	 * 当前进度
	 */
	private int progress = 0;

	/**
	 * 最大进度
	 */
	private static final int maxProgress = 100;

	/**
	 * 打钩
	 */
	private ValueAnimator mTickAnimation;

	private RectF mRectF = new RectF();
	private Paint circlePaint = new Paint();
	private Paint tickPaint = new Paint();
	/**
	 * 画笔宽度
	 */
	private int strokeWidth = 20;
	// /**
	// *
	// */
	// private float radius = 0;

	/**
	 * 测量打钩
	 */
	private PathMeasure tickPathMeasure;

	/**
	 * 打钩百分比
	 * 
	 * @param context
	 */
	private float tickPrecent = 0;

	public ProgressToast(Context context) {
		super(context);
		init();
	}

	public ProgressToast(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressToast(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//
		mRectF.set(new RectF(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth));
		//
		int centerX = getMeasuredHeight() / 2;
		int centerY = getMeasuredWidth() / 2;

		// 初始化打钩路径
		Path tickPath = new Path();
		tickPath.moveTo(centerX - centerX / 4, centerY - centerY / 4);
		tickPath.lineTo(centerX, centerY + centerY / 8);
		tickPath.lineTo(centerY + centerX * 2 / 4, centerY - centerY * 2 / 4);
		tickPathMeasure = new PathMeasure(tickPath, false);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(widthSpecSize + 10 * strokeWidth, heightSpecSize + 10 * strokeWidth);
	}

	private void init() {
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(Color.argb(255, 48, 63, 159));
		circlePaint.setStrokeWidth(strokeWidth);
		circlePaint.setStyle(Paint.Style.STROKE);

		tickPaint.setColor(Color.argb(255, 0, 150, 136));
		tickPaint.setAntiAlias(true);
		tickPaint.setStrokeWidth(strokeWidth);
		tickPaint.setStyle(Paint.Style.STROKE);

		// 打钩动画
		mTickAnimation = ValueAnimator.ofFloat(0f, 1f);
		mTickAnimation.setStartDelay(1000);
		mTickAnimation.setDuration(500);
		mTickAnimation.setInterpolator(new AccelerateInterpolator());
		mTickAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				tickPrecent = (Float) animation.getAnimatedValue();
				invalidate();
			}
		});

		mTickAnimation.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				status = 1;
			}
		});

	}

	/**
	 * 绘制打钩
	 * 
	 * @param canvas
	 */
	private void drawTick(Canvas canvas) {
		Path path = new Path();
		/*
		 * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas. A simple workaround is to add a single operation to this path, such as dst.rLineTo(0,
		 * 0).
		 */
		tickPathMeasure.getSegment(0, tickPrecent * tickPathMeasure.getLength(), path, true);
		path.rLineTo(0, 0);
		canvas.drawPath(path, tickPaint);
		canvas.drawArc(mRectF, 0, 360, false, tickPaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		switch (status) {
		case 0:
			float precent = 1.0f * progress / maxProgress;
			canvas.drawArc(mRectF, -90 - 270 * precent, -(60 + precent * 300), false, circlePaint);
			// canvas.drawArc(mRectF, -90, -90 + progress, false, circlePaint);
			startAnimation(progress += 3);
			Log.d("pregress", progress);
			break;
		case 1:
			drawTick(canvas);
			break;
		}
	}

	/**
	 * 开启动画
	 * 
	 * @param progress
	 *            圆形动画的进度值
	 */
	public void startAnimation(int progress) {
		postInvalidate();
		if (progress < maxProgress) {
			status = 0;
		} else if (progress >= maxProgress) {
			status = 1;
			mTickAnimation.start();
		}
	}

}
