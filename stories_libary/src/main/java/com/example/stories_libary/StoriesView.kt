package com.example.stories_libary

import android.annotation.SuppressLint
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
) : FrameLayout(context, attrs, defStyleAttr), StoriesProgressView.StoriesListener {
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
            setOnTouchListener(onTouchListener)
        }

        with(skipArea) {
            setOnClickListener { skip() }
            setOnTouchListener(onTouchListener)
        }
    }

    private fun skip() {
        storiesProgressView.skip()
    }

    private fun reverse() {
        if (position - 1 < 0) return
        listener?.loadView(--position)
        storiesProgressView.reverse()
    }


    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onStoriesStart() {
        listener?.loadView(position)
    }

    override fun onNext() {
        if (position + 1 < max)
            listener?.loadView(++position)
    }

    override fun onComplete() {
        listener?.onComplete()
    }

    /**
     * Required
     * Loaded views counts
     * @param listSize: Int -> number exactly of view shown
     * @param frequencyRange: Int -> milliseconds between start and finish a PausableProgressBar()
     **/
    fun startAnimationWithSize(listSize: Int, frequencyRange: Int?) {
        this.max = listSize
        with(storiesProgressView) {
            setStoriesCount(listSize)
            frequencyRange?.let { setStoriesFrequency(it) }?.run { setStoriesFrequency(50) }
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
        fun loadView(position: Int)
        fun onComplete()
    }

}