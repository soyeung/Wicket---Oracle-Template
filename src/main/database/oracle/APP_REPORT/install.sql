grant select on app_data.report_category_link    to &report_user
/

grant select on app_data.report_category_report  to &report_user
/

grant select on app_data.v_app_user              to &report_user
/

grant select on app_data.v_authentication_log    to &report_user
/

grant select on app_data.v_adm_rd_dbrl           to &report_user
/

grant select on app_data.v_usr_rd_dbrl           to &report_user
/

grant select on app_data.v_usr_rd_rpt            to &report_user
/

grant select on app_data.v_usr_rd_rptc           to &report_user
/

grant select on app_data.v_app_user_role         to &report_user
/

grant select on app_data.v_usr_rd_saur           to &report_user
/

grant select on app_data.app_user                to &report_user
/

grant select on app_data.authentication_log      to &report_user
/

@&path_to_this_file/&reporting_system_path/types/ty_report

@&path_to_this_file/&reporting_system_path/types/tty_report

@&path_to_this_file/&reporting_system_path/packages/pk_reporting.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_reporting.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_authentication_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_authentication_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_role_matrix_by_role_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_role_matrix_by_role_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_role_matrix_by_user_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_role_matrix_by_user_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_unindexed_fk_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_unindexed_fk_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_current_activity_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_current_activity_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_tables_without_pk_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_tables_without_pk_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_session_lock_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_session_lock_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_usage_data_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_usage_data_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_security_model_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_security_model_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_user_privileges_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_user_privileges_report.bdy

@&path_to_this_file/&reporting_system_path/packages/pk_least_privileges_report.hdr

@&path_to_this_file/&reporting_system_path/packages/pk_least_privileges_report.bdy