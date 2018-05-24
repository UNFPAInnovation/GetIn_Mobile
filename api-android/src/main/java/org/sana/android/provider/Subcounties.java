package org.sana.android.provider;

import android.net.Uri;

import org.sana.core.Subcounty;

/**
 */
public class Subcounties {

    private Subcounties() {
    }

    /**
     * The content:// style URI for this content provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + Models.AUTHORITY + "/core/subcounty");

    /**
     * The MIME type for a directory of objects.
     */
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + Models.AUTHORITY + ".subcounty";

    /**
     * The MIME type of single object.
     */
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + Models.AUTHORITY + ".subcounty";

    /**
     * The default sort order.
     */
    public static final String DEFAULT_SORT_ORDER = Contract.NAME +  " ASC";

    /**
     * Column definitions for the table.
     */
    public static interface Contract extends BaseContract<Subcounty> {

        public static final String NAME = "name";
        public static final String DISTRICT = "district";
        public static final String COUNTY = "county";

    }
}
