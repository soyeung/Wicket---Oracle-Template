revoke create session from &utility_user
/

alter user &utility_user account lock
/

revoke create session from &data_user
/

alter user &data_user account lock
/

revoke create session from &log_user
/

alter user &log_user account lock
/

revoke create session from &exception_user
/

alter user &exception_user account lock
/

revoke drop any role , create session from &usermgr_user
/

alter user &usermgr_user account lock
/

alter user &usermgr_user default role none
/

revoke drop any role , create session from &refdata_user
/

alter user &refdata_user account lock
/

revoke drop any role , create session from &report_user
/

alter user &report_user account lock
/

revoke drop any role , create session from &ucp_user
/

alter user &ucp_user account lock
/