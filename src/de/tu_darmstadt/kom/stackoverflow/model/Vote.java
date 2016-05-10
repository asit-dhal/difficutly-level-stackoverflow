package de.tu_darmstadt.kom.stackoverflow.model;

import java.util.Date;

public class Vote {
	private long m_id;
	private long m_postId;
	private int m_voteTypeId;
	private long m_userId;
	private Date m_creationDate;
	private int m_bountyAmount;
	/**
	 * @return the m_id
	 */
	public long getId() {
		return m_id;
	}
	/**
	 * @return the m_postId
	 */
	public long getPostId() {
		return m_postId;
	}
	/**
	 * @return the m_voteTypeId
	 */
	public int getVoteTypeId() {
		return m_voteTypeId;
	}
	/**
	 * @return the m_userId
	 */
	public long getUserId() {
		return m_userId;
	}
	/**
	 * @return the m_creationDate
	 */
	public Date getCreationDate() {
		return m_creationDate;
	}
	/**
	 * @return the m_bountyAmount
	 */
	public int getBountyAmount() {
		return m_bountyAmount;
	}
	/**
	 * @param m_id the m_id to set
	 */
	public void setId(long m_id) {
		this.m_id = m_id;
	}
	/**
	 * @param m_postId the m_postId to set
	 */
	public void setPostId(long m_postId) {
		this.m_postId = m_postId;
	}
	/**
	 * @param m_voteTypeId the m_voteTypeId to set
	 */
	public void setVoteTypeId(int m_voteTypeId) {
		this.m_voteTypeId = m_voteTypeId;
	}
	/**
	 * @param m_userId the m_userId to set
	 */
	public void setUserId(long m_userId) {
		this.m_userId = m_userId;
	}
	/**
	 * @param m_creationDate the m_creationDate to set
	 */
	public void setCreationDate(Date m_creationDate) {
		this.m_creationDate = m_creationDate;
	}
	/**
	 * @param m_bountyAmount the m_bountyAmount to set
	 */
	public void setBountyAmount(int m_bountyAmount) {
		this.m_bountyAmount = m_bountyAmount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vote [m_id=" + m_id + ", m_postId=" + m_postId
				+ ", m_voteTypeId=" + m_voteTypeId + ", m_userId=" + m_userId
				+ ", m_creationDate=" + m_creationDate + ", m_bountyAmount="
				+ m_bountyAmount + "]";
	}

	
}
