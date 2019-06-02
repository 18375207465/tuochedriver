package com.tuochebang.service.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class SwitchView extends View {
    public static final int STATE_SWITCH_OFF = 1;
    public static final int STATE_SWITCH_OFF2 = 2;
    public static final int STATE_SWITCH_ON = 4;
    public static final int STATE_SWITCH_ON2 = 3;
    private float bAnim;
    private float bBottom;
    private float bLeft;
    private float bOff2LeftX;
    private float bOffLeftX;
    private float bOffset;
    private float bOn2LeftX;
    private float bOnLeftX;
    private final Path bPath;
    private float bRadius;
    private final RectF bRectF;
    private float bRight;
    private float bStrokeWidth;
    private float bTop;
    private float bWidth;
    private int lastState;
    private OnStateChangedListener listener;
    private int mHeight;
    private int mWidth;
    private final Paint paint;
    private float sAnim;
    private float sBottom;
    private float sCenterX;
    private float sCenterY;
    private float sHeight;
    private float sLeft;
    private final Path sPath;
    private float sRight;
    private float sScale;
    private float sTop;
    private float sWidth;
    private RadialGradient shadowGradient;
    private float shadowHeight;
    private int state;

    public interface OnStateChangedListener {
        void toggleToOff();

        void toggleToOn();
    }

    /* renamed from: com.tuochebang.service.widget.SwitchView$2 */
    class C08312 implements OnStateChangedListener {
        C08312() {
        }

        public void toggleToOn() {
            SwitchView.this.toggleSwitch(4);
        }

        public void toggleToOff() {
            SwitchView.this.toggleSwitch(1);
        }
    }

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.sPath = new Path();
        this.bPath = new Path();
        this.bRectF = new RectF();
        this.state = 1;
        this.lastState = this.state;
        this.listener = new C08312();
        setLayerType(1, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, (int) (((float) widthSize) * 0.65f));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.sTop = 0.0f;
        this.sLeft = 0.0f;
        this.sRight = (float) this.mWidth;
        this.sBottom = ((float) this.mHeight) * 0.91f;
        this.sWidth = this.sRight - this.sLeft;
        this.sHeight = this.sBottom - this.sTop;
        this.sCenterX = (this.sRight + this.sLeft) / 2.0f;
        this.sCenterY = (this.sBottom + this.sTop) / 2.0f;
        this.shadowHeight = ((float) this.mHeight) - this.sBottom;
        this.bTop = 0.0f;
        this.bLeft = 0.0f;
        float f = this.sBottom;
        this.bBottom = f;
        this.bRight = f;
        this.bWidth = this.bRight - this.bLeft;
        float halfHeightOfS = (this.sBottom - this.sTop) / 2.0f;
        this.bRadius = 0.95f * halfHeightOfS;
        this.bOffset = this.bRadius * 0.2f;
        this.bStrokeWidth = (halfHeightOfS - this.bRadius) * 2.0f;
        this.bOnLeftX = this.sWidth - this.bWidth;
        this.bOn2LeftX = this.bOnLeftX - this.bOffset;
        this.bOffLeftX = 0.0f;
        this.bOff2LeftX = 0.0f;
        this.sScale = 1.0f - (this.bStrokeWidth / this.sHeight);
        RectF sRectF = new RectF(this.sLeft, this.sTop, this.sBottom, this.sBottom);
        this.sPath.arcTo(sRectF, 90.0f, 180.0f);
        sRectF.left = this.sRight - this.sBottom;
        sRectF.right = this.sRight;
        this.sPath.arcTo(sRectF, 270.0f, 180.0f);
        this.sPath.close();
        this.bRectF.left = this.bLeft;
        this.bRectF.right = this.bRight;
        this.bRectF.top = this.bTop + (this.bStrokeWidth / 2.0f);
        this.bRectF.bottom = this.bBottom - (this.bStrokeWidth / 2.0f);
        this.shadowGradient = new RadialGradient(this.bWidth / 2.0f, this.bWidth / 2.0f, this.bWidth / 2.0f, ViewCompat.MEASURED_STATE_MASK, 0, TileMode.CLAMP);
    }

    private void calcBPath(float percent) {
        this.bPath.reset();
        this.bRectF.left = this.bLeft + (this.bStrokeWidth / 2.0f);
        this.bRectF.right = this.bRight - (this.bStrokeWidth / 2.0f);
        this.bPath.arcTo(this.bRectF, 90.0f, 180.0f);
        this.bRectF.left = (this.bLeft + (this.bOffset * percent)) + (this.bStrokeWidth / 2.0f);
        this.bRectF.right = (this.bRight + (this.bOffset * percent)) - (this.bStrokeWidth / 2.0f);
        this.bPath.arcTo(this.bRectF, 270.0f, 180.0f);
        this.bPath.close();
    }

    private float calcBTranslate(float percent) {
        float result = 0.0f;
        switch (this.state - this.lastState) {
            case -3:
                result = this.bOffLeftX + ((this.bOnLeftX - this.bOffLeftX) * percent);
                break;
            case -2:
                if (this.state != 1) {
                    if (this.state == 2) {
                        result = this.bOff2LeftX + ((this.bOnLeftX - this.bOff2LeftX) * percent);
                        break;
                    }
                }
                result = this.bOffLeftX + ((this.bOn2LeftX - this.bOffLeftX) * percent);
                break;
            case -1:
                if (this.state != 3) {
                    if (this.state == 1) {
                        result = this.bOffLeftX + ((this.bOff2LeftX - this.bOffLeftX) * percent);
                        break;
                    }
                }
                result = this.bOn2LeftX + ((this.bOnLeftX - this.bOn2LeftX) * percent);
                break;
            case 1:
                if (this.state != 2) {
                    if (this.state == 4) {
                        result = this.bOnLeftX - ((this.bOnLeftX - this.bOn2LeftX) * percent);
                        break;
                    }
                }
                result = this.bOff2LeftX - ((this.bOff2LeftX - this.bOffLeftX) * percent);
                break;
            case 2:
                if (this.state != 4) {
                    if (this.state == 4) {
                        result = this.bOn2LeftX - ((this.bOn2LeftX - this.bOffLeftX) * percent);
                        break;
                    }
                }
                result = this.bOnLeftX - ((this.bOnLeftX - this.bOff2LeftX) * percent);
                break;
            case 3:
                result = this.bOnLeftX - ((this.bOnLeftX - this.bOffLeftX) * percent);
                break;
        }
        return result - this.bOffLeftX;
    }

    protected void onDraw(Canvas canvas) {
        float f;
        super.onDraw(canvas);
        this.paint.setAntiAlias(true);
        boolean isOn = this.state == 4 || this.state == 3;
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(isOn ? -2803651 : -1842205);
        canvas.drawPath(this.sPath, this.paint);
        if (this.sAnim - 0.1f > 0.0f) {
            f = this.sAnim - 0.1f;
        } else {
            f = 0.0f;
        }
        this.sAnim = f;
        if (this.bAnim - 0.1f > 0.0f) {
            f = this.bAnim - 0.1f;
        } else {
            f = 0.0f;
        }
        this.bAnim = f;
        float scale = this.sScale * (isOn ? this.sAnim : 1.0f - this.sAnim);
        float scaleOffset = ((this.bOnLeftX + this.bRadius) - this.sCenterX) * (isOn ? 1.0f - this.sAnim : this.sAnim);
        canvas.save();
        canvas.scale(scale, scale, this.sCenterX + scaleOffset, this.sCenterY);
        this.paint.setColor(-1);
        canvas.drawPath(this.sPath, this.paint);
        canvas.restore();
        canvas.save();
        canvas.translate(calcBTranslate(this.bAnim), this.shadowHeight);
        boolean isState2 = this.state == 3 || this.state == 2;
        calcBPath(isState2 ? 1.0f - this.bAnim : this.bAnim);
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(-13421773);
        this.paint.setShader(this.shadowGradient);
        canvas.drawPath(this.bPath, this.paint);
        this.paint.setShader(null);
        canvas.translate(0.0f, -this.shadowHeight);
        canvas.scale(0.98f, 0.98f, this.bWidth / 2.0f, this.bWidth / 2.0f);
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(-1);
        canvas.drawPath(this.bPath, this.paint);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(this.bStrokeWidth * 0.5f);
        this.paint.setColor(isOn ? -2803651 : -4210753);
        canvas.drawPath(this.bPath, this.paint);
        canvas.restore();
        this.paint.reset();
        if (this.sAnim > 0.0f || this.bAnim > 0.0f) {
            invalidate();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if ((this.state == 4 || this.state == 1) && this.sAnim * this.bAnim == 0.0f) {
            switch (event.getAction()) {
                case 0:
                    return true;
                case 1:
                case 3:
                    this.lastState = this.state;
                    if (this.state == 1) {
                        refreshState(2);
                    } else if (this.state == 4) {
                        refreshState(3);
                    }
                    this.bAnim = 1.0f;
                    invalidate();
                    if (this.listener != null) {
                        if (this.state != 2) {
                            if (this.state == 3) {
                                this.listener.toggleToOff();
                                break;
                            }
                        }
                        this.listener.toggleToOn();
                        break;
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void refreshState(int newState) {
        this.lastState = this.state;
        this.state = newState;
        postInvalidate();
    }

    public int getState() {
        return this.state;
    }

    public void setState(boolean isOn) {
        refreshState(isOn ? 4 : 1);
    }

    public void toggleSwitch(boolean letItOn) {
        final int wich = letItOn ? 4 : 1;
        postDelayed(new Runnable() {
            public void run() {
                SwitchView.this.toggleSwitch(wich);
            }
        }, 300);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void toggleSwitch(int r5) {
        /*
        r4 = this;
        r3 = 4;
        r2 = 1;
        monitor-enter(r4);
        if (r5 == r3) goto L_0x0007;
    L_0x0005:
        if (r5 != r2) goto L_0x0028;
    L_0x0007:
        if (r5 != r3) goto L_0x0012;
    L_0x0009:
        r0 = r4.lastState;	 Catch:{ all -> 0x002a }
        if (r0 == r2) goto L_0x001d;
    L_0x000d:
        r0 = r4.lastState;	 Catch:{ all -> 0x002a }
        r1 = 2;
        if (r0 == r1) goto L_0x001d;
    L_0x0012:
        if (r5 != r2) goto L_0x0021;
    L_0x0014:
        r0 = r4.lastState;	 Catch:{ all -> 0x002a }
        if (r0 == r3) goto L_0x001d;
    L_0x0018:
        r0 = r4.lastState;	 Catch:{ all -> 0x002a }
        r1 = 3;
        if (r0 != r1) goto L_0x0021;
    L_0x001d:
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4.sAnim = r0;	 Catch:{ all -> 0x002a }
    L_0x0021:
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4.bAnim = r0;	 Catch:{ all -> 0x002a }
        r4.refreshState(r5);	 Catch:{ all -> 0x002a }
    L_0x0028:
        monitor-exit(r4);
        return;
    L_0x002a:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tuochebang.service.widget.SwitchView.toggleSwitch(int):void");
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("empty listener");
        }
        this.listener = listener;
    }
}
