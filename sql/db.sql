-- create table posts 
-- engine : MyISAM -- to make insertion faster
DROP TABLE IF EXISTS `posts`$$

CREATE TABLE if not exists  `posts` (
  `stack_id` bigint(20) DEFAULT NULL,
  `post_type_id` int(11) DEFAULT NULL,
  `accepted_answer_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `score` bigint(20) DEFAULT NULL,
  `view_count` bigint(20) DEFAULT NULL,
  `body` longtext,
  `owner_user_id` bigint(20) DEFAULT NULL,
  `last_editor_user_id` bigint(20) DEFAULT NULL,
  `last_editor_display_name` varchar(200) DEFAULT NULL,
  `last_edit_date` datetime DEFAULT NULL,
  `last_activity_date` datetime DEFAULT NULL,
  `title` text,
  `tags` text,
  `answer_count` int(11) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `favorite_count` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `closed_date` datetime DEFAULT NULL,
  `community_owned_date` datetime DEFAULT NULL
 ) ENGINE=MyISAM DEFAULT CHARSET=utf8 $$
 

DROP TABLE IF EXISTS `users`$$
CREATE TABLE if not exists `users` (
  `user_id` bigint(20) DEFAULT NULL,
  `reputation` bigint(20) DEFAULT NULL,
  --`creation_date` datetime DEFAULT NULL,
  --`display_name` text DEFAULT NULL,
  --`last_access_date` datetime DEFAULT NULL,
  --`website_url` text DEFAULT NULL,
  --`location` text DEFAULT NULL,
  --`about_me` text DEFAULT NULL,
  `views` bigint(20) DEFAULT NULL,
  `up_votes` bigint(20) DEFAULT NULL,
  `down_votes` bigint(20) DEFAULT NULL,
  `account_id` text
  ) ENGINE=MyISAM DEFAULT CHARSET=utf8 $$
  
DROP TABLE IF EXISTS `votes` $$
CREATE TABLE if not exists `votes` (
  `vote_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `vote_type_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `bounty_amount` bigint(20) DEFAULT NULL
  ) ENGINE=MyISAM DEFAULT CHARSET=utf8 $$

   
DROP PROCEDURE IF EXISTS `create_tables`$$

CREATE PROCEDURE `create_tables` (IN tag_name VARCHAR(100))
BEGIN
		SET @`tag_name` := tag_name;
		SET @`table_name` := CONCAT("posts_", @`tag_name`);
	
		SET @`query` := CONCAT("DROP TABLE IF EXISTS ", @`table_name`);
		CALL execute_dynamic_sql(@`query`);
		
		
		
		SET @`query` := CONCAT("CREATE TABLE ", @`table_name`, "(  ",
	  		"`stack_id` bigint(20) DEFAULT NULL, ",
	  		"`post_type_id` int(11) DEFAULT NULL, ",
	  		"`accepted_answer_id` bigint(20) DEFAULT NULL, ",
	  		"`parent_id` bigint(20) DEFAULT NULL, ",
	  		"`creation_date` datetime DEFAULT NULL, ",
	  		"`score` bigint(20) DEFAULT NULL, ",
	  		"`view_count` bigint(20) DEFAULT NULL, ",
	  		"`body` longtext, ",
	  		"`owner_user_id` bigint(20) DEFAULT NULL, ",
	  		"`last_editor_user_id` bigint(20) DEFAULT NULL, ",
	  		"`last_editor_display_name` varchar(200) DEFAULT NULL, ",
	  		"`last_edit_date` datetime DEFAULT NULL, ",
	  		"`last_activity_date` datetime DEFAULT NULL, ",
	  		"`title` text, ",
	  		"`tags` text, ",
	  		"`answer_count` int(11) DEFAULT NULL, ",
	  		"`comment_count` int(11) DEFAULT NULL, ",
	  		"`favorite_count` bigint(20) DEFAULT NULL, ",
	  		"`status` int(11) DEFAULT NULL, ",
	  		"`closed_date` datetime DEFAULT NULL, ",
	  		"`community_owned_date` datetime DEFAULT NULL, ",
	  		"`label` varchar(20) DEFAULT NULL, ",
	  		"`user_label` varchar(20) DEFAULT NULL, ",
	  		"`code` longtext  DEFAULT NULL, ",
	  		"`loc` double DEFAULT NULL ",
	 		") ENGINE=MyISAM DEFAULT CHARSET=utf8; ");
	 	CALL execute_dynamic_sql(@`query`);
END$$

DROP PROCEDURE IF EXISTS `create_tables_csv`$$

CREATE PROCEDURE `create_tables_csv` (IN tag_csv VARCHAR(100))
BEGIN
	DECLARE a INT Default 0 ;
    DECLARE str VARCHAR(255);
    simple_loop: LOOP
    	SET a=a+1;
        SET str=SPLIT_STR(tag_csv,",",a);
         IF str='' THEN
            LEAVE simple_loop;
         END IF;
         CALL create_tables(str);
   END LOOP simple_loop;
END$$ 


DROP PROCEDURE IF EXISTS `post_processing` $$

CREATE PROCEDURE `post_processing` (IN tag_name VARCHAR(100))
BEGIN
	SET @`tag_name` := `tag_name`;
	SET @`table_name` := CONCAT("posts_", @`tag_name`);
	SET @`query` := CONCAT("Insert into ", @`table_name`, " select *, null, null, null, null from posts where tags like '%", @`tag_name`, "%'");
	CALL execute_dynamic_sql(@`query`);
	
	SET @`query` := CONCAT("Insert into ", @`table_name`, 
				" select *, null, null, null, null from posts",
				" where post_type_id = 2",
				" and stack_id in (", 
				" select accepted_answer_id from posts where tags like '%", @`tag_name`, "%' and post_type_id=1)");
	CALL execute_dynamic_sql(@`query`);
	  
	SET @`query` := CONCAT("create  index idx_post_type_id_", @`tag_name`, " using hash on ", @`table_name`, "(post_type_id)");
	CALL execute_dynamic_sql(@`query`);

	SET @`query` := CONCAT("create index idx_accepted_answer_id_", @`tag_name`, " using btree on ", @`table_name`, "(accepted_answer_id)");
	CALL execute_dynamic_sql(@`query`);
	
	SET @`query` := CONCAT("create index idx_score_", @`tag_name`, "  using btree on ", @`table_name`, "(score)");
	CALL execute_dynamic_sql(@`query`);

	SET @`query` := CONCAT("create index idx_creation_date_", @`tag_name`, " using btree on ", @`table_name`, "(creation_date)");
	CALL execute_dynamic_sql(@`query`);
	
	SET @`query` := CONCAT("create  index idx_label_", @`tag_name`, " using hash on ", @`table_name`, "(label)");
	CALL execute_dynamic_sql(@`query`);

	SET @`query` := CONCAT("create  index idx_user_label_", @`tag_name`, " using hash on ", @`table_name`, "(user_label)");
	CALL execute_dynamic_sql(@`query`);

	SET @`query` := CONCAT("alter table ", @`table_name`, " add primary key(stack_id)");
	CALL execute_dynamic_sql(@`query`);
	
	SET @`query` := CONCAT("update ", @`table_name`,
				" set label = 'easy' ",
				" where stack_id in ( ", 
				" select stack_id from ( ", 
				" select a.stack_id from ( ",
				" select stack_id, accepted_answer_id, creation_date ",  
				" from ", @`table_name`, 
				" where accepted_answer_id <> 0 and post_type_id = 1 ) as a ",  
				" inner join( ", 
				" select * from ", @`table_name`,  
				" where post_type_id = 2) as b ", 
				" on a.accepted_answer_id = b.stack_id ",  
				"where (b.creation_date - a.creation_date) <= 259200) as c)");
	CALL execute_dynamic_sql(@`query`);

	SET @`query` := CONCAT("update ", @`table_name`,
					" set label = 'difficult' ", 
					" where post_type_id = 1", 
					" and score >= 1",
					" and accepted_answer_id = 0");
	CALL execute_dynamic_sql(@`query`);
END $$

DROP PROCEDURE IF EXISTS `post_processing_csv`$$

CREATE PROCEDURE `post_processing_csv` (IN tag_csv VARCHAR(100))
BEGIN
	DECLARE a INT Default 0 ;
    DECLARE str VARCHAR(255);
    simple_loop: LOOP
    	SET a=a+1;
        SET str=SPLIT_STR(tag_csv,",",a);
         IF str='' THEN
            LEAVE simple_loop;
         END IF;
         CALL post_processing(str);
   END LOOP simple_loop;
END$$ 


DROP PROCEDURE IF EXISTS `execute_dynamic_sql`$$

CREATE PROCEDURE `execute_dynamic_sql` (IN `sql_query` LONGTEXT)
BEGIN
  SET @`sql_query` := `sql_query`;
  SELECT CONCAT('Executing ', @`sql_query`);
  PREPARE `stmt` FROM @`sql_query`;
  EXECUTE `stmt`;
  DEALLOCATE PREPARE `stmt`;
END $$

CREATE FUNCTION SPLIT_STR(
  x VARCHAR(255),
  delim VARCHAR(12),
  pos INT
)
RETURNS VARCHAR(255)
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');