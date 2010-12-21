prompt create roles and grants necessary such that the table 'app_data.report' can be managed via the ref data management facility

define role_rdm_report = ROLE_RDM_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_report
/

grant &role_rdm_report to &usermgr_user with admin option
/

grant
      insert
    , update
   on
      app_data.report
   to
      &role_rdm_report
/

grant
      select
   on
      app_data.v_adm_rd_rpt
   to
      &role_rdm_report
/

grant
      select
   on
      app_data.sq_rpt
   to
      &role_rdm_report
/