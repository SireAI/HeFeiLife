package com.sire.bbsmodule.Views.EmojiView.category;

import android.support.annotation.NonNull;

import com.sire.bbsmodule.Views.EmojiView.EmojiProvider;
import com.sire.bbsmodule.Views.EmojiView.emoji.EmojiCategory;

public final class IosEmojiProvider implements EmojiProvider {
  @Override @NonNull
  public EmojiCategory[] getCategories() {
    return new EmojiCategory[] {
      new PeopleCategory()
    };
  }
}
