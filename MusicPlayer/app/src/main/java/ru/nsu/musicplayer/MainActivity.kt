package ru.nsu.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackList: List<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        recyclerView = findViewById(R.id.recyclerViewTracks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter
    }

    private fun initData() {
        trackList = List(20) {
            Track(
                "No Hay Ley №${it + 1}",
                "Kali Uchis",
                R.raw.track
            )}
        trackAdapter = TrackAdapter(this, trackList)
    }
}
