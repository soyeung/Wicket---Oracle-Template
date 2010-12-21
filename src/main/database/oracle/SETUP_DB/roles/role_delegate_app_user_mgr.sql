prompt defining roles and grants required to use pk_delegate_app_user_mgr

--
-- define the roles
--

define role_delegate_app_user_mgr = ROLE_DELEGATE_APP_USER_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_delegate_app_user_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_delegate_app_user_mgr
/

grant &role_delegate_app_user_mgr to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_delegate_user_manager
--

grant
      execute
   on
      app_user.pk_delegate_app_user_mgr
   to
      &role_delegate_app_user_mgr
/

grant
      execute
   on
      app_user.ty_delegate_app_user
   to
      &role_delegate_app_user_mgr
/

grant
      execute
   on
      app_user.tty_delegate_app_user
   to
      &role_delegate_app_user_mgr
/