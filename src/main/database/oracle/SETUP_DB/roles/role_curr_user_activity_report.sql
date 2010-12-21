prompt defining roles and grants required to use app_report.pkcurrentuseractivity

--
-- define the roles
--

define role_curr_user_activity_report = ROLE_CURR_USER_ACTIVITY_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_curr_user_activity_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_curr_user_activity_report
/

grant &role_curr_user_activity_report to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_curr_user_activity_report
--

grant
      execute
   on
      app_report.pk_current_activity_report
   to
      &role_curr_user_activity_report
/