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
package org.sana.core;

import com.google.gson.annotations.Expose;

import org.sana.api.IObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An entity that collects data.
 * 
 * @author Sana Development
 *
 */
public class Observer extends Model implements IObserver{

	/**
	 * Simple user representation
	 */
    public static class User{
        public String first_name = null;
        public String last_name = null;
		public String username = null;
		public String password = null;
		public boolean is_admin = false;
		public String[] groups = new String[0];
    }

	@Expose
	public User user = new User();

	public String username = null;

	public String password = null;

	@Expose
	public String role;
	private String phone_number;
	@Expose
	Set<Location> locations = new HashSet<>();
	@Expose
    public Location subcounty = null;
	/** Default Constructor */
	public Observer() {
        user = new User();
    }

    public Observer(Observer obj){
		setUsername(obj.getUsername());
		setPassword(obj.getPassword());
		setRole(obj.getRole());
		setLocations(obj.getLocations());
    }
	
	/**
	 * Creates a new instance with a specified unique id.
	 * 
	 * @param uuid The UUID of the instance
	 */
	public Observer(String uuid){
		super();
		setUuid(uuid);
        user = new User();
	}

	public User getUser() {
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IObserver#getUsername()
	 */
	public String getUsername() {
		return user.username;
	}

	/**
	 * Sets the username for an instance of this class. 
	 *
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		user.username = username;
	}

	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IObserver#getPassword()
	 */
	public String getPassword() {
		return user.password;
	}

	/**
	 * Sets the password for an instance of this class. 
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		user.password = password;
	}

	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IObserver#getRole()
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role for an instance of this class. 
	 *
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	/**
	 * Sets the role for an instance of this class.
	 *
	 * @param phone_number the role to set
	 */
	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

    public String getFirstName(){
        return user.first_name;
    }

    public void setFirstName(String name){
        user.first_name = name;
    }

    public String getLastName(){
        return user.last_name;
    }

    public void setLastName(String name){
        user.last_name = name;
    }

    public List<Location> getLocations(){
		return new ArrayList<>(locations);
    }

    public void setLocations(Collection<Location> locations){
		this.locations.clear();
        this.locations.addAll(locations);
    }
	
	public void setLocations(String[] locations) {
        this.locations.clear();
		for (String location : locations) {
			Location l = new Location();
			l.setUuid(location);
			this.locations.add(l);
		}
	}

	public boolean isAdmin() {
		return user.is_admin;
	}

	public boolean setIsAdmin(boolean isAdmin) {
		return user.is_admin = isAdmin;
	}

    @Override
    public Location getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(Location subcounty) {
        this.subcounty = subcounty;
    }
}
