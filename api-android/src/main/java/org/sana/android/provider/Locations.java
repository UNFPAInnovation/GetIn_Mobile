package org.sana.android.provider;

import android.net.Uri;

import org.sana.core.Location;

/**
 * Created by winkler.em@gmail.com, on 06/28/2016.
 */
public class Locations {

    private Locations() {
    }

    /**
     * The content:// style URI for this content provider.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + Models.AUTHORITY + "/core/location");

    /**
     * The MIME type for a directory of procedures.
     */
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + Models.AUTHORITY + ".location";

    /**
     * The MIME type of single procedure.
     */
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + Models.AUTHORITY + ".location";

    /**
     * The default sort order.
     */
    public static final String DEFAULT_SORT_ORDER = "modified DESC";

    /**
     * Column definitions for the Observer table.
     *
     * @author Sana Development
     */
    public static interface Contract extends BaseContract<Location> {

        public static final String NAME = "name";
        public static final String CODE = "code";

    }
}
