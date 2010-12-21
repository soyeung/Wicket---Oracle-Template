prompt creating roles for use of ucp management system

define role_ucp_mgr = ROLE_UCP_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_ucp_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_ucp_mgr
/

grant &role_ucp_mgr to &usermgr_user with admin option
/