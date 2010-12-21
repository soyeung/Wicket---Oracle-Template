prompt defining roles and grants required to use pk_standard_user_password_mgr

--
-- define the roles
--

define role_std_user_password_mgr = ROLE_STD_USER_PASSWORD_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_std_user_password_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_std_user_password_mgr
/

grant &role_std_user_password_mgr to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_std_user_password_mgr
--

grant
      execute
   on
      app_user.pk_standard_user_password_mgr
   to
      &role_std_user_password_mgr
/