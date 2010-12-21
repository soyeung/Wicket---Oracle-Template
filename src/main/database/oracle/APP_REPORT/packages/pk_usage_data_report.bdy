prompt create package app_report.pk_usage_data_report

create or replace package body app_report.pk_usage_data_report
as

    /**
      * name : fn_get_data
      */
    function fn_get_data
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
               select
                      data_set_key
                    , data_key
                    , data_value
                 from (
                             select
                                    'LOGINS_PER_HOUR'    data_set_key
                                  , count( * )           data_key
                                  , to_char( lgn_hour )  data_value
                               from (
                                        select
                                               extract( hour from lgn.lgn_time ) lgn_hour
                                          from
                                               app_data.authentication_log lgn
                                         where
                                               lgn.lgn_time between
                                                                    sysdate - 12/24
                                                                and
                                                                    sysdate
                                    )
                           group by
                                    lgn_hour

                          union all

                             select
                                    'TOP_10_USER_LOGINS'  data_set_key
                                  , num_logins            data_key
                                  , aur_username          data_value
                               from (
                                        select
                                               aur_username                             aur_username
                                             , num_logins                               num_logins
                                             , rank() over ( order by num_logins desc ) rn
                                          from (
                                                     select
                                                            aur.aur_username aur_username
                                                          , count( * )       num_logins
                                                       from
                                                                 app_data.authentication_log lgn
                                                            join app_data.app_user           aur on lgn.aur_id = aur.aur_id
                                                   group by
                                                            aur.aur_username
                                               )
                                    )
                              where
                                    rn <= 10
                      )
             order by
                      data_set_key asc
                    , data_key     asc
                    ;

        return v_data_set;

    end fn_get_data;

end pk_usage_data_report;
/