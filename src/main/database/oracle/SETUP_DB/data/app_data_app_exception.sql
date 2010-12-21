prompt register custom exceptions

insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20000 , 'UNKNOWN_USER'               , 'The user is unknown' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20001 , 'BAD_TRACE_DIR'              , 'Tracefile locations cannot include the "=" character' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20002 , 'DANGEROUS_USERNAME'         , 'The username supplied was not safe' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20003 , 'DANGEROUS_PASSWORD'         , 'The password supplied was not safe' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20004 , 'CANNOT_DISABLE_OWN_ACCOUNT' , 'It is not possible to disable your own account' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20005 , 'DANGEROUS_ROLENAME'         , 'The role supplied is considered to be unsafe' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20006 , 'OPTIMISTIC_LOCKING'         , 'An optimistic locking conflict has arisen' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20007 , 'NON_EDITABLE_REF_DATA'      , 'An attempt to modify non-editable reference data was made' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20008 , 'SIMILAR_USERNAME_PASSWORD'  , 'The username and password are too similar' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20009 , 'PASSWORD_TOO_SMALL'         , 'The password is not long enough' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20010 , 'BANNED_PASSWORD'            , 'The requested password is banned from use' );
insert into app_data.app_exception aex ( aex.aex_id , aex.aex_code , aex.aex_message ) values ( -20011 , 'MISSING_PASSWORD_CONTENT'   , 'The password must contain at least 1 digit, 1 character and 1 punctuation' );
commit;