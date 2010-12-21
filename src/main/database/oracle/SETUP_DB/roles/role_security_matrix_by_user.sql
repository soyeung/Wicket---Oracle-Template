prompt creating roles for use of pksecuritymatrixreportbyuser

define role_security_matrix_by_user = ROLE_SECURITY_MATRIX_BY_USER

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_security_matrix_by_user'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_security_matrix_by_user
/

grant &role_security_matrix_by_user to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_role_matrix_by_user_report
   to
      &role_security_matrix_by_user
/