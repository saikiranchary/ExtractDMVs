SELECT wait_type, wait_time_ms, waiting_tasks_count, max_wait_time_ms, signal_wait_time_ms,
wait_time_ms/waiting_tasks_count AS AvgWaitTimems
FROM sys.dm_os_wait_stats where waiting_tasks_count>0 order by wait_time_ms desc;