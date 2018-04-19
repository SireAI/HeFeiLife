package com.sire.corelibrary.View.EmojiView.category;

import android.support.annotation.NonNull;

import com.sire.corelibrary.View.EmojiView.EmojiProvider;
import com.sire.corelibrary.View.EmojiView.emoji.EmojiCategory;

public final class IosEmojiProvider implements EmojiProvider {
  @Override @NonNull
  public EmojiCategory[] getCategories() {
    return new EmojiCategory[] {
      new PeopleCategory()
    };
  }
}
