prompt creating roles for use of pksessionlockreport

define role_session_lock_report = ROLE_SESSION_LOCK_REPORT

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_session_lock_report'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_session_lock_report
/

grant &role_session_lock_report to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_session_lock_report
   to
      &role_session_lock_report
/