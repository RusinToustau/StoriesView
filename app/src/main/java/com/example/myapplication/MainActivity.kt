package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.stories_libary.PausableProgressBar
import com.example.stories_libary.StoriesProgressView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var playBtn : Button
    private lateinit var pauseBtn : Button
    private lateinit var stopBtn : Button
    private lateinit var resumeBtn : Button
    private lateinit var exampleBtn : Button
    private lateinit var storiesView : StoriesProgressView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pb = findViewById<PausableProgressBar>(R.id.pausableProgressBar)

        playBtn = findViewById(R.id.playButton)
        pauseBtn = findViewById(R.id.pauseButton)
        stopBtn = findViewById(R.id.stopButton)
        resumeBtn = findViewById(R.id.resumeButton)
        exampleBtn = findViewById(R.id.showStoriesExample)

        playBtn.setOnClickListener { pb.startAnimation() }
        pauseBtn.setOnClickListener { pb.pauseAnimation() }
        resumeBtn.setOnClickListener { pb.resumeAnimation() }
        stopBtn.setOnClickListener { pb.stopAnimation() }
        exampleBtn.setOnClickListener { startActivity(Intent(this, StoriesViewActivity::class.java)) }

        storiesView = findViewById(R.id.storiesProgressView)

        with(storiesView) {
            setStoriesCount(storiesCount = 5)
            setStoriesDuration(duration = 5000)
            startStories()
            setStoriesListener(storiesListener)
        }
    }

    val pausableCallback = object : PausableProgressBar.Callback {
        override fun onStartProgress() {
            /** Empty Method **/
        }

        override fun onFinishProgress() {
            /** Do Something when finish **/
        }
    }

    val storiesListener = object : StoriesProgressView.StoriesListener {
        override fun onStoriesStart() {
            /** Empty Method **/
        }

        override fun onNext() {
            showToast("Siguiente Storie")
        }

        override fun onComplete() {
            showToast("Completado")
        }
    }

    private fun showToast(text:String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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
