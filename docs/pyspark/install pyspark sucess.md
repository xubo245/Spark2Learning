
##1. 安装步骤：
###download
	
	wget https://www.python.org/ftp/python/2.7.14/Python-2.7.14.tar.xz
	xz -d Python-2.7.14.tar.xz 
	tar -xvf Python-2.7.14.tar 

###install
	 ./configure --prefix=/usr/local/python2.7.14
	
	make
	make install
	
	
	ln -f -s /usr/local/python2.7.14/bin/python2.7 /usr/local/bin/python2.7.14

##2. /etc/profile
	export SPARK_HOME=/xubo/git/spark/
	export PYTHONPATH=$SPARK_HOME/python/:$SPARK_HOME/python/lib/py4j-0.10.6-src.zip:/xubo/git/spark/python/lib/pyspark.zip:$PYTHONPATH
	export PYSPARK_PYTHON=/usr/local/python3.5
	export PYSPARK_DRIVER_PYTHON=/usr/local/python3.5
	#os.environ["PYSPARK_PYTHON"]=PYSPARK_PYTHON
	#os.environ["PYSPARK_DRIVER_PYTHON"]=PYSPARK_DRIVER_PYTHON
##3.
IDEA run configuration:
	
	PYTHONPATH   /xubo/git/spark/python/:/xubo/git/spark/python/lib/py4j-0.10.6-src.zip:/xubo/git/spark/python/lib/pyspark.zip:
	
	SPARK_HOME    /xubo/git/spark/

安装运行成功：

	
	/xubo/tools/python/python2.7.14/bin/python /xubo/git/spark/examples/src/main/python/pi.py
	18/01/21 22:10:07 WARN Utils: Your hostname, hadoop resolves to a loopback address: 127.0.0.1; using 10.229.51.168 instead (on interface eth0)
	18/01/21 22:10:07 WARN Utils: Set SPARK_LOCAL_IP if you need to bind to another address
	18/01/21 22:10:08 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
	Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
	Setting default log level to "WARN".
	To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
	18/01/21 22:10:08 WARN Utils: Service 'SparkUI' could not bind on port 4040. Attempting port 4041.
	18/01/21 22:10:10 WARN TaskSetManager: Stage 0 contains a task of very large size (371 KB). The maximum recommended task size is 100 KB.
	Pi is roughly 3.136280
	
	Process finished with exit code 0
	





其他尝试：
python3.5  没有成功

	 ./configure --prefix=/usr/local/python3.5
	
	ln -f -s /usr/local/python3/bin/python3.5 /usr/local/python3.5


参考：

【1】 https://www.cnblogs.com/feng18/p/5854912.html
