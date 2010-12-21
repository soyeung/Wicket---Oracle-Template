prompt defining roles and grants required to use pk_standard_app_user_mgr

--
-- define the roles
--

define role_standard_app_user_mgr = ROLE_STANDARD_APP_USER_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_standard_app_user_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_standard_app_user_mgr
/

grant &role_standard_app_user_mgr to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_standard_app_user_manager
--

grant
      execute
   on
      app_user.pk_standard_app_user_mgr
   to
      &role_standard_app_user_mgr
/

grant
      execute
   on
      app_user.ty_standard_app_user
   to
      &role_standard_app_user_mgr
/

grant
      execute
   on
      app_user.tty_standard_app_user
   to
      &role_standard_app_user_mgr
/