package ru.nsu.musicplayer
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackIcon: ImageView = itemView.findViewById(R.id.imageViewIcon)
        val trackName: TextView = itemView.findViewById(R.id.textViewTrackName)
        val artistName: TextView = itemView.findViewById(R.id.textViewArtistName)
        val playPauseButton: ImageView = itemView.findViewById(R.id.buttonPlayPause)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.trackName.text = track.trackName
        holder.artistName.text = track.artistName

        holder.playPauseButton.setImageResource(R.drawable.ic_static_play)

        var isPlaying = false

        holder.playPauseButton.setOnClickListener {
            isPlaying = !isPlaying

            val drawable = if (isPlaying) {
                holder.playPauseButton.setImageResource(R.drawable.ic_pause)
                holder.playPauseButton.drawable as AnimatedVectorDrawable
            } else {
                holder.playPauseButton.setImageResource(R.drawable.ic_play)
                holder.playPauseButton.drawable as AnimatedVectorDrawable
            }

            drawable.start()
        }
    }

    override fun getItemCount(): Int = trackList.size
}
