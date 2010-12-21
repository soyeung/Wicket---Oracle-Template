prompt defining roles and grants required to use pk_delegate_app_user_mgr

--
-- define the roles
--

define role_standard_app_user_creator = ROLE_STANDARD_APP_USER_CREATOR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_standard_app_user_creator'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_standard_app_user_creator
/

grant &role_standard_app_user_creator to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_delegate_user_manager
--

grant
      execute
   on
      app_user.pk_standard_user_creation
   to
      &role_standard_app_user_creator
/