-- list Table Info: Name, row_count, data_size,index_size
SELECT name = object_schema_name(object_id) + '.' + object_name(object_id), row_count, data_size = 8*sum(case
when index_id < 2
then in_row_data_page_count + lob_used_page_count + row_overflow_used_page_count
else lob_used_page_count + row_overflow_used_page_count
end) , index_size = 8*(sum(used_page_count) - sum(case
when index_id < 2 then in_row_data_page_count + lob_used_page_count + row_overflow_used_page_count
else lob_used_page_count + row_overflow_used_page_count
end))
FROM
sys.dm_db_partition_stats
where object_schema_name(object_id) != 'sys' GROUP BY object_id, row_count ORDER BY name asc, index_size DESC