-- New script in culturelinkdb.
-- Date: 2024. 7. 15.
-- Time: 오전 3:14:25
--db-oa18v.pub-cdb.ntruss.com                - target database host
--db-oa18v.pub-cdb.ntruss.com         - tunnel host name
--3306                - target database port
--${server}              - target server name
--culturelinkdb            - target database name
--dkswl                - database user name
--jdbc:mysql://db-oa18v.pub-cdb.ntruss.com:3306/culturelinkdb                 - connection URL
--dev     - connection type
--culturelinkdb          - datasource
--C:\Users\dkswl\OneDrive\Documents\code_upload\Auto_window\multi_it\backend\db\db1\General        - project path
--General        - project name
--2024. 7. 15.                - current date
--C:\Users\dkswl\OneDrive\Documents\code_upload\Auto_window\multi_it\backend\db\db1           - workspace path
--C:\Users\dkswl                - OS user home path
--C:\Users\dkswl\AppData\Local\DBeaver        - application install path
--C:\Users\dkswl\AppData\Local\DBeaver    - application install path
--DBeaver    - application name
--24.0.3.202404211624 - application version
--222.99.55.179            - local IP address
--2024. 7. 15.                - current date
--오전 3:14:25                - current time
--dkswl                - OS user name


/*CREATE TABLE region(
    region_id INT AUTO_INCREMENT PRIMARY KEY,
    region_name VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);*/


CREATE TABLE user(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100),
    password VARCHAR(100) UNIQUE,
    user_name VARCHAR(100),
    tel VARCHAR(100),
    gender VARCHAR(20),
    region_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE `user` ADD CONSTRAINT `FK_REGION_TO_USERS_1` FOREIGN KEY(
	`region_id`
)
REFERENCES `region`(
	`region_id`
);


ALTER TABLE `user`
DROP CONSTRAINT `FK_REGION_TO_USERS_1`;


DROP TABLE users cascade;
DROP TABLE region cascade;

CREATE TABLE role(
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100),
    role_content VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE role cascade;



CREATE TABLE user_role_mapping(
    user_id INT,
    role_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`user_id`,`role_id`)
);

ALTER TABLE `user_role_mapping` ADD CONSTRAINT `FK_USERS_TO_USERS_ROLE_MAPPING_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);

ALTER TABLE `user_role_mapping` ADD CONSTRAINT `FK_ROLE_TO_USERS_ROLE_MAPPING_1` FOREIGN KEY(
	`role_id`
)
REFERENCES `role`(
	`role_id`
);

DROP TABLE user_role_mapping cascade;





/*CREATE TABLE category(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100),
    category_content VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE category cascade;

CREATE TABLE culture(
    culture_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE `culture` ADD CONSTRAINT `FK_CULTURE_CATAEGORY_TO_CULTURE_1` FOREIGN KEY(
	`category_id`
)
REFERENCES `category`(
	`id`
);

DROP TABLE culture cascade;*/



CREATE TABLE time(
    time_id INT AUTO_INCREMENT PRIMARY KEY,
    time_name VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE time cascade;

CREATE TABLE festival(
    festival_id INT AUTO_INCREMENT PRIMARY KEY,
/*    category_id INT,
    culture_id INT,*/
    region_id INT,
    time_id INT,
--    avg_rate DOUBLE,
    festival_name VARCHAR(100),
    season VARCHAR(100),
    start_date DATETIME,
    end_date DATETIME,
    place VARCHAR(100),
    festival_content VARCHAR(300),
    img_url VARCHAR(300),
    host_institution VARCHAR(100),
    sponser_institution VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

/*ALTER TABLE `festival` ADD CONSTRAINT `FK_CATEGORY_TO_FESTIVAL_1` FOREIGN KEY(
	`category_id`
)
REFERENCES `category`(
	`category_id`
);

ALTER TABLE `festival` ADD CONSTRAINT `FK_CULTURE_TO_FESTIVAL_1` FOREIGN KEY(
	`culture_id`
)
REFERENCES `culture`(
	`culture_id`
);*/

ALTER TABLE `festival` ADD CONSTRAINT `FK_REGION_TO_FESTIVAL_1` FOREIGN KEY(
	`region_id`
)
REFERENCES `region`(
	`region_id`
);

ALTER TABLE `festival` ADD CONSTRAINT `FK_TIME_TO_FESTIVAL_1` FOREIGN KEY(
	`time_id`
)
REFERENCES `time`(
	`time_id`
);



DROP TABLE festival cascade;



CREATE TABLE sort(
    sort_id INT AUTO_INCREMENT PRIMARY KEY,
    sort_name VARCHAR(100),
    sort_content VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE sort cascade;




CREATE TABLE festival_keyword(
    festival_keyword_id INT AUTO_INCREMENT PRIMARY KEY,
    festival_keyword_name VARCHAR(100),
    emotion_stat INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

/*ALTER TABLE `festival_keyword` ADD CONSTRAINT `FK_CATEGORY_TO_FESTIVAL_KEYWORD_1` FOREIGN KEY(
	`category_id`
)
REFERENCES `category`(
	`category_id`
);*/


DROP TABLE festival_keyword cascade;






CREATE TABLE festival_review_naver_keyword_mapping(
    festival_id INT,
    sort_id INT,
    keyword_id INT,
    festival_count INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`festival_id`,`sort_id`,`keyword_id`)
);

ALTER TABLE `festival_review_naver_keyword_mapping` ADD CONSTRAINT `FK_FESTIVAL_TO_FESTIVAL_REVIEW_NAVER_KEYWORD_MAPPING_1` FOREIGN KEY(
	`festival_id`
)
REFERENCES `festival`(
	`festival_id`
);

ALTER TABLE `festival_review_naver_keyword_mapping` ADD CONSTRAINT `FK_KEYWORD_SORT_TO_FESTIVAL_REVIEW_NAVER_KEYWORD_MAPPING_1` FOREIGN KEY(
	`sort_id`
)
REFERENCES `sort`(
	`sort_id`
);

ALTER TABLE `festival_review_naver_keyword_mapping` ADD CONSTRAINT `FK_FESTIVAL_KEYWORD_TO_FESTIVAL_REVIEW_NAVER_KEYWORD_MAPPING_1` FOREIGN KEY(
	`keyword_id`
)
REFERENCES `festival_keyword`(
	`festival_keyword_id`
);


DROP TABLE festival_review_naver_keyword_mapping cascade;




CREATE TABLE festival_review(
    festival_review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    festival_id INT,
    festival_review_star DOUBLE,
    festival_review_content VARCHAR(1000),
    attachment VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE `festival_review` ADD CONSTRAINT `FK_USERS_TO_FESTIVAL_REVIEW_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);

ALTER TABLE `festival_review` ADD CONSTRAINT `FK_FESTIVAL_TO_FESTIVAL_REVIEW_1` FOREIGN KEY(
	`festival_id`
)
REFERENCES `festival`(
	`festival_id`
);


DROP TABLE festival_review cascade;





CREATE TABLE festival_naver_url_mapping(

    festival_id INT,
    page_url VARCHAR(300),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`festival_id`,`page_url`)
);


ALTER TABLE `festival_naver_url_mapping` ADD CONSTRAINT `FK_FESTIVAL_TO_FESTIVAL_NAVER_URL_1` FOREIGN KEY(
	`festival_id`
)
REFERENCES `festival`(
	`festival_id`
);


DROP TABLE festival_naver_url cascade;





CREATE TABLE user_festival_love_hate_keyword_mapping(
    user_id INT,
    sort_id INT,
    keyword_id INT,
    festival_count INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`user_id`,`sort_id`,`keyword_id`)
);

ALTER TABLE `user_festival_love_hate_keyword_mapping` ADD CONSTRAINT `FK_USERS_TO_USER_FESTIVAL_LOVE_HATE_KEYWORD_MAPPING_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);

ALTER TABLE `user_festival_love_hate_keyword_mapping` ADD CONSTRAINT `FK_SORT_TO_USER_FESTIVAL_LOVE_HATE_KEYWORD_MAPPING_1` FOREIGN KEY(
	`sort_id`
)
REFERENCES `sort`(
	`sort_id`
);

ALTER TABLE `user_festival_love_hate_keyword_mapping` ADD CONSTRAINT `FK_KEYWORD_TO_USER_FESTIVAL_LOVE_HATE_KEYWORD_MAPPING_1` FOREIGN KEY(
	`keyword_id`
)
REFERENCES `festival_keyword`(
	`festival_keyword_id`
);


DROP TABLE user_festival_love_hate_keyword_mapping cascade;






CREATE TABLE user_festival_love_hate_festival_mapping(
    user_id INT,
    sort ENUM('찜','관심없음'),
    festival_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`user_id`,`sort`, `festival_id`)
);

ALTER TABLE `user_festival_love_hate_festival_mapping` ADD CONSTRAINT `FK_USERS_TO_USER_FESTIVAL_LOVE_HATE_CULTURE_MAPPING_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);


ALTER TABLE `user_festival_love_hate_festival_mapping` ADD CONSTRAINT `FK_KEYWORD_TO_USER_FESTIVAL_LOVE_HATE_FESTIVAL_MAPPING_1` FOREIGN KEY(
	`festival_id`
)
REFERENCES `festival`(
	`festival_id`
);


DROP TABLE user_festival_love_hate_culture_mapping cascade;






CREATE TABLE user_time_mapping(
    user_id INT,
    time_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`user_id`,`time_id`)
);


ALTER TABLE `user_time_mapping` ADD CONSTRAINT `FK_USER_TO_USER_TIME_MAPPING_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);

ALTER TABLE `user_time_mapping` ADD CONSTRAINT `FK_TIME_TO_USER_TIME_MAPPING_1` FOREIGN KEY(
	`time_id`
)
REFERENCES `time`(
	`time_id`
);

DROP TABLE user_time_mapping cascade;










CREATE TABLE performance_genre(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


DROP TABLE performance_genre cascade;







CREATE TABLE user_genre_mapping(
    user_id INT,
    performance_genre_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`user_id`,`performance_genre_id`)
);


ALTER TABLE `user_genre_mapping` ADD CONSTRAINT `FK_USER_TO_USER_GENRE_MAPPING_1` FOREIGN KEY(
	`user_id`
)
REFERENCES `user`(
	`user_id`
);

ALTER TABLE `user_genre_mapping` ADD CONSTRAINT `FK_PERFORMANCE_GENRE_TO_USER_GENRE_MAPPING_1` FOREIGN KEY(
	`performance_genre_id`
)
REFERENCES `performance_genre`(
	`id`
);

DROP TABLE user_genre_mapping cascade;









CREATE VIEW vw_user_role_data AS
	SELECT
		u.user_id,
		u.email,
		u.password,
		u.user_name,
		u.tel,
		u.gender,
		u.region_id,
		r.role_id,
		r.role_content

	FROM user u
		JOIN user_role_mapping urm ON u.user_id=urm.user_id
		JOIN role r ON urm.role_id=r.role_id;















ALTER TABLE `region_map` ADD CONSTRAINT `FK_REGION_TO_REGION_MAP_1` FOREIGN KEY(
	`region_id`
)
REFERENCES `region`(
	`region_id`
);




INSERT INTO region
VALUES
( '66', '전국',now(), now()),
( '11', '서울',now(), now()),
( '21', '부산',now(), now()),
( '22', '대구',now(), now()),
( '23', '인천',now(), now()),
( '24', '광주',now(), now()),
( '25', '대전',now(), now()),
( '26', '울산',now(), now()),
( '45', '세종',now(), now()),
( '31', '경기',now(), now()),
( '32', '강원',now(), now()),
( '33', '충북',now(), now()),
( '34', '충남',now(), now()),
( '35', '전북',now(), now()),
( '36', '전남',now(), now()),
( '37', '경북',now(), now()),
( '38', '경남',now(), now()),
( '50', '제주',now(), now());



ALTER TABLE `cultural_properties` ADD CONSTRAINT `FK_REGION_TO_CULTURAL_PROPERTIES_1` FOREIGN KEY(
	`city_code`
)
REFERENCES `region`(
	`region_id`
);




ALTER TABLE festival_naver_url
DROP CONSTRAINT PRIMARY;


CREATE VIEW vw_user_review_data AS
	SELECT
		fr.*,
		u.user_name,
		u.user_profile_pic

	FROM user u
		JOIN
			festival_review fr
		ON
			u.user_id=fr.user_id;


ALTER TABLE festival_naver_url
DROP CONSTRAINT PRIMARY;





CREATE OR REPLACE VIEW vw_festival_keyword_tf_idf_data AS

WITH all_words_count AS(

	SELECT fc.festival_id , fc.sort_code , SUM(fc.freq) AS all_words_count

	from festival_content_review_naver_keyword_mapping fc

	GROUP BY fc.festival_id , fc.sort_code


),

all_docu_count AS(

	SELECT fc.sort_code, COUNT(DISTINCT fc.festival_id) AS all_docu_count

	from festival_content_review_naver_keyword_mapping fc

	GROUP BY fc.sort_code

),

include_word_docu_count AS(

	SELECT fc.sort_code, fc.festival_keyword_id, COUNT(DISTINCT fc.festival_id) AS include_word_docu_count

	from festival_content_review_naver_keyword_mapping fc

	GROUP BY fc.festival_keyword_id,
			fc.sort_code


)

SELECT

	 fc.festival_id ,
	 fc.sort_code,
	 fc.festival_keyword_id,

	 fc.freq,
	 all_words_count.all_words_count,
     all_docu_count.all_docu_count,
     include_word_docu_count.include_word_docu_count,


     (fc.freq)/(all_words_count.all_words_count) AS "tf",


     LOG10((all_docu_count.all_docu_count/(include_word_docu_count.include_word_docu_count + 1)))
     AS "idf",

     (fc.freq)/(all_words_count.all_words_count)*LOG10((all_docu_count.all_docu_count
     /(include_word_docu_count.include_word_docu_count + 1)))
     AS "tf_idf"

FROM
	festival_content_review_naver_keyword_mapping fc

LEFT JOIN all_words_count ON
	fc.festival_id = all_words_count.festival_id
		AND  fc.sort_code = all_words_count.sort_code

LEFT JOIN all_docu_count ON
		fc.sort_code = all_docu_count.sort_code

LEFT JOIN include_word_docu_count ON
	fc.festival_keyword_id = include_word_docu_count.festival_keyword_id
	AND  fc.sort_code = include_word_docu_count.sort_code;






SELECT fc.festival_id , fc.sort_code , SUM(fc.freq)

from festival_content_review_naver_keyword_mapping fc

GROUP BY fc.festival_id , fc.sort_code;


SELECT SUM(fc.freq) from festival_content_review_naver_keyword_mapping fc;

SELECT SUM(fc.freq) from festival_content_review_naver_keyword_mapping fc
WHERE fc.festival_id =148;



SELECT fc.sort_code, COUNT(DISTINCT fc.festival_id) from festival_content_review_naver_keyword_mapping fc
GROUP BY fc.sort_code ;




SELECT fc.sort_code, fc.festival_keyword_id, COUNT(DISTINCT fc.festival_id)

FROM festival_content_review_naver_keyword_mapping fc

GROUP BY fc.festival_keyword_id,
		fc.sort_code;




SELECT fc.festival_id , fc.sort_code , SUM(fc.freq)

from festival_content_review_naver_keyword_mapping fc

GROUP BY fc.festival_id , fc.sort_code;


SELECT SUM(fc.freq) from festival_content_review_naver_keyword_mapping fc;

SELECT SUM(fc.freq) from festival_content_review_naver_keyword_mapping fc
WHERE fc.festival_id =148;

SELECT COUNT(DISTINCT fc.festival_id) from festival_content_review_naver_keyword_mapping fc;


SELECT fc.festival_keyword_id, COUNT(DISTINCT fc.festival_id)

from festival_content_review_naver_keyword_mapping fc

GROUP BY fc.festival_keyword_id;




CREATE OR REPLACE VIEW vw_festival_keyword_tf_idf_top10_data AS

WITH ranked_tf_id_data AS(

	SELECT
		vf.*,
		ROW_NUMBER()
		OVER (PARTITION BY vf.festival_id, vf.sort_code ORDER BY vf.tf_idf DESC) AS tf_idf_rank
	FROM
		vw_festival_keyword_tf_idf_data vf
)

SELECT
	rd.festival_id,
	rd.sort_code,
	rd.festival_keyword_id,
	rd.freq,
	rd.all_words_count,
	rd.all_docu_count,
	rd.include_word_docu_count,
	rd.tf,
	rd.idf,
	rd.tf_idf

FROM ranked_tf_id_data rd

WHERE rd.tf_idf_rank<=10;



CREATE OR REPLACE VIEW vw_festival_user_love_hate_keyword_mapping_data AS
SELECT
	uf.user_id,
	uf.sort_code,
	vf.festival_keyword_id,
	COUNT(vf.festival_keyword_id) AS festival_keyword_count


FROM
	user_festival_love_hate_festival_mapping uf
LEFT JOIN
	vw_festival_keyword_tf_idf_top10_data vf ON
		uf.festival_id = vf.festival_id
GROUP BY
	uf.user_id,
	uf.sort_code,
	vf.festival_keyword_id;