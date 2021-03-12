package com.example.stories_libary

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar

class PausableProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var callback: Callback? = null
    private var mVelocity: Long = 50
    private var mProgressBar: ProgressBar
    private var mPaused = false
    private var progressState = 0

    init {
        inflate(context, R.layout.pausable_pregress_bar, this)
        with(context.obtainStyledAttributes(attrs, R.styleable.PausableProgressBar)) {
            setVelocity(getInteger(R.styleable.PausableProgressBar_velocity, 50))
            recycle()
        }
        mProgressBar = findViewById(R.id.storyProgressBar)
    }

    /**
     * Set stories count and each story duration
     *
     * @param frequency milli
     */

    fun setVelocity(frequency: Int) {
        this.mVelocity = frequency.toLong()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun startAnimation() {
        mPaused = false
        callback?.onStartProgress()
        runProgressBar()
    }

    fun stopAnimation() {
        mPaused = true
        progressState = 0
        mProgressBar.progress = 0
    }

    fun markAsComplete() {
        mPaused = true
        progressState = 100
        mProgressBar.progress = 100
    }

    fun pauseAnimation() {
        mPaused = true
    }

    fun resumeAnimation() {
        mPaused = false
        runProgressBar()
    }

    fun cleanCallback() {
        callback = null
    }

    private fun runProgressBar() {
        if ( progressState <  100 && !mPaused ) {
            addProgress()
        } else if (progressState == 100) {
            callback?.onFinishProgress()
        }
    }

    private fun addProgress() {
        Handler().postDelayed({
            mProgressBar.progress = progressState++
            runProgressBar()
        }, mVelocity)
    }

    interface Callback {
        fun onStartProgress()
        fun onFinishProgress()
    }
}
