
SELECT top 100 percent dbschemas.[name] as 'Schema', dbtables.[name] as 'Table',
dbindexes.[name] as 'Index', dbindexes.index_id as IdxID,
cast(indexstats.avg_fragmentation_in_percent as decimal(6,2)) as Prcent,
indexstats.page_count, replace(index_type_desc,' index','') as IndxType,
fragment_count as Fragments, index_depth as IdxDeep,
cast(avg_fragment_size_in_pages as decimal(10,2)) as AvgFragSize
FROM sys.dm_db_index_physical_stats (DB_ID(), NULL, NULL, NULL, NULL) AS indexstats
INNER JOIN sys.tables dbtables on dbtables.[object_id] = indexstats.[object_id]
INNER JOIN sys.schemas dbschemas on dbtables.[schema_id] = dbschemas.[schema_id]
INNER JOIN sys.indexes AS dbindexes ON dbindexes.[object_id] = indexstats.[object_id]
AND indexstats.index_id = dbindexes.index_id
WHERE indexstats.database_id = DB_ID()
and cast(indexstats.avg_fragmentation_in_percent as decimal(6,2))>0
ORDER BY indexstats.avg_fragmentation_in_percent desc