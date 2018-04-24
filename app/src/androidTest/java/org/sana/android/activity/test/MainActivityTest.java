/**
 * Copyright (c) 2013, Sana
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sana nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL Sana BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF 
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sana.android.activity.test;

import org.junit.Test;
import org.sana.R;
import org.sana.android.activity.MainActivity;
import org.sana.android.procedure.Procedure;
import org.sana.api.task.EncounterTask;
import org.sana.core.Observer;
import org.sana.core.Patient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Sana Development
 *
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class MainActivityTest extends ActivityInstrumentationTestCase<MainActivity> {
	/**
	 * @param
	 */
	public MainActivityTest() {
		super("org.sana", MainActivity.class);
	}

	public static final String TAG = MainActivityTest.class.getSimpleName();

		MainActivity mainy = new MainActivity();
		EncounterTask noww = new EncounterTask();



		Calendar cal = Calendar.getInstance();
		@Test
		public void testCalculateFirstVisit() throws Exception {
			// onView(withText("Say hello!")).perform(click());

			Patient patient = new Patient();
			Procedure procedure =Procedure.fromRawResource(getActivity(), R.raw.mapping_form_vht);
			Observer assignedTo = new Observer();
 
			patient.getANC_status();
			patient.getLMD();
			patient.setLMD(new Date(2016, 8, 23));
			patient.setDob(new Date(1999, 8, 24));
			patient.setANC_status("yes");
			assignedTo.getUuid();
			assignedTo.setUuid(UUID.randomUUID().toString());

			Date date =cal.getTime();

			Date lmd =patient.getLMD();
			Date dob =patient.getDob();
			long days = date.getTime() - lmd.getTime();

			long age = date.getTime() - dob.getTime();
			long no_LMD = days /( 60 * 24 * 60 * 1000);



			cal.setTime(new Date());
       /* Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        Date x = c1.getTime();
        long days = x.getTime() - lmd.getTime();
        int day_of_week = c1.get(Calendar.DAY_OF_WEEK);*/

			Date due_on = noww.due_on;
			//EncounterTask due_on1 =mainy.calculateFirstVisit(patient, procedure ,assignedTo);
			//assertEquals("task due on", due_on1, new Date());





		}



		@Test
		public void testGetVisits() throws Exception {

		}

		@Test
		public void testCreateTasks() throws Exception {


		}

		@Test
		public void testHandleBroadcast() throws Exception {

		}
	}


