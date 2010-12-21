prompt create package app_report.pk_security_model_report

create or replace package body app_report.pk_security_model_report
as

    /**
      * name: fn_get_report
      */
    function fn_get_report
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             with
                role_tree
             as
             (

                   select
                          null                                  granted_role
                        , dbrl.dbrl_code                        grantee
                     from
                          app_data.v_adm_rd_dbrl dbrl

                union all

                   select
                          dba_sys_privs.grantee                 granted_role
                        , dba_sys_privs.privilege               grantee
                     from
                          sys.dba_sys_privs dba_sys_privs

                union all

                   select
                          role_role_privs.role                  granted_role
                        , role_role_privs.granted_role          grantee
                     from
                          sys.role_role_privs role_role_privs

                union all

                   select
                          dba_tab_privs.grantee                                                                              granted_role
                        , dba_tab_privs.owner || '.' || dba_tab_privs.table_name || ' ( ' || dba_tab_privs.privilege || ' )' grantee
                     from
                          sys.dba_tab_privs dba_tab_privs
             )
                        select
                              role_tree.grantee object_privilege
                            , level             hlevel
                          from
                               role_tree role_tree
                    start with
                               role_tree.granted_role is null
                    connect by
                               prior role_tree.grantee = role_tree.granted_role
             order siblings by
                               grantee asc
                             ;

        return v_data_set;

    end fn_get_report;

end pk_security_model_report;
/

sho err