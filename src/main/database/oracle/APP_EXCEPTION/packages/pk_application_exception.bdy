prompt creating package body pk_application_exception

create or replace package body app_exception.pk_application_exception
as

    /**
      * name: pr_raise_exception
      */

    procedure pr_raise_exception
    (
        p_code in app_data.app_exception.aex_code%type
    )
    is

        v_aex_id      app_data.app_exception.aex_id%type;
        v_aex_message app_data.app_exception.aex_message%type;

        /**
          * name: pr_find_exception
          */

        procedure pr_find_exception
        is
        begin

            select
                     aex.aex_id
                 ,   aex.aex_message
                     into
                     v_aex_id
                 ,   v_aex_message
              from
                   app_data.app_exception aex
             where
                   aex.aex_code = p_code
                 ;

        end pr_find_exception;

    begin

        pr_find_exception;

        raise_application_error
        (
          v_aex_id
        , v_aex_message
        );

    end pr_raise_exception;

    /**
      * name: pr_raise_exception
      */

    procedure pr_raise_exception
    (
        p_code    in app_data.app_exception.aex_code%type
    ,   p_message in varchar2
    )
    is

        v_aex_id app_data.app_exception.aex_id%type;

        /* ****************************************************************
         * name: pr_find_exception
         * **************************************************************** */

        procedure pr_find_exception
        is
        begin

          select
                 aex.aex_id
                 into
                 v_aex_id
            from
                 app_data.app_exception aex
           where
                 aex.aex_code = p_code
               ;

        end pr_find_exception;

    begin

        pr_find_exception;

        raise_application_error
        (
            v_aex_id
        ,   p_message
        );

    end pr_raise_exception;

end pk_application_exception;
/

sho err