package com.sire.corelibrary.View.EmojiView.listeners;

import android.support.annotation.NonNull;

import com.sire.corelibrary.View.EmojiView.EmojiImageView;
import com.sire.corelibrary.View.EmojiView.emoji.Emoji;


public interface OnEmojiClickListener {
  void onEmojiClick(@NonNull EmojiImageView emoji, @NonNull Emoji imageView);
}
