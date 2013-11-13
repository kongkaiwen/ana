/* 

{"missing": {"boolean": true, "text": text}, "incorrect": {"boolean": true, "text": text}} <- kbeval.jsp
{"model": value, "description": text}  <- evaluate.jsp 

*/

DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS kbeval;

CREATE TABLE chat (
	ID int NOT NULL AUTO_INCREMENT,
	Model varchar(255) NOT NULL,
	Description varchar(255),
	Correct varchar(255),
	PRIMARY KEY (ID)
);

CREATE TABLE kbeval (
	ID int AUTO_INCREMENT,
	Missing tinyint(1),
	MissingTXT text,
	Incorrect tinyint(1),
	IncorrectTXT text,
	PRIMARY KEY (ID)
);
