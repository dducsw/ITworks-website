CREATE DATABASE RECRUITMENT_WEBSITE;
GO 

USE RECRUITMENT_WEBSITE;
GO

CREATE TABLE [USER]
(
	ID				int				IDENTITY(1,1)			PRIMARY KEY,
    Username		nvarchar(50)	UNIQUE					NOT NULL,
    Email			varchar(255)	UNIQUE					NOT NULL,
    Password		varchar(255)							NOT NULL,
    FName			nvarchar(50)							NOT NULL,
    LName			nvarchar(50)							NOT NULL,
    Created_date	datetime2		DEFAULT GETDATE()		NOT NULL,
    Profile_Picture varchar(255)							NOT NULL,
    Address			nvarchar(255)							NOT NULL,
    BDate			date									NOT NULL,
    Bio				nvarchar(max)							NOT NULL,
    PhoneNum		varchar(20)								NOT NULL,

	-- Ràng buộc user phải đủ 18 tuổi trở lên
	CHECK (BDate IS NULL OR BDate <= DATEADD(YEAR, -18, GETDATE())),
	-- Ràng buộc email đúng định dạng
    CHECK (Email IS NULL OR Email LIKE '%_@__%.__%'),
	-- Ràng buộc sđt đúng định dạng
	CHECK (PhoneNum NOT LIKE '%[^0-9]%' AND PhoneNum LIKE '0%'),
	-- Ràng buộc mật khẩu phải từ 6 kí tự trở lên 
	CHECK (LEN(Password) >= 6)
);
GO

--------------------------------------------------------------------------------
CREATE TABLE [PACKAGE]
(
	PackageName			nvarchar(100)						PRIMARY KEY,
	Cost				decimal(18,2)						NOT NULL,
	Description			nvarchar(max)						NOT NULL,
);
GO

--------------------------------------------------------------------------------
CREATE TABLE [CANDIDATE]
(
	CandidateID		int				            		PRIMARY KEY,
);
ALTER TABLE CANDIDATE 
ADD CONSTRAINT FK_CANDIDATE_USER
FOREIGN KEY (CandidateID) REFERENCES [dbo].[USER](ID)
ON DELETE CASCADE ON UPDATE CASCADE;
GO

-----------------------------------------------------------------------------
CREATE TABLE [COMPANY]
(
	MgrEmployerID	int				DEFAULT 0			NOT NULL,
	TaxNumber		varchar(13)							PRIMARY KEY,
	CompanyName		nvarchar(100)						NOT NULL,
    CNationality	nvarchar(50)						NOT NULL,
    Website			varchar(255)						NOT NULL,
    Industry		nvarchar(100)						NOT NULL,
    CompanySize		int									NOT NULL,
    Logo			varbinary(max)						NOT NULL,
    Description		nvarchar(max)						NOT NULL,
    PackageName		nvarchar(100)						NOT NULL,
);

ALTER TABLE COMPANY
ADD CONSTRAINT CK_Company_WebsiteFormat
CHECK (Website IS NULL OR Website LIKE 'http://%' OR Website LIKE 'https://%');

ALTER TABLE COMPANY
ADD CONSTRAINT FK_COMPANY_PACKAGE
FOREIGN KEY (PackageName) REFERENCES  [dbo].[PACKAGE](PackageName)
ON DELETE NO ACTION ON UPDATE CASCADE;

ALTER TABLE [COMPANY]
ADD CONSTRAINT FK_COMPANY_EMPLOYER
FOREIGN KEY (MgrEmployerID) REFERENCES [dbo].[EMPLOYER](EmployerID);
GO

-----------------------------------------------------------------------------------
CREATE TABLE [EMPLOYER]
(
	EmployerID		int										PRIMARY KEY,
	TaxNumber		varchar(13)								NOT NULL,

	-- Ràng buộc TaxNumber đúng định dạng
	CONSTRAINT CK_EMPLOYER_TaxNumberFormat
    CHECK (TaxNumber LIKE '[0-9]%' AND (LEN(TaxNumber) = 10 OR LEN(TaxNumber) = 13)),
);

ALTER TABLE EMPLOYER
ADD CONSTRAINT FK_EMPLOYER_USER
FOREIGN KEY (EmployerID) REFERENCES [dbo].[USER](ID)
ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE EMPLOYER ADD
CONSTRAINT FK_EMPLOYER_COMPANY 
FOREIGN KEY (TaxNumber) REFERENCES [dbo].[COMPANY](TaxNumber)
ON DELETE NO ACTION ON UPDATE CASCADE;


---------------------------------------------------------------------------------------
CREATE TABLE [FOLLOW]
(
	CandidateID		int				            		NOT NULL,
	EmployerID		int									NOT NULL,

	PRIMARY KEY (CandidateID , EmployerID),

);

ALTER TABLE FOLLOW
ADD CONSTRAINT FK_FOLLOW_CANDIDATE
FOREIGN KEY (CandidateID) REFERENCES [dbo].[CANDIDATE](CandidateID),	

CONSTRAINT FK_FOLLOW_EMPLOYER
FOREIGN KEY (EmployerID) REFERENCES [dbo].[EMPLOYER](EmployerID)
GO
--------------------------------------------------------------------------------------------
CREATE TABLE [SocialMediaLink]
(
	ID				 int				            		NOT NULL,
	Flatform		 varchar(50)							NOT NULL,
	Link			 varchar(255)							NOT NULL,

	PRIMARY KEY (ID , Flatform , Link),

	CONSTRAINT CK_SocialMediaLink_LinkFormat
	CHECK (Link IS NULL OR Link LIKE 'http://%' OR Link LIKE 'https://%')
);

ALTER TABLE SocialMediaLink
ADD CONSTRAINT FK_SocialMediaLink_USER 
FOREIGN KEY (ID) REFERENCES [dbo].[USER](ID)
ON DELETE CASCADE ON UPDATE CASCADE;
GO

-------------------------------------------------------------------------------------------
CREATE TABLE [WorkExperience]--
(
	CandidateID			int									NOT NULL,
	WorkID				int			   IDENTITY(1,1)	    PRIMARY KEY,
	savedCV				varchar(255)						NOT NULL,
	YearOfExperience	int									NOT NULL,
);

ALTER TABLE WorkExperience
ADD CONSTRAINT FK_WorkExperience_CANDIDATE
FOREIGN KEY (CandidateID) REFERENCES [dbo].[CANDIDATE](CandidateID);
GO

--------------------------------------------------------------------------------------------
CREATE TABLE [EDUCATION]--
(
	EduName				nvarchar(100)						PRIMARY KEY,
	EduType				nvarchar(30)						NOT NULL,
	Address				nvarchar(255)						NOT NULL,
);
GO

--------------------------------------------------------------------------------------------
CREATE TABLE [STUDY]--
(
	WorkID              int					                NOT NULL,
	EduName				nvarchar(100)						NOT NULL,
	Degree				nvarchar(100)						NOT NULL,
	StartYear			int											,
	EndYear				int											,

	PRIMARY KEY (WorkID , EduName, Degree),

	CHECK (StartYear >= 1900 AND StartYear <= YEAR(GETDATE())),
	CHECK (EndYear >= StartYear)
);
ALTER TABLE STUDY
ADD	CONSTRAINT FK_STUDY_WorkExperience 
FOREIGN KEY (WorkID) REFERENCES [dbo].[WorkExperience](WorkID);

ALTER TABLE STUDY
ADD CONSTRAINT FK_STUDY_EDUCATION
FOREIGN KEY (EduName) REFERENCES [dbo].[EDUCATION](EduName);
GO

----------------------------------------------------------------------------------------------
CREATE TABLE [CERTIFICATION]--
(
	WorkID				int									NOT NULL,
	CertID				varchar(50)							NOT NULL,    
    CertName			nvarchar(100)								,
    Score				nvarchar(20),
    Issuer 				nvarchar(100),
    issueDate 			date 								NOT NULL,
    expireDate 			date,

	PRIMARY KEY (WorkID , CertID),

    -- Ràng buộc kiểm tra ngày hết hạn phải sau ngày cấp
    CONSTRAINT CK_Certification_Expire 
    CHECK (expireDate IS NULL OR expireDate > issueDate)
);
ALTER TABLE CERTIFICATION
ADD CONSTRAINT FK_CERTIFICATION_WorkExperience 
FOREIGN KEY (WorkID) REFERENCES [dbo].[WorkExperience](WorkID);
GO

---------------------------------------------------------------------------------------------
CREATE TABLE [JobHistory]-- 
(
	WorkID				int									NOT NULL,
	HistoryID			int			IDENTITY(1,1)			NOT NULL,
	CompanyName			nvarchar(100)						NOT NULL, -- Them NOT NULL -dd
	StartTime			date ,
	EndTime				date ,
	Position			nvarchar(100),

	PRIMARY KEY (WorkID , HistoryID),

	-- Ràng buộc kiểm tra ngày kết thúc phải sau ngày bắt đầu
    CONSTRAINT CK_JobHistory_EndTime
	CHECK (EndTime IS NULL OR EndTime > StartTime),
);

ALTER TABLE JobHistory
ADD CONSTRAINT FK_JoBHistory_WorkExperience
FOREIGN KEY (WorkID) REFERENCES [dbo].[WorkExperience](WorkID);
GO

--------------------------------------------------------------------------------------------
CREATE TABLE [JOB]--
(
	JobID				int				IDENTITY(1,1)			PRIMARY KEY,
    JobType				nvarchar(50)							NOT NULL,
    ContractType		nvarchar(50)							NOT NULL,
    Level				nvarchar(50)							NOT NULL,
    Quantity			int									    NOT NULL,
    SalaryFrom			decimal(18,2)							NOT NULL,
    SalaryTo			decimal(18,2)							NOT NULL,
    RequireExpYear		int,
    Location			nvarchar(100)							NOT NULL,
    JD					nvarchar(max)							NOT NULL,							
    JobName				nvarchar(100)							NOT NULL,
    postDate			datetime2		DEFAULT	GETDATE(),
    expireDate			datetime2								NOT NULL,
    JobStatus			nvarchar(50)							NOT NULL,
    EmployerID			int										NOT NULL,
    TaxNumber			varchar(13)								NOT NULL,

	CHECK (TaxNumber LIKE '[0-9]%' AND (LEN(TaxNumber) = 10 OR LEN(TaxNumber) = 13)),
	CHECK (RequireExpYear >= 0),
	CHECK (Quantity > 0),
	CHECK (SalaryFrom >= 0),
	CHECK (SalaryTo >= SalaryFrom),
	CHECK (expireDate > postDate)
);

ALTER TABLE JOB
ADD CONSTRAINT FK_JOB_EMPLOYER
FOREIGN KEY (EmployerID)  REFERENCES [dbo].[EMPLOYER](EmployerID);

ALTER TABLE JOB
ADD CONSTRAINT FK_JOB_COMPANY
FOREIGN KEY (TaxNumber) REFERENCES [dbo].[COMPANY](TaxNumber);
GO

-----------------------------------------------------------------------------------------
CREATE TABLE [JOB_CATEGORY]  --
(
    JCName				nvarchar(100)							PRIMARY KEY,
    Speciality			nvarchar(100)
);
GO

-----------------------------------------------------------------------------------------
CREATE TABLE [RELATED] --
(
    JCName1				nvarchar(100),
    JCName2				nvarchar(100),

    PRIMARY KEY (JCName1, JCName2)
);

ALTER TABLE RELATED
ADD CONSTRAINT FK_Related_JCName1 FOREIGN KEY (JCName1) 
REFERENCES JOB_CATEGORY(JCName);

ALTER TABLE RELATED
ADD CONSTRAINT FK_Related_JCName2 FOREIGN KEY (JCName2) 
REFERENCES JOB_CATEGORY(JCName);
GO

------------------------------------------------------------------------------------------
CREATE TABLE [IN]--
(
	JobID				int				            			NOT NULL,
	JCName				nvarchar(100)							NOT NULL,

	PRIMARY KEY (JobID , JCName),
);

ALTER TABLE [IN]
ADD CONSTRAINT FK_IN_JOB
FOREIGN KEY (JobID) REFERENCES [dbo].[JOB](JobID);

ALTER TABLE [IN]
ADD CONSTRAINT FK_IN_JOB_CATEGORY 
FOREIGN KEY (JCName) REFERENCES [dbo].[JOB_CATEGORY](JCName);
GO

-------------------------------------------------------------------------------------------
CREATE TABLE [NOTIFICATION]-- 
(
	 nID				int				IDENTITY(1,1)			PRIMARY KEY,
    Content				nvarchar(max)							NOT NULL,
    Title				nvarchar(255)							NOT NULL,
    Time				datetime2		DEFAULT GETDATE()		NOT NULL,
    CandidateID			int										NULL,
    EmployerID			int										NULL,
    JobID				int										NULL,

);

ALTER TABLE NOTIFICATION
ADD CONSTRAINT FK_NOTIFICATION_CANDIDATE
FOREIGN KEY (CandidateID) REFERENCES [dbo].[CANDIDATE](CandidateID);

ALTER TABLE NOTIFICATION
ADD CONSTRAINT FK_NOTIFICATION_EMPLOYER
FOREIGN KEY (EmployerID) REFERENCES [dbo].[EMPLOYER](EmployerID);

ALTER TABLE NOTIFICATION
ADD	CONSTRAINT FK_NOTIFICATION_JOB
FOREIGN KEY (JobID) REFERENCES [dbo].[JOB](JobID);
GO

---------------------------------------------------------------------------------------------
CREATE TABLE [APPLY]--
(
	CandidateID				int								NOT NULL,
    JobID					int								NOT NULL,
    CoverLetter				nvarchar(max),
    Date					datetime2 DEFAULT GETDATE()		NOT NULL,
    Status					nvarchar(50),
    UploadCV				nvarchar(255),

    PRIMARY KEY (CandidateID, JobID),
);

ALTER TABLE APPLY
ADD CONSTRAINT FK_Apply_Candidate FOREIGN KEY (CandidateID) 
REFERENCES [dbo].[CANDIDATE](CandidateID)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE APPLY
ADD CONSTRAINT FK_Apply_Job FOREIGN KEY (JobID) 
REFERENCES [dbo].[JOB](JobID)
ON DELETE CASCADE ON UPDATE CASCADE;
GO

---------------------------------------------------------------------------------------------
CREATE TABLE [SKILL]--
(
	SkillID				int		IDENTITY(1,1)				PRIMARY KEY,
	SkillName			nvarchar(50)						NOT NULL,
	Description			nvarchar(max)								,
)
GO

---------------------------------------------------------------------------------------------
CREATE TABLE [REQUIRE]--
(
	JobID				INT		NOT NULL,
	SkillID				INT		NOT NULL,

	PRIMARY KEY (JobID, SkillID)
);

ALTER TABLE REQUIRE
ADD CONSTRAINT FK_REQUIRE_JOB FOREIGN KEY (JobID) REFERENCES JOB(JobID);

ALTER TABLE REQUIRE
ADD CONSTRAINT FK_REQUIRE_SKILL FOREIGN KEY (SkillID) REFERENCES SKILL(SkillID);
GO

--------------------------------------------------------------------------------------------
CREATE TABLE [INCLUDE]
(
	WorkID				INT		NOT NULL,
	SkillID				INT		NOT NULL,

	PRIMARY KEY (WorkID, SkillID)
);

ALTER TABLE INCLUDE 
ADD CONSTRAINT FK_INCLUDE_WORK 
FOREIGN KEY (WorkID) REFERENCES WorkExperience(WorkID) ON DELETE CASCADE;

ALTER TABLE INCLUDE 
ADD	CONSTRAINT FK_INCLUDE_SKILL 
FOREIGN KEY (SkillID) REFERENCES SKILL(SkillID) ON DELETE CASCADE;
GO

--------------------------------------------------------------------------------------------
CREATE TABLE [FAVORITE]
(
	CandidateID     INT             NOT NULL,   
    JobID           INT             NOT NULL,   
    Date            DATETIME2       NOT NULL DEFAULT GETDATE(),   
    
    PRIMARY KEY (CandidateID, JobID),          
);

ALTER TABLE FAVORITE 
ADD CONSTRAINT FK_FAVORITE_CANDIDATE
FOREIGN KEY (CandidateID) REFERENCES CANDIDATE(CandidateID)
ON DELETE CASCADE;
 
ALTER TABLE FAVORITE
ADD CONSTRAINT FK_FAVORITE_JOB
FOREIGN KEY (JobID) REFERENCES JOB(JobID)
ON DELETE CASCADE;
GO

------------------------------------------------------------------------------------------
CREATE TABLE FEEDBACK
(
    FeedID          INT             IDENTITY(1,1)   PRIMARY KEY,  
    CandidateID     INT             NOT NULL,       
    JobID           INT             NOT NULL,       
    Content         NVARCHAR(MAX)   NULL,           
    Rank            INT             NOT NULL,       
    FeedDate        DATETIME2       NOT NULL DEFAULT GETDATE(),  
    
    -- Ràng buộc cho trường Rank: chỉ nhận giá trị từ 1 đến 5
    CONSTRAINT CK_FEEDBACK_Rank
    CHECK (Rank BETWEEN 1 AND 5)
);

ALTER TABLE FEEDBACK
ADD CONSTRAINT FK_FEEDBACK_CANDIDATE
FOREIGN KEY (CandidateID) REFERENCES CANDIDATE(CandidateID)
ON DELETE CASCADE;

ALTER TABLE FEEDBACK
ADD CONSTRAINT FK_FEEDBACK_JOB
FOREIGN KEY (JobID) REFERENCES JOB(JobID)
ON DELETE CASCADE;
GO

----------------------------------------------------------------------------------------------
-- TRIGGER ứng viên chỉ được ứng tuyển mỗi công việc 1 lần 

CREATE TRIGGER TR_APPLY_PreventDuplicateApplications
ON APPLY
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra xem ứng viên đã ứng tuyển công việc này chưa
    IF EXISTS (
        SELECT 1 
        FROM APPLY a
        INNER JOIN inserted i ON a.CandidateID = i.CandidateID AND a.JobID = i.JobID
    )
    BEGIN
        RAISERROR(N'Ứng viên chỉ được ứng tuyển mỗi công việc 1 lần!', 16, 1);
        RETURN;
    END
    
    -- Nếu chưa ứng tuyển, thực hiện chèn dữ liệu
    INSERT INTO APPLY (CandidateID, JobID, CoverLetter, Date, Status, UploadCV)
    SELECT CandidateID, JobID, CoverLetter, Date, Status, UploadCV
    FROM inserted;
END;
GO

-----------------------------------------------------------------
-- TRIGGER Ứng viên chỉ được gửi đánh giá sau khi được ứng tuyển vào một công việc nào đó 

CREATE TRIGGER TR_FEEDBACK_CheckEmploymentStatus
ON FEEDBACK
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON; -- Ngăn chặn việc hiển thị thông báo "n row(s) affected"
    
    -- Kiểm tra xem ứng viên đã được tuyển dụng vào công việc đó chưa
    IF EXISTS (
        SELECT 1 
        FROM inserted i
        LEFT JOIN APPLY a ON i.CandidateID = a.CandidateID AND i.JobID = a.JobID
        WHERE a.CandidateID IS NULL OR a.Status <> N'Đã tuyển'
    )
    BEGIN
        RAISERROR(N'Ứng viên chỉ được gửi đánh giá sau khi được tuyển dụng vào công việc này!', 16, 1);
        RETURN;
    END
    
    -- Nếu điều kiện thỏa mãn, thực hiện chèn dữ liệu
    INSERT INTO FEEDBACK (CandidateID, JobID, Content, Rank, FeedDate)
    SELECT CandidateID, JobID, Content, Rank, FeedDate
    FROM inserted;
END;
GO

---------------------------------------------------------------------
-- TRIGGER bài đánh giá phải dựa vào số sao từ 1 đến 5 và không được để trống 

CREATE TRIGGER TR_FEEDBACK_ValidateRank_After
ON FEEDBACK
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra số sao có hợp lệ không
    IF EXISTS (
        SELECT 1 
        FROM inserted 
        WHERE Rank IS NULL OR Rank < 1 OR Rank > 5
    )
    BEGIN
        ROLLBACK TRANSACTION;
        RAISERROR(N'Số sao đánh giá phải từ 1 đến 5 và không được để trống!', 16, 1);
        RETURN;
    END
    
    -- Kiểm tra nội dung đánh giá có bị để trống không
    IF EXISTS (
        SELECT 1 
        FROM inserted 
        WHERE Content IS NULL OR TRIM(Content) = ''
    )
    BEGIN
        ROLLBACK TRANSACTION;
        RAISERROR(N'Nội dung đánh giá không được để trống!', 16, 1);
        RETURN;
    END
END;
GO

------------------------------------------------------------------------------
-- TRIGGER nhà tuyển dụng không thể đóng tin ứng tuyển nếu chưa xử lý hết các đơn ứng tuyển 

CREATE OR ALTER TRIGGER TR_JOB_PreventCloseIfPendingApplications
ON JOB
INSTEAD OF UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Kiểm tra xem có thay đổi trạng thái công việc thành "Đã đóng" không
    IF UPDATE(JobStatus) AND EXISTS (
        SELECT 1
        FROM inserted i
        INNER JOIN deleted d ON i.JobID = d.JobID
        WHERE i.JobStatus = N'Đã đóng'
          AND d.JobStatus <> N'Đã đóng'
    )
    BEGIN
        -- Kiểm tra xem có đơn ứng tuyển chưa xử lý không
        IF EXISTS (
            SELECT 1
            FROM inserted i
            INNER JOIN APPLY a ON i.JobID = a.JobID
            WHERE i.JobStatus = N'Đã đóng'
              AND a.Status NOT IN (N'Đã tuyển', N'Đã từ chối')
        )
        BEGIN
            -- Hiển thị thông báo lỗi chi tiết
            DECLARE @JobsWithPendingApplications TABLE (
                JobID INT,
                JobName NVARCHAR(100),
                PendingCount INT
            );
            
            INSERT INTO @JobsWithPendingApplications (JobID, JobName, PendingCount)
            SELECT 
                j.JobID,
                j.JobName,
                COUNT(a.CandidateID) AS PendingCount
            FROM inserted i
            INNER JOIN JOB j ON i.JobID = j.JobID
            INNER JOIN APPLY a ON j.JobID = a.JobID
            WHERE i.JobStatus = N'Đã đóng'
              AND a.Status NOT IN (N'Đã tuyển', N'Đã từ chối')
            GROUP BY j.JobID, j.JobName;
            
            DECLARE @ErrorMessage NVARCHAR(MAX) = N'';
            SELECT TOP 1 @ErrorMessage = N'Không thể đóng tin tuyển dụng "' + JobName + 
                                         N'" vì còn ' + CAST(PendingCount AS NVARCHAR(10)) + 
                                         N' đơn ứng tuyển chưa được xử lý.'
            FROM @JobsWithPendingApplications;
            
            RAISERROR(@ErrorMessage, 16, 1);
            RETURN;
        END
    END

    UPDATE j
    SET 
        j.JobType = i.JobType,
        j.ContractType = i.ContractType,
        j.Level = i.Level,
        j.Quantity = i.Quantity,
        j.SalaryFrom = i.SalaryFrom,
        j.SalaryTo = i.SalaryTo,
        j.RequireExpYear = i.RequireExpYear,
        j.Location = i.Location,
        j.JD = i.JD,
        j.JobName = i.JobName,
        j.postDate = i.postDate,
        j.expireDate = i.expireDate,
        j.JobStatus = i.JobStatus,
        j.EmployerID = i.EmployerID,
        j.TaxNumber = i.TaxNumber
    FROM JOB j
    INNER JOIN inserted i ON j.JobID = i.JobID;
END;
GO



