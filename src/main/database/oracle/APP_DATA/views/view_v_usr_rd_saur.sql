prompt creating view app_data.v_usr_rd_saur

create or replace view app_data.v_usr_rd_saur
as
select
       aur.aur_id       aur_id
     , aur.aur_username aur_username
  from
            app_data.app_user          aur
       join app_data.standard_app_user saur on aur.aur_id = saur.aur_id
  with
       read only
/