package com.kamancho.melisma.trending.presentation

import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.media3.common.*
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import javax.inject.Inject



 /**
 * Created by HP on 20.03.2023.
 **/
 @UnstableApi
interface MediaControllerWrapper: Player {


     class Base @Inject constructor(
         private val controllerFuture: ListenableFuture<MediaController>,
     ) : MediaControllerWrapper {

         private val controller: MediaController?
             get() = if (controllerFuture.isDone) controllerFuture.get() else null



         override fun getApplicationLooper(): Looper {
             TODO("Not yet implemented")
         }

         override fun addListener(listener: Player.Listener) {
             controller?.addListener(listener)
         }

         override fun removeListener(listener: Player.Listener) {
             controller?.removeListener(listener)
         }

         override fun setMediaItems(mediaItems: MutableList<MediaItem>) {
             controller?.setMediaItems(mediaItems)
         }


         override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) {
             TODO("Not yet implemented")
         }

         override fun setMediaItems(
             mediaItems: MutableList<MediaItem>,
             startIndex: Int,
             startPositionMs: Long,
         ) {
             TODO("Not yet implemented")
         }

         override fun setMediaItem(mediaItem: MediaItem) {
             controller?.setMediaItem(mediaItem)
         }

         override fun setMediaItem(mediaItem: MediaItem, startPositionMs: Long) {
             TODO("Not yet implemented")
         }

         override fun setMediaItem(mediaItem: MediaItem, resetPosition: Boolean) {
             TODO("Not yet implemented")
         }

         override fun addMediaItem(mediaItem: MediaItem) {
             TODO("Not yet implemented")
         }

         override fun addMediaItem(index: Int, mediaItem: MediaItem) {
             TODO("Not yet implemented")
         }

         override fun addMediaItems(mediaItems: MutableList<MediaItem>) {
             controller?.addMediaItems(mediaItems)
         }

         override fun addMediaItems(index: Int, mediaItems: MutableList<MediaItem>) {
             TODO("Not yet implemented")
         }

         override fun moveMediaItem(currentIndex: Int, newIndex: Int) {
             TODO("Not yet implemented")
         }

         override fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int) {
             TODO("Not yet implemented")
         }

         override fun replaceMediaItem(index: Int, mediaItem: MediaItem) {
             controller?.replaceMediaItem(index,mediaItem)
         }

         override fun replaceMediaItems(
             fromIndex: Int,
             toIndex: Int,
             mediaItems: MutableList<MediaItem>,
         ) {
             TODO("Not yet implemented")
         }

         override fun removeMediaItem(index: Int) {
             TODO("Not yet implemented")
         }

         override fun removeMediaItems(fromIndex: Int, toIndex: Int) {
             TODO("Not yet implemented")
         }

         override fun clearMediaItems() {
             controller?.clearMediaItems()
         }

         override fun isCommandAvailable(command: Int): Boolean {
             TODO("Not yet implemented")
         }

         override fun canAdvertiseSession(): Boolean {
             TODO("Not yet implemented")
         }

         override fun getAvailableCommands(): Player.Commands {
             TODO("Not yet implemented")
         }

         override fun prepare() {
             controller?.prepare()
         }

         override fun getPlaybackState(): Int {
             TODO("Not yet implemented")
         }

         override fun getPlaybackSuppressionReason(): Int {
             TODO("Not yet implemented")
         }

         override fun isPlaying(): Boolean {
             TODO("Not yet implemented")
         }

         override fun getPlayerError(): PlaybackException? {
             TODO("Not yet implemented")
         }

         override fun play() {
             controller?.play()
         }

         override fun pause() {
             controller?.pause()
         }

         override fun setPlayWhenReady(playWhenReady: Boolean) {
             controller?.playWhenReady = playWhenReady
         }

         override fun getPlayWhenReady(): Boolean = controller?.playWhenReady?: false

         override fun setRepeatMode(repeatMode: Int) {
             controller?.repeatMode = repeatMode
         }

         override fun getRepeatMode(): Int = controller?.repeatMode ?: Player.REPEAT_MODE_OFF

         override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
             controller?.shuffleModeEnabled = shuffleModeEnabled
         }

         override fun getShuffleModeEnabled(): Boolean = controller?.shuffleModeEnabled ?: false

         override fun isLoading(): Boolean {
             TODO("Not yet implemented")
         }

         override fun seekToDefaultPosition() {
                 controller?.seekToDefaultPosition()
         }

         override fun seekToDefaultPosition(mediaItemIndex: Int) {
             controller?.seekToDefaultPosition(mediaItemIndex)
         }

         override fun seekTo(positionMs: Long) {
             controller?.seekTo(positionMs)
         }

         override fun seekTo(mediaItemIndex: Int, positionMs: Long) {
             TODO("Not yet implemented")
         }

         override fun getSeekBackIncrement(): Long {
             TODO("Not yet implemented")
         }

         override fun seekBack() {
             TODO("Not yet implemented")
         }

         override fun getSeekForwardIncrement(): Long {
             TODO("Not yet implemented")
         }

         override fun seekForward() {
             TODO("Not yet implemented")
         }

         override fun hasPrevious(): Boolean {
             TODO("Not yet implemented")
         }

         override fun hasPreviousWindow(): Boolean {
             TODO("Not yet implemented")
         }

         override fun hasPreviousMediaItem(): Boolean {
             TODO("Not yet implemented")
         }

         override fun previous() {
             TODO("Not yet implemented")
         }

         override fun seekToPreviousWindow() {
             TODO("Not yet implemented")
         }

         override fun seekToPreviousMediaItem() {
             controller?.seekToPreviousMediaItem()
         }

         override fun getMaxSeekToPreviousPosition(): Long {
             TODO("Not yet implemented")
         }

         override fun seekToPrevious() {
             TODO("Not yet implemented")
         }

         override fun hasNext(): Boolean {
             TODO("Not yet implemented")
         }

         override fun hasNextWindow(): Boolean {
             TODO("Not yet implemented")
         }

         override fun hasNextMediaItem(): Boolean {
             TODO("Not yet implemented")
         }

         override fun next() {
             TODO("Not yet implemented")
         }

         override fun seekToNextWindow() {
             TODO("Not yet implemented")
         }

         override fun seekToNextMediaItem() {
             controller?.seekToNextMediaItem()
         }

         override fun seekToNext() {
             TODO("Not yet implemented")
         }

         override fun setPlaybackParameters(playbackParameters: PlaybackParameters) {
             TODO("Not yet implemented")
         }

         override fun setPlaybackSpeed(speed: Float) {
             TODO("Not yet implemented")
         }

         override fun getPlaybackParameters(): PlaybackParameters {
             TODO("Not yet implemented")
         }

         override fun stop() {
             controller?.stop()
         }

         override fun release() {
             controller?.release()
         }

         override fun getCurrentTracks(): Tracks {
             TODO("Not yet implemented")
         }

         override fun getTrackSelectionParameters(): TrackSelectionParameters {
             TODO("Not yet implemented")
         }

         override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) {
             TODO("Not yet implemented")
         }

         override fun getMediaMetadata(): MediaMetadata {
             TODO("Not yet implemented")
         }

         override fun getPlaylistMetadata(): MediaMetadata {
             TODO("Not yet implemented")
         }

         override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) {
             TODO("Not yet implemented")
         }

         override fun getCurrentManifest(): Any? {
             TODO("Not yet implemented")
         }

         override fun getCurrentTimeline(): Timeline {
             TODO("Not yet implemented")
         }

         override fun getCurrentPeriodIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getCurrentWindowIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getCurrentMediaItemIndex(): Int = controller!!.currentMediaItemIndex

         override fun getNextWindowIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getNextMediaItemIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getPreviousWindowIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getPreviousMediaItemIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getCurrentMediaItem(): MediaItem? = controller?.currentMediaItem


         override fun getMediaItemCount(): Int {
             return controller!!.mediaItemCount
         }

         override fun getMediaItemAt(index: Int): MediaItem {
             TODO("Not yet implemented")
         }

         override fun getDuration(): Long = controller?.duration ?: 0

         override fun getCurrentPosition(): Long = controller?.currentPosition ?: 1

         override fun getBufferedPosition(): Long {
             TODO("Not yet implemented")
         }

         override fun getBufferedPercentage(): Int {
             TODO("Not yet implemented")
         }

         override fun getTotalBufferedDuration(): Long {
             TODO("Not yet implemented")
         }

         override fun isCurrentWindowDynamic(): Boolean {
             TODO("Not yet implemented")
         }

         override fun isCurrentMediaItemDynamic(): Boolean {
             TODO("Not yet implemented")
         }

         override fun isCurrentWindowLive(): Boolean {
             TODO("Not yet implemented")
         }

         override fun isCurrentMediaItemLive(): Boolean {
             TODO("Not yet implemented")
         }

         override fun getCurrentLiveOffset(): Long {
             TODO("Not yet implemented")
         }

         override fun isCurrentWindowSeekable(): Boolean {
             TODO("Not yet implemented")
         }

         override fun isCurrentMediaItemSeekable(): Boolean {
             TODO("Not yet implemented")
         }

         override fun isPlayingAd(): Boolean {
             TODO("Not yet implemented")
         }

         override fun getCurrentAdGroupIndex(): Int {
             TODO("Not yet implemented")
         }

         override fun getCurrentAdIndexInAdGroup(): Int {
             TODO("Not yet implemented")
         }

         override fun getContentDuration(): Long  = controller?.contentDuration?:0

         override fun getContentPosition(): Long {
             TODO("Not yet implemented")
         }

         override fun getContentBufferedPosition(): Long {
             TODO("Not yet implemented")
         }

         override fun getAudioAttributes(): AudioAttributes {
             TODO("Not yet implemented")
         }

         override fun setVolume(volume: Float) {
             TODO("Not yet implemented")
         }

         override fun getVolume(): Float {
             TODO("Not yet implemented")
         }

         override fun clearVideoSurface() {
             TODO("Not yet implemented")
         }

         override fun clearVideoSurface(surface: Surface?) {
             TODO("Not yet implemented")
         }

         override fun setVideoSurface(surface: Surface?) {
             TODO("Not yet implemented")
         }

         override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
             TODO("Not yet implemented")
         }

         override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
             TODO("Not yet implemented")
         }

         override fun setVideoSurfaceView(surfaceView: SurfaceView?) {
             TODO("Not yet implemented")
         }

         override fun clearVideoSurfaceView(surfaceView: SurfaceView?) {
             TODO("Not yet implemented")
         }

         override fun setVideoTextureView(textureView: TextureView?) {
             TODO("Not yet implemented")
         }

         override fun clearVideoTextureView(textureView: TextureView?) {
             TODO("Not yet implemented")
         }

         override fun getVideoSize(): VideoSize {
             TODO("Not yet implemented")
         }

         override fun getSurfaceSize(): Size {
             TODO("Not yet implemented")
         }

         override fun getCurrentCues(): CueGroup {
             TODO("Not yet implemented")
         }

         override fun getDeviceInfo(): DeviceInfo {
             TODO("Not yet implemented")
         }

         override fun getDeviceVolume(): Int {
             TODO("Not yet implemented")
         }

         override fun isDeviceMuted(): Boolean {
             TODO("Not yet implemented")
         }

         override fun setDeviceVolume(volume: Int) {
             TODO("Not yet implemented")
         }

         override fun setDeviceVolume(volume: Int, flags: Int) {
             TODO("Not yet implemented")
         }

         override fun increaseDeviceVolume() {
             TODO("Not yet implemented")
         }

         override fun increaseDeviceVolume(flags: Int) {
             TODO("Not yet implemented")
         }

         override fun decreaseDeviceVolume() {
             TODO("Not yet implemented")
         }

         override fun decreaseDeviceVolume(flags: Int) {
             TODO("Not yet implemented")
         }

         override fun setDeviceMuted(muted: Boolean) {
             TODO("Not yet implemented")
         }

         override fun setDeviceMuted(muted: Boolean, flags: Int) {
             TODO("Not yet implemented")
         }


     }
 }