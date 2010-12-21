CREATE OR REPLACE PACKAGE BODY app_runstats.pkRunstats
AS

  g_start NUMBER;
  g_run1  NUMBER;
  g_run2  NUMBER;

  /* ****************************************************************
   * Name: prStart
   * **************************************************************** */

  PROCEDURE prStart
  IS 
  BEGIN

    DELETE FROM run_stats;

    INSERT 
      INTO 
           run_stats 
    SELECT 
           'before'
         , stats.* 
      FROM 
           app_runstats.v_run_stats stats;
        
    g_start := DBMS_UTILITY.get_time;

  END prStart;

  /* ****************************************************************
   * Name: prMiddle
   * **************************************************************** */

  PROCEDURE prMiddle
  IS
  BEGIN
   
    g_run1 := ( DBMS_UTILITY.get_time - g_start );
 
    INSERT 
      INTO 
           run_stats 
    SELECT 
           'after 1'
         , stats.* 
      FROM 
           app_runstats.v_run_stats stats
         ;
    
    g_start := DBMS_UTILITY.get_time;

  END prMiddle; 

  /* ****************************************************************
   * Name: prStop
   * **************************************************************** */

  PROCEDURE prStop
  (
    p_difference_threshold IN NUMBER DEFAULT 0
  )
  IS
  BEGIN
    
    g_run2 := ( DBMS_UTILITY.get_time - g_start );

    DBMS_OUTPUT.put_line ( 'Run1 ran in ' || g_run1 || ' hsecs' );
    DBMS_OUTPUT.put_line ( 'Run2 ran in ' || g_run2 || ' hsecs' );

    IF ( g_run2 <> 0 ) THEN
    
      DBMS_OUTPUT.put_line ( 'run 1 ran in ' || ROUND ( g_run1 / g_run2 * 100 , 2 ) ||  '% of the time' );

    END IF;
    
    DBMS_OUTPUT.put_line ( CHR ( 9 ) );

    INSERT 
      INTO 
           run_stats 
    SELECT 
           'after 2'
         , stats.* 
      FROM 
           app_runstats.v_run_stats stats
         ;

    DBMS_OUTPUT.put_line ( RPAD ( 'Name', 30 ) || LPAD ( 'Run1' , 12 ) || LPAD ( 'Run2', 12 ) || LPAD ( 'Diff' , 12 ) );

    FOR x IN ( 
                 SELECT 
                        RPAD    ( a.name , 30 ) 
                     || TO_CHAR ( b.value - a.value , '999,999,999' ) 
                     || TO_CHAR ( c.value - b.value , '999,999,999' ) 
                     || TO_CHAR ( ( ( c.value - b.value ) - ( b.value - a.value ) ) , '999,999,999' ) data
                   FROM 
                        app_runstats.run_stats a
                      , app_runstats.run_stats b
                      , app_runstats.run_stats c
                  WHERE 
                        a.name  = b.name
                    AND b.name  = c.name
                    AND a.runid = 'before'
                    AND b.runid = 'after 1'
                    AND c.runid = 'after 2'
                    AND ABS ( ( c.value - b.value ) - ( b.value - a.value ) )  > p_difference_threshold
               ORDER BY 
                        ABS ( ( c.value - b.value ) - ( b.value - a.value ) )
             ) 
    LOOP
        DBMS_OUTPUT.put_line ( x.data );
    END LOOP;

    DBMS_OUTPUT.put_line ( CHR ( 9 ) );
    DBMS_OUTPUT.put_line ( 'Run1 latches total versus runs -- difference and pct' );
    DBMS_OUTPUT.put_line ( LPAD ( 'Run1' , 12 ) || LPAD ( 'Run2' , 12 ) || LPAD ( 'Diff' , 12 ) || LPAD ( 'Pct' , 10 ) );

    FOR x IN ( 
               SELECT 
                      TO_CHAR ( run1 , '999,999,999' ) 
                   || TO_CHAR ( run2 , '999,999,999' ) 
                   || TO_CHAR ( diff , '999,999,999' ) 
                   || TO_CHAR ( ROUND ( run1 / DECODE ( run2 , 0 , TO_NUMBER ( 0 ) , run2 ) * 100 , 2 ) , '99,999.99' ) || '%' data
                 FROM ( 
                        SELECT 
                               SUM ( b.value - a.value ) run1
                             , SUM ( c.value - b.value ) run2
                             , SUM ( ( c.value - b.value ) - ( b.value - a.value ) ) diff
                          FROM 
                               app_runstats.run_stats a
                             , app_runstats.run_stats b
                             , app_runstats.run_stats c
                         WHERE 
                               a.name  = b.name
                           AND b.name  = c.name
                           AND a.runid = 'before'
                           AND b.runid = 'after 1'
                           AND c.runid = 'after 2'
                           AND a.name LIKE 'LATCH%'
                      )
    ) LOOP
        DBMS_OUTPUT.put_line ( x.data );
    END LOOP;

  END prStop;

END pkRunstats;
/

SHO ERR