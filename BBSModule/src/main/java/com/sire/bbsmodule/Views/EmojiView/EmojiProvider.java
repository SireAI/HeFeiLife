package com.sire.bbsmodule.Views.EmojiView;

import android.support.annotation.NonNull;

import com.sire.bbsmodule.Views.EmojiView.emoji.EmojiCategory;


/**
 * Interface for a custom emoji implementation that can be used with {@link EmojiManager}.
 *
 * @since 0.4.0
 */
public interface EmojiProvider {
  /**
   * @return The Array of categories.
   * @since 0.4.0
   */
  @NonNull
  EmojiCategory[] getCategories();
}
