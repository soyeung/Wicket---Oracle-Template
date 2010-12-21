prompt create 'basic' user type

define uty_basic = Basic
insert into app_data.app_user_type      ( uty_id , uty_name ) values ( app_data.sq_uty.nextval , '&uty_basic' );
insert into app_data.app_user_type_role ( uty_id , dbrl_id  ) values ( ( select uty_id from app_data.app_user_type where uty_name = '&uty_basic' ) , ( select dbrl_id from app_data.db_role where dbrl_code = '&role_connect' ) );
commit;