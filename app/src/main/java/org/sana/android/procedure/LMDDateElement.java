package org.sana.android.procedure;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import org.sana.R;
import org.sana.android.Constants;
import org.sana.android.app.Locales;
import org.sana.android.app.Preferences;
import org.sana.android.widget.CustomDatePicker;
import org.sana.util.DateUtil;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Marting on 15/09/2016.
 */
public class LMDDateElement extends ProcedureElement {
    class DateValueSetter implements CustomDatePicker.OnDateChangedListener{
        final String TAG = DateValueSetter.class.getSimpleName();
        @Override
        public void onDateChanged(CustomDatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            Log.i(TAG, "onDateChanged()");
            Log.d(TAG, "current value: " + answer);
            dateValue.set(Calendar.YEAR, year);
            dateValue.set(Calendar.MONTH, monthOfYear);
            dateValue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateValue.add(Calendar.MONTH, 9);
            answer = DateUtil.format(dateValue.getTime());
            Log.d(TAG, "new value: " + answer);
            Log.d(TAG, "the age is: " + getAge(year,monthOfYear, dayOfMonth));
        }

    }
    CustomDatePicker dp = null;
    Date dateAnswer = new Date();
    Calendar dateValue = Calendar.getInstance(TimeZone.getDefault());

    /** {@inheritDoc} */
    @Override
    protected View createView(Context c) {
        dp = new CustomDatePicker(c);
        String locale = Preferences.getString(c, Constants.PREFERENCE_LOCALE, "en");
        Locales.updateLocale(c, locale);
        String[] months = c.getResources().getStringArray(R.array.months_long_format);
        dp.setMonthValues(months);
        dp.init(dateValue.get(Calendar.YEAR),
                dateValue.get(Calendar.MONTH),
                dateValue.get(Calendar.DAY_OF_MONTH),
                new DateValueSetter());
        return encapsulateQuestion(c, dp);
    }

    /** {@inheritDoc} */
    @Override
    public String getAnswer() {
        if(!isViewActive())
            return answer;
        else {
            dateValue.set(Calendar.YEAR, dp.getYear());
            dateValue.set(Calendar.MONTH, dp.getMonth());
            dateValue.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
            return DateUtil.format(dateValue.getTime());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ElementType getType() {
        return ElementType.DATE;
    }

    /** {@inheritDoc} */
    @Override
    public void setAnswer(String answer) {
        if(!TextUtils.isEmpty(answer)){
            try {
                dateValue.setTime(DateUtil.parseDate(answer));
            } catch (ParseException e) {
                Log.e(DateElement.class.getSimpleName(),
                        "Invalid date string? " + answer);
                e.printStackTrace();
            }
        }

        if (isViewActive()) {
            dp.updateDate(dateValue.get(Calendar.YEAR),
                    dateValue.get(Calendar.MONTH),
                    dateValue.get(Calendar.DAY_OF_MONTH));
        }
    }

    private LMDDateElement(String id, String question, String answer,
                        String concept, String figure, String audio)
    {
        super(id, question, answer, concept, figure, audio);
    }

    /**
     * Creates the element from an XML procedure definition.
     *
     * @param id The unique identifier of this element within its procedure.
     * @param question The text that will be displayed to the user as a question
     * @param answer The result of data capture.
     * @param concept A required categorization of the type of data captured.
     * @param figure An optional figure to display to the user.
     * @param audio An optional audio prompt to play for the user.
     * @param node The source xml node.
     * @return A new element.
     * @throws ProcedureParseException if an error occurred while parsing
     * 		additional information from the Node
     */
    public static LMDDateElement fromXML(String id, String question, String answer,
                                      String concept, String figure, String audio, Node node) throws
            ProcedureParseException
    {
        return new LMDDateElement(id, question, answer, concept, figure, audio);
    }

    /**
     * method to calculate the age of the girl
     */
    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    /**
     * method to add 9 months to last normal menstrual period
     */
//    public Date EDD
}
