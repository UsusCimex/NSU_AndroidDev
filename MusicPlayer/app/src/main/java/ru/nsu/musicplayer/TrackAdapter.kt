package ru.nsu.musicplayer
import android.graphics.drawable.AnimatedVectorDrawable
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val context: Context, private val trackList: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var currentlyPlayingPosition: Int = RecyclerView.NO_POSITION
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false

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

        if (position == currentlyPlayingPosition) {
            holder.playPauseButton.setImageResource(R.drawable.ic_static_pause)
        } else {
            holder.playPauseButton.setImageResource(R.drawable.ic_static_play)
        }

        holder.playPauseButton.setOnClickListener {
            if (position == currentlyPlayingPosition) {
                if (isPaused) {
                    resumePlaying(holder)
                } else {
                    pausePlaying(holder)
                }
            } else {
                if (currentlyPlayingPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(currentlyPlayingPosition)
                }
                startPlaying(holder, position, track.trackId)
            }
        }
    }

    override fun getItemCount(): Int = trackList.size

    private fun startPlaying(holder: TrackViewHolder, position: Int, trackId: Int) {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        mediaPlayer = MediaPlayer.create(context, trackId)
        mediaPlayer?.start()

        currentlyPlayingPosition = position
        isPaused = false

        holder.playPauseButton.setImageResource(R.drawable.ic_pause)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
    }

    private fun pausePlaying(holder: TrackViewHolder) {
        mediaPlayer?.pause()
        isPaused = true

        holder.playPauseButton.setImageResource(R.drawable.ic_play)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
    }

    private fun resumePlaying(holder: TrackViewHolder) {
        mediaPlayer?.start()
        isPaused = false

        holder.playPauseButton.setImageResource(R.drawable.ic_pause)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
    }
}
