package org.sana.android.provider;

import android.net.Uri;

import org.sana.core.District;

/**
 */
public class Districts {

    private Districts() {
    }

    /**
     * The content:// style URI for this content provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + Models.AUTHORITY + "/core/district");

    /**
     * The MIME type for a directory of objects.
     */
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + Models.AUTHORITY + ".district";

    /**
     * The MIME type of single object.
     */
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + Models.AUTHORITY + ".district";

    /**
     * The default sort order.
     */
    public static final String DEFAULT_SORT_ORDER = Contract.NAME +  " ASC";

    /**
     * Column definitions for the table.
     */
    public static interface Contract extends BaseContract<District> {

        public static final String NAME = "name";

    }
}
