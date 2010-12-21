prompt create roles and grants necessary such that the table 'list_intersection_ref_data' can be managed via the ref data management facility

define role_rdm_app_user_type_role = ROLE_RDM_APP_USER_TYPE_ROLE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_app_user_type_role'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_app_user_type_role
/

grant &role_rdm_app_user_type_role to &usermgr_user with admin option
/

grant 
      select
   on
      app_data.v_adm_rd_uty
   to
      &role_rdm_app_user_type_role
/

grant 
      select
   on
      app_data.v_adm_rd_dbrl
   to
      &role_rdm_app_user_type_role
/

grant
      select 
    , insert
    , delete
   on
      app_data.app_user_type_role
   to
      &role_rdm_app_user_type_role
/