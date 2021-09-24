-- MySQL dump 10.13  Distrib 5.5.57, for Linux (x86_64)
--
-- Host: mpowerdbinstancencrypted.c0yv5wrdxsnu.ap-south-1.rds.amazonaws.com    Database: empowerdb
-- ------------------------------------------------------
-- Server version	5.5.54-log

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
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (user_id),
  UNIQUE KEY UK_k8d0f2n7n88w1a16yhua64onx (user_name)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE seller_detail (
  seller_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  connector varchar(55) NOT NULL,
  contact_no bigint(20) NOT NULL,
  group_policy_number varchar(55) NOT NULL,
  is_password_set bit(1) DEFAULT 0,
  seller_email_id varchar(255) NOT NULL,
  seller_name varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (seller_detail_id),
  UNIQUE KEY UK_cluxp0ndjy71mk6knplvrxk0r (contact_no),
  KEY FK_seller_details_user_id (user_id),
  CONSTRAINT FK8ead18gws4in2eaw47vwnoai8 FOREIGN KEY (user_id) REFERENCES users (user_id),
  CONSTRAINT FK_seller_details_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE customer_details (
  customer_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  adhaar_number varchar(12) NOT NULL,
  application_completion_date bigint(20) DEFAULT NULL,
  application_step varchar(55) DEFAULT NULL,
  cas_id varchar(15) NOT NULL,
  cust_email_id varchar(55) NOT NULL,
  cust_mobile_no bigint(20) NOT NULL,
  cust_otp_verified_date bigint(20) DEFAULT NULL,
  customer_name varchar(60) DEFAULT NULL,
  dob bigint(20) NOT NULL,
  hdf_signed_date bigint(20) NOT NULL,
  loan_app_number varchar(16) NOT NULL,
  loan_tenure double DEFAULT 0,
  loan_type varchar(55) NOT NULL,
  policy_holder_name varchar(55) NOT NULL,
  proposal_number varchar(55) DEFAULT NULL,
  relationship_with_gp_policy_holder varchar(55) NOT NULL,
  scheme_type varchar(55) NOT NULL,
  app_status varchar(55) DEFAULT NULL,
  sum_assured double NOT NULL DEFAULT 0,
  version bigint(20) DEFAULT 0,
  seller_detail_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (customer_detail_id),
  UNIQUE KEY UK_hjrvqj8jjectnd8p9jbpecvj1 (loan_app_number),
  UNIQUE KEY UK_l40cy312vcnjq2fcpxdq9n13q (proposal_number),
  KEY FKialw4u09up4ei9jryh58gx76s (seller_detail_id),
  CONSTRAINT FKialw4u09up4ei9jryh58gx76s FOREIGN KEY (seller_detail_id) REFERENCES seller_detail (seller_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE nominee_details (
  nominee_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  date_of_birth varchar(55) NOT NULL,
  gender varchar(20) NOT NULL,
  is_appointee bit(1) NOT NULL DEFAULT 0,
  nominee_name varchar(60) DEFAULT NULL,
  relation_with_nominee varchar(55) DEFAULT NULL,
  relationship_with_assured varchar(55) NOT NULL,
  version bigint(20) DEFAULT 0,
  cust_detail_id bigint(20) NOT NULL,
  PRIMARY KEY (nominee_detail_id),
  KEY FK91sau6pgx8uqaj2990vtwu2po (cust_detail_id),
  CONSTRAINT FK91sau6pgx8uqaj2990vtwu2po FOREIGN KEY (cust_detail_id) REFERENCES customer_details (customer_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE proposal_number_dtl (
  proposal_number_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  current_day_time bigint(20) NOT NULL,
  start_number int(11) NOT NULL DEFAULT 1,
  use_number int(11) NOT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (proposal_number_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE appointee_details (
  appointee_detail_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  appointee_name varchar(60) DEFAULT NULL,
  date_of_birth varchar(55) DEFAULT NULL,
  gender varchar(20) DEFAULT NULL,
  is_relation_with_appointee bit(1) NOT NULL DEFAULT 0,
  appointee_relationship_with_beneficiary varchar(55) DEFAULT NULL,
  relationship_with_assured varchar(55) NOT NULL,
  nominee_id bigint(20) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (appointee_detail_id),
  KEY FKfil1wx3o4p69sx9c8p7cnepsl (nominee_id),
  CONSTRAINT FKfil1wx3o4p69sx9c8p7cnepsl FOREIGN KEY (nominee_id) REFERENCES nominee_details (nominee_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;

CREATE TABLE date_dir (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  date varchar(50) NOT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY date (date)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;
 
CREATE TABLE bank_dir (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  bank_name varchar(255) DEFAULT NULL,
  file_extention varchar(255) DEFAULT NULL,
  date_dir_id bigint(20) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  aws_file_path varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK3h3m9gpvfk4boxmvbcnjh90jl (date_dir_id),
  CONSTRAINT FK3h3m9gpvfk4boxmvbcnjh90jl FOREIGN KEY (date_dir_id) REFERENCES date_dir (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;



CREATE TABLE health_declaration (
  health_dcl_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  health_declaration bit(1) NOT NULL DEFAULT 0,
  negative_declaration varchar(150) NOT NULL,
  other_insurance varchar(255) NOT NULL,
  cust_detail_id bigint(20) NOT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (health_dcl_id),
  KEY FKsrs9b0a5l600t792fd5rvhsc6 (cust_detail_id),
  CONSTRAINT FKsrs9b0a5l600t792fd5rvhsc6 FOREIGN KEY (cust_detail_id) REFERENCES customer_details (customer_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE mandatory_declaration (
  mandatory_dcl_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  signed_date bigint(20) NOT NULL,
  mandatory_declaration bit(1) NOT NULL DEFAULT 0,
  place varchar(30) NOT NULL,
  policy_holder_name varchar(55) NOT NULL,
  cust_detail_id bigint(20) NOT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (mandatory_dcl_id),
  KEY FKmxk313rb3x8got4y3gyce65ix (cust_detail_id),
  CONSTRAINT FKmxk313rb3x8got4y3gyce65ix FOREIGN KEY (cust_detail_id) REFERENCES customer_details (customer_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;

CREATE TABLE mli_email (
  mli_email_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  mail_id_to varchar(255) NOT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (mli_email_id)
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
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (otp_history_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


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
  version bigint(20) DEFAULT 0,
  aws_file_path varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_loanappnumber_doctype (loan_app_number,doc_type),
  KEY FKte9vk27w2o8upd0srcw15usdt (bank_dir_id),
  CONSTRAINT FKte9vk27w2o8upd0srcw15usdt FOREIGN KEY (bank_dir_id) REFERENCES bank_dir (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;


CREATE TABLE seller_banks (
  seller_bank_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  bank_name varchar(55) NOT NULL,
  status varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  seller_detail_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (seller_bank_id),
  KEY FKamikd9k6wpk95vewwqxt80lis (seller_detail_id),
  CONSTRAINT FKamikd9k6wpk95vewwqxt80lis FOREIGN KEY (seller_detail_id) REFERENCES seller_detail (seller_detail_id)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;

CREATE TABLE cron_job (
  cron_job_id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_on bigint(20) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_on bigint(20) DEFAULT NULL,
  status bit(1) NOT NULL DEFAULT 0,
  cron_type varchar(55) DEFAULT NULL,
  version bigint(20) DEFAULT 0,
  PRIMARY KEY (cron_job_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

ALTER TABLE users MODIFY status tinyint(1) DEFAULT '0';
ALTER TABLE seller_detail MODIFY is_password_set tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE nominee_details MODIFY is_appointee tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE appointee_details MODIFY is_relation_with_appointee tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE health_declaration MODIFY health_declaration tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE mandatory_declaration MODIFY mandatory_declaration tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE cron_job MODIFY status tinyint(1) NOT NULL DEFAULT '0';

insert into users(user_id,user_name,user_type,status,password) values(1001,'admin@loansecure_mli','ROLE_ADMIN',1,'$2a$10$icR8mqWC4fuuyFwxZHjTnOfZui2BXgB5N8sx8Bli2ETNtTTrxOqM6');
 
insert into proposal_number_dtl(proposal_number_id,modified_on,current_day_time,start_number,use_number) values(1,1510669302,1510666649108,1,1);


insert into cron_job values(1,NULL,NULL,NULL,NULL,FALSE,'EXCEL_TO_BANK',0);


CREATE INDEX idx_users_user_name
ON users (user_name);

CREATE INDEX idx_customer_details_proposal_number
ON customer_details (proposal_number);


