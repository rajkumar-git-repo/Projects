=========================================================Need to run on production server======================================

--- 22 NOV 2017  query for increase size of nominee & appointee name 55 to 60 
ALTER TABLE nominee_details MODIFY nominee_name VARCHAR (60);
ALTER TABLE appointee_details MODIFY appointee_name VARCHAR (60);


-- 17 NOV 2017  query for increase size of customer name 55 to 60 

ALTER TABLE customer_details MODIFY customer_name VARCHAR (60);

-- 21 NOV 2017 for making date as unique in date_dir and loan_app_number, doc_type in scheme_dir 
ALTER TABLE date_dir ADD UNIQUE (date);
ALTER TABLE date_dir MODIFY date varchar(50) NOT NULL;
ALTER TABLE scheme_dir ADD CONSTRAINT unique_loanappnumber_doctype UNIQUE (loan_app_number, doc_type);

--Aadhaar number from bigint  to varchar(12) 
ALTER TABLE customer_details change adhaar_number adhaar_number varchar(12) NOT NULL;




--24/11/2017-START---Adding version column in remaining tables;

ALTER TABLE proposal_number_dtl  ADD version bigint(20) DEFAULT 0;

ALTER TABLE appointee_details  ADD version bigint(20) DEFAULT 0;
 
ALTER TABLE cust_success_otp  ADD version bigint(20) DEFAULT 0;
  
ALTER TABLE date_dir  ADD version bigint(20) DEFAULT 0;
  
ALTER TABLE bank_dir  ADD version bigint(20) DEFAULT 0;
  
ALTER TABLE health_declaration  ADD version bigint(20) DEFAULT 0;
  
ALTER TABLE mandatory_declaration  ADD version bigint(20) DEFAULT 0;
    
ALTER TABLE mli_email  ADD version bigint(20) DEFAULT 0;
    
ALTER TABLE otp_history  ADD version bigint(20) DEFAULT 0;
    
ALTER TABLE scheme_dir  ADD version bigint(20) DEFAULT 0;

ALTER TABLE customer_details  ALTER version SET DEFAULT 0;
ALTER TABLE nominee_details  ALTER version SET DEFAULT 0;
ALTER TABLE seller_banks  ALTER version SET DEFAULT 0;
ALTER TABLE seller_detail  ALTER version SET DEFAULT 0;
ALTER TABLE users  ALTER version SET DEFAULT 0;

--24/11/2017-END-----Adding version column in remaining tables;




--Drop seller_detail_seller_bank_entity from server not in use

  DROP TABLE seller_detail_seller_bank_entity;



--Create cron job table query

CREATE TABLE cron_job (
  cron_job_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  status BIT NOT NULL DEFAULT 0,
  cron_type varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (`cron_job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--insert query for cron job table

insert into cron_job values(1,NULL,NULL,NULL,NULL,FALSE,'EXCEL_TO_BANK',0);









ALTER TABLE scheme_dir ADD aws_file_path VARCHAR (255);
ALTER TABLE bank_dir ADD aws_file_path VARCHAR (255);

--Drop cust_success_otp from server not in use
DROP TABLE cust_success_otp;

--Adding user_id as FK in seller_detail table.
ALTER TABLE seller_detail 
ADD COLUMN user_id BIGINT(20) , 
ADD CONSTRAINT FK_seller_detail_user_id FOREIGN KEY (user_id) 
REFERENCES users(user_id);
=========================================================Need to run on production server======================================

CREATE TABLE users (
  user_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  first_name varchar(55) DEFAULT NULL,
  last_login bigint(20) DEFAULT NULL,
  last_name varchar(55) DEFAULT NULL,
  mobile varchar(20) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  status bit(1) DEFAULT NULL,
  user_name varchar(55) DEFAULT NULL,
  user_type varchar(50) DEFAULT NULL,
  version bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY UK_k8d0f2n7n88w1a16yhua64onx (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=latin1;




CREATE TABLE seller_detail (
  seller_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  connector varchar(55) NOT NULL,
  contact_no bigint(10) NOT NULL,
  group_policy_number varchar(55) NOT NULL,
  is_password_set BIT NOT NULL DEFAULT 0,
  seller_email_id varchar(255) NOT NULL,
  seller_name varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (`seller_detail_id`),
  UNIQUE KEY UK_cluxp0ndjy71mk6knplvrxk0r (`contact_no`),
  KEY FK8ead18gws4in2eaw47vwnoai8 (`user_id`),
  CONSTRAINT FK8ead18gws4in2eaw47vwnoai8 FOREIGN KEY (`user_id`) REFERENCES users (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=latin1;





CREATE TABLE customer_details (
  customer_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  application_completion_date bigint(20) DEFAULT NULL,
  application_step varchar(55) DEFAULT NULL,
  cust_email_id varchar(55) NOT NULL,
  cust_mobile_no bigint(20) NOT NULL,
  cust_otp_verified_date bigint(20) DEFAULT NULL,
  customer_name varchar(55) NOT NULL,
  dob bigint(20) NOT NULL,
  hdf_signed_date bigint(20) NOT NULL,
  loan_app_number varchar(16) NOT NULL,
  loan_tenure double DEFAULT '0',
  loan_type varchar(55) NOT NULL,
  policy_holder_name varchar(55) NOT NULL,
  proposal_number varchar(55) DEFAULT NULL,
  relationship_with_gp_policy_holder varchar(55) NOT NULL,
  scheme_type varchar(55) NOT NULL,
  app_status varchar(55) DEFAULT NULL,
  sum_assured double NOT NULL DEFAULT '0',
  version bigint(20) DEFAULT NULL,
  seller_detail_id bigint(20) DEFAULT NULL,
  adhaar_number bigint(20) NOT NULL,
  cas_id varchar(15) NOT NULL,
  PRIMARY KEY (`customer_detail_id`),
  UNIQUE KEY UK_l40cy312vcnjq2fcpxdq9n13q (`proposal_number`),
  KEY FKialw4u09up4ei9jryh58gx76s (`seller_detail_id`),
  CONSTRAINT FKialw4u09up4ei9jryh58gx76s FOREIGN KEY (`seller_detail_id`) REFERENCES seller_detail (`seller_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;





CREATE TABLE nominee_details (
  nominee_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  date_of_birth varchar(55) NOT NULL,
  gender varchar(20) NOT NULL,
  is_appointee BIT NOT NULL DEFAULT 0,
  nominee_name varchar(55) NOT NULL,
  relation_with_nominee varchar(55) DEFAULT NULL,
  relationship_with_assured varchar(55) NOT NULL,
  version bigint(20) DEFAULT NULL,
  cust_detail_id bigint(20) NOT NULL,
  PRIMARY KEY (`nominee_detail_id`),
  KEY FK91sau6pgx8uqaj2990vtwu2po (`cust_detail_id`),
  CONSTRAINT FK91sau6pgx8uqaj2990vtwu2po FOREIGN KEY (`cust_detail_id`) REFERENCES customer_details (`customer_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;





CREATE TABLE proposal_number_dtl (
  proposal_number_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  current_day_time bigint(20) NOT NULL,
  start_number int(11) NOT NULL DEFAULT '1',
  use_number int(11) NOT NULL,
  PRIMARY KEY (proposal_number_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;






CREATE TABLE appointee_details (
  appointee_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  appointee_name varchar(55) DEFAULT NULL,
  date_of_birth varchar(55) DEFAULT NULL,
  gender varchar(20) DEFAULT NULL,
  is_relation_with_appointee BIT NOT NULL DEFAULT 0,
  appointee_relationship_with_beneficiary varchar(55) DEFAULT NULL,
  relationship_with_assured varchar(55) NOT NULL,
  nominee_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (`appointee_detail_id`),
  KEY FKfil1wx3o4p69sx9c8p7cnepsl (`nominee_id`),
  CONSTRAINT FKfil1wx3o4p69sx9c8p7cnepsl FOREIGN KEY (`nominee_id`) REFERENCES nominee_details (`nominee_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;








CREATE TABLE cust_success_otp (
  cust_otp_verfy_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  cust_mobile_no bigint(20) NOT NULL,
  success_otp int(11) NOT NULL,
  cust_detail_id bigint(20) NOT NULL,
  PRIMARY KEY (`cust_otp_verfy_id`),
  KEY FK2c02dy6n4xrn9aldlx1iju1vs (`cust_detail_id`),
  CONSTRAINT FK2c02dy6n4xrn9aldlx1iju1vs FOREIGN KEY (`cust_detail_id`) REFERENCES customer_details (`customer_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;






CREATE TABLE date_dir (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  date varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;




CREATE TABLE bank_dir (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  bank_name varchar(255) DEFAULT NULL,
  file_extention varchar(255) DEFAULT NULL,
  date_dir_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY FK3h3m9gpvfk4boxmvbcnjh90jl (`date_dir_id`),
  CONSTRAINT FK3h3m9gpvfk4boxmvbcnjh90jl FOREIGN KEY (`date_dir_id`) REFERENCES date_dir (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;




CREATE TABLE health_declaration (
  health_dcl_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  health_declaration BIT NOT NULL DEFAULT 0,
  negative_declaration varchar(200) DEFAULT NULL,
  other_insurance varchar(100) NOT NULL,
  cust_detail_id bigint(20) NOT NULL,
  PRIMARY KEY (`health_dcl_id`),
  KEY FKsrs9b0a5l600t792fd5rvhsc6 (`cust_detail_id`),
  CONSTRAINT FKsrs9b0a5l600t792fd5rvhsc6 FOREIGN KEY (`cust_detail_id`) REFERENCES customer_details (`customer_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;






CREATE TABLE mandatory_declaration (
  mandatory_dcl_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  signed_date bigint(20) NOT NULL,
  mandatory_declaration BIT NOT NULL DEFAULT 0,
  place varchar(30) NOT NULL,
  policy_holder_name varchar(55) NOT NULL,
  cust_detail_id bigint(20) NOT NULL,
  PRIMARY KEY (`mandatory_dcl_id`),
  KEY FKmxk313rb3x8got4y3gyce65ix (`cust_detail_id`),
  CONSTRAINT FKmxk313rb3x8got4y3gyce65ix FOREIGN KEY (`cust_detail_id`) REFERENCES customer_details (`customer_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;





CREATE TABLE mli_email (
  mli_email_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  mail_id_to varchar(255) NOT NULL,
  PRIMARY KEY (`mli_email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;






CREATE TABLE otp_history (
  otp_history_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  cont_no bigint(20) DEFAULT NULL,
  otp int(11) DEFAULT NULL,
  otp_status varchar(55) DEFAULT NULL,
  user_type varchar(255) DEFAULT NULL,
  unique_token varchar(255) DEFAULT NULL,
  PRIMARY KEY (`otp_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=latin1;






CREATE TABLE scheme_dir (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  doc_type varchar(255) DEFAULT NULL,
  file_extention varchar(255) DEFAULT NULL,
  loan_app_number varchar(255) DEFAULT NULL,
  proposal_complete_date varchar(255) DEFAULT NULL,
  scheme_name varchar(255) DEFAULT NULL,
  bank_dir_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY FKte9vk27w2o8upd0srcw15usdt (`bank_dir_id`),
  CONSTRAINT FKte9vk27w2o8upd0srcw15usdt FOREIGN KEY (`bank_dir_id`) REFERENCES bank_dir (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;



CREATE TABLE seller_banks (
  seller_bank_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  bank_name varchar(55) DEFAULT NULL,
  status varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT NULL,
  seller_detail_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (`seller_bank_id`),
  KEY FKamikd9k6wpk95vewwqxt80lis (`seller_detail_id`),
  CONSTRAINT FKamikd9k6wpk95vewwqxt80lis FOREIGN KEY (`seller_detail_id`) REFERENCES seller_detail (`seller_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;


insert into users(user_id,user_name,user_type,status,password) values(1,'admin@loansecure_mli','ROLE_ADMIN',1,'$2a$10$icR8mqWC4fuuyFwxZHjTnOfZui2BXgB5N8sx8Bli2ETNtTTrxOqM6');
 
insert into proposal_number_dtl(proposal_number_id,modified_on,current_day_time,start_number,use_number) values(1,1510669302,1510666649108,1,1);




==========================================================Truncate query if required=====================

	SET FOREIGN_KEY_CHECKS = 0; 
	TRUNCATE TABLE appointee_details;
	TRUNCATE TABLE nominee_details;
	TRUNCATE TABLE health_declaration;
	TRUNCATE TABLE mandatory_declaration;
	TRUNCATE TABLE otp_history;
	TRUNCATE TABLE cust_success_otp;
	TRUNCATE TABLE mli_email; 
	TRUNCATE TABLE  scheme_dir ;
	TRUNCATE TABLE  bank_dir;
	TRUNCATE TABLE  date_dir ;
	TRUNCATE TABLE  customer_details;
	SET FOREIGN_KEY_CHECKS = 1;
	TRUNCATE TABLE  seller_banks;
	TRUNCATE TABLE  seller_detail;
	TRUNCATE TABLE  proposal_number_dtl ;
	TRUNCATE TABLE  users;

==========================================================Truncate query if required=====================

#================ Date 23/05/2019 ====================================



CREATE TABLE ci_rider_ques_master (
  ci_rider_ques_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  question varchar(1200) NOT NULL,
  PRIMARY KEY (ci_rider_ques_id)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;



CREATE TABLE cam_reports (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  cam_report_urls varchar(250),
  cam_report_urls_name varchar(75),
  proposalNumber varchar(55),
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

update customer_details set app_status='PHYSICAL_FORM_SUBMISSION'where app_status='Physical_Form_Verification';

update customer_details set app_status='PHYSICAL_FORM_VERIFICATION'where app_status='PHYSICAL_FORM_SUBMISSION';























