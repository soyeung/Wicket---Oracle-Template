-- ddl is ordered like this because of dependencies

@&path_to_this_file/&data_user/tables/table_error_log

@&path_to_this_file/&data_user/tables/table_message_log

@&path_to_this_file/&data_user/tables/table_app_exception

--
-- table clusters / general
--

@&path_to_this_file/&data_user/clusters/cluster_cl_app_user_type

@&path_to_this_file/&data_user/clusters/cluster_cl_db_role

@&path_to_this_file/&data_user/clusters/cluster_cl_app_user_profile

@&path_to_this_file/&data_user/clusters/cluster_cl_ref_data

--
-- tables / users
--

@&path_to_this_file/&data_user/tables/table_restricted_sqlplus_function

@&path_to_this_file/&data_user/tables/table_language

@&path_to_this_file/&data_user/tables/table_app_user

@&path_to_this_file/&data_user/tables/table_authentication_log

@&path_to_this_file/&data_user/tables/table_delegate_app_user

@&path_to_this_file/&data_user/tables/table_proxy_app_user

@&path_to_this_file/&data_user/tables/table_standard_app_user

--
-- views
--

@&path_to_this_file/&data_user/views/view_v_authentication_log

--
-- tables / users / db roles
--

@&path_to_this_file/&data_user/tables/table_app_user_type

@&path_to_this_file/&data_user/tables/table_db_role_category

@&path_to_this_file/&data_user/tables/table_db_role

@&path_to_this_file/&data_user/tables/table_default_db_role

@&path_to_this_file/&data_user/tables/table_manageable_db_role

@&path_to_this_file/&data_user/tables/table_app_user_type_role

@&path_to_this_file/&data_user/tables/table_app_user_profile

--
-- views / user mgr
--

@&path_to_this_file/&data_user/views/view_v_app_user

@&path_to_this_file/&data_user/views/view_v_proxy_app_user

@&path_to_this_file/&data_user/views/view_v_standard_app_user

@&path_to_this_file/&data_user/views/view_v_delegate_app_user

@&path_to_this_file/&data_user/views/view_v_app_user_role

--
-- tables / ref data
--

@&path_to_this_file/&data_user/tables/table_ref_data_structure

@&path_to_this_file/&data_user/tables/table_editable_ref_data_structure

@&path_to_this_file/&data_user/tables/table_list_ref_data

@&path_to_this_file/&data_user/tables/table_subdiv_ref_data

@&path_to_this_file/&data_user/tables/table_coded_list_ref_data

@&path_to_this_file/&data_user/tables/table_list_intersection_ref_data

@&path_to_this_file/&data_user/tables/table_update_only_list_ref_data

@&path_to_this_file/&data_user/tables/table_ref_data_structure_link

@&path_to_this_file/&data_user/tables/table_activity_log

--
-- views / ref data
--

@&path_to_this_file/&data_user/views/view_v_ref_data_structure

@&path_to_this_file/&data_user/views/view_v_editable_ref_data_structure

@&path_to_this_file/&data_user/views/view_v_subdiv_ref_data

@&path_to_this_file/&data_user/views/view_v_list_ref_data

@&path_to_this_file/&data_user/views/view_v_coded_list_ref_data

@&path_to_this_file/&data_user/views/view_v_update_only_list_ref_data

--
-- tables / report system
--

@&path_to_this_file/&data_user/tables/table_report

@&path_to_this_file/&data_user/tables/table_report_category

@&path_to_this_file/&data_user/tables/table_report_category_link

@&path_to_this_file/&data_user/tables/table_report_category_report

--
-- views / ref data
--

@&path_to_this_file/&data_user/views/view_v_adm_rd_aurp

@&path_to_this_file/&data_user/views/view_v_adm_rd_dbrl

@&path_to_this_file/&data_user/views/view_v_adm_rd_lng

@&path_to_this_file/&data_user/views/view_v_adm_rd_mdbrl

@&path_to_this_file/&data_user/views/view_v_adm_rd_rpt

@&path_to_this_file/&data_user/views/view_v_adm_rd_rptc

@&path_to_this_file/&data_user/views/view_v_adm_rd_rptcr

@&path_to_this_file/&data_user/views/view_v_adm_rd_utr

@&path_to_this_file/&data_user/views/view_v_adm_rd_uty

@&path_to_this_file/&data_user/views/view_v_adm_rd_dbrlc

@&path_to_this_file/&data_user/views/view_v_usr_rd_aurp

@&path_to_this_file/&data_user/views/view_v_usr_rd_dbrl

@&path_to_this_file/&data_user/views/view_v_usr_rd_lng

@&path_to_this_file/&data_user/views/view_v_usr_rd_mdbrl

@&path_to_this_file/&data_user/views/view_v_usr_rd_rpt

@&path_to_this_file/&data_user/views/view_v_usr_rd_rptc

@&path_to_this_file/&data_user/views/view_v_usr_rd_rptcr

@&path_to_this_file/&data_user/views/view_v_usr_rd_saur

@&path_to_this_file/&data_user/views/view_v_usr_rd_utr

@&path_to_this_file/&data_user/views/view_v_usr_rd_uty

@&path_to_this_file/&data_user/views/view_v_usr_rd_dbrlc