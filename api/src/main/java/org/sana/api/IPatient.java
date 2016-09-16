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
package org.sana.api;

import java.util.Date;

/**
 * @author Sana Development
 *
 */
public interface IPatient extends ISubject{

	/**
	 * @return the given_name
	 */
	public abstract String getGiven_name();

	/**
	 * @return the family_name
	 */
	public abstract String getFamily_name();

	/**
	 * @return the dob
	 */
	public abstract Date getDob();

	/**
	 * @return the gender
	 */
	public abstract String getGender();
	/**
	 * @return the pNumber
	 */
	public abstract String getpNumber();
	/**
	 * @return the pNumber
	 */
	public abstract String getHolder_pNumber();
	/**
	 * @return the pNumber
	 */
	public abstract Date getLMD();
	/**
	 * @return the pNumber
	 */
	public abstract String getMarital_status();
	/**
	 * @return the pNumber
	 */
	public abstract String getEducation_level();
	/**
	 * @return the pNumber
	 */
	public abstract String getContraceptive_use();
	/**
	 * @return the pNumber
	 */
	public abstract String getANC_status();
	/**
	 * @return the pNumber
	 */
	public abstract String getANC_visit();
	/**
	 * @return the pNumber
	 */
	public abstract String getEDD();
	/**
	 * @return the receive_sms
	 */
	public abstract String getReceive_sms();
	/**
	 * @return the follow_up
	 */
	public abstract String getFollow_up();
	/**
	 * @return the CUG_status
	 */
	public abstract String getCUG_status();
	/**
	 * @return the comment
	 */
	public abstract String getComment();
	/**
	 * @return the bleeding
	 */
	public abstract String getBleeding();
	/**
	 * @return the fever
	 */
	public abstract String getFever();
	/**
	 * @return the swollen_feet
	 */
	public abstract String getSwollen_feet();
	/**
	 * @return the blurred_vision
	 */
	public abstract String getBlurred_vision();

	/**
	 * @return the image
	 */
	public abstract java.net.URI getImage();
	
	public abstract ILocation getLocation();

    public abstract String getSystemId();

}
