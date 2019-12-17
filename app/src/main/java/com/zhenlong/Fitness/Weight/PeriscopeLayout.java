package com.zhenlong.Fitness.Weight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.res.ResourcesCompat;

import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Util.BezierEvaluator;

import java.util.Random;

public class PeriscopeLayout extends RelativeLayout {
    private Interpolator line = new LinearInterpolator();
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dce = new DecelerateInterpolator();
    private Interpolator accdec = new AccelerateDecelerateInterpolator();
    private Interpolator[] interpolators;

    private int mHeight;
    private int mWidth;
    private LayoutParams lp;
    private Drawable[] drawables;
    private Random random = new Random();

    private int dHeight;
    private int dWidth;

    public PeriscopeLayout(Context context) {
        super(context);
        init();
    }

    public PeriscopeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PeriscopeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // 初始化显示的图片
        drawables = new Drawable[3];
//        Drawable red = getResources().getDrawable(R.drawable.pl_red);
//        Drawable yellow = getResources().getDrawable(R.drawable.pl_yellow);
//        Drawable blue = getResources().getDrawable(R.drawable.pl_blue);
        Drawable red = ResourcesCompat.getDrawable(getResources(), R.drawable.star_red, null);
        Drawable yellow = ResourcesCompat.getDrawable(getResources(), R.drawable.star_yellow, null);
        Drawable blue = ResourcesCompat.getDrawable(getResources(), R.drawable.star_blue, null);
        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;

        dHeight = red.getIntrinsicHeight();
        dWidth = red.getIntrinsicWidth();

        lp = new LayoutParams(dWidth, dHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    public void addHeart() {

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[random.nextInt(3)]);
        imageView.setLayoutParams(lp);

        addView(imageView);

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();

    }
    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }
    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f,
                1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X,
                0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y,
                0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }
    private ValueAnimator getBezierValueAnimator(View target) {

        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2),
                getPointF(1));

        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(
                        (mWidth - dWidth) / 2, mHeight - dHeight),
                new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));

        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);

            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }
    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView((target));
        }
    }


}
