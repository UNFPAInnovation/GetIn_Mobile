package org.sana.android.db;

import java.util.ArrayList;
import java.util.List;

import org.sana.android.content.Uris;
import org.sana.android.provider.BaseContract;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Collection of database helper methods.
 * 
 * @author Sana Development
 *
 */
public final class DBUtils {

    public static final String NS = " ";
    public static final String EQUALS = "=";
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String SELECTION_ARG = "?";
    public static final String CAST = "CAST";
    public static final String AS = "AS";
    public static final String LEFT_PARENTHETICAL = "(";
    public static final String RIGHT_PARENTHETICAL = ")";
    public static final String TEXT = "TEXT";

    private DBUtils() {
    }

    /**
     * Returns the selection statement as:<br/>
     * ( uuid = "uri.getLastPathSegment()" ) AND ( selection )
     *
     * @param uri
     * @param whereClause
     * @return
     */
    public static String getWhereClauseWithUUID(Uri uri, String whereClause) {
        String select = whereClause;
        select = concatenateWhere(whereClause,
                BaseContract.UUID + " = '" + uri.getLastPathSegment() + "'");
        return select;
    }

    /**
     * Returns the selection statement as
     * ( _ID = "uri.getPathSegments().get(1)" ) AND ( selection )
     * or as the original based on whether the uri match was a dir or item.
     * Relies on matcher values for *.dir being even integers.
     *
     * @param uri
     * @param whereClause
     * @return
     */
    public static String getWhereClauseWithID(Uri uri, String whereClause) {
        return concatenateWhere(whereClause,
                BaseContract._ID + " = " + uri.getLastPathSegment());
    }

    /**
     * Returns the selection statement as
     * ( _ID = "uri.getPathSegments().get(1)" ) AND ( selection )
     * or as the original based on whether the uri match was a dir or item.
     * Relies on matcher values for *.dir being even integers.
     *
     * @param uri
     * @param whereClause
     * @return
     */
    public static String getWhereClause(Uri uri, int match, String whereClause) {
        String select = whereClause;
        if (Uris.isItemType(uri)) {
            if ((Uris.getTypeDescriptor(uri) & Uris.ITEM_ID) != 0)
                select = concatenateWhere(whereClause,
                        BaseColumns._ID + " LIKE " + uri.getLastPathSegment());
            else
                select = concatenateWhere(whereClause,
                        BaseContract.UUID + " LIKE '" + uri.getLastPathSegment() + "'");
        }
        return select;
    }

    /**
     * Constructs a SQL WHERE clause with {@link android.provider.BaseColumns#_ID _ID}
     * prepended.
     *
     * @param id         The value of the _ID column
     * @param wherClause
     * @return
     */
    public static String getWhereClause(long id, String wherClause) {
        return concatenateWhere(BaseColumns._ID + " LIKE " + id, wherClause);
    }

    /**
     * @param cursor
     * @param column
     * @return
     */
    public static List<String> dumpStringColumn(Cursor cursor, int column) {
        List<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(column));
        }
        return list;
    }

    /**
     * Appends one set of selection args to another. This is useful when adding
     * a selection argument to a user provided set.
     *
     * @param originalValues
     * @param newValues
     * @return
     */
    public static String[] appendSelectionArgs(String[] originalValues,
                                               String[] newValues) {
        int l1 = (originalValues != null) ? originalValues.length : 0;
        int l2 = (newValues != null) ? newValues.length : 0;
        String[] result = new String[l1 + l2];
        for (int index = 0; index < l1 + l2; index++) {
            result[index] = (index < l1) ? originalValues[index] : newValues[index];
        }
        return result;
    }

    /**
     * Concatenates two SQL WHERE clauses, handling empty or null values.
     *
     * @param arg1
     * @param arg2
     * @return
     */
    public static String concatenateWhere(String arg1, String arg2) {
        String cat = "";
        // Both non empty concatenate
        if (!TextUtils.isEmpty(arg1) && !TextUtils.isEmpty(arg2))
            cat = String.format("%s AND %s", arg1, arg2);
        else {
            // at least one empty
            if (!TextUtils.isEmpty(arg1))
                cat = arg1;
            else if (!TextUtils.isEmpty(arg2))
                cat = arg2;
        }
        //cat.replace("=", " LIKE ");
        return cat;
    }

    /**
     * Converts any Uri query string into a select statement by taking
     * every key value pair in the query into "key = 'value'".
     *
     * @param uri
     * @return
     */
    public static String convertUriQueryToSelect(Uri uri) {
        String qString = uri.getQuery();
        // Shortcut out for null query
        if (TextUtils.isEmpty(qString))
            return null;
        StringBuilder select = new StringBuilder();
        String[] rawQuery = uri.getQuery().split(",");
        for (int index = 0; index < rawQuery.length; index++) {
            String[] kv = rawQuery[index].split("=");
            // append space after first key value pair
            if (index > 0) select.append(" ");
            select.append(String.format("%s LIKE '%s'", kv[0], kv[1]));
        }
        return select.toString();
    }

    public static String concatenateUriQueryToSelect(String select, Uri uri) {
        String qString = uri.getQuery();
        // Shortcut out for null query
        if (TextUtils.isEmpty(qString))
            return null;
        StringBuilder builder = new StringBuilder();
        if(!TextUtils.isEmpty(select)) {
            builder.append(select);
        }

        int position = 0;
        for (String key : uri.getQueryParameterNames()) {
            if(position > 0)
                builder.append(" AND ");
            builder.append(key)
                    .append("=")
                    .append("'")
                    .append(uri.getQueryParameter(key))
                    .append("'");
            position++;
        }
        String[] rawQuery = uri.getQuery().split(",");
        for (int index = 0; index < rawQuery.length; index++) {
            String[] kv = rawQuery[index].split("=");
            // append space after first key value pair
            if (index > 0) builder.append(" ");
            builder.append(String.format("%s LIKE '%s'", kv[0], kv[1]));
        }
        return select.toString();
    }

    public static StringBuilder castAs(StringBuilder builder, String column, String expression) {
        builder.append(column)
                .append(LEFT_PARENTHETICAL)
                .append(CAST)
                .append(expression)
                .append(NS)
                .append(AS)
                .append(NS)
                .append(TEXT)
                .append(RIGHT_PARENTHETICAL);
        return builder;
    }

    public static StringBuilder and(StringBuilder builder, String x, String y) {
        return builder.append(x)
                .append(NS)
                .append(AND)
                .append(NS)
                .append(y);
    }


    public static StringBuilder or(StringBuilder builder, String x, String y) {
        return builder.append(x)
                .append(NS)
                .append(OR)
                .append(NS)
                .append(y);
    }
}
