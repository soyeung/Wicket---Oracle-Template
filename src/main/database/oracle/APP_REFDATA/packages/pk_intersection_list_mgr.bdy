prompt creating package body app_refdata.pk_intersection_list_mgr

create or replace package body app_refdata.pk_intersection_list_mgr
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_refdata_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;

    /**
      * name : fn_get_parent_data
      */

    function fn_get_parent_data
    (
        p_rds_id in app_data.v_list_ref_data.rds_id%type
    )
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             app_refdata.pk_intersection_list_sql.fn_get_parent_data_sql ( p_rds_id => p_rds_id )
           ;

        return v_data_set;

    end fn_get_parent_data;

    /**
      * name : fn_get_child_data
      */

    function fn_get_child_data
    (
        p_rds_id    in app_data.v_list_ref_data.rds_id%type
    ,   p_parent_id in int
    )
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

         open
              v_data_set
          for
              app_refdata.pk_intersection_list_sql.fn_get_child_data_sql ( p_rds_id => p_rds_id )
        using
              p_parent_id
            ;

        return v_data_set;

    end fn_get_child_data;

    /**
      * name : pr_update
      */

    procedure pr_update
    (
      p_rds_id    in app_data.v_list_ref_data.rds_id%type
    , p_parent_id in int
    , p_dataset   in app_refdata.tty_intersection_list_ref_data
    )
    is

        c_procedure_name constant varchar2(30 char) := 'pr_update';
        c_start_time     constant number            := dbms_utility.get_time;

    begin

        if app_refdata.pk_ref_data_mgr.fn_is_editable_data_structure
           (
               p_rds_id => p_rds_id
           )
        then

            execute immediate app_refdata.pk_intersection_list_sql.fn_get_delete_sql( p_rds_id => p_rds_id ) using p_parent_id , p_dataset;

            execute immediate app_refdata.pk_intersection_list_sql.fn_get_insert_sql( p_rds_id => p_rds_id ) using p_parent_id , p_dataset , p_parent_id;

            app_log.pk_log.pr_log_activity
            (
                p_package_owner  => c_package_owner
            ,   p_package_name   => c_package_name
            ,   p_procedure_name => c_procedure_name
            ,   p_start_time     => c_start_time
            ,   p_end_time       => dbms_utility.get_time
            );

        else

            app_log.pk_log.pr_log_error
            (
                p_sqlcode           => app_exception.pk_exception_codes.c_non_editable_ref_data
            ,   p_sqlerrm           => app_exception.pk_exception_codes.c_non_editable_ref_data
            ,   p_calling_procedure => c_procedure_name
            ,   p_error_backtrace   => 'n/a'
            ,   p_message           => 'an attempt was made to edit non-editable reference data.'
            );

            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code    => app_exception.pk_exception_codes.c_non_editable_ref_data
            ,   p_message => 'this reference data cannot be modified'
            );

        end if;

    end pr_update;

end pk_intersection_list_mgr;
/

sho err