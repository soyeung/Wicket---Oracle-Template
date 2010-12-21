prompt create roles and grants necessary such that the table 'app_data.app_user_type' can be managed via the ref data management facility

define role_rdm_app_user_type = ROLE_RDM_APP_USER_TYPE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_app_user_type'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_app_user_type
/

grant &role_rdm_app_user_type to &usermgr_user with admin option
/

grant
      update
    , insert
   on
      app_data.app_user_type
   to
      &role_rdm_app_user_type
/

grant
      select
   on
      app_data.v_adm_rd_uty
   to
      &role_rdm_app_user_type
/

grant
      select
   on
      app_data.sq_uty
   to
      &role_rdm_app_user_type
/