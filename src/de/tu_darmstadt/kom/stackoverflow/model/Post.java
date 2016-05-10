package de.tu_darmstadt.kom.stackoverflow.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/*Format in dump file
Id
PostTypeId (listed in the PostTypes table)
	1 - Question
	2 - Answer
	3 - Orphaned tag wiki
	4 - Tag wiki excerpt
	5 - Tag wiki
	6 -Moderator nomination
	7 - "Wiki placeholder" (seems to only be the election description)
	8 - Privilege wiki
AcceptedAnswerId (only present if PostTypeId is 1)
ParentID (only present if PostTypeId is 2)
CreationDate
Score
ViewCount
Body
OwnerUserId (present only if user has not been deleted; always -1 for tag wiki entries (i.e., the community user owns them))
OwnerDisplayName
LastEditorUserId
LastEditorDisplayName="Rich B"
LastEditDate="2009-03-05T22:28:34.823" - the date and time of the most recent edit to the post
LastActivityDate="2009-03-11T12:51:01.480" - the date and time of the most recent activity on the post. For a question, this could be the post being edited, a new answer was posted, a bounty was started, etc.
Title
Tags
AnswerCount
CommentCount
FavoriteCount
ClosedDate (present only if the post is closed)
CommunityOwnedDate (present only if post is community wikied)

Sample XML
<row Id="4" 
PostTypeId="1" 
AcceptedAnswerId="7" 
CreationDate="2008-07-31T21:42:52.667" 
Score="305" 
ViewCount="20324"
Body="&lt;p&gt;I want to use a track-bar to change a form's opacity.&lt;/p&gt;&#xA;&#xA;&lt;p&gt;This is my code:&lt;/p&gt;&#xA;&#xA;&lt;pre&gt;&lt;code&gt;decimal trans = trackBar1.Value / 5000;&#xA;this.Opacity = trans;&#xA;&lt;/code&gt
;&lt;/pre&gt;&#xA;&#xA;&lt;p&gt;When I try to build it, I get this error:&lt;/p&gt;&#xA;&#xA;&lt;blockquote&gt;&#xA;  &lt;p&gt;Cannot implicitly convert type 'decimal' to 'double'.&lt;/p&gt;&#xA;&lt;/blockquote&gt;&#xA;&#xA;&lt;p&gt;I tri
ed making &lt;code&gt;trans&lt;/code&gt; a &lt;code&gt;double&lt;/code&gt;, but then the control doesn't work. This code has worked fine for me in VB.NET in the past. &lt;/p&gt;&#xA;" 
OwnerUserId="8" 
LastEditorUserId="451518" 
LastEditorDisplayName="Rich B" 
LastEditDate="2014-07-28T10:02:50.557" 
LastActivityDate="2014-07-28T10:02:50.557" 
Title="When setting a form's opacity should I use a decimal or double?" 
Tags="&lt;c#&gt;&lt;winforms&gt;&lt;type-conversion&gt;&lt;opacity&gt;" 
AnswerCount="13" 
CommentCount="1" 
FavoriteCount="28" 
CommunityOwnedDate="2012-10-31T16:42:47.213" />
*/


/**
 * Post Class which contains all the data of a post
 * Detailed Format : http://meta.stackexchange.com/questions/2677/database-schema-documentation-for-the-public-data-dump-and-sede
 * @author Asit
 *
 */
/**
 * @author Asit
 *
 */
public class Post{
	
	private long m_id;
	private int m_postTypeId;
	private long m_acceptedAnswerId;
	private long m_parentId;
	private Date m_creationDate;
	private long m_score;
	private long m_viewCount;
	private String m_body;
	private long m_ownerUserId;
	private long m_lastEditorUserId;
	private String m_lastEditorDisplayName;
	private Date m_lastEditDate;
	private Date m_lastActivityDate;
	private String m_title;
	private ArrayList<String> m_tags = new ArrayList<String>();
	private int m_answerCount;
	private int m_commentCount;
	private long m_favoriteCount;
	private int m_status;
	private Date m_closedDate;
	private Date m_communityOwnedDate;
	private String m_userLabel;
	private String m_label;
	
	/**
	 * Sets the id of post(question or answer)
	 * 
	 * @param id Id of the question or answer
	 * 
	 */
	public void setId(long id) { 
		m_id = id; 
	}
	
	/**
	 * Sets the post type(integer) of the post
	 * 		1 - Question
	 * 		2 - Answer
	 * 		3 - Orphaned tag wiki
	 *		4 - Tag wiki excerpt
	 *		5 - Tag wiki
	 * 		6 -Moderator nomination
	 *		7 - "Wiki placeholder" (seems to only be the election description)
	 *		8 - Privilege wiki
	 * 
	 * @param postTypeId post type(integer) of the question or answer
	 * 
	 */
	public void setPostTypeId(int postTypeId) { 
		m_postTypeId = postTypeId; 
	}
	
	
	/**
	 * Sets the accepted answer id for questions(for post_type_id 1)
	 * @param acceptedAnswerId id of accepted answer 
	 * 
	 */
	public void setAcceptedAnswerId(long acceptedAnswerId) { 
		m_acceptedAnswerId = acceptedAnswerId; 
	}
	
	/**
	 * Sets the Parent Id of qnswers(for post_type_id 2)
	 * @param parentId id of the question of the answer
	 * 
	 */	
	public void setParentID(long parentId) { 
		m_parentId = parentId; 
	}
	
	/**
	 * Sets the creation date
	 * @param creationDate creation date of the post
	 * 
	 */
	public void setCreationDate(Date creationDate) { 
		m_creationDate = creationDate; 
	}
	
	/**
	 * Sets the score of the post
	 * @param  score score of the post
	 * 
	 */
	public void setScore(long score) { 
		m_score = score; 
	}
	
	
	/**
	 * Sets the view count
	 * @param viewCount view count of the post
	 * 
	 */
	public void setViewCount(long viewCount) { 
		m_viewCount = viewCount; 
	}
	
	
	/**
	 * Sets the text of the post
	 * @param body text of the post
	 * 
	 */
	public void setBody(String body) { 
		m_body = body; 
	}
	
	
	/** 
	 * Sets the owner user id of the post
	 * @param ownerUserId owner user id of the post
	 * 
	 */
	public void setOwnerUserId(long ownerUserId) { 
		m_ownerUserId = ownerUserId; 
	}
	
	
	/**
	 * Sets user id of last editor user
	 * @param lastEditorUserId User Id of last editor user
	 * 
	 */
	public void setLastEditorUserId(long lastEditorUserId) { 
		m_lastEditorUserId = lastEditorUserId; 
	}
	
	/**
	 * Sets display name of  last editor
	 * @param lastEditorDisplayName display name of last editor
	 * 
	 */
	public void setLastEditorDisplayName(String lastEditorDisplayName) { 
		m_lastEditorDisplayName = lastEditorDisplayName; 
	}
	
	/**
	 * Set Last edited Date
	 * @param lastEditDate Lasted Edited Date
	 */
	public void setLastEditDate(Date lastEditDate) { 
		m_lastEditDate = lastEditDate; 
	}
	
	
	/**
	 * Sets the date of the last activity
	 * @param lastActivityDate Last activity date
	 */
	public void setLastActivityDate(Date lastActivityDate) { 
		m_lastActivityDate = lastActivityDate; 
	}
	
	/**
	 * Sets the title of the Post
	 * @param title Title of the Post
	 */
	public void setTitle(String title) { 
		m_title = title; 
	}
	
	/**
	 * Sets the collection of tags(of questions)
	 * @param tags ArrayList of Tags
	 */
	public void setTags(ArrayList<String> tags) { 
		m_tags = tags;
	}
	
	/**
	 * Sets the answer of count
	 * @param answerCount No. of answers
	 */
	public void setAnswerCount(int answerCount) { 
		m_answerCount = answerCount; 
	}
	
	/**
	 * Sets the comment count
	 * @param commentCount No. of comments
	 */
	public void setCommentCount(int commentCount) { 
		m_commentCount = commentCount; 
	}
	
	/**
	 * Sets Favorite Count
	 * @param favoriteCount No. of Favorite on the post
	 */
	public void setFavoriteCount(long favoriteCount) { 
		m_favoriteCount = favoriteCount; 
	}
	
	/**
	 * Sets status(not used)
	 * @param status status of the question
	 */
	public void setStatus(int status) { 
		m_status = status; 
	}
	
	/**
	 * Sets the closed date of the question
	 * @param closedDate Closed Date of the question
	 */
	public void setClosedDate(Date closedDate) { 
		m_closedDate = closedDate; 
	}
	
	/**
	 * Sets the Community owned date
	 * @param communityOwnedDate Community owned Date
	 */
	public void setCommunityOwnedDate(Date communityOwnedDate) { 
		m_communityOwnedDate = communityOwnedDate; 
	}
	
	/**
	 * Sets the User label
	 * @param label User Label(easy, difficult, empty string)
	 */
	public void setUserLabel(String label) {
		m_userLabel = label;
	}
	
	/**
	 * Sets the label based on meta information
	 * @param label Label based on meta information
	 */
	public void setLabel(String label) {
		m_label = label;
	}
	
	/**
	 * @return Returns Id of the Post
	 */
	public long GetId() { 
		return m_id; 
	}
	
	/**
	 * @return Returns Post Type(1-question, 2-answer)
	 */
	public int GetPostTypeId() { 
		return m_postTypeId; 
	}
	
	/**
	 * @return Returns Id of the accepted answer of the post(question)
	 */
	public long GetAcceptedAnswerId() { 
		return m_acceptedAnswerId; 
	}
	
	/**
	 * @return Returns Parent Id of the post(answer)
	 */
	public long GetParentId() { 
		return m_parentId; 
	}
	
	/**
	 * @return Returns creation date of the post
	 */
	public Date GetCreationDate() { 
		return m_creationDate; 
	}
	
	/**
	 * @return Returns score of the post
	 */
	public long GetScore() { 
		return m_score; 
	}
	
	/**
	 * @return Returns view count of the Post
	 */
	public long GetViewCount() { 
		return m_viewCount; 
	}
	
	/**
	 * @return Returns text of the post
	 */
	public String GetBody() { 
		return m_body; 
	}
	
	/**
	 * @return Returns user id of the owner of the post
	 */
	public long GetOwnerUserId() { 
		return m_ownerUserId; 
	}
	
	/**
	 * @return Returns id of the user who last edited the post
	 */
	public long GetLastEditorUserId() { 
		return m_lastEditorUserId; 
	}
	
	
	/**
	 * @return Returns display name of the user who last edited the post
	 */
	public String GetLastEditorDisplayName() { 
		return m_lastEditorDisplayName; 
	}
	
	/**
	 * @return Returns Date of last edit done on the Post
	 */
	public Date GetLastEditDate() { 
		return m_lastEditDate; 
	}
	
	/**
	 * @return Returns date of last activity done on the post 
	 */
	public Date GetLastActivityDate() { 
		return m_lastActivityDate; 
	}
	
	/**
	 * @return Returns the title of the Post
	 */
	public String GetTitle() { 
		return m_title; 
	}
	
	/**
	 * @return Returns a collection of Tags(question only)
	 */
	public ArrayList<String> GetTags() { 
		return m_tags;
	}
	
	/**
	 * @return Returns No. of Answers
	 */
	public int GetAnswerCount() { 
		return m_answerCount; 
	} 
	
	/**
	 * @return Returns No. of Comments
	 */
	public int GetCommentCount() { 
		return m_commentCount; 
	}
	
	/**
	 * @return Returns Favorite Count of a post
	 */
	public long GetFavoriteCount() { 
		return m_favoriteCount; 
	}
	
	/**
	 * @return Status of the post(not used at all)
	 */
	public int GetStatus() { 
		return m_status; 
	}
	
	/**
	 * @return Closed Date of the question
	 */
	public Date GetClosedDate() { 
		return m_closedDate; 
	}
	
	/**
	 * @return Returns Community Owned Date of the question
	 */
	public Date GetCommunityOwnedDate() { 
		return m_communityOwnedDate; 
	}
	
	/**
	 * @return User Label of the post(done manually)
	 */
	public String GetUserLabel() {
		return m_userLabel;
	}
	
	/**
	 * @return Label of the post(label based on meta information)
	 */
	public String GetLabel() {
		return m_label;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//http://www.javapractices.com/topic/TopicAction.do;jsessionid=00034CD28E73C839D6070D2DD49E9639?Id=55
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append( this.getClass().getName() );
		result.append( " Object {" );
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for ( Field field : fields  ) {
			result.append("  ");
		    try {
		    	result.append( field.getName() );
		    	result.append(": ");
		    	//requires access to private field:
		    	result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		    	System.out.println(ex);
		    }
		    result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

}
