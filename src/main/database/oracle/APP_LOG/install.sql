grant select on app_data.sq_act                     to &log_user
/

grant select on app_data.sq_erl                     to &log_user
/

grant select on app_data.sq_msl                     to &log_user
/

grant select on app_data.sq_lgn                     to &log_user
/

grant select, insert on app_data.error_log          to &log_user
/

grant select, insert on app_data.message_log        to &log_user
/

grant select, insert on app_data.activity_log       to &log_user
/

grant select, insert on app_data.authentication_log to &log_user
/

grant select         on app_data.v_app_user         to &log_user
/

@&path_to_this_file/&logging_system_path/packages/pk_log_installation_only