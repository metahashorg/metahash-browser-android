package org.metahash.browser.presentation.view.ripple

import android.animation.ValueAnimator
import android.graphics.Paint
import android.graphics.Path

data class Ripple(
        val mPaint: Paint,
        val mPath: Path,
        var mProgress: Float,
        var mAnimator: ValueAnimator? = null,
        var mAnimRunning: Boolean = false
)