package com.example.fcppomotimer

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import com.example.fcppomotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentDownTimer: CountDownTimer? = null

    private val soundPool = SoundPool.Builder().build()
    private var tickingSound: Int? = null
    private var bellSound: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initSound()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun init(){
        binding.seekBar.setOnSeekBarChangeListener(
            object  : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser)
                    updateTime(progress*60*1000L)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    TODO("Not yet implemented")
                    seekBar?:return
                    if(seekBar.progress ==0){
                        stopCountDown()
                    }else{
                        startCountDown(seekBar = seekBar)
                    }


                }


            }
        )
    }

    private fun initSound(){
        tickingSound = soundPool.load(this,R.raw.timer_ticking,1)
        bellSound = soundPool.load(this,R.raw.timer_bell,1)
    }

    private fun startCountDown(seekBar: SeekBar?){
        seekBar ?: return
        currentDownTimer = createCountDownTimer(seekBar.progress*60*1000L)
        currentDownTimer?.start()
        tickingSound?.let {
            soundPool.play(it,1F,1F,0,-1,1F)
        }
    }
    private fun stopCountDown(){
        currentDownTimer?.cancel()
        currentDownTimer = null
        soundPool.autoPause()
    }

    private fun createCountDownTimer(initialmillis: Long) =
        object : CountDownTimer(initialmillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                TODO("Not yet implemented")
                updateTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
                completeCountDown()
            }
        }

    private fun completeCountDown(){
        updateTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSound?.let {
            soundPool.play(it,1F,1F,0,0,1F)
        }
    }

    private fun updateTime(remainMillis: Long){
        binding.minTv.text = "%02d".format(remainMillis/1000/60)
        binding.secTv.text = "%02d".format(remainMillis/1000%60)
    }

    private fun updateSeekBar(remainMillis: Long){
        binding.seekBar.progress = (remainMillis/1000/60).toInt()
    }
}