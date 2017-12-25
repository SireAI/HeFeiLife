package com.sire.bbsmodule.Views.EmojiView.listeners;

import android.support.annotation.NonNull;

import com.sire.bbsmodule.Views.EmojiView.EmojiImageView;
import com.sire.bbsmodule.Views.EmojiView.emoji.Emoji;


public interface OnEmojiLongClickListener {
  void onEmojiLongClick(@NonNull EmojiImageView view, @NonNull Emoji emoji);
}
