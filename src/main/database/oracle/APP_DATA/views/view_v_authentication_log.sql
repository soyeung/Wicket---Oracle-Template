prompt creating view v_authentication_log

create or replace view app_data.v_authentication_log
as
select
       lgn.lgn_id
     , aur.aur_id
     , aur.aur_username
     , lgn.lgn_time
     , lgn.lgn_ip_address
     , lgn.lgn_http_session
  from
            app_data.authentication_log lgn 
       join app_data.app_user           aur on lgn.aur_id = aur.aur_id
/