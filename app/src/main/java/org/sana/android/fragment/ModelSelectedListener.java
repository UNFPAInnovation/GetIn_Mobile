package org.sana.android.fragment;

import org.sana.core.Model;

/**
 * Created by winkler.em@gmail.com, on 12/23/2016.
 */
public interface ModelSelectedListener<T extends Model> {

    public void onModelSelected(T instance);
}
