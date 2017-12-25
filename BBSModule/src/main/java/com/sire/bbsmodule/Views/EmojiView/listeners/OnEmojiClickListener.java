package com.sire.bbsmodule.Views.EmojiView.listeners;

import android.support.annotation.NonNull;

import com.sire.bbsmodule.Views.EmojiView.EmojiImageView;
import com.sire.bbsmodule.Views.EmojiView.emoji.Emoji;


public interface OnEmojiClickListener {
  void onEmojiClick(@NonNull EmojiImageView emoji, @NonNull Emoji imageView);
}
