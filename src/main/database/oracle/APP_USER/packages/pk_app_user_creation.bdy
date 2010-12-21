prompt creating package body pk_app_user_creation

create or replace package body app_user.pk_app_user_creation
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;
    c_user          constant varchar2( 30 char ) := user;

    /**
      * name: pr_create_user
      */

    procedure pr_create_user
    (
        p_username in  app_data.v_app_user.aur_username%type
    ,   p_password in  varchar2
    ,   p_uty_id   in  app_data.v_usr_rd_uty.uty_id%type
    ,   p_aurp_id  in  app_data.v_usr_rd_aurp.aurp_id%type
    ,   p_aur_id   out app_data.v_app_user.aur_id%type
    )
    is

        c_procedure_name constant varchar2(50 char) := 'pr_create_user';

        v_aurp_code app_data.v_usr_rd_aurp.aurp_code%type;

        /**
          * name: pr_validate_input
          */

        procedure pr_validate_input
        is
        begin

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_username not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_username
            ,   p_exception_message => 'the username "' || p_username || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

            app_utility.pk_assert.pr_assert_condition
            (
                p_condition         => p_password not like '%"%'
            ,   p_aex_code          => app_exception.pk_exception_codes.c_dangerous_password
            ,   p_exception_message => 'the password "' || p_password || '" is considered to be dangerous and is not permitted'
            ,   p_invoker           => c_package_name || '.' || c_procedure_name
            );

        end pr_validate_input;

        /**
          * name: pr_create_application_user
          */

        procedure pr_create_application_user
        is
        begin

            select
                   vaurp.aurp_code
                   into
                   v_aurp_code
              from
                   app_data.v_usr_rd_aurp vaurp
             where
                   vaurp.aurp_id = p_aurp_id
                 ;

            execute immediate ('create user "' || p_username || '" identified by "' || p_password || '" profile ' || v_aurp_code);

        end pr_create_application_user;

        /**
          * name: pr_grant_user_to_proxy
          */

        procedure pr_grant_user_to_proxy
        is

             v_proxy_username app_data.v_proxy_app_user.aur_username%type;

        begin

            select
                   vpaur.aur_username
                   into
                    v_proxy_username
              from
                   app_data.v_proxy_app_user vpaur
                 ;

            execute immediate ('alter user "' || p_username || '" grant connect through "' ||  v_proxy_username || '" authentication required');

        end pr_grant_user_to_proxy;

        /**
          * name: pr_grant_privileges
          */

        procedure pr_grant_privileges
        is
        begin

            for i in (
                         select
                                vutr.dbrl_code
                           from
                                app_data.v_adm_rd_utr vutr
                          where
                                vutr.uty_id = p_uty_id
                     )
            loop

                execute immediate ( 'grant ' || i.dbrl_code || ' to "' || p_username || '"' );

            end loop;

        end pr_grant_privileges;

        /**
          * name: pr_set_default_privileges
          */

        procedure pr_set_default_privileges
        is

            v_default_role_list varchar2(32767 char);

        begin

            with
                 data
            as
            (
                select
                       dbrl.dbrl_code                                    dbrl_code
                     , row_number() over ( order by dbrl.dbrl_code asc ) rn
                  from
                            app_data.app_user_type_role uty
                       join app_data.db_role            dbrl  on uty.dbrl_id  = dbrl.dbrl_id
                       join app_data.default_db_role    ddbrl on dbrl.dbrl_id = ddbrl.dbrl_id
                 where
                       uty.uty_id = p_uty_id
            )
               select
                      max( ltrim( sys_connect_by_path ( d.dbrl_code , ', ' ) , ', ' ) )
                      into
                      v_default_role_list
                  from
                       data d
            start with
                       d.rn = 1
            connect by
                       prior
                             d.rn = d.rn - 1
                    ;

            if  v_default_role_list is not null then
                execute immediate ( 'alter user "' || p_username || '" default role ' ||  v_default_role_list );
            else
                execute immediate ( 'alter user "' || p_username || '" default role none' );
            end if;

        end pr_set_default_privileges;

        /**
          * name: pr_register_user
          */

        procedure pr_register_user
        is
        begin

                 insert
                   into
                        app_data.app_user aur
                      (
                        aur_id
                      , aur_username
                      )
                 values
                      (
                        app_data.sq_aur.nextval
                      , p_username
                      )
            returning
                      aur_id
                 into
                      p_aur_id
                    ;

        end pr_register_user;

        /**
          * name: pr_restrict_sql_plus
          */

        procedure pr_restrict_sql_plus
        is

            c_sqlplus_product constant system.product_user_profile.product%type    := 'sql*plus';
            c_disable_keyword constant system.product_user_profile.char_value%type := 'disabled';

        begin

            insert
              into
                   system.product_user_profile pup
                 (
                   pup.product
                 , pup.userid
                 , pup.attribute
                 , pup.scope
                 , pup.numeric_value
                 , pup.char_value
                 , pup.date_value
                 , pup.long_value
                 )
            select
                   c_sqlplus_product
                 , upper(p_username)
                 , rsf.rsf_function
                 , null
                 , null
                 , c_disable_keyword
                 , null
                 , null
              from
                   app_data.restricted_sqlplus_function rsf
                 ;

        end pr_restrict_sql_plus;

    begin

        pr_validate_input;

        pr_create_application_user;

        pr_grant_user_to_proxy;

        pr_grant_privileges;

        pr_set_default_privileges;

        pr_register_user;

        pr_restrict_sql_plus;

    end pr_create_user;

end pk_app_user_creation;
/

sho err