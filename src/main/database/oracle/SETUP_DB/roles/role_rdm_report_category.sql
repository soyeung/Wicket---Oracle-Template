prompt create roles and grants necessary such that the table 'app_data.report_category' can be managed via the ref data management facility

define role_rdm_report_category = ROLE_RDM_REPORT_CATEGORY

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_rdm_report_category'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_rdm_report_category
/

grant &role_rdm_report_category to &usermgr_user with admin option
/

grant
      insert
    , update
   on
      app_data.report_category
   to
      &role_rdm_report_category
/

grant
      select
   on
      app_data.v_adm_rd_rptc
   to
      &role_rdm_report_category
/

grant
      select
   on
      app_data.sq_rptc
   to
      &role_rdm_report_category
/