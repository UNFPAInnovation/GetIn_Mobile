/**
 * 
 */
package org.sana.core;

import java.net.URI;
import java.util.Date;

import org.sana.api.ILocation;
import org.sana.api.IPatient;

import com.google.gson.annotations.Expose;


/**
 * A subject in the medical domain.
 * 
 * @author Sana Development
 *
 */
public class Patient extends Subject implements IPatient{

    @Expose
    public String given_name;
    @Expose
    public String family_name;
    @Expose
    public Date dob;
    @Expose
    public String gender;
    @Expose
    public URI image;
    @Expose
    public Location location;
    @Expose
    public String system_id;
	@Expose
	boolean confirmed = true;
    @Expose
    boolean dobEstimated = false;
	@Expose
	public String pNumber;
	@Expose
	public String holder_pNumber;
	@Expose
	public Date LMD;
	@Expose
	public String marital_status;
	@Expose
	public String education_level;
	@Expose
	public String contraceptive_use;
	@Expose
	public String ANC_status;
	@Expose
	public String ANC_visit;
	@Expose
	public String EDD;
	@Expose
	public String receive_sms;
	@Expose
	public String follow_up;
	@Expose
	public String CUG_status;
	@Expose
	public String comment;
	@Expose
	public String bleeding;
	@Expose
	public String fever;
	@Expose
	public String swollen_feet;
	@Expose
	public String blurred_vision;

	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getGiven_name()
	 */
	@Override
	public String getGiven_name() {
		return given_name;
	}
	/**
	 * @param given_name the given_name to set
	 */
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getpNumber()
	 */
	@Override
	public String getpNumber() {
		return pNumber;
	}
	/**
	 * @param pNumber the pNumber to set
	 */
	public void setpNumber(String pNumber) {
		this.pNumber = pNumber;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getFamily_name()
	 */
	@Override
	public String getFamily_name() {
		return family_name;
	}
	/**
	 * @param family_name the family_name to set
	 */
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getDob()
	 */
	@Override
	public Date getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getGender()
	 */
	@Override
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getImage()
	 */
	@Override
	public URI getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(URI image) {
		this.image = image;
	}
	
	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getLocation()
	 */
	@Override
	public ILocation getLocation() {
		return location;
	}

	@Override
	public String getSystemId() {
		return system_id;
	}
	public void setSystemId(String system_id){
		this.system_id = system_id;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getHolder_pNumber()
	 */
	@Override
	public String getHolder_pNumber() {
		return holder_pNumber;
	}
	/**
	 * @param holder_pNumber the holder_pNumber to set
	 */
	public void setHolder_pNumber(String holder_pNumber) {
		this.holder_pNumber = holder_pNumber;
	}

	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getHolder_pNumber()
	 */
	@Override
	public Date getLMD() {
		return LMD;
	}
	/**
	 * @param LMD the LMD to set
	 */
	public void setLMD(Date LMD) {
		this.LMD = LMD;
	}

	@Override
	public String getMarital_status() {
		return marital_status;
	}
	/**
	 * @param marital_status the marital_status to set
	 */
	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	@Override
	public String getEducation_level() {
		return education_level;
	}
	/**
	 * @param education_level the education_level to set
	 */
	public void setEducation_level(String education_level) {
		this.education_level = education_level;
	}

	@Override
	public String getContraceptive_use() {
		return contraceptive_use;
	}
	/**
	 * @param contraceptive_use the contraceptive_use to set
	 */
	public void setContraceptive_use(String contraceptive_use) {
		this.contraceptive_use = contraceptive_use;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getANC_status()
	 */
	@Override
	public String getANC_status() {
		return ANC_status;
	}
	/**
	 * @param ANC_status the ANC_status to set
	 */
	public void setANC_status(String ANC_status) {
		this.ANC_status = ANC_status;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getANC_visit()
	 */
	@Override
	public String getANC_visit() {
		return ANC_visit;
	}
	/**
	 * @param ANC_visit the ANC_visit to set
	 */
	public void setANC_visit(String ANC_visit) {
		this.ANC_visit = ANC_visit;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getEDD()
	 */
	@Override
	public String getEDD() {
		return EDD;
	}
	/**
	 * @param EDD the EDD to set
	 */
	public void setEDD(String EDD) {
		this.EDD = EDD;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getReceive_sms()
	 */
	@Override
	public String getReceive_sms() {
		return receive_sms;
	}
	/**
	 * @param receive_sms the receive_sms to set
	 */
	public void setreceive_sms(String receive_sms) {
		this.receive_sms = receive_sms;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getFollow_up()
	 */
	@Override
	public String getFollow_up() {
		return follow_up;
	}
	/**
	 * @param follow_up the follow_up to set
	 */
	public void setfollow_up(String follow_up) {
		this.follow_up = follow_up;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getCUG_status()
	 */
	@Override
	public String getCUG_status() {
		return CUG_status;
	}
	/**
	 * @param CUG_status the CUG_status to set
	 */
	public void setCUG_status(String CUG_status) {
		this.CUG_status = CUG_status;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getComment()
	 */
	@Override
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setcomment(String comment) {
		this.comment = comment;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getBleeding()
	 */
	@Override
	public String getBleeding() {
		return bleeding;
	}
	/**
	 * @param bleeding the bleeding to set
	 */
	public void setBleeding(String bleeding) {
		this.bleeding = bleeding;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getFever()
	 */
	@Override
	public String getFever() {
		return fever;
	}
	/**
	 * @param fever the fever to set
	 */
	public void setFever(String fever) {
		this.fever = fever;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getBleeding()
	 */
	@Override
	public String getSwollen_feet() {
		return swollen_feet;
	}
	/**
	 * @param swollen_feet the swollen_feet to set
	 */
	public void setSwollen_feet(String swollen_feet) {
		this.swollen_feet = swollen_feet;
	}
	/* (non-Javadoc)
	 * @see org.sana.core.IPatient#getBlurred_vision()
	 */
	@Override
	public String getBlurred_vision() {
		return blurred_vision;
	}
	/**
	 * @param blurred_vision the blurred_vision to set
	 */
	public void setBlurred_vision(String blurred_vision) {
		this.blurred_vision = blurred_vision;
	}


	public boolean getConfirmed(){
		return confirmed;
	}
	public void setConfirmed(boolean confirmed){
		this.confirmed = confirmed;
	}

    public boolean isDobEstimated() {
        return dobEstimated;
    }

    public void setDobEstimated(boolean dobEstimated) {
        this.dobEstimated = dobEstimated;
    }
}
