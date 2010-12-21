prompt creating reference data manager system

grant select on app_data.ref_data_structure             to &refdata_user
/

grant select on app_data.coded_list_ref_data            to &refdata_user
/

grant select on app_data.v_ref_data_structure           to &refdata_user
/

grant select on app_data.v_editable_ref_data_structure  to &refdata_user
/

grant select on app_data.v_subdiv_ref_data              to &refdata_user
/

grant select on app_data.v_list_ref_data                to &refdata_user
/

grant select on app_data.v_update_only_list_ref_data    to &refdata_user
/

grant select on app_data.v_coded_list_ref_data          to &refdata_user
/

grant select on app_data.v_app_user_role                to &refdata_user
/

grant select on app_data.ref_data_structure_link        to &refdata_user
/

grant select on app_data.list_intersection_ref_data     to &refdata_user
/

grant select on app_data.v_adm_rd_dbrl                  to &refdata_user
/

grant select on app_data.sq_rds                         to &refdata_user
/

grant select on app_data.v_ref_data_structure           to &refdata_user
/

grant select, insert on app_data.ref_data_structure     to &refdata_user
/

grant select, insert on app_data.coded_list_ref_data    to &refdata_user
/

grant select, insert on app_data.list_ref_data          to &refdata_user
/

grant select, insert on app_data.update_only_list_ref_data to &refdata_user
/

grant select, insert on app_data.ref_data_structure_link   to &refdata_user
/

grant select, insert on app_data.subdiv_ref_data            to &refdata_user
/

grant select, insert on app_data.list_intersection_ref_data to &refdata_user
/

grant select, insert on app_data.db_role to &refdata_user
/

grant select on app_data.db_role_category to &refdata_user
/

grant select on app_data.sq_dbrl to &refdata_user
/

grant select, insert on app_data.default_db_role to &refdata_user
/

grant insert on app_data.manageable_db_role to &refdata_user
/

grant
      insert
    , select
    , delete
   on
      app_data.editable_ref_data_structure
   to
      &refdata_user
/

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_coded_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_coded_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_intersection_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_intersection_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_list_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_ref_data_structure

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_ref_data_structure

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_subdiv_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_subdiv_ref_data

@&path_to_this_file\&ref_data_mgr_system_path\types\ty_db_role

@&path_to_this_file\&ref_data_mgr_system_path\types\tty_db_role

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_ref_data_column_names.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_ref_data_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_ref_data_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_sql.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_sql.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_update_only_list_sql.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_update_only_list_sql.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_update_only_list_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_update_only_list_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_subdivision_sql.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_subdivision_sql.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_subdivision_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_list_subdivision_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_coded_list_sql.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_coded_list_sql.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_coded_list_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_coded_list_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_intersection_list_sql.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_intersection_list_sql.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_intersection_list_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_intersection_list_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_configure_ref_data.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_configure_ref_data.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_ref_data_structure_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_ref_data_structure_mgr.bdy

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_db_role_mgr.hdr

@&path_to_this_file\&ref_data_mgr_system_path\packages\pk_db_role_mgr.bdy