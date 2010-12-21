prompt creating package body app_report.pk_session_lock_report

create or replace package body app_report.pk_session_lock_report
as

    /**
      * name : fn_get_report
      */

    function fn_get_report
    return sys_refcursor
    is

        v_data_set sys_refcursor;

    begin

        open
             v_data_set
         for
             with
                 lock_data
             as
             (

               select
                      /*+ materialize */
                                         dba_lock.session_id      session_id
                                       , lock_session.username    username
                                       , prev_sql.sql_text        prev_sql_text
                                       , curr_sql.sql_text        curr_sql_text
                                       , dba_lock.lock_type       lock_type
                                       , dba_lock.mode_held       mode_held
                                       , dba_lock.mode_requested  mode_requested
                                       , dba_lock.lock_id1        lock_id1
                                       , dba_lock.lock_id2        lock_id2
                                       , dba_lock.blocking_others blocking_others
                 from
                                dba_lock  dba_lock
                           join v$session lock_session on dba_lock.session_id = lock_session.sid
                      left join (
                                  select
                                         distinct
                                                  v$sql.sql_id
                                                , v$sql.sql_text
                                    from
                                         v$sql v$sql
                                ) prev_sql
                             on   lock_session.prev_sql_id = prev_sql.sql_id
                      left join (
                                  select
                                         distinct
                                                  v$sql.sql_id
                                                , v$sql.sql_text
                                    from
                                         v$sql v$sql
                                ) curr_sql
                             on   lock_session.sql_id = curr_sql.sql_id

             )
             ,
                 hold_lock
             as
             (
                  select
                         null                                    holding_session_id
                       , holding_lock.session_id                 session_id
                       , holding_lock.username                   username
                       , null                                    lock_type
                       , null                                    mode_held
                       , null                                    mode_requested
                       , null                                    lock_id1
                       , null                                    lock_id2
                       , holding_lock.prev_sql_text              prev_sql_text
                       , holding_lock.curr_sql_text              curr_sql_text
                    from
                         lock_data holding_lock
                   where
                         holding_lock.blocking_others = 'Blocking'
                     and holding_lock.mode_held       not in ( 'None' , 'Null' )
                     and holding_lock.session_id      not in (
                                                               select
                                                                      il_holding_lock.session_id
                                                                 from
                                                                      lock_data il_holding_lock
                                                                where
                                                                      il_holding_lock.mode_requested !=  'None'
                                                             )

               union all

                  select
                         holding_lock.session_id                 holding_session_id
                       , waiting_lock.session_id                 session_id
                       , waiting_lock.username                   username
                       , waiting_lock.lock_type                  lock_type
                       , waiting_lock.mode_held                  mode_held
                       , waiting_lock.mode_requested             mode_requested
                       , waiting_lock.lock_id1                   lock_id1
                       , waiting_lock.lock_id2                   lock_id2
                       , waiting_lock.prev_sql_text              prev_sql_text
                       , waiting_lock.curr_sql_text              curr_sql_text
                    from
                              lock_data holding_lock
                         join lock_data waiting_lock    on (
                                                                 holding_lock.lock_type = waiting_lock.lock_type
                                                             and holding_lock.lock_id1  = waiting_lock.lock_id1
                                                             and holding_lock.lock_id2  = waiting_lock.lock_id2
                                                           )
                   where
                         holding_lock.blocking_others = 'Blocking'
                     and holding_lock.mode_held       not in ( 'None' , 'Null' )
                     and waiting_lock.mode_requested  not in ( 'None' )
             )
                      select
                             level                                            hlevel
                           , d.session_id                                     session_id
                           , d.username                                       username
                           , d.lock_type                                      lock_type
                           , d.mode_held                                      mode_held
                           , d.mode_requested                                 mode_requested
                           , d.lock_id1                                       lock_id1
                           , d.lock_id2                                       lock_id2
                           , d.prev_sql_text                                  prev_sql_text
                           , d.curr_sql_text                                  curr_sql_text
                        from
                             hold_lock d
                  start with
                             d.holding_session_id is null
                  connect by
                             prior d.session_id = d.holding_session_id
              order siblings
                             by d.session_id asc
                           ;

        return v_data_set;

    end fn_get_report;

end pk_session_lock_report;
/

sho err