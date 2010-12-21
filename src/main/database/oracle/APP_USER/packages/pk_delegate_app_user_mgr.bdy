prompt creating package body pk_delegate_app_user_mgr

create or replace package body app_user.pk_delegate_app_user_mgr
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_user_management_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;
    c_user          constant varchar2(30 char) := user;

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
                                           'DBRL'             datasetkey
                                         , vdbrl.dbrl_id      id
                                         , vdbrl.dbrl_name    name
                 from
                      app_data.v_usr_rd_dbrl vdbrl
             order by
                      dbrl_order asc
                    , dbrl_name  asc
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
      * name: fn_get_delegate_users
      */

    function fn_get_delegate_users
    (
        p_aur_username       in app_data.v_delegate_app_user.aur_username%type
    ,   p_is_account_enabled in app_data.v_delegate_app_user.aur_is_account_enabled%type
    ,   p_is_tracing_enabled in app_data.v_delegate_app_user.aur_is_tracing_enabled%type
    ,   p_dbrl_id            in app_data.v_usr_rd_dbrl.dbrl_id%type
    ,   p_lower_record_limit in int                                                      default null
    ,   p_upper_record_limit in int                                                      default null
    )
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_delegate_users';

        v_data_set sys_refcursor;
        v_sql      varchar2( 32767 char ) default    ' select '
                                                 || '        vdaur.aur_id                 id '
                                                 || '      , vdaur.aur_username           aur_username   '
                                                 || '      , vdaur.aur_profile            aur_profile    '
                                                 || '      , vdaur.aur_is_account_enabled aur_is_account_enabled   '
                                                 || '      , vdaur.aur_is_tracing_enabled aur_is_tracing_enabled   '
                                                 || '      , vdaur.aur_created_date       created_date   '
                                                 || '      , vdaur.aur_updated_date       updated_date   '
                                                 || '   from '
                                                 || '        app_data.v_delegate_app_user vdaur '
                                                 || '  where '
                                                 || '        1 = 1 '
                                                 ;

        /**
          * name: pr_set_username
          */

        procedure pr_set_username
        is
        begin

            if p_aur_username is not null then
                v_sql := v_sql || ' and vdaur.aur_username like ''%''||:p_aur_username||''%'' ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_aur_username is null) ';
            end if;

        end pr_set_username;

        /**
          * name: pr_set_is_account_enabled
          */

        procedure pr_set_is_account_enabled
        is
        begin

            if p_is_account_enabled is not null then
                v_sql := v_sql || ' and vdaur.aur_is_account_enabled = :p_is_account_enabled ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_is_account_enabled is null)';
            end if;

        end pr_set_is_account_enabled;

        /**
          * name: pr_set_is_tracing_enabled
          */

        procedure pr_set_is_tracing_enabled
        is
        begin

            if p_is_tracing_enabled is not null then
                v_sql := v_sql || ' and vdaur.aur_is_tracing_enabled = :p_is_tracing_enabled ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_is_tracing_enabled is null)';
            end if;

        end pr_set_is_tracing_enabled;

        /**
          * name: pr_set_db_role
          */

        procedure pr_set_db_role
        is
        begin
            if p_dbrl_id is not null then
                v_sql := v_sql || ' and vdaur.aur_id in (select vaurrol.aur_id from app_data.v_app_user_role vaurrol where vaurrol.dbrl_id = :p_dbrl_id) ';
            else
                v_sql := v_sql || ' and (1 = 1 or :p_dbrl_id is null)';
            end if;
        end pr_set_db_role;

        /**
          * name: pr_set_pagination
          */

        procedure pr_set_pagination
        is
        begin

            if
                     p_lower_record_limit is not null
                 and p_upper_record_limit is not null
            then

                /* add extra attribute to gain ordering */

                v_sql :=    ' select '
                         || '        /*+ first_rows_' || ( p_upper_record_limit - p_lower_record_limit + 1 ) || '*/'
                         || '        id '
                         || '      , aur_username '
                         || '      , aur_profile '
                         || '      , aur_is_account_enabled '
                         || '      , aur_is_tracing_enabled '
                         || '      , created_date '
                         || '      , updated_date '
                         || '      , row_number() over (order by aur_username asc) rn '
                         || '   from (' || v_sql || ') '
                          ;

                v_sql :=    ' select '
                         || '        id '
                         || '      , aur_username '
                         || '      , aur_profile '
                         || '      , aur_is_account_enabled '
                         || '      , aur_is_tracing_enabled '
                         || '      , created_date '
                         || '      , updated_date '
                         || '   from (' || v_sql || ') '
                         || '  where '
                         || '        rn between :p_lower_record_limit and :p_upper_record_limit ';

            else

                v_sql := v_sql || ' and (1 = 1 or :p_lower_record_limit is null) and (1 = 1 or :p_upper_record_limit is null) ';

            end if;

        end pr_set_pagination;

    /**
      * name: pr_add_ordering
      */

        procedure pr_add_ordering
        is
        begin

            if
                     p_lower_record_limit is not null
                 and p_upper_record_limit is not null
            then
                v_sql := v_sql || ' order by rn asc ';
            else
                v_sql := v_sql || ' order by aur_username asc ';
            end if;

        end pr_add_ordering;

    begin

        pr_set_username;

        pr_set_is_account_enabled;

        pr_set_is_tracing_enabled;

        pr_set_db_role;

        pr_set_pagination;

        pr_add_ordering;

         open
              v_data_set
          for
              v_sql
        using
                p_aur_username
            ,   p_is_account_enabled
            ,   p_is_tracing_enabled
            ,   p_dbrl_id
            ,   p_lower_record_limit
            ,   p_upper_record_limit
            ;

        app_log.pk_log.pr_log_activity
        (
          p_package_owner  => c_package_owner
        , p_package_name   => c_package_name
        , p_procedure_name => c_procedure_name
        , p_start_time     => c_start_time
        , p_end_time       => dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_delegate_users;

    /**
      * name: pr_update_delegate_users
      */

    procedure pr_update_delegate_users
    (
        p_dataset in app_user.tty_delegate_app_user
    )
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'pr_update_delegate_users';

        /**
          * name: pr_set_updated_user_details
          */

        procedure pr_set_updated_user_details
        is

            c_now constant timestamp := systimestamp;

        begin

            --
            -- update the 'app_user' table so that we have a record of who made the last update and when.
            --

            update
                   app_data.app_user aur
               set
                   aur.aur_updated_by   = c_user
                 , aur.aur_updated_date = c_now
             where
                   exists (
                            select
                                   null
                              from
                                   table
                                   (
                                       cast
                                       (
                                           p_dataset as app_user.tty_delegate_app_user
                                       )
                                   )                                ds
                             where
                                   ds.aur_id           = aur.aur_id
                               and ds.aur_updated_date = aur.aur_updated_date
                          )
                 ;

        end pr_set_updated_user_details;

    begin

        for i in (
                     select
                            ds.aur_id                      aur_id
                          , ds.aur_username                aur_username

                          , ds.aur_is_account_enabled      new_is_account_enabled
                          , vdgaur.aur_is_account_enabled  current_is_account_enabled

                          , ds.aur_is_tracing_enabled      new_is_tracing_enabled
                          , vdgaur.aur_is_tracing_enabled  current_is_tracing_enabled

                          , ds.aur_updated_date
                       from
                            table
                            (
                                cast
                                (
                                    p_dataset as app_user.tty_delegate_app_user
                                )
                            )                                        ds
                            join app_data.v_delegate_app_user        vdgaur on (
                                                                                     ds.aur_username     = vdgaur.aur_username
                                                                                 and ds.aur_updated_date = vdgaur.aur_updated_date
                                                                               )
                 )
        loop

            --
            -- enable/disable account
            --

            if (
                   i.new_is_account_enabled != i.current_is_account_enabled
               )
            then

                case
                    i.new_is_account_enabled
                when
                    'Y'
                then
                    app_user.pk_app_user_mgr.pr_enable_user
                    (
                        p_username => i.aur_username
                    );
                when
                    'N'
                then
                    app_user.pk_app_user_mgr.pr_disable_user
                    (
                        p_username => i.aur_username
                    );
                end case;

            end if;

            --
            -- enable/disable tracing
            --

            if (
                   i.new_is_tracing_enabled != i.current_is_tracing_enabled
               )
            then

                case i.new_is_tracing_enabled
                    when
                        'Y'
                    then
                        if (i.aur_username = c_user) then
                            app_utility.pk_session_utility.pr_set_trace_file_identifier ( p_tracefile_identifier => c_user );
                        end if;

                        sys.dbms_monitor.client_id_trace_enable ( client_id => i.aur_username , waits => true , binds => true );
                    when
                        'N'
                    then
                         sys.dbms_monitor.client_id_trace_disable ( i.aur_username );
                end case;

            end if;

        end loop;

        pr_set_updated_user_details;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_update_delegate_users;

end pk_delegate_app_user_mgr;
/

sho err