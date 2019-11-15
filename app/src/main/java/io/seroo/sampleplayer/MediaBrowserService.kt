package io.seroo.sampleplayer

import android.app.MediaRouteButton
import android.app.Service
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver

class MediaBrowserService : MediaBrowserServiceCompat() {
    companion object {
        const val LOG_TAG = "MediaBrowserService"
        const val EMPTY_MEDIA_ROOT_ID = "empty_media_id"
    }

    private var mediaSessionCompat: MediaSessionCompat? = null
    private lateinit var statusBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()

        mediaSessionCompat = MediaSessionCompat(baseContext, LOG_TAG).apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            statusBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)

            setPlaybackState(statusBuilder.build())
//            setCallback()

            setSessionToken(sessionToken)

            startForeground(0, builder.build())
        }
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (EMPTY_MEDIA_ROOT_ID== parentId) {
            result.sendResult(null)
            return
        }

        val mediaItem= emptyList<MediaBrowserCompat.MediaItem>()

        result.sendResult(mediaItem.toMutableList())
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): MediaBrowserServiceCompat.BrowserRoot {
        return MediaBrowserServiceCompat.BrowserRoot(EMPTY_MEDIA_ROOT_ID, null)
    }

    val controller = mediaSessionCompat?.controller
    val mediaMetadata = controller?.metadata
    val description = mediaMetadata?.description

    val builder = NotificationCompat.Builder(baseContext, getString(R.string.channel_id)).apply {
        setContentTitle(description?.title)
        setContentText(description?.subtitle)
        setSubText(description?.description)
        setLargeIcon(description?.iconBitmap)

        setContentIntent(controller?.sessionActivity)

        setDeleteIntent(
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                baseContext,
                PlaybackStateCompat.ACTION_STOP
            )
        )

        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        setSmallIcon(R.drawable.ic_android_24px)
        color = ContextCompat.getColor(baseContext, R.color.colorPrimaryDark)

        addAction(
            NotificationCompat.Action(
                R.drawable.ic_pause_24px,
                getString(R.string.pause),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    baseContext,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
        )

        setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSessionCompat?.sessionToken)
                .setShowActionsInCompactView(0)
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        baseContext,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
        )
    }


}