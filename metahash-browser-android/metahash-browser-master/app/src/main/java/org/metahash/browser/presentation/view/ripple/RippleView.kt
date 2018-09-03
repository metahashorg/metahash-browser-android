package org.metahash.browser.presentation.view.ripple

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import org.metahash.browser.R
import org.metahash.browser.extentions.dpToPx

class RippleView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : View(context, attrs, defStyle) {

    private val MAX_ALPHA = 255
    private val ANIMATION_DURATION = 2500L
    private val RING_DELAY = 700L
    private val RINGS_COUNT = 6

    private val START_COLOR = ContextCompat.getColor(context, R.color.colorWhite)
    private val END_COLOR = ContextCompat.getColor(context, R.color.colorBlue)

    private val mPaintWidth = 2.dpToPx.toFloat()
    var mRestartAfterStop = true
    var mBoundViewSize = -1

    private val mRings = mutableListOf<Ripple>()

    init {
        for (i in 0 until RINGS_COUNT) {
            mRings.add(createNewRing(i))
        }
    }

    private fun createNewRing(position: Int): Ripple {
        val ripple = Ripple(
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = mPaintWidth
                },
                Path(),
                0F)
        ripple.mAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                mRings[position].mAnimRunning = true
                mRings[position].mProgress = it.animatedValue as Float

                //change color
                mRings[position].mPaint.color = getInterpolatedColor(mRings[position].mProgress)
                //set alpha
                mRings[position].mPaint.alpha = (MAX_ALPHA - MAX_ALPHA * mRings[position].mProgress).toInt()

                invalidate()
                if (mRings[position].mProgress == 1F) {
                    mRings[position].mAnimRunning = false
                }

            }
        }
        return ripple
    }

    private fun getInterpolatedColor(percent: Float,
                                     startColor: Int = START_COLOR,
                                     endColor: Int = END_COLOR) =
            ColorInterpolator.evaluate(percent, startColor, endColor) as Int

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isAnimRunning()) {
            val center = width / 2F
            mRings
                    .filter { it.mAnimRunning }
                    .forEach {
                        with(it) {
                            mPath.rewind()
                            mPath.addCircle(center, center, getRadius(mProgress), Path.Direction.CCW)
                            canvas?.drawPath(mPath, mPaint)
                        }
                    }
        } else if (mRestartAfterStop) {
            start()
        }
    }

    private fun isAnimRunning() = mRings.any { it.mAnimRunning }

    private fun getRadius(progress: Float): Float {
        val difference = getMaxRadius() - getMinRadius()
        return difference * progress + getMinRadius()
    }

    private fun getMinRadius(): Float {
        return if (mBoundViewSize == -1) {
            mPaintWidth * 2
        } else {
            (mBoundViewSize / 2F) + mPaintWidth * 2
        }
    }

    private fun getMaxRadius() = (width / 2f) - mPaintWidth

    fun start() {
        for (i in 0 until mRings.size) {
            mRings[i]
            mRings[i].mAnimRunning = false
            mRings[i].mAnimator?.startDelay = RING_DELAY * i
            mRings[i].mProgress = 0F
            mRings[i].mAnimator?.start()
        }
    }
}