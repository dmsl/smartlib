SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `SMARTLIB_BOOK` (
  `B_ID` int(11) NOT NULL auto_increment,
  `U_ID` int(11) NOT NULL,
  `BI_ID` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL default '0',
  `dateOfInsert` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`B_ID`),
  KEY `USERID` (`U_ID`),
  KEY `FKUSER_ID` (`U_ID`),
  KEY `FK_BI_ID_WOW` (`BI_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;



CREATE TABLE IF NOT EXISTS `SMARTLIB_BOOK_INFO` (
  `BI_ID` int(11) NOT NULL auto_increment,
  `isbn` varchar(20) collate utf8_unicode_ci NOT NULL,
  `title` varchar(200) collate utf8_unicode_ci NOT NULL,
  `authors` varchar(100) collate utf8_unicode_ci NOT NULL,
  `publishedYear` int(5) NOT NULL,
  `pageCount` int(5) NOT NULL,
  `dateOfInsert` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `imgURL` varchar(200) collate utf8_unicode_ci NOT NULL default 'images/nocover.png',
  `lang` varchar(2) collate utf8_unicode_ci NOT NULL default 'en',
  PRIMARY KEY  (`BI_ID`),
  UNIQUE KEY `isbn_UNIQUE` (`isbn`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=0 ;



CREATE TABLE IF NOT EXISTS `SMARTLIB_BOOK_KEYWORDS` (
  `keyword` varchar(20) collate utf8_unicode_ci NOT NULL,
  `B_ID` int(11) NOT NULL,
  PRIMARY KEY  (`keyword`,`B_ID`),
  KEY `fk_BOOK_KEYWORDS_BOOK1` (`B_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE IF NOT EXISTS `SMARTLIB_BORROWS` (
  `BORROW_ID` int(11) NOT NULL auto_increment,
  `B_ID` int(11) NOT NULL,
  `U_ID` int(11) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`BORROW_ID`),
  UNIQUE KEY `B_ID_UNIQUE` (`B_ID`),
  KEY `fk_REQUESTS_BOOK1` (`B_ID`),
  KEY `fk_REQUESTS_USER10` (`U_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;

CREATE TABLE IF NOT EXISTS `SMARTLIB_BORROW_HISTORY` (
  `BH_ID` int(11) NOT NULL auto_increment,
  `B_ID` int(11) NOT NULL,
  `U_ID` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `dateComplete` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`BH_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=0 ;


CREATE TABLE IF NOT EXISTS `SMARTLIB_REQUESTS` (
  `U_ID` int(11) NOT NULL,
  `B_ID` int(11) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `acknowledge` tinyint(4) NOT NULL default '-1',
  PRIMARY KEY  (`B_ID`,`U_ID`),
  KEY `fk_REQUESTS_BOOK1` (`B_ID`),
  KEY `fk_REQUESTS_USER1` (`U_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `SMARTLIB_USER` (
  `U_ID` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci default NULL,
  `password` varchar(255) collate utf8_unicode_ci default NULL,
  `name` varchar(20) collate utf8_unicode_ci NOT NULL,
  `surname` varchar(30) collate utf8_unicode_ci NOT NULL,
  `email` varchar(50) collate utf8_unicode_ci NOT NULL,
  `telephone` varchar(15) collate utf8_unicode_ci NOT NULL,
  `allowRequests` tinyint(4) NOT NULL default '1',
  `level` tinyint(4) NOT NULL default '0',
  `activationCode` varchar(10) collate utf8_unicode_ci default NULL,
  `registerDate` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`U_ID`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='User table' AUTO_INCREMENT=0 ;


ALTER TABLE `SMARTLIB_BOOK`
  ADD CONSTRAINT `FK_BI_ID` FOREIGN KEY (`BI_ID`) REFERENCES `SMARTLIB_BOOK_INFO` (`BI_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_USER_ID` FOREIGN KEY (`U_ID`) REFERENCES `SMARTLIB_USER` (`U_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;



ALTER TABLE `SMARTLIB_BOOK_KEYWORDS`
  ADD CONSTRAINT `fk_BOOK_KEYWORDS_BOOK1` FOREIGN KEY (`B_ID`) REFERENCES `SMARTLIB_BOOK` (`B_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `SMARTLIB_BORROWS`
  ADD CONSTRAINT `fk_REQUESTS_BOOK10` FOREIGN KEY (`B_ID`) REFERENCES `SMARTLIB_BOOK` (`B_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_REQUESTS_USER10` FOREIGN KEY (`U_ID`) REFERENCES `SMARTLIB_USER` (`U_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `SMARTLIB_REQUESTS`
  ADD CONSTRAINT `fk_REQUESTS_BOOK1` FOREIGN KEY (`B_ID`) REFERENCES `SMARTLIB_BOOK` (`B_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_REQUESTS_USER1` FOREIGN KEY (`U_ID`) REFERENCES `SMARTLIB_USER` (`U_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;