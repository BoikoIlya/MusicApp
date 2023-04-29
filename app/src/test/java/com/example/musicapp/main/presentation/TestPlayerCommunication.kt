package com.example.musicapp.main.presentation

import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.*
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import com.example.musicapp.app.core.PlayerControlsCommunication
import com.example.musicapp.player.presentation.TrackPlaybackPositionCommunication
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by HP on 17.03.2023.
 **/

class TestPlayerControlsCommunication: PlayerControlsCommunication{
    var data: PlayerControlsState = PlayerControlsState.Disabled
    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = Unit

    override fun map(newValue: PlayerControlsState) {
        data = newValue
    }

}

class TestTrackPlaybackPositionCommunication: TrackPlaybackPositionCommunication{
    var data = Pair(0,"")

    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<Pair<Int, String>>,
    ) = Unit

    override fun map(newValue: Pair<Int, String>) {
        data = newValue
    }

}
class TestCurrentQueueCommunication: CurrentQueueCommunication{
    var data = emptyList<MediaItem>().toMutableList()


    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    ) = Unit

    override fun map(newValue: List<MediaItem>) {
        data.clear()
        data.addAll(newValue)
    }

}

class TestSelectedTrackCommunication: SelectedTrackCommunication{
    var data: MediaItem = MediaItem.Builder().setMediaId("2").build()
    override suspend fun collect(
        lifecycleOwner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    ) = Unit
    override fun map(newValue: MediaItem) {
       data = newValue
    }


}



class TestPlayerCommunication:PlayerCommunication {

    val stateList = emptyList<PlayerCommunicationState>().toMutableList()

    override fun map(state: PlayerCommunicationState) {
        stateList.add(state)
    }

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = Unit

    override suspend fun collectCurrentQueue(
        owner: LifecycleOwner,
        collector: FlowCollector<List<MediaItem>>,
    )  = Unit

    override suspend fun collectSelectedTrack(
        owner: LifecycleOwner,
        collector: FlowCollector<MediaItem>,
    )  = Unit
}

class TestMediaController: MediaControllerWrapper{

    var currentPositionPlayback:Long = 0
    var durationn: Long = 0
    var seekPos:Long = 0
    var mediaItemList = mutableListOf<MediaItem>()
    var currenttMediaItem = MediaItem.Builder().build()
    var currenttMediaItemIndex = 0
    var isPrepared = false
    var isPlayingg = false
    var repeatModee = REPEAT_MODE_OFF
    var seekToDefaultPositionIndex = 0
    var shuffleModeEnabledd = false
    var nextTrackClicked = 0
    var previousTrackClicked = 0

    override fun getApplicationLooper(): Looper {
        TODO("Not yet implemented")
    }

    override fun addListener(listener: Player.Listener) {}

    override fun removeListener(listener: Player.Listener) {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>) {
        mediaItemList.clear()
        mediaItemList.addAll(mediaItems)
    }

    override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) {
    }

    override fun setMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long,
    ) {
        TODO("Not yet implemented")
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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

    override fun removeMediaItem(index: Int) {
        TODO("Not yet implemented")
    }

    override fun removeMediaItems(fromIndex: Int, toIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun clearMediaItems() {
        TODO("Not yet implemented")
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
        isPrepared = true
    }

    override fun getPlaybackState(): Int {
        TODO("Not yet implemented")
    }

    override fun getPlaybackSuppressionReason(): Int {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getPlayerError(): PlaybackException? {
        TODO("Not yet implemented")
    }

    override fun play() {
        isPlayingg = true
    }

    override fun pause() {
        isPlayingg = false
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        isPlayingg = true
    }

    override fun getPlayWhenReady(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setRepeatMode(repeatMode: Int) {
        repeatModee = repeatMode
    }

    override fun getRepeatMode(): Int {
        return repeatModee
    }

    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        shuffleModeEnabledd = shuffleModeEnabled
    }

    override fun getShuffleModeEnabled(): Boolean {
        return shuffleModeEnabledd
    }

    override fun isLoading(): Boolean {
        TODO("Not yet implemented")
    }

    override fun seekToDefaultPosition() {
    }

    override fun seekToDefaultPosition(mediaItemIndex: Int) {
        seekToDefaultPositionIndex = mediaItemIndex
    }

    override fun seekTo(positionMs: Long) {
        seekPos = positionMs
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

    override fun previous() {}

    override fun seekToPreviousWindow() {
        TODO("Not yet implemented")
    }

    override fun seekToPreviousMediaItem() {
        previousTrackClicked++
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
        nextTrackClicked++
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
        TODO("Not yet implemented")
    }

    override fun stop(reset: Boolean) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
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

    override fun getCurrentMediaItemIndex(): Int {
        return currentMediaItemIndex
    }

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

    override fun getCurrentMediaItem(): MediaItem? {
       return currenttMediaItem
    }

    override fun getMediaItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getMediaItemAt(index: Int): MediaItem {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Long {
       return durationn
    }

    override fun getCurrentPosition(): Long {
        return currentPositionPlayback
    }

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

    override fun getContentDuration(): Long {
        TODO("Not yet implemented")
    }

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

    override fun increaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun decreaseDeviceVolume() {
        TODO("Not yet implemented")
    }

    override fun setDeviceMuted(muted: Boolean) {
        TODO("Not yet implemented")
    }

}