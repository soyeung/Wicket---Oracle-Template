prompt create roles and grants necessary such that the table 'app_data.app_user_profile' can be managed via the ref data management facility

define role_rdm_app_user_profile = ROLE_RDM_APP_USER_PROFILE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_app_user_profile'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_app_user_profile
/

grant &role_rdm_app_user_profile to &usermgr_user with admin option
/

grant
      insert
    , update
   on
      app_data.app_user_profile
   to
      &role_rdm_app_user_profile
/

grant
      select
   on
      app_data.v_adm_rd_aurp
   to
      &role_rdm_app_user_profile
/

grant
      select
   on
      app_data.sq_aurp
   to
      &role_rdm_app_user_profile
/