prompt create roles and grants necessary such that the table 'app_data.db_role' can be managed via the ref data management facility

define role_rdm_db_role = ROLE_RDM_DB_ROLE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_db_role'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_db_role
/

grant &role_rdm_db_role to &usermgr_user with admin option
/

grant
      update
   on
      app_data.db_role
   to
      &role_rdm_db_role
/

grant
      select
   on
      app_data.v_adm_rd_dbrl
   to
      &role_rdm_db_role
/

grant
      select
   on
      app_data.sq_dbrl
   to
      &role_rdm_db_role
/