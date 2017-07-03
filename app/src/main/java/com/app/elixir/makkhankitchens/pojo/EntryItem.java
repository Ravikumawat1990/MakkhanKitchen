package com.app.elixir.makkhankitchens.pojo;

import com.app.elixir.makkhankitchens.interfac.Item;

/**
 * Created by Elixir on 19-Aug-2016.
 */
public class EntryItem implements Item {
    public final String title;
    public final String subtitle;

    public EntryItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
