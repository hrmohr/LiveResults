--
-- Table structure for table `authorities`
--

CREATE TABLE `authorities` (
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `authority` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`),
  KEY `fk_authorities_users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `wca_competitions`
--

CREATE TABLE `wca_competitions` (
  `competitionId` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `country` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `endDate` date DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `organiser` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `startDate` date NOT NULL,
  `venue` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wcaDelegate` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `website` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`competitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `wca_competitors`
--

CREATE TABLE `wca_competitors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `firstname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `surname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wcaId` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `registeredEventsId` int(11) NOT NULL,
  `competitionId` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK170ADA7D970425DB` (`competitionId`),
  KEY `FK170ADA7D87C1F2EF` (`registeredEventsId`),
  CONSTRAINT `FK170ADA7D87C1F2EF` FOREIGN KEY (`registeredEventsId`) REFERENCES `wca_registered_events` (`id`),
  CONSTRAINT `FK170ADA7D970425DB` FOREIGN KEY (`competitionId`) REFERENCES `wca_competitions` (`competitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `wca_events`
--

DROP TABLE IF EXISTS `wca_events`;
CREATE TABLE `wca_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `format` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `live` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `timeFormat` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `competitionId` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK95646183970425DB` (`competitionId`),
  CONSTRAINT `FK95646183970425DB` FOREIGN KEY (`competitionId`) REFERENCES `wca_competitions` (`competitionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `wca_registered_events`
--

CREATE TABLE `wca_registered_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `signedUpFor2x2` bit(1) NOT NULL,
  `signedUpFor3x3` bit(1) NOT NULL,
  `signedUpFor4x4` bit(1) NOT NULL,
  `signedUpFor5x5` bit(1) NOT NULL,
  `signedUpFor6x6` bit(1) NOT NULL,
  `signedUpFor7x7` bit(1) NOT NULL,
  `signedUpForBf` bit(1) NOT NULL,
  `signedUpForBf4` bit(1) NOT NULL,
  `signedUpForBf5` bit(1) NOT NULL,
  `signedUpForClk` bit(1) NOT NULL,
  `signedUpForFeet` bit(1) NOT NULL,
  `signedUpForFm` bit(1) NOT NULL,
  `signedUpForMbf` bit(1) NOT NULL,
  `signedUpForMgc` bit(1) NOT NULL,
  `signedUpForMinx` bit(1) NOT NULL,
  `signedUpForMmgc` bit(1) NOT NULL,
  `signedUpForOh` bit(1) NOT NULL,
  `signedUpForPyr` bit(1) NOT NULL,
  `signedUpForSq1` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `wca_results`
--

CREATE TABLE `wca_results` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `average` int(11) NOT NULL,
  `best` int(11) NOT NULL,
  `country` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `firstname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `regionalAverageRecord` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `regionalSingleRecord` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result1` int(11) NOT NULL,
  `result2` int(11) NOT NULL,
  `result3` int(11) NOT NULL,
  `result4` int(11) NOT NULL,
  `result5` int(11) NOT NULL,
  `surname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `wcaId` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `worst` int(11) NOT NULL,
  `eventId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAA9EC8CC63AC8651` (`eventId`),
  CONSTRAINT `FKAA9EC8CC63AC8651` FOREIGN KEY (`eventId`) REFERENCES `wca_events` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
