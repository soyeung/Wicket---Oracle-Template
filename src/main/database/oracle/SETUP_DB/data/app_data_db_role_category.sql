prompt define the role categories that our application has

define rc_user_management        = "User Management"
define rc_application_management = "Application Management"
define rc_ref_data_management    = "Reference Data Management"
define rc_db_management          = "DB Management"
define rc_report                 = "Report"

prompt add the role categories

insert into app_data.db_role_category( dbrlc_id , dbrlc_name ) values ( app_data.sq_dbrlc.nextval , '&rc_user_management' );
insert into app_data.db_role_category( dbrlc_id , dbrlc_name ) values ( app_data.sq_dbrlc.nextval , '&rc_application_management' );
insert into app_data.db_role_category( dbrlc_id , dbrlc_name ) values ( app_data.sq_dbrlc.nextval , '&rc_ref_data_management' );
insert into app_data.db_role_category( dbrlc_id , dbrlc_name ) values ( app_data.sq_dbrlc.nextval , '&rc_db_management' );
insert into app_data.db_role_category( dbrlc_id , dbrlc_name ) values ( app_data.sq_dbrlc.nextval , '&rc_report' );
commit;