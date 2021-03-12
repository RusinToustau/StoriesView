package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.stories_libary.PausableProgressBar
import com.example.stories_libary.StoriesProgressView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var playBtn: Button
    private lateinit var pauseBtn:Button
    private lateinit var stopBtn:Button
    private lateinit var resumeBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pb = findViewById<PausableProgressBar>(R.id.pausableProgressBar)

        playBtn = findViewById(R.id.playButton)
        pauseBtn = findViewById(R.id.pauseButton)
        stopBtn = findViewById(R.id.stopButton)
        resumeBtn = findViewById(R.id.resumeButton)

        playBtn.setOnClickListener { pb.startAnimation() }
        pauseBtn.setOnClickListener { pb.pauseAnimation() }
        resumeBtn.setOnClickListener { pb.resumeAnimation() }
        stopBtn.setOnClickListener { pb.stopAnimation() }

        val spv = findViewById<StoriesProgressView>(R.id.storiesProgressView)

        with(spv) {
            setStoriesCount(5)
            setStoriesFrequency(50)
            startStories()
            setStoriesListener(storiesListener)
        }
    }

    val pausableCallback = object : PausableProgressBar.Callback {
        override fun onStartProgress() {
            /** Empty Method **/
        }

        override fun onFinishProgress() {
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

}
