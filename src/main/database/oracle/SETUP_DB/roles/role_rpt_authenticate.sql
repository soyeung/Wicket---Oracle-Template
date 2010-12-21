prompt creating roles for use of pkauthenticationreport

define role_authenticate_report_user = ROLE_RPT_AUTHENTICATE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_authenticate_report_user'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_authenticate_report_user
/

grant &role_authenticate_report_user to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_authentication_report
   to
      &role_authenticate_report_user
/