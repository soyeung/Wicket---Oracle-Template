prompt register system reports

/** *********************************************************
  * User Activity Reports
  */

/**
  * Authentication Report
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Authentication' , 'com.github.wicketoracle.app.report.logonhistory.LogonHistoryReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_authenticate_report_user' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_user_activity' ) , ( select rpt_id from app_data.report where rpt_name = 'Authentication' ) );

/**
  * Current User Activity
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Current User Activity Report' , 'com.github.wicketoracle.app.report.useractivity.UserActivityReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_curr_user_activity_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_user_activity' ) , ( select rpt_id from app_data.report where rpt_name = 'Current User Activity Report' ) );

/**
  * System Usage
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Usage Report' , 'com.github.wicketoracle.app.report.usage.UsageReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_usage_data_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = 'user activity' ) , ( select rpt_id from app_data.report where rpt_name = 'Usage Report' ) );

/** *********************************************************
  * Security Reports
  */

/**
  * Security Matrix - By Role
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Security Matrix - By Role' , 'com.github.wicketoracle.app.report.securitymatrixbyrole.SecurityMatrixByRoleReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_security_matrix_by_role' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_security' ) , ( select rpt_id from app_data.report where rpt_name = 'Security Matrix - By Role' ) );

/**
  * Security Matrix - By User
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Security Matrix - By User' , 'com.github.wicketoracle.app.report.securitymatrixbyuser.SecurityMatrixByUserReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_security_matrix_by_user' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_security' ) , ( select rpt_id from app_data.report where rpt_name = 'Security Matrix - By User' ) );

/**
  * Application security model
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Security Model' , 'com.github.wicketoracle.app.report.securitymodel.SecurityModelReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_security_model_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id )
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_security' ) , ( select rpt_id from app_data.report where rpt_name = 'Security Model' ) );

/**
  * User Privileges
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'User Privileges' , 'com.github.wicketoracle.app.report.userprivileges.UserPrivilegesReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_user_privileges_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id )
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_security' ) , ( select rpt_id from app_data.report where rpt_name = 'User Privileges' ) );

commit;

/** *********************************************************
  * DB Management Reports
  */

/**
  * Unindexed Foreign Keys
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Unindexed Foreign Keys' , 'com.github.wicketoracle.app.report.unindexedforeignkey.UnindexedForeignKeyReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_unindexed_fk_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_db_management' ) , ( select rpt_id from app_data.report where rpt_name = 'Unindexed Foreign Keys' ) );

/**
  * Tables without primary keys
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Tables without Primary Keys Report' , 'com.github.wicketoracle.app.report.tableswithoutpk.TablesWithoutPKReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_tables_without_pk_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_db_management' ) , ( select rpt_id from app_data.report where rpt_name = 'Tables without Primary Keys Report' ) );

/**
  * Locked sessions
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Session Lock Report' , 'com.github.wicketoracle.app.report.sessionlock.SessionLockReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_session_lock_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_db_management' ) , ( select rpt_id from app_data.report where rpt_name = 'Session Lock Report' ) );

/**
  * Least privileges
  */

insert into app_data.report ( rpt_id , rpt_name , rpt_page , dbrl_id ) 
values ( app_data.sq_rpt.nextval , 'Least privileges' , 'com.github.wicketoracle.app.report.leastprivileges.LeastPrivilegesReportPage' , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_least_privileges_report' ) );

insert into app_data.report_category_report ( rptc_id , rpt_id ) 
values ( ( select rptc_id from app_data.report_category where rptc_name = '&repcat_db_management' ) , ( select rpt_id from app_data.report where rpt_name = 'Least privileges' ) );