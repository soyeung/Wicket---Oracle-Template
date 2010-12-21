prompt creating roles for use of pkunindexedforeignkeyreport

define role_unindexed_fk_report = ROLE_UNINDEXED_FK_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_unindexed_fk_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_unindexed_fk_report
/

grant &role_unindexed_fk_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_unindexed_fk_report
   to
      &role_unindexed_fk_report
/