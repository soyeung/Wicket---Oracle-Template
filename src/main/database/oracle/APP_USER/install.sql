grant select , insert on system.product_user_profile    to &usermgr_user
/

grant select on app_data.v_app_user                     to &usermgr_user
/

grant select on app_data.app_user_type_role             to &usermgr_user
/

grant select on app_data.v_usr_rd_uty                   to &usermgr_user
/

grant select on app_data.default_db_role                to &usermgr_user
/

grant select on app_data.db_role                        to &usermgr_user
/

grant select on app_data.v_proxy_app_user               to &usermgr_user
/

grant select on app_data.v_adm_rd_utr                   to &usermgr_user
/

grant select, update, insert on app_data.app_user          to &usermgr_user
/

grant select on app_data.sq_aur                            to &usermgr_user
/

grant select on app_data.restricted_sqlplus_function       to &usermgr_user
/

grant select on app_data.v_usr_rd_dbrl                     to &usermgr_user
/

grant select on app_data.v_usr_rd_mdbrl                    to &usermgr_user
/

grant select on app_data.v_app_user_role                   to &usermgr_user
/

grant insert on app_data.delegate_app_user                 to &usermgr_user
/

grant select on app_data.v_delegate_app_user               to &usermgr_user
/

grant select on app_data.v_usr_rd_dbrl                     to &usermgr_user
/

grant insert, update, select on app_data.standard_app_user to &usermgr_user
/

grant select on app_data.v_standard_app_user               to &usermgr_user
/

grant select on app_data.v_usr_rd_aurp                     to &usermgr_user
/

grant select on app_data.v_usr_rd_lng                      to &usermgr_user
/

grant select on app_data.v_adm_rd_lng                      to &usermgr_user
/

@&path_to_this_file/&user_mgr_system_path/types/ty_db_role

@&path_to_this_file/&user_mgr_system_path/types/tty_db_role

@&path_to_this_file/&user_mgr_system_path/types/ty_standard_app_user

@&path_to_this_file/&user_mgr_system_path/types/tty_standard_app_user

@&path_to_this_file/&user_mgr_system_path/types/ty_delegate_app_user

@&path_to_this_file/&user_mgr_system_path/types/tty_delegate_app_user

@&path_to_this_file/&user_mgr_system_path/packages/pk_set_app_user_roles.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_set_app_user_roles.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_password_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_password_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_role_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_role_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_app_user_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_app_user_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_app_user_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_app_user_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_user_role_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_user_role_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_role_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_role_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_password_mgr.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_password_mgr.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_creation.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_app_user_creation.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_creation.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_standard_user_creation.bdy

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_user_creation.hdr

@&path_to_this_file/&user_mgr_system_path/packages/pk_delegate_user_creation.bdy