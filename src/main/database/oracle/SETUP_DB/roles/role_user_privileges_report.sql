prompt creating roles for use of app_report.pk_user_privileges_report

define role_user_privileges_report = ROLE_USER_PRIVILEGES_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_user_privileges_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_user_privileges_report
/

grant &role_user_privileges_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_user_privileges_report
   to
      &role_user_privileges_report
/