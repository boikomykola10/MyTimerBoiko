package com.example.mytimer
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.concurrent.TimeUnit

// MainActivity.kt

class MainActivity : AppCompatActivity() {
    // UI елементи
    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var setTimeButton: Button

    // Змінні таймера
    private lateinit var countDownTimer: CountDownTimer
    private var initialTimeInMillis: Long = 60000 // Initial time in milliseconds (e.g., 1 minute)
    private var timeLeftInMillis: Long = initialTimeInMillis
    private var timerRunning: Boolean = false

    // Викликається при створенні додатку

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація UI елементів
        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)
        setTimeButton = findViewById(R.id.setTimeButton)

        // Налаштування обробників подій для кнопок
        startButton.setOnClickListener {
            if (timerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        setTimeButton.setOnClickListener {
            showTimePickerDialog()
        }
    }

    // Запускає таймер та розпочинає відлік часу до завершення.
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountdownText()
            }

            override fun onFinish() {
                timerRunning = false
            }
        }.start()

        timerRunning = true
    }

    // Призупиняє таймер та оновлює інтерфейс користувача.
    private fun pauseTimer() {
        countDownTimer.cancel()
        timerRunning = false
    }

    // Скасовує та скидає таймер до початкового значення.
    private fun resetTimer() {
        countDownTimer.cancel()
        timerRunning = false
        timeLeftInMillis = initialTimeInMillis
        updateCountdownText()
    }

    // Оновлює текст виводу таймера.
    private fun updateCountdownText() {
        val hours = TimeUnit.MILLISECONDS.toHours(timeLeftInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) % 60

        val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView.text = timeFormatted
    }

    // Відображає діалогове вікно для вибору часу.
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Set the chosen time to initialTimeInMillis
                val selectedTimeInMillis =
                    TimeUnit.HOURS.toMillis(hourOfDay.toLong()) +
                            TimeUnit.MINUTES.toMillis(minute.toLong())
                initialTimeInMillis = selectedTimeInMillis
                timeLeftInMillis = initialTimeInMillis
                updateCountdownText()
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
    }
}
