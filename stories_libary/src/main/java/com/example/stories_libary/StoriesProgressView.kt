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

    private var current = 0
    private var storiesListener: StoriesListener? = null

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
        if (current + 1 < progressBars.size) {
            storiesListener?.onNext()
            progressBars[++current].startAnimation()
        } else {
            storiesListener?.onComplete()
        }
    }

    fun setStoriesListener(storiesListener: StoriesListener) {
        this.storiesListener = storiesListener
    }

    private fun previous() {
        if (current - 1 < 0) return
        progressBars[current--].stopAnimation()
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
     * @param duration milliseconds
     */
    fun setStoriesDuration(duration: Int) {
        bindViews()
        for (i in progressBars.indices) {
            progressBars[i].setProgressBarDuration(duration.toLong())
            progressBars[i].callback = callback(i)
        }
    }

    fun startStories() {
        storiesListener?.onStoriesStart()
        progressBars.getOrNull(current)?.startAnimation()
    }

    fun destroy() {
        for (p in progressBars) {
            p.stopAnimation()
        }
    }
}
