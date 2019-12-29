package com.example.androidcardlist

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class CustomSwipeRefreshLayout: ViewGroup {

    companion object {
        // TODO : companion object にする必要性
        private val DRAG_RATE = .5f
    }

    private var mTarget: View? = null
    private val mRefreshView: ImageView? = null
    private var mIsBeingDragged: Boolean = false
    // TODO : こいつはなんだ
    private val mActivePointerId = 0
    private var mInitialMotionY = 0f
    private var mRefreshing = false
    private var mInitialOffsetTop = 0
    private var mDispatchTargetTouchDown = false
    private var mTotalDragDistance = 0
    private var mDragPercent = 0f;

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // スワイプ時アニメーションのターゲットにするビューを決めて mTarget に入れる
        ensureTarget()

        // パディングを除いた幅と高さをターゲットビューとリフレッシュ時表示画像を設定したビューに設定する
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY)
        // TODO : null の場合 return した方が良い？
        mTarget?.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        mRefreshView?.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        ensureTarget()

        // TODO : ちょっとよくわからん
        mTarget?.layout(paddingLeft,
            paddingTop + mTarget!!.top,
            paddingLeft + measuredWidth - paddingRight,
            paddingTop + measuredHeight - paddingBottom + mTarget!!.top)

        mRefreshView!!.layout(paddingLeft,
            paddingTop,
            paddingLeft + measuredWidth - paddingRight,
            paddingTop + measuredHeight - paddingBottom)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // ドラッグされていないときは通常のタッチイベントをする、つまり何もしない
        if (!mIsBeingDragged) {
            return super.onTouchEvent(event)
        }

        // TODO : あとで deprecated 対策
        // ドラッグアクション別に処理を行う
        val action = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_MOVE -> {
                // TODO : あとで deprecated 対策
                val pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }

                // TODO : あとで deprecated 対策
                val y = MotionEventCompat.getY(event, pointerIndex)
                val yDiff = y - mInitialMotionY
                if (mRefreshing) {
                    var targetY = (mInitialOffsetTop + yDiff).toInt()
                    if (targetY < 0) {
                        if (mDispatchTargetTouchDown) {
                            mTarget!!.dispatchTouchEvent(event)
                        } else {
                            val obtain: MotionEvent = MotionEvent.obtain(event)
                            obtain.action = MotionEvent.ACTION_DOWN
                            mDispatchTargetTouchDown = true
                            mTarget!!.dispatchTouchEvent(obtain)
                        }
                        targetY = 0
                    } else if (targetY > mTotalDragDistance) {
                        // ドラッグ可能な最大範囲を上回っていた場合は最大範囲を指定
                        targetY = mTotalDragDistance
                    } else {
                        // TODO: 0 <= targetY <= ドラッグ可能な最大範囲の場合は・・何してる？
                        if (mDispatchTargetTouchDown) {
                            val obtain: MotionEvent = MotionEvent.obtain(event)
                            obtain.action = MotionEvent.ACTION_CANCEL
                            mDispatchTargetTouchDown = false
                            mTarget!!.dispatchTouchEvent(obtain)
                        }
                    }
                } else {
                    val scrollTop = yDiff * DRAG_RATE
                    // TODO : ドラッグ可能範囲に対する何の割合？
                    val originalDragPercent = scrollTop / mTotalDragDistance
                    if (originalDragPercent < 0) {
                        return false
                    }
                    mDragPercent = Math.min(1f, Math.abs(originalDragPercent))
                }

            }
            MotionEventCompat.ACTION_POINTER_DOWN -> {

            }
            MotionEventCompat.ACTION_POINTER_UP -> {

            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> {

            }
        }

        return true
    }

    /**
     * スワイプ時アニメーションのターゲットにするビューを決める
     */
    private fun ensureTarget() {
        if (mTarget != null) {
            return
        }
        if (childCount > 0) {
            for (i in 0..childCount) {
                var child = getChildAt(i)
                // 子ビューにリフレッシュ時の読み込み画像は含めない
                if (child != mRefreshView) {
                    // 子ビューが複数あるときは一番最後に定義されたビューをターゲットとする
                    mTarget = child
                }
            }
        }

    }

    // TODO: 実装の必要がないのであとで消す
    private fun canChildScrollUp(): Boolean {
        return false
    }
}