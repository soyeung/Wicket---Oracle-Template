prompt creating create db session role

define role_connect = ROLE_CREATE_SESSION

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_connect'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_connect
/

grant create session to &role_connect
/

grant &role_connect to &usermgr_user with admin option
/