prompt creating view v_proxy_app_user

create or replace view app_data.v_proxy_app_user
as
select 
       vaur.aur_id
     , vaur.aur_username
     , vaur.aur_is_account_enabled
     , vaur.aur_is_tracing_enabled
     , vaur.aur_created_date
  from 
            app_data.v_app_user     vaur
       join app_data.proxy_app_user paur on vaur.aur_id = paur.aur_id
  with
       read only
/