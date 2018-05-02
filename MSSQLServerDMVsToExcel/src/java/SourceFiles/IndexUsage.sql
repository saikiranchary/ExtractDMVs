select
database_id,object_id,index_id,user_seeks,user_scans,user_lookups,user_updates,CONVERT(VARCHAR, last_user_seek, 120) AS last_user_seek,CONVERT(VARCHAR, last_user_scan, 120) AS last_user_scan,CONVERT(VARCHAR, last_user_lookup, 120) AS last_user_lookup,CONVERT(VARCHAR, last_user_update, 120) AS last_user_update,system_seeks,system_scans,system_lookups,system_updates,CONVERT(VARCHAR, last_system_seek, 120) AS last_system_seek,CONVERT(VARCHAR, last_system_scan, 120) AS last_system_scan,CONVERT(VARCHAR, last_system_lookup, 120) AS last_system_lookup,CONVERT(VARCHAR, last_system_update, 120) AS last_system_update
from [sys].[dm_db_index_usage_stats]