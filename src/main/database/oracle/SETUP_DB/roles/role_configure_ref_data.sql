prompt defining roles and grants required to use pk_ref_data_mgr

--
-- define the roles
--

define role_configure_ref_data = ROLE_CONFIGURE_REF_DATA

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_configure_ref_data'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_configure_ref_data
/

grant &role_configure_ref_data to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_configure_ref_data_mgr
--

grant 
      execute 
   on 
      app_refdata.pk_configure_ref_data
   to 
      &role_configure_ref_data
/

grant 
      execute 
   on 
      app_refdata.ty_ref_data_structure 
   to 
      &role_configure_ref_data
/

grant 
      execute 
   on 
      app_refdata.tty_ref_data_structure 
   to 
      &role_configure_ref_data
/