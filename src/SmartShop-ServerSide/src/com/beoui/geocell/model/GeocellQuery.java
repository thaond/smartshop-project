package com.beoui.geocell.model;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * GeocellQuery splits the traditional query in 3 parts: the base query string,
 * the declared parameters and the list of object parameters.
 * 
 * Additional information on
 * http://code.google.com/appengine/docs/java/datastore/queriesandindexes.html
 * 
 * This allows us to create new queries and adding conditions/filters like in
 * the proximity fetch.
 * 
 * @author Alexandre Gellibert
 * 
 */
public class GeocellQuery<T> {

	/**
	 * Base query string without the declared parameters and without the entity
	 * name. Ex: "lastName == lastNameParam"
	 * 
	 * CAREFUL: must not contain "order" clauses!
	 */
	private String baseQuery;

	/**
	 * (Optional) Declared parameters. Ex: "String lastNameParam"
	 */
	private String declaredParameters;
	private String decleredImports;

	/**
	 * (Optional) List of parameters. Ex: Arrays.asList("Smith")
	 */
	private List<T> parameters;

	private long fromRecord;
	private long toRecord;

	public GeocellQuery(String baseQuery, String declaredParameters,
			List<T> parameters) {
//		Validate.notEmpty(baseQuery);
		this.baseQuery = baseQuery;
		this.declaredParameters = declaredParameters;
		this.parameters = parameters;
	}

	public String getBaseQuery() {
		return baseQuery;
	}

	public String getDeclaredParameters() {
		return declaredParameters;
	}

	public List<T> getParameters() {
		return parameters;
	}

	/**
	 * @param fromRecord
	 *            the fromRecord to set
	 */
	public void setFromRecord(int fromRecord) {
		this.fromRecord = fromRecord;
	}

	/**
	 * @return the fromRecord
	 */
	public long getFromRecord() {
		return fromRecord;
	}

	/**
	 * @param toRecord
	 *            the toRecord to set
	 */
	public void setToRecord(int toRecord) {
		this.toRecord = toRecord;
	}

	/**
	 * @return the toRecord
	 */
	public long getToRecord() {
		return toRecord;
	}
	
	public void setRange(long fromRecord, long toRecord){
		this.fromRecord = fromRecord;
		this.toRecord = toRecord;
	}

	/**
	 * @param decleredImports the decleredImports to set
	 */
	public void setDeclearedImports(String decleredImports) {
		this.decleredImports = decleredImports;
	}

	/**
	 * @return the decleredImports
	 */
	public String getDecleredImports() {
		return decleredImports;
	}

}
