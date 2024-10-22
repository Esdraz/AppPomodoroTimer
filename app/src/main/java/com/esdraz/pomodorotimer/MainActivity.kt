package com.esdraz.pomodorotimer

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private var isRunning = false
    private var isInBreak = false
    private val workTimeInMillis: Long = 1 * 60 * 1000 // 25 minutos em milissegundos
    private val breakTimeInMillis: Long = 1 * 30 * 1000 // 5 minutos em milissegundos para a pausa
    private var timeInMillis: Long = workTimeInMillis

    private var currentSession = 1
    private val totalSessions = 4 // Total de sessões de Pomodoro

    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var timerText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sessionCountText: TextView
    private lateinit var focusPauseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)
        stopButton = findViewById(R.id.stopButton)
        timerText = findViewById(R.id.timerText)
        progressBar = findViewById(R.id.progressBar)
        sessionCountText = findViewById(R.id.sessionCount)
        focusPauseText = findViewById(R.id.focusPauseText)

        progressBar.max = 100
        progressBar.progress = 100

        updateSessionCount()
        updateFocusPauseText()

        startPauseButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInMillis = millisUntilFinished
                updateTimerText()
                updateProgressBar()
            }

            override fun onFinish() {
                if (!isInBreak) {
                    // Se a sessão de trabalho terminou, começa a pausa
                    isInBreak = true
                    timeInMillis = breakTimeInMillis
                    updateFocusPauseText()
                    startTimer()
                } else {
                    // Se a pausa terminou, avança para a próxima sessão de trabalho
                    isInBreak = false
                    currentSession++
                    if (currentSession <= totalSessions) {
                        timeInMillis = workTimeInMillis
                        updateSessionCount()
                        updateFocusPauseText()
                        startTimer()
                    } else {
                        // Se todas as sessões foram concluídas
                        resetTimer()
                    }
                }
            }
        }.start()
        isRunning = true
        updateButtonUI()
    }

    private fun pauseTimer() {
        timer.cancel()
        isRunning = false
        updateButtonUI()
    }

    private fun resetTimer() {
        timer.cancel()
        isInBreak = false
        currentSession = 1
        timeInMillis = workTimeInMillis
        updateTimerText()
        updateProgressBar()
        updateSessionCount()
        updateFocusPauseText()
        isRunning = false
        updateButtonUI()
    }

    private fun stopTimer() {
        timer.cancel()
        isRunning = false
        isInBreak = false
        currentSession = 1
        timeInMillis = workTimeInMillis
        updateTimerText()
        updateProgressBar()
        updateSessionCount()
        updateFocusPauseText()
        updateButtonUI()
    }

    private fun updateTimerText() {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateProgressBar() {
        val totalTime = if (isInBreak) breakTimeInMillis else workTimeInMillis
        val progress = (timeInMillis.toFloat() / totalTime) * 100
        progressBar.progress = progress.toInt()
    }

    private fun updateSessionCount() {
        sessionCountText.text = "Sessão $currentSession de $totalSessions"
    }

    private fun updateFocusPauseText() {
        if (isInBreak) {
            focusPauseText.text = "PAUSA"
        } else {
            focusPauseText.text = "FOCUS"
        }
    }

    private fun updateButtonUI() {
        if (isRunning) {
            startPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            startPauseButton.setImageResource(R.drawable.ic_play)
        }
    }
}