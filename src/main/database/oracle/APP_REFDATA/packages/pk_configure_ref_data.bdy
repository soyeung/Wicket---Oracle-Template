prompt creating package body pk_configure_ref_data

create or replace package body app_refdata.pk_configure_ref_data
as

    c_package_owner constant varchar2( 30 char ) := app_utility.pk_schema_names.c_refdata_schema;
    c_package_name  constant varchar2( 30 char ) := $$plsql_unit;

    c_current_user  constant varchar2( 30 char ) := user;

    /**
      * name : pr_configure_ref_data
      */

    procedure pr_configure_ref_data
    (
        p_data_structure in app_refdata.tty_ref_data_structure
    )
    is

        c_start_time     constant number            := dbms_utility.get_time;
        c_procedure_name constant varchar2(30 char) := 'pr_configure_ref_data';

        /**
          * name : pr_set_as_editable
          */

        procedure pr_set_as_editable
        is
        begin

            insert
              into
                   app_data.editable_ref_data_structure
                 (
                   rds_id
                 )
            select
                   prds.rds_id
              from
                   table( cast( p_data_structure as app_refdata.tty_ref_data_structure ) ) prds
             where
                   prds.rds_is_editable = 'Y'
               and not exists (
                                select
                                       null
                                  from
                                       app_data.editable_ref_data_structure erds
                                 where
                                       erds.rds_id = prds.rds_id
                              )
                 ;

        end pr_set_as_editable;

        /**
          * name : pr_set_as_non_editable
          */

        procedure pr_set_as_non_editable
        is
        begin

            delete
              from
                   app_data.editable_ref_data_structure erds
             where
                   exists (
                            select
                                   null
                              from
                                   table( cast( p_data_structure as app_refdata.tty_ref_data_structure ) )prds
                             where
                                   prds.rds_id          = erds.rds_id
                               and prds.rds_is_editable = 'N'
                          )
                 ;

        end pr_set_as_non_editable;

    begin

        pr_set_as_editable;

        pr_set_as_non_editable;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => dbms_utility.get_time
        );

    end pr_configure_ref_data;

end pk_configure_ref_data;
/

sho err