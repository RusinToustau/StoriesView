package com.example.stories_libary

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout

class StoriesView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        StoriesProgressView.StoriesListener, View.OnTouchListener {
    private var pressTime = 0L
    private var limit = 500L
    private var storiesProgressView: StoriesProgressView
    private var reverseArea: View
    private var skipArea: View
    private var position = 0
    private var max = 0
    private var listener: Listener? = null

    init {
        inflate(context, R.layout.stories_view, this)
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        storiesProgressView = findViewById(R.id.storiesProgressView)
        reverseArea = findViewById(R.id.reverse)
        skipArea = findViewById(R.id.skip)
        storiesProgressView.setStoriesListener(this)
        setMotionEvents()
    }

    private fun setMotionEvents() {
        with(reverseArea) {
            setOnClickListener { reverse() }
            setOnTouchListener(this@StoriesView)
        }

        with(skipArea) {
            setOnClickListener { skip() }
            setOnTouchListener(this@StoriesView)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView.pause()
                return false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView.resume()
                return limit < now - pressTime
            }
        }
        return false
    }

    private fun skip() {
        storiesProgressView.skip()
    }

    private fun reverse() {
        if (position - 1 < 0) return
        listener?.onPositionChanged(--position)
        storiesProgressView.reverse()
    }

    override fun onStoriesStart() {
        listener?.onPositionChanged(position)
    }

    override fun onNext() {
        if (position + 1 < max)
            listener?.onPositionChanged(++position)
    }

    override fun onComplete() {
        listener?.onComplete()
    }

    /**
     * Required
     * Loaded views counts
     * @param listSize: Int -> number exactly of view shown
     * @param duration: Int -> milliseconds between start and finish a PausableProgressBar()
     **/
    fun startAnimationWithSize(listSize: Int, duration: Int?) {
        this.max = listSize
        with(storiesProgressView) {
            setStoriesCount(listSize)
            duration?.let { setStoriesDuration(it) }
            startStories()
        }
    }

    /**
     * Required
     * @param StoriesView.Listener
     **/
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onPositionChanged(position: Int)
        fun onComplete()
    }

    fun destroy() {
        storiesProgressView.destroy()
        this.listener = null
    }

    fun pause() {
        storiesProgressView.pause()
    }

    fun resume() {
        storiesProgressView.resume()
    }
}