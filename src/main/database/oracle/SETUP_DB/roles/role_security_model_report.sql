prompt creating roles for use of app_report.pk_security_model_report

define role_security_model_report = ROLE_SECURITY_MODEL_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_security_model_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_security_model_report
/

grant &role_security_model_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_security_model_report
   to
      &role_security_model_report
/