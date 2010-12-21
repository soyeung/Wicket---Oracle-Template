CREATE OR REPLACE VIEW app_runstats.v_run_stats
AS 
   SELECT 
          'STAT...' || a.name name, b.value
     FROM 
          v$statname a
        , v$mystat   b
    WHERE 
          a.statistic# = b.statistic#
UNION ALL
   SELECT 
          'LATCH.' || name
       ,  gets
     FROM 
          v$latch
UNION ALL
   SELECT 
          'STAT...Elapsed Time'
        , hsecs 
     FROM 
          v$timer
        ;