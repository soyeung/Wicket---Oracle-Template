define default_tablespace       = TS_TEMPLATE
define default_temp_tablespace  = TTS_TEMPLATE
define default_undo_tablespace  = UTS_TEMPLATE

define path_to_dft_tablespace       = 'c:/oracle_tablespace/ts_template.dbf'
define path_to_dft_temp_tablespace  = 'c:/oracle_tablespace/tts_template.dbf'
define path_to_dft_undo_tablespace  = 'c:/oracle_tablespace/uts_template.dbf'

drop tablespace &default_tablespace including contents and datafiles cascade constraints
/

create smallfile tablespace &default_tablespace
datafile '&path_to_dft_tablespace'
size 50m autoextend on
logging
extent management local
segment space management auto
flashback on
/

drop tablespace &default_temp_tablespace including contents and datafiles cascade constraints
/

create smallfile temporary tablespace &default_temp_tablespace
tempfile '&path_to_dft_temp_tablespace'
size 50m autoextend on
extent management local
/

create undo tablespace &default_undo_tablespace
datafile '&path_to_dft_undo_tablespace'
size 50m reuse autoextend on 
extent management local
/

alter database default tablespace &default_tablespace
/

alter database default temporary tablespace &default_temp_tablespace
/

alter system set undo_tablespace = &default_undo_tablespace
/