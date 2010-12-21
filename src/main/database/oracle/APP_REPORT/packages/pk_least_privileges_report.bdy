prompt creating package header app_report.pk_least_privileges_report

create or replace package body app_report.pk_least_privileges_report
as

    /**
      * name : fn_get_report
      */
    function fn_get_report
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
               select
                      dba_tab_privs.grantee    privilege_holder
                    , dba_tab_privs.owner      object_owner
                    , dba_tab_privs.table_name object_name
                    , dba_tab_privs.privilege  granted_privilege
                 from
                      sys.dba_tab_privs dba_tab_privs
                where
                      dba_tab_privs.grantee like 'APP%'
                  and not exists (

                                   select
                                          null
                                     from
                                          sys.dba_dependencies dba_dependencies
                                    where
                                          dba_dependencies.referenced_owner = dba_tab_privs.owner
                                      and dba_dependencies.referenced_name  = dba_tab_privs.table_name

                                 )
             order by
                      privilege_holder  asc
                    , object_owner      asc
                    , object_name       asc
                    , granted_privilege asc
                    ;

        return v_data_set;

    end fn_get_report;

end pk_least_privileges_report;
/

sho err