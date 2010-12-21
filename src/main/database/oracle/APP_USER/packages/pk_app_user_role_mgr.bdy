prompt creating package body pk_app_user_role_mgr

create or replace package body app_user.pk_app_user_role_mgr
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;

    /**
      * name: fn_get_all_roles
      */

    function fn_get_all_roles
    (
        p_aur_id in app_data.v_app_user.aur_id%type
    )
    return sys_refcursor
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'fn_get_all_roles';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
               select
                      vdbrl.dbrlc_name  name
                    , vdbrl.dbrl_id     id
                    , vdbrl.dbrl_name   descr
                    , case
                        when
                             vaurr.dbrl_code is null
                        then
                             'N'
                        else
                            'Y'
                      end               dbrl_is_assigned_to_user
                 from
                                app_data.v_usr_rd_mdbrl   vdbrl
                      left join app_data.v_app_user_role  vaurr on (
                                                                         vdbrl.dbrl_code = vaurr.dbrl_code
                                                                     and vaurr.aur_id    = p_aur_id
                                                                   )
             order by
                      vdbrl.dbrlc_name asc
                    , vdbrl.dbrl_name asc
                ;

        app_log.pk_log.pr_log_activity
        (
            p_parameter_info => '(p_aur_id => ' || to_char( p_aur_id ) || ')'
        ,   p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_all_roles;

    /**
      * name: pr_update_user_roles
      */

    procedure pr_update_user_roles
    (
        p_aur_id  in app_data.v_app_user.aur_id%type
    ,   p_dataset in app_user.tty_db_role
    )
    is

        v_username app_data.v_app_user.aur_username%type;

        /**
          * name: pr_get_username
          */

        procedure pr_get_username
        is
        begin

            select
                   vaur.aur_username
                   into
                   v_username
              from
                   app_data.v_app_user vaur
             where
                   vaur.aur_id = p_aur_id
                 ;

        end pr_get_username;

        /**
          * name: pr_set_roles
          */

        procedure pr_set_roles
        is
        begin

            for i in ( /* retrieve a list of the roles whose status needs to change */
                       select
                              ds.dbrl_id                   dbrl_id
                            , vdbrl.dbrl_code              dbrl_code
                            , ds.dbrl_is_assigned_to_user  dbrl_is_assigned_to_user
                         from
                                        table
                                        (
                                          cast
                                          (
                                            p_dataset as app_user.tty_db_role
                                          )
                                        )                             ds
                                   join app_data.v_usr_rd_mdbrl       vdbrl on ds.dbrl_id = vdbrl.dbrl_id
                              left join app_data.v_app_user_role      vaurr on (
                                                                                     vdbrl.dbrl_code = vaurr.dbrl_code
                                                                                 and vaurr.aur_id    = p_aur_id
                                                                               )
                        where
                              ds.dbrl_is_assigned_to_user != case
                                                               when
                                                                    vaurr.dbrl_code is null
                                                               then
                                                                    'N'
                                                               else
                                                                    'Y'
                                                             end
                     )
            loop

                case i.dbrl_is_assigned_to_user
                    when
                        'Y'
                    then
                        execute immediate 'grant '  || i.dbrl_code || ' to "'   || v_username || '"';
                    when
                        'N'
                    then
                        execute immediate 'revoke ' || i.dbrl_code || ' from "' || v_username || '"';
                end case;

            end loop;

        end pr_set_roles;

        /**
          * name: pr_set_default_roles
          */

        procedure pr_set_default_roles
        is

            v_default_role_list varchar2(32767 char);

        begin

            with
                data
            as
            (
              select
                     vaurdbrl.dbrl_code                       dbrl_code
                   , row_number()
                       over
                          ( order by vaurdbrl.dbrl_code asc ) rn
                from
                          app_data.v_app_user_role vaurdbrl
                     join app_data.default_db_role ddbrl    on vaurdbrl.dbrl_id = ddbrl.dbrl_id
               where
                     vaurdbrl.aur_username = v_username
             )
                 select
                        max
                        (
                          ltrim
                          (
                            sys_connect_by_path
                            (
                              d.dbrl_code
                            , ', '
                            )
                          , ', '
                          )
                        )
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

            if v_default_role_list is not null then

                execute immediate ('alter user "' || v_username || '" default role ' || v_default_role_list);

            else

                execute immediate ('alter user "' || v_username || '" default role none');

            end if;

        end pr_set_default_roles;

    begin

        pr_get_username;

        pr_set_roles;

        pr_set_default_roles;

    end pr_update_user_roles;

end pk_app_user_role_mgr;
/

sho err