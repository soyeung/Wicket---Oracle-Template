prompt creating roles for use of pkreporting

define role_report_user = ROLE_REPORT_USER

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_report_user'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_report_user
/

grant &role_report_user to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_reporting
   to
      &role_report_user
/