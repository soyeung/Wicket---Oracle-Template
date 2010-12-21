prompt creating package pk_standard_user_creation

create or replace package body app_user.pk_standard_user_creation
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;
    c_user          constant varchar2( 30 char ) := user;

    /**
      * name: fn_get_list_ref_data
      */

    function fn_get_list_ref_data
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_list_ref_data';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             select
                    /*+ result_cache */
                                        datasetkey
                                      , id
                                      , name
               from (

                           select
                                 'AURP'              datasetkey
                                , vaurp.aurp_id       id
                                , vaurp.aurp_name     name
                                , vaurp.aurp_order    ordering
                             from
                                  app_data.v_usr_rd_aurp vaurp

                        union all

                           select
                                  'UTY'               datasetkey
                                , vuty.uty_id         id
                                , vuty.uty_name       name
                                , vuty.uty_order      ordering
                             from
                                  app_data.v_usr_rd_uty vuty

                        union all

                           select
                                  'LNG'               datasetkey
                                , vlng.lng_id         id
                                , vlng.lng_name       name
                                , vlng.lng_order      ordering
                             from
                                  app_data.v_usr_rd_lng vlng
                    )
           order by
                    datasetkey  asc
                  , ordering    asc
                  , name        asc
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

    end fn_get_list_ref_data;

    /**
      * name: pr_create_standard_user
      */

    procedure pr_create_standard_user
    (
        p_username in app_data.v_app_user.aur_username%type
    ,   p_password in varchar2
    ,   p_uty_id   in app_data.v_usr_rd_uty.uty_id%type
    ,   p_aurp_id  in app_data.v_usr_rd_aurp.aurp_id%type
    ,   p_lng_id   in app_data.v_usr_rd_lng.lng_id%type
    )
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'pr_create_standard_user';

        v_aur_id app_data.v_app_user.aur_id%type;

        /**
          * name: pr_register_standard_user
          */

        procedure pr_register_standard_user
        is
        begin
    
            insert
              into
                   app_data.standard_app_user
                 (
                   aur_id
                 , lng_id
                 )
            values
                 (
                   v_aur_id
                 , p_lng_id
                 );
    
        end pr_register_standard_user;

    begin

        pk_app_user_creation.pr_create_user
        (
            p_username => p_username
        ,   p_password => p_password
        ,   p_uty_id   => p_uty_id
        ,   p_aurp_id  => p_aurp_id
        ,   p_aur_id   => v_aur_id
        );

        pr_register_standard_user;

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => '(p_username => ' || p_username || '; p_uty_id => ' || p_uty_id || '; p_aurp_id => ' || p_aurp_id || ')'
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_create_standard_user;

end pk_standard_user_creation;
/

sho err