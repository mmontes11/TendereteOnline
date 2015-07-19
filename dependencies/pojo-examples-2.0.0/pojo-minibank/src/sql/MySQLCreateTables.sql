-- ----------------------------------------------------------------------------
-- "MiniBank" example.
-------------------------------------------------------------------------------

-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool -----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE AccountOp;
DROP TABLE Account;

-- ------------------------------- Account ------------------------------------

CREATE TABLE Account ( accId BIGINT NOT NULL AUTO_INCREMENT,
    usrId BIGINT NOT NULL, balance DOUBLE PRECISION NOT NULL,
    version BIGINT, CONSTRAINT AccountPK PRIMARY KEY(accId),
    CONSTRAINT validBalance CHECK ( balance >= 0 ) ) ENGINE = InnoDB;

CREATE INDEX AccountIndexByAccId ON Account (accId);
CREATE INDEX AccountIndexByUserId ON Account (accId, usrId);

-- ----------------------------- AccountOperation -----------------------------

CREATE TABLE AccountOp (accOpId BIGINT NOT NULL AUTO_INCREMENT,
    accId BIGINT NOT NULL, date TIMESTAMP NOT NULL,
    type TINYINT NOT NULL, amount DOUBLE PRECISION NOT NULL,
    CONSTRAINT AccountOpPK PRIMARY KEY(accOpId),
    CONSTRAINT AccountOpAccIdFK FOREIGN KEY(accId)
        REFERENCES Account (accId),
    CONSTRAINT validType CHECK ( type >= 0 AND type <=1 ),
    CONSTRAINT validAmount CHECK ( amount > 0 ),
    INDEX AccountOpIndexForFK (accId)) ENGINE = InnoDB;

CREATE INDEX AccountOpIndexByAccOpId ON AccountOp (accOpId);
CREATE INDEX AccountOpIndexByDate ON AccountOp (accOpId, date);
