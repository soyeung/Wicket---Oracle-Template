prompt create roles and grants necessary such that the table 'app_data.language' can be managed via the ref data management facility

define role_rdm_language = ROLE_RDM_LANGUAGE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_language'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_language
/

grant &role_rdm_language to &usermgr_user with admin option
/

grant
      insert
    , update
   on
      app_data.language
   to
      &role_rdm_language
/

grant
      select
   on
      app_data.v_adm_rd_lng
   to
      &role_rdm_language
/

grant
      select
   on
      app_data.sq_lng
   to
      &role_rdm_language
/