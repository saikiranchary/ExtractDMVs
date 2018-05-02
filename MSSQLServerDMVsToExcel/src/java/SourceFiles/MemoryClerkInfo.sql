SELECT type, name, pages_kb, virtual_memory_reserved_kb, virtual_memory_committed_kb, awe_allocated_kb, 
shared_memory_reserved_kb, shared_memory_committed_kb, page_size_in_bytes
FROM sys.dm_os_memory_clerks where pages_kb>0
ORDER BY pages_kb DESC