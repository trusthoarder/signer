package com.mike_burns.signer.infrastructure.ui;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * A simple list adapter for rendering a fixed list of items into a {@link android.widget.ListView}.
 *
 * @param <T> The type of the objects in your list.
 */
public abstract class BasicAdapter<T> extends BaseAdapter
{
    private final int viewId;
    private final List<T> items;

    /**
     * @param viewId the resource id of the view that should be rendered for each row.
     * @param items your list of items.
     */
    public BasicAdapter( int viewId, List<T> items )
    {
        this.viewId = viewId;
        this.items = items;
    }

    /**
     * This is the only thing you need to worry about. Implement this method, and use the item you're given to render
     * its data into the view you are given.
     *
     * @param item The item for the current row to render.
     * @param view The view for the current row to render.
     */
    protected abstract void render( T item, View view );

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public T getItem( int i )
    {
        return items.get(i);
    }

    @Override
    public long getItemId( int i )
    {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup )
    {
        if(view == null)
        {
            view = View.inflate( viewGroup.getContext(), viewId, null );
        }

        render( items.get(i), view );

        return view;
    }
}