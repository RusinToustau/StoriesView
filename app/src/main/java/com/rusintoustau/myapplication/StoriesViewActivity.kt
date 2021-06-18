package com.rusintoustau.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.rusintoustau.stories_libary.StoriesView

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class StoriesViewActivity : AppCompatActivity(), StoriesView.Listener {
    private lateinit var storiesView: StoriesView
    private lateinit var mImageView: ImageView
    private val resourcesList = intArrayOf(
        R.drawable.ba,
        R.drawable.londres,
        R.drawable.paris
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories_view)
        mImageView = findViewById(R.id.imageView)
        setStoriesView()
    }

    private fun setStoriesView() {
        storiesView = findViewById(R.id.myStoriesView)
        with(storiesView) {
            setListener(this@StoriesViewActivity)
            startAnimationWithSize(listSize = resourcesList.size, duration = 5000)
        }
    }

    override fun onPositionChanged(position: Int) {
        resourcesList.getOrNull(position)?.let {
            mImageView.setImageDrawable(getDrawable(it))
        }
    }

    override fun onComplete() {
        onBackPressed()
    }

    override fun onDestroy() {
        storiesView.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        storiesView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        storiesView.resume()
    }
}