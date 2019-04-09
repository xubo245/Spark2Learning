	/usr/bin/python3.5 /xubo/git/spark/examples/src/main/python/pi.py
	Traceback (most recent call last):
	  File "/xubo/git/spark/examples/src/main/python/pi.py", line 33, in <module>
	    .appName("PythonPi")\
	  File "/xubo/git/spark/python/pyspark/sql/session.py", line 174, in getOrCreate
	    sc = SparkContext.getOrCreate(sparkConf)
	  File "/xubo/git/spark/python/pyspark/context.py", line 331, in getOrCreate
	    SparkContext(conf=conf or SparkConf())
	  File "/xubo/git/spark/python/pyspark/context.py", line 115, in __init__
	    SparkContext._ensure_initialized(self, gateway=gateway, conf=conf)
	  File "/xubo/git/spark/python/pyspark/context.py", line 280, in _ensure_initialized
	    SparkContext._gateway = gateway or launch_gateway(conf)
	  File "/xubo/git/spark/python/pyspark/java_gateway.py", line 77, in launch_gateway
	    proc = Popen(command, stdin=PIPE, preexec_fn=preexec_func, env=env)
	  File "/usr/local/python3/lib/python3.5/subprocess.py", line 950, in __init__
	    restore_signals, start_new_session)
	  File "/usr/local/python3/lib/python3.5/subprocess.py", line 1540, in _execute_child
	    raise child_exception_type(errno_num, err_msg)
	FileNotFoundError: [Errno 2] No such file or directory: '/xubo/git/spark:/./bin/spark-submit'
	
	Process finished with exit code 1
