package de.tu_darmstadt.kom.stackoverflow.model;

import java.util.Date;

public class User {

	private int m_id;
	private int m_reputation;
	private Date m_creationDate;
	private String m_displayName;
	private Date m_lastAccessDate;
	private String m_websiteUrl;
	private String m_location;
	private String m_aboutMe;
	private int m_views;
	private int m_upVotes;
	private int m_downVotes;
	private int m_age;
	private String m_profileImageUrl;
	private int m_accountId;
	/**
	 * @return the m_rowId
	 */
	public int getId() {
		return m_id;
	}
	/**
	 * @return the m_reputation
	 */
	public int getReputation() {
		return m_reputation;
	}
	/**
	 * @return the m_creationDate
	 */
	public Date getCreationDate() {
		return m_creationDate;
	}
	/**
	 * @return the m_displayName
	 */
	public String getDisplayName() {
		return m_displayName;
	}
	/**
	 * @return the m_lastAccessDate
	 */
	public Date getLastAccessDate() {
		return m_lastAccessDate;
	}
	/**
	 * @return the m_websiteUrl
	 */
	public String getWebsiteUrl() {
		return m_websiteUrl;
	}
	/**
	 * @return the m_location
	 */
	public String getLocation() {
		return m_location;
	}
	/**
	 * @return the m_views
	 */
	public int getViews() {
		return m_views;
	}
	/**
	 * @return the m_upVotes
	 */
	public int getUpVotes() {
		return m_upVotes;
	}
	/**
	 * @return the m_downVotes
	 */
	public int getDownVotes() {
		return m_downVotes;
	}
	/**
	 * @return the m_accountId
	 */
	public int getAccountId() {
		return m_accountId;
	}
	/**
	 * @param m_rowId the m_rowId to set
	 */
	public void setId(int m_rowId) {
		this.m_id = m_rowId;
	}
	/**
	 * @param m_reputation the m_reputation to set
	 */
	public void setReputation(int m_reputation) {
		this.m_reputation = m_reputation;
	}
	/**
	 * @param m_creationDate the m_creationDate to set
	 */
	public void setCreationDate(Date m_creationDate) {
		this.m_creationDate = m_creationDate;
	}
	/**
	 * @param m_displayName the m_displayName to set
	 */
	public void setDisplayName(String m_displayName) {
		this.m_displayName = m_displayName;
	}
	/**
	 * @param m_lastAccessDate the m_lastAccessDate to set
	 */
	public void setLastAccessDate(Date m_lastAccessDate) {
		this.m_lastAccessDate = m_lastAccessDate;
	}
	/**
	 * @param m_websiteUrl the m_websiteUrl to set
	 */
	public void setWebsiteUrl(String m_websiteUrl) {
		this.m_websiteUrl = m_websiteUrl;
	}
	/**
	 * @param m_location the m_location to set
	 */
	public void setLocation(String m_location) {
		this.m_location = m_location;
	}
	/**
	 * @param m_views the m_views to set
	 */
	public void setViews(int m_views) {
		this.m_views = m_views;
	}
	/**
	 * @param m_upVotes the m_upVotes to set
	 */
	public void setUpVotes(int m_upVotes) {
		this.m_upVotes = m_upVotes;
	}
	/**
	 * @param m_downVotes the m_downVotes to set
	 */
	public void setDownVotes(int m_downVotes) {
		this.m_downVotes = m_downVotes;
	}
	/**
	 * @param m_accountId the m_accountId to set
	 */
	public void setAccountId(int m_accountId) {
		this.m_accountId = m_accountId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [m_rowId=" + m_id + ", m_reputation=" + m_reputation
				+ ", m_creationDate=" + m_creationDate + ", m_displayName="
				+ m_displayName + ", m_lastAccessDate=" + m_lastAccessDate
				+ ", m_websiteUrl=" + m_websiteUrl + ", m_location="
				+ m_location + ", m_views=" + m_views + ", m_upVotes="
				+ m_upVotes + ", m_downVotes=" + m_downVotes + ", m_accountId="
				+ m_accountId + "]";
	}
	/**
	 * @return the m_aboutMe
	 */
	public String getAboutMe() {
		return m_aboutMe;
	}
	/**
	 * @return the m_age
	 */
	public int getAge() {
		return m_age;
	}
	/**
	 * @return the m_profileImageUrl
	 */
	public String getProfileImageUrl() {
		return m_profileImageUrl;
	}
	/**
	 * @param m_aboutMe the m_aboutMe to set
	 */
	public void setAboutMe(String m_aboutMe) {
		this.m_aboutMe = m_aboutMe;
	}
	/**
	 * @param m_age the m_age to set
	 */
	public void setAge(int m_age) {
		this.m_age = m_age;
	}
	/**
	 * @param m_profileImageUrl the m_profileImageUrl to set
	 */
	public void setProfileImageUrl(String m_profileImageUrl) {
		this.m_profileImageUrl = m_profileImageUrl;
	}
	
	
}
