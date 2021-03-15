package com.example.stories_libary

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class StoriesProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val progressBarLayoutParam = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
    private val spaceLayoutParam = LayoutParams(8, LayoutParams.WRAP_CONTENT)
    private val progressBars = ArrayList<PausableProgressBar>()
    private var storiesCount : Int = -1

    private var current = -1
    private var storiesListener: StoriesListener? = null
    private var isComplete: Boolean = false

    private var isSkipStart: Boolean = false

    init {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
        with(context.obtainStyledAttributes(attrs, R.styleable.StoriesProgressView)) {
            storiesCount = getInteger(R.styleable.StoriesProgressView_progressCount, 0)
        }
        bindViews()
    }

    interface StoriesListener {
        fun onStoriesStart()
        fun onNext()
        fun onComplete()
    }

    private fun bindViews() {
        progressBars.clear()
        removeAllViews()

        for (i in 0 until storiesCount) {
            val p = createProgressBar()
            progressBars.add(p)
            addView(p)
            if (i + 1 < storiesCount) {
                addView(createSpace())
            }
        }
    }

    private fun createProgressBar(): PausableProgressBar {
        val p = PausableProgressBar(context)
        p.layoutParams = progressBarLayoutParam
        return p
    }

    private fun createSpace(): View {
        val v = View(context)
        v.layoutParams = spaceLayoutParam
        return v
    }

    /**
     * Pause and Resume story
     */
    fun pause() {
        if (current < 0) return
        progressBars.getOrNull(current)?.pauseAnimation()
    }

    fun resume() {
        if (current < 0) return
        progressBars.getOrNull(current)?.resumeAnimation()
    }

    private fun callback(index: Int): PausableProgressBar.Callback {
        return object : PausableProgressBar.Callback {
            override fun onStartProgress() {
                current = index
            }

            override fun onFinishProgress() {
                next()
            }
        }
    }

    private fun next() {
        val next = current + 1
        if (next <= progressBars.size - 1) {
            storiesListener?.onNext()
            progressBars[next].startAnimation()
        } else {
            isComplete = true
            storiesListener?.onComplete()
        }
        isSkipStart = false
    }

    fun setStoriesListener(storiesListener: StoriesListener) {
        this.storiesListener = storiesListener
    }

    private fun previous() {
        if (current - 1 < 0) return
        progressBars[--current].stopAnimation()
        progressBars[current].startAnimation()
    }

    /**
     * Skip and Reverse current story
     */
    fun skip() {
        progressBars[current].markAsComplete()
        next()
    }

    fun reverse() {
        progressBars[current].stopAnimation()
        previous()
    }

    /**
     * Set story count and create views
     *
     * @param storiesCount story count
     */

    fun setStoriesCount(storiesCount: Int) {
        this.storiesCount = storiesCount
    }

    /**
     * Set stories count and each story duration
     *
     * @param frequency milliseconds between add one percent on progress bar
     */
    fun setStoriesFrequency(frequency: Int) {
        bindViews()
        for (i in progressBars.indices) {
            progressBars[i].setVelocity(frequency)
            progressBars[i].setCallback(callback(i))
        }
    }

    fun startStories() {
        storiesListener?.onStoriesStart()
        current = 0
        progressBars.getOrNull(current)?.startAnimation()
    }

    fun onDestroy() {
        this.storiesListener = null
        for (progressBar in progressBars) {
            progressBar.stopAnimation()
            progressBar.cleanCallback()
        }
    }
}
