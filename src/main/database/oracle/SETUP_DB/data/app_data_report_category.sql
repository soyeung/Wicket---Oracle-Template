prompt register report categories

define repcat_user_activity = 'user activity'
define repcat_security      = 'security'
define repcat_db_management = 'db management'

insert into app_data.report_category ( rptc_id , rptc_name ) values ( app_data.sq_rptc.nextval , '&repcat_user_activity' );
insert into app_data.report_category ( rptc_id , rptc_name ) values ( app_data.sq_rptc.nextval , '&repcat_security' );
insert into app_data.report_category ( rptc_id , rptc_name ) values ( app_data.sq_rptc.nextval , '&repcat_db_management' );
commit;