prompt creating package body app_report.pk_user_privileges_report

create or replace package body app_report.pk_user_privileges_report
as

    /**
      * name : fn_get_list_ref_data
      */
    function fn_get_list_ref_data
    return sys_refcursor
    is
        v_data_set sys_refcursor;
    begin

        open
             v_data_set
         for
               select
                      /*+ result_cache */
                                          'AUR'             datasetkey
                                        , saur.aur_id       id
                                        , saur.aur_username name
                 from
                      app_data.v_usr_rd_saur saur
             order by
                      name asc
                    ;

        return v_data_set;

    end fn_get_list_ref_data;

    /**
      * name : fn_get_report
      */
    function fn_get_report
    (
        p_aur_id in app_data.v_usr_rd_saur.aur_id%type
    )
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
                        , saur.aur_username                     grantee
                     from
                          app_data.v_usr_rd_saur saur
                    where
                          saur.aur_id = p_aur_id

                union all

                   select
                          dba_role_privs.grantee                granted_role
                        , dba_role_privs.granted_role           grantee
                     from
                          dba_role_privs dba_role_privs

                union all

                   select
                          dba_tab_privs.grantee                                                                              granted_role
                        , dba_tab_privs.owner || '.' || dba_tab_privs.table_name || ' ( ' || dba_tab_privs.privilege || ' )' grantee
                     from
                          sys.dba_tab_privs dba_tab_privs

                union all

                   select
                          dba_sys_privs.grantee                 granted_role
                        , dba_sys_privs.privilege               grantee
                     from
                          sys.dba_sys_privs dba_sys_privs

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
                               object_privilege asc
                             ;

        return v_data_set;

    end fn_get_report;

end pk_user_privileges_report;
/

sho err