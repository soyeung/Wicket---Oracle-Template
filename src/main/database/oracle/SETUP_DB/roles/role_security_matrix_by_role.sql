prompt creating roles for use of pksecuritymatrixreportbyrole

define role_security_matrix_by_role = ROLE_SECURITY_MATRIX_BY_ROLE

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_security_matrix_by_role'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_security_matrix_by_role
/

grant &role_security_matrix_by_role to &usermgr_user with admin option
/

grant
      execute
   on
      app_report.pk_role_matrix_by_role_report
   to
      &role_security_matrix_by_role
/