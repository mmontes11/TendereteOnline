-- -----------------------------------------------------------------------------
-- Advanced Hibernate Tutorial.
--------------------------------------------------------------------------------

-- Indexes for primary keys have been explicitly created.

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

-- The following constraint must be dropped before table Employee could be
-- dropped due to Department references Employee
--
ALTER TABLE Department DROP FOREIGN KEY DepartmentDirIdFK;

DROP TABLE Employee;
DROP TABLE Department;

-- ----------------------------- Department ------------------------------------

CREATE TABLE Department (depId BIGINT NOT NULL AUTO_INCREMENT, dirId BIGINT,
    name VARCHAR(100) NOT NULL, creationDate DATE NOT NULL,
    version BIGINT NOT NULL,
    CONSTRAINT DepartmentPK PRIMARY KEY(depId)) ENGINE = InnoDB;

CREATE INDEX DepartmentIndexByDepId ON Department (depId);

-- ------------------------------ Employee -------------------------------------

CREATE TABLE Employee (empId BIGINT NOT NULL AUTO_INCREMENT,
    depId BIGINT, firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, position VARCHAR(10) NOT NULL,
    salary INTEGER NOT NULL, version BIGINT NOT NULL,
    CONSTRAINT EmployeePK PRIMARY KEY(empId),
    CONSTRAINT EmployeeDepIdFK FOREIGN KEY(depId) REFERENCES Department (depId),
    INDEX EmployeeIndexForFK (depId)) ENGINE = InnoDB;

CREATE INDEX EmployeeIndexByEmpId ON Employee (empId);

-- The following constraint can not be added when the Department table is
-- created, since Department creation would reference to Employee, which is
-- created later (and also references Department table).
--
ALTER TABLE Department ADD CONSTRAINT DepartmentDirIdFK FOREIGN KEY(dirId)
    REFERENCES Employee (empId);