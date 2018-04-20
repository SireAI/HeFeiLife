package com.sire.corelibrary.View.EmojiView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.sire.corelibrary.View.EmojiView.emoji.Emoji;
import com.sire.corelibrary.View.EmojiView.listeners.OnEmojiClickListener;
import com.sire.corelibrary.View.EmojiView.listeners.OnEmojiLongClickListener;

import java.util.Collection;

final class RecentEmojiGridView extends EmojiGridView {
  private RecentEmoji recentEmojis;

  RecentEmojiGridView(@NonNull final Context context) {
    super(context);
  }

  public RecentEmojiGridView init(@Nullable final OnEmojiClickListener onEmojiClickListener,
                                                       @Nullable final OnEmojiLongClickListener onEmojiLongClickListener,
                                                       @NonNull final RecentEmoji recentEmoji) {
    recentEmojis = recentEmoji;

    final Collection<Emoji> emojis = recentEmojis.getRecentEmojis();
    emojiArrayAdapter = new EmojiArrayAdapter(getContext(), emojis.toArray(new Emoji[emojis.size()]), null,
            onEmojiClickListener, onEmojiLongClickListener);

    setAdapter(emojiArrayAdapter);

    return this;
  }

  public void invalidateEmojis() {
    emojiArrayAdapter.updateEmojis(recentEmojis.getRecentEmojis());
  }
}
