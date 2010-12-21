prompt creating package body app_refdata.pk_list_subdivision_sql

create or replace package body app_refdata.pk_list_subdivision_sql
as

    /**
      * name : fn_get_data_sql
      */

    function fn_get_data_sql
    (
        p_rds_id in app_data.v_subdiv_ref_data.rds_id%type
    )
    return varchar2
    is

        v_sql               varchar2( 32767 char );
        v_rds_owning_schema app_data.v_subdiv_ref_data.rds_owning_schema%type;
        v_rds_admin_view    app_data.v_subdiv_ref_data.rds_admin_view%type;
        v_rds_alias         app_data.v_subdiv_ref_data.rds_alias%type;
        v_rds_column_prefix app_data.v_subdiv_ref_data.rds_column_prefix%type;
        v_view_alias        varchar2( 15 char );

    begin

        select
               vsrd.rds_owning_schema
             , vsrd.rds_admin_view
             , vsrd.rds_alias
             , vsrd.rds_column_prefix

               into

               v_rds_owning_schema
             , v_rds_admin_view
             , v_rds_alias
             , v_rds_column_prefix
          from
               app_data.v_subdiv_ref_data vsrd
         where
               vsrd.rds_id = p_rds_id
             ;

        v_view_alias := 'v' || v_rds_alias;

        v_sql := v_sql || '   select '
                       || '          ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix      || ' ' || pk_ref_data_column_names.c_id_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_code_suffix    || ' ' || pk_ref_data_column_names.c_code_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix    || ' ' || pk_ref_data_column_names.c_name_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix   || ' ' || pk_ref_data_column_names.c_order_alias
                       || '        , ' || v_view_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_include_suffix || ' ' || pk_ref_data_column_names.c_include_alias
                       || '     from '
                       || '          ' || v_rds_owning_schema || '.' || v_rds_admin_view || ' ' || v_view_alias
                       || ' order by '
                       || '          ' || v_rds_column_prefix || pk_ref_data_column_names.c_order_suffix || ' asc '
                       || '        , ' || v_rds_column_prefix || pk_ref_data_column_names.c_name_suffix  || ' asc ';

        return v_sql;

    end fn_get_data_sql;

    /**
      * name : fn_get_update_sql
      */

    function fn_get_update_sql
    (
      p_rds_id in app_data.v_subdiv_ref_data.rds_id%type
    )
    return app_utility.tty_string
    is

        /* variables storing products of function */

        v_sql_statements app_utility.tty_string := app_utility.tty_string();
        v_merge_sql      varchar2(32767 char);
        v_delete_sql     varchar2(32767 char);

        v_rds_owning_schema app_data.v_subdiv_ref_data.rds_owning_schema%type;
        v_rds_table_name    app_data.v_subdiv_ref_data.rds_table_name%type;
        v_rds_alias         app_data.v_subdiv_ref_data.rds_alias%type;
        v_rds_column_prefix app_data.v_subdiv_ref_data.rds_column_prefix%type;

        /**
          * name: pr_build_sql
          */

        procedure pr_build_sql
        is

            c_in_dataset_col_prefix constant varchar2(3  char) := 'lrd';
            c_in_dataset_alias      constant varchar2(2  char) := 'ds';

        begin

            v_merge_sql

                    :=    'merge   '
                       || ' into   '
                       || '        ' || v_rds_owning_schema || '.' || v_rds_table_name || ' ' || v_rds_alias
                       || ' using ( '
                       || '         select '
                       || '                ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix
                       || '              , ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_include_suffix
                       || '           from '
                       || '                table(cast(:p_dataset as app_refdata.tty_subdiv_ref_data)) ' || c_in_dataset_alias
                       || '          where '
                       || '                ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_include_suffix ||' = ''Y'' '
                       || '       ) ' || c_in_dataset_alias
                       || '    on ('  || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix  ||' = ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix ||')'
                       || ' when '
                       || '   not matched then '
                       || '     insert  '
                       || '           ( '
                       || '             ' || v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix
                       || '           ) '
                       || '     values  '
                       || '           ( '
                       || '             ' || c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix
                       || '           ) '
                        ;

            v_sql_statements.extend();
            v_sql_statements( v_sql_statements.count ) := app_utility.ty_string( v_merge_sql );

            v_delete_sql  :=    'delete '
                            ||   'from '
                            || '       ' || v_rds_owning_schema || '.' || v_rds_table_name || ' ' || v_rds_alias
                            || ' where '
                            ||         v_rds_alias || '.' || v_rds_column_prefix || pk_ref_data_column_names.c_id_suffix
                            ||    ' in ( '
                            ||         ' select '
                            ||                    c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_id_suffix
                            ||           ' from '
                            ||                ' table(cast(:p_dataset as app_refdata.tty_subdiv_ref_data)) ds '
                            ||          ' where '
                            ||                  c_in_dataset_alias || '.' || c_in_dataset_col_prefix || pk_ref_data_column_names.c_include_suffix || ' = ''N'''
                            ||       ' ) '
                             ;

            v_sql_statements.extend();
            v_sql_statements( v_sql_statements.count ) := app_utility.ty_string( v_delete_sql );

        end pr_build_sql;

    begin

        select
               vsrd.rds_owning_schema
             , vsrd.rds_table_name
             , vsrd.rds_alias
             , vsrd.rds_column_prefix

               into

               v_rds_owning_schema
             , v_rds_table_name
             , v_rds_alias
             , v_rds_column_prefix
          from
               app_data.v_subdiv_ref_data vsrd
         where
               vsrd.rds_id = p_rds_id
             ;

        pr_build_sql;

        return v_sql_statements;

    end fn_get_update_sql;

end pk_list_subdivision_sql;
/

sho err