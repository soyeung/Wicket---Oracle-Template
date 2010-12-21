prompt defining roles and grants required to use pk_ref_data_mgr

--
-- define the roles
--

define role_ref_data_mgr = ROLE_REF_DATA_MGR

begin
  for i in (
             select
                    dba_rol.role
               from
                    sys.dba_roles dba_rol
              where
                    dba_rol.role = '&role_ref_data_mgr'
           )
  loop
    execute immediate 'drop role ' || i.role || ' cascade';
  end loop;
end;
/

create role &role_ref_data_mgr
/

grant &role_ref_data_mgr to &usermgr_user with admin option
/

--
-- define the priviliges conferred by role_ref_data_mgr
--

grant execute on app_refdata.pk_ref_data_mgr to &role_ref_data_mgr
/

grant execute on app_refdata.ty_coded_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.tty_coded_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.ty_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.tty_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.ty_subdiv_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.tty_subdiv_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.ty_intersection_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.tty_intersection_list_ref_data to &role_ref_data_mgr
/

grant execute on app_refdata.pk_list_sql to &role_ref_data_mgr
/

grant execute on app_refdata.pk_list_mgr to &role_ref_data_mgr
/

grant execute on app_refdata.pk_update_only_list_sql to &role_ref_data_mgr
/

grant execute on app_refdata.pk_update_only_list_mgr to &role_ref_data_mgr
/

grant execute on app_refdata.pk_list_subdivision_sql to &role_ref_data_mgr
/

grant execute on app_refdata.pk_list_subdivision_mgr to &role_ref_data_mgr
/

grant execute on app_refdata.pk_coded_list_sql to &role_ref_data_mgr
/

grant execute on app_refdata.pk_coded_list_mgr to &role_ref_data_mgr
/

grant execute on app_refdata.pk_intersection_list_sql to &role_ref_data_mgr
/

grant execute on app_refdata.pk_intersection_list_mgr to &role_ref_data_mgr
/