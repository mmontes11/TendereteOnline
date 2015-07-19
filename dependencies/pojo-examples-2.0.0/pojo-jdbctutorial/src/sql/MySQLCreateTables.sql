-- ----------------------------------------------------------------------------
-- JDBC Tutorial.
-------------------------------------------------------------------------------

DROP TABLE TutAccount;
CREATE TABLE TutAccount ( accId VARCHAR(40) COLLATE latin1_bin NOT NULL,
    balance DOUBLE PRECISION NOT NULL, 
    CONSTRAINT TutAccountPK PRIMARY KEY(accId),
    CONSTRAINT validBalance CHECK ( balance >= 0 ) ) ENGINE = InnoDB;

CREATE INDEX TutAccountIndexByAccId ON TutAccount (accId);
