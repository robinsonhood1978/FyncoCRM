# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.27)
# Database: fynco
# Generation Time: 2020-02-08 21:12:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table advertising
# ------------------------------------------------------------

DROP TABLE IF EXISTS `advertising`;

CREATE TABLE `advertising` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `advertise_name` varchar(50) NOT NULL COMMENT '告广名称',
  `version_name` varchar(50) NOT NULL COMMENT '版位名称',
  `image_url` varchar(100) NOT NULL COMMENT '图片路径',
  `link_url` varchar(100) NOT NULL DEFAULT '#' COMMENT '链接路径',
  `link_prompt` varchar(20) NOT NULL COMMENT '链接提示',
  `using` char(1) NOT NULL DEFAULT '1' COMMENT '是否启用（默认为1，启用）',
  `type` char(1) NOT NULL DEFAULT '0' COMMENT '认默为0，是广告图片。为1是合作伙伴',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table alert
# ------------------------------------------------------------

DROP TABLE IF EXISTS `alert`;

CREATE TABLE `alert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  `content` varchar(200) DEFAULT NULL,
  `class_name` varchar(30) NOT NULL DEFAULT '',
  `type` tinyint(2) DEFAULT NULL COMMENT 'notification类别id',
  `creator` int(11) NOT NULL COMMENT '创建人',
  `type_name` varchar(20) DEFAULT NULL COMMENT 'notification类别名称',
  `user_id` int(11) NOT NULL COMMENT '属于谁',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '行动开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '行动结束时间',
  `alert_time` timestamp NULL DEFAULT NULL COMMENT '提醒时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:需提醒 1:不需提醒',
  `allday` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否全天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='待办事项表';

LOCK TABLES `alert` WRITE;
/*!40000 ALTER TABLE `alert` DISABLE KEYS */;

INSERT INTO `alert` (`id`, `title`, `parent_id`, `content`, `class_name`, `type`, `creator`, `type_name`, `user_id`, `start_time`, `end_time`, `alert_time`, `create_time`, `status`, `allday`)
VALUES
	(30,'test',NULL,NULL,'bg-danger',20,24,'Meeting',24,'2020-01-07 11:35:00','2020-01-07 16:35:00','2020-01-06 11:35:00','2020-01-06 15:20:25',0,0),
	(31,'test',30,NULL,'bg-danger',20,24,'Meeting',25,'2020-01-07 11:35:00','2020-01-07 16:35:00','2020-01-06 11:35:00','2020-01-06 15:20:25',0,0),
	(32,'test',30,NULL,'bg-danger',20,24,'Meeting',26,'2020-01-07 11:35:00','2020-01-07 16:35:00','2020-01-06 11:35:00','2020-01-06 15:20:25',0,0);

/*!40000 ALTER TABLE `alert` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table application
# ------------------------------------------------------------

DROP TABLE IF EXISTS `application`;

CREATE TABLE `application` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `application_no` varchar(30) DEFAULT NULL,
  `application_date` date DEFAULT NULL,
  `adviser_name` varchar(30) DEFAULT NULL,
  `adviser_email` varchar(30) DEFAULT NULL,
  `adviser_contact_number` varchar(20) DEFAULT NULL,
  `lender` varchar(30) DEFAULT NULL,
  `lending_amount_required` decimal(20,2) DEFAULT NULL,
  `lending_purpose` json DEFAULT NULL,
  `purpose` varchar(100) DEFAULT NULL,
  `LVR` varchar(30) DEFAULT NULL,
  `finance_date` date DEFAULT NULL,
  `settlement_date` date DEFAULT NULL,
  `borrower` varchar(30) DEFAULT NULL,
  `security_holder` varchar(30) DEFAULT NULL,
  `UMI` varchar(30) DEFAULT NULL,
  `existing_loan_amount` decimal(20,2) DEFAULT NULL,
  `total_loan_amount` decimal(20,2) DEFAULT NULL,
  `security` json DEFAULT NULL,
  `documents` json DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `creator` int(11) DEFAULT NULL COMMENT '录入人员',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 0-Processing，1-Submitted,2-Pre-approved,3-Conditionally Approved,4-Pending Settlement,5-Completed',
  `asset` json DEFAULT NULL,
  `liability` json DEFAULT NULL,
  `monthincome` json DEFAULT NULL,
  `monthincome_from_rent` json DEFAULT NULL,
  `monthexpend` json DEFAULT NULL,
  `note` json DEFAULT NULL,
  `total_income` decimal(20,2) DEFAULT NULL,
  `total_expense` decimal(20,2) DEFAULT NULL,
  `surplus` decimal(20,2) DEFAULT NULL,
  `total_assets` decimal(20,2) DEFAULT NULL,
  `total_liability` decimal(20,2) DEFAULT NULL,
  `equity` decimal(20,2) DEFAULT NULL,
  `pdf` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;

INSERT INTO `application` (`id`, `application_no`, `application_date`, `adviser_name`, `adviser_email`, `adviser_contact_number`, `lender`, `lending_amount_required`, `lending_purpose`, `purpose`, `LVR`, `finance_date`, `settlement_date`, `borrower`, `security_holder`, `UMI`, `existing_loan_amount`, `total_loan_amount`, `security`, `documents`, `create_time`, `creator`, `status`, `asset`, `liability`, `monthincome`, `monthincome_from_rent`, `monthexpend`, `note`, `total_income`, `total_expense`, `surplus`, `total_assets`, `total_liability`, `equity`, `pdf`)
VALUES
	(30,NULL,'2020-01-21','Bin Luo','robinsonhood1978@gmail.com','02108063825','Bin Luo',3000000.00,'["Construction", "Development"]','Buy a new house','lvr is what','2020-02-09','2022-08-01','Potti','ANZ or Bnz','UMI is what',800000.00,109000000.00,'[{"use": "self living", "bank": "Anz", "owner": "Bin Luo", "value": "800000", "property": "2 Mellons Bay Rd., Howick, Auckland", "property_type": "Private"}, {"use": "rent", "bank": "Bnz", "owner": "Ceejay", "value": "900000", "property": "36 Eastern Beach Rd.", "property_type": "apartment"}]','{"payslip": {"url": "", "name": "Payslip"}, "appraisal": {"url": "", "name": "Appraisal"}, "ird_summary": {"url": "", "name": "IRD Summary"}, "bank_statement": {"url": "", "name": "Bank Statement"}, "gifting_letter": {"url": "", "name": "Gifting Letter"}, "loan_agreement": {"url": "", "name": "Loan agreement"}, "loan_statement": {"url": "", "name": "Loan Statement"}, "rate_lock_form": {"url": "", "name": "Rate Lock Form"}, "support_letter": {"url": "", "name": "Support Letter"}, "approval_letter": {"url": "", "name": "Approval Letter"}, "building_report": {"url": "", "name": "Building Report"}, "lease_agreement": {"url": "", "name": "Lease Agreement"}, "additional_files": {"url": "/upload/202001/202001301920004679313.pdf", "name": "Additional Files"}, "application_form": {"url": "/upload/202001/202001301920035978691.pdf", "name": "Application Form"}, "financial_report": {"url": "", "name": "Financial Report"}, "rental_agreement": {"url": "", "name": "Rental Agreement"}, "valuation_report": {"url": "", "name": "Valuation Report"}, "boarder_agreement": {"url": "", "name": "Boarder Agreement"}, "proof_of_identity": {"url": "", "name": "Proof of Identity"}, "sales_purchase_agreement": {"url": "", "name": "Sales Purchase Agreement"}}','2020-01-21 23:31:06',24,1,'{"cash": {"text": "Cash", "cash_d": "cash", "cash_v": "800"}, "saving": [{"text": "Saving", "saving_d": "Anz", "saving_v": "1000"}], "deposit": {"text": "Deposit on property (If Paid)", "deposit_d": "Deposit on property (If Paid)", "deposit_v": "8000"}, "vehicle": [{"text": "Motor Vehicle", "vehicle_d": "Honda", "vehicle_v": "700"}], "property": [{"text": "Property", "property_d": "Property1", "property_v": "10000"}, {"text": "Property 2", "property_d": "Property2", "property_v": "20000"}, {"text": "Property 3", "property_d": "Propert3", "property_v": "30000"}], "furniture": {"text": "Furniture", "furniture_d": "furniture", "furniture_v": "9000"}, "kiwisaver": [{"text": "KiwiSaver", "kiwisaver_d": "KiwiSaver1", "kiwisaver_v": "3000"}], "investment": [{"text": "Investment", "investment_d": "Iv1", "investment_v": "10000"}], "otherasset": [{"text": "Other Assets (Specify)", "otherasset_d": "Other Assets1", "otherasset_v": "1888"}], "businessown": [{"text": "Business owned", "businessown_d": "Business owned1", "businessown_v": "10001"}], "total_value": 111289, "term_deposit": {"text": "Term Deposit", "term_deposit_d": "term deposit", "term_deposit_v": "6000"}, "other_currency_account": {"text": "Other Currency Account", "other_currency_account_d": " Other Currency Account", "other_currency_account_v": "900"}}','{"loandrop": [{"loandrop_d": "Provider       1      Monthly Cost 1", "loandrop_v": "1000", "loandrop_sel": "Home Loan"}, {"loandrop_d": "Provider    22         Monthly Cost22", "loandrop_v": "2000", "loandrop_sel": "Commercial Loan"}, {"loandrop_d": "Provider     33        Monthly Cost33", "loandrop_v": "3000", "loandrop_sel": "Business Loan"}], "overlimit": [{"name": "Overdraft Limit", "limit": "1212", "balance": "1232", "provider": "213"}], "revcredit": [{"name": "Revolving Credit", "limit": "123", "balance": "111", "provider": "123"}], "storecard": [{"name": "Store Card", "limit": "5555", "balance": "4222", "provider": "aasd"}], "creditcard": [{"name": "Credit Card", "limit": "4444", "balance": "333", "provider": "2assad"}], "total_value": 14298, "hirepurchase": [{"name": "Hire Purchase", "balance": "500", "provider": "sdfsd", "monthly_cost": "500"}], "otherliability": [{"name": "Other Liabilities", "balance": "900", "description": "other onaaa"}, {"name": "", "balance": "1000", "description": "test others"}]}','[{"applicantid": "1", "monthincome": [{"monthincome_net": "800", "monthincome_gross": "1000", "monthincome_income": "Salary/wages", "monthincome_description": "First Job"}, {"monthincome_net": "1500", "monthincome_gross": "2000", "monthincome_income": "Second Job", "monthincome_description": "Technical Company"}, {"monthincome_net": "600", "monthincome_gross": "800", "monthincome_income": "Shareholder Salary", "monthincome_description": "What ever "}], "applicant_name": "Chao Li"}, {"applicantid": "2", "monthincome": [{"monthincome_net": "5000", "monthincome_gross": "40000", "monthincome_income": "Welfare Benefit", "monthincome_description": "stestwaset"}, {"monthincome_net": "7000", "monthincome_gross": "8000", "monthincome_income": "Shareholder Salary", "monthincome_description": "assfdff"}], "applicant_name": "Bo Chen"}]','[{"monthincomefr_net": "9000", "monthincomefr_income": "Rental Income", "monthincomefr_description": "36 Eastern Beach Rd."}, {"monthincomefr_net": "8000", "monthincomefr_income": "Boarder Income", "monthincomefr_description": "58 howick"}, {"monthincomefr_net": "6000", "monthincomefr_income": "Other", "monthincomefr_description": "2 melons bay Rd."}]','[{"monthexpend_expend": "New Mortgage Proposed", "monthexpend_expense": "1000", "monthexpend_description": "test1 expense"}, {"monthexpend_expend": "Existing Mortgage", "monthexpend_expense": "2000", "monthexpend_description": "test2 expense"}, {"monthexpend_expend": "Credit Card", "monthexpend_expense": "3000", "monthexpend_description": "aadsf"}, {"monthexpend_expend": "Personal Loan", "monthexpend_expense": "500", "monthexpend_description": "asfdasf"}]','["hehe", "haha"]',37900.00,6500.00,31400.00,111289.00,14298.00,96991.00,'/upload/20200206/sm_30_202002061024027423.pdf'),
	(36,NULL,'2020-02-04','Bin Luo','robinsonhood1@gmail.com','02108063825b2',NULL,0.00,'[]',NULL,NULL,NULL,NULL,NULL,NULL,'112212.12',0.00,0.00,'[]','{"payslip": {"url": "", "name": "Payslip"}, "appraisal": {"url": "", "name": "Appraisal"}, "ird_summary": {"url": "", "name": "IRD Summary"}, "bank_statement": {"url": "", "name": "Bank Statement"}, "gifting_letter": {"url": "", "name": "Gifting Letter"}, "loan_agreement": {"url": "", "name": "Loan agreement"}, "loan_statement": {"url": "", "name": "Loan Statement"}, "rate_lock_form": {"url": "", "name": "Rate Lock Form"}, "support_letter": {"url": "", "name": "Support Letter"}, "approval_letter": {"url": "", "name": "Approval Letter"}, "building_report": {"url": "", "name": "Building Report"}, "lease_agreement": {"url": "", "name": "Lease Agreement"}, "additional_files": {"url": "", "name": "Additional Files"}, "application_form": {"url": "", "name": "Application Form"}, "financial_report": {"url": "", "name": "Financial Report"}, "rental_agreement": {"url": "", "name": "Rental Agreement"}, "valuation_report": {"url": "", "name": "Valuation Report"}, "boarder_agreement": {"url": "", "name": "Boarder Agreement"}, "proof_of_identity": {"url": "", "name": "Proof of Identity"}, "sales_purchase_agreement": {"url": "", "name": "Sales Purchase Agreement"}}','2020-02-04 14:42:17',24,0,'{"cash": {"text": "Cash", "cash_d": "", "cash_v": ""}, "saving": [], "deposit": {"text": "Deposit on property (If Paid)", "deposit_d": "", "deposit_v": ""}, "vehicle": [], "property": [], "furniture": {"text": "Furniture", "furniture_d": "", "furniture_v": ""}, "kiwisaver": [], "investment": [], "otherasset": [], "businessown": [], "total_value": 0, "term_deposit": {"text": "Term Deposit", "term_deposit_d": "", "term_deposit_v": ""}, "other_currency_account": {"text": "Other Currency Account", "other_currency_account_d": "", "other_currency_account_v": ""}}','{"loandrop": [], "overlimit": [], "revcredit": [], "storecard": [], "creditcard": [], "total_value": 0, "hirepurchase": [], "otherliability": []}','[{"applicantid": "11", "monthincome": [{"monthincome_net": "", "monthincome_gross": "", "monthincome_income": "Salary/wages", "monthincome_description": ""}], "applicant_name": "Bin Luo"}]','[{"monthincomefr_net": "", "monthincomefr_income": "Rental Income", "monthincomefr_description": ""}]','[{"monthexpend_expend": "New Mortgage Proposed", "monthexpend_expense": "", "monthexpend_description": ""}]','[]',0.00,0.00,0.00,0.00,0.00,0.00,'/upload/20200204/sm_36_202002041442057198.pdf'),
	(37,NULL,'2020-02-04','Bin Luo','robinsonhood1@gmail.com','02108063825b2','ANZ',0.00,'[]',NULL,NULL,NULL,NULL,NULL,NULL,'0',0.00,0.00,'[]','{"payslip": {"url": "", "name": "Payslip"}, "appraisal": {"url": "", "name": "Appraisal"}, "ird_summary": {"url": "", "name": "IRD Summary"}, "bank_statement": {"url": "", "name": "Bank Statement"}, "gifting_letter": {"url": "", "name": "Gifting Letter"}, "loan_agreement": {"url": "", "name": "Loan agreement"}, "loan_statement": {"url": "", "name": "Loan Statement"}, "rate_lock_form": {"url": "", "name": "Rate Lock Form"}, "support_letter": {"url": "", "name": "Support Letter"}, "approval_letter": {"url": "", "name": "Approval Letter"}, "building_report": {"url": "", "name": "Building Report"}, "lease_agreement": {"url": "", "name": "Lease Agreement"}, "additional_files": {"url": "", "name": "Additional Files"}, "application_form": {"url": "", "name": "Application Form"}, "financial_report": {"url": "", "name": "Financial Report"}, "rental_agreement": {"url": "", "name": "Rental Agreement"}, "valuation_report": {"url": "", "name": "Valuation Report"}, "boarder_agreement": {"url": "", "name": "Boarder Agreement"}, "proof_of_identity": {"url": "", "name": "Proof of Identity"}, "sales_purchase_agreement": {"url": "", "name": "Sales Purchase Agreement"}}','2020-02-04 15:41:20',24,0,'{"cash": {"text": "Cash", "cash_d": "", "cash_v": 0}, "saving": [{"text": "Saving", "saving_d": "", "saving_v": ""}], "deposit": {"text": "Deposit on property (If Paid)", "deposit_d": "", "deposit_v": 0}, "vehicle": [{"text": "Motor Vehicle", "vehicle_d": "", "vehicle_v": ""}], "property": [{"text": "Property", "property_d": "", "property_v": ""}], "furniture": {"text": "Furniture", "furniture_d": "", "furniture_v": 0}, "kiwisaver": [{"text": "KiwiSaver", "kiwisaver_d": "", "kiwisaver_v": ""}], "investment": [{"text": "Investment", "investment_d": "", "investment_v": ""}], "otherasset": [{"text": "Other Assets (Specify)", "otherasset_d": "", "otherasset_v": ""}], "businessown": [{"text": "Business owned", "businessown_d": "", "businessown_v": ""}], "total_value": 0, "term_deposit": {"text": "Term Deposit", "term_deposit_d": "", "term_deposit_v": 0}, "other_currency_account": {"text": "Other Currency Account", "other_currency_account_d": "", "other_currency_account_v": 0}}','{"loandrop": [{"loandrop_d": "", "loandrop_v": "", "loandrop_sel": "Home Loan"}], "overlimit": [{"name": "Overdraft Limit", "limit": "", "balance": "", "provider": ""}], "revcredit": [{"name": "Revolving Credit", "limit": "", "balance": "", "provider": ""}], "storecard": [{"name": "Store Card", "limit": "", "balance": "", "provider": ""}], "creditcard": [{"name": "Credit Card", "limit": "", "balance": "", "provider": ""}], "total_value": 0, "hirepurchase": [{"name": "Hire Purchase", "balance": "", "provider": "", "monthly_cost": ""}], "otherliability": [{"name": "Other Liabilities", "balance": "", "description": ""}]}','[{"applicantid": "1", "monthincome": [{"monthincome_net": "", "monthincome_gross": "", "monthincome_income": "Salary/wages", "monthincome_description": ""}], "applicant_name": "Bin Luo"}, {"applicantid": "2", "monthincome": [{"monthincome_net": "", "monthincome_gross": "", "monthincome_income": "Salary/wages", "monthincome_description": ""}], "applicant_name": "Bo Chen"}]','[{"monthincomefr_net": "", "monthincomefr_income": "Rental Income", "monthincomefr_description": ""}]','[{"monthexpend_expend": "New Mortgage Proposed", "monthexpend_expense": "", "monthexpend_description": ""}]','[]',0.00,0.00,0.00,0.00,0.00,0.00,'/upload/20200208/sm_37_202002082152014605.pdf');

/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table application_client
# ------------------------------------------------------------

DROP TABLE IF EXISTS `application_client`;

CREATE TABLE `application_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `application_id` (`application_id`),
  KEY `client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `application_client` WRITE;
/*!40000 ALTER TABLE `application_client` DISABLE KEYS */;

INSERT INTO `application_client` (`id`, `application_id`, `client_id`)
VALUES
	(386,30,1),
	(387,30,2),
	(395,36,11),
	(422,37,1),
	(423,37,2);

/*!40000 ALTER TABLE `application_client` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '父目录ID',
  `name` varchar(100) DEFAULT NULL COMMENT '产品目录名称',
  `lft` int(11) NOT NULL DEFAULT '1' COMMENT '树左边',
  `rgt` int(11) NOT NULL DEFAULT '2' COMMENT '树右边',
  `depth` int(11) NOT NULL DEFAULT '0' COMMENT '目录级别',
  `priority` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  PRIMARY KEY (`id`),
  KEY `fk_category_parent` (`parent_id`),
  CONSTRAINT `fk_cat_parent` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品分类表';



# Dump of table channel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `channel`;

CREATE TABLE `channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '父栏目ID',
  `name` varchar(100) DEFAULT NULL COMMENT '栏目名称',
  `path` varchar(30) DEFAULT NULL COMMENT '访问路径',
  `lft` int(11) DEFAULT '1' COMMENT '树左边',
  `rgt` int(11) DEFAULT '2' COMMENT '树右边',
  `depth` int(11) DEFAULT '0' COMMENT '栏目级别',
  `priority` int(11) DEFAULT '10' COMMENT '排列顺序',
  `is_display` tinyint(1) DEFAULT '1' COMMENT '是否显示',
  `tpl_channel` varchar(100) DEFAULT NULL COMMENT '栏目页模板',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '内容页模板',
  `title_img` varchar(100) DEFAULT NULL COMMENT '缩略图',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图',
  `description` varchar(255) DEFAULT NULL COMMENT '摘要',
  `txt` longtext COMMENT '栏目内容',
  `is_recommend` tinyint(1) DEFAULT '1' COMMENT '是否推荐',
  `expert` varchar(255) DEFAULT NULL COMMENT '专家名',
  `position` varchar(1000) DEFAULT NULL COMMENT '专家职位',
  `introduction` varchar(1000) DEFAULT NULL COMMENT '专家介绍',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS栏目表';

LOCK TABLES `channel` WRITE;
/*!40000 ALTER TABLE `channel` DISABLE KEYS */;

INSERT INTO `channel` (`id`, `parent_id`, `name`, `path`, `lft`, `rgt`, `depth`, `priority`, `is_display`, `tpl_channel`, `tpl_content`, `title_img`, `content_img`, `description`, `txt`, `is_recommend`, `expert`, `position`, `introduction`)
VALUES
	(1,20,'博客','blogcn',12,13,1,1,1,'/channel/blog.html','/content/view.html',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(5,20,'资讯','infocn',14,15,1,1,1,'/channel/info.html','/content/info.html',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(6,20,'知识','knowcn',16,17,1,1,1,'/channel/know.html','/content/know.html',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(19,NULL,'English','en',1,10,0,1,1,'0','0',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(20,NULL,'中文','cn',11,18,0,1,1,'0','0',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(21,19,'News','news',6,7,1,1,1,'/channel/info.html','/content/info.html','/upload/201912/201912191600016.jpg',NULL,NULL,NULL,1,NULL,NULL,NULL),
	(22,19,'Notice','notice',8,9,1,1,1,'/channel/info.html','/content/view.html','/upload/201912/201912191605027.jpg',NULL,NULL,NULL,1,NULL,NULL,NULL),
	(23,19,'website','website',2,3,1,1,1,'/channel/info.html','/content/info.html',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL),
	(24,19,'know','know',4,5,1,1,1,'/channel/info.html','/content/info.html',NULL,NULL,NULL,NULL,1,NULL,NULL,NULL);

/*!40000 ALTER TABLE `channel` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table client
# ------------------------------------------------------------

DROP TABLE IF EXISTS `client`;

CREATE TABLE `client` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT '0' COMMENT '客户类型1-client,0-lead,2-join applicant',
  `title` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` tinyint(2) DEFAULT NULL COMMENT '性别1-male,0-female,2-not definition',
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `dependants_number` int(4) DEFAULT NULL,
  `dependants_age` json DEFAULT NULL,
  `residency_nz` tinyint(2) DEFAULT NULL COMMENT '0-None,1-Permanent Resident,2-New Zealand Citizenship',
  `marital_status` tinyint(2) DEFAULT NULL COMMENT '0-Married,1-Single,2-De Facto,3-Other',
  `current_residential_address` varchar(100) DEFAULT NULL,
  `postal_address` varchar(100) DEFAULT NULL,
  `time_stayed_years` int(4) DEFAULT NULL,
  `time_stayed_months` int(4) DEFAULT NULL,
  `previous_residential_address` json DEFAULT NULL,
  `p_time_stayed_years` int(4) DEFAULT NULL,
  `p_time_stayed_months` int(4) DEFAULT NULL,
  `current_living_situation` tinyint(2) DEFAULT NULL COMMENT '0-Renting,1-Living in own home,2-Boarding,3-Other',
  `prefered_name` varchar(20) DEFAULT NULL,
  `personal_email` json DEFAULT NULL,
  `work_email` varchar(50) DEFAULT NULL,
  `mobile` json DEFAULT NULL,
  `address` json DEFAULT NULL,
  `home_phone` varchar(20) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `work_phone` varchar(20) DEFAULT NULL,
  `wechat` varchar(30) DEFAULT NULL,
  `source_of_income` json DEFAULT NULL,
  `occupation` varchar(30) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `duration` varchar(20) DEFAULT NULL,
  `gross_income` varchar(20) DEFAULT NULL,
  `current_employment` json DEFAULT NULL,
  `previous_employment` json DEFAULT NULL,
  `property` json DEFAULT NULL,
  `loan` json DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `creator` int(11) NOT NULL COMMENT '录入人员',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '账户状态 0-正常，1-关闭',
  `avatar` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;

INSERT INTO `client` (`id`, `type`, `title`, `birthday`, `gender`, `first_name`, `last_name`, `dependants_number`, `dependants_age`, `residency_nz`, `marital_status`, `current_residential_address`, `postal_address`, `time_stayed_years`, `time_stayed_months`, `previous_residential_address`, `p_time_stayed_years`, `p_time_stayed_months`, `current_living_situation`, `prefered_name`, `personal_email`, `work_email`, `mobile`, `address`, `home_phone`, `comments`, `work_phone`, `wechat`, `source_of_income`, `occupation`, `company`, `duration`, `gross_income`, `current_employment`, `previous_employment`, `property`, `loan`, `description`, `create_time`, `creator`, `status`, `avatar`)
VALUES
	(1,2,'Ms.','1979-04-04',0,'Bin','Luo',1,'[]',1,1,'2 Mellons Bay Rd., Howick, Auckland','2 Mellons Bay Rd., Howick, Auckland',2,1,'[{"preaddress": "11", "p_time_stayed_years": "5", "p_time_stayed_months": "6"}, {"preaddress": "33", "p_time_stayed_years": "5777", "p_time_stayed_months": "6888"}]',5,6,3,'Carley','["robinsonhood1978@gmail.com", "robinsonhood1978@gmail.com"]','robinsonhood1978@gmail.com','["02108063825", "0210682957"]','[]','11212121221',NULL,'02108066666','xiaoming','["Salary/Wage"]','2','1','3','4','[{"curcompany": "1", "curduration": "3", "gross_income": "4", "curoccupation": "2"}, {"curcompany": "5", "curduration": "7", "gross_income": "8", "curoccupation": "6"}]','[]','[]','[]','Add from Refix Client on 13/01/2020','2020-01-13 05:51:34',24,0,NULL),
	(2,1,'Mr.',NULL,1,'Bo','Chen',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'["kyle86248825@hotmail.com"]',NULL,'[""]',NULL,NULL,'Bin Guo Referred',NULL,NULL,'["Salary/Wage"]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'[{"task_id": 38, "loan_bank": "ANZ", "loan_notes": "", "loan_amount": "528,366.01", "loan_account_no": "0229-0088472771-1001", "loan_customer_no": "71917235", "loan_review_date": "13/11/2020", "loan_interest_rate": "3.54%", "loan_reminder_days": "30", "loan_effective_date": "13/11/2019", "loan_now_rate_period": "12", "loan_borrowing_entity": "Bo Chen & Ziyan Liu"}]',NULL,'2020-01-13 05:58:31',24,0,NULL),
	(3,1,'Mr.','1989-11-20',1,'Xu','Cui',NULL,'[]',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,'["cuixu1120@hotmail.com"]',NULL,'["02102272137"]','["94 Rising Prade, Fairview Heights"]',NULL,NULL,NULL,NULL,'["Salary/Wage"]','Accountant','LJ Capital Management Ltd',NULL,'52000',NULL,'[]','[{"use": "O/O", "bank": "ANZ", "owner": "Xu CUI & Yurong HOU", "value": "1525000", "property": "94 Rising Prade, Fairview Heights", "property_type": "House"}]','[{"task_id": 39, "loan_bank": "ANZ", "loan_notes": "", "loan_amount": "895,301.82", "loan_account_no": "0229-0088569903-1001", "loan_customer_no": "71700757", "loan_review_date": "09/11/2020", "loan_interest_rate": "3.54%", "loan_reminder_days": "30", "loan_effective_date": "09/11/2019", "loan_now_rate_period": "12", "loan_borrowing_entity": "Xu CUI & Yurong HOU"}]',NULL,'2020-01-13 06:13:25',24,0,NULL),
	(4,1,'Ms.','1990-11-16',0,'Yurong','Hou',NULL,'[]',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,'["viviennehou1116@gmail.com"]',NULL,'["02108269931"]','["94 Rising Parade, Fairview Heights"]',NULL,'Xu CUI\'s wife',NULL,NULL,'["Salary/Wage"]','Registered Nurse','Oceania Healthcare',NULL,'63450',NULL,'[]','[{"use": "", "bank": "ANZ", "owner": "Xu CUI & Yurong HOU", "value": "", "property": "94 Rising Parade, Fairview Heights", "property_type": ""}]','[{"task_id": 40, "loan_bank": "ANZ", "loan_notes": "Contact her husband Xu CUI", "loan_amount": "895,301.82", "loan_account_no": "0229-0088569903-1001", "loan_customer_no": "71700757", "loan_review_date": "09/11/2020", "loan_interest_rate": "3.54%", "loan_reminder_days": "30", "loan_effective_date": "09/11/2019", "loan_now_rate_period": "12", "loan_borrowing_entity": "Xu CUI & Yurong HOU"}]',NULL,'2020-01-13 06:26:29',24,0,NULL),
	(5,1,'Mr.','1958-01-05',1,'Qianghan','Liang',NULL,'[]',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'[""]',NULL,'["0279552108"]','["33 Brunton Place, Glenfield"]',NULL,'Wenjuan Zhang\'s Husband',NULL,NULL,'["Salary/Wage"]','Builder','HL Foundation',NULL,'36400',NULL,'[]','[{"use": "O/O", "bank": "ANZ", "owner": "Liang, Qianghan & Zhang, Wenjuan", "value": "870000", "property": "33 Brunton Place, Glenfield", "property_type": "House"}]','[{"task_id": "41", "loan_bank": "ANZ", "loan_notes": "Contact his wife: Wenjuan Zhang", "loan_amount": "600,190.47", "loan_account_no": "0281-0088434820-1001", "loan_customer_no": "82673763", "loan_review_date": "27/05/2021", "loan_interest_rate": "3.39%", "loan_reminder_days": "30", "loan_effective_date": "27/11/2019", "loan_now_rate_period": "18", "loan_borrowing_entity": "Liang, Qianghan & Zhang, Wenjuan"}]',NULL,'2020-01-13 06:39:00',24,0,NULL),
	(6,1,'Ms.','1976-11-24',0,'Wenjuan','Zhang',NULL,'[]',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'Jean','[""]',NULL,'["0279016911"]','["33 Brunton Place, Glenfield"]',NULL,NULL,NULL,NULL,'["Salary/Wage"]','Waitress','Sunrise Kitchen',NULL,'41600',NULL,'[]','[{"use": "O/O", "bank": "ANZ", "owner": "Liang, Qianghan & Zhang, Wenjuan", "value": "870000", "property": "33 Brunton Place, Glenfield", "property_type": "House"}]','[{"task_id": 42, "loan_bank": "ANZ", "loan_notes": "", "loan_amount": "600,190.47", "loan_account_no": "0281-0088434820-1001", "loan_customer_no": "88434820", "loan_review_date": "27/05/2021", "loan_interest_rate": "3.39&", "loan_reminder_days": "30", "loan_effective_date": "27/11/2019", "loan_now_rate_period": "18", "loan_borrowing_entity": "Liang, Qianghan & Zhang, Wenjuan"}]',NULL,'2020-01-13 06:46:48',24,0,NULL),
	(7,1,'Miss','1988-01-23',0,'Lu','Liu',NULL,'[]',1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'Lulu','["liulu88123@hotmail.com"]',NULL,'["0277858580"]','["1/64 STREDWICK DRIVE TORBAY"]',NULL,NULL,NULL,NULL,'["Salary/Wage"]','Designer',NULL,NULL,NULL,NULL,'[]','[{"use": "O/O", "bank": "ANZ", "owner": "Lu Liu", "value": "740000", "property": "1/64 STREDWICK DRIVE TORBAY", "property_type": "House"}]','[{"task_id": "43", "loan_bank": "ANZ", "loan_notes": "", "loan_amount": "550,707", "loan_account_no": "0998-0088379613-1001", "loan_customer_no": "71206906", "loan_review_date": "25/05/2021", "loan_interest_rate": "3.39%", "loan_reminder_days": "30", "loan_effective_date": "25/11/2019", "loan_now_rate_period": "18", "loan_borrowing_entity": "Lu Liu"}]',NULL,'2020-01-13 06:53:50',24,0,NULL),
	(8,1,'Ms.','1978-09-16',0,'Ya','An',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'["anya2012@live.cn"]',NULL,'["0274305262"]',NULL,NULL,NULL,NULL,NULL,'["Salary/Wage"]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'[{"task_id": 44, "loan_bank": "ANZ", "loan_notes": "", "loan_amount": "", "loan_account_no": "", "loan_customer_no": "71626106", "loan_review_date": "01/05/2020", "loan_interest_rate": "", "loan_reminder_days": "30", "loan_effective_date": "", "loan_now_rate_period": "6", "loan_borrowing_entity": "Ya An"}]',NULL,'2020-01-21 23:12:55',24,0,NULL),
	(9,2,'Mr',NULL,1,'Bin2','Luo2',NULL,'[]',1,0,NULL,NULL,NULL,NULL,'[{"preaddress": "11", "p_time_stayed_years": "22", "p_time_stayed_months": "33"}, {"preaddress": "44", "p_time_stayed_years": "55", "p_time_stayed_months": "66"}]',NULL,NULL,0,NULL,'["robinsonhood1978@gmail.com"]','robinsonhood1978@gmail.com','["02108063825"]','[]',NULL,NULL,NULL,NULL,'[]',NULL,NULL,NULL,NULL,'[]','[]','[]','[]',NULL,'2020-02-01 00:59:07',24,0,NULL),
	(10,1,NULL,NULL,1,'Li','An',NULL,'[]',1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,'[""]',NULL,'[""]','[""]',NULL,NULL,NULL,NULL,'[]',NULL,NULL,NULL,NULL,'[]','[]','[]','[]',NULL,'2020-02-01 01:12:50',24,0,NULL);

/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL COMMENT '文章id',
  `txt` longtext COMMENT '内容',
  `uid` int(11) DEFAULT NULL COMMENT '发布人',
  `release_date` datetime DEFAULT NULL COMMENT '发布日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论内容表';

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;

INSERT INTO `comment` (`id`, `content_id`, `txt`, `uid`, `release_date`)
VALUES
	(4,6,'非常有社会责任感的企业！',9,'2016-01-06 09:05:27'),
	(5,39,'呵呵',16,'2019-09-06 20:17:26');

/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '公司名称',
  `nature` varchar(20) DEFAULT NULL COMMENT '公司性质',
  `service` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `product` varchar(50) DEFAULT NULL COMMENT '经营产品',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `linkman` varchar(20) DEFAULT NULL COMMENT '联系人',
  `gender` enum('2','1','0') NOT NULL DEFAULT '2' COMMENT '性别',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(30) DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(100) DEFAULT NULL COMMENT '公司地址',
  `zipcode` varchar(10) DEFAULT NULL COMMENT '邮编',
  `intro` varchar(200) DEFAULT NULL COMMENT '公司介绍',
  `create_actor` int(11) NOT NULL COMMENT '录入人员',
  `update_actor` int(11) DEFAULT NULL COMMENT '修改人员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '录入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生产厂商表';



# Dump of table config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '中文名称',
  `code` varchar(20) NOT NULL COMMENT '英文代码',
  `value` varchar(20) NOT NULL DEFAULT '' COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统设置表';

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;

INSERT INTO `config` (`id`, `name`, `code`, `value`)
VALUES
	(1,'文章审核','contentCheck','0'),
	(2,'产品审核','productCheck','1'),
	(3,'手续费率','serv_fee_rate','0.02');

/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) DEFAULT NULL COMMENT '栏目ID',
  `type_id` int(11) DEFAULT '1' COMMENT '属性ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `short_title` varchar(50) DEFAULT NULL COMMENT '简短标题',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `origin` varchar(100) DEFAULT NULL COMMENT '来源',
  `origin_url` varchar(255) DEFAULT NULL COMMENT '来源链接',
  `is_recommend` tinyint(2) DEFAULT '0' COMMENT '是否推荐',
  `status` tinyint(2) DEFAULT '2' COMMENT '状态(0:草稿;1:审核中;2:审核通过;3:未通过)',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `media_path` varchar(255) DEFAULT NULL COMMENT '媒体路径',
  `media_type` varchar(20) DEFAULT NULL COMMENT '媒体类型',
  `title_img` varchar(100) DEFAULT NULL COMMENT '标题图片',
  `content_img` varchar(100) DEFAULT NULL COMMENT '内容图片',
  `type_img` varchar(100) DEFAULT NULL COMMENT '类型图片',
  `link` varchar(255) DEFAULT NULL COMMENT '外部链接',
  `tpl_content` varchar(100) DEFAULT NULL COMMENT '指定模板',
  `txt` longtext COMMENT '内容',
  `creator` int(11) DEFAULT NULL COMMENT '发布人',
  `release_date` datetime DEFAULT NULL COMMENT '发布日期',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `number` varchar(100) DEFAULT NULL COMMENT '期刊数',
  `guest` varchar(50) DEFAULT NULL COMMENT '访谈嘉宾',
  `introduction` varchar(255) DEFAULT NULL COMMENT '嘉宾介绍',
  `custom_time` datetime DEFAULT NULL COMMENT '自定义的发布时间',
  `priority` tinyint(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息内容表';

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;

INSERT INTO `content` (`id`, `channel_id`, `type_id`, `title`, `short_title`, `author`, `origin`, `origin_url`, `is_recommend`, `status`, `description`, `media_path`, `media_type`, `title_img`, `content_img`, `type_img`, `link`, `tpl_content`, `txt`, `creator`, `release_date`, `update_time`, `number`, `guest`, `introduction`, `custom_time`, `priority`)
VALUES
	(41,21,1,'Privacy Policy',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'atest<img class=\"am-img-responsive\" src=\"data:image/jpeg;base64,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\">',24,'2019-12-18 17:05:46','2019-12-18 17:24:02',NULL,NULL,NULL,NULL,0),
	(42,21,1,'11',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'111',24,'2019-12-18 17:12:01','2019-12-18 17:24:02',NULL,NULL,NULL,NULL,0),
	(43,22,1,'22',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'22',24,'2019-12-18 17:15:10','2019-12-19 16:32:18',NULL,NULL,NULL,NULL,0),
	(44,21,1,'22',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'22',24,'2019-12-18 17:16:27','2019-12-18 17:24:02',NULL,NULL,NULL,NULL,0),
	(45,22,1,'22',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'22',24,'2019-12-18 17:17:02','2019-12-19 16:32:21',NULL,NULL,NULL,NULL,0),
	(46,21,1,'22',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'22',24,'2019-12-18 17:17:27','2019-12-18 17:24:02',NULL,NULL,NULL,NULL,0),
	(48,21,1,'3',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',1,'2019-12-18 18:19:30','2019-12-18 18:19:30',NULL,NULL,NULL,NULL,0),
	(49,22,1,'The 8 biggest companies using and accepting Bitcoin',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'look爱他',24,'2019-12-23 11:27:47','2019-12-23 11:27:47',NULL,NULL,NULL,NULL,0),
	(50,21,1,'Privacy Policy',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'111',24,'2019-12-23 23:38:50','2019-12-23 23:38:50',NULL,NULL,NULL,NULL,0),
	(52,21,1,'121312',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'312312',24,'2020-01-06 09:48:44','2020-01-06 09:48:44',NULL,NULL,NULL,NULL,0),
	(53,21,1,'111',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'111',24,'2020-01-06 19:51:37','2020-01-06 19:51:37',NULL,NULL,NULL,NULL,0),
	(54,21,1,'111',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2222',24,'2020-01-06 19:52:18','2020-01-06 19:52:18',NULL,NULL,NULL,NULL,0),
	(55,22,1,'hehe',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'haha',24,'2020-01-06 19:57:44','2020-01-06 19:57:44',NULL,NULL,NULL,NULL,0),
	(56,21,1,'hhhh',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaaa',24,'2020-01-06 20:00:12','2020-01-06 20:00:12',NULL,NULL,NULL,NULL,0),
	(57,21,1,'222',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'444',24,'2020-01-06 20:01:02','2020-01-06 20:01:02',NULL,NULL,NULL,NULL,0),
	(58,21,1,'11123122',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'werewr',24,'2020-01-06 20:05:40','2020-01-06 20:05:40',NULL,NULL,NULL,NULL,0),
	(59,21,1,'11',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'222',24,'2020-01-06 20:08:07','2020-01-06 20:08:07',NULL,NULL,NULL,NULL,0),
	(60,21,1,'121',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1212',24,'2020-01-06 20:09:03','2020-01-06 20:09:03',NULL,NULL,NULL,NULL,0),
	(61,21,1,'11',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'22',24,'2020-01-06 20:11:41','2020-01-06 20:11:41',NULL,NULL,NULL,NULL,0),
	(62,21,1,'222',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'222',24,'2020-01-06 20:29:36','2020-01-06 20:29:36',NULL,NULL,NULL,NULL,0),
	(63,21,1,'333',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'333',24,'2020-01-06 20:31:28','2020-01-06 20:31:28',NULL,NULL,NULL,NULL,0),
	(64,22,1,'Test',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Hello',24,'2020-01-06 21:06:24','2020-01-06 21:06:24',NULL,NULL,NULL,NULL,0),
	(65,21,1,'111',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'111',24,'2020-01-06 21:21:47','2020-01-06 21:21:47',NULL,NULL,NULL,NULL,0),
	(66,21,1,'111222',NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'<font face=\"Courier\">alasdfasdf</font>',24,'2020-01-20 15:07:21','2020-01-20 15:07:21',NULL,NULL,NULL,NULL,0);

/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_att
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_att`;

CREATE TABLE `content_att` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL COMMENT '信息ID',
  `name` varchar(100) NOT NULL COMMENT '附件名称',
  `url` varchar(200) NOT NULL COMMENT '附件存放路径',
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排列顺序',
  `picMsg` varchar(300) DEFAULT NULL COMMENT '附件描述(用于会客厅图片描述)',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `content_att_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息附件表';



# Dump of table content_channel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_channel`;

CREATE TABLE `content_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `channel_id` (`channel_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `content_channel_ibfk_1` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`),
  CONSTRAINT `content_channel_ibfk_2` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容栏目关联表';

LOCK TABLES `content_channel` WRITE;
/*!40000 ALTER TABLE `content_channel` DISABLE KEYS */;

INSERT INTO `content_channel` (`id`, `channel_id`, `content_id`)
VALUES
	(68,22,41),
	(69,22,42),
	(70,22,43),
	(71,22,44),
	(72,22,45),
	(73,22,46),
	(75,21,48),
	(76,22,49),
	(77,21,50),
	(79,21,52),
	(80,21,53),
	(81,21,54),
	(82,22,55),
	(83,21,56),
	(84,21,57),
	(85,21,58),
	(86,21,59),
	(87,21,60),
	(88,21,61),
	(89,21,62),
	(90,21,63),
	(91,22,64),
	(92,21,65),
	(93,21,66);

/*!40000 ALTER TABLE `content_channel` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_count
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_count`;

CREATE TABLE `content_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL COMMENT '信息ID',
  `totalClick` int(11) NOT NULL DEFAULT '0' COMMENT '总点击量',
  `dayClick` int(11) NOT NULL DEFAULT '0' COMMENT '周点击量',
  `weekClick` int(11) NOT NULL DEFAULT '0' COMMENT '周点击量',
  `monthClick` int(11) NOT NULL DEFAULT '0' COMMENT '月点击量',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `content_count_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息点击量表';

LOCK TABLES `content_count` WRITE;
/*!40000 ALTER TABLE `content_count` DISABLE KEYS */;

INSERT INTO `content_count` (`id`, `content_id`, `totalClick`, `dayClick`, `weekClick`, `monthClick`)
VALUES
	(36,41,0,0,0,0),
	(37,42,0,0,0,0),
	(38,43,0,0,0,0),
	(39,44,0,0,0,0),
	(40,45,0,0,0,0),
	(41,46,0,0,0,0),
	(43,48,0,0,0,0),
	(44,49,0,0,0,0),
	(45,50,0,0,0,0),
	(47,52,0,0,0,0),
	(48,53,0,0,0,0),
	(49,54,0,0,0,0),
	(50,55,0,0,0,0),
	(51,56,0,0,0,0),
	(52,57,0,0,0,0),
	(53,58,0,0,0,0),
	(54,59,0,0,0,0),
	(55,60,0,0,0,0),
	(56,61,0,0,0,0),
	(57,62,0,0,0,0),
	(58,63,0,0,0,0),
	(59,64,0,0,0,0),
	(60,65,0,0,0,0),
	(61,66,0,0,0,0);

/*!40000 ALTER TABLE `content_count` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_keywords
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_keywords`;

CREATE TABLE `content_keywords` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL,
  `keywords_id` int(11) NOT NULL,
  `weight` int(11) NOT NULL COMMENT '关键字权重',
  PRIMARY KEY (`id`),
  KEY `keywords_id` (`keywords_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `content_keywords_ibfk_1` FOREIGN KEY (`keywords_id`) REFERENCES `keywords` (`id`),
  CONSTRAINT `content_keywords_ibfk_2` FOREIGN KEY (`content_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容和关键字关联表';



# Dump of table content_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_type`;

CREATE TABLE `content_type` (
  `type_id` int(11) NOT NULL,
  `type_name` varchar(20) NOT NULL COMMENT '名称',
  `img_width` int(11) DEFAULT NULL COMMENT '图片宽',
  `img_height` int(11) DEFAULT NULL COMMENT '图片高',
  `has_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有图片',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS内容类型表';

LOCK TABLES `content_type` WRITE;
/*!40000 ALTER TABLE `content_type` DISABLE KEYS */;

INSERT INTO `content_type` (`type_id`, `type_name`, `img_width`, `img_height`, `has_image`, `is_disabled`)
VALUES
	(1,'普通',100,100,0,0),
	(2,'图文',143,98,1,0),
	(3,'焦点',280,200,1,0),
	(4,'头条',0,0,0,0);

/*!40000 ALTER TABLE `content_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table content_view
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content_view`;

CREATE TABLE `content_view` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) NOT NULL COMMENT '信息ID',
  `user_id` int(11) NOT NULL,
  `visit_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息访问表';

LOCK TABLES `content_view` WRITE;
/*!40000 ALTER TABLE `content_view` DISABLE KEYS */;

INSERT INTO `content_view` (`id`, `content_id`, `user_id`, `visit_time`)
VALUES
	(2,41,1,'2019-12-19 18:33:32'),
	(4,41,25,'2019-12-19 19:07:07'),
	(5,41,26,'2019-12-19 19:36:15'),
	(6,41,24,'2019-12-19 22:47:01'),
	(7,42,24,'2019-12-19 22:50:51'),
	(8,48,24,'2019-12-20 11:30:28'),
	(9,49,24,'2019-12-23 11:28:15'),
	(10,50,24,'2020-01-03 08:03:30'),
	(11,64,24,'2020-01-06 21:06:35');

/*!40000 ALTER TABLE `content_view` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table currency_code
# ------------------------------------------------------------

DROP TABLE IF EXISTS `currency_code`;

CREATE TABLE `currency_code` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(30) DEFAULT NULL COMMENT '货币代码',
  `is_show` tinyint(2) DEFAULT '0' COMMENT '是否显示,1-显示,0-不显示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货币代码表';

LOCK TABLES `currency_code` WRITE;
/*!40000 ALTER TABLE `currency_code` DISABLE KEYS */;

INSERT INTO `currency_code` (`id`, `name`, `code`, `is_show`)
VALUES
	(3,'阿联酋迪拉姆（United Arab Emirates Dirham）','AED',0),
	(4,'阿富汗尼（Afghan Afghani）','AFN',0),
	(5,'阿尔巴尼列克（Albania Lek）','ALL',0),
	(6,'亚美尼亚德拉姆（Armenia Dram）','AMD',0),
	(7,'荷兰盾（Dutch Guilder）','ANG',0),
	(8,'安哥拉宽扎（Angola Kwanza）','AOA',0),
	(9,'阿根廷比索（Argentina Peso）','ARS',0),
	(10,'澳元（Australia Dollar）','AUD',1),
	(11,'阿鲁巴弗罗林（Aruba Florin）','AWG',0),
	(12,'阿塞拜疆马纳特（Azerbaijan Manat）','AZN',0),
	(13,'波黑可兑换马克（Bosnia Convertible Mark）','BAM',0),
	(14,'巴巴多斯元（Barbados Dollar）','BBD',0),
	(15,'孟加拉国塔卡（Bangladesh Taka）','BDT',0),
	(16,'保加利亚列弗（Bulgaria Lev）','BGN',0),
	(17,'巴林第纳尔（Bahrain Dinar）','BHD',0),
	(18,'布隆迪法郎（Burundi Franc）','BIF',0),
	(19,'百慕大元（Bermudian Dollar）','BMD',0),
	(20,'文莱元（Brunei Dollar）','BND',0),
	(21,'玻利维亚诺（Bolivian Boliviano）','BOB',0),
	(22,'巴西雷亚尔（Brazilian Real）','BRL',0),
	(23,'巴哈马元（Bahamian Dollar）','BSD',0),
	(24,'比特币（BitCoin）','BTC',1),
	(25,'不丹努扎姆（Bhutanese Ngultrum）','BTN',0),
	(26,'博茨瓦纳普拉（Botswana Pula ）','BWP',0),
	(27,'白俄罗斯卢布（Belarusian Ruble）','BYR',0),
	(28,'伯利兹元（Belize Dollar）','BZD',0),
	(29,'加元（Canadian Dollar）','CAD',1),
	(30,'刚果法郎（Congolese Franc）','CDF',0),
	(31,'瑞士法郎（Swiss Franc）','CHF',0),
	(32,'智利比索(基金)（Chilean Unidad de Fomento）','CLF',0),
	(33,'智利比索（Chilean Peso）','CLP',0),
	(34,'中国离岸人民币（Chinese Offshore Renminbi）','CNH',0),
	(35,'人民币（Chinese Yuan）','CNY',1),
	(36,'哥伦比亚比索（Colombia Peso ）','COP',0),
	(37,'哥斯达黎加科朗（Costa Rica Colon）','CRC',0),
	(38,'古巴比索（Cuban Peso）','CUP',0),
	(39,'佛得角埃斯库多（Cape Verde Escudo）','CVE',0),
	(40,'塞普路斯镑（Cyprus Pound）','CYP',0),
	(41,'捷克克朗（Czech Republic Koruna）','CZK',0),
	(42,'德国马克（Deutsche Mark）','DEM',0),
	(43,'吉布提法郎（Djiboutian Franc）','DJF',0),
	(44,'丹麦克朗（Danish Krone）','DKK',0),
	(45,'多米尼加比索（Dominican Peso）','DOP',0),
	(46,'阿尔及利亚第纳尔（Algerian Dinar ）','DZD',0),
	(47,'厄瓜多尔苏克雷（Ecuadorian Sucre）','ECS',0),
	(48,'埃及镑（Egyptian Pound）','EGP',0),
	(49,'厄立特里亚纳克法（Eritrean Nakfa ）','ERN',0),
	(50,'埃塞俄比亚比尔（Ethiopian Birr）','ETB',0),
	(51,'欧元（Euro）','EUR',1),
	(52,'斐济元（Fiji Dollar）','FJD',0),
	(53,'福克兰群岛镑（Falkland Islands Pound）','FKP',0),
	(54,'法国法郎（French Franc）','FRF',0),
	(55,'英镑（British Pound）','GBP',1),
	(56,'格鲁吉亚拉里（Georgian Lari）','GEL',0),
	(57,'加纳塞地（Ghanaian Cedi）','GHS',0),
	(58,'直布罗陀镑（Gibraltar Pound）','GIP',0),
	(59,'冈比亚达拉西（Gambian Dalasi）','GMD',0),
	(60,'几内亚法郎（Guinean Franc）','GNF',0),
	(61,'危地马拉格查尔（Guatemalan Quetzal）','GTQ',0),
	(62,'圭亚那元（Guyanese Dollar）','GYD',0),
	(63,'港币（Hong Kong Dollar）','HKD',1),
	(64,'洪都拉斯伦皮拉（Honduran Lempira）','HNL',0),
	(65,'克罗地亚库纳（Croatian Kuna）','HRK',0),
	(66,'海地古德（Haitian Gourde）','HTG',0),
	(67,'匈牙利福林（Hungarian Forint）','HUF',0),
	(68,'印度尼西亚卢比（Indonesian Rupiah）','IDR',0),
	(69,'爱尔兰镑（Irish Pound）','IEP',0),
	(70,'以色列新谢克尔（Israeli New Shekel）','ILS',0),
	(71,'印度卢比（Indian Rupee）','INR',0),
	(72,'伊拉克第纳尔（Iraqi Dinar）','IQD',0),
	(73,'伊朗里亚尔（Iranian Rial）','IRR',0),
	(74,'冰岛克郎（Icelandic Krona）','ISK',0),
	(75,'意大利里拉（Italian Lira）','ITL',0),
	(76,'牙买加元（Jamaican Dollar）','JMD',0),
	(77,'约旦第纳尔（Jordanian Dinar）','JOD',0),
	(78,'日元（Japanese Yen）','JPY',0),
	(79,'肯尼亚先令（Kenyan Shilling）','KES',0),
	(80,'吉尔吉斯斯坦索姆（Kyrgyzstani Som）','KGS',0),
	(81,'柬埔寨瑞尔（Cambodian Riel）','KHR',0),
	(82,'科摩罗法郎（Comorian franc）','KMF',0),
	(83,'朝鲜元（North Korean Won）','KPW',0),
	(84,'韩元（South Korean Won）','KRW',0),
	(85,'科威特第纳尔（Kuwaiti Dinar）','KWD',0),
	(86,'开曼群岛元（Cayman Islands Dollar）','KYD',0),
	(87,'哈萨克斯坦坚戈（Kazakstani Tenge）','KZT',0),
	(88,'老挝基普（Lao kip）','LAK',0),
	(89,'黎巴嫩镑（Lebanese Pound）','LBP',0),
	(90,'斯里兰卡卢比（Sri Lankan Rupee）','LKR',0),
	(91,'利比里亚元（Liberian dollar）','LRD',0),
	(92,'莱索托洛蒂（Lesotho Loti）','LSL',0),
	(93,'立陶宛立特（Lithuanian Litas）','LTL',0),
	(94,'拉脱维亚拉特（Latvian Lats）','LVL',0),
	(95,'利比亚第纳尔（Libyan Dinar）','LYD',0),
	(96,'摩洛哥迪拉姆（Moroccan Dirham）','MAD',0),
	(97,'摩尔多瓦列伊（Moldovan Leu）','MDL',0),
	(98,'马达加斯加阿里亚里（Malagasy Ariary）','MGA',0),
	(99,'马其顿代纳尔（Macedonian Denar）','MKD',0),
	(100,'缅甸元（Myanmar Kyat）','MMK',0),
	(101,'蒙古图格里克（Mongolian Tugrik）','MNT',0),
	(102,'澳门元（Macau Pataca）','MOP',0),
	(103,'毛里塔尼亚乌吉亚（Mauritania Ouguiya）','MRO',0),
	(104,'毛里求斯卢比（Mauritian Rupee）','MUR',0),
	(105,'马尔代夫拉菲亚（Maldives Rufiyaa）','MVR',0),
	(106,'马拉维克瓦查（Malawian Kwacha）','MWK',0),
	(107,'墨西哥比索（Mexican Peso）','MXN',0),
	(108,'墨西哥(资金)（Mexican Unidad De Inversion）','MXV',0),
	(109,'林吉特（Malaysian Ringgit）','MYR',0),
	(110,'莫桑比克新梅蒂卡尔（New Mozambican Metical）','MZN',0),
	(111,'纳米比亚元（Namibian Dollar）','NAD',0),
	(112,'尼日利亚奈拉（Nigerian Naira）','NGN',0),
	(113,'尼加拉瓜新科多巴（Nicaraguan Cordoba Oro）','NIO',0),
	(114,'挪威克朗（Norwegian Krone）','NOK',0),
	(115,'尼泊尔卢比（Nepalese Rupee）','NPR',0),
	(116,'新西兰元（New Zealand Dollar）','NZD',1),
	(117,'阿曼里亚尔（Omani Rial）','OMR',0),
	(118,'巴拿马巴波亚（Panamanian Balboa）','PAB',0),
	(119,'秘鲁新索尔（Peruvian Nuevo Sol）','PEN',0),
	(120,'巴布亚新几内亚基那（Papua New Guinea Kina）','PGK',0),
	(121,'菲律宾比索（Philippine Peso）','PHP',0),
	(122,'巴基斯坦卢比（Pakistan Rupee）','PKR',0),
	(123,'波兰兹罗提（Polish Zloty）','PLN',0),
	(124,'巴拉圭瓜拉尼（Paraguayan Guarani）','PYG',0),
	(125,'卡塔尔里亚尔（Qatari Riyal）','QAR',0),
	(126,'罗马尼亚列伊（Romanian Leu）','RON',0),
	(127,'塞尔维亚第纳尔（Serbian Dinar）','RSD',0),
	(128,'俄罗斯卢布（Russian Ruble）','RUB',0),
	(129,'卢旺达法郎（Rwandan Franc）','RWF',0),
	(130,'沙特里亚尔（Saudi Arabian Riyal）','SAR',0),
	(131,'所罗门群岛元（Solomon Islands Dollar）','SBD',0),
	(132,'塞舌尔卢比（Seychelles Rupee）','SCR',0),
	(133,'苏丹磅（Sudanese Pound）','SDG',0),
	(134,'瑞典克朗（Swedish Krona）','SEK',0),
	(135,'新加坡元（Singapore Dollar）','SGD',0),
	(136,'圣赫勒拿镑（Saint Helena Pound）','SHP',0),
	(137,'斯洛文尼亚托拉尔（Slovenian Tolar）','SIT',0),
	(138,'塞拉利昂利昂（Sierra Leonean Leone）','SLL',0),
	(139,'索马里先令（Somali Shilling）','SOS',0),
	(140,'苏里南元（Suriname Dollar）','SRD',0),
	(141,'圣多美多布拉（Sao Tome Dobra）','STD',0),
	(142,'萨尔瓦多科朗（Salvadoran Colon）','SVC',0),
	(143,'叙利亚镑（Syrian Pound）','SYP',0),
	(144,'斯威士兰里兰吉尼（Swazi Lilangeni）','SZL',0),
	(145,'泰铢（Thai Baht）','THB',0),
	(146,'塔吉克斯坦索莫尼（Tajikistan Somoni）','TJS',0),
	(147,'土库曼斯坦马纳特（Turkmenistan Manat）','TMT',0),
	(148,'突尼斯第纳尔（Tunisian Dinar）','TND',0),
	(149,'汤加潘加（Tongan Pa Anga）','TOP',0),
	(150,'土耳其里拉（Turkish Lira）','TRY',0),
	(151,'特立尼达多巴哥元（Trinidad and Tobago Dollar）','TTD',0),
	(152,'新台币（New Taiwan Dollar）','TWD',0),
	(153,'坦桑尼亚先令（Tanzanian Shilling）','TZS',0),
	(154,'乌克兰格里夫纳（Ukrainian Hryvnia）','UAH',0),
	(155,'乌干达先令（Ugandan Shilling）','UGX',0),
	(156,'美元（United States Dollar）','USD',1),
	(157,'乌拉圭比索（Uruguayan Peso）','UYU',0),
	(158,'乌兹别克斯坦苏姆（Uzbekistani Som）','UZS',0),
	(159,'委内瑞拉玻利瓦尔（Venezuelan Bolivar Fuerte）','VEF',0),
	(160,'越南盾（Viet Nam Dong）','VND',0),
	(161,'瓦努阿图瓦图（Vanuatu Vatu）','VUV',0),
	(162,'萨摩亚塔拉（Samoa Tala）','WST',0),
	(163,'中非法郎（Central African CFA Franc）','XAF',0),
	(164,'银价盎司（Ounces of Silver）','XAG',0),
	(165,'金价盎司（Ounces of Gold）','XAU',0),
	(166,'东加勒比元（East Caribbean Dollar）','XCD',0),
	(167,'铜价盎司（Ounces of Copper）','XCP',0),
	(168,'IMF特别提款权（IMF Special Drawing Rights）','XDR',0),
	(169,'西非法郎（West African CFA）','XOF',0),
	(170,'钯价盎司（Ounces of Palladium）','XPD',0),
	(171,'太平洋法郎（French Pacific Franc）','XPF',0),
	(172,'珀价盎司（Ounces of Platinum）','XPT',0),
	(173,'也门里亚尔（Yemeni Rial）','YER',0),
	(174,'南非兰特（South African Rand）','ZAR',0),
	(175,'赞比亚克瓦查（Zambian Kwacha）','ZMW',0),
	(176,'津巴布韦元（Zimbabwean Dollar）','ZWL',0);

/*!40000 ALTER TABLE `currency_code` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dict
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dict`;

CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '字典名称',
  `ename` varchar(20) NOT NULL COMMENT '字典值，可能为数字或英文',
  `code` varchar(20) NOT NULL COMMENT '英文编码',
  `fname` varchar(20) NOT NULL COMMENT '类别名称:对应英文编码,同一组字典类别名称和英文编码一致',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典表';

LOCK TABLES `dict` WRITE;
/*!40000 ALTER TABLE `dict` DISABLE KEYS */;

INSERT INTO `dict` (`id`, `name`, `ename`, `code`, `fname`)
VALUES
	(1,'现金','Cash','pay_method','支付方式'),
	(2,'银行卡','Bank Card','pay_method','支付方式'),
	(3,'Visa信用卡','Visa Card','pay_method','支付方式'),
	(4,'微信','Wechat','pay_method','支付方式'),
	(5,'支付宝','Alipay','pay_method','支付方式'),
	(6,'Master卡','Master Card','pay_method','支付方式');

/*!40000 ALTER TABLE `dict` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table dict_base
# ------------------------------------------------------------

DROP TABLE IF EXISTS `dict_base`;

CREATE TABLE `dict_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '中文名称',
  `code` varchar(20) NOT NULL COMMENT '英文编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础字典表';

LOCK TABLES `dict_base` WRITE;
/*!40000 ALTER TABLE `dict_base` DISABLE KEYS */;

INSERT INTO `dict_base` (`id`, `name`, `code`)
VALUES
	(1,'支付方式','pay_method');

/*!40000 ALTER TABLE `dict_base` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table email
# ------------------------------------------------------------

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject` varchar(255) DEFAULT '' COMMENT '标题',
  `content` longtext COMMENT '内容',
  `attachment` json DEFAULT NULL COMMENT '附件',
  `sender` varchar(30) NOT NULL COMMENT '发信息人',
  `receiver` varchar(30) NOT NULL COMMENT '接收人',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '发送状态 0未发送，1已发送',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `creator` int(11) DEFAULT NULL COMMENT '录入人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;

INSERT INTO `email` (`id`, `subject`, `content`, `attachment`, `sender`, `receiver`, `status`, `create_time`, `creator`)
VALUES
	(1,'Welcome!','Dear Robin,<div><br></div><div>welcome to my house on Saturday night!</div>',NULL,'robin@taijicoin.nz','robinsonhood1978@gmail.com',0,'2020-01-02 22:43:58',24),
	(2,'Welcome!','Test',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',0,'2020-01-02 22:47:09',24),
	(3,'1','2',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-02 22:51:48',24),
	(4,'Be good luck!','<font face=\"Arial\" color=\"#f83a22\"><b>Will you enjoy it ?</b></font>',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-03 05:54:04',24),
	(5,'111','<font face=\"Arial\" color=\"#f83a22\"><b>Will you enjoy it ?</b></font>',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-03 10:54:31',26),
	(6,'Be good luck!','<font face=\"Arial\" color=\"#f83a22\"><b>Will you enjoy it ?</b></font>',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-03 11:18:39',24),
	(7,'Hello','<font face=\"Arial\" color=\"#f83a22\"><b>Will you enjoy it ?</b></font>',NULL,'robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-03 11:18:59',24),
	(8,'Be good luck!','Important','[{"att_file": "/upload/20200130/sm_30_202001301418023279.pdf", "att_name": "Application Form"}]','robin@taijicoin.nz','robinsonhood1978@gmail.com',1,'2020-01-30 17:38:30',24),
	(9,'Be good luck!','hehe','[{"att_file": "/upload/20200130/sm_30_202001301418023279.pdf", "att_name": "Application Form"}]','robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-30 17:40:37',24),
	(10,'Important File','good job record','[{"att_file": "/upload/20200130/sm_30_202001301418023279.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robinsonhood1978@gmail.com',1,'2020-01-30 17:57:59',24),
	(11,NULL,'11','[{"att_file": "/upload/202001/202001301839026036454.pdf", "att_name": "approval_letter"}, {"att_file": "/upload/202001/202001301839003360789.pdf", "att_name": "application_form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-30 18:48:26',24),
	(12,'New Files','Please reserve these files','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robinsonhood1978@gmail.com',1,'2020-01-30 20:16:59',24),
	(13,'Fine Files','many files','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robinsonhood1978@gmail.com',1,'2020-01-30 20:33:27',24),
	(14,'Welcome!','呵呵haha','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-31 00:06:47',24),
	(15,'Important File','testing','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','fyncotester@outlook.com','robin@taijicoin.nz',1,'2020-01-31 00:15:41',24),
	(16,'testing','good','[{"att_file": "/upload/20200130/sm_30_202001301418023279.pdf", "att_name": "Application Form"}]','fyncotester@outlook.com','robin@taijicoin.nz',1,'2020-01-31 00:33:11',24),
	(17,'11','11','[{"att_file": "/upload/20200131/sm_30_202001310048013712.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-31 17:33:04',24),
	(18,'Be good luck!','111','[{"att_file": "/upload/20200131/sm_30_202001310048013712.pdf", "att_name": "Application Form"}]','robin@taijicoin.nz','robin@taijicoin.nz',1,'2020-01-31 17:35:29',24),
	(19,'Be good luck!','嗷嗷','[{"att_file": "/upload/20200131/sm_30_202001310048013712.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-31 18:47:10',24),
	(20,'Welcome!','111','[{"att_file": "/upload/20200131/sm_30_202001311849044820.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-31 18:50:46',24),
	(21,'haha','hehe','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','robinsonhood15@gmail.com','robin@taijicoin.nz',1,'2020-01-31 18:53:35',24),
	(22,'Important File','hehe','[{"att_file": "/upload/202001/202001301920004679313.pdf", "att_name": "Additional Files"}, {"att_file": "/upload/202001/202001301920035978691.pdf", "att_name": "Application Form"}]','fyncotester@outlook.com','robin@taijicoin.nz',1,'2020-01-31 19:15:54',24);

/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table friendlink
# ------------------------------------------------------------

DROP TABLE IF EXISTS `friendlink`;

CREATE TABLE `friendlink` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `friendlinkctg_id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL COMMENT '网站名称',
  `url` varchar(255) NOT NULL COMMENT '网站地址',
  `logo` varchar(150) DEFAULT NULL COMMENT '网站logo',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否显示',
  PRIMARY KEY (`id`),
  KEY `fk_jc_ctg_friendlink` (`friendlinkctg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS友情链接';



# Dump of table friendlinkctg
# ------------------------------------------------------------

DROP TABLE IF EXISTS `friendlinkctg`;

CREATE TABLE `friendlinkctg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CMS友情链接类别';

LOCK TABLES `friendlinkctg` WRITE;
/*!40000 ALTER TABLE `friendlinkctg` DISABLE KEYS */;

INSERT INTO `friendlinkctg` (`id`, `name`)
VALUES
	(4,'文字链接'),
	(5,'品牌专区（图片链接）');

/*!40000 ALTER TABLE `friendlinkctg` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table general_att
# ------------------------------------------------------------

DROP TABLE IF EXISTS `general_att`;

CREATE TABLE `general_att` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(20) NOT NULL COMMENT '对应基础表名',
  `column_name` varchar(20) NOT NULL COMMENT '对应字段名',
  `primary_id` int(11) NOT NULL COMMENT '基础表中对应的ID',
  `name` varchar(100) NOT NULL COMMENT '附件名称',
  `url` varchar(200) NOT NULL COMMENT '附件存放路径',
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排列顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通用附件表';

LOCK TABLES `general_att` WRITE;
/*!40000 ALTER TABLE `general_att` DISABLE KEYS */;

INSERT INTO `general_att` (`id`, `table_name`, `column_name`, `primary_id`, `name`, `url`, `priority`)
VALUES
	(16,'trade','final_proof',23,'201909191310024833412.gif','/upload/201909/201909191310024833412.gif',1),
	(17,'trade','final_proof',23,'201909191310024857376.jpg','/upload/201909/201909191310024857376.jpg',2),
	(18,'trade','final_proof',23,'201909191310024875720.jpg','/upload/201909/201909191310024875720.jpg',3),
	(19,'trade','final_proof',23,'201909191625030248985.jpg','/upload/201909/201909191625030248985.jpg',4),
	(20,'trade','final_proof',23,'201909191625030253894.jpg','/upload/201909/201909191625030253894.jpg',5),
	(21,'trade','transfer_screenshots',23,'201909191310040709927.jpg','/upload/201909/201909191310040709927.jpg',1),
	(22,'trade','transfer_screenshots',23,'201909191310040729137.jpg','/upload/201909/201909191310040729137.jpg',2),
	(23,'trade','transfer_screenshots',23,'201909191310040743689.jpg','/upload/201909/201909191310040743689.jpg',3),
	(24,'trade','transfer_screenshots',23,'201909191625043359497.png','/upload/201909/201909191625043359497.png',4),
	(25,'trade','transfer_screenshots',23,'201909191625043371895.png','/upload/201909/201909191625043371895.png',5),
	(30,'trade','final_proof',24,'201909191630021090374.gif','/upload/201909/201909191630021090374.gif',1),
	(31,'trade','final_proof',24,'201909191630021114335.jpg','/upload/201909/201909191630021114335.jpg',2),
	(32,'trade','transfer_screenshots',24,'201909191630028206822.jpg','/upload/201909/201909191630028206822.jpg',1),
	(33,'trade','transfer_screenshots',24,'201909191630028226696.jpg','/upload/201909/201909191630028226696.jpg',2),
	(34,'trade','transfer_screenshots',24,'201909191649043163576.jpg','/upload/201909/201909191649043163576.jpg',3),
	(35,'trade','transfer_screenshots',24,'201909191649043187290.jpg','/upload/201909/201909191649043187290.jpg',4),
	(36,'trade','transfer_screenshots',24,'201909191649043206389.jpg','/upload/201909/201909191649043206389.jpg',5),
	(37,'trade','transfer_screenshots',24,'201909191649043226378.jpg','/upload/201909/201909191649043226378.jpg',6),
	(38,'trade','transfer_screenshots',24,'201909191649043247918.jpg','/upload/201909/201909191649043247918.jpg',7);

/*!40000 ALTER TABLE `general_att` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table goods
# ------------------------------------------------------------

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `cat_id` int(11) NOT NULL COMMENT '目录ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(30) DEFAULT NULL COMMENT '编号',
  `spec` varchar(100) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `color` varchar(10) DEFAULT NULL COMMENT '颜色',
  `intro` varchar(200) DEFAULT NULL COMMENT '备注',
  `origin` varchar(30) DEFAULT NULL COMMENT '产地',
  `company_id` int(11) NOT NULL DEFAULT '0' COMMENT '生产厂商',
  `create_actor` int(11) NOT NULL COMMENT '录入人员',
  `update_actor` int(11) DEFAULT NULL COMMENT '修改人员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `update_time` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `fk_goods_catid` (`cat_id`),
  KEY `fk_goods_companyid` (`company_id`),
  CONSTRAINT `fk_goods_catid` FOREIGN KEY (`cat_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_goods_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';



# Dump of table goods_att
# ------------------------------------------------------------

DROP TABLE IF EXISTS `goods_att`;

CREATE TABLE `goods_att` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `good_id` int(12) unsigned NOT NULL COMMENT '产品ID',
  `thumbnailname` varchar(100) NOT NULL COMMENT '附件名称',
  `thumbnailurl` varchar(200) NOT NULL COMMENT '附件存放路径',
  `name` varchar(100) NOT NULL,
  `url` varchar(200) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '排列顺序',
  PRIMARY KEY (`id`),
  KEY `good_id` (`good_id`),
  CONSTRAINT `goods_att_ibfk_1` FOREIGN KEY (`good_id`) REFERENCES `goods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品附件表';



# Dump of table keywords
# ------------------------------------------------------------

DROP TABLE IF EXISTS `keywords`;

CREATE TABLE `keywords` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(50) NOT NULL COMMENT '文章关键字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关键字表';

LOCK TABLES `keywords` WRITE;
/*!40000 ALTER TABLE `keywords` DISABLE KEYS */;

INSERT INTO `keywords` (`id`, `word`)
VALUES
	(1,'邮币卡'),
	(2,'电子盘');

/*!40000 ALTER TABLE `keywords` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table mailserver
# ------------------------------------------------------------

DROP TABLE IF EXISTS `mailserver`;

CREATE TABLE `mailserver` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL DEFAULT '',
  `smtp_domain` varchar(30) NOT NULL DEFAULT '',
  `imap_domain` varchar(30) NOT NULL DEFAULT '',
  `send_port` varchar(10) NOT NULL,
  `imap_port` varchar(10) NOT NULL,
  `smtp_ssl_enable` tinyint(2) NOT NULL DEFAULT '0',
  `smtp_starttls_enable` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `mailserver` WRITE;
/*!40000 ALTER TABLE `mailserver` DISABLE KEYS */;

INSERT INTO `mailserver` (`id`, `name`, `smtp_domain`, `imap_domain`, `send_port`, `imap_port`, `smtp_ssl_enable`, `smtp_starttls_enable`)
VALUES
	(1,'gmail','smtp.gmail.com','imap.gmail.com','465','993',1,0),
	(2,'office365','smtp.office365.com','outlook.office365.com','587','993',0,1),
	(3,'outlook','eas.outlook.com','smtp-mail.outlook.com','587','993',0,1),
	(4,'taijicoin','mail.taijicoin.nz','mail.taijicoin.nz','465','993',1,0);

/*!40000 ALTER TABLE `mailserver` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `name` varchar(255) DEFAULT '' COMMENT '标题',
  `content` longtext COMMENT '站内信息内容',
  `create_time` datetime NOT NULL COMMENT '发送时间',
  `sender` int(11) NOT NULL COMMENT '发信息人',
  `receiver` int(11) NOT NULL COMMENT '接收人',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '消息状态 0未读，1已读',
  `box` int(2) NOT NULL DEFAULT '1' COMMENT '消息信箱 0收件箱 1发件箱 2草稿箱 3垃圾箱',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '消息类型 0-News,1-Notice,20-Meeting,21-Interview,22-Appointment,23-Event,24-Activity,25-Other,4-Task,5-Refix,6-Birthday',
  `type_name` varchar(20) DEFAULT NULL COMMENT '类型名称',
  `link_id` int(11) NOT NULL COMMENT '关联表中数据id',
  `alert_time` datetime DEFAULT NULL COMMENT '提醒时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站内信';

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;

INSERT INTO `message` (`id`, `name`, `content`, `create_time`, `sender`, `receiver`, `status`, `box`, `type`, `type_name`, `link_id`, `alert_time`)
VALUES
	(8,'You have an upcoming Refix with client [Bin Luo] due on 11/05/1112',NULL,'2020-01-11 15:40:25',24,24,0,1,5,'Refix',33,'1112-04-11 00:00:00'),
	(10,'You have an upcoming Refix with client [Bin Luo] due on 11/08/2020',NULL,'2020-01-11 15:44:08',24,24,0,1,5,'Refix',35,'2020-07-12 00:00:00'),
	(11,'You have an upcoming Refix with client [Bin Luo] due on 11/08/2021',NULL,'2020-01-13 13:48:04',24,24,0,1,5,'Refix',36,'2021-07-21 00:00:00'),
	(12,'New Announcement [111222] has published',NULL,'2020-01-20 15:07:21',24,1,0,1,0,'Announcement',66,'2020-01-20 15:07:21'),
	(13,'New Announcement [111222] has published',NULL,'2020-01-20 15:07:21',24,24,0,1,0,'Announcement',66,'2020-01-20 15:07:21'),
	(14,'New Announcement [111222] has published',NULL,'2020-01-20 15:07:21',24,25,0,1,0,'Announcement',66,'2020-01-20 15:07:21'),
	(15,'New Announcement [111222] has published',NULL,'2020-01-20 15:07:21',24,26,0,1,0,'Announcement',66,'2020-01-20 15:07:21'),
	(16,'You have an upcoming Refix with client [ ] due on 11/08/2020',NULL,'2020-01-21 22:43:41',24,24,0,1,5,'Refix',37,'2020-07-12 00:00:00'),
	(17,'You have an upcoming Refix with client [ ] due on 11/05/2236',NULL,'2020-01-21 23:24:54',24,24,0,1,5,'Refix',38,'2236-04-27 00:00:00');

/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `name`)
VALUES
	(1,'普通会员'),
	(2,'系统管理员');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role_uri
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role_uri`;

CREATE TABLE `role_uri` (
  `role_id` int(11) NOT NULL,
  `uri` varchar(100) NOT NULL,
  KEY `fk_role_uri` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色授权表';

LOCK TABLES `role_uri` WRITE;
/*!40000 ALTER TABLE `role_uri` DISABLE KEYS */;

INSERT INTO `role_uri` (`role_id`, `uri`)
VALUES
	(2,'/tm/'),
	(2,'/admin/trade/'),
	(2,'/admin/trade/statistics/'),
	(2,'/im/'),
	(2,'/admin/channel/'),
	(2,'/admin/content/'),
	(2,'/pm/'),
	(2,'/admin/category/'),
	(2,'/admin/goods/'),
	(2,'/admin/company/'),
	(2,'/um/'),
	(2,'/admin/user/'),
	(2,'/admin/role/'),
	(2,'/admin/config/'),
	(2,'/admin/dict/'),
	(2,'/admin/visit/');

/*!40000 ALTER TABLE `role_uri` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  `creator` int(11) NOT NULL COMMENT '创建人',
  `user_id` int(11) NOT NULL COMMENT '属于谁',
  `due_date` datetime DEFAULT NULL COMMENT '到期时间',
  `reminder_date` datetime DEFAULT NULL COMMENT '提醒时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:未完成 1:已完成',
  `reminder_days` smallint(4) DEFAULT '0',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:task 1:refix',
  `client_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务计划表';

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;

INSERT INTO `task` (`id`, `title`, `description`, `creator`, `user_id`, `due_date`, `reminder_date`, `create_time`, `status`, `reminder_days`, `type`, `client_id`)
VALUES
	(33,'Bin Luo','111',24,24,'1112-05-11 00:00:00','1112-04-11 00:00:00','2020-01-11 15:40:25',0,30,1,1),
	(35,'Bin Luo','haha',24,24,'2020-08-11 00:00:00','2020-07-12 00:00:00','2020-01-11 15:44:08',0,30,1,1),
	(36,'Bin Luo','11111',24,24,'2021-08-11 00:00:00','2021-07-21 00:00:00','2020-01-13 13:48:04',0,21,1,2),
	(37,' ','hehe',24,24,'2020-08-11 00:00:00','2020-07-12 00:00:00','2020-01-21 22:43:41',0,30,1,2),
	(38,' ','ahahhasdlflas',24,24,'2236-05-11 00:00:00','2236-04-27 00:00:00','2020-01-21 23:24:54',0,14,1,2);

/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template
# ------------------------------------------------------------

DROP TABLE IF EXISTS `template`;

CREATE TABLE `template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `path` varchar(50) DEFAULT NULL COMMENT '路径',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '模板类型 0-栏目模板，1-内容模板',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表';

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;

INSERT INTO `template` (`id`, `name`, `path`, `type`)
VALUES
	(1,'博客栏目','/channel/blog.html',0),
	(2,'资讯栏目','/channel/info.html',0),
	(3,'知识栏目','/channel/know.html',0),
	(4,'博客内容','/content/view.html',1),
	(5,'资讯内容','/content/info.html',1),
	(6,'知识内容','/content/know.html',1);

/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table trade
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trade`;

CREATE TABLE `trade` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL,
  `trade_no` varchar(20) NOT NULL DEFAULT '',
  `is_self` tinyint(2) NOT NULL DEFAULT '0' COMMENT '收款人是否客户本人,0-非本人,1-本人',
  `final_proof` varchar(100) DEFAULT NULL COMMENT '转正不可撤销声明',
  `pay_cy` char(3) NOT NULL COMMENT '支付币种',
  `pay_total` decimal(10,4) NOT NULL COMMENT '支付总金额',
  `pay_md1` varchar(10) NOT NULL DEFAULT '' COMMENT '支付方式1',
  `pay_md1_amount` decimal(10,4) NOT NULL COMMENT '支付方式1金额',
  `pay_md1_memo` varchar(30) DEFAULT NULL COMMENT '支付方式1备注',
  `pay_md2` varchar(10) NOT NULL DEFAULT '' COMMENT '支付方式2',
  `pay_md2_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '支付方式2金额',
  `pay_md2_memo` varchar(30) DEFAULT NULL COMMENT '支付方式2备注',
  `add_info` varchar(100) DEFAULT NULL COMMENT '附加信息',
  `serv_fee` varchar(10) NOT NULL DEFAULT '' COMMENT '手续费',
  `serv_fee_amount` decimal(10,4) NOT NULL COMMENT '手续费金额',
  `serv_fee_memo` varchar(30) DEFAULT NULL COMMENT '手续费备注',
  `ex_rate` decimal(10,2) NOT NULL COMMENT '交易汇率',
  `rate_direction` tinyint(2) NOT NULL DEFAULT '0' COMMENT '汇率方向,0-正向,1-反向',
  `computing_md` tinyint(2) NOT NULL DEFAULT '0' COMMENT '计算方法,0-相除,1-相乘',
  `withdraw_cy` char(3) NOT NULL DEFAULT '' COMMENT '取款币种',
  `withdraw_total` decimal(10,4) NOT NULL COMMENT '取款总金额',
  `withdraw_md1` varchar(10) NOT NULL DEFAULT '' COMMENT '取款方式1',
  `withdraw_md1_amount` decimal(10,4) NOT NULL COMMENT '取款方式1金额',
  `withdraw_md1_memo` varchar(30) DEFAULT NULL COMMENT '取款方式1备注',
  `withdraw_md2` varchar(10) NOT NULL DEFAULT '' COMMENT '取款方式2',
  `withdraw_md2_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '取款方式2金额',
  `withdraw_md2_memo` varchar(30) DEFAULT NULL COMMENT '取款方式2备注',
  `create_actor` int(11) NOT NULL COMMENT '录入人员',
  `update_actor` int(11) DEFAULT NULL COMMENT '修改人员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `custom_time` datetime DEFAULT NULL COMMENT '自定义交易时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易表';

LOCK TABLES `trade` WRITE;
/*!40000 ALTER TABLE `trade` DISABLE KEYS */;

INSERT INTO `trade` (`id`, `uid`, `trade_no`, `is_self`, `final_proof`, `pay_cy`, `pay_total`, `pay_md1`, `pay_md1_amount`, `pay_md1_memo`, `pay_md2`, `pay_md2_amount`, `pay_md2_memo`, `add_info`, `serv_fee`, `serv_fee_amount`, `serv_fee_memo`, `ex_rate`, `rate_direction`, `computing_md`, `withdraw_cy`, `withdraw_total`, `withdraw_md1`, `withdraw_md1_amount`, `withdraw_md1_memo`, `withdraw_md2`, `withdraw_md2_amount`, `withdraw_md2_memo`, `create_actor`, `update_actor`, `create_time`, `update_time`, `custom_time`)
VALUES
	(13,15,'20190826230123',0,'/upload/201909/201909021203009.jpeg','CNY',1000.0000,'Bank Card',800.0000,'1','Visa Card',200.0000,'2','12345','Visa Card',30.0000,'3',0.22,1,0,'NZD',4545.4500,'Wechat',1000.0000,'4','Alipay',3545.4500,'5',1,NULL,'2019-08-26 23:01:23',NULL,NULL),
	(14,16,'20190828150933',1,'/upload/201909/201909021439013.png','CNY',10000.0000,'Cash',10000.0000,NULL,'Bank Card',0.0000,NULL,NULL,'Cash',200.0000,NULL,4.75,0,0,'NZD',2105.2600,'Cash',2105.2600,NULL,'Cash',0.0000,NULL,16,NULL,'2019-08-28 15:09:33',NULL,NULL),
	(15,16,'20190906080124',1,'/upload/201909/201909060759038.jpeg','NZD',100.0000,'Cash',100.0000,NULL,'Cash',0.0000,NULL,NULL,'Cash',2.0000,NULL,1700.00,0,0,'BTC',0.0600,'Cash',0.0600,NULL,'Cash',0.0000,NULL,1,NULL,'2019-09-06 08:01:24',NULL,NULL),
	(16,16,'20190912113956',1,NULL,'CNY',1000.0000,'Cash',100.0000,NULL,'Bank Card',900.0000,NULL,NULL,'Cash',20.0000,NULL,4.76,0,0,'NZD',210.0800,'Cash',210.0800,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-12 11:39:56',NULL,NULL),
	(23,16,'20190919131233',1,NULL,'NZD',10000.0000,'Cash',10000.0000,NULL,'Cash',0.0000,NULL,NULL,'Cash',200.0000,NULL,15400.00,0,0,'BTC',0.6494,'Cash',0.6494,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-19 13:12:33',NULL,NULL),
	(24,16,'20190919163058',1,NULL,'NZD',10000.0000,'Cash',10000.0000,NULL,'Cash',0.0000,NULL,NULL,'Cash',200.0000,NULL,15400.00,0,0,'BTC',0.6494,'Cash',0.6494,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-19 16:30:58',NULL,NULL),
	(25,16,'20190919214717',1,NULL,'NZD',10000.0000,'Cash',10000.0000,NULL,'Cash',0.0000,NULL,NULL,'Cash',200.0000,NULL,15400.00,0,0,'BTC',0.6494,'Cash',0.6494,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-19 21:47:17',NULL,'2019-09-18 21:47:17'),
	(26,16,'20190920222157',1,NULL,'BTC',0.3980,'Cash',0.3980,NULL,'Cash',0.0000,NULL,NULL,'Cash',0.0100,NULL,17588.00,0,1,'NZD',7000.0240,'Cash',7000.0240,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-20 22:21:57',NULL,'2019-09-20 22:21:57'),
	(27,16,'20190920230819',1,NULL,'NZD',0.3980,'Cash',0.3980,NULL,'Cash',0.0000,NULL,NULL,'Cash',0.0100,NULL,17588.00,0,1,'BTC',7000.0240,'Cash',7000.0240,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-20 23:08:19',NULL,'2019-09-20 23:08:19'),
	(28,16,'20190921091707',1,NULL,'BTC',0.3980,'Cash',0.3980,NULL,'Cash',0.0000,NULL,NULL,'Cash',0.0100,NULL,17588.00,0,1,'NZD',7000.0200,'Cash',7000.0200,NULL,'Cash',0.0000,NULL,16,NULL,'2019-09-21 09:17:07',NULL,'2019-09-21 09:17:07');

/*!40000 ALTER TABLE `trade` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT '',
  `pwd` varchar(50) DEFAULT '',
  `email` varchar(50) NOT NULL DEFAULT '',
  `work_phone` varchar(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT '',
  `prefered_name` varchar(20) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `office_location` varchar(100) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `department` varchar(50) DEFAULT NULL,
  `employee_number` varchar(20) DEFAULT NULL,
  `kaihu_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '开户申请状态 0-未申请，1-已申请，2-已开户',
  `register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `access_code` varchar(8) DEFAULT NULL COMMENT '通行码',
  `ac_status` tinyint(2) DEFAULT '1' COMMENT '通行码是否有效0-无效1-有效',
  `status` tinyint(2) DEFAULT '1' COMMENT '账户状态1-有效0-无效',
  `role_id` int(10) DEFAULT NULL,
  `sendmail_account` varchar(30) DEFAULT NULL,
  `sendmail_password` varchar(50) DEFAULT NULL,
  `mail_host` varchar(100) DEFAULT NULL,
  `logo` varchar(100) DEFAULT NULL,
  `uselogo` tinyint(2) DEFAULT '1' COMMENT '是否使用logo.0-不使用，1-使用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `username`, `first_name`, `last_name`, `pwd`, `email`, `work_phone`, `mobile`, `prefered_name`, `birthday`, `office_location`, `title`, `avatar`, `department`, `employee_number`, `kaihu_status`, `register_time`, `access_code`, `ac_status`, `status`, `role_id`, `sendmail_account`, `sendmail_password`, `mail_host`, `logo`, `uselogo`)
VALUES
	(1,'admin','admin','admin','4941a13dbce0ea7432b126ce68e7aa7a','a','skype','1','4546','2007-01-02','2 Mellons Bay Road, Howick',NULL,'/upload/201912/201912191600016.jpg',NULL,'2',1,NULL,NULL,0,1,2,NULL,NULL,NULL,NULL,0),
	(24,NULL,'Bin','Luo','152d2e7be12f852428a20fc1cddb77a8','robinsonhood1@gmail.com','02108063825a2','02108063825b2','12a2',NULL,'o2','t112','/upload/202002/202002081714037.jpg','d112','e12',0,'2019-12-09 22:20:42','89499499',0,1,1,'robin@taijicoin.nz','111111','taijicoin','/upload/202002/202002081910030.jpg',1),
	(25,NULL,'Evan','Zeng','','robinsonhood1978@gmail.com','02108063825','02108063825',NULL,NULL,NULL,NULL,'/upload/201912/201912191605027.jpg',NULL,NULL,0,'2019-12-19 19:01:36','44621732',1,1,1,NULL,NULL,NULL,NULL,0),
	(26,NULL,'Ceejay','Chen','152d2e7be12f852428a20fc1cddb77a8','fynco.storage@gmail.com','02108063825','02108063877',NULL,NULL,NULL,NULL,'/upload/202001/202001030932023.jpg',NULL,NULL,0,'2019-12-19 19:35:41','39209074',1,1,1,'robin@taijicoin.nz','111111',NULL,NULL,0);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table visits
# ------------------------------------------------------------

DROP TABLE IF EXISTS `visits`;

CREATE TABLE `visits` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ua` varchar(200) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `cid` varchar(50) DEFAULT NULL,
  `cname` varchar(50) DEFAULT NULL,
  `refurl` varchar(50) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `screenX` varchar(20) DEFAULT NULL,
  `screenY` varchar(20) DEFAULT NULL,
  `os` varchar(30) DEFAULT NULL,
  `brower` varchar(30) DEFAULT NULL,
  `visit_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `idx_stat_ip` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户访问表';

LOCK TABLES `visits` WRITE;
/*!40000 ALTER TABLE `visits` DISABLE KEYS */;

INSERT INTO `visits` (`id`, `ua`, `ip`, `cid`, `cname`, `refurl`, `url`, `screenX`, `screenY`, `os`, `brower`, `visit_time`)
VALUES
	(1,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-10-14 21:47:46'),
	(2,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/','http://localhost:81/news','1440','900','MacIntel','Chrome','2019-10-14 21:47:57'),
	(3,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/news','http://localhost:81/m','1440','900','MacIntel','Chrome','2019-10-14 21:57:50'),
	(4,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-10-14 23:22:15'),
	(5,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-10-17 12:29:43'),
	(6,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-10-17 12:29:52'),
	(7,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/','http://localhost:81/blog','1440','900','MacIntel','Chrome','2019-10-17 12:30:09'),
	(8,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/blog','http://localhost:81/blog-39','1440','900','MacIntel','Chrome','2019-10-17 12:41:43'),
	(9,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/blog','http://localhost:81/news','1440','900','MacIntel','Chrome','2019-10-17 12:42:01'),
	(10,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/news','http://localhost:81/news-36','1440','900','MacIntel','Chrome','2019-10-17 12:42:02'),
	(11,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','http://localhost:81/news-36','http://localhost:81/know-37','1440','900','MacIntel','Chrome','2019-10-17 12:42:36'),
	(12,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36','27.252.63.211','NZ','NEW ZEALAND','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-10-17 13:00:50'),
	(13,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-12-04 12:03:37'),
	(14,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:81/','http://localhost:81/l','1440','900','MacIntel','Chrome','2019-12-04 12:03:43'),
	(15,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:81/l','http://localhost:81/r','1440','900','MacIntel','Chrome','2019-12-04 12:03:47'),
	(16,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:81/','1440','900','MacIntel','Chrome','2019-12-04 12:37:33'),
	(17,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 13:04:09'),
	(18,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/login','1440','900','MacIntel','Chrome','2019-12-04 21:53:30'),
	(19,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/login','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-04 21:53:42'),
	(20,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 21:55:42'),
	(21,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 21:59:11'),
	(22,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/news-36','1440','900','MacIntel','Chrome','2019-12-04 21:59:14'),
	(23,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/know-38','1440','900','MacIntel','Chrome','2019-12-04 21:59:29'),
	(24,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/r','1440','900','MacIntel','Chrome','2019-12-04 21:59:37'),
	(25,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:06:50'),
	(26,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:07:00'),
	(27,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:07:06'),
	(28,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:08:29'),
	(29,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:08:30'),
	(30,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:11:26'),
	(31,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:11:29'),
	(32,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:11:40'),
	(33,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:12:37'),
	(34,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:12:40'),
	(35,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/blog','1440','900','MacIntel','Chrome','2019-12-04 22:12:41'),
	(36,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/blog','http://localhost:82/know-37','1440','900','MacIntel','Chrome','2019-12-04 22:12:44'),
	(37,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:40:01'),
	(38,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-04 22:44:58'),
	(39,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-04 22:46:53'),
	(40,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-04 22:46:56'),
	(41,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m/kaihu','1440','900','MacIntel','Chrome','2019-12-04 22:46:58'),
	(42,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/kaihu','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-04 22:47:16'),
	(43,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/person','http://localhost:82/m/balance','1440','900','MacIntel','Chrome','2019-12-04 22:47:17'),
	(44,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/avatar','1440','900','MacIntel','Chrome','2019-12-04 22:47:21'),
	(45,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/avatar','http://localhost:82/m/balance','1440','900','MacIntel','Chrome','2019-12-04 22:47:23'),
	(46,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:47:25'),
	(47,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:49:25'),
	(48,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:50:44'),
	(49,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:51:03'),
	(50,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/pwd','http://localhost:82/m/blogs','1440','900','MacIntel','Chrome','2019-12-04 22:51:06'),
	(51,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/blogs','http://localhost:82/m/balance','1440','900','MacIntel','Chrome','2019-12-04 22:51:24'),
	(52,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/blogs','http://localhost:82/m/balance','1440','900','MacIntel','Chrome','2019-12-04 22:51:34'),
	(53,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/balance','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-04 22:51:35'),
	(54,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/person','http://localhost:82/m/avatar','1440','900','MacIntel','Chrome','2019-12-04 22:51:38'),
	(55,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/avatar','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:51:39'),
	(56,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/write','http://localhost:82/m/blogs','1440','900','MacIntel','Chrome','2019-12-04 22:51:50'),
	(57,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/blogs','http://localhost:82/m/kaihu','1440','900','MacIntel','Chrome','2019-12-04 22:51:59'),
	(58,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/blogs','http://localhost:82/m/kaihu','1440','900','MacIntel','Chrome','2019-12-04 22:52:29'),
	(59,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/kaihu','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-04 22:52:36'),
	(60,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:52:42'),
	(61,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-04 22:52:44'),
	(62,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-04 22:52:46'),
	(63,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/blog','1440','900','MacIntel','Chrome','2019-12-04 22:52:48'),
	(64,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/blog','http://localhost:82/blog-39','1440','900','MacIntel','Chrome','2019-12-04 22:52:54'),
	(65,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 13:54:24'),
	(66,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 16:39:04'),
	(67,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 17:11:21'),
	(68,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 17:13:03'),
	(69,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-05 17:13:12'),
	(70,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 17:15:17'),
	(71,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 17:17:19'),
	(72,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/','1440','900','MacIntel','Chrome','2019-12-05 17:19:46'),
	(73,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/logout','http://localhost:82/r','1440','900','MacIntel','Chrome','2019-12-05 17:23:15'),
	(74,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:21:48'),
	(75,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:36:34'),
	(76,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:37:36'),
	(77,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:39:01'),
	(78,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:39:04'),
	(79,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:40:25'),
	(80,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:40:52'),
	(81,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/l','1440','900','MacIntel','Chrome','2019-12-06 07:42:43'),
	(82,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/l','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-06 07:53:07'),
	(83,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/news','http://localhost:82/blog','1440','900','MacIntel','Chrome','2019-12-06 07:53:08'),
	(84,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/blog','http://localhost:82/news','1440','900','MacIntel','Chrome','2019-12-06 07:53:11'),
	(85,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-07 05:16:03'),
	(86,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 08:56:48'),
	(87,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 15:54:07'),
	(88,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m/avatar','1440','900','MacIntel','Chrome','2019-12-09 15:54:10'),
	(89,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/avatar','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 15:54:18'),
	(90,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/person','http://localhost:82/m/pwd','1440','900','MacIntel','Chrome','2019-12-09 15:54:32'),
	(91,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/pwd','http://localhost:82/m/avatar','1440','900','MacIntel','Chrome','2019-12-09 15:54:37'),
	(92,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/avatar','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 15:54:38'),
	(93,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/m/','1440','900','MacIntel','Chrome','2019-12-09 16:16:48'),
	(94,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m/','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 16:17:03'),
	(95,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/r','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 18:21:39'),
	(96,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','','http://localhost:82/m/kaihu','1440','900','MacIntel','Chrome','2019-12-09 18:23:07'),
	(97,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 22:33:03'),
	(98,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 22:33:30'),
	(99,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/','http://localhost:82/m','1440','900','MacIntel','Chrome','2019-12-09 23:07:59'),
	(100,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 23:08:01'),
	(101,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 23:32:05'),
	(102,'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36','103.76.205.150','88','国内未能识别的地区','http://localhost:82/m','http://localhost:82/m/person','1440','900','MacIntel','Chrome','2019-12-09 23:49:32');

/*!40000 ALTER TABLE `visits` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
