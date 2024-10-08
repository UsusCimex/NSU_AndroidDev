package ru.nsu.musicplayer
import android.graphics.drawable.AnimatedVectorDrawable
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val context: Context, private val trackList: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private val TAG = "TrackAdapter"

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
        Log.d(TAG, "onCreateViewHolder вызван")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        Log.d(TAG, "onBindViewHolder для позиции $position: ${track.trackName}")

        holder.trackName.text = track.trackName
        holder.artistName.text = track.artistName

        if (position == currentlyPlayingPosition) {
            holder.playPauseButton.setImageResource(R.drawable.ic_static_pause)
            Log.d(TAG, "Устанавливаем кнопку паузы для позиции $position")
        } else {
            holder.playPauseButton.setImageResource(R.drawable.ic_static_play)
            Log.d(TAG, "Устанавливаем кнопку воспроизведения для позиции $position")
        }

        holder.playPauseButton.setOnClickListener {
            Log.d(TAG, "Кнопка Play/Pause нажата на позиции $position")
            if (position == currentlyPlayingPosition) {
                if (isPaused) {
                    Log.d(TAG, "Продолжаем воспроизведение на позиции $position")
                    resumePlaying(holder)
                } else {
                    Log.d(TAG, "Ставим на паузу воспроизведение на позиции $position")
                    pausePlaying(holder)
                }
            } else {
                if (currentlyPlayingPosition != RecyclerView.NO_POSITION) {
                    Log.d(TAG, "Обновляем предыдущую позицию $currentlyPlayingPosition")
                    notifyItemChanged(currentlyPlayingPosition)
                }
                Log.d(TAG, "Начинаем воспроизведение на позиции $position")
                startPlaying(holder, position, track.trackId)
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount вызван: ${trackList.size} элементов")
        return trackList.size
    }

    private fun startPlaying(holder: TrackViewHolder, position: Int, trackId: Int) {
        Log.d(TAG, "startPlaying вызван для позиции $position с trackId $trackId")
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            Log.d(TAG, "Останавливаем и освобождаем предыдущий MediaPlayer")
        }

        mediaPlayer = MediaPlayer.create(context, trackId)
        mediaPlayer?.start()
        Log.d(TAG, "MediaPlayer запущен для trackId $trackId")

        currentlyPlayingPosition = position
        isPaused = false

        holder.playPauseButton.setImageResource(R.drawable.ic_pause)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
        Log.d(TAG, "Анимация кнопки паузы запущена")
    }

    private fun pausePlaying(holder: TrackViewHolder) {
        Log.d(TAG, "pausePlaying вызван")
        mediaPlayer?.pause()
        isPaused = true

        holder.playPauseButton.setImageResource(R.drawable.ic_play)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
        Log.d(TAG, "Анимация кнопки воспроизведения запущена")
    }

    private fun resumePlaying(holder: TrackViewHolder) {
        Log.d(TAG, "resumePlaying вызван")
        mediaPlayer?.start()
        isPaused = false

        holder.playPauseButton.setImageResource(R.drawable.ic_pause)
        val drawable = holder.playPauseButton.drawable as AnimatedVectorDrawable
        drawable.start()
        Log.d(TAG, "Анимация кнопки паузы запущена")
    }
}
