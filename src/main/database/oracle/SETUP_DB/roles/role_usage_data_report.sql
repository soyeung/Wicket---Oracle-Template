prompt creating roles for use of pksessionlockreport

define role_usage_data_report = ROLE_USAGE_DATA_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_usage_data_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_usage_data_report
/

grant &role_usage_data_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_usage_data_report
   to
      &role_usage_data_report
/