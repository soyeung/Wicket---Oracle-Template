prompt creating roles - allow user to view debug information / tools

define role_view_debug_info = ROLE_VIEW_DEBUG_INFO

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_view_debug_info'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_view_debug_info
/

grant &role_view_debug_info to &usermgr_user with admin option
/