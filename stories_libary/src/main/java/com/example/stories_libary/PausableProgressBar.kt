package com.example.stories_libary

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar

class PausableProgressBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Animator.AnimatorListener {

    private var mProgressBar: ProgressBar
    private lateinit var animator: ObjectAnimator
    var animationDuration: Long = 5000
    var callback: Callback? = null

    init {
        inflate(context, R.layout.pausable_progress_bar, this)
        with(context.obtainStyledAttributes(attrs, R.styleable.PausableProgressBar)) {
            recycle()
        }
        mProgressBar = findViewById(R.id.storyProgressBar)
    }

    /**
     * Set stories count and each story duration
     *
     * @param frequency milli
     */

    fun setProgressBarDuration(animationDuration: Long) {
        this.animationDuration = animationDuration
        animator = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100)
        with(animator) {
            duration = animationDuration
            interpolator = DecelerateInterpolator()
            addListener(this@PausableProgressBar)
        }
    }

    override fun onAnimationStart(p0: Animator?) {
        callback?.onStartProgress()
    }

    override fun onAnimationEnd(p0: Animator?) {
        callback?.onFinishProgress()
    }

    override fun onAnimationCancel(p0: Animator?) {
        // Not implemented //
    }

    override fun onAnimationRepeat(p0: Animator?) {
        // Not implemented //
    }

    fun startAnimation() {
        mProgressBar.progress = 0
        animator.start()
    }

    fun stopAnimation() {
        animator.pause()
        mProgressBar.progress = 0
    }

    fun markAsComplete() {
        animator.pause()
        mProgressBar.progress = 100
    }

    fun pauseAnimation() {
        animator.pause()
    }

    fun resumeAnimation() {
        animator.resume()
    }

    fun cleanCallback() {
        callback = null
    }

    interface Callback {
        fun onStartProgress()
        fun onFinishProgress()
    }
}
