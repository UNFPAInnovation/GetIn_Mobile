package org.sana.android.provider;

import android.net.Uri;

import org.sana.core.AmbulanceDriver;

/**
 * Created by winkler.em@gmail.com, on 11/18/2016.
 */
public class AmbulanceDrivers {
    public static final String TAG = AmbulanceDrivers.class.getSimpleName();

    public static final String AUTHORITY = "org.sana.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * The MIME type of CONTENT_URI providing a directory of patients.
     */
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/org.sana.ambulancedriver";

    /** The content type of {@link #CONTENT_URI} for a single instance. */
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/org.sana.ambulancedriver";

    /**
     * The content:// style URI for this content provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/core/ambulancedriver");

    public interface Contract extends BaseContract<AmbulanceDriver>{
        public static final String PHONE_NUMBER = "phone_number";
    }

}
