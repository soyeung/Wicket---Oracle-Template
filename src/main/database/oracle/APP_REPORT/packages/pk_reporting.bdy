prompt creating package body app_report.pk_reporting

create or replace package body app_report.pk_reporting
as

    c_package_owner constant varchar2(30 char) := app_utility.pk_schema_names.c_report_schema;
    c_package_name  constant varchar2(30 char) := $$plsql_unit;
    c_user          constant varchar2(30 char) := user;

    /**
      * name : fn_get_report_list
      */

    function fn_get_report_list
    return sys_refcursor
    is

        c_start_time     constant number              := dbms_utility.get_time;
        c_procedure_name constant varchar2( 30 char ) := 'fn_get_report_list';

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             with
                  data
             as
             (
                  select
                         null             rptc_parent_id
                       , rptc.rptc_id     rptc_child_id
                       , rptc.rptc_name
                    from
                         app_data.v_usr_rd_rptc rptc
                   where
                         rptc.rptc_id not in (
                                               select
                                                      rptcl.rptc_child_id
                                                 from
                                                      app_data.report_category_link rptcl
                                             )

               union all

                  select
                         rptcl.rptc_id        rptc_parent_id
                       , rptcl.rptc_child_id  rptc_child_id
                       , rptc.rptc_name
                    from
                              app_data.v_usr_rd_rptc        rptc
                         join app_data.report_category_link rptcl on rptc.rptc_id = rptcl.rptc_child_id
             )
                        select
                               d.rptc_name     name
                             , level           hlevel
                             , cast
                               (
                                 multiset
                                 (
                                   (
                                     select
                                            rpt.rpt_name
                                          , dbrl.dbrl_code
                                          , rpt.rpt_page
                                       from
                                                 app_data.v_usr_rd_rpt           rpt
                                            join app_data.v_adm_rd_dbrl          dbrl     on rpt.dbrl_id  = dbrl.dbrl_id
                                            join app_data.report_category_report rptcr    on rpt.rpt_id   = rptcr.rpt_id
                                            join app_data.v_app_user_role        vaurdbrl on dbrl.dbrl_id = vaurdbrl.dbrl_id
                                      where
                                            rptcr.rptc_id         = d.rptc_child_id
                                        and vaurdbrl.aur_username = c_user
                                   )
                                 )
                                 as app_report.tty_report
                               )               rpt_set
                          from
                               data d
                         where
                               exists (
                                        select
                                               null
                                          from
                                                    app_data.report_category_report rptcr
                                               join app_data.v_usr_rd_rpt           rpt      on rptcr.rpt_id = rpt.rpt_id
                                               join app_data.v_adm_rd_dbrl          dbrl     on rpt.dbrl_id  = dbrl.dbrl_id
                                               join app_data.v_app_user_role        vaurdbrl on dbrl.dbrl_id = vaurdbrl.dbrl_id
                                         where
                                               rptcr.rptc_id         = d.rptc_child_id
                                           and vaurdbrl.aur_username = c_user
                                      )
                    start with
                               d.rptc_parent_id is null
                    connect by
                               prior d.rptc_child_id = d.rptc_parent_id
             order siblings by d.rptc_name asc
                             ;

        app_log.pk_log.pr_log_activity
        (
            p_package_owner  => c_package_owner
        ,   p_package_name   => c_package_name
        ,   p_procedure_name => c_procedure_name
        ,   p_start_time     => c_start_time
        ,   p_end_time       => sys.dbms_utility.get_time
        );

        return v_data_set;

    end fn_get_report_list;

end pk_reporting;
/

sho err