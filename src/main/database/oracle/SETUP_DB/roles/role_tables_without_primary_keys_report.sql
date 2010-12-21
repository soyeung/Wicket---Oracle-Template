prompt creating roles for use of pk_unindexed_fk_report

define role_tables_without_pk_report = ROLE_TABLES_WITHOUT_PK_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_tables_without_pk_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_tables_without_pk_report
/

grant &role_tables_without_pk_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_tables_without_pk_report
   to
      &role_tables_without_pk_report
/