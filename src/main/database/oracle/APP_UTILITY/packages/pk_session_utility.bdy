prompt creating package body app_utility.pk_session_utility

create or replace package body app_utility.pk_session_utility
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_utility_schema;
    c_package_name  constant varchar2( 50 char ) := $$plsql_unit;
    c_user          constant varchar2( 30 char ) := user;

    /**
      * name : fn_get_user_details
      */

    function fn_get_user_details
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_user_details';

        v_dataset sys_refcursor;

    begin

        open
             v_dataset
         for
             select
                    vsaur.aur_id
                  , vsaur.aur_username
                  , vsaur.aur_password_expiry_date
                  , vsaur.aur_number_of_roles
                  , vsaur.aur_granted_roles
                  , vsaur.lng_code
               from
                    app_data.v_standard_app_user vsaur
              where
                    vsaur.aur_username = c_user
                  ;

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => '(p_username => ' || c_user || ')'
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

        return v_dataset;

    end fn_get_user_details;

    /**
      * name : pr_configure_session
      */

    procedure pr_configure_session
    (
        p_client_info in varchar2
    )
    is

        c_procedure_name     constant varchar2 ( 50 char ) := 'pr_configure_session';
        v_is_tracing_enabled          varchar2 ( 1  char ) := 'N';

        /**
          * name : pr_set_session_state_data
          */

        procedure pr_set_session_state_data
        is
        begin

            select
                   case when dba_etr.primary_id is not null
                        then 'Y'
                   end

                   into

                   v_is_tracing_enabled
              from
                             app_data.app_user          aur
                        join app_data.standard_app_user saur    on aur.aur_id       = saur.aur_id
                   left join sys.dba_enabled_traces     dba_etr on aur.aur_username = dba_etr.primary_id
             where
                   aur.aur_username = c_user
                 ;

        exception

            when no_data_found then

                app_log.pk_log.pr_log_error
                (
                    p_sqlcode           => app_exception.pk_exception_codes.c_unknown_user
                ,   p_sqlerrm           => app_exception.pk_exception_codes.c_unknown_user
                ,   p_calling_procedure => c_package_name || '.' || c_procedure_name
                ,   p_error_backtrace   => 'n/a'
                ,   p_message           => 'the following username was submitted to the system -> ' || c_user
                );

                app_exception.pk_application_exception.pr_raise_exception
                (
                    p_code    => app_exception.pk_exception_codes.c_unknown_user
                ,   p_message => 'the user ' || c_user || ' is not registered with the application.'
                );

        end pr_set_session_state_data;

    begin

        sys.dbms_application_info.set_client_info
        (
            client_info => p_client_info
        );

        pr_set_session_state_data;

        if v_is_tracing_enabled = 'Y' then

            app_utility.pk_session_utility.pr_set_trace_file_identifier ( p_tracefile_identifier => c_user );

            sys.dbms_session.set_identifier ( c_user );

        end if;

    end pr_configure_session;

    /**
      * name : pr_set_trace_file_identifier
      */

    procedure pr_set_trace_file_identifier
    (
        p_tracefile_identifier in varchar2
    )
    is
    begin

        execute immediate 'alter session set tracefile_identifier = ' || p_tracefile_identifier;

    end pr_set_trace_file_identifier;

end pk_session_utility;
/

sho err