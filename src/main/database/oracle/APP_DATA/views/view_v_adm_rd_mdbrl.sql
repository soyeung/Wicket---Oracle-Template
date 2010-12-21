prompt creating view app_data.v_adm_rd_mdbrl

create or replace view app_data.v_adm_rd_mdbrl
as 
select 
       dbrl.dbrl_id               dbrl_id
     , dbrl.dbrl_code             dbrl_code
     , dbrl.dbrl_name             dbrl_name
     , dbrl.dbrl_order            dbrl_order
     , dbrl.dbrl_created_by       dbrl_created_by
     , dbrl.dbrl_created_date     dbrl_created_date
     , dbrl.dbrl_updated_date     dbrl_updated_date
     , case 
         when 
           mdbrl.dbrl_id is null 
         then 
           'N'
         else 
           'Y' 
       end                        dbrl_is_included
  from 
                 app_data.db_role            dbrl
       left join app_data.manageable_db_role mdbrl on dbrl.dbrl_id = mdbrl.dbrl_id
/