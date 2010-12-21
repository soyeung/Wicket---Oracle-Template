prompt creating package body pk_ref_data_mgr

create or replace package body app_refdata.pk_ref_data_mgr
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_refdata_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;
    c_current_user  constant varchar2( 30 char ) := user;

    /**
      * name: fn_get_data_structure_list
      */

    function fn_get_data_structure_list
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_data_structure_list';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             with
                  data
             as
             (
                    select
                           null                           rds_parent_id
                         , vrds.rds_id                    rds_child_id
                         , vrds.dbrl_code
                         , vrds.rds_description
                         , vrds.rdt_code
                         , vrds.rds_is_editable
                      from
                                app_data.v_ref_data_structure  vrds
                           join app_data.v_app_user_role       vaurr on vrds.dbrl_code = vaurr.dbrl_code
                     where
                               vaurr.aur_username = c_current_user
                           and vrds.rds_id not in (
                                                    select
                                                           rdsl.rds_child_id
                                                      from
                                                           app_data.ref_data_structure_link rdsl
                                                  )

                 union all

                    select
                           rdsl.rds_id                     rds_parent_id
                         , rdsl.rds_child_id               rds_child_id
                         , vrds.dbrl_code
                         , vrds.rds_description
                         , vrds.rdt_code
                         , vrds.rds_is_editable
                      from
                                app_data.ref_data_structure_link  rdsl
                           join app_data.v_ref_data_structure     vrds  on rdsl.rds_child_id = vrds.rds_id
                           join app_data.v_app_user_role          vaurr on vrds.dbrl_code    = vaurr.dbrl_code
                     where
                           vaurr.aur_username = c_current_user
             )
                     select
                            d.rds_child_id      id
                          , d.dbrl_code         code
                          , d.rds_description   descr
                          , d.rds_is_editable   is_editable
                          , d.rdt_code          rdt_code
                          , level               hlevel
                       from
                            data d
                 start with
                            d.rds_parent_id is null
                 connect by
                            prior d.rds_child_id = d.rds_parent_id
             order siblings
                            by
                               d.rds_description asc
                          ;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_data_structure_list;

    /**
      * name : fn_is_editable_data_structure
      */

    function fn_is_editable_data_structure
    (
        p_rds_id in app_data.v_editable_ref_data_structure.rds_id%type
    )
    return boolean
    is

        v_is_editable varchar2( 1 char ) := 'N';

    begin

        select
               'Y'
               into
               v_is_editable
          from
               app_data.v_editable_ref_data_structure erds
         where
               erds.rds_id = p_rds_id
             ;

        return true;

    exception

        when no_data_found then

            return false;

    end fn_is_editable_data_structure;

    /**
      * name : fn_get_list_metadata
      */

    function fn_get_list_metadata
    (
        p_rds_id in app_data.v_ref_data_structure.rds_id%type
    )
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             select
                    max( case when dtc.column_name = rds.rds_alias || '_CODE' then dtc.data_length end ) max_code_length
                  , max( case when dtc.column_name = rds.rds_alias || '_NAME' then dtc.data_length end ) max_name_length
               from
                         app_data.ref_data_structure  rds
                    join sys.dba_tab_cols             dtc  on (
                                                                    rds.rds_owning_schema = dtc.owner
                                                                and rds.rds_table_name    = dtc.table_name
                                                              )
              where
                    dtc.column_name in (
                                         rds.rds_alias || '_CODE'
                                       , rds.rds_alias || '_NAME'
                                       )
                and rds.rds_id = p_rds_id
                  ;

        return v_data_set;

    end fn_get_list_metadata;

end pk_ref_data_mgr;
/

sho err