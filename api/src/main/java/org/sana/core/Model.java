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

import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import org.sana.api.IModel;
import org.sana.util.DateUtil;

/**
 * The basic implementation of the core behavior of the core objects in the 
 * data model.
 * 
 * @author Sana Development
 *
 */
public abstract class Model implements IModel{
	
	public String uuid;
	
	public Date created;
	
	public Date modified;
	
	public Model(){}
	
	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IModel#getCreated()
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the {@link java.util.Date Date} when this object was created.
	 */
	public void setCreated(Date date) {
		this.created = date;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IModel#getModified()
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * Sets the {@link java.util.Date Date} when this object was last modified.
	 */
	public void setModified(Date modified) {
		this.modified = modified;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.sana.api.IModel#getUuid()
	 */
	public String getUuid() {
		return uuid;
	}
	
	/** 
	 * Sets the instance's universally unique identifier
	 * 
	 * @param uuid the new UUID
	 * @throws IllegalArgumentException if the format of the argument does not 
	 * 	conform to {@link #UUID_REGEX}
	 */
	public void setUuid(String uuid) {
		this.uuid = java.util.UUID.fromString(uuid).toString();
	}
	
    @Override
    public String toString(){
        return String.format("<%s %s>",this.getClass().getSimpleName(),uuid);
    }

    /**
     * Returns the {@link java.lang.String String} representation of a field.
     *
     * @param fieldName
     * @return
     */
    public String getField(String fieldName) {
        String fieldStr = null;
        Object fieldVal = null;
        try {
            Field field = getClass().getField(fieldName);
            Class<?> klazz = field.getType();
            fieldVal = field.get(this);
            if (klazz.isAssignableFrom(Collection.class)) {
                TypeVariable<? extends Class<?>>[] params = klazz.getTypeParameters();
                int size = params.length;
                for (TypeVariable<? extends Class<?>> tv : params) {

                    ;
                }
                fieldStr = String.valueOf(field.get(this));
            } else if (klazz.isAssignableFrom(Date.class)) {
                fieldStr = (fieldVal != null) ?
                        DateUtil.format((Date) fieldVal) : "";
            } else {
                fieldStr = String.valueOf(field.get(this));
            }
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldStr;
    }

    /**
     * Sets the value of a field from a {@link java.lang.String String}
     * representation
     *
     * @param fieldName
     * @param value
     * @return
     */
    public boolean setField(String fieldName, String value) {
        boolean fieldSet = false;
        try {
            Field field = getClass().getField(fieldName);
            Class<?> klazz = field.getType();
            if (klazz.isAssignableFrom(String.class)) {
                field.set(this, String.valueOf(value));
            } else if (klazz.isAssignableFrom(CharSequence.class)) {
                field.set(this, String.valueOf(value));
            } else if (klazz.isAssignableFrom(Boolean.TYPE)) {
                field.setBoolean(this, Boolean.parseBoolean(value));
            } else if (klazz.isAssignableFrom(Integer.TYPE)) {
                field.setInt(this, Integer.parseInt(value));
            } else if (klazz.isAssignableFrom(Double.TYPE)) {
                field.setDouble(this, Double.parseDouble(value));
            } else if (klazz.isAssignableFrom(Character.TYPE)) {
                char ch = (value == null) ? ' ' : value.charAt(0);
                field.setChar(this, ch);
            } else if (klazz.isAssignableFrom(Byte.TYPE)) {
                field.setShort(this, Byte.parseByte(value));
            } else if (klazz.isAssignableFrom(Short.TYPE)) {
                field.setShort(this, Short.parseShort(value));
            } else if (klazz.isAssignableFrom(Long.TYPE)) {
                field.setLong(this, Long.parseLong(value));
            } else if (klazz.isAssignableFrom(Float.TYPE)) {
                field.setFloat(this, Float.parseFloat(value));
            } else if (klazz.isAssignableFrom(Date.class)) {
                field.set(this, DateUtil.parseDate(value));
            } else if (klazz.isAssignableFrom(URI.class)) {
                field.set(this, URI.create(value));
            } else if (klazz.isArray()) {
                field.getGenericType()
                ;
            } else if (klazz.isAssignableFrom(Collection.class)) {
                TypeVariable<? extends Class<?>>[] p = klazz.getTypeParameters();
                Class<?> parameterizedKlazz = klazz.getTypeParameters().getClass();

            } else {
                TypeVariable<? extends Class<?>>[] p = klazz.getTypeParameters();
                throw new IllegalArgumentException("Type not allowed: "
                        + klazz.getSimpleName() + ", " + klazz.getGenericSuperclass().toString());
            }
            fieldSet = true;
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fieldSet;
    }
}
