prompt creating package body pk_set_app_user_roles

create or replace package body app_user.pk_set_app_user_roles
as

    c_package_name constant varchar2(30 char) := $$plsql_unit;

    /**
      * name : pr_disable_roles
      */

    procedure pr_disable_roles
    is

        v_default_role_list app_utility.tty_string;

        /**
          * name : pr_set_default_role_list
          */

        procedure pr_set_default_role_list
        is
        begin

            select
                   cast
                   (
                       multiset
                       (
                           (
                               select
                                      urp.granted_role
                                 from
                                      sys.user_role_privs urp
                                where
                                      urp.default_role = 'YES'
                           )
                       )
                       as app_utility.tty_string
                   )
                   into
                   v_default_role_list
              from
                   dual d
             where
                   rownum <= 1
                 ;

        end pr_set_default_role_list;

    begin

        pr_set_default_role_list;

        sys.dbms_session.set_role ( app_utility.pk_string_utility.fn_get_comma_delimited_string ( v_default_role_list ) );

    end pr_disable_roles;

    /**
      * name : pr_enable_role
      */

    procedure pr_enable_role
    (
        p_role in varchar2
    )
    is

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_role not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_rolename
            ,   p_exception_message => 'the rolename "' || p_role || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name
            );

        end pr_validate_input;

    begin

        pr_validate_input;

        sys.dbms_session.set_role
        (
            role_cmd => p_role
        );

    end pr_enable_role;

    /**
      * name : pr_enable_roles
      */

    procedure pr_enable_roles
    (
        p_role_list in app_utility.tty_string
    )
    is

        c_role_list_count constant int                   := p_role_list.count;
        v_role_list                varchar2(32767 char);

        /**
          * name : pr_check_roles
          */

        procedure pr_check_roles
        is
        begin

            if (
                   c_role_list_count = 1
               )
            then

                app_utility.pk_assert.pr_assert_condition
                (
                    p_condition         => p_role_list( 1 ).string not like '%"%'
                ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_rolename
                ,   p_exception_message => 'the rolename "' || p_role_list( 1 ).string || '" is considered to be dangerous and is not permitted'
                ,   p_invoker           => c_package_name
                );

            else

                for i in (
                             select
                                    ds.string
                               from
                                    table
                                    (
                                        cast
                                        (
                                            p_role_list as app_utility.tty_string
                                        )
                                    ) ds
                         )
                loop

                    app_utility.pk_assert.pr_assert_condition
                    (
                        p_condition         => i.string not like '%"%'
                    ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_rolename
                    ,   p_exception_message => 'the rolename "' || i.string || '" is considered to be dangerous and is not permitted'
                    ,   p_invoker           => c_package_name
                    );

                end loop;

            end if;

        end pr_check_roles;

        /**
          * name : pr_set_role_list
          */

        procedure pr_set_role_list
        is
        begin

            if
                c_role_list_count = 1
            then

                v_role_list := p_role_list( 1 ).string;

            else

                v_role_list := app_utility.pk_string_utility.fn_get_comma_delimited_string( p_role_list );

            end if;

        end pr_set_role_list;

    begin

        pr_check_roles;

        pr_set_role_list;

        sys.dbms_session.set_role
        (
            role_cmd => v_role_list
        );

    end pr_enable_roles;

end pk_set_app_user_roles;
/

sho err