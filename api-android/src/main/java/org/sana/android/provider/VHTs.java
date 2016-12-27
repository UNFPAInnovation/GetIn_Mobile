package org.sana.android.provider;

import android.net.Uri;

import org.sana.core.VHT;

/**
 * Created by Marting on 21/11/2016.
 */

public class VHTs {
    public static final String TAG = VHTs.class.getSimpleName();

    public static final String AUTHORITY = "org.sana.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * The MIME type of CONTENT_URI providing a directory of vhts.
     */
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/org.sana.vht";

    /** The content type of {@link #CONTENT_URI} for a single instance. */
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/org.sana.vht";

    /**
     * The content:// style URI for this content provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/core/vht");
    /**
     * sort order according to the first name
     */
    public static final String FIRST_NAME_SORT_ORDER = Contract.FIRST_NAME + " ASC";


    public interface Contract extends BaseContract<VHT>{
        public static final String PHONE_NUMBER = "phone_number";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String LOCATION = "location";
    }
}
