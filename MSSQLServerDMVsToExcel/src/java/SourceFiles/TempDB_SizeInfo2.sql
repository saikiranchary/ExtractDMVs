SELECT (SUM(unallocated_extent_page_count)*1.0/128) AS free_space_in_MB,
(SUM(version_store_reserved_page_count + 
    user_object_reserved_page_count +internal_object_reserved_page_count + 
    mixed_extent_page_count)*1.0/128) AS used_space_in_MB
FROM sys.dm_db_file_space_usage;