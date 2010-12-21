CREATE GLOBAL TEMPORARY TABLE app_runstats.run_stats
( 
  runid VARCHAR2(15)
, name  VARCHAR2(80)
, value INT 
)
ON COMMIT PRESERVE ROWS
/