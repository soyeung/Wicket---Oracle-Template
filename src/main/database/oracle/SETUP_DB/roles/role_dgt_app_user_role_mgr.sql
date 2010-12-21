prompt defining roles and grants required to use pk_delegate_user_role_mgr

define role_dgt_app_user_role_mgr = ROLE_DGT_APP_USER_ROLE_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_dgt_app_user_role_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_dgt_app_user_role_mgr
/

grant &role_dgt_app_user_role_mgr to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_dgt_app_user_role_mgr
--

grant
      execute
   on
      app_user.pk_delegate_user_role_mgr
   to
      &role_dgt_app_user_role_mgr
/

grant
      execute
   on
      app_user.ty_db_role
   to
      &role_dgt_app_user_role_mgr
/

grant
      execute
   on
      app_user.tty_db_role
   to
      &role_dgt_app_user_role_mgr
/