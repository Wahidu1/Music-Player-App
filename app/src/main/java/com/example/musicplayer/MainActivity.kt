package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    private var finalTime = 0.0
    private var forwardTime = 10000
    private var backwardTime = 10000
    private var oneTimeOnly = 0

    // Handler
    private var handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private lateinit var songTitle: TextView
    private lateinit var backButton: Button
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var forwardButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var timer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        songTitle = findViewById(R.id.song_title)
        backButton = findViewById(R.id.back_button)
        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        forwardButton = findViewById(R.id.forward_button)
        seekBar = findViewById(R.id.seek_bar)
        timer = findViewById(R.id.display_time)


        //media player
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.sara_vicky
        )
        seekBar.isClickable = false

        //Adding functionality
        playButton.setOnClickListener {
            mediaPlayer.start()
            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }
            timer.text = startTime.toString()
            seekBar.progress = (startTime.toInt())

            handler.postDelayed(updateSongTime, 100)
        }
        // Setting music title

        ("" + resources.getResourceEntryName(
            R.raw.sara_vicky)).also { this.songTitle.text = it }

        //Stop Button
        pauseButton.setOnClickListener {
            mediaPlayer.pause()
        }

        //Forward Button
        forwardButton.setOnClickListener {
            val temp = startTime
            if (temp + backwardTime <= finalTime) {
                startTime += backwardTime

                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(
                    this,
                    "Can't Jump forward", Toast.LENGTH_LONG
                ).show()
            }
        }
        // back button
        backButton.setOnClickListener {
            val temp = startTime
            if (temp - forwardTime >= 0) {
                startTime -= forwardTime

                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(
                    this,
                    "Can't Jump Back", Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    //Creating the Runnable
    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            timer.text =
                "" +
                        String.format(
                            "%d min , %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(
                                startTime.toLong()
                                        - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        startTime.toLong()
                                    )
                                ))
                        )


            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}