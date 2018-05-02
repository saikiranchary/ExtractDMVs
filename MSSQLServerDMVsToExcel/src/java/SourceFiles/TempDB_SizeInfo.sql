--use the catalog view sys.database_files and the dynamic management view sys.dm_db_file_space_usage to return size information for the tempdb database.
-- The view sys.dm_db_file_space_usage is applicable only to tempdb.
SELECT 
name AS FileName, 
size*1.0/128 AS FileSizeinMB,
'MaximumSizeinMB' = 
    CASE max_size 
       WHEN 0 THEN 'No growth is allowed.'
       WHEN -1 THEN 'Autogrowth is on.'
       WHEN 268435456 
          THEN 'Log file will grow to a maximum size of 2 TB.'
       ELSE CAST (max_size*1.0/128 AS nvarchar(30))
    END,
growth AS 'GrowthValue',
'GrowthIncrement' = 
    CASE 
       WHEN growth = 0 THEN 'File size is fixed and will not grow.'
       WHEN growth > 0 AND is_percent_growth = 0 
          THEN 'Growth value is in units of 8-KB pages.'
       ELSE 'Growth value is a percentage.'
    END
FROM tempdb.sys.database_files;
