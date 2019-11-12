package io.seroo.sampleplayer

sealed class BaseBindViewHolder() {
    abstract class<T> BaseBindViewHolderT1(item: T1, position: Int): BaseBindViewHolder()
}