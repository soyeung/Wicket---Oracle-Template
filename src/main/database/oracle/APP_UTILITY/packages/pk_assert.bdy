prompt creating package body app_utility.pk_assert

create or replace package body app_utility.pk_assert
as

    /**
      * name : pr_assert_condition
      */

    procedure pr_assert_condition
    (
        p_condition         in boolean
    ,   p_aex_code          in app_data.app_exception.aex_code%type
    ,   p_exception_message in varchar2
    ,   p_invoker           in varchar2
    )
    is
    begin

        if (
               not p_condition
               or
               p_condition is null
           )
        then

            app_log.pk_log.pr_log_error
            (
                p_sqlcode           => p_aex_code
            ,   p_sqlerrm           => p_aex_code
            ,   p_calling_procedure => p_invoker
            ,   p_error_backtrace   => 'n/a'
            ,   p_message           => p_exception_message
            );

            app_exception.pk_application_exception.pr_raise_exception
            (
                p_code    => p_aex_code
            ,   p_message => p_exception_message
            );

        end if;

    end pr_assert_condition;

end pk_assert;
/

sho err