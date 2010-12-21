prompt creating package body app_refdata.pk_list_subdivision_mgr

create or replace package body app_refdata.pk_list_subdivision_mgr
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_refdata_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    /**
      * name : fn_get_data
      */

    function fn_get_data
    (
        p_rds_id in app_data.v_subdiv_ref_data.rds_id%type
    )
    return sys_refcursor
    is

        c_sql      constant varchar2( 32767 char ) := app_refdata.pk_list_subdivision_sql.fn_get_data_sql( p_rds_id => p_rds_id );
        v_data_set          sys_refcursor;

    begin

        open
             v_data_set
         for
             c_sql
           ;

        return v_data_set;

    end fn_get_data;

    /**
      * name : pr_update
      */

    procedure pr_update
    (
        p_rds_id  in app_data.v_subdiv_ref_data.rds_id%type
    ,   p_dataset in app_refdata.tty_subdiv_ref_data
    )
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'pr_update';

        v_sql_statements constant app_utility.tty_string := app_refdata.pk_list_subdivision_sql.fn_get_update_sql( p_rds_id => p_rds_id );

        /**
          * name: pr_update_list
          */

        procedure pr_update_list
        is
        begin

            for i in (

                        select
                               ds.string
                          from
                               table( cast( v_sql_statements as app_utility.tty_string ) ) ds
                     )
            loop

                execute immediate
                                  i.string
                            using
                                  p_dataset
                                ;

            end loop;

        end pr_update_list;

    begin

        if app_refdata.pk_ref_data_mgr.fn_is_editable_data_structure
           (
               p_rds_id => p_rds_id
           )
        then

            pr_update_list;

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

end pk_list_subdivision_mgr;
/

sho err