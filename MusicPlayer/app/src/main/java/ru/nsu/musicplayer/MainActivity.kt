package ru.nsu.musicplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackList: List<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate вызван")
        setContentView(R.layout.activity_main)

        initData()

        recyclerView = findViewById(R.id.recyclerViewTracks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        Log.d(TAG, "RecyclerView и Adapter установлены")
    }

    private fun initData() {
        Log.d(TAG, "initData вызван")
        trackList = List(20) {
            Track(
                "No Hay Ley №${it + 1}",
                "Kali Uchis",
                R.raw.track
            )}
        trackAdapter = TrackAdapter(this, trackList)
        Log.d(TAG, "TrackAdapter инициализирован с ${trackList.size} треками")
    }
}
