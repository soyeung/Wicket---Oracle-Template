prompt creating package pk_log - for the installation only
prompt because most packages in the public api log activity, and we are installing using SYS, we must disable the log_activity procedure

create or replace package app_log.pk_log

    /**
      * purpose : to provide a mechanism by which messages can be recorded without adversely effecting the application itself.
      */

as

    /**
      * name   : pr_log_app_authentication
      * desc   : record a message in the 'authentication_log' table. the message is always recorded
      *        : regardless of whether the calling program completes successfully or not.
      * params : p_ip_address   -> ip address of user authenticating with the application
      *        : p_http_session -> http session string
      */

    procedure pr_log_app_authentication
    (
        p_ip_address   in app_data.authentication_log.lgn_ip_address%type
    ,   p_http_session in app_data.authentication_log.lgn_http_session%type
    );

    /**
      * name   : pr_log_error
      * desc   : record a message in the 'error_log' table. the message is always recorded
      *        : regardless of whether the calling program completes successfully or not.
      * params : p_sqlcode -> specify the sqlcode if the message is recording information about an exception.
      *        : p_sqlerrm -> record the sqlerrm (error message) if the message is recording information about an exception.
      *        : p_calling_procedure -> the name of the program unit calling this procedure.
      *        : p_error_backtrace -> store the error stack - this can be accessed by assigning this parameter the value of dbms_utility.format_error_backtrace
      *        : p_message -> record a message.
      */

    procedure pr_log_error
    (
        p_sqlcode           in app_data.error_log.erl_sqlcode%type
    ,   p_sqlerrm           in app_data.error_log.erl_sqlerrm%type
    ,   p_calling_procedure in app_data.error_log.erl_calling_procedure%type
    ,   p_error_backtrace   in app_data.error_log.erl_error_backtrace%type
    ,   p_message           in app_data.error_log.erl_message%type
    );

    /**
      * name   : pr_log_message
      * desc   : record a message in the 'message_log' table. the message is always recorded
      *        : regardless of whether the calling program completes successfully or not.
      * params : p_calling_procedure -> the name of the program unit calling this procedure.
      *        : p_message -> record a message.
      */

    procedure pr_log_message
    (
        p_message           in app_data.message_log.msl_message%type
    ,   p_calling_procedure in app_data.message_log.msl_calling_procedure%type
    );

    /**
      * name   : pr_log_activity
      * desc   : records the activity performed in the system. modules providing a
      *        : transactional api to the application call this by convention.
      * params : p_description -> description of the operation being logged
      *        : p_parameter_info -> potentially usefule information about the parameter values the operation was called with
      *        : p_package_owner -> the schema owning the object which has run this activity
      *        : p_package_name -> the name of the package which has run this activity
      *        : p_module_name -> the name of the function/procedure which has run this activity
      *        : p_start_time -> the time at which this operation started. use dbms_utility.get_time
      *        : p_end_time -> the time at which this operation ended. use dbms_utility.get_time
      */

    procedure pr_log_activity
    (
        p_parameter_info in app_data.activity_log.act_parameter_info%type default 'n/a'
    ,   p_package_owner  in app_data.activity_log.act_package_owner%type
    ,   p_package_name   in app_data.activity_log.act_package_name%type
    ,   p_procedure_name in app_data.activity_log.act_procedure_name%type
    ,   p_start_time     in number
    ,   p_end_time       in number                                        default sys.dbms_utility.get_time
    );

end pk_log;
/

sho err

create or replace package body app_log.pk_log
as

    c_current_user constant app_data.v_app_user.aur_username%type := user;

    /**
      * name: pr_log_app_authentication
      */

    procedure pr_log_app_authentication
    (
        p_ip_address   in app_data.authentication_log.lgn_ip_address%type
    ,   p_http_session in app_data.authentication_log.lgn_http_session%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.authentication_log lgn
             (
                 lgn.lgn_id
             ,   lgn.lgn_ip_address
             ,   lgn.lgn_http_session
             ,   lgn.aur_id
             )
        values
             (
                 app_data.sq_lgn.nextval
             ,   p_ip_address
             ,   p_http_session
             ,   (
                     select
                            vaur.aur_id
                       from
                            app_data.v_app_user vaur
                      where
                            vaur.aur_username = c_current_user
                 )
             );

        commit;

    end pr_log_app_authentication;

    /**
      * name: pr_log_error
      */

    procedure pr_log_error
    (
        p_sqlcode           in app_data.error_log.erl_sqlcode%type
    ,   p_sqlerrm           in app_data.error_log.erl_sqlerrm%type
    ,   p_calling_procedure in app_data.error_log.erl_calling_procedure%type
    ,   p_error_backtrace   in app_data.error_log.erl_error_backtrace%type
    ,   p_message           in app_data.error_log.erl_message%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.error_log erl
             (
                 erl.erl_id
             ,   erl.erl_sqlcode
             ,   erl.erl_sqlerrm
             ,   erl.erl_calling_procedure
             ,   erl.erl_error_backtrace
             ,   erl.erl_message
             )
        values
             (
                 app_data.sq_erl.nextval
             ,   p_sqlcode
             ,   p_sqlerrm
             ,   p_calling_procedure
             ,   p_error_backtrace
             ,   p_message
             );

        commit;

    end pr_log_error;

    /**
      * name: pr_log_message
      */

    procedure pr_log_message
    (
        p_message           in app_data.message_log.msl_message%type
    ,   p_calling_procedure in app_data.message_log.msl_calling_procedure%type
    )
    is

        pragma autonomous_transaction;

    begin

        insert
          into
               app_data.message_log msl
             (
                 msl.msl_id
             ,   msl.msl_message
             ,   msl.msl_calling_procedure
             )
        values
             (
                 app_data.sq_msl.nextval
             ,   p_message
             ,   p_calling_procedure
             );

        commit;

    end pr_log_message;

    /**
      * name: pr_log_activity
      */

    procedure pr_log_activity
    (
        p_parameter_info in app_data.activity_log.act_parameter_info%type default 'n/a'
    ,   p_package_owner  in app_data.activity_log.act_package_owner%type
    ,   p_package_name   in app_data.activity_log.act_package_name%type
    ,   p_procedure_name in app_data.activity_log.act_procedure_name%type
    ,   p_start_time     in number
    ,   p_end_time       in number                                        default sys.dbms_utility.get_time
    )
    is
    begin

        null;

    end pr_log_activity;

end pk_log;
/

sho err

grant execute on app_log.pk_log to public
/